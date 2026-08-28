package academy.javaengineering.modern.records;

import java.util.Objects;

/**
 * Record with complex validation and business logic.
 */
public class RecordWithValidation {

    public record Email(String address) {
        public Email {
            Objects.requireNonNull(address, "Email address cannot be null");
            if (!address.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Invalid email format: " + address);
            }
        }

        public String domain() {
            return address.substring(address.indexOf('@') + 1);
        }

        public String username() {
            return address.substring(0, address.indexOf('@'));
        }
    }

    public record Money(double amount, String currency) {
        public Money {
            if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
            Objects.requireNonNull(currency, "Currency cannot be null");
        }

        public Money add(Money other) {
            if (!this.currency.equals(other.currency)) {
                throw new IllegalArgumentException("Cannot add different currencies");
            }
            return new Money(this.amount + other.amount, this.currency);
        }

        public Money multiply(double factor) {
            return new Money(this.amount * factor, this.currency);
        }
    }

    public record Range(int start, int end) {
        public Range {
            if (start > end) {
                throw new IllegalArgumentException("Start must be <= end");
            }
        }

        public boolean contains(int value) {
            return value >= start && value <= end;
        }

        public int length() {
            return end - start + 1;
        }
    }

    public static void main(String[] args) {
        // Email validation
        var email = new Email("user@example.com");
        System.out.println("Email: " + email);
        System.out.println("Username: " + email.username());
        System.out.println("Domain: " + email.domain());

        // Money operations
        var price1 = new Money(10.50, "USD");
        var price2 = new Money(5.25, "USD");
        var total = price1.add(price2);
        System.out.println("\nMoney: " + price1 + " + " + price2 + " = " + total);
        System.out.println("Doubled: " + price1.multiply(2));

        // Range operations
        var range = new Range(1, 10);
        System.out.println("\nRange: " + range);
        System.out.println("Contains 5: " + range.contains(5));
        System.out.println("Contains 11: " + range.contains(11));
        System.out.println("Length: " + range.length());
    }
}
