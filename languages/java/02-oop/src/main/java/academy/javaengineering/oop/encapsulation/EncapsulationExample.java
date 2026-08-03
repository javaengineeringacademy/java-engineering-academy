package academy.javaengineering.oop.encapsulation;

import java.util.Objects;

/**
 * Demonstrates encapsulation through getters, setters, and data hiding.
 *
 * <p>Encapsulation is the bundling of data with methods that operate on that data,
 * restricting direct access to some components. It protects object invariants
 * and provides a stable API.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Private fields with public accessors</li>
 *   <li>Validation in setters to maintain invariants</li>
 *   <li>Immutable objects (no setters, defensive copies)</li>
 *   <li>Computed properties via getters</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class EncapsulationExample {

    /**
     * Mutable bank account with encapsulation enforcing business rules.
     */
    public static class BankAccount {
        private final String accountId;
        private String holderName;
        private double balance;
        private String status;
        private static final double OVERDRAFT_LIMIT = -500.00;

        public BankAccount(String accountId, String holderName, double initialBalance) {
            this.accountId = Objects.requireNonNull(accountId);
            this.holderName = Objects.requireNonNull(holderName);
            if (initialBalance < 0) {
                throw new IllegalArgumentException("Initial balance cannot be negative");
            }
            this.balance = initialBalance;
            this.status = "ACTIVE";
        }

        public String getAccountId() { return accountId; }

        public String getHolderName() { return holderName; }

        public void setHolderName(String holderName) {
            if (holderName == null || holderName.isBlank()) {
                throw new IllegalArgumentException("Holder name cannot be blank");
            }
            this.holderName = holderName;
        }

        public double getBalance() { return balance; }

        public String getStatus() { return status; }

        /**
         * Deposits money into the account.
         *
         * @param amount the amount to deposit (must be positive)
         * @throws IllegalArgumentException if amount is negative or account is closed
         */
        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }
            if ("CLOSED".equals(status)) {
                throw new IllegalStateException("Cannot deposit to a closed account");
            }
            this.balance += amount;
        }

        /**
         * Withdraws money from the account if sufficient funds exist.
         *
         * @param amount the amount to withdraw
         * @return {@code true} if withdrawal succeeded
         * @throws IllegalArgumentException if amount is invalid
         * @throws IllegalStateException if account is closed
         */
        public boolean withdraw(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive");
            }
            if ("CLOSED".equals(status)) {
                throw new IllegalStateException("Cannot withdraw from a closed account");
            }
            if (balance - amount < OVERDRAFT_LIMIT) {
                return false;
            }
            this.balance -= amount;
            return true;
        }

        /**
         * Computes whether the account is in good standing.
         *
         * @return {@code true} if balance is non-negative
         */
        public boolean isInGoodStanding() {
            return balance >= 0;
        }

        public void close() {
            if (balance != 0) {
                throw new IllegalStateException("Cannot close account with non-zero balance");
            }
            this.status = "CLOSED";
        }

        @Override
        public String toString() {
            return "BankAccount{id='%s', holder='%s', balance=$%.2f, status='%s'}".formatted(
                    accountId, holderName, balance, status);
        }
    }

    /**
     * Immutable address class - no setters, defensive copies.
     */
    public static class Address {
        private final String street;
        private final String city;
        private final String state;
        private final String zipCode;
        private final String country;

        public Address(String street, String city, String state, String zipCode, String country) {
            this.street = Objects.requireNonNull(street);
            this.city = Objects.requireNonNull(city);
            this.state = Objects.requireNonNull(state);
            this.zipCode = Objects.requireNonNull(zipCode);
            this.country = Objects.requireNonNull(country);
        }

        public String getStreet() { return street; }
        public String getCity() { return city; }
        public String getState() { return state; }
        public String getZipCode() { return zipCode; }
        public String getCountry() { return country; }

        /**
         * Factory method to create a US address.
         */
        public static Address usAddress(String street, String city, String state, String zipCode) {
            return new Address(street, city, state, zipCode, "US");
        }

        /**
         * Returns a new Address with the street updated (immutable update pattern).
         */
        public Address withStreet(String newStreet) {
            return new Address(newStreet, city, state, zipCode, country);
        }

        public String getFormatted() {
            return "%s, %s, %s %s, %s".formatted(street, city, state, zipCode, country);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Address other)) return false;
            return street.equals(other.street) && city.equals(other.city)
                    && state.equals(other.state) && zipCode.equals(other.zipCode)
                    && country.equals(other.country);
        }

        @Override
        public int hashCode() {
            return Objects.hash(street, city, state, zipCode, country);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Encapsulation Demo ===\n");

        // Mutable object with validation
        System.out.println("--- Bank Account (Mutable) ---");
        BankAccount account = new BankAccount("ACC-001", "Alice Johnson", 1000.00);
        System.out.println("Created: " + account);

        account.deposit(500.00);
        System.out.println("After deposit:   Balance = $%.2f".formatted(account.getBalance()));

        boolean withdrew = account.withdraw(200.00);
        System.out.println("Withdraw $200:   %s | Balance = $%.2f".formatted(withdrew, account.getBalance()));

        boolean overdraft = account.withdraw(2000.00);
        System.out.println("Withdraw $2000:  %s | Balance = $%.2f".formatted(overdraft, account.getBalance()));

        System.out.println("In good standing: " + account.isInGoodStanding());

        // Demonstrate validation
        try {
            account.deposit(-100);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation: " + e.getMessage());
        }

        // Immutable object
        System.out.println("\n--- Address (Immutable) ---");
        Address addr1 = Address.usAddress("123 Main St", "Springfield", "IL", "62704");
        System.out.println("Original:    " + addr1.getFormatted());

        Address addr2 = addr1.withStreet("456 Oak Ave");
        System.out.println("New street:  " + addr2.getFormatted());
        System.out.println("Unchanged:   " + addr1.getFormatted());

        System.out.println("\nAddresses equal: " + addr1.equals(addr2));
        System.out.println("Same hashCode:   " + (addr1.hashCode() == addr2.hashCode()));
    }
}
