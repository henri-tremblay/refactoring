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

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Objects;

public final class Amount {
    private final BigDecimal value;

    private static final Amount ZERO = new Amount(BigDecimal.ZERO);

    public static Amount zero() {
        return ZERO;
    }

    public static Amount amount(long value) {
        return new Amount(BigDecimal.valueOf(value));
    }

    public static Amount amount(@Nonnull BigDecimal value) {
        return new Amount(Objects.requireNonNull(value));
    }

    public static Amount amount(@Nonnull String value) {
        return new Amount(new BigDecimal(Objects.requireNonNull(value)));
    }

    public static Amount add(Amount a1, Amount a2) {
        return new Amount(a1.value.add(a2.value));
    }

    public static Amount substract(Amount a1, Amount a2) {
        return new Amount(a1.value.subtract(a2.value));
    }

    private Amount(@Nonnull BigDecimal value) {
        this.value = value;
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    public boolean isZero() {
        return value.signum() == 0;
    }

    public Amount negate() {
        return new Amount(value.negate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return value.equals(amount.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
