package academy.javaengineering.oop.serialization;

import java.io.Serializable;

/**
 * Demonstrates the importance of {@code serialVersionUID} for version control.
 *
 * <p>{@code serialVersionUID} is a unique identifier for each Serializable class.
 * It ensures that a sender and receiver of a serialized object have compatible
 * classes. If the serialVersionUID changes between serialization and deserialization,
 * an {@link java.io.InvalidClassException} is thrown.</p>
 *
 * <p>This example shows that without a consistent serialVersionUID, modifying a
 * class breaks deserialization of previously serialized objects.</p>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Serialized Document v1 (serialVersionUID=1L)
 * Deserialized Document v1: Document{title='Report', version=1}
 * Changing Document to v2 (serialVersionUID=2L)...
 * Deserialization with mismatched UID: InvalidClassException
 * </pre>
 */
public class SerialVersionUIDExample {

  /**
   * Version 1 of a Document class with serialVersionUID = 1.
   */
  private static class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String title;
    private final int version;

    Document(String title, int version) {
      this.title = title;
      this.version = version;
    }

    @Override
    public String toString() {
      return "Document{title='" + title + "', version=" + version + "}";
    }
  }

  /**
   * Simulates a modified version of Document with a different serialVersionUID.
   */
  private static class DocumentV2 implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String title;
    private final int version;
    private final String author; // New field

    DocumentV2(String title, int version, String author) {
      this.title = title;
      this.version = version;
      this.author = author;
    }

    @Override
    public String toString() {
      return "DocumentV2{title='" + title + "', version=" + version
          + ", author='" + author + "'}";
    }
  }

  /**
   * Demonstrates serialVersionUID compatibility and mismatch.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Document original = new Document("Report", 1);
    System.out.println("Serialized Document v1 (serialVersionUID=1L)");

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);
      byte[] serialized = bos.toByteArray();

      // Deserialize with matching serialVersionUID - succeeds
      try (var bis = new java.io.ByteArrayInputStream(serialized);
          var ois = new java.io.ObjectInputStream(bis)) {
        Document deserialized = (Document) ois.readObject();
        System.out.println("Deserialized Document v1: " + deserialized);
      }

      // Attempt deserialization with mismatched serialVersionUID - fails
      System.out.println("Changing Document to v2 (serialVersionUID=2L)...");
      try (var bis = new java.io.ByteArrayInputStream(serialized);
          var ois = new java.io.ObjectInputStream(bis)) {
        DocumentV2 deserialized = (DocumentV2) ois.readObject();
        System.out.println("Deserialized: " + deserialized);
      } catch (java.io.InvalidClassException e) {
        System.out.println("Deserialization with mismatched UID: " + e.getClass().getSimpleName());
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
