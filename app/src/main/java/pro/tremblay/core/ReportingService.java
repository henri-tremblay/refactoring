/*
 * Copyright 2019-2021 the original author or authors.
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
        LocalDate now = LocalDate.now();
        LocalDate beginningOfYear = now.withDayOfYear(1);

        Position working = new Position()
                .cash(current.getCash());

        List<SecurityPosition> positions = current.getSecurityPositions().stream()
                .map(securityPosition -> new SecurityPosition()
                        .quantity(securityPosition.getQuantity())
                        .security(securityPosition.getSecurity())
                )
                .collect(Collectors.toList());
        working.securityPositions(positions);

        List<Transaction> orderedTransaction = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());

        LocalDate today = now;
        int transactionIndex = 0;
        while (!today.isBefore(beginningOfYear)) {
            if (transactionIndex >= orderedTransaction.size())  {
                break;
            }
            Transaction transaction = orderedTransaction.get(transactionIndex);
            while (transaction.getDate().equals(today)) {
                // It's a transaction on the date, process it
                revert(working, transaction);
                transactionIndex++;
                if (transactionIndex >= orderedTransaction.size())  {
                    break;
                }
                transaction = orderedTransaction.get(transactionIndex);
            }

            today = today.minusDays(1);
        }

        BigDecimal initialCashValue = working.getCash();
        BigDecimal currentCashValue = current.getCash();

        BigDecimal initialSecPosValue = working.getSecurityPositions()
                .stream()
                .map(securityPosition -> securityPosition.getQuantity().multiply(PriceService.getPrice(beginningOfYear, securityPosition.getSecurity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal currentSecPosValue = current.getSecurityPositions()
                .stream()
                .map(securityPosition -> securityPosition.getQuantity().multiply(PriceService.getPrice(now, securityPosition.getSecurity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal initialValue = initialCashValue.add(initialSecPosValue);

        BigDecimal roi;
        if(initialValue.signum() == 0) {
            roi = BigDecimal.ZERO.setScale(10, RoundingMode.UNNECESSARY);
        }
        else {
            roi = currentCashValue.add(currentSecPosValue).subtract(initialValue)
                    .divide(initialValue, 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100L));
        }

        int yearLength = Preferences.preferences().getInteger("LENGTH_OF_YEAR");

        roi = roi.multiply(BigDecimal.valueOf(yearLength)).divide(BigDecimal.valueOf(now.getDayOfYear()), 2, RoundingMode.HALF_UP);

        return roi;
    }

    private void revert(Position current, Transaction transaction) {
        switch (transaction.getType()) {
            case BUY: {
                current.cash(current.getCash().add(transaction.getCash()));
                SecurityPosition pos = current.getSecurityPositions().stream()
                        .filter(sec -> sec.getSecurity().equals(transaction.getSecurity()))
                        .findAny()
                        .orElse(null);
                if (pos == null) {
                    pos = new SecurityPosition().quantity(BigDecimal.ZERO).security(transaction.getSecurity());
                    current.getSecurityPositions().add(pos);
                }
                pos.quantity(pos.getQuantity().subtract(transaction.getQuantity()));
                break;
            }
            case SELL:
                current.cash(current.getCash().subtract(transaction.getCash()));
                SecurityPosition pos = current.getSecurityPositions().stream()
                        .filter(sec -> sec.getSecurity().equals(transaction.getSecurity()))
                        .findAny()
                        .orElse(null);
                if (pos == null) {
                    pos = new SecurityPosition().quantity(BigDecimal.ZERO).security(transaction.getSecurity());
                    current.getSecurityPositions().add(pos);
                }
                pos.quantity(pos.getQuantity().add(transaction.getQuantity()));
                break;
            case DEPOSIT:
                current.cash(current.getCash().subtract(transaction.getCash()));
                break;
            case WITHDRAWAL:
                current.cash(current.getCash().add(transaction.getCash()));
                break;
        }
    }

}
