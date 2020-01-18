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
