package pro.tremblay.core;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

public class PriceServiceTest {

//    private StrangeTimeSource timeSource = new StrangeTimeSource()
//            .now(Instant.now());

//    @Test
//    public void testGetKey() {
//        String key = PriceService.getKey(Security.IBM, LocalDate.of(2019, 2, 4));
//        assertThat(key).isEqualTo("2019-02-04#" + Security.IBM);
//    }

    @Test
    public void getPrice_weShouldGetANewPriceEverySecond() throws Exception {
        BigDecimal first = PriceService.getRealTimePrice(Security.GOOGL);
        TimeUnit.SECONDS.sleep(2); // wait 2 seconds to make sure it changes
        BigDecimal second = PriceService.getRealTimePrice(Security.GOOGL);
        assertThat(second).isNotEqualByComparingTo(first);
    }
}
