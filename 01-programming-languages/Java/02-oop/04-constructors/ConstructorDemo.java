/**
 * Demonstrates default, parameterized, and copy constructors.
 *
 * <p>Constructors initialize objects when they are created.
 * They have the same name as the class and no return type.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Default constructor (no parameters)</li>
 *   <li>Parameterized constructor</li>
 *   <li>Copy constructor (creates deep copy)</li>
 *   <li>Constructor chaining with {@code this()}</li>
 * </ul>
 */
public class ConstructorDemo {

    private final String name;
    private final int age;
    private final String[] hobbies;

    /** Default constructor. */
    public ConstructorDemo() {
        this("Unknown", 0, new String[0]);
    }

    /** Parameterized constructor with two fields. */
    public ConstructorDemo(String name, int age) {
        this(name, age, new String[0]);
    }

    /** Full parameterized constructor. */
    public ConstructorDemo(String name, int age, String[] hobbies) {
        this.name = name;
        this.age = age;
        // Defensive copy for array immutability
        this.hobbies = hobbies != null ? hobbies.clone() : new String[0];
    }

    /** Copy constructor - creates independent deep copy. */
    public ConstructorDemo(ConstructorDemo other) {
        this(other.name, other.age, other.hobbies);
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String[] getHobbies() { return hobbies.clone(); }

    public boolean hasHobbies() {
        return hobbies.length > 0;
    }

    @Override
    public String toString() {
        return "ConstructorDemo{name='%s', age=%d, hobbies=%d}".formatted(
                name, age, hobbies.length);
    }
}