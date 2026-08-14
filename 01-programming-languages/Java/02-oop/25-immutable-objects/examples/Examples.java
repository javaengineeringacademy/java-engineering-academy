package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Immutable Object Patterns ===\n");

        // WHY: Immutable objects are thread-safe, cacheable, no defensive copying
        // INTERNAL: All fields final, no setters, deep copy in constructor
        // ENGINEERING: Use for values, keys in maps, concurrent access

        Money m1 = new Money(100, "USD");
        Money m2 = m1.add(new Money(50, "USD"));
        Money m3 = m1.add(new Money(25, "EUR"));

        System.out.println("m1: " + m1); // Still 100!
        System.out.println("m2: " + m2);
        System.out.println("m3: " + m3);

        // Safe to share across threads without synchronization
        // Can be used as HashMap keys (hashcode won't change)
        java.util.Map<Money, String> prices = new java.util.HashMap<>();
        prices.put(new Money(10, "USD"), "Book");
        System.out.println("Price of Book: " + prices.get(new Money(10, "USD")));

        // TRADE-OFF: Immutability vs performance
        // Immutability: safe, thread-safe, but creates new objects
        // Mutability: faster, but requires synchronization
    }
}

final class Money { // final prevents subclassing
    private final int amount;  // final field
    private final String currency;

    Money(int amount, String currency) {
        // Validate in constructor
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        if (currency == null) throw new IllegalArgumentException("Currency cannot be null");
        this.amount = amount;
        this.currency = currency;
    }

    // No setters!

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount + other.amount, this.currency); // New instance
    }

    public int getAmount() { return amount; }
    public String getCurrency() { return currency; }

    @Override
    public String toString() { return amount + " " + currency; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money m = (Money) o;
        return amount == m.amount && currency.equals(m.currency);
    }

    @Override
    public int hashCode() { return java.util.Objects.hash(amount, currency); }
}
