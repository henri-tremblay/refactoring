package pro.tremblay.core;

import java.math.BigDecimal;

public class SecurityPosition {

    private Security security;
    private BigDecimal quantity;

    public Security getSecurity() {
        return security;
    }

    public SecurityPosition security(Security security) {
        this.security = security;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public SecurityPosition quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "SecurityPosition{" +
            "security=" + security +
            ", quantity=" + quantity +
            '}';
    }
}
