package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Sealed Classes (Java 17+) ===\n");

        // WHY: Sealed classes control inheritance hierarchy, enable exhaustive pattern matching
        // INTERNAL: JVM enforces permitted subclasses at bytecode level
        // ENGINEERING: Use for domain modeling with fixed set of subtypes

        Shape circle = new CircleShape(5.0);
        Shape rect = new RectShape(4.0, 6.0);
        Shape triangle = new TriShape(3.0, 4.0, 5.0);

        System.out.println("Circle area: " + calculateArea(circle));
        System.out.println("Rect area: " + calculateArea(rect));
        System.out.println("Tri area: " + calculateArea(triangle));

        // TRADE-OFF: Sealed vs enum
        // Sealed: can have behavior, state, more flexible
        // Enum: simpler, fixed instances, pattern matching friendly
    }

    static double calculateArea(Shape shape) {
        // Java 21+ pattern matching with sealed classes
        if (shape instanceof CircleShape c) {
            return Math.PI * c.radius() * c.radius();
        } else if (shape instanceof RectShape r) {
            return r.width() * r.height();
        } else if (shape instanceof TriShape t) {
            return 0.5 * t.base() * t.height();
        }
        throw new IllegalArgumentException("Unknown shape");
    }
}

sealed interface Shape permits CircleShape, RectShape, TriShape {}

record CircleShape(double radius) implements Shape {}
record RectShape(double width, double height) implements Shape {}
record TriShape(double base, double height) implements Shape {}
