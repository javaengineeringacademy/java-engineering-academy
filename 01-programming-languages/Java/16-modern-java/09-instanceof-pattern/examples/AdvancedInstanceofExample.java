package academy.javaengineering.modern.instanceofpattern;

import java.util.List;

/**
 * Advanced instanceof pattern matching usage.
 */
public class AdvancedInstanceofExample {

    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    public static void main(String[] args) {
        // Pattern matching with sealed types
        System.out.println("=== Pattern Matching with Sealed Types ===");
        List<Shape> shapes = List.of(
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 8),
            new Circle(2)
        );

        for (Shape shape : shapes) {
            if (shape instanceof Circle c && c.radius() > 3) {
                System.out.println("Large circle: radius=" + c.radius());
            } else if (shape instanceof Rectangle r && r.width() == r.height()) {
                System.out.println("Square: side=" + r.width());
            } else if (shape instanceof Triangle t && t.base() == t.height()) {
                System.out.println("Isosceles right triangle");
            } else if (shape instanceof Shape s) {
                System.out.println("Other shape: " + s.getClass().getSimpleName());
            }
        }

        // Pattern matching with nested objects
        System.out.println("\n=== Pattern Matching with Nested Objects ===");
        record Person(String name, int age, List<String> hobbies) {}
        List<Object> people = List.of(
            new Person("Alice", 30, List.of("Reading", "Hiking")),
            new Person("Bob", 25, List.of("Gaming")),
            "Not a person",
            new Person("Charlie", 35, List.of())
        );

        for (Object obj : people) {
            if (obj instanceof Person p && p.age() > 25 && !p.hobbies().isEmpty()) {
                System.out.println("Adult with hobbies: " + p.name());
            } else if (obj instanceof Person p && p.age() <= 25) {
                System.out.println("Young person: " + p.name());
            } else if (obj instanceof Person p) {
                System.out.println("Person without hobbies: " + p.name());
            } else {
                System.out.println("Not a person: " + obj);
            }
        }

        // Pattern matching in method calls
        System.out.println("\n=== Pattern Matching in Method Calls ===");
        Object[] inputs = {"Hello", 42, List.of(1, 2, 3), null};
        for (Object input : inputs) {
            System.out.println(processObject(input));
        }

        // Pattern with complex conditions
        System.out.println("\n=== Pattern with Complex Conditions ===");
        String[] emails = {"user@example.com", "admin@test.org", "invalid-email", "user+tag@domain.com"};
        for (String email : emails) {
            if (email instanceof String e && e.contains("@") && e.length() > 10) {
                System.out.println("Valid email: " + e);
            } else if (email instanceof String e) {
                System.out.println("Invalid email: " + e);
            } else {
                System.out.println("Not a string");
            }
        }
    }

    static String processObject(Object obj) {
        if (obj instanceof String s && s.length() > 5) {
            return "Long string: " + s;
        } else if (obj instanceof String s) {
            return "Short string: " + s;
        } else if (obj instanceof Integer i && i > 100) {
            return "Large integer: " + i;
        } else if (obj instanceof Integer i) {
            return "Small integer: " + i;
        } else if (obj instanceof List<?> list && !list.isEmpty()) {
            return "Non-empty list: " + list.size() + " elements";
        } else if (obj instanceof List<?>) {
            return "Empty list";
        } else if (obj == null) {
            return "Null value";
        } else {
            return "Unknown: " + obj.getClass().getSimpleName();
        }
    }
}
