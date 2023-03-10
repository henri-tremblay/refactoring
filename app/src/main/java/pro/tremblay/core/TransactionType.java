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

/**
 * Type of transactions.
 */
@ThreadSafe
public enum TransactionType {
    /** Securities were bought using cash */
    BUY,
    /** Securities were sold to get cash */
    SELL,
    /** Cash was added to the position */
    DEPOSIT,
    /** Cash was removed from the position */
    WITHDRAWAL;

    /**
     * Tells if this type of transactions involve a security movement.
     *
     * @return if some security quantity was exchanged
     */
    public boolean hasQuantity() {
        return switch (this) {
            case BUY, SELL -> true;
            case DEPOSIT, WITHDRAWAL -> false;
        };
    }

    /**
     * Revert a transaction from a position.
     *
     * @param position the position on which the transaction will be reverted
     * @param transaction the transaction to revert
     */
    public void revert(@Nonnull Position position, @Nonnull Transaction transaction) {
        switch (this) {
            case BUY -> {
                position.addCash(transaction.getCash());
                position.addSecurityPosition(transaction.getSecurity(), transaction.getQuantity().negate());
            }
            case SELL -> {
                position.addCash(transaction.getCash().negate());
                position.addSecurityPosition(transaction.getSecurity(), transaction.getQuantity());
            }
            case DEPOSIT -> position.addCash(transaction.getCash().negate());
            case WITHDRAWAL -> position.addCash(transaction.getCash());
        }
    }

}
