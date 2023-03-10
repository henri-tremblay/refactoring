/*
 * Copyright 2019-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pro.tremblay.core;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Service reporting useful information on a position.
 */
@ThreadSafe
public class ReportingService {

    public static final String LENGTH_OF_YEAR = "LENGTH_OF_YEAR";

    private final Preferences preferences;
    private final Clock clock;
    private final PriceService priceService;

    public ReportingService(@Nonnull Preferences preferences, @Nonnull Clock clock) {
        this(preferences, clock, new PriceService(clock));
    }

    public ReportingService(@Nonnull Preferences preferences, @Nonnull Clock clock, @Nonnull PriceService priceService) {
        this.preferences = preferences;
        this.clock = clock;
        this.priceService = priceService;
    }

    /**
     * Calculate the annualized return on investment since the beginning of the year (Year To Date). We use the simplest method
     * possible. We have the following
     * <ul>
     *     <li>Current value: {@code cash + security_quantity * current_security_price} based on the current position</li>
     *     <li>Initial value: {@code cash + security_quantity * start_of_year_security_price} based on the start of year position</li>
     *     <li>Absolute ROI: {@code (current_value - initial_value) / initial_value * 100}</li>
     *     <li>Year length: As set in the preferences</li>
     *     <li>Annualized ROI : {@code absolute_roi * year_length / days_since_beginning_of_year} </li>
     * </ul>
     *
     * Then formula is {@code (current_value - initial_value) / initial_value}.
     *
     * @param current the current position of today, won't be modified by this call
     * @param transactions all transactions on this position, they are not sorted and might be before the beginning of the year
     * @return annualized return on investment since beginning of the year
     */
    @Nonnull
    public Percentage calculateReturnOnInvestmentYTD(@Nonnull Position current, @Nonnull Collection<Transaction> transactions) {
        LocalDate now = LocalDate.now(clock);
        LocalDate beginningOfYear = now.withDayOfYear(1);

        Position working = current.copy();

        List<Transaction> orderedTransaction = antichronologicalOrderedTransactions(transactions);

        rewindTransactions(beginningOfYear, now, working, orderedTransaction);

        Amount initialValue = calculatePositionValue(beginningOfYear, working);

        if(initialValue.isZero()) {
            return Percentage.zero();
        }

        Amount currentValue = calculatePositionValue(now, current);

        return calculateRoi(now, initialValue, currentValue);
    }

    private Percentage calculateRoi(LocalDate now, Amount initialValue, Amount currentValue) {
        Percentage roi = currentValue.asRatioOf(initialValue);

        int yearLength = preferences.getInteger(LENGTH_OF_YEAR);

        return roi.scale(now.getDayOfYear(), yearLength);
    }

    private Amount calculatePositionValue(LocalDate beginningOfYear, Position position) {
        Amount initialCashValue = position.cash();
        Amount initialSecPosValue = position.securityPositionValue(beginningOfYear, priceService);
        return initialCashValue.add(initialSecPosValue);
    }

    private void rewindTransactions(LocalDate beginningOfYear, LocalDate today, Position working, List<Transaction> orderedTransaction) {
        int transactionIndex = 0;
        while (!today.isBefore(beginningOfYear)) {
            if (transactionIndex >= orderedTransaction.size())  {
                break;
            }
            Transaction transaction = orderedTransaction.get(transactionIndex);
            while (transaction.date().equals(today)) {
                // It's a transaction on the date, process it
                transaction.revert(working);
                transactionIndex++;
                if (transactionIndex >= orderedTransaction.size())  {
                    break;
                }
                transaction = orderedTransaction.get(transactionIndex);
            }

            today = today.minusDays(1);
        }
    }

    private List<Transaction> antichronologicalOrderedTransactions(@Nonnull Collection<Transaction> transactions) {
        return transactions.stream()
                .sorted(Comparator.comparing((Transaction t) -> t.date()).reversed())
                .toList();
    }

}
