package academy.javaengineering.oop.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

/**
 * Tests for Java serialization techniques.
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
class SerializationTest {

  // ---- Helper: serialize and deserialize roundtrip ----

  private static <T> T roundtrip(T obj) throws Exception {
    var bos = new ByteArrayOutputStream();
    var oos = new ObjectOutputStream(bos);
    oos.writeObject(obj);
    oos.flush();
    var bis = new ByteArrayInputStream(bos.toByteArray());
    var ois = new ObjectInputStream(bis);
    @SuppressWarnings("unchecked")
    T result = (T) ois.readObject();
    return result;
  }

  // ---- Inner test types ----

  static class BasicEmployee implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final int age;
    private final String department;

    BasicEmployee(String name, int age, String department) {
      this.name = name;
      this.age = age;
      this.department = department;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BasicEmployee e = (BasicEmployee) o;
      return age == e.age
          && java.util.Objects.equals(name, e.name)
          && java.util.Objects.equals(department, e.department);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(name, age, department);
    }

    @Override
    public String toString() {
      return "BasicEmployee{name='" + name + "', age=" + age
          + ", department='" + department + "'}";
    }
  }

  static class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int userId;
    private transient String token;
    private transient long loginTime;

    UserSession(int userId, String token, long loginTime) {
      this.userId = userId;
      this.token = token;
      this.loginTime = loginTime;
    }
  }

  static class DocumentV1 implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final int version;

    DocumentV1(String title, int version) {
      this.title = title;
      this.version = version;
    }
  }

  static class DocumentV2 implements Serializable {
    private static final long serialVersionUID = 2L;

    private final String title;
    private final int version;
    private final String author;

    DocumentV2(String title, int version, String author) {
      this.title = title;
      this.version = version;
      this.author = author;
    }
  }

  static class SecureAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private transient String password;

    SecureAccount(String username, String password) {
      this.username = username;
      this.password = password;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeUTF(xorEncrypt(password, 42));
    }

    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      password = xorEncrypt(in.readUTF(), 42);
    }

    private static String xorEncrypt(String data, int key) {
      if (data == null) {
        return null;
      }
      char[] chars = data.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        chars[i] ^= key;
      }
      return new String(chars);
    }

    String password() {
      return password;
    }
  }

  static class Money implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double amount;
    private final String currency;

    Money(double amount, String currency) {
      this.amount = amount;
      this.currency = currency;
    }

    private Object writeReplace() {
      return new MoneyProxy(this);
    }

    private void readObject(ObjectInputStream in) throws InvalidClassException {
      throw new InvalidClassException("Proxy required");
    }

    double amount() {
      return amount;
    }

    String currency() {
      return currency;
    }

    @Override
    public String toString() {
      return "Money{amount=%.2f, currency='%s'}".formatted(amount, currency);
    }

    private static final class MoneyProxy implements Serializable {
      private static final long serialVersionUID = 1L;

      private double amount;
      private String currency;

      MoneyProxy(Money money) {
        this.amount = money.amount;
        this.currency = money.currency;
      }

      private Object readResolve() {
        return new Money(amount, currency);
      }
    }
  }

  static class DatabaseConnection implements Serializable {
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

  static class ExternalProduct implements Externalizable {
    private int id;
    private String name;
    private double price;

    public ExternalProduct() {}

    ExternalProduct(int id, String name, double price) {
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

    int id() {
      return id;
    }

    String name() {
      return name;
    }

    double price() {
      return price;
    }
  }

  // ---- Test cases ----

  @Test
  void testBasicSerialization() throws Exception {
    var original = new BasicEmployee("Alice", 30, "Engineering");
    var deserialized = roundtrip(original);

    assertEquals(original, deserialized);
    assertEquals(original.toString(), deserialized.toString());
  }

  @Test
  void testTransientField() throws Exception {
    var original = new UserSession(42, "abc-123-def", 1719849600000L);
    var deserialized = roundtrip(original);

    assertEquals(42, deserialized.userId);
    assertEquals(null, deserialized.token);
    assertEquals(0L, deserialized.loginTime);
  }

  @Test
  void testSerialVersionUID() throws Exception {
    var v1 = new DocumentV1("Report", 1);
    var bos = new ByteArrayOutputStream();
    var oos = new ObjectOutputStream(bos);
    oos.writeObject(v1);
    oos.flush();
    byte[] serialized = bos.toByteArray();

    var bis = new ByteArrayInputStream(serialized);
    var ois = new ObjectInputStream(bis);
    DocumentV1 deserialized = (DocumentV1) ois.readObject();
    assertEquals("Report", deserialized.title);
    assertEquals(1, deserialized.version);

    var bis2 = new ByteArrayInputStream(serialized);
    var ois2 = new ObjectInputStream(bis2);
    assertThrows(InvalidClassException.class, () -> ois2.readObject());
  }

  @Test
  void testCustomSerialization() throws Exception {
    var original = new SecureAccount("bob", "s3cret!");
    var deserialized = roundtrip(original);

    assertEquals("bob", deserialized.username);
    assertEquals(original.password(), deserialized.password());
  }

  @Test
  void testSerializationProxy() throws Exception {
    var original = new Money(100.00, "USD");
    var deserialized = roundtrip(original);

    assertEquals(original.amount(), deserialized.amount(), 0.001);
    assertEquals(original.currency(), deserialized.currency());
    assertEquals(original.toString(), deserialized.toString());
  }

  @Test
  void testSingletonPreservation() throws Exception {
    DatabaseConnection instance1 = DatabaseConnection.getInstance();
    var deserialized = roundtrip(instance1);

    assertSame(instance1, deserialized);
  }

  @Test
  void testExternalizable() throws Exception {
    var original = new ExternalProduct(101, "Laptop", 999.99);
    var deserialized = roundtrip(original);

    assertEquals(original.id(), deserialized.id());
    assertEquals(original.name(), deserialized.name());
    assertEquals(original.price(), deserialized.price(), 0.001);
  }
}
