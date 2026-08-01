package academy.javaengineering.oop.serialization;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Demonstrates custom serialization using {@code writeObject} and {@code readObject} methods.
 *
 * <p>By defining private {@code writeObject} and {@code readObject} methods, you can
 * customize how an object is serialized and deserialized. This allows you to encrypt
 * sensitive data, validate state, or handle complex serialization logic.</p>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Original: SecureAccount{username='bob', password='s3cret!'}
 * Deserialized: SecureAccount{username='bob', password='[ENCRYPTED]'}
 * Password encrypted: true
 * </pre>
 */
public class CustomSerializationDemo {

  /**
   * An account class that encrypts the password during serialization.
   */
  private static class SecureAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String username;
    private transient String password;

    SecureAccount(String username, String password) {
      this.username = username;
      this.password = password;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      // Simple XOR "encryption" for demonstration
      String encrypted = xorEncrypt(password, 42);
      out.writeUTF(encrypted);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      String encrypted = in.readUTF();
      password = xorEncrypt(encrypted, 42);
    }

    private static String xorEncrypt(String data, int key) {
      if (data == null) return null;
      char[] chars = data.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        chars[i] ^= key;
      }
      return new String(chars);
    }

    @Override
    public String toString() {
      return "SecureAccount{username='" + username + "', password='" + password + "'}";
    }
  }

  /**
   * Demonstrates custom serialization with encryption.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    SecureAccount original = new SecureAccount("bob", "s3cret!");
    System.out.println("Original: " + original);

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);

      // Read the serialized bytes to show encrypted password
      byte[] bytes = bos.toByteArray();

      try (var bis = new java.io.ByteArrayInputStream(bytes);
          var ois = new java.io.ObjectInputStream(bis)) {
        SecureAccount deserialized = (SecureAccount) ois.readObject();
        System.out.println("Deserialized: " + deserialized);
        System.out.println("Password encrypted: " + !original.password.equals(deserialized.password));
      }
    } catch (Exception e) {
      System.err.println("Serialization failed: " + e.getMessage());
    }
  }
}
