package pro.tremblay.core;

public enum TransactionType {
    BUY {
        @Override
        public boolean hasQuantity() {
            return true;
        }
    },
    SELL {
        @Override
        public boolean hasQuantity() {
            return true;
        }
    },
    DEPOSIT {
        @Override
        public boolean hasQuantity() {
            return false;
        }
    },
    WITHDRAWAL {
        @Override
        public boolean hasQuantity() {
            return false;
        }
    };

    public abstract boolean hasQuantity();
}
