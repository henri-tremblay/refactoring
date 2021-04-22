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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static pro.tremblay.core.BigDecimalUtil.bd;

public class ReportingServiceTest {

    private ReportingService reportingService = new ReportingService();

    private final Position current = new Position()
            .cash(BigDecimal.ZERO)
            .securityPositions(new ArrayList<>());

    @Before
    public void setup() {
        System.setProperty("LENGTH_OF_YEAR", "360");
    }

    @After
    public void after() {
        System.clearProperty("LENGTH_OF_YEAR");
    }

    @Test
    public void calculateReturnOnInvestmentYTD_noTransactionAndPosition() {
        BigDecimal roi = reportingService.calculateReturnOnInvestmentYTD(current, Collections.emptyList());
        assertThat(roi).isEqualTo("0.00");
    }

    @Test
    public void calculateReturnOnInvestmentYTD_cashAdded() {
        current.cash(bd(200));

        Collection<Transaction> transactions = Collections.singleton(
                new Transaction()
                        .cash(bd(100))
                        .type(TransactionType.DEPOSIT)
                        .date(LocalDate.now().minusDays(10)));

        BigDecimal roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);

        // Current cash value = 200$, Current security value = 0$
        // Initial cash value = 100$, Initial cash value = 0$
        // Year length = 360
        BigDecimal actual = BigDecimal.valueOf((200.0 - 100.0) / 100.0 * 100.0 * 360.0 / LocalDate.now().getDayOfYear())
            .setScale(2, RoundingMode.HALF_UP);
        assertThat(roi).isEqualTo(actual);
    }

    @Test
    public void calculateReturnOnInvestmentYTD_secBought() {
        current.getSecurityPositions().add(new SecurityPosition()
            .security(Security.GOOGL)
            .quantity(bd(50)));

        BigDecimal priceAtTransaction = PriceService.getPrice(LocalDate.now().minusDays(10), Security.GOOGL);

        Transaction transaction = new Transaction()
            .security(Security.GOOGL)
            .quantity(bd(50))
            .cash(priceAtTransaction.multiply(bd(50)))
            .type(TransactionType.BUY)
            .date(LocalDate.now().minusDays(10));
        Collection<Transaction> transactions = Collections.singleton(transaction);

        BigDecimal roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);

        BigDecimal priceNow = PriceService.getPrice(LocalDate.now(), Security.GOOGL);

        BigDecimal initialCashValue = transaction.getCash(); // initialSecValue = 0
        BigDecimal currentSecValue = priceNow.multiply(bd(50)); // currentCashValue = 0

        BigDecimal actual = currentSecValue.subtract(initialCashValue)
            .divide(initialCashValue, 10, RoundingMode.HALF_UP)
            .multiply(bd(100))
            .multiply(bd(360))
            .divide(bd(LocalDate.now().getDayOfYear()), 2, RoundingMode.HALF_UP);
        assertThat(roi).isEqualTo(actual);
    }

    @Test
    public void calculateReturnOnInvestmentYTD_twoCashTransactions() {
        current.cash(bd(200));

        Collection<Transaction> transactions = Arrays.asList(
            new Transaction()
                .cash(bd(100))
                .type(TransactionType.DEPOSIT)
                .date(LocalDate.now().minusDays(10)),
            new Transaction()
                .cash(bd(50))
                .type(TransactionType.DEPOSIT)
                .date(LocalDate.now().minusDays(9)));

        BigDecimal roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);

        // Current cash value = 200$, Current security value = 0$
        // Initial cash value = 50$, Initial cash value = 0$
        // Year length = 360
        BigDecimal actual = BigDecimal.valueOf((200.0 - 50.0) / 50.0 * 100.0 * 360.0 / LocalDate.now().getDayOfYear())
            .setScale(2, RoundingMode.HALF_UP);
        assertThat(roi).isEqualTo(actual);
    }

    @Test
    public void calculateReturnOnInvestmentYTD_twoCashTransactionsOnTheSameDay() {
        current.cash(bd(200));

        Collection<Transaction> transactions = Arrays.asList(
            new Transaction()
                .cash(bd(100))
                .type(TransactionType.DEPOSIT)
                .date(LocalDate.now().minusDays(10)),
            new Transaction()
                .cash(bd(50))
                .type(TransactionType.DEPOSIT)
                .date(LocalDate.now().minusDays(10)));

        BigDecimal roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);

        // Current cash value = 200$, Current security value = 0$
        // Initial cash value = 50$, Initial cash value = 0$
        // Year length = 360
        BigDecimal actual = BigDecimal.valueOf((200.0 - 50.0) / 50.0 * 100.0 * 360.0 / LocalDate.now().getDayOfYear())
            .setScale(2, RoundingMode.HALF_UP);
        assertThat(roi).isEqualTo(actual);
    }
}
