/*
 * Copyright 2019-2020 the original author or authors.
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

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class ReportingServiceTest {

//    @Rule
//    public RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

//    private Preferences preferences = mock(Preferences.class);
//
    private ReportingService reportingService = new ReportingService();

//    @After
//    public void after() {
//        System.clearProperty(ReportingService.LENGTH_OF_YEAR);
//    }
//
    @Test
    public void calculateReturnOnInvestmentYTD_noTransactionAndPosition() {
        System.setProperty("LENGTH_OF_YEAR", "360");

//        when(preferences.getInteger(ReportingService.LENGTH_OF_YEAR)).thenReturn(360);

        Position current = new Position()
                .securityPositions(Collections.emptyList())
                .cash(BigDecimal.ZERO);
        BigDecimal roi = reportingService.calculateReturnOnInvestmentYTD(current, Collections.emptyList());
        assertThat(roi).isEqualTo("0.00");
    }

//    @Test
//    public void calculateReturnOnInvestmentYTD_cashAdded() {
//        Position current = new Position()
//                .securityPositions(Collections.emptyList())
//                .cash(BigDecimal.valueOf(200L));
//
//        Collection<Transaction> transactions = Collections.singleton(
//                new Transaction()
//                        .cash(BigDecimal.valueOf(100L))
//                        .type(TransactionType.DEPOSIT)
//                        .date(LocalDate.now().minusDays(100)));
//
//        when(preferences.getInteger(ReportingService.LENGTH_OF_YEAR)).thenReturn(360);
//
//        BigDecimal roi = reportingService.calculateReturnOnInvestmentYTD(current, transactions);
//        assertThat(roi).isEqualTo("120.00"); // (200 - 100) / 100 * 100 * 360 / LocalDate.now().getDaysOfYear()
//    }
}
