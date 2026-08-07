/**
 * RecordsAdvancedDemo.java
 *
 * Demonstrates advanced features of records in Java 16+.
 * Includes records with methods, canonical constructors, and more.
 *
 * Compile with: javac RecordsAdvancedDemo.java
 * Run with: java RecordsAdvancedDemo
 *
 * Expected Output:
 * === Advanced Records (Java 16+) ===
 *
 * --- 1. Record with Custom Methods ---
 * Circle: radius=5.0, area=78.54, circumference=31.42
 * Circle is large: true
 *
 * --- 2. Canonical Constructor for Validation ---
 * Product: name=Widget, price=$9.99, quantity=100
 * Product: name=Gadget, price=$19.99, quantity=50
 * Validation caught: Price cannot be negative
 *
 * --- 3. Record with Static Factory Methods ---
 * Point at (3.0, 4.0)
 * Distance from origin: 5.00
 * Midpoint: Point[x=2.5, y=3.5]
 *
 * --- 4. Record Implementing Multiple Interfaces ---
 * Invoice: id=INV-001, customer=Acme Corp, amount=$1500.00
 * Invoice print: Invoice INV-001: Acme Corp ($1500.00) - 3 items
 * Invoice to map: {id=INV-001, customer=Acme Corp, amount=1500.0, items=[Widget, Gadget, Doohickey]}
 *
 * --- 5. Record with Generics ---
 * Pair: first=hello, second=42
 * Mapped pair: first=HELLO, second=84
 *
 * --- 6. Record as DTO ---
 * API Response: status=200, message=OK, data=[a, b, c]
 * Is success: true
 *
 * --- 7. Record Equality and Hashing ---
 * User 1: User[username=alice, email=alice@example.com, age=30]
 * User 2: User[username=alice, email=alice@example.com, age=30]
 * User 1 equals User 2: true
 * User 1 hashCode == User 2 hashCode: true
 *
 * --- 8. Record in Collections ---
 * Sorted usernames: [alice, bob, charlie]
 * Unique points in set: 2
 */
import java.util.*;
import java.util.stream.*;

public class RecordsAdvancedDemo {

    // =====================================================
    // 1. Record with Custom Methods
    // =====================================================
    // Records can have custom methods beyond the auto-generated ones

    public record Circle(double radius) {
        // Computed properties
        public double area() {
            return Math.PI * radius * radius;
        }

        public double circumference() {
            return 2 * Math.PI * radius;
        }

        public double diameter() {
            return 2 * radius;
        }

        // Custom behavior
        public boolean isLarge() {
            return radius > 5.0;
        }

        // Static factory method
        public static Circle unit() {
            return new Circle(1.0);
        }

        // Override toString
        @Override
        public String toString() {
            return String.format("Circle: radius=%.1f, area=%.2f, circumference=%.2f",
                    radius, area(), circumference());
        }
    }

    // =====================================================
    // 2. Canonical Constructor for Validation
    // =====================================================
    // Compact constructor allows validation and normalization

    public record Product(String name, double price, int quantity) {
        // Compact constructor for validation and normalization
        public Product {
            Objects.requireNonNull(name, "Name cannot be null");
            if (name.isBlank()) {
                throw new IllegalArgumentException("Name cannot be blank");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            // Compact constructor can modify final fields
            name = name.trim().toLowerCase();
        }

        // Custom methods
        public double totalPrice() {
            return price * quantity;
        }

        public Product withQuantity(int newQuantity) {
            return new Product(name, price, newQuantity);
        }
    }

    // =====================================================
    // 3. Record with Static Factory Methods
    // =====================================================
    // Records can have static factory methods for creation

    public record Point(double x, double y) {
        // Static factory methods
        public static Point origin() {
            return new Point(0, 0);
        }

        public static Point of(double x, double y) {
            return new Point(x, y);
        }

        // Custom methods
        public double distanceTo(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        public double distanceFromOrigin() {
            return distanceTo(origin());
        }

        public Point midpoint(Point other) {
            return new Point((x + other.x) / 2, (y + other.y) / 2);
        }

        // Override toString
        @Override
        public String toString() {
            return String.format("Point[x=%.1f, y=%.1f]", x, y);
        }
    }

    // =====================================================
    // 4. Record Implementing Multiple Interfaces
    // =====================================================
    // Records can implement multiple interfaces

    public interface Printable {
        String print();
    }

    public interface Serializable {
        Map<String, Object> toMap();
    }

    public record Invoice(String id, String customer, double amount, List<String> items)
            implements Printable, Serializable {

        public Invoice {
            Objects.requireNonNull(id);
            Objects.requireNonNull(customer);
            if (amount < 0) throw new IllegalArgumentException("Amount negative");
            items = List.copyOf(items); // Defensive copy
        }

        @Override
        public String print() {
            return String.format("Invoice %s: %s ($%.2f) - %d items",
                    id, customer, amount, items.size());
        }

        @Override
        public Map<String, Object> toMap() {
            return Map.of(
                    "id", id,
                    "customer", customer,
                    "amount", amount,
                    "items", items
            );
        }

        public double discountedAmount(double percent) {
            return amount * (1 - percent / 100);
        }
    }

    // =====================================================
    // 5. Record with Generics
    // =====================================================
    // Records can be generic

    public record Pair<A, B>(A first, B second) {
        public <C> Pair<A, C> mapSecond(java.util.function.Function<B, C> mapper) {
            return new Pair<>(first, mapper.apply(second));
        }

        public <C> Pair<C, B> mapFirst(java.util.function.Function<A, C> mapper) {
            return new Pair<>(mapper.apply(first), second);
        }
    }

    // =====================================================
    // 6. Record as DTO
    // =====================================================
    // Records are perfect for Data Transfer Objects

    public record ApiResponse<T>(int status, String message, T data, long timestamp) {
        public static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(200, "OK", data, System.currentTimeMillis());
        }

        public static <T> ApiResponse<T> error(int status, String message) {
            return new ApiResponse<>(status, message, null, System.currentTimeMillis());
        }

        public boolean isSuccess() {
            return status >= 200 && status < 300;
        }
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Advanced Records (Java 16+) ===\n");

        recordWithCustomMethods();
        canonicalConstructorForValidation();
        recordWithStaticFactoryMethods();
        recordImplementingMultipleInterfaces();
        recordWithGenerics();
        recordAsDTO();
        recordEqualityAndHashing();
        recordInCollections();

        System.out.println("\n=== Complete ===");
    }

    // =====================================================
    // Demo Methods
    // =====================================================

    private static void recordWithCustomMethods() {
        System.out.println("--- 1. Record with Custom Methods ---");

        Circle circle1 = new Circle(5.0);
        Circle circle2 = Circle.unit(); // Static factory method

        System.out.println(circle1);
        System.out.println("Circle is large: " + circle1.isLarge());
        System.out.println("Unit circle: " + circle2);

        System.out.println();
    }

    private static void canonicalConstructorForValidation() {
        System.out.println("--- 2. Canonical Constructor for Validation ---");

        Product p1 = new Product("  Widget  ", 9.99, 100);
        Product p2 = new Product("GADGET", 19.99, 50);

        System.out.println("Product: " + p1.name() + ", price=$" + p1.price() +
                ", quantity=" + p1.quantity());
        System.out.println("Product: " + p2.name() + ", price=$" + p2.price() +
                ", quantity=" + p2.quantity());

        // Validation works
        try {
            Product invalid = new Product("", -1, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation caught: " + e.getMessage());
        }

        System.out.println();
    }

    private static void recordWithStaticFactoryMethods() {
        System.out.println("--- 3. Record with Static Factory Methods ---");

        Point p1 = Point.of(3.0, 4.0);
        Point p2 = Point.origin();
        Point p3 = p1.midpoint(new Point(2.0, 3.0));

        System.out.println("Point at " + p1);
        System.out.printf("Distance from origin: %.2f%n", p1.distanceFromOrigin());
        System.out.println("Midpoint: " + p3);

        System.out.println();
    }

    private static void recordImplementingMultipleInterfaces() {
        System.out.println("--- 4. Record Implementing Multiple Interfaces ---");

        Invoice inv = new Invoice("INV-001", "Acme Corp", 1500.00,
                List.of("Widget", "Gadget", "Doohickey"));

        System.out.println("Invoice: id=" + inv.id() + ", customer=" + inv.customer() +
                ", amount=$" + inv.amount());
        System.out.println("Invoice print: " + inv.print());
        System.out.println("Invoice to map: " + inv.toMap());
        System.out.printf("Discounted: $%.2f%n", inv.discountedAmount(10));

        System.out.println();
    }

    private static void recordWithGenerics() {
        System.out.println("--- 5. Record with Generics ---");

        Pair<String, Integer> pair = new Pair<>("hello", 42);
        Pair<String, Integer> mappedFirst = pair.mapFirst(String::toUpperCase);
        Pair<String, Integer> mappedSecond = pair.mapSecond(i -> i * 2);

        System.out.println("Pair: first=" + pair.first() + ", second=" + pair.second());
        System.out.println("Mapped pair: first=" + mappedFirst.first() +
                ", second=" + mappedFirst.second());

        System.out.println();
    }

    private static void recordAsDTO() {
        System.out.println("--- 6. Record as DTO ---");

        ApiResponse<List<String>> success = ApiResponse.success(List.of("a", "b", "c"));
        ApiResponse<Void> error = ApiResponse.error(404, "Not found");

        System.out.println("API Response: status=" + success.status() +
                ", message=" + success.message() +
                ", data=" + success.data());
        System.out.println("Is success: " + success.isSuccess());

        System.out.println();
    }

    private static void recordEqualityAndHashing() {
        System.out.println("--- 7. Record Equality and Hashing ---");

        record User(String username, String email, int age) {}

        User user1 = new User("alice", "alice@example.com", 30);
        User user2 = new User("alice", "alice@example.com", 30);

        System.out.println("User 1: " + user1);
        System.out.println("User 2: " + user2);
        System.out.println("User 1 equals User 2: " + user1.equals(user2));
        System.out.println("User 1 hashCode == User 2 hashCode: " +
                (user1.hashCode() == user2.hashCode()));

        System.out.println();
    }

    private static void recordInCollections() {
        System.out.println("--- 8. Record in Collections ---");

        record User(String username, String email, int age) {}
        record Point(int x, int y) {}

        List<User> users = List.of(
                new User("alice", "a@test.com", 25),
                new User("bob", "b@test.com", 30),
                new User("charlie", "c@test.com", 35)
        );

        // Using records with streams
        List<String> usernames = users.stream()
                .map(User::username)
                .sorted()
                .toList();
        System.out.println("Sorted usernames: " + usernames);

        // Records in sets work with value semantics
        Set<Point> pointSet = new HashSet<>(List.of(
                new Point(1, 2),
                new Point(1, 2),
                new Point(3, 4)
        ));
        System.out.println("Unique points in set: " + pointSet.size());

        System.out.println();
    }
}
