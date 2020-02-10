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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service reporting useful information on a position.
 */
@ThreadSafe
public class ReportingService {

    public static final String LENGTH_OF_YEAR = "LENGTH_OF_YEAR";

    private final Preferences preferences;
    private final TimeSource timeSource;
    private final PriceService priceService;

    public ReportingService(@Nonnull Preferences preferences, @Nonnull TimeSource timeSource) {
        this(preferences, timeSource, new PriceService(timeSource));
    }

    public ReportingService(@Nonnull Preferences preferences, @Nonnull TimeSource timeSource, @Nonnull PriceService priceService) {
        this.preferences = preferences;
        this.timeSource = timeSource;
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
    public BigDecimal calculateReturnOnInvestmentYTD(@Nonnull Position current, @Nonnull Collection<Transaction> transactions) {
        LocalDate now = timeSource.today();
        LocalDate beginningOfYear = now.withDayOfYear(1);

        Position working = current.copy();

        List<Transaction> orderedTransaction = antichronologicalOrderedTransactions(transactions);

        rewindTransactions(beginningOfYear, now, working, orderedTransaction);

        Amount initialValue = calculatePositionValue(beginningOfYear, working);

        if(initialValue.isZero()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY);
        }

        Amount currentValue = calculatePositionValue(now, current);

        return calculateRoi(now, initialValue, currentValue);
    }

    private BigDecimal calculateRoi(LocalDate now, Amount initialValue, Amount currentValue) {
        BigDecimal roi = Amount.substract(currentValue, initialValue)
                    .toBigDecimal()
                    .divide(initialValue.toBigDecimal(), 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100L));

        int yearLength = preferences.getInteger(LENGTH_OF_YEAR);

        return roi.multiply(BigDecimal.valueOf(yearLength))
            .divide(BigDecimal.valueOf(now.getDayOfYear()), 2, RoundingMode.HALF_UP);
    }

    private Amount calculatePositionValue(LocalDate beginningOfYear, Position working) {
        Amount initialCashValue = working.getCash();
        Amount initialSecPosValue = working.securityPositionValue(beginningOfYear, priceService);
        return Amount.add(initialCashValue, initialSecPosValue);
    }

    private void rewindTransactions(LocalDate beginningOfYear, LocalDate today, Position working, List<Transaction> orderedTransaction) {
        int transactionIndex = 0;
        while (!today.isBefore(beginningOfYear)) {
            if (transactionIndex >= orderedTransaction.size())  {
                break;
            }
            Transaction transaction = orderedTransaction.get(transactionIndex);
            while (transaction.getDate().equals(today)) {
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
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());
    }

}
