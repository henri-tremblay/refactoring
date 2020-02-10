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
import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A transaction that happened on a position.
 */
@NotThreadSafe
public class Transaction {

    /** Type of transaction **/
    private TransactionType type;
    /** Date at which the transaction occured */
    private LocalDate date;
    /** Amount of cash exchanged during the transaction. The amount is always positive, the side of the transaction is determined by its type */
    private Amount cash;
    /** Securities bought or sold if securities are involved in the transaction */
    private Security security;
    /** Quantity of securities exchanged during the transaction. The quantity is always positive, the side of the transaction is determined by its type */
    private BigDecimal quantity;

    public static Transaction transaction() {
        return new Transaction();
    }

    public TransactionType getType() {
        return type;
    }

    public Transaction type(TransactionType type) {
        this.type = type;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Transaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public Amount getCash() {
        return cash;
    }

    public Transaction cash(Amount cash) {
        this.cash = cash;
        return this;
    }

    public Security getSecurity() {
        return security;
    }

    public Transaction security(Security security) {
        this.security = security;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Transaction quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void revert(@Nonnull Position position) {
        getType().revert(position, this);
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "type=" + type +
            ", date=" + date +
            ", cash=" + cash +
            ", security=" + security +
            ", quantity=" + quantity +
            '}';
    }
}
