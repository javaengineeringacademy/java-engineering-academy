package academy.javaengineering.modern.recordpatterns;

import java.util.List;

/**
 * Record patterns with real-world patterns.
 */
public class RecordPatternsRealWorld {

    // Domain model
    record Person(String name, int age, Address address) {}
    record Address(String street, String city, String zip) {}
    record Order(String id, Person customer, List<OrderItem> items) {}
    record OrderItem(String name, int quantity, double price) {}

    // API response
    sealed interface ApiResponse permits Success, Error, Redirect {}
    record Success(int status, Object data) implements ApiResponse {}
    record Error(int status, String message) implements ApiResponse {}
    record Redirect(String url) implements ApiResponse {}

    public static void main(String[] args) {
        // Person with nested address
        System.out.println("=== Person with Address ===");
        Person person = new Person("Alice", 30, new Address("123 Main St", "Springfield", "62701"));
        if (person instanceof Person(String name, int age, Address(String street, String city, String zip))) {
            System.out.printf("Name: %s, Age: %d%n", name, age);
            System.out.printf("Address: %s, %s %s%n", street, city, zip);
        }

        // Order processing
        System.out.println("\n=== Order Processing ===");
        Order order = new Order(
            "ORD-001",
            new Person("Bob", 25, new Address("456 Oak Ave", "Chicago", "60601")),
            List.of(
                new OrderItem("Laptop", 1, 999.99),
                new OrderItem("Mouse", 2, 29.99)
            )
        );

        if (order instanceof Order(String id, Person(String name, var age, Address(String street, var city, var zip)), List<OrderItem> items)) {
            System.out.printf("Order %s for %s%n", id, name);
            System.out.printf("Shipping to: %s, %s %s%n", street, city, zip);
            System.out.println("Items:");
            for (OrderItem item : items) {
                System.out.printf("  %s x%d: $%.2f%n", item.name(), item.quantity(), item.price());
            }
        }

        // API response handling
        System.out.println("\n=== API Response Handling ===");
        List<ApiResponse> responses = List.of(
            new Success(200, "OK"),
            new Error(404, "Not Found"),
            new Redirect("/login"),
            new Success(200, List.of("item1", "item2"))
        );

        for (ApiResponse response : responses) {
            if (response instanceof Success(int status, Object data) && status == 200) {
                System.out.println("Success: " + data);
            } else if (response instanceof Error(int status, String message)) {
                System.out.printf("Error %d: %s%n", status, message);
            } else if (response instanceof Redirect(String url)) {
                System.out.println("Redirect to: " + url);
            } else {
                System.out.println("Unknown response");
            }
        }

        // Complex validation
        System.out.println("\n=== Complex Validation ===");
        Person[] people = {
            new Person("Alice", 30, new Address("123 Main St", "Springfield", "62701")),
            new Person("Bob", 15, new Address("456 Oak Ave", "Chicago", "60601")),
            new Person("Charlie", 25, new Address("789 Pine Rd", "", "62702"))
        };

        for (Person p : people) {
            if (p instanceof Person(String name, int age, Address(String street, String city, String zip))
                    && age >= 18 && !city.isEmpty() && !zip.isEmpty()) {
                System.out.printf("Valid adult: %s from %s%n", name, city);
            } else if (p instanceof Person(String name, int age, var addr) && age < 18) {
                System.out.println("Minor: " + name);
            } else if (p instanceof Person(String name, var age, Address(String street, String city, var zip))
                    && city.isEmpty()) {
                System.out.println("Missing city for: " + name);
            }
        }
    }
}
