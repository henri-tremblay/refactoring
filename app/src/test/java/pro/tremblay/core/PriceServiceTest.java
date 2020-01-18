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
