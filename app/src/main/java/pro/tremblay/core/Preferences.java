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
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Global configuration of the application.
 */
@ThreadSafe
public class Preferences {

    private final ConcurrentMap<String, String> preferences = new ConcurrentHashMap<>();

    public void put(@Nonnull String key, @Nonnull String value) {
        preferences.put(key, value);
    }

    @Nonnull
    public String getString(@Nonnull String key) {
        String value = preferences.get(key);
        if (value == null) {
            throw new IllegalArgumentException(key + " is not a known preference");
        }
        return value;
    }

    public int getInteger(@Nonnull String key) {
        String value = getString(key);
        return Integer.parseInt(value);
    }
}
