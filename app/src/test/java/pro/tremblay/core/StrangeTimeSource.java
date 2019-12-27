package pro.tremblay.core;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;

public class StrangeTimeSource {

    private LocalDate today;
    private Instant now;

    public StrangeTimeSource today(LocalDate today) {
        this.today = today;
        return this;
    }

    public LocalDate today() {
        return today;
    }

    public StrangeTimeSource now(Instant now) {
        this.now = now;
        return this;
    }

    public Instant now() {
        return now;
    }

    public void moveNow(long seconds, TemporalUnit unit) {
        now = now.plus(seconds, unit);
    }
}
