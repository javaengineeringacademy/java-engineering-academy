package academy.javaengineering.oop.serialization;

import java.io.Serializable;

/**
 * Demonstrates the {@code transient} keyword for excluding fields from serialization.
 *
 * <p>Fields marked as {@code transient} are not serialized. After deserialization,
 * transient fields retain their default values (null for objects, 0 for primitives,
 * false for booleans).</p>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Original: UserSession{userId=42, token='abc-123-def', loginTime=1719849600000}
 * Deserialized: UserSession{userId=42, token='null', loginTime=0}
 * Token persisted: false
 * Login time persisted: false
 * </pre>
 */
public class TransientExample {

  /**
   * Represents a user session where the token and login time are transient.
   */
  private static class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int userId;
    private transient String token;
    private transient long loginTime;

    UserSession(int userId, String token, long loginTime) {
      this.userId = userId;
      this.token = token;
      this.loginTime = loginTime;
    }

    @Override
    public String toString() {
      return "UserSession{userId=" + userId
          + ", token='" + token + "', loginTime=" + loginTime + "}";
    }
  }

  /**
   * Demonstrates transient fields losing their values after deserialization.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    UserSession original = new UserSession(42, "abc-123-def", System.currentTimeMillis());
    System.out.println("Original: " + original);

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);

      try (var bis = new java.io.ByteArrayInputStream(bos.toByteArray());
          var ois = new java.io.ObjectInputStream(bis)) {
        UserSession deserialized = (UserSession) ois.readObject();
        System.out.println("Deserialized: " + deserialized);
        System.out.println("Token persisted: " + (deserialized.token != null));
        System.out.println("Login time persisted: " + (deserialized.loginTime != 0));
      }
    } catch (Exception e) {
      System.err.println("Serialization failed: " + e.getMessage());
    }
  }
}
