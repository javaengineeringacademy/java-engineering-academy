package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Record Patterns (Java 16+) ===\n");

        // WHY: Records are concise immutable data carriers
        // INTERNAL: Compiler generates constructor, getters, equals, hashCode, toString
        // ENGINEERING: Use for DTOs, value objects, immutable data

        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = new Point(10, 20);

        System.out.println("p1: " + p1);
        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1.equals(p3): " + p1.equals(p3));
        System.out.println("Distance: " + String.format("%.2f", p1.distanceTo(p3)));

        // Record with validation
        Person person = new Person("Alice", 30);
        System.out.println("\nPerson: " + person);

        // TRADE-OFF: Records vs Lombok vs hand-written
        // Records: built-in, immutable, concise
        // Lombok: more features (@Builder, @Data), external dependency
        // Hand-written: full control, verbose
    }
}

record Point(int x, int y) {
    // Compact constructor for validation
    Point {
        if (x < 0 || y < 0) throw new IllegalArgumentException("Coordinates must be non-negative");
    }

    // Custom method
    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}

record Person(String name, int age) {
    Person {
        if (age < 0 || age > 150) throw new IllegalArgumentException("Invalid age: " + age);
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
    }
}
