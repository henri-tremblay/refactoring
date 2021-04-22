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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;

/**
 * Helper class to make it easier to create a {@code BigDecimal}.
 */
@ThreadSafe
public final class BigDecimalUtil {

    @Nonnull
    public static BigDecimal bd(@Nonnull String value) {
        return new BigDecimal(value);
    }

    @Nonnull
    public static BigDecimal bd(int value) {
        return BigDecimal.valueOf(value);
    }

    private BigDecimalUtil() {}
}
