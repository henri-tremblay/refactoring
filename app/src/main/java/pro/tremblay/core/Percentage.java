/*
 * Copyright 2019-2023 the original author or authors.
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
import java.util.Objects;

@ThreadSafe
public final class Percentage extends Numeric<Percentage> {

    private static final Percentage HUNDRED = pct("100");
    private static final Percentage ZERO = pct("0");

    public static Percentage hundred() {
        return HUNDRED;
    }

    public static Percentage zero() {
        return ZERO;
    }

    public static Percentage pct(long value) {
        return new Percentage(BigDecimal.valueOf(value));
    }

    public static Percentage pct(@Nonnull String value) {
        return new Percentage(new BigDecimal(Objects.requireNonNull(value)));
    }

    public static Percentage pct(@Nonnull BigDecimal value) {
        return new Percentage(Objects.requireNonNull(value));
    }

    private Percentage(@Nonnull BigDecimal value) {
        super(value);
    }

    @Override
    protected Percentage fromValue(@Nonnull BigDecimal newValue) {
        return new Percentage(newValue);
    }

    @Override
    public int precision() {
        return 2;
    }

    @Override
    public String toString() {
        return super.toString() + "%";
    }
}
