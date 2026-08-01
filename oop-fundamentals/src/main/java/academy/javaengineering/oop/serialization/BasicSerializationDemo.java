package academy.javaengineering.oop.serialization;

import java.io.Serializable;

/**
 * Demonstrates basic usage of the {@link Serializable} interface.
 *
 * <p>Any class that implements Serializable can have its state serialized
 * to a byte stream and deserialized back. This is the simplest form of
 * serialization in Java.</p>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Original: Employee{name='Alice', age=30, department='Engineering'}
 * Deserialized: Employee{name='Alice', age=30, department='Engineering'}
 * Objects are equal: true
 * </pre>
 */
public class BasicSerializationDemo {

  /**
   * A simple employee class that implements Serializable.
   */
  private static class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final int age;
    private final String department;

    Employee(String name, int age, String department) {
      this.name = name;
      this.age = age;
      this.department = department;
    }

    @Override
    public String toString() {
      return "Employee{name='" + name + "', age=" + age + ", department='" + department + "'}";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Employee employee = (Employee) o;
      return age == employee.age
          && java.util.Objects.equals(name, employee.name)
          && java.util.Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {
      return java.util.Objects.hash(name, age, department);
    }
  }

  /**
   * Demonstrates serializing and deserializing an Employee object.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Employee original = new Employee("Alice", 30, "Engineering");
    System.out.println("Original: " + original);

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);

      try (var bis = new java.io.ByteArrayInputStream(bos.toByteArray());
          var ois = new java.io.ObjectInputStream(bis)) {
        Employee deserialized = (Employee) ois.readObject();
        System.out.println("Deserialized: " + deserialized);
        System.out.println("Objects are equal: " + original.equals(deserialized));
      }
    } catch (Exception e) {
      System.err.println("Serialization failed: " + e.getMessage());
    }
  }
}
