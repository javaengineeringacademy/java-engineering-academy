package academy.javaengineering.oop.encapsulation;

/**
 * Person - Encapsulated class demonstrating data hiding with validation.
 * 
 * <p>Shows proper encapsulation with:
 * <ul>
 *   <li>Private fields</li>
 *   <li>Public getters with computed properties</li>
 *   <li>Setters with validation logic</li>
 *   <li>Read-only fields (no setter)</li>
 *   <li>Write-only fields (no getter)</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Person {

    private final String name;        // Immutable - set once in constructor
    private int age;                   // Mutable - with validation
    private String phoneNumber;        // Read/Write
    private String ssn;                // Write-only (set but can't read back)
    private final long createdTimestamp; // Read-only - set once, never changes

    public Person(String name, int age) {
        this.name = name;
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        this.age = age;
        this.createdTimestamp = System.currentTimeMillis();
    }

    // Getter - read access
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // Setter with validation - controlled write access
    public boolean setAge(int age) {
        if (age < 0 || age > 150) {
            System.out.println("  Rejected: Age must be between 0 and 150");
            return false;
        }
        this.age = age;
        return true;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Write-only (no getter for SSN - security)
    public void setSSN(String ssn) {
        this.ssn = ssn; // Store but never expose
    }

    // Read-only (no setter for timestamp)
    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    // Computed property via getter
    public String getAgeCategory() {
        if (age < 13) return "Child";
        if (age < 18) return "Teenager";
        if (age < 65) return "Adult";
        return "Senior";
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}