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

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Global configuration of the application.
 */
@ThreadSafe
public class Preferences {

    private static final Preferences INSTANCE = new Preferences();

    public static Preferences preferences() {
        return INSTANCE;
    }

    private final ConcurrentMap<String, String> preferences = new ConcurrentHashMap<>();

    private Preferences() {}

    public void put(String key, String value) {
        preferences.put(key, value);
    }

    public String getString(String key) {
        String value = preferences.get(key);
        if (value != null) {
            return value;
        }
        return System.getProperty(key);
    }

    public int getInteger(String key) {
        String value = getString(key);
        if (value == null) {
            throw new IllegalArgumentException(key + " is not a known preference");
        }
        return Integer.parseInt(value);
    }
}
