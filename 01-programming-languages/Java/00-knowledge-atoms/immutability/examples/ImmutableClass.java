import java.math.BigDecimal;
import java.util.Currency;

public final class ImmutableClass {
    public static void main(String[] args) {
        Money usd10 = new Money(new BigDecimal("10.00"), Currency.getInstance("USD"));
        Money usd20 = new Money(new BigDecimal("20.00"), Currency.getInstance("USD"));
        Money usd30 = usd10.add(usd20);

        System.out.println(usd10 + " + " + usd20 + " = " + usd30);
        System.out.println("usd10 unchanged: " + usd10);
        System.out.println("usd20 unchanged: " + usd20);

        // Different currencies should throw
        try {
            Money eur5 = new Money(new BigDecimal("5.00"), Currency.getInstance("EUR"));
            usd10.add(eur5);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " + amount;
    }
}
