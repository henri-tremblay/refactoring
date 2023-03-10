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

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static pro.tremblay.core.Amount.amnt;
import static pro.tremblay.core.Assertions.assertThat;
import static pro.tremblay.core.Position.position;
import static pro.tremblay.core.Quantity.qty;

public class PositionTest {

    private Position position = position()
        .cash(amnt(10))
        .addSecurityPosition(Security.GOOGL, qty(22))
        .addSecurityPosition(Security.APPL, qty(11));

    @Test
    public void copy() {
        Position actual = position.copy();
        assertThat(actual).isNotSameAs(position);
        assertThat(actual.getCash()).isEqualTo(position.getCash());
        assertThat(actual.getSecurityPositions())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(position.getSecurityPositions());
    }

    @Test
    public void addCash() {
        position.addCash(amnt(200));
        assertThat(position.getCash()).isEqualTo("210.00");
    }

    @Test
    public void addSecurity_existing() {
        position.addSecurityPosition(Security.GOOGL, qty(30));
        Quantity securityPosition = position.getSecurityPosition(Security.GOOGL);
        assertThat(securityPosition).isEqualTo("52");
    }

    @Test
    public void addSecurity_new() {
        position.addSecurityPosition(Security.IBM, qty(30));
        Quantity securityPosition = position.getSecurityPosition(Security.IBM);
        assertThat(securityPosition).isEqualTo("30");
    }

    @Test
    public void securityPositionValue() {
        LocalDate now = LocalDate.now();

        PriceService priceService = mock(PriceService.class);
        expect(priceService.getPrice(now, Security.GOOGL)).andStubReturn(amnt(10));
        expect(priceService.getPrice(now, Security.APPL)).andStubReturn(amnt(5));
        replay(priceService);

        Amount result = position.securityPositionValue(now, priceService);
        assertThat(result).isEqualTo("275.00"); // 22 * 10 + 11 * 5
    }

    @Test
    public void securityPositionValue_noPriceNeededForFlatPosition() {
        LocalDate now = LocalDate.now();

        position = position()
            .addSecurityPosition(Security.APPL, qty(0));

        PriceService priceService = mock(PriceService.class);
        replay(priceService);

        Amount result = position.securityPositionValue(now, priceService);
        assertThat(result).isEqualTo(Amount.zero());

        verify(priceService);
    }

    @Test
    public void getSecurityPosition() {
        assertThat(position.getSecurityPosition(Security.GOOGL)).isEqualTo("22");
        assertThat(position.getSecurityPosition(Security.IBM)).isZero();
    }

    @Test
    public void testToString() {
        assertThat(position.toString()).isEqualTo("Position{cash=10.00$, securityPositions=[APPL=11, GOOGL=22]}");
    }

    @Test
    public void addSecurityPositions() {
        position.addSecurityPositions(
            SecurityPosition.securityPosition(Security.IBM, qty(100)),
            SecurityPosition.securityPosition(Security.INTC, qty(200)));
        assertThat(position.getSecurityPosition(Security.IBM)).isEqualTo("100");
        assertThat(position.getSecurityPosition(Security.INTC)).isEqualTo("200");
        assertThat(position.getSecurityPosition(Security.GOOGL)).isEqualTo("22");
        assertThat(position.getSecurityPosition(Security.APPL)).isEqualTo("11");
    }
}
