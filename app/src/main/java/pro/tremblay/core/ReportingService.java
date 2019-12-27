package pro.tremblay.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReportingService {

    public BigDecimal calculateReturnOnInvestmentYTD(Position current, Collection<Transaction> transactions) {
        LocalDate now = LocalDate.now();
        LocalDate beginningOfYear = now.withDayOfYear(1);

        Position working = new Position()
                .cash(current.getCash());

        List<SecurityPosition> positions = current.getSecurityPositions().stream()
                .map(securityPosition -> new SecurityPosition()
                        .quantity(securityPosition.getQuantity())
                        .security(securityPosition.getSecurity())
                )
                .collect(Collectors.toList());
        working.securityPositions(positions);

        List<Transaction> orderedTransaction = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());

        LocalDate today = now;
        int transactionIndex = 0;
        while (!today.isBefore(beginningOfYear)) {
            if (transactionIndex >= orderedTransaction.size())  {
                break;
            }
            Transaction transaction = orderedTransaction.get(transactionIndex);
            // It's a transaction on the date, process it
            if (transaction.getDate().equals(today)) {
                revert(working, transaction);
            }
            today = today.minusDays(1);
        }

        BigDecimal initialCashValue = working.getCash();
        BigDecimal currentCashValue = current.getCash();

        BigDecimal initialSecPosValue = null;
        try {
            initialSecPosValue = working.getSecurityPositions()
                .stream()
                .map(securityPosition -> PriceService.getPrice(beginningOfYear, securityPosition.getSecurity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch(NullPointerException e) {
            working.getSecurityPositions().forEach(System.out::println);
        }
        BigDecimal currentSecPosValue = current.getSecurityPositions()
                .stream()
                .map(securityPosition -> PriceService.getPrice(now, securityPosition.getSecurity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal initialValue = BigDecimalUtil.add(initialCashValue, initialSecPosValue);
        BigDecimal roi;
        if(initialValue.signum() == 0) {
            roi = BigDecimal.ZERO.setScale(10, RoundingMode.UNNECESSARY);
        }
        else {
            roi = (BigDecimalUtil.add(currentCashValue.subtract(initialCashValue), currentSecPosValue.subtract(initialSecPosValue)))
                    .divide(initialValue, 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100L));
        }

        int yearLength = Preferences.preferences().getInteger("LENGTH_OF_YEAR");

        roi = roi.multiply(BigDecimal.valueOf(yearLength)).divide(BigDecimal.valueOf(now.getDayOfYear()), 2, RoundingMode.HALF_UP);

        return roi;
    }

    private void revert(Position current, Transaction transaction) {
        switch (transaction.getType()) {
            case BUY: {
                current.cash(current.getCash().subtract(transaction.getCash()));
                SecurityPosition pos = current.getSecurityPositions().stream()
                        .filter(sec -> sec.getSecurity().equals(transaction.getSecurity()))
                        .findAny()
                        .orElse(null);
                if (pos == null) {
                    pos = new SecurityPosition().quantity(BigDecimal.ZERO).security(transaction.getSecurity());
                    current.getSecurityPositions().add(pos);
                }
                pos.quantity(BigDecimalUtil.add(pos.getQuantity(), transaction.getQuantity()));
                break;
            }
            case SELL:
                current.cash(BigDecimalUtil.add(current.getCash(), transaction.getCash()));
                SecurityPosition pos = current.getSecurityPositions().stream()
                        .filter(sec -> sec.getSecurity().equals(transaction.getSecurity()))
                        .findAny()
                        .orElse(null);
                if (pos == null) {
                    pos = new SecurityPosition().quantity(BigDecimal.ZERO).security(transaction.getSecurity());
                    current.getSecurityPositions().add(pos);
                }
                pos.quantity(pos.getQuantity().subtract(transaction.getQuantity()));
                break;
            case DEPOSIT:
                current.cash(current.getCash().subtract(transaction.getCash()));
                break;
            case WITHDRAWAL:
                current.cash(BigDecimalUtil.add(current.getCash(), transaction.getCash()));
                break;
        }
    }

}
