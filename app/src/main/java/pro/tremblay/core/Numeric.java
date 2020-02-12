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
public abstract class Numeric<T extends Numeric<T>> {
    protected final BigDecimal value;

    protected Numeric(@Nonnull BigDecimal value) {
        this.value = value;
    }

    protected abstract T fromValue(@Nonnull BigDecimal newValue);

    public T add(@Nonnull T numeric) {
        return fromValue(value.add(numeric.value));
    }

    public T substract(@Nonnull T numeric) {
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
