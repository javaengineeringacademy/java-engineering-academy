package academy.javaengineering.oop.object-copying;

/**
 * Demonstrates builder-based copy pattern.
 *
 * <p>The builder pattern provides a flexible way to create copies of objects,
 * especially when objects have many fields. You can modify specific fields
 * during the copy process.</p>
 *
 * <p>Expected output:</p>
 * <pre>
 * === Builder Copy Pattern ===
 * Original: Order{id=1, customer='Alice', amount=299.99, items=[Widget, Gadget]}
 * Full copy: Order{id=1, customer='Alice', amount=299.99, items=[Widget, Gadget]}
 * Modified copy: Order{id=2, customer='Bob', amount=499.99, items=[Widget, Gadget]}
 * original == fullCopy: false
 * original.items == modifiedCopy.items: false
 * After modifiedCopy.items().add("Doohickey"):
 * Original items: [Widget, Gadget]
 * Modified items: [Widget, Gadget, Doohickey]
 * </pre>
 */
public class BuilderCopyExample {

  public static void main(String[] args) {
    System.out.println("=== Builder Copy Pattern ===");

    Order original = new Order(1, "Alice", 299.99, java.util.List.of("Widget", "Gadget"));
    Order fullCopy = original.toBuilder().build();
    Order modifiedCopy =
        original.toBuilder().id(2).customer("Bob").amount(499.99).build();

    System.out.println("Original: " + original);
    System.out.println("Full copy: " + fullCopy);
    System.out.println("Modified copy: " + modifiedCopy);
    System.out.println("original == fullCopy: " + (original == fullCopy));
    System.out.println("original.items == modifiedCopy.items: " + (original.items() == modifiedCopy.items()));

    modifiedCopy.items().add("Doohickey");

    System.out.println("After modifiedCopy.items().add(\"Doohickey\"):");
    System.out.println("Original items: " + original.items());
    System.out.println("Modified items: " + modifiedCopy.items());
  }

  static class Order {
    private final int id;
    private final String customer;
    private final double amount;
    private final java.util.List<String> items;

    Order(int id, String customer, double amount, java.util.List<String> items) {
      this.id = id;
      this.customer = customer;
      this.amount = amount;
      this.items = new java.util.ArrayList<>(items);
    }

    private Order(Builder builder) {
      this.id = builder.id;
      this.customer = builder.customer;
      this.amount = builder.amount;
      this.items = new java.util.ArrayList<>(builder.items);
    }

    int id() {
      return id;
    }

    String customer() {
      return customer;
    }

    double amount() {
      return amount;
    }

    java.util.List<String> items() {
      return items;
    }

    Builder toBuilder() {
      return new Builder(this);
    }

    @Override
    public String toString() {
      return "Order{id=" + id + ", customer='" + customer + "', amount=" + amount
          + ", items=" + items + "}";
    }

    static class Builder {
      private int id;
      private String customer;
      private double amount;
      private java.util.List<String> items;

      Builder() {}

      Builder(Order order) {
        this.id = order.id;
        this.customer = order.customer;
        this.amount = order.amount;
        this.items = new java.util.ArrayList<>(order.items);
      }

      Builder id(int id) {
        this.id = id;
        return this;
      }

      Builder customer(String customer) {
        this.customer = customer;
        return this;
      }

      Builder amount(double amount) {
        this.amount = amount;
        return this;
      }

      Builder items(java.util.List<String> items) {
        this.items = new java.util.ArrayList<>(items);
        return this;
      }

      Order build() {
        return new Order(this);
      }
    }
  }
}
