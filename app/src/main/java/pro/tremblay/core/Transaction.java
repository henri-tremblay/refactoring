package pro.tremblay.core;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private TransactionType type;
    private LocalDate date;
    private BigDecimal cash;
    private Security security;
    private BigDecimal quantity;

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

    public BigDecimal getCash() {
        return cash;
    }

    public Transaction cash(BigDecimal cash) {
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
}
