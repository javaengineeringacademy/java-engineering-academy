package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Value Object Patterns ===\n");

        // WHY: Value objects represent immutable data, equality by value
        // INTERNAL: All fields final, equals/hashCode by content, no identity
        // ENGINEERING: Use record (Java 16+) for simple value objects

        Money price1 = new Money(100, "USD");
        Money price2 = new Money(100, "USD");
        Money price3 = new Money(200, "USD");

        System.out.println("price1 == price2: " + (price1 == price2)); // false
        System.out.println("price1.equals(price2): " + price1.equals(price2)); // true
        System.out.println("price1.equals(price3): " + price1.equals(price3)); // false

        // Use as map key (works because equals/hashCode implemented)
        java.util.Map<Money, String> catalog = new java.util.HashMap<>();
        catalog.put(price1, "Widget");
        System.out.println("Widget price: " + catalog.get(new Money(100, "USD")));

        // TRADE-OFF: Value class vs record
        // Value class: mutable fields possible, more control
        // Record: immutable, concise, auto-generated equals/hashCode/toString
    }
}

class Money {
    private final int amount;
    private final String currency;

    Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money m = (Money) o;
        return amount == m.amount && currency.equals(m.currency);
    }

    @Override
    public int hashCode() { return java.util.Objects.hash(amount, currency); }

    @Override
    public String toString() { return amount + " " + currency; }
}
