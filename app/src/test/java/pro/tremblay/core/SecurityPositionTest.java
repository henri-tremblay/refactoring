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

import static org.assertj.core.api.Assertions.assertThat;
import static pro.tremblay.core.SecurityPosition.securityPosition;

public class SecurityPositionTest {

    private SecurityPosition securityPosition = securityPosition(Security.GOOGL, Quantity.ten());

    @Test
    public void isFlat_notFlat() {
        assertThat(securityPosition.isFlat()).isFalse();
    }

    @Test
    public void isFlat_flat() {
        securityPosition = securityPosition(Security.GOOGL, Quantity.zero());
        assertThat(securityPosition.isFlat()).isTrue();
    }

    @Test
    public void testToString() {
        assertThat(securityPosition.toString()).isEqualTo("SecurityPosition[security=GOOGL, quantity=10]");
    }

    @Test
    public void testHashCode() {
        assertThat(securityPosition.hashCode()).isNotEqualTo(0);
    }

    @Test
    public void testEquals() {
        assertThat(securityPosition).isNotEqualTo(null);
        assertThat(securityPosition).isEqualTo(securityPosition);

        SecurityPosition newSecurityPosition = securityPosition(Security.GOOGL, Quantity.ten());
        assertThat(securityPosition).isEqualTo(newSecurityPosition);
        newSecurityPosition = securityPosition(Security.GOOGL, Quantity.zero());
        assertThat(securityPosition).isNotEqualTo(newSecurityPosition);
        newSecurityPosition = securityPosition(Security.APPL, Quantity.ten());
        assertThat(securityPosition).isNotEqualTo(newSecurityPosition);
    }
}
