package academy.javaengineering.oop.object-copying;

/**
 * Demonstrates Cloneable interface usage.
 *
 * <p>The {@link Cloneable} interface is a marker interface that indicates
 * {@link Object#clone()} may be called on an object without throwing
 * {@link CloneNotSupportedException}. Classes that implement Cloneable should
 * override the {@code clone()} method.</p>
 *
 * <p>Expected output:</p>
 * <pre>
 * === Cloneable Interface Usage ===
 * Original: Product{name='Laptop', price=999.99, tags=[Electronics, Computer]}
 * Cloned: Product{name='Laptop', price=999.99, tags=[Electronics, Computer]}
 * original == cloned: false
 * original.tags == cloned.tags: false
 * After cloned.tags().add("Gaming"):
 * Original tags: [Electronics, Computer]
 * Cloned tags: [Electronics, Computer, Gaming]
 * </pre>
 */
public class CloneableExample {

  public static void main(String[] args) {
    System.out.println("=== Cloneable Interface Usage ===");

    Product original = new Product("Laptop", 999.99, java.util.List.of("Electronics", "Computer"));
    Product cloned = original.clone();

    System.out.println("Original: " + original);
    System.out.println("Cloned: " + cloned);
    System.out.println("original == cloned: " + (original == cloned));
    System.out.println("original.tags == cloned.tags: " + (original.tags() == cloned.tags()));

    cloned.tags().add("Gaming");

    System.out.println("After cloned.tags().add(\"Gaming\"):");
    System.out.println("Original tags: " + original.tags());
    System.out.println("Cloned tags: " + cloned.tags());
  }

  static class Product implements Cloneable {
    private final String name;
    private final double price;
    private final java.util.List<String> tags;

    Product(String name, double price, java.util.List<String> tags) {
      this.name = name;
      this.price = price;
      this.tags = new java.util.ArrayList<>(tags);
    }

    String name() {
      return name;
    }

    double price() {
      return price;
    }

    java.util.List<String> tags() {
      return tags;
    }

    @Override
    protected Product clone() {
      try {
        Product cloned = (Product) super.clone();
        cloned.tags = new java.util.ArrayList<>(this.tags);
        return cloned;
      } catch (CloneNotSupportedException e) {
        throw new AssertionError(e);
      }
    }

    @Override
    public String toString() {
      return "Product{name='" + name + "', price=" + price + ", tags=" + tags + "}";
    }
  }
}
