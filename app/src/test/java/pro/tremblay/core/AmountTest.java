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

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.tremblay.core.Amount.amount;

public class AmountTest {

    Amount a1 = amount("1.2");
    Amount a2 = amount("1.2");
    Amount a3 = amount("1.3");

    @Test
    public void amountInteger() {
        assertThat(amount(12L).toBigDecimal()).isEqualTo(BigDecimal.valueOf(12));
    }

    @Test
    public void amountBigDecimal() {
        assertThat(amount(BigDecimal.valueOf(12)).toBigDecimal()).isEqualTo(BigDecimal.valueOf(12));
    }

    @Test
    public void isZero() {
        assertThat(amount("0.00").isZero()).isTrue();
        assertThat(amount("0.01").isZero()).isFalse();
    }

    @Test
    public void toBigDecimal() {
        assertThat(a1.toBigDecimal()).isEqualTo(BigDecimal.valueOf(12, 1));
    }

    @Test
    public void add() {
        assertThat(Amount.add(a1, a3)).isEqualTo(amount("2.5"));
    }

    @Test
    public void substract() {
        assertThat(Amount.substract(a3, a1)).isEqualTo(amount("0.1"));
    }

    @Test
    public void negate() {
        assertThat(a1.negate()).isEqualTo(amount("-1.2"));
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
        assertThat(a1.toString()).isEqualTo("1.2");
    }
}
