package academy.javaengineering.functional.optional;

import java.util.Optional;

/**
 * Comprehensive examples of Optional in Java 21.
 *
 * <p>This class demonstrates all aspects of Optional including creation,
 * operations, chaining, and integration with streams. Each example is
 * self-contained and can be run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Creating Optional instances</li>
 *   <li>Accessing values safely</li>
 *   <li>Optional operations (map, flatMap, filter)</li>
 *   <li>Optional chaining</li>
 *   <li>Integration with streams</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class OptionalExamples {

    private OptionalExamples() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates creating Optional instances.
     */
    public static void creatingOptional() {
        System.out.println("=== Creating Optional ===\n");

        // Empty Optional
        Optional<String> empty = Optional.empty();
        System.out.println("Empty is present: " + empty.isPresent());
        System.out.println("Empty value: " + empty.orElse("N/A"));

        // Optional with value
        Optional<String> present = Optional.of("Hello");
        System.out.println("Present is present: " + present.isPresent());
        System.out.println("Present value: " + present.get());

        // Optional with nullable
        Optional<String> nullable = Optional.ofNullable(null);
        Optional<String> nonNull = Optional.ofNullable("World");
        System.out.println("Nullable is present: " + nullable.isPresent());
        System.out.println("NonNull is present: " + nonNull.isPresent());
    }

    /**
     * Demonstrates accessing Optional values.
     */
    public static void accessingValues() {
        System.out.println("\n=== Accessing Values ===\n");

        Optional<String> present = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // get() - throws if empty
        System.out.println("Present get: " + present.get());

        // orElse() - returns default
        System.out.println("Present orElse: " + present.orElse("Default"));
        System.out.println("Empty orElse: " + empty.orElse("Default"));

        // orElseGet() - computes default
        System.out.println("Empty orElseGet: " + empty.orElseGet(() -> "Computed"));

        // orElseThrow() - throws exception
        try {
            empty.orElseThrow(() -> new RuntimeException("Missing value"));
        } catch (RuntimeException e) {
            System.out.println("Empty orElseThrow: " + e.getMessage());
        }
    }

    /**
     * Demonstrates Optional operations.
     */
    public static void optionalOperations() {
        System.out.println("\n=== Optional Operations ===\n");

        Optional<String> present = Optional.of("hello");
        Optional<String> empty = Optional.empty();

        // ifPresent
        present.ifPresent(value -> System.out.println("ifPresent: " + value));
        empty.ifPresent(value -> System.out.println("ifPresent: " + value));

        // ifPresentOrElse
        present.ifPresentOrElse(
            value -> System.out.println("Found: " + value),
            () -> System.out.println("Not found")
        );
        empty.ifPresentOrElse(
            value -> System.out.println("Found: " + value),
            () -> System.out.println("Not found")
        );

        // map
        Optional<Integer> length = present.map(String::length);
        System.out.println("Map length: " + length.orElse(0));

        // filter
        Optional<String> filtered = present.filter(s -> s.length() > 3);
        System.out.println("Filter > 3: " + filtered.orElse("N/A"));

        Optional<String> notFiltered = present.filter(s -> s.length() > 10);
        System.out.println("Filter > 10: " + notFiltered.orElse("N/A"));
    }

    /**
     * Demonstrates Optional chaining.
     */
    public static void optionalChaining() {
        System.out.println("\n=== Optional Chaining ===\n");

        record Address(String city, String zipCode) {}
        record User(String name, Address address) {}

        User userWithAddress = new User("Alice", new Address("New York", "10001"));
        User userWithoutAddress = new User("Bob", null);
        User nullUser = null;

        // Chain operations
        java.util.function.Function<User, Optional<String>> getCity = user ->
            Optional.ofNullable(user)
                .map(User::address)
                .map(Address::city);

        System.out.println("Alice's city: " + getCity.apply(userWithAddress).orElse("Unknown"));
        System.out.println("Bob's city: " + getCity.apply(userWithoutAddress).orElse("Unknown"));
        System.out.println("Null's city: " + getCity.apply(nullUser).orElse("Unknown"));
    }

    /**
     * Demonstrates Optional with streams.
     */
    public static void optionalWithStreams() {
        System.out.println("\n=== Optional with Streams ===\n");

        record Product(String name, Optional<String> discount) {}

        java.util.List<Product> products = java.util.List.of(
            new Product("Laptop", Optional.of("10%")),
            new Product("Phone", Optional.empty()),
            new Product("Tablet", Optional.of("5%"))
        );

        // Find products with discounts
        java.util.List<String> productsWithDiscount = products.stream()
            .filter(p -> p.discount().isPresent())
            .map(Product::name)
            .toList();
        System.out.println("Products with discount: " + productsWithDiscount);

        // Get all discounts using flatMap
        java.util.List<String> discounts = products.stream()
            .map(Product::discount)
            .flatMap(Optional::stream)
            .toList();
        System.out.println("Discounts: " + discounts);
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        creatingOptional();
        accessingValues();
        optionalOperations();
        optionalChaining();
        optionalWithStreams();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Use Optional for return types when value may be absent");
        System.out.println("2. Avoid Optional for fields and parameters");
        System.out.println("3. Use orElse, ifPresent instead of get()");
        System.out.println("4. Chain operations with map, flatMap, filter");
        System.out.println("5. Integrate with streams using Optional::stream");
    }
}
