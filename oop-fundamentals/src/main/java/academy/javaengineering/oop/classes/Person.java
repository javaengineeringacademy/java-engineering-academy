package academy.javaengineering.oop.classes;

import java.util.Objects;

/**
 * Example demonstrating a well-designed class with proper encapsulation.
 * 
 * <p>This class demonstrates:
 * <ul>
 *   <li>Private fields with validation</li>
 *   <li>Constructor with validation</li>
 *   <li>Getters without setters for immutable fields</li>
 *   <li>Setter with validation for mutable field</li>
 *   <li>Proper {@code toString()}, {@code equals()}, {@code hashCode()}</li>
 * </ul>
 */
public final class Person {
    private final String name;
    private int age;
    private final String email;

    /**
     * Constructs a Person with validation.
     *
     * @param name the person's name (non-null, non-blank)
     * @param age the person's age (must be >= 0)
     * @param email the person's email (non-null, valid format)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Person(String name, int age, String email) {
        this.name = Objects.requireNonNull(name, "Name is required");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.age = validateAge(age);
        this.email = validateEmail(email);
    }

    private int validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age must be >= 0");
        }
        return age;
    }

    private String validateEmail(String email) {
        Objects.requireNonNull(email, "Email is required");
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    public void setAge(int age) {
        this.age = validateAge(age);
    }

    @Override
    public String toString() {
        return "Person{name='%s', age=%d, email='%s'}".formatted(name, age, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && 
               name.equals(person.name) && 
               email.equals(person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }
}