package academy.javaengineering.oop.bank;

/**
 * Represents a customer in the banking system.
 */
public final class Customer {
    private final String customerId;
    private final String name;
    private final String email;
    private final Address address;

    public Customer(String customerId, String name, String email, Address address) {
        this.customerId = Objects.requireNonNull(customerId, "Customer ID required");
        this.name = Objects.requireNonNull(name, "Name required");
        this.email = Objects.requireNonNull(email, "Email required");
        this.address = Objects.requireNonNull(address, "Address required");
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Address getAddress() { return address; }

    public void updateEmail(String newEmail) {
        this.email = Objects.requireNonNull(newEmail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return "Customer{customerId='%s', name='%s', email='%s'}".formatted(customerId, name, email);
    }
}