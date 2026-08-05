package academy.javaengineering.patterns.creational;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {
    private String firstName;
    private String lastName;
    private int age = 0;
    private String email = "";
    private String phone = "";
    private final List<String> addresses = new ArrayList<>();

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder age(int age) {
        this.age = age;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder addAddress(String address) {
        this.addresses.add(address);
        return this;
    }

    public UserBuilder addresses(List<String> addresses) {
        this.addresses.addAll(addresses);
        return this;
    }

    public User build() {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        return new User.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .age(age)
                .email(email)
                .phone(phone)
                .addresses(new ArrayList<>(addresses))
                .build();
    }
}
