package academy.javaengineering.oop.classes;

/**
 * Demonstrates Java class definition with fields, constructors, and getters/setters.
 *
 * <p>A class encapsulates data (fields) and behavior (methods).
 * Proper access control via getters/setters protects internal state.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Private fields for encapsulation</li>
 *   <li>Multiple constructors (default, parameterized)</li>
 *   <li>Getter and setter methods with validation</li>
 *   <li>toString(), equals(), hashCode() overrides</li>
 * </ul>
 */
public class Person {

    private String name;
    private int age;
    private String email;

    /** Default constructor. */
    public Person() {
        this.name = "Unknown";
        this.age = 0;
        this.email = "";
    }

    /** Parameterized constructor. */
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }

    /** Checks if person is an adult. */
    public boolean isAdult() {
        return age >= 18;
    }

    @Override
    public String toString() {
        return "Person{name='%s', age=%d, email='%s'}".formatted(name, age, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person other)) return false;
        return age == other.age
                && java.util.Objects.equals(name, other.name)
                && java.util.Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age, email);
    }
}
