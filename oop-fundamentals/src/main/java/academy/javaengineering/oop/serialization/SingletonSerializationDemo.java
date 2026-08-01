package academy.javaengineering.oop.serialization;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Demonstrates singleton serialization using {@code readResolve}.
 *
 * <p>Without {@code readResolve}, deserializing a singleton would create a new instance,
 * breaking the singleton guarantee. By implementing {@code readResolve}, we ensure the
 * same instance is returned during deserialization.</p>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Original singleton: DatabaseConnection{connectionId=1}
 * Deserialized singleton: DatabaseConnection{connectionId=1}
 * Same instance: true
 * </pre>
 */
public class SingletonSerializationDemo {

  /**
   * A singleton database connection that survives serialization.
   */
  private static final class DatabaseConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    private static DatabaseConnection instance;

    private final int connectionId;

    private DatabaseConnection() {
      this.connectionId = 1;
    }

    static DatabaseConnection getInstance() {
      if (instance == null) {
        instance = new DatabaseConnection();
      }
      return instance;
    }

    private Object readResolve() {
      return getInstance();
    }

    @Override
    public String toString() {
      return "DatabaseConnection{connectionId=" + connectionId + "}";
    }
  }

  /**
   * Demonstrates singleton preservation across serialization.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    DatabaseConnection original = DatabaseConnection.getInstance();
    System.out.println("Original singleton: " + original);

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);

      try (var bis = new java.io.ByteArrayInputStream(bos.toByteArray());
          var ois = new java.io.ObjectInputStream(bis)) {
        DatabaseConnection deserialized = (DatabaseConnection) ois.readObject();
        System.out.println("Deserialized singleton: " + deserialized);
        System.out.println("Same instance: " + (original == deserialized));
      }
    } catch (Exception e) {
      System.err.println("Serialization failed: " + e.getMessage());
    }
  }
}
