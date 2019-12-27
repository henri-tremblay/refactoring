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
