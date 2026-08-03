package academy.javaengineering.oop.`01-classes-and-objects`;

/**
 * Demonstrates Java class definitions, object creation, and constructors.
 *
 * <p>A class is a blueprint for objects. It defines fields (state) and methods (behavior).
 * Objects are instances of classes created at runtime.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Class definition with fields and methods</li>
 *   <li>Object instantiation with {@code new}</li>
 *   <li>Default and parameterized constructors</li>
 *   <li>Reference variables and object identity</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ClassesAndObjects {

    /**
     * Enterprise example: A customer entity used in e-commerce systems.
     */
    public static class Customer {
        private long id;
        private String name;
        private String email;
        private String tier;

        /** Default constructor - initializes with default values. */
        public Customer() {
            this.id = 0L;
            this.name = "Unknown";
            this.email = "";
            this.tier = "STANDARD";
        }

        /**
         * Parameterized constructor for creating a fully initialized customer.
         *
         * @param id    unique identifier
         * @param name  customer full name
         * @param email customer email address
         * @param tier  loyalty tier (STANDARD, SILVER, GOLD, PLATINUM)
         */
        public Customer(long id, String name, String email, String tier) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.tier = tier;
        }

        /** Copy constructor - creates a deep copy of another Customer. */
        public Customer(Customer other) {
            this.id = other.id;
            this.name = other.name;
            this.email = other.email;
            this.tier = other.tier;
        }

        public long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getTier() { return tier; }

        public void setTier(String tier) { this.tier = tier; }

        /**
         * Checks if this customer qualifies for free shipping.
         *
         * @return {@code true} if GOLD or PLATINUM tier
         */
        public boolean qualifiesForFreeShipping() {
            return "GOLD".equals(tier) || "PLATINUM".equals(tier);
        }

        @Override
        public String toString() {
            return "Customer{id=%d, name='%s', email='%s', tier='%s'}".formatted(id, name, email, tier);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Customer other)) return false;
            return id == other.id;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(id);
        }
    }

    /**
     * Enterprise example: An order line item with immutable fields.
     */
    public static class OrderItem {
        private final String productId;
        private final String productName;
        private final int quantity;
        private final double unitPrice;

        public OrderItem(String productId, String productName, int quantity, double unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }

        public double getLineTotal() {
            return quantity * unitPrice;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Classes and Objects Demo ===\n");

        // Creating objects with different constructors
        Customer defaultCustomer = new Customer();
        System.out.println("Default customer: " + defaultCustomer);

        Customer premiumCustomer = new Customer(1001L, "Alice Johnson", "alice@example.com", "PLATINUM");
        System.out.println("Premium customer: " + premiumCustomer);

        Customer copiedCustomer = new Customer(premiumCustomer);
        System.out.println("Copied customer:  " + copiedCustomer);

        // Object identity vs equality
        System.out.println("\n== operator: " + (premiumCustomer == copiedCustomer));
        System.out.println(".equals():    " + premiumCustomer.equals(copiedCustomer));

        // Method behavior
        System.out.println("\nFree shipping eligible: " + premiumCustomer.qualifiesForFreeShipping());

        // Working with immutable objects
        OrderItem item1 = new OrderItem("SKU-001", "Wireless Mouse", 2, 29.99);
        OrderItem item2 = new OrderItem("SKU-002", "Mechanical Keyboard", 1, 149.99);

        System.out.println("\nItem: %s x%d @ $%.2f = $%.2f".formatted(
                item1.getProductName(), item1.getQuantity(),
                item1.getUnitPrice(), item1.getLineTotal()));
        System.out.println("Item: %s x%d @ $%.2f = $%.2f".formatted(
                item2.getProductName(), item2.getQuantity(),
                item2.getUnitPrice(), item2.getLineTotal()));

        double total = item1.getLineTotal() + item2.getLineTotal();
        System.out.printf("Order total: $%.2f%n", total);
    }
}
