package academy.javaengineering.modern.sealed;

/**
 * Basic sealed class examples.
 */
public class BasicSealedExample {

    // Sealed interface with records
    public sealed interface Shape permits Circle, Rectangle, Triangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Triangle(double base, double height) implements Shape {}

    // Sealed class hierarchy
    public sealed abstract class Animal permits Dog, Cat, Bird {}
    public final class Dog extends Animal {
        private final String name;
        public Dog(String name) { this.name = name; }
        public String name() { return name; }
    }
    public sealed class Cat extends Animal permits DomesticCat {}
    public final class DomesticCat extends Cat {
        private final String name;
        public DomesticCat(String name) { this.name = name; }
        public String name() { return name; }
    }
    public non-sealed class Bird extends Animal {
        private final String name;
        public Bird(String name) { this.name = name; }
        public String name() { return name; }
    }

    public static void main(String[] args) {
        // Shape examples
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);
        Shape triangle = new Triangle(3, 8);

        System.out.println("Shape areas:");
        System.out.println("Circle: " + calculateArea(circle));
        System.out.println("Rectangle: " + calculateArea(rectangle));
        System.out.println("Triangle: " + calculateArea(triangle));

        // Animal examples
        Animal dog = new Dog("Rex");
        Animal cat = new DomesticCat("Whiskers");
        Animal bird = new Bird("Tweety");

        System.out.println("\nAnimal sounds:");
        System.out.println("Dog: " + getSound(dog));
        System.out.println("Cat: " + getSound(cat));
        System.out.println("Bird: " + getSound(bird));

        // Exhaustive checking with pattern matching
        System.out.println("\nPattern matching:");
        describeShape(circle);
        describeShape(rectangle);
        describeShape(triangle);
    }

    static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
            case Triangle t -> 0.5 * t.base() * t.height();
        };
    }

    static String getSound(Animal animal) {
        return switch (animal) {
            case Dog d -> "Woof";
            case DomesticCat c -> "Meow";
            case Bird b -> "Tweet";
        };
    }

    static void describeShape(Shape shape) {
        String description = switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
            case Triangle t -> "Triangle " + t.base() + "x" + t.height();
        };
        System.out.println("  " + description);
    }
}
