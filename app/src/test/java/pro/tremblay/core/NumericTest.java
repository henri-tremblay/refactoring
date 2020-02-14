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

import org.junit.Test;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

import static pro.tremblay.core.Assertions.assertThat;

public class NumericTest {

    private static class ANumeric extends Numeric<ANumeric> {
        protected ANumeric(@Nonnull BigDecimal value) {
            super(value);
        }

        @Override
        protected ANumeric fromValue(@Nonnull BigDecimal newValue) {
            return new ANumeric(newValue);
        }

        @Override
        public int precision() {
            return 1;
        }
    }

    ANumeric a1 = n("1.2");
    ANumeric a2 = n("1.2");
    ANumeric a3 = n("1.3");

    private ANumeric n(String s) {
        return new ANumeric(new BigDecimal(s));
    }

    @Test
    public void isZero() {
        assertThat(n("0.0").isZero()).isTrue();
        assertThat(n("0.1").isZero()).isFalse();
    }

    @Test
    public void toBigDecimal() {
        assertThat(a1).isEqualTo("1.2");
    }

    @Test
    public void add() {
        assertThat(a1.add(a3)).isEqualTo("2.5");
    }

    @Test
    public void substract() {
        assertThat(a3.substract(a1)).isEqualTo("0.1");
    }

    @Test
    public void negate() {
        assertThat(a1.negate()).isEqualTo("-1.2");
    }

    @Test
    public void scale() {
        assertThat(a1.scale(100, 200)).isEqualTo("2.4");
    }
    @Test
    public void testEquals() {
        assertThat(a1).isEqualTo(a1);
        assertThat(a1).isEqualTo(a2);
        assertThat(a1).isNotEqualTo(a3);
        assertThat(a1).isNotEqualTo(null);
        assertThat(a1).isNotEqualTo("");
    }

    @Test
    public void testHashCode() {
        assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
    }
}
