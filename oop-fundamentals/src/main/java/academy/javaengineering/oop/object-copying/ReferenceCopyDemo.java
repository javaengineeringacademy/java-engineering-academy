package academy.javaengineering.oop.object-copying;

/**
 * Demonstrates reference copy vs object copy.
 *
 * <p>A reference copy creates a new reference variable that points to the same
 * object in memory. Modifying one reference affects the other since both point
 * to the same object.</p>
 *
 * <p>Expected output:</p>
 * <pre>
 * === Reference Copy vs Object Copy ===
 * original.name: Alice
 * originalRef.name: Alice
 * After originalRef.setName("Bob"):
 * original.name: Bob
 * originalRef.name: Bob
 * original == originalRef: true
 * </pre>
 */
public class ReferenceCopyDemo {

  public static void main(String[] args) {
    System.out.println("=== Reference Copy vs Object Copy ===");

    Person original = new Person("Alice", 30);
    Person originalRef = original;

    System.out.println("original.name: " + original.name());
    System.out.println("originalRef.name: " + originalRef.name());

    originalRef.setName("Bob");

    System.out.println("After originalRef.setName(\"Bob\"):");
    System.out.println("original.name: " + original.name());
    System.out.println("originalRef.name: " + originalRef.name());
    System.out.println("original == originalRef: " + (original == originalRef));
  }

  record Person(String name, int age) {
    Person {
      if (name == null) {
        throw new IllegalArgumentException("Name cannot be null");
      }
    }

    Person withName(String newName) {
      return new Person(newName, age);
    }
  }
}
