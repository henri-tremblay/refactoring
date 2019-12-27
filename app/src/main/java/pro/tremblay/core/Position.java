package pro.tremblay.core;

import java.math.BigDecimal;
import java.util.Collection;

public class Position {

    private BigDecimal cash;
    private Collection<SecurityPosition> securityPositions;

    public BigDecimal getCash() {
        return cash;
    }

    public Position cash(BigDecimal cash) {
        this.cash = cash;
        return this;
    }

    public Collection<SecurityPosition> getSecurityPositions() {
        return securityPositions;
    }

    public Position securityPositions(Collection<SecurityPosition> securityPositions) {
        this.securityPositions = securityPositions;
        return this;
    }
}
