package academy.javaengineering.oop.serialization;

/**
 * Demonstrates enum serialization guarantees.
 *
 * <p>Java enums are automatically serializable and have special serialization guarantees:
 * <ul>
 *   <li>No need to implement {@link java.io.Serializable}</li>
 *   <li>{@code serialVersionUID} is handled automatically</li>
 *   <li>Enum instances are singletons - deserialization always returns the same instance</li>
 *   <li>The serialization mechanism is different from regular objects</li>
 * </ul>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Original: Priority{level=HIGH, name='HIGH'}
 * Deserialized: Priority{level=HIGH, name='HIGH'}
 * Same instance: true
 * Serialized size is small: true
 * </pre>
 */
public class EnumSerializationDemo {

  /**
   * A priority enum demonstrating enum serialization.
   */
  private enum Priority {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private final int level;

    Priority(int level) {
      this.level = level;
    }

    int getLevel() {
      return level;
    }

    @Override
    public String toString() {
      return "Priority{level=" + level + ", name='" + name() + "'}";
    }
  }

  /**
   * Demonstrates enum serialization and instance identity.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Priority original = Priority.HIGH;
    System.out.println("Original: " + original);

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);
      byte[] serialized = bos.toByteArray();

      try (var bis = new java.io.ByteArrayInputStream(serialized);
          var ois = new java.io.ObjectInputStream(bis)) {
        Priority deserialized = (Priority) ois.readObject();
        System.out.println("Deserialized: " + deserialized);
        System.out.println("Same instance: " + (original == deserialized));
        System.out.println("Serialized size is small: " + (serialized.length < 50));
      }
    } catch (Exception e) {
      System.err.println("Serialization failed: " + e.getMessage());
    }
  }
}
