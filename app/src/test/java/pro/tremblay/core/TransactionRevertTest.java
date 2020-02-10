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

import static org.assertj.core.api.Assertions.*;
import static pro.tremblay.core.Amount.amount;
import static pro.tremblay.core.BigDecimalUtil.bd;
import static pro.tremblay.core.Position.position;
import static pro.tremblay.core.SecurityPosition.securityPosition;
import static pro.tremblay.core.Transaction.transaction;

public class TransactionRevertTest {

    private Position position = position()
        .cash(amount(100));

    @Test
    public void hasQuantity() {
        assertThat(TransactionType.BUY.hasQuantity()).isTrue();
        assertThat(TransactionType.SELL.hasQuantity()).isTrue();
        assertThat(TransactionType.DEPOSIT.hasQuantity()).isFalse();
        assertThat(TransactionType.WITHDRAWAL.hasQuantity()).isFalse();
    }

    @Test
    public void revertBuy() {
        Transaction transaction = transaction()
            .type(TransactionType.BUY)
            .cash(amount(50))
            .security(Security.GOOGL)
            .quantity(bd(20));

        transaction.revert(position);

        assertThat(position.getCash().toBigDecimal()).isEqualTo("150");
        assertThat(position.getSecurityPositions())
            .usingFieldByFieldElementComparator()
            .containsOnly(securityPosition(Security.GOOGL, bd(-20)));

        // Do it again now that the position exists
        transaction.revert(position);

        assertThat(position.getCash().toBigDecimal()).isEqualTo("200");
        assertThat(position.getSecurityPositions())
            .usingFieldByFieldElementComparator()
            .containsOnly(securityPosition(Security.GOOGL, bd(-40)));
    }

    @Test
    public void revertSell() {
        Transaction transaction = transaction()
            .type(TransactionType.SELL)
            .cash(amount(30))
            .security(Security.GOOGL)
            .quantity(bd(20));

        transaction.revert(position);

        assertThat(position.getCash().toBigDecimal()).isEqualTo("70");
        assertThat(position.getSecurityPositions())
            .usingFieldByFieldElementComparator()
            .containsOnly(securityPosition(Security.GOOGL, bd(20)));

        // Do it again now that the position exists
        transaction.revert(position);

        assertThat(position.getCash().toBigDecimal()).isEqualTo("40");
        assertThat(position.getSecurityPositions())
            .usingFieldByFieldElementComparator()
            .containsOnly(securityPosition(Security.GOOGL, bd(40)));

    }

    @Test
    public void revertDeposit() {
        Transaction transaction = transaction()
            .type(TransactionType.DEPOSIT)
            .cash(amount(30));

        transaction.revert(position);

        assertThat(position.getCash().toBigDecimal()).isEqualTo("70");
        assertThat(position.getSecurityPositions()).isEmpty();
    }

    @Test
    public void revertWithdrawal() {
        Transaction transaction = transaction()
            .type(TransactionType.WITHDRAWAL)
            .cash(amount(30));

        transaction.revert(position);

        assertThat(position.getCash().toBigDecimal()).isEqualTo("130");
        assertThat(position.getSecurityPositions()).isEmpty();
    }
}
