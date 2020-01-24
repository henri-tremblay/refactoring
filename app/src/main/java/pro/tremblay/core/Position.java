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

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pro.tremblay.core.SecurityPosition.securityPosition;

/**
 * All positions (cash and security) of a user. There is only one cash position since we are trading in only one
 * currency.
 */
@NotThreadSafe
public class Position {

    private BigDecimal cash;
    private final Map<Security, BigDecimal> securityPositions = new HashMap<>();

    public static Position position() {
        return new Position();
    }

    public Position addSecurityPosition(Security security, BigDecimal quantity) {
        securityPositions.compute(security, (sec, currentQuantity) -> {
            if(currentQuantity == null) {
                return quantity;
            }
            return quantity.add(currentQuantity);
        });
        return this;
    }

    public BigDecimal getSecurityPosition(Security security) {
        return securityPositions.getOrDefault(security, BigDecimal.ZERO);
    }

    public void addCash(BigDecimal cash) {
        this.cash = this.cash.add(cash);
    }


    public BigDecimal getCash() {
        return cash;
    }

    public Position cash(BigDecimal cash) {
        this.cash = cash;
        return this;
    }

    public Position addSecurityPositions(SecurityPosition... securityPositions) {
        for (SecurityPosition sp : securityPositions) {
            this.securityPositions.put(sp.getSecurity(), sp.getQuantity());
        }
        return this;
    }

    public Position copy() {
        Position position = position()
                .cash(cash);
        position.securityPositions.putAll(securityPositions);
        return position;
    }

    private List<Map.Entry<Security, BigDecimal>> sortedSecurityPositions() {
        return securityPositions.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
    }

    public BigDecimal securityPositionValue(LocalDate date, PriceService priceService) {
        return securityPositions
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().signum() != 0)
            .map(entry -> priceService.getPrice(date, entry.getKey()).multiply(entry.getValue()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Collection<SecurityPosition> getSecurityPositions() {
        return securityPositions
            .entrySet()
            .stream()
            .map(entry -> securityPosition(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Position{" +
            "cash=" + cash +
            ", securityPositions=" + securityPositions +
            '}';
    }
}
