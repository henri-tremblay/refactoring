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

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;

/**
 * Quantity possessed of a given security.
 */
@NotThreadSafe
public class SecurityPosition {

    private Security security;
    private BigDecimal quantity;

    public Security getSecurity() {
        return security;
    }

    public SecurityPosition security(Security security) {
        this.security = security;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public SecurityPosition quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "SecurityPosition{" +
            "security=" + security +
            ", quantity=" + quantity +
            '}';
    }
}
