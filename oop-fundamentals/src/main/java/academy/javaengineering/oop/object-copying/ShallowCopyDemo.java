package academy.javaengineering.oop.object-copying;

/**
 * Demonstrates shallow copy using {@link Object#clone()}.
 *
 * <p>A shallow copy duplicates the object but does not recursively copy nested
 * objects. The cloned object shares references to the same nested objects as
 * the original.</p>
 *
 * <p>Expected output:</p>
 * <pre>
 * === Shallow Copy with clone() ===
 * Original address: 123 Main St
 * Cloned address: 123 Main St
 * original == cloned: false
 * original.address == cloned.address: true
 * After cloned.setAddress("456 Oak Ave"):
 * Original address: 456 Oak Ave
 * Cloned address: 456 Oak Ave
 * </pre>
 */
public class ShallowCopyDemo {

  public static void main(String[] args) {
    System.out.println("=== Shallow Copy with clone() ===");

    Address address = new Address("123 Main St");
    Employee original = new Employee("Alice", 1001, address);
    Employee cloned = original.clone();

    System.out.println("Original address: " + original.address());
    System.out.println("Cloned address: " + cloned.address());
    System.out.println("original == cloned: " + (original == cloned));
    System.out.println("original.address == cloned.address: " + (original.address() == cloned.address()));

    cloned.setAddress("456 Oak Ave");

    System.out.println("After cloned.setAddress(\"456 Oak Ave\"):");
    System.out.println("Original address: " + original.address());
    System.out.println("Cloned address: " + cloned.address());
  }

  static class Address implements Cloneable {
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

    @Override
    protected Address clone() {
      try {
        return (Address) super.clone();
      } catch (CloneNotSupportedException e) {
        throw new AssertionError(e);
      }
    }
  }

  static class Employee implements Cloneable {
    private String name;
    private int id;
    private Address address;

    Employee(String name, int id, Address address) {
      this.name = name;
      this.id = id;
      this.address = address;
    }

    String name() {
      return name;
    }

    int id() {
      return id;
    }

    Address address() {
      return address;
    }

    void setStreet(String street) {
      this.address.setStreet(street);
    }

    void setAddress(String street) {
      this.address.setStreet(street);
    }

    @Override
    protected Employee clone() {
      try {
        return (Employee) super.clone();
      } catch (CloneNotSupportedException e) {
        throw new AssertionError(e);
      }
    }
  }
}
