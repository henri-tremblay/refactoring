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

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class PreferencesTest {

    private Preferences preferences = Preferences.preferences();

    @Test
    public void getString_unknownPreference() {
        assertThat(preferences.getString("aaa")).isNull();
    }

    @Test
    public void getInteger_unknownPreference() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> preferences.getInteger("aaa"))
            .withMessage("aaa is not a known preference");
    }

    @Test
    public void getString() {
        preferences.put("a", "value");
        assertThat(preferences.getString("a")).isEqualTo("value");
    }

    @Test
    public void getInteger() {
        preferences.put("a", "123");
        assertThat(preferences.getInteger("a")).isEqualTo(123);
    }

    @Test
    public void getInteger_invalidNumber() {
        preferences.put("a", "value");
        assertThatExceptionOfType(NumberFormatException.class)
            .isThrownBy(() -> preferences.getInteger("a"));
    }
}
