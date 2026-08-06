import java.util.*;
import java.util.stream.*;

/**
 * Records in Java (Java 16+)
 * Declaration, compact constructors, custom methods, and use cases.
 */
public class Records {

    // --- Basic record declaration ---
    record User(String username, String email, int age) {}

    // --- Record with compact constructor ---
    record Product(String name, double price, int quantity) {
        Compact constructor for validation and normalization
        Product {
            Objects.requireNonNull(name, "Name cannot be null");
            if (name.isBlank()) {
                throw new IllegalArgumentException("Name cannot be blank");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            // Compact constructor can modify final fields
            name = name.trim().toLowerCase();
        }
    }

    // --- Record with custom methods ---
    record Rectangle(double width, double height) {
        // Computed property
        double area() {
            return width * height;
        }

        double perimeter() {
            return 2 * (width + height);
        }

        boolean isSquare() {
            return Math.abs(width - height) < 0.001;
        }

        // Static factory
        static Rectangle square(double side) {
            return new Rectangle(side, side);
        }

        static Rectangle fromPoints(double x1, double y1, double x2, double y2) {
            return new Rectangle(Math.abs(x2 - x1), Math.abs(y2 - y1));
        }

        // Override toString
        @Override
        public String toString() {
            return String.format("Rectangle[%.1f x %.1f, area=%.1f]",
                width, height, area());
        }
    }

    // --- Record implementing interface ---
    interface Printable {
        String print();
    }

    interface Serializable {
        Map<String, Object> toMap();
    }

    record Invoice(String id, String customer, double amount, List<String> items)
            implements Printable, Serializable {

        Invoice {
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

        double discountedAmount(double percent) {
            return amount * (1 - percent / 100);
        }
    }

    // --- Record with generic components ---
    record Pair<A, B>(A first, B second) {
        <C> Pair<A, C> mapSecond(java.util.function.Function<B, C> mapper) {
            return new Pair<>(first, mapper.apply(second));
        }

        <C> Pair<C, B> mapFirst(java.util.function.Function<A, C> mapper) {
            return new Pair<>(mapper.apply(first), second);
        }
    }

    // --- Record as DTO ---
    record ApiResponse<T>(int status, String message, T data, long timestamp) {
        static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(200, "OK", data, System.currentTimeMillis());
        }

        static <T> ApiResponse<T> error(int status, String message) {
            return new ApiResponse<>(status, message, null, System.currentTimeMillis());
        }

        boolean isSuccess() {
            return status >= 200 && status < 300;
        }
    }

    // --- Record vs Lombok @Value ---
    /*
     * Lombok @Value (before records):
     *
     * @Value
     * public class User {
     *     String username;
     *     String email;
     *     int age;
     * }
     *
     * Record equivalent:
     * record User(String username, String email, int age) {}
     *
     * Records provide:
     * - Auto-generated equals, hashCode, toString
     * - Canonical constructor
     * - Accessor methods (field() instead of getField())
     * - Immutability (all fields final)
     * - No Lombok dependency required
     */

    public static void main(String[] args) {
        System.out.println("=== Records in Java ===\n");

        basicDeclaration();
        compactConstructor();
        customMethods();
        recordsAsDTOs();
        genericRecords();
        recordComparison();

        System.out.println("\n=== Complete ===");
    }

    static void basicDeclaration() {
        System.out.println("--- Basic Record Declaration ---");

        User user = new User("alice", "alice@example.com", 30);

        // Accessor methods
        System.out.println("Username: " + user.username());
        System.out.println("Email: " + user.email());
        System.out.println("Age: " + user.age());

        // Auto-generated methods
        System.out.println("toString: " + user);
        System.out.println("hashCode: " + user.hashCode());

        User user2 = new User("alice", "alice@example.com", 30);
        System.out.println("equals: " + user.equals(user2));

        // Records are immutable
        // user.username = "bob"; // Compile error

        System.out.println();
    }

    static void compactConstructor() {
        System.out.println("--- Compact Constructor ---");

        // Validation and normalization
        Product p1 = new Product("  Widget  ", 9.99, 100);
        System.out.println("Product: " + p1.name() + " - " + p1.price());

        Product p2 = new Product("GADGET", 19.99, 50);
        System.out.println("Product: " + p2.name() + " - " + p2.price());

        // Validation works
        try {
            Product invalid = new Product("", -1, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation caught: " + e.getMessage());
        }

        System.out.println();
    }

    static void customMethods() {
        System.out.println("--- Custom Methods ---");

        Rectangle r1 = new Rectangle(5, 10);
        Rectangle r2 = Rectangle.square(7);
        Rectangle r3 = Rectangle.fromPoints(0, 0, 3, 4);

        System.out.println("r1: " + r1);
        System.out.println("r2: " + r2 + " is square: " + r2.isSquare());
        System.out.println("r3: " + r3);
        System.out.println("r1 perimeter: " + r1.perimeter());

        System.out.println();
    }

    static void recordsAsDTOs() {
        System.out.println("--- Records as DTOs ---");

        // API response
        ApiResponse<List<String>> success = ApiResponse.success(List.of("a", "b"));
        System.out.println("Success: " + success);
        System.out.println("Is success: " + success.isSuccess());

        ApiResponse<Void> error = ApiResponse.error(404, "Not found");
        System.out.println("Error: " + error);

        // Invoice
        Invoice inv = new Invoice("INV-001", "Acme Corp", 1500.00,
            List.of("Widget", "Gadget", "Doohickey"));
        System.out.println(inv.print());
        System.out.println("To map: " + inv.toMap());
        System.out.println("Discounted: $" + inv.discountedAmount(10));

        System.out.println();
    }

    static void genericRecords() {
        System.out.println("--- Generic Records ---");

        Pair<String, Integer> pair = new Pair<>("hello", 42);
        System.out.println("Pair: " + pair);

        Pair<String, Integer> mapped = pair.mapSecond(String::valueOf);
        System.out.println("Mapped: " + mapped);

        Pair<String, Integer> mappedFirst = pair.mapFirst(String::toUpperCase);
        System.out.println("Mapped first: " + mappedFirst);

        System.out.println();
    }

    static void recordComparison() {
        System.out.println("--- Record vs Lombok @Value vs Manual ---");

        // Record approach (modern Java)
        record PointRecord(int x, int y) {}
        PointRecord pr1 = new PointRecord(1, 2);
        PointRecord pr2 = new PointRecord(1, 2);
        System.out.println("Record equals: " + pr1.equals(pr2));

        // Stream operations with records
        List<User> users = List.of(
            new User("alice", "a@test.com", 25),
            new User("bob", "b@test.com", 30),
            new User("charlie", "c@test.com", 35)
        );

        List<String> usernames = users.stream()
            .map(User::username)
            .sorted()
            .toList();
        System.out.println("Sorted usernames: " + usernames);

        // Records in sets/maps work with value semantics
        Set<PointRecord> pointSet = new HashSet<>(List.of(
            new PointRecord(1, 2),
            new PointRecord(1, 2),
            new PointRecord(3, 4)
        ));
        System.out.println("Unique points in set: " + pointSet.size()); // 2

        System.out.println();
    }
}
