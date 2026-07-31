package academy.javaengineering.oop.encapsulation;

/**
 * Money - Immutable value class demonstrating encapsulation for data integrity.
 * 
 * <p>Immutable objects have no setters - once created, their state never changes.
 * This provides thread-safety and predictable behavior.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public final class Money {

    private final double amount;
    private final String currency;

    public Money(double amount, String currency) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies: " 
                + this.currency + " + " + other.currency);
        }
        return new Money(this.amount + other.amount, this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract different currencies");
        }
        if (this.amount < other.amount) {
            throw new IllegalArgumentException("Insufficient amount");
        }
        return new Money(this.amount - other.amount, this.currency);
    }

    public Money multiply(double factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Factor cannot be negative");
        }
        return new Money(this.amount * factor, this.currency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Money other)) return false;
        return Double.compare(other.amount, amount) == 0 
            && currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }
}