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

import javax.annotation.concurrent.ThreadSafe;
import javax.annotation.Nonnull;

/**
 * Type of transactions.
 */
@ThreadSafe
public enum TransactionType {
    /** Securities were bought using cash */
    BUY {
        @Override
        public boolean hasQuantity() {
            return true;
        }

        @Override
        public void revert(@Nonnull Position position, @Nonnull Transaction transaction) {
            position.addCash(transaction.getCash());
            position.addSecurityPosition(transaction.getSecurity(), transaction.getQuantity().negate());
        }
    },
    /** Securities were sold to get cash */
    SELL {
        @Override
        public boolean hasQuantity() {
            return true;
        }

        @Override
        public void revert(@Nonnull Position position, @Nonnull Transaction transaction) {
            position.addCash(transaction.getCash().negate());
            position.addSecurityPosition(transaction.getSecurity(), transaction.getQuantity());
        }
    },
    /** Cash was added to the position */
    DEPOSIT {
        @Override
        public boolean hasQuantity() {
            return false;
        }

        @Override
        public void revert(@Nonnull Position position, @Nonnull Transaction transaction) {
            position.addCash(transaction.getCash().negate());
        }

    },
    /** Cash was removed from the position */
    WITHDRAWAL {
        @Override
        public boolean hasQuantity() {
            return false;
        }

        @Override
        public void revert(@Nonnull Position position, @Nonnull Transaction transaction) {
            position.addCash(transaction.getCash());
        }
    };

    /**
     * Tells if this type of transactions involve a security movement.
     *
     * @return if some security quantity was exchanged
     */
    public abstract boolean hasQuantity();

    /**
     * Revert a transaction from a position.
     *
     * @param position the position on which the transaction will be reverted
     * @param transaction the transaction to revert
     */
    public abstract void revert(@Nonnull Position position, @Nonnull Transaction transaction);

}
