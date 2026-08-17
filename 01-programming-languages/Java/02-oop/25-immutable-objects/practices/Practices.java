package academy.javaengineering.oop.practices;

import java.util.Objects;

/**
 * Practice: Immutable Objects in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating immutable classes (final class, final fields)
 * - Defensive copying in constructor and getter
 * - Returning new objects from methods instead of mutating
 * - Understanding why immutable objects are thread-safe
 * - Proper equals() and hashCode() for immutable objects
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 25-immutable-objects ===\n");

        // Test Exercise 1: Create an ImmutablePoint
        ImmutablePoint p1 = new ImmutablePoint(3, 4);
        System.out.println("Exercise 1 - ImmutablePoint creation: "
            + (p1.getX() == 3 && p1.getY() == 4 ? "PASS" : "FAIL"));

        // Test Exercise 2: translate() returns a NEW point, does not mutate
        ImmutablePoint p2 = p1.translate(2, 3);
        System.out.println("Exercise 2 - translate() returns new object: "
            + (p2.getX() == 5 && p2.getY() == 7 && p1.getX() == 3 && p1.getY() == 4 ? "PASS" : "FAIL"));

        // Test Exercise 3: equals() and hashCode() work correctly
        ImmutablePoint p3 = new ImmutablePoint(5, 7);
        System.out.println("Exercise 3 - equals/hashCode: "
            + (p2.equals(p3) && p2.hashCode() == p3.hashCode() ? "PASS" : "FAIL"));

        // Test Exercise 4: toString() is meaningful
        String str = p1.toString();
        System.out.println("Exercise 4 - toString(): "
            + (str.contains("3") && str.contains("4") ? "PASS" : "FAIL"));

        // Test Exercise 5: Immutability - fields cannot change
        // If the class is final and fields are final, this test ensures correctness
        System.out.println("Exercise 5 - Immutability guarantee: "
            + (p1.getX() == 3 ? "PASS" : "FAIL"));
    }
}

/**
 * TODO 1: Complete ImmutablePoint so it is truly immutable:
 * - Make the class final
 * - Make all fields private final
 * - Constructor initializes x and y
 * - Getters only (no setters)
 * - translate(dx, dy) returns a NEW ImmutablePoint
 * - Override equals() and hashCode()
 * - Override toString() to return "Point{x=3, y=4}"
 */
final class ImmutablePoint {
    // YOUR CODE HERE: fields, constructor, methods
}
