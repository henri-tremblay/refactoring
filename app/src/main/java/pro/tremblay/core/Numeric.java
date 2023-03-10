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
import java.math.RoundingMode;
import java.util.Objects;

@ThreadSafe
public abstract sealed class Numeric<T extends Numeric<T>> permits Amount, Percentage, Quantity {
    protected final BigDecimal value;

    protected Numeric(@Nonnull BigDecimal value) {
        this.value = value.setScale(precision(), RoundingMode.HALF_UP);
    }

    protected abstract T fromValue(@Nonnull BigDecimal newValue);

    public abstract int precision();

    public T add(@Nonnull T numeric) {
        return fromValue(value.add(numeric.value));
    }

    public T subtract(@Nonnull T numeric) {
        return fromValue(value.subtract(numeric.value));
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    public boolean isZero() {
        return value.signum() == 0;
    }

    public T negate() {
        return fromValue(value.negate());
    }

    /**
     * Scale this numerator to another denominator. E.g. if this is "3" on "4" ("from" param)
     * and we want to scale to "8" ("to" param), we expect 3 x 8 / 4 = 6 as a result.
     *
     * @param from denominator from which to start
     * @param to denominator to go to
     * @return the numerator scaling to another denominator
     */
    public T scale(int from, int to) {
        return fromValue(value.multiply(BigDecimal.valueOf(to))
            .divide(BigDecimal.valueOf(from), 2, RoundingMode.HALF_UP));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Numeric<?> numeric = (Numeric<?>) o;
        return value.equals(numeric.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return toBigDecimal().toPlainString();
    }
}
