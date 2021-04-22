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
import java.util.Collection;

/**
 * All positions (cash and security) of a user. There is only one cash position since we are trading in only one
 * currency.
 */
@NotThreadSafe
public class Position {

    private BigDecimal cash;
    private Collection<SecurityPosition> securityPositions;

    public BigDecimal getCash() {
        return cash;
    }

    public Position cash(BigDecimal cash) {
        this.cash = cash;
        return this;
    }

    public Collection<SecurityPosition> getSecurityPositions() {
        return securityPositions;
    }

    public Position securityPositions(Collection<SecurityPosition> securityPositions) {
        this.securityPositions = securityPositions;
        return this;
    }

    @Override
    public String toString() {
        return "Position{" +
            "cash=" + cash +
            ", securityPositions=" + securityPositions +
            '}';
    }
}
