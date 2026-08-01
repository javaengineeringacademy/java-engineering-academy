package academy.javaengineering.oop.object-copying;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * Tests for object copying techniques.
 *
 * @author Java Engineering Academy
 * @version 1.0
 */
class ObjectCopyingTest {

  // ---- Shared helper types ----

  static class Address implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private String street;

    Address(String street) {
      this.street = street;
    }

    String street() {
      return street;
    }

    void setStreet(String street) {
      this.street = street;
    }

    Address deepCopy() {
      return new Address(this.street);
    }

    @Override
    protected Address clone() {
      try {
        return (Address) super.clone();
      } catch (CloneNotSupportedException e) {
        throw new AssertionError(e);
      }
    }
  }

  static class MutablePerson implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private Address address;

    MutablePerson(String name, int age, Address address) {
      this.name = name;
      this.age = age;
      this.address = address;
    }

    String name() {
      return name;
    }

    int age() {
      return age;
    }

    Address address() {
      return address;
    }

    void setName(String name) {
      this.name = name;
    }

    MutablePerson deepCopy() {
      return new MutablePerson(this.name, this.age, this.address.deepCopy());
    }

    @Override
    protected MutablePerson clone() {
      try {
        return (MutablePerson) super.clone();
      } catch (CloneNotSupportedException e) {
        throw new AssertionError(e);
      }
    }
  }

  static class Student {
    private final String name;
    private final int grade;
    private final List<Integer> scores;

    Student(String name, int grade, List<Integer> scores) {
      this.name = name;
      this.grade = grade;
      this.scores = new ArrayList<>(scores);
    }

    Student(Student other) {
      this.name = other.name;
      this.grade = other.grade;
      this.scores = new ArrayList<>(other.scores);
    }

    String name() {
      return name;
    }

    int grade() {
      return grade;
    }

    List<Integer> scores() {
      return scores;
    }
  }

  static class Product implements Cloneable {
    private final String name;
    private final double price;
    private final List<String> tags;

    Product(String name, double price, List<String> tags) {
      this.name = name;
      this.price = price;
      this.tags = new ArrayList<>(tags);
    }

    String name() {
      return name;
    }

    double price() {
      return price;
    }

    List<String> tags() {
      return tags;
    }

    @Override
    protected Product clone() {
      try {
        Product cloned = (Product) super.clone();
        cloned.tags = new ArrayList<>(this.tags);
        return cloned;
      } catch (CloneNotSupportedException e) {
        throw new AssertionError(e);
      }
    }
  }

  static class Order {
    private final int id;
    private final String customer;
    private final double amount;
    private final List<String> items;

    Order(int id, String customer, double amount, List<String> items) {
      this.id = id;
      this.customer = customer;
      this.amount = amount;
      this.items = new ArrayList<>(items);
    }

    private Order(Builder builder) {
      this.id = builder.id;
      this.customer = builder.customer;
      this.amount = builder.amount;
      this.items = new ArrayList<>(builder.items);
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

    List<String> items() {
      return items;
    }

    Builder toBuilder() {
      return new Builder(this);
    }

    static class Builder {
      private int id;
      private String customer;
      private double amount;
      private List<String> items;

      Builder() {}

      Builder(Order order) {
        this.id = order.id;
        this.customer = order.customer;
        this.amount = order.amount;
        this.items = new ArrayList<>(order.items);
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

      Builder items(List<String> items) {
        this.items = new ArrayList<>(items);
        return this;
      }

      Order build() {
        return new Order(this);
      }
    }
  }

  static class Config implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String host;
    private final int port;
    private final ArrayList<String> options;

    Config(String host, int port, List<String> options) {
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
  }

  @SuppressWarnings("unchecked")
  static <T extends Serializable> T serializationDeepCopy(T obj) {
    try {
      var bos = new ByteArrayOutputStream();
      var oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();

      var bis = new ByteArrayInputStream(bos.toByteArray());
      var ois = new ObjectInputStream(bis);
      return (T) ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException("Failed to deep copy via serialization", e);
    }
  }

  // ---- Test cases ----

  @Test
  void testReferenceCopy() {
    var original = new MutablePerson("Alice", 30, new Address("123 Main St"));
    var originalRef = original;

    assertSame(original, originalRef);
    assertEquals(original.name(), originalRef.name());

    originalRef.setName("Bob");
    assertEquals("Bob", original.name());
    assertEquals("Bob", originalRef.name());
  }

  @Test
  void testShallowCopy() {
    var address = new Address("123 Main St");
    var original = new MutablePerson("Alice", 1001, address);
    var cloned = original.clone();

    assertNotSame(original, cloned);
    assertEquals(original.name(), cloned.name());
    assertEquals(original.age(), cloned.age());

    cloned.setAddress("456 Oak Ave");
    assertEquals("456 Oak Ave", original.address().street());
  }

  @Test
  void testDeepCopy() {
    var address = new Address("123 Main St");
    var original = new MutablePerson("Alice", 30, address);
    var deepCopy = original.deepCopy();

    assertNotSame(original, deepCopy);
    assertEquals(original.name(), deepCopy.name());
    assertEquals(original.age(), deepCopy.age());
    assertNotSame(original.address(), deepCopy.address());
    assertEquals(original.address().street(), deepCopy.address().street());

    deepCopy.address().setStreet("456 Oak Ave");
    assertEquals("123 Main St", original.address().street());
    assertEquals("456 Oak Ave", deepCopy.address().street());
  }

  @Test
  void testCopyConstructor() {
    var original = new Student("Alice", 95, List.of(90, 95, 100));
    var copy = new Student(original);

    assertNotSame(original, copy);
    assertEquals(original.name(), copy.name());
    assertEquals(original.grade(), copy.grade());
    assertNotSame(original.scores(), copy.scores());
    assertEquals(original.scores(), copy.scores());

    copy.scores().add(85);
    assertEquals(3, original.scores().size());
    assertEquals(4, copy.scores().size());
  }

  @Test
  void testCloneableClone() {
    var original = new Product("Laptop", 999.99, List.of("Electronics", "Computer"));
    var cloned = original.clone();

    assertNotSame(original, cloned);
    assertEquals(original.name(), cloned.name());
    assertEquals(original.price(), cloned.price());
    assertNotSame(original.tags(), cloned.tags());
    assertEquals(original.tags(), cloned.tags());

    cloned.tags().add("Gaming");
    assertEquals(2, original.tags().size());
    assertEquals(3, cloned.tags().size());
  }

  @Test
  void testBuilderCopy() {
    var original = new Order(1, "Alice", 299.99, List.of("Widget", "Gadget"));
    var fullCopy = original.toBuilder().build();
    var modifiedCopy =
        original.toBuilder().id(2).customer("Bob").amount(499.99).build();

    assertNotSame(original, fullCopy);
    assertEquals(original.id(), fullCopy.id());
    assertEquals(original.customer(), fullCopy.customer());
    assertEquals(original.amount(), fullCopy.amount());
    assertNotSame(original.items(), fullCopy.items());
    assertEquals(original.items(), fullCopy.items());

    assertEquals(2, modifiedCopy.id());
    assertEquals("Bob", modifiedCopy.customer());
    assertEquals(499.99, modifiedCopy.amount(), 0.001);
    assertNotSame(original.items(), modifiedCopy.items());

    modifiedCopy.items().add("Doohickey");
    assertEquals(2, original.items().size());
    assertEquals(3, modifiedCopy.items().size());
  }

  @Test
  void testSerializationCopy() {
    var original = new Config("localhost", 8080, List.of("debug", "verbose"));
    var copy = serializationDeepCopy(original);

    assertNotSame(original, copy);
    assertEquals(original.host(), copy.host());
    assertEquals(original.port(), copy.port());
    assertNotSame(original.options(), copy.options());
    assertEquals(original.options(), copy.options());

    copy.options().add("production");
    assertEquals(2, original.options().size());
    assertEquals(3, copy.options().size());
  }
}
