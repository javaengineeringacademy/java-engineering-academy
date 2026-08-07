import java.util.Objects;

public final class Exercise1 {
    public static void main(String[] args) {
        Person alice = new Person("Alice", 30, "alice@example.com");
        Person olderAlice = alice.withAge(31);

        System.out.println("Original: " + alice);
        System.out.println("With new age: " + olderAlice);
        System.out.println("Same reference? " + (alice == olderAlice));
        System.out.println("Same content? " + alice.equals(olderAlice));
    }
}

/*
 * TODO: Implement the immutable Person class below.
 *
 * Requirements:
 * - All fields must be private and final
 * - No setter methods
 * - withAge() returns a NEW Person (does not modify this one)
 * - Proper equals(), hashCode(), toString()
 */
final class Person {
    private final String name;
    private final int age;
    private final String email;

    // TODO: Constructor

    // TODO: Getters

    // TODO: withAge method

    // TODO: equals, hashCode, toString
}
