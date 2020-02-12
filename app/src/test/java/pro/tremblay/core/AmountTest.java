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

import java.math.BigDecimal;

import static pro.tremblay.core.Amount.amnt;
import static pro.tremblay.core.Assertions.assertThat;

public class AmountTest {

    Amount a1 = amnt("1.2");
    Amount a2 = amnt("1.2");
    Amount a3 = amnt("1.3");

    @Test
    public void amountInteger() {
        assertThat(Amount.amnt(12L)).isEqualTo("12.00");
    }

    @Test
    public void amountBigDecimal() {
        assertThat(Amount.amnt(BigDecimal.valueOf(12))).isEqualTo("12.00");
    }

    @Test
    public void isZero() {
        assertThat(amnt("0.00").isZero()).isTrue();
        assertThat(amnt("0.01").isZero()).isFalse();
    }

    @Test
    public void toBigDecimal() {
        assertThat(a1).isEqualTo("1.20");
    }

    @Test
    public void add() {
        assertThat(a1.add(a3)).isEqualTo("2.50");
    }

    @Test
    public void substract() {
        assertThat(a3.substract(a1)).isEqualTo("0.10");
    }

    @Test
    public void negate() {
        assertThat(a1.negate()).isEqualTo("-1.20");
    }

    @Test
    public void testEquals() {
        assertThat(a1).isEqualTo(a2);
        assertThat(a1).isNotEqualTo(a3);
        assertThat(a1).isNotEqualTo(null);
    }

    @Test
    public void testHashCode() {
        assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
    }

    @Test
    public void testToString() {
        assertThat(a1.toString()).isEqualTo("1.20$");
    }
}
