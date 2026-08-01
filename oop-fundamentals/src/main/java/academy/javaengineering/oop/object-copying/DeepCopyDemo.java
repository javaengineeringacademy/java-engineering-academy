package academy.javaengineering.oop.object-copying;

/**
 * Demonstrates deep copy using manual recursion.
 *
 * <p>A deep copy duplicates the object and all nested objects recursively.
 * Modifying nested objects in the copy does not affect the original.</p>
 *
 * <p>Expected output:</p>
 * <pre>
 * === Deep Copy with Manual Recursion ===
 * Original address: 123 Main St
 * Deep copied address: 123 Main St
 * original == deepCopy: false
 * original.address == deepCopy.address: true
 * After deepCopy.setStreet("456 Oak Ave"):
 * Original address: 123 Main St
 * Deep copied address: 456 Oak Ave
 * </pre>
 */
public class DeepCopyDemo {

  public static void main(String[] args) {
    System.out.println("=== Deep Copy with Manual Recursion ===");

    Address address = new Address("123 Main St");
    Person original = new Person("Alice", 30, address);
    Person deepCopy = original.deepCopy();

    System.out.println("Original address: " + original.address().street());
    System.out.println("Deep copied address: " + deepCopy.address().street());
    System.out.println("original == deepCopy: " + (original == deepCopy));
    System.out.println("original.address == deepCopy.address: " + (original.address() == deepCopy.address()));

    deepCopy.address().setStreet("456 Oak Ave");

    System.out.println("After deepCopy.setStreet(\"456 Oak Ave\"):");
    System.out.println("Original address: " + original.address().street());
    System.out.println("Deep copied address: " + deepCopy.address().street());
  }

  static class Address {
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
  }

  static class Person {
    private String name;
    private int age;
    private Address address;

    Person(String name, int age, Address address) {
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

    Person deepCopy() {
      return new Person(this.name, this.age, this.address.deepCopy());
    }
  }
}
