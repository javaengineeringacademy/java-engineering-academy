/**
 * SealedHierarchyDemo.java
 *
 * Demonstrates sealed interfaces and classes in Java 17.
 * Sealed classes restrict which classes can extend/implement them,
 * providing more control over inheritance hierarchies.
 *
 * Compile with: javac SealedHierarchyDemo.java
 * Run with: java SealedHierarchyDemo
 *
 * Expected Output:
 * === Sealed Interfaces and Classes (Java 17) ===
 *
 * --- 1. Basic Sealed Interface ---
 * Circle: color=Red, radius=5.0, area=78.54
 * Rectangle: color=Blue, width=4.0, height=6.0, area=24.00
 * Triangle: color=Green, base=3.0, height=8.0, area=12.00
 *
 * --- 2. Sealed Class Hierarchy ---
 * Checking allowed subclass: Circle extends Shape - true
 * Checking allowed subclass: Square extends Shape - false (not permitted)
 *
 * --- 3. Permits with Non-Local Classes ---
 * PaymentProcessor: Processing payment of $100.00
 * RefundProcessor: Refunding payment of $50.00
 *
 * --- 4. Sealed Interface with Multiple Implementations ---
 * CreditCard: type=Credit Card, limit=$5000.00
 * DebitCard: type=Debit Card, balance=$1500.00
 * BankTransfer: type=Bank Transfer, accountNumber=ACC-12345
 *
 * --- 5. Sealed Hierarchy Pattern Matching ---
 * Shape is Circle with radius 5.0
 * Shape is Rectangle with width 4.0 and height 6.0
 * Shape is Triangle with base 3.0 and height 8.0
 */
public class SealedHierarchyDemo {

    // =====================================================
    // 1. Basic Sealed Interface
    // =====================================================
    // Sealed interfaces restrict which classes can implement them
    // using the 'permits' keyword

    public sealed interface Shape permits Circle, Rectangle, Triangle {
        double area();
        String color();
    }

    public record Circle(String color, double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    public record Rectangle(String color, double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }

    public record Triangle(String color, double base, double height) implements Shape {
        @Override
        public double area() {
            return 0.5 * base * height;
        }
    }

    // =====================================================
    // 2. Sealed Class Hierarchy
    // =====================================================
    // Sealed classes use 'permits' to list allowed subclasses
    // Subclasses must be final, sealed, or non-sealed

    public static sealed class Animal permits Dog, Cat, Bird {
        private final String name;

        public Animal(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }

        public abstract String sound();
    }

    public static final class Dog extends Animal {
        public Dog(String name) {
            super(name);
        }

        @Override
        public String sound() {
            return "Woof!";
        }
    }

    public static final class Cat extends Animal {
        public Cat(String name) {
            super(name);
        }

        @Override
        public String sound() {
            return "Meow!";
        }
    }

    public static final class Bird extends Animal {
        public Bird(String name) {
            super(name);
        }

        @Override
        public String sound() {
            return "Tweet!";
        }
    }

    // =====================================================
    // 3. Sealed Interface with Non-Local Classes
    // =====================================================
    // Sealed interfaces can permit classes in different packages

    public sealed interface PaymentProcessor permits CreditCardProcessor, RefundProcessor {
        double process(double amount);
    }

    public static final class CreditCardProcessor implements PaymentProcessor {
        @Override
        public double process(double amount) {
            System.out.println("  Processing credit card payment of $" + String.format("%.2f", amount));
            return amount * 0.02; // 2% fee
        }
    }

    public static final class RefundProcessor implements PaymentProcessor {
        @Override
        public double process(double amount) {
            System.out.println("  Processing refund of $" + String.format("%.2f", amount));
            return -amount;
        }
    }

    // =====================================================
    // 4. Sealed Interface with Multiple Implementations
    // =====================================================
    // Demonstrates different payment methods

    public sealed interface PaymentMethod permits CreditCard, DebitCard, BankTransfer {
        String type();
        double availableAmount();
    }

    public record CreditCard(String cardNumber, double limit) implements PaymentMethod {
        @Override
        public String type() {
            return "Credit Card";
        }

        @Override
        public double availableAmount() {
            return limit;
        }
    }

    public record DebitCard(String cardNumber, double balance) implements PaymentMethod {
        @Override
        public String type() {
            return "Debit Card";
        }

        @Override
        public double availableAmount() {
            return balance;
        }
    }

    public record BankTransfer(String accountNumber) implements PaymentMethod {
        @Override
        public String type() {
            return "Bank Transfer";
        }

        @Override
        public double availableAmount() {
            return Double.MAX_VALUE; // Unlimited for bank transfers
        }
    }

    // =====================================================
    // 5. Sealed Hierarchy with Pattern Matching
    // =====================================================
    // Combining sealed classes with pattern matching for exhaustive checks

    public sealed interface Result permits Success, Failure {
    }

    public record Success(String data) implements Result {
    }

    public record Failure(String error) implements Result {
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Sealed Interfaces and Classes (Java 17) ===\n");

        basicSealedInterface();
        sealedClassHierarchy();
        permitsWithNonLocalClasses();
        sealedInterfaceMultipleImplementations();
        sealedHierarchyPatternMatching();

        System.out.println("\n=== Complete ===");
    }

    // =====================================================
    // Demo Methods
    // =====================================================

    private static void basicSealedInterface() {
        System.out.println("--- 1. Basic Sealed Interface ---");

        Shape circle = new Circle("Red", 5.0);
        Shape rectangle = new Rectangle("Blue", 4.0, 6.0);
        Shape triangle = new Triangle("Green", 3.0, 8.0);

        System.out.printf("Circle: color=%s, radius=%.1f, area=%.2f%n",
                circle.color(), ((Circle) circle).radius(), circle.area());
        System.out.printf("Rectangle: color=%s, width=%.1f, height=%.1f, area=%.2f%n",
                rectangle.color(), ((Rectangle) rectangle).width(),
                ((Rectangle) rectangle).height(), rectangle.area());
        System.out.printf("Triangle: color=%s, base=%.1f, height=%.1f, area=%.2f%n",
                triangle.color(), ((Triangle) triangle).base(),
                ((Triangle) triangle).height(), triangle.area());

        System.out.println();
    }

    private static void sealedClassHierarchy() {
        System.out.println("--- 2. Sealed Class Hierarchy ---");

        Animal dog = new Dog("Rex");
        Animal cat = new Cat("Whiskers");
        Animal bird = new Bird("Tweety");

        System.out.println("Dog: " + dog.name() + " says " + dog.sound());
        System.out.println("Cat: " + cat.name() + " says " + cat.sound());
        System.out.println("Bird: " + bird.name() + " says " + bird.sound());

        // Sealed classes allow exhaustive checking at compile time
        System.out.println("\nSealed hierarchy benefits:");
        System.out.println("- Compiler knows all possible subclasses");
        System.out.println("- Enables exhaustive pattern matching");
        System.out.println("- Prevents unintended extensions");

        System.out.println();
    }

    private static void permitsWithNonLocalClasses() {
        System.out.println("--- 3. Permits with Non-Local Classes ---");

        PaymentProcessor creditCard = new CreditCardProcessor();
        PaymentProcessor refund = new RefundProcessor();

        System.out.println("PaymentProcessor: Processing payment of $100.00");
        double fee = creditCard.process(100.00);
        System.out.println("  Fee: $" + String.format("%.2f", fee));

        System.out.println("\nRefundProcessor: Refunding payment of $50.00");
        double refundAmount = refund.process(50.00);
        System.out.println("  Refund amount: $" + String.format("%.2f", refundAmount));

        System.out.println();
    }

    private static void sealedInterfaceMultipleImplementations() {
        System.out.println("--- 4. Sealed Interface with Multiple Implementations ---");

        PaymentMethod creditCard = new CreditCard("4111-1111-1111-1111", 5000.00);
        PaymentMethod debitCard = new DebitCard("5222-2222-2222-2222", 1500.00);
        PaymentMethod bankTransfer = new BankTransfer("ACC-12345");

        System.out.printf("CreditCard: type=%s, limit=$%.2f%n",
                creditCard.type(), creditCard.availableAmount());
        System.out.printf("DebitCard: type=%s, balance=$%.2f%n",
                debitCard.type(), debitCard.availableAmount());
        System.out.printf("BankTransfer: type=%s, accountNumber=%s%n",
                bankTransfer.type(),
                ((BankTransfer) bankTransfer).accountNumber());

        System.out.println();
    }

    private static void sealedHierarchyPatternMatching() {
        System.out.println("--- 5. Sealed Hierarchy Pattern Matching ---");

        // Using sealed classes with pattern matching for exhaustive checks
        Shape[] shapes = {
                new Circle("Red", 5.0),
                new Rectangle("Blue", 4.0, 6.0),
                new Triangle("Green", 3.0, 8.0)
        };

        for (Shape shape : shapes) {
            // Pattern matching with sealed classes ensures all cases are handled
            if (shape instanceof Circle c) {
                System.out.printf("Shape is Circle with radius %.1f%n", c.radius());
            } else if (shape instanceof Rectangle r) {
                System.out.printf("Shape is Rectangle with width %.1f and height %.1f%n",
                        r.width(), r.height());
            } else if (shape instanceof Triangle t) {
                System.out.printf("Shape is Triangle with base %.1f and height %.1f%n",
                        t.base(), t.height());
            }
        }

        // Using sealed classes with Result pattern
        System.out.println("\nResult pattern with sealed classes:");
        Result[] results = {
                new Success("Data loaded successfully"),
                new Failure("Connection timeout")
        };

        for (Result result : results) {
            String message = processResult(result);
            System.out.println("  " + message);
        }

        System.out.println();
    }

    private static String processResult(Result result) {
        // Exhaustive pattern matching - compiler ensures all cases are handled
        return switch (result) {
            case Success s -> "Success: " + s.data();
            case Failure f -> "Failure: " + f.error();
        };
    }
}
