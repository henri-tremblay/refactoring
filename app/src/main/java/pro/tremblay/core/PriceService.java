package pro.tremblay.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PriceService {

    private static final ConcurrentMap<String, BigDecimal> prices = new ConcurrentHashMap<>();

    private static final Random random = new Random();

    static {
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

    public static BigDecimal getPrice(LocalDate date, Security security) {
        BigDecimal price = prices.get(getKey(security, date));
        if(price == null) {
            System.out.println("No price for " + security + " on " + date);
        }
        return price;
    }

    public static BigDecimal getRealTimePrice(Security security) {
        return BigDecimal.valueOf(random.nextInt(100));
    }
}
