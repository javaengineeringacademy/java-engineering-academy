package academy.javaengineering.oop.`14-solid-principles`;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates all five SOLID principles with enterprise patterns.
 *
 * <h3>SOLID Principles:</h3>
 * <ul>
 *   <li><b>S</b>ingle Responsibility: One reason to change</li>
 *   <li><b>O</b>pen/Closed: Open for extension, closed for modification</li>
 *   <li><b>L</b>iskov Substitution: Subtypes must be substitutable</li>
 *   <li><b>I</b>nterface Segregation: Many specific interfaces</li>
 *   <li><b>D</b>ependency Inversion: Depend on abstractions</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SOLIDExample {

    // ==================== S - Single Responsibility ====================

    /** Each class has ONE reason to change. */
    public static class User {
        private final String name;
        private final String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    /** Only responsible for user validation. */
    public static class UserValidator {
        public boolean isValid(User user) {
            return user.getName() != null && !user.getName().isBlank()
                    && user.getEmail() != null && user.getEmail().contains("@");
        }
    }

    /** Only responsible for user persistence. */
    public static class UserRepository {
        private final List<User> users = new ArrayList<>();

        public void save(User user) { users.add(user); }
        public List<User> findAll() { return new ArrayList<>(users); }
    }

    // ==================== O - Open/Closed ====================

    /** Open for extension (new discounts), closed for modification. */
    public interface DiscountStrategy {
        double calculate(double amount);
        String getDiscountType();
    }

    public static class NoDiscount implements DiscountStrategy {
        @Override
        public double calculate(double amount) { return 0; }

        @Override
        public String getDiscountType() { return "None"; }
    }

    public static class PercentageDiscount implements DiscountStrategy {
        private final double percentage;

        public PercentageDiscount(double percentage) { this.percentage = percentage; }

        @Override
        public double calculate(double amount) { return amount * percentage / 100; }

        @Override
        public String getDiscountType() { return "Percentage (%.1f%%)".formatted(percentage); }
    }

    public static class FixedAmountDiscount implements DiscountStrategy {
        private final double fixedAmount;

        public FixedAmountDiscount(double fixedAmount) { this.fixedAmount = fixedAmount; }

        @Override
        public double calculate(double amount) { return Math.min(fixedAmount, amount); }

        @Override
        public String getDiscountType() { return "Fixed ($%.2f)".formatted(fixedAmount); }
    }

    /** New discount type added WITHOUT modifying existing code. */
    public static class BuyOneGetHalfOff implements DiscountStrategy {
        @Override
        public double calculate(double amount) { return amount * 0.25; }

        @Override
        public String getDiscountType() { return "Buy One Get Half Off"; }
    }

    public static class PriceCalculator {
        private final DiscountStrategy discountStrategy;

        public PriceCalculator(DiscountStrategy discountStrategy) {
            this.discountStrategy = discountStrategy;
        }

        public double calculateFinalPrice(double originalPrice) {
            double discount = discountStrategy.calculate(originalPrice);
            return originalPrice - discount;
        }
    }

    // ==================== L - Liskov Substitution ====================

    /** Base shape contract. */
    public interface Shape {
        double area();
        String getType();
    }

    /** Circle can substitute Shape without breaking behavior. */
    public static class Circle implements Shape {
        private final double radius;

        public Circle(double radius) { this.radius = radius; }

        @Override
        public double area() { return Math.PI * radius * radius; }

        @Override
        public String getType() { return "Circle"; }
    }

    /** Rectangle can substitute Shape without breaking behavior. */
    public static class Rectangle implements Shape {
        private final double width;
        private final double height;

        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public double area() { return width * height; }

        @Override
        public String getType() { return "Rectangle"; }
    }

    /** Square correctly extends Rectangle with consistent behavior. */
    public static class Square extends Rectangle {
        public Square(double side) { super(side, side); }

        @Override
        public String getType() { return "Square"; }
    }

    public static class ShapeAreaCalculator {
        public static double totalArea(Shape[] shapes) {
            double total = 0;
            for (Shape shape : shapes) {
                total += shape.area();
            }
            return total;
        }
    }

    // ==================== I - Interface Segregation ====================

    /** Segregated interfaces - each has ONE purpose. */
    public interface Readable {
        String read();
    }

    public interface Writable {
        void write(String content);
    }

    public interface CloseableResource {
        void close();
    }

    /** ReadOnlyFile only implements what it needs. */
    public static class ReadOnlyFile implements Readable {
        private final String content;

        public ReadOnlyFile(String content) { this.content = content; }

        @Override
        public String read() { return content; }
    }

    /** ReadWriteFile implements both read and write. */
    public static class ReadWriteFile implements Readable, Writable, CloseableResource {
        private String content = "";
        private boolean closed = false;

        @Override
        public String read() {
            if (closed) throw new IllegalStateException("File is closed");
            return content;
        }

        @Override
        public void write(String content) {
            if (closed) throw new IllegalStateException("File is closed");
            this.content = content;
        }

        @Override
        public void close() { closed = true; }
    }

    // ==================== D - Dependency Inversion ====================

    /** High-level module depends on abstraction, not concretion. */
    public interface NotificationSender {
        void send(String recipient, String message);
    }

    /** Low-level module implements abstraction. */
    public static class EmailSender implements NotificationSender {
        @Override
        public void send(String recipient, String message) {
            System.out.println("  [EMAIL] To: %s | Message: %s".formatted(recipient, message));
        }
    }

    public static class SmsSender implements NotificationSender {
        @Override
        public void send(String recipient, String message) {
            System.out.println("  [SMS] To: %s | Message: %s".formatted(recipient, message));
        }
    }

    /** High-level module depends on abstraction. */
    public static class OrderNotificationService {
        private final NotificationSender sender;

        public OrderNotificationService(NotificationSender sender) {
            this.sender = sender;
        }

        public void notifyOrderConfirmed(String customerEmail, String orderId) {
            sender.send(customerEmail, "Order %s confirmed!".formatted(orderId));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== SOLID Principles Demo ===\n");

        // S - Single Responsibility
        System.out.println("--- S: Single Responsibility ---");
        UserValidator validator = new UserValidator();
        UserRepository repo = new UserRepository();
        User user = new User("Alice", "alice@example.com");
        System.out.println("Valid: " + validator.isValid(user));
        repo.save(user);
        System.out.println("Saved users: " + repo.findAll().size());

        // O - Open/Closed
        System.out.println("\n--- O: Open/Closed ---");
        double price = 100.00;
        DiscountStrategy[] discounts = {
                new NoDiscount(),
                new PercentageDiscount(20),
                new FixedAmountDiscount(15),
                new BuyOneGetHalfOff()
        };
        for (DiscountStrategy d : discounts) {
            PriceCalculator calc = new PriceCalculator(d);
            System.out.printf("  %s: $%.2f -> $%.2f%n",
                    d.getDiscountType(), price, calc.calculateFinalPrice(price));
        }

        // L - Liskov Substitution
        System.out.println("\n--- L: Liskov Substitution ---");
        Shape[] shapes = {new Circle(5), new Rectangle(4, 6), new Square(3)};
        for (Shape s : shapes) {
            System.out.printf("  %s: area=%.2f%n", s.getType(), s.area());
        }
        System.out.println("  Total area: " + ShapeAreaCalculator.totalArea(shapes));

        // I - Interface Segregation
        System.out.println("\n--- I: Interface Segregation ---");
        ReadOnlyFile readOnly = new ReadOnlyFile("Read-only content");
        ReadWriteFile readWrite = new ReadWriteFile();
        readWrite.write("Read-write content");
        System.out.println("  ReadOnly: " + readOnly.read());
        System.out.println("  ReadWrite: " + readWrite.read());
        readWrite.close();
        System.out.println("  Closed file read attempt: ");
        try {
            readWrite.read();
        } catch (IllegalStateException e) {
            System.out.println("    " + e.getMessage());
        }

        // D - Dependency Inversion
        System.out.println("\n--- D: Dependency Inversion ---");
        OrderNotificationService emailNotif = new OrderNotificationService(new EmailSender());
        OrderNotificationService smsNotif = new OrderNotificationService(new SmsSender());
        emailNotif.notifyOrderConfirmed("alice@example.com", "ORD-001");
        smsNotif.notifyOrderConfirmed("+15551234567", "ORD-002");
    }
}
