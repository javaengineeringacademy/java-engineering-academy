/**
 * RecordPatternsDemo.java
 *
 * Demonstrates record patterns (deconstruction) in Java 16+.
 * Record patterns allow you to destructure records in pattern matching.
 *
 * Compile with: javac RecordPatternsDemo.java
 * Run with: java RecordPatternsDemo
 *
 * Expected Output:
 * === Record Patterns (Deconstruction) ===
 *
 * --- 1. Basic Record Pattern ---
 * Circle: radius=5.0
 * Rectangle: width=4.0, height=6.0
 * Triangle: base=3.0, height=8.0
 *
 * --- 2. Nested Record Patterns ---
 * Person: name=Alice, address=Street=123 Main St, City=Springfield, State=IL
 * Company: name=Acme, headquarters=Street=456 Oak Ave, City=Chicago, State=IL
 *
 * --- 3. Record Patterns in Switch ---
 * Shape is Circle: radius=5.0, area=78.54
 * Shape is Rectangle: width=4.0, height=6.0, area=24.00
 * Shape is Triangle: base=3.0, height=8.0, area=12.00
 *
 * --- 4. Guarded Record Patterns ---
 * Small circle with radius 2.0
 * Large circle with radius 10.0
 * Tiny circle with radius 0.5
 *
 * --- 5. Record Patterns with sealed interfaces ---
 * Payment: CreditCard ending in 1111, amount=$100.00
 * Payment: DebitCard ending in 2222, amount=$50.00
 * Payment: BankTransfer account ACC-12345, amount=$200.00
 *
 * --- 6. Destructuring Complex Records ---
 * Order: id=ORD-001, customer=Alice, total=$150.00
 *   Item: Widget, quantity=2, price=$9.99
 *   Item: Gadget, quantity=1, price=$29.99
 *   Item: Doohickey, quantity=3, price=$4.99
 *
 * --- 7. Record Patterns in Stream Operations ---
 * Processing circle with radius 2.0
 * Processing rectangle with width 3.0 and height 4.0
 * Processing triangle with base 5.0 and height 6.0
 * Processing circle with radius 7.0
 * Processing rectangle with width 8.0 and height 9.0
 */
import java.util.*;
import java.util.stream.*;

public class RecordPatternsDemo {

    // =====================================================
    // 1. Basic Record Pattern
    // =====================================================
    // Record patterns destructure records in pattern matching

    public sealed interface Shape permits Circle, Rectangle, Triangle {
        double area();
    }

    public record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    public record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }

    public record Triangle(double base, double height) implements Shape {
        @Override
        public double area() {
            return 0.5 * base * height;
        }
    }

    // =====================================================
    // 2. Nested Record Patterns
    // =====================================================
    // Record patterns can destructure nested records

    public record Address(String street, String city, String state) {
    }

    public record Person(String name, Address address) {
    }

    public record Company(String name, Address headquarters) {
    }

    // =====================================================
    // 3. Record Patterns with Sealed Interfaces
    // =====================================================
    // Record patterns work with sealed hierarchies

    public sealed interface PaymentMethod permits CreditCard, DebitCard, BankTransfer {
    }

    public record CreditCard(String cardNumber, double limit) implements PaymentMethod {
    }

    public record DebitCard(String cardNumber, double balance) implements PaymentMethod {
    }

    public record BankTransfer(String accountNumber) implements PaymentMethod {
    }

    public record Payment(PaymentMethod method, double amount) {
    }

    // =====================================================
    // 4. Complex Record for Destructuring
    // =====================================================
    // Records can contain other records

    public record OrderItem(String name, int quantity, double price) {
        public double totalPrice() {
            return quantity * price;
        }
    }

    public record Order(String id, String customer, List<OrderItem> items) {
        public double total() {
            return items.stream()
                    .mapToDouble(OrderItem::totalPrice)
                    .sum();
        }
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Record Patterns (Deconstruction) ===\n");

        basicRecordPattern();
        nestedRecordPatterns();
        recordPatternsInSwitch();
        guardedRecordPatterns();
        recordPatternsWithSealedInterfaces();
        destructuringComplexRecords();
        recordPatternsInStreamOperations();

        System.out.println("\n=== Complete ===");
    }

    // =====================================================
    // Demo Methods
    // =====================================================

    private static void basicRecordPattern() {
        System.out.println("--- 1. Basic Record Pattern ---");

        Shape[] shapes = {
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 8.0)
        };

        for (Shape shape : shapes) {
            // Record pattern destructures the record
            if (shape instanceof Circle c) {
                System.out.printf("Circle: radius=%.1f%n", c.radius());
            } else if (shape instanceof Rectangle r) {
                System.out.printf("Rectangle: width=%.1f, height=%.1f%n",
                        r.width(), r.height());
            } else if (shape instanceof Triangle t) {
                System.out.printf("Triangle: base=%.1f, height=%.1f%n",
                        t.base(), t.height());
            }
        }

        System.out.println();
    }

    private static void nestedRecordPatterns() {
        System.out.println("--- 2. Nested Record Patterns ---");

        Person person = new Person("Alice", new Address("123 Main St", "Springfield", "IL"));
        Company company = new Company("Acme", new Address("456 Oak Ave", "Chicago", "IL"));

        // Nested record pattern destructuring
        if (person instanceof Person(String name, Address(String street, String city, String state))) {
            System.out.printf("Person: name=%s, address=%s=%s, City=%s, State=%s%n",
                    name, "Street", street, city, state);
        }

        if (company instanceof Company(String name, Address(String street, String city, String state))) {
            System.out.printf("Company: name=%s, headquarters=%s=%s, City=%s, State=%s%n",
                    name, "Street", street, city, state);
        }

        System.out.println();
    }

    private static void recordPatternsInSwitch() {
        System.out.println("--- 3. Record Patterns in Switch ---");

        Shape[] shapes = {
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 8.0)
        };

        for (Shape shape : shapes) {
            // Record patterns in switch expressions
            String description = switch (shape) {
                case Circle c -> String.format("Shape is Circle: radius=%.1f, area=%.2f",
                        c.radius(), c.area());
                case Rectangle r -> String.format("Shape is Rectangle: width=%.1f, height=%.1f, area=%.2f",
                        r.width(), r.height(), r.area());
                case Triangle t -> String.format("Shape is Triangle: base=%.1f, height=%.1f, area=%.2f",
                        t.base(), t.height(), t.area());
            };
            System.out.println(description);
        }

        System.out.println();
    }

    private static void guardedRecordPatterns() {
        System.out.println("--- 4. Guarded Record Patterns ---");

        Shape[] circles = {
                new Circle(2.0),
                new Circle(10.0),
                new Circle(0.5)
        };

        for (Shape shape : circles) {
            // Guarded record patterns
            String description = switch (shape) {
                case Circle c when c.radius() < 1.0 -> "Tiny circle with radius " + c.radius();
                case Circle c when c.radius() <= 5.0 -> "Small circle with radius " + c.radius();
                case Circle c -> "Large circle with radius " + c.radius();
                default -> "Not a circle";
            };
            System.out.println(description);
        }

        System.out.println();
    }

    private static void recordPatternsWithSealedInterfaces() {
        System.out.println("--- 5. Record Patterns with Sealed Interfaces ---");

        Payment[] payments = {
                new Payment(new CreditCard("4111-1111-1111-1111", 5000.00), 100.00),
                new Payment(new DebitCard("5222-2222-2222-2222", 1500.00), 50.00),
                new Payment(new BankTransfer("ACC-12345"), 200.00)
        };

        for (Payment payment : payments) {
            // Record pattern with nested sealed interface pattern
            if (payment instanceof Payment(PaymentMethod method, double amount)) {
                String description = switch (method) {
                    case CreditCard cc -> String.format("CreditCard ending in %s, amount=$%.2f",
                            cc.cardNumber().substring(cc.cardNumber().length() - 4), amount);
                    case DebitCard dc -> String.format("DebitCard ending in %s, amount=$%.2f",
                            dc.cardNumber().substring(dc.cardNumber().length() - 4), amount);
                    case BankTransfer bt -> String.format("BankTransfer account %s, amount=$%.2f",
                            bt.accountNumber(), amount);
                };
                System.out.println("Payment: " + description);
            }
        }

        System.out.println();
    }

    private static void destructuringComplexRecords() {
        System.out.println("--- 6. Destructuring Complex Records ---");

        Order order = new Order("ORD-001", "Alice", List.of(
                new OrderItem("Widget", 2, 9.99),
                new OrderItem("Gadget", 1, 29.99),
                new OrderItem("Doohickey", 3, 4.99)
        ));

        // Destructure the order
        if (order instanceof Order(String id, String customer, List<OrderItem> items)) {
            System.out.printf("Order: id=%s, customer=%s, total=$%.2f%n",
                    id, customer, order.total());

            // Destructure each item
            for (OrderItem item : items) {
                if (item instanceof OrderItem(String name, int quantity, double price)) {
                    System.out.printf("  Item: %s, quantity=%d, price=$%.2f%n",
                            name, quantity, price);
                }
            }
        }

        System.out.println();
    }

    private static void recordPatternsInStreamOperations() {
        System.out.println("--- 7. Record Patterns in Stream Operations ---");

        List<Shape> shapes = List.of(
                new Circle(2.0),
                new Rectangle(3.0, 4.0),
                new Triangle(5.0, 6.0),
                new Circle(7.0),
                new Rectangle(8.0, 9.0)
        );

        // Using record patterns in stream operations
        shapes.stream()
                .filter(shape -> shape instanceof Circle || shape instanceof Rectangle)
                .forEach(shape -> {
                    if (shape instanceof Circle c) {
                        System.out.printf("Processing circle with radius %.1f%n", c.radius());
                    } else if (shape instanceof Rectangle r) {
                        System.out.printf("Processing rectangle with width %.1f and height %.1f%n",
                                r.width(), r.height());
                    }
                });

        System.out.println();
    }
}
