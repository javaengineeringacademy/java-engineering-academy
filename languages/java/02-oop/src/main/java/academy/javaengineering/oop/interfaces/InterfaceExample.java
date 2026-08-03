package academy.javaengineering.oop.interfaces;

import java.util.List;
import java.util.Objects;

/**
 * Demonstrates interfaces including default methods, static methods,
 * and functional interfaces.
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Interface definition and implementation</li>
 *   <li>Default methods (Java 8+)</li>
 *   <li>Static methods in interfaces</li>
 *   <li>Functional interfaces with {@code @FunctionalInterface}</li>
 *   <li>Multiple interface implementation</li>
 *   <li>Interface inheritance</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class InterfaceExample {

    // ==================== Basic Interface ====================

    /**
     * Interface for entities that can be validated.
     */
    public interface Validatable {
        List<String> validate();

        /** Default method - can be overridden but provides behavior. */
        default boolean isValid() {
            return validate().isEmpty();
        }

        /** Static factory method for creating validation results. */
        static Validatable alwaysValid() {
            return List::isEmpty;
        }
    }

    // ==================== Interface Inheritance ====================

    /**
     * Base persistable interface.
     */
    public interface Persistable {
        Object getId();
        boolean isPersisted();

        default String getEntityType() {
            return getClass().getSimpleName();
        }
    }

    /**
     * Auditable extends Persistable - interface inheritance.
     */
    public interface Auditable extends Persistable {
        String getCreatedBy();
        java.time.Instant getCreatedAt();

        default String auditSummary() {
            return "%s[%s] created by %s at %s".formatted(
                    getEntityType(), getId(), getCreatedBy(), getCreatedAt());
        }
    }

    // ==================== Multiple Interface Implementation ====================

    /**
     * Enterprise product entity implementing multiple interfaces.
     */
    public static class Product implements Auditable, Validatable {
        private final long id;
        private String name;
        private double price;
        private String category;
        private final String createdBy;
        private final java.time.Instant createdAt;

        public Product(long id, String name, double price, String category, String createdBy) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.category = category;
            this.createdBy = createdBy;
            this.createdAt = java.time.Instant.now();
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }

        @Override
        public Object getId() { return id; }

        @Override
        public boolean isPersisted() { return id > 0; }

        @Override
        public String getCreatedBy() { return createdBy; }

        @Override
        public java.time.Instant getCreatedAt() { return createdAt; }

        @Override
        public List<String> validate() {
            var errors = new java.util.ArrayList<String>();
            if (name == null || name.isBlank()) errors.add("Name is required");
            if (price < 0) errors.add("Price must be non-negative");
            if (category == null || category.isBlank()) errors.add("Category is required");
            return errors;
        }

        @Override
        public String toString() {
            return "Product{id=%d, name='%s', price=%.2f}".formatted(id, name, price);
        }
    }

    // ==================== Functional Interfaces ====================

    /**
     * Functional interface for transforming product data.
     */
    @FunctionalInterface
    public interface ProductTransformer<T> {
        T transform(Product product);
    }

    /**
     * Functional interface for filtering products.
     */
    @FunctionalInterface
    public interface ProductFilter {
        boolean matches(Product product);

        /** Negate this filter. */
        default ProductFilter negate() {
            return product -> !matches(product);
        }

        /** Combine with another filter using AND. */
        default ProductFilter and(ProductFilter other) {
            return product -> matches(product) && other.matches(product);
        }
    }

    /**
     * Utility class using functional interfaces.
     */
    public static class ProductCatalog {
        private final java.util.List<Product> products = new java.util.ArrayList<>();

        public void add(Product product) { products.add(product); }

        public List<Product> filter(ProductFilter filter) {
            return products.stream().filter(filter::matches).toList();
        }

        public <T> List<T> map(ProductTransformer<T> transformer) {
            return products.stream().map(transformer::transform).toList();
        }

        public int count(ProductFilter filter) {
            return (int) products.stream().filter(filter::matches).count();
        }
    }

    // ==================== Default Method in Action ====================

    public static class Order implements Auditable {
        private final long id;
        private final String customerName;
        private final String createdBy;

        public Order(long id, String customerName, String createdBy) {
            this.id = id;
            this.customerName = customerName;
            this.createdBy = createdBy;
        }

        @Override public Object getId() { return id; }
        @Override public boolean isPersisted() { return true; }
        @Override public String getCreatedBy() { return createdBy; }
        @Override public java.time.Instant getCreatedAt() { return java.time.Instant.now(); }

        public String getCustomerName() { return customerName; }

        @Override
        public String getEntityType() { return "Order"; } // Override default
    }

    public static void main(String[] args) {
        System.out.println("=== Interfaces Demo ===\n");

        // Basic interface with default methods
        System.out.println("--- Validatable Interface ---");
        Product validProduct = new Product(1, "Widget", 29.99, "Electronics", "admin");
        Product invalidProduct = new Product(2, "", -5.0, "", "admin");

        System.out.println("Valid product valid? " + validProduct.isValid());
        System.out.println("Invalid product errors: " + invalidProduct.validate());

        // Multiple interface implementation
        System.out.println("\n--- Multiple Interface Implementation ---");
        System.out.println(validProduct.auditSummary());

        // Functional interfaces
        System.out.println("\n--- Functional Interfaces ---");
        ProductCatalog catalog = new ProductCatalog();
        catalog.add(new Product(1, "Laptop", 999.99, "Electronics", "admin"));
        catalog.add(new Product(2, "Mouse", 29.99, "Electronics", "admin"));
        catalog.add(new Product(3, "Desk", 199.99, "Furniture", "user1"));
        catalog.add(new Product(4, "Chair", 149.99, "Furniture", "user1"));
        catalog.add(new Product(5, "Keyboard", 79.99, "Electronics", "admin"));

        // Method references as functional interface implementations
        ProductFilter electronicsFilter = p -> "Electronics".equals(p.getCategory());
        ProductFilter expensiveFilter = p -> p.getPrice() > 100;

        System.out.println("Electronics: " + catalog.filter(electronicsFilter));
        System.out.println("Expensive: " + catalog.filter(expensiveFilter));
        System.out.println("Expensive Electronics: " + catalog.filter(expensiveFilter.and(electronicsFilter)));

        // Transform with lambda
        List<String> names = catalog.map(p -> p.getName().toUpperCase());
        System.out.println("Names: " + names);

        List<Double> prices = catalog.map(Product::getPrice);
        System.out.println("Prices: " + prices);

        // Interface inheritance
        System.out.println("\n--- Interface Inheritance ---");
        Order order = new Order(1001, "Alice", "system");
        System.out.println(order.auditSummary());
        System.out.println("Entity type: " + order.getEntityType());
    }
}
