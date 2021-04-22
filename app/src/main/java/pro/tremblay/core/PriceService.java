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
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service returning security prices. This is actually a fake implementation using randomly generated prices.
 */
@ThreadSafe
public class PriceService {

    private static final ConcurrentMap<String, BigDecimal> prices = new ConcurrentHashMap<>();

    private static final Random random = new Random();

    static {
        // Randomly generated price since the beginning of the year
        LocalDate now = LocalDate.now();
        for (Security security : Security.values()) {
            LocalDate start = now.withDayOfYear(1);
            BigDecimal price = BigDecimal.valueOf(100 + random.nextInt(200));
            while(!start.isAfter(now)) {
                BigDecimal tick = BigDecimal.valueOf(random.nextGaussian()).setScale(2, RoundingMode.HALF_UP);
                prices.put(getKey(security, start), price.add(tick));
                start = start.plusDays(1);
            }
        }
    }

    private static String getKey(Security security, LocalDate date) {
        return date + "#" + security;
    }

    /**
     * Returns the price at a given date for a security. The implementation generates random prices to
     * simulate a real price source. During your refactoring, please keep the current price generation concept.
     *
     * @param date date on which we want the price
     * @param security security for which we want a price
     * @throws IllegalArgumentException if no price is found at this date
     * @return the price of the security at a given date
     */
    @Nonnull
    public static BigDecimal getPrice(@Nonnull LocalDate date, @Nonnull Security security) {
        BigDecimal price = prices.get(getKey(security, date));
        if(price == null) {
            throw new IllegalArgumentException("No price for " + security + " on " + date);
        }
        return price;
    }

    private PriceService() {}
}
