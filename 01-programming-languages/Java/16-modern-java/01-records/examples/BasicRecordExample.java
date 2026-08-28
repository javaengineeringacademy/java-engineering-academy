package academy.javaengineering.modern.records;

import java.util.List;

/**
 * Basic record examples demonstrating core functionality.
 */
public class BasicRecordExample {

    // Simple record
    public record Point(int x, int y) {}

    // Record with validation
    public record PositiveNumber(int value) {
        public PositiveNumber {
            if (value < 0) throw new IllegalArgumentException("Must be positive: " + value);
        }
    }

    // Record with multiple components
    public record Person(String firstName, String lastName, int age) {
        public String fullName() {
            return firstName + " " + lastName;
        }
    }

    public static void main(String[] args) {
        // Create records
        var point = new Point(10, 20);
        var person = new Person("John", "Doe", 30);

        // Accessor methods (not getters!)
        System.out.println("Point x: " + point.x());
        System.out.println("Point y: " + point.y());

        // toString
        System.out.println("Point: " + point);
        System.out.println("Person: " + person);

        // equals and hashCode
        var point2 = new Point(10, 20);
        System.out.println("point.equals(point2): " + point.equals(point2));
        System.out.println("point.hashCode() == point2.hashCode(): " + 
            (point.hashCode() == point2.hashCode()));

        // Validation in compact constructor
        try {
            new PositiveNumber(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation: " + e.getMessage());
        }

        // Record with custom method
        System.out.println("Full name: " + person.fullName());

        // Records in collections
        List<Point> points = List.of(
            new Point(1, 2),
            new Point(3, 4),
            new Point(5, 6)
        );
        System.out.println("Points: " + points);
    }
}
