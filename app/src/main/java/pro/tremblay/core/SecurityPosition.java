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
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Objects;

/**
 * Quantity possessed of a given security.
 */
@NotThreadSafe
public class SecurityPosition {

    private final Security security;
    private final Quantity quantity;

    public static SecurityPosition securityPosition(Security security, Quantity quantity) {
        return new SecurityPosition(security, quantity);
    }

    private SecurityPosition(Security security, Quantity quantity) {
        this.security = security;
        this.quantity = quantity;
    }

    @Nonnull
    public Security security() {
        return security;
    }

    @Nonnull
    public Quantity quantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "SecurityPosition{" +
            "security=" + security +
            ", quantity=" + quantity +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityPosition that = (SecurityPosition) o;
        return security == that.security && quantity.equals(that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(security, quantity);
    }

    public boolean isFlat() {
        return quantity.isZero();
    }
}
