package academy.javaengineering.oop.bank.project;

import java.util.Objects;

/**
 * Customer - Represents a bank customer.
 * 
 * <p>Encapsulation: Private fields with getters.
 * Immutability: All fields are final.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public final class Customer {

    private final String customerId;
    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(String customerId, String firstName, String lastName, String email) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer other = (Customer) obj;
        return Objects.equals(customerId, other.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return "Customer{id='" + customerId + "', name='" + getFullName() + "'}";
    }
}