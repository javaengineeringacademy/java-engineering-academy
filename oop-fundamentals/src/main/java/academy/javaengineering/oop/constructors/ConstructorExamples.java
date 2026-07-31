package academy.javaengineering.oop.constructors;

/**
 * Demonstrates various constructor patterns.
 */
public final class ConstructorExamples {

    private final String name;
    private int age;
    private String address;

    /**
     * Primary constructor with all parameters.
     */
    public ConstructorExamples(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    /**
     * Constructor chaining - delegates to primary constructor.
     */
    public ConstructorExamples(String name, int age) {
        this(name, age, "Unknown");
    }

    /**
     * Convenience constructor chaining to two-parameter constructor.
     */
    public ConstructorExamples(String name) {
        this(name, 0);
    }

    // Copy constructor
    public ConstructorExamples(ConstructorExamples other) {
        this.name = other.name;
        this.age = other.age;
        this.address = other.address;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getAddress() { return address; }

    public static void main(String[] args) {
        ConstructorExamples e1 = new ConstructorExamples("Alice", 30, "123 Main St");
        ConstructorExamples e2 = new ConstructorExamples("Bob", 25);
        ConstructorExamples e3 = new ConstructorExamples("Charlie");
        ConstructorExamples copy = new ConstructorExamples(e1);

        System.out.println("e1: " + e1.getName() + ", " + e1.getAge() + ", " + e1.getAddress());
        System.out.println("e2: " + e2.getName() + ", " + e2.getAge() + ", " + e2.getAddress());
        System.out.println("e3: " + e3.getName() + ", " + e3.getAge() + ", " + e3.getAddress());
        System.out.println("Copy of e1: " + copy.getName() + ", " + copy.getAge());
    }
}