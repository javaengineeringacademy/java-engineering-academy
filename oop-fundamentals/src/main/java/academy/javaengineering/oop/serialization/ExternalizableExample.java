package academy.javaengineering.oop.serialization;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

/**
 * Demonstrates the {@link Externalizable} interface for custom serialization control.
 *
 * <p>Unlike {@link java.io.Serializable}, Externalizable gives you full control
 * over the serialization process. You must implement both {@code writeExternal}
 * and {@code readExternal} methods, plus a public no-arg constructor.</p>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Original: Product{id=101, name='Laptop', price=999.99}
 * Deserialized: Product{id=101, name='Laptop', price=999.99}
 * All fields match: true
 * </pre>
 */
public class ExternalizableExample {

  /**
   * A product class that implements Externalizable for full serialization control.
   */
  private static class Product implements Externalizable {

    private int id;
    private String name;
    private double price;

    /** Public no-arg constructor required by Externalizable. */
    public Product() {}

    Product(int id, String name, double price) {
      this.id = id;
      this.name = name;
      this.price = price;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
      out.writeInt(id);
      out.writeUTF(name);
      out.writeDouble(price);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
      id = in.readInt();
      name = in.readUTF();
      price = in.readDouble();
    }

    @Override
    public String toString() {
      return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
  }

  /**
   * Demonstrates serializing and deserializing a Product using Externalizable.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Product original = new Product(101, "Laptop", 999.99);
    System.out.println("Original: " + original);

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);

      try (var bis = new java.io.ByteArrayInputStream(bos.toByteArray());
          var ois = new java.io.ObjectInputStream(bis)) {
        Product deserialized = (Product) ois.readObject();
        System.out.println("Deserialized: " + deserialized);
        System.out.println("All fields match: "
            + (original.id == deserialized.id
                && original.name.equals(deserialized.name)
                && original.price == deserialized.price));
      }
    } catch (Exception e) {
      System.err.println("Serialization failed: " + e.getMessage());
    }
  }
}
