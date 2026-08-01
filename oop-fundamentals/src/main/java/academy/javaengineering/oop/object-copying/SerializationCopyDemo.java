package academy.javaengineering.oop.object-copying;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Demonstrates deep copy using serialization.
 *
 * <p>Serialization can be used to create a deep copy by serializing an object
 * to a byte stream and then deserializing it back. This requires all nested
 * objects to implement {@link Serializable}.</p>
 *
 * <p>Expected output:</p>
 * <pre>
 * === Deep Copy with Serialization ===
 * Original: Config{host='localhost', port=8080, options=[debug, verbose]}
 * Serialized copy: Config{host='localhost', port=8080, options=[debug, verbose]}
 * original == copy: false
 * original.options == copy.options: false
 * After copy.options().add("production"):
 * Original options: [debug, verbose]
 * Copy options: [debug, verbose, production]
 * </pre>
 */
public class SerializationCopyDemo {

  public static void main(String[] args) {
    System.out.println("=== Deep Copy with Serialization ===");

    Config original = new Config("localhost", 8080, java.util.List.of("debug", "verbose"));
    Config copy = deepCopy(original);

    System.out.println("Original: " + original);
    System.out.println("Serialized copy: " + copy);
    System.out.println("original == copy: " + (original == copy));
    System.out.println("original.options == copy.options: " + (original.options() == copy.options()));

    copy.options().add("production");

    System.out.println("After copy.options().add(\"production\"):");
    System.out.println("Original options: " + original.options());
    System.out.println("Copy options: " + copy.options());
  }

  /**
   * Creates a deep copy of the given object using serialization.
   *
   * @param <T> the type of the object to copy
   * @param obj the object to copy
   * @return a deep copy of the object
   * @throws RuntimeException if serialization or deserialization fails
   */
  @SuppressWarnings("unchecked")
  static <T extends Serializable> T deepCopy(T obj) {
    try {
      var bos = new java.io.ByteArrayOutputStream();
      var oos = new java.io.ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();

      var bis = new java.io.ByteArrayInputStream(bos.toByteArray());
      var ois = new java.io.ObjectInputStream(bis);
      return (T) ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException("Failed to deep copy via serialization", e);
    }
  }

  static class Config implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String host;
    private final int port;
    private final ArrayList<String> options;

    Config(String host, int port, java.util.List<String> options) {
      this.host = host;
      this.port = port;
      this.options = new ArrayList<>(options);
    }

    String host() {
      return host;
    }

    int port() {
      return port;
    }

    ArrayList<String> options() {
      return options;
    }

    @Override
    public String toString() {
      return "Config{host='" + host + "', port=" + port + ", options=" + options + "}";
    }
  }
}
