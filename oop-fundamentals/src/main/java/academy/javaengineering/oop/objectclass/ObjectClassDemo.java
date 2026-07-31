package academy.javaengineering.oop.objectclass;

/**
 * ObjectClassDemo - Demonstrates Object class methods available to all Java objects.
 * 
 * <p><b>Object Class Methods:</b>
 * <ul>
 *   <li>{@code toString()} - String representation</li>
 *   <li>{@code equals(Object)} - Logical equality comparison</li>
 *   <li>{@code hashCode()} - Hash code for collections</li>
 *   <li>{@code getClass()} - Runtime class information</li>
 *   <li>{@code clone()} - Object cloning</li>
 *   <li>{@code finalize()} - Garbage collection callback (deprecated)</li>
 *   <li>{@code wait()} / {@code notify()} / {@code notifyAll()} - Thread coordination</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class ObjectClassDemo {

    private ObjectClassDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Object Class Demo ===\n");

        // toString()
        System.out.println("--- toString() ---");
        Student student = new Student("Alice", 95);
        System.out.println("Default: " + student.toString());
        System.out.println("Implicit: " + student); // Calls toString() automatically

        // equals()
        System.out.println("\n--- equals() ---");
        Student alice1 = new Student("Alice", 95);
        Student alice2 = new Student("Alice", 95);
        Student bob = new Student("Bob", 88);

        System.out.println("alice1 == alice2: " + (alice1 == alice2));        // false (different objects)
        System.out.println("alice1.equals(alice2): " + alice1.equals(alice2)); // true (same content)
        System.out.println("alice1.equals(bob): " + alice1.equals(bob));       // false

        // hashCode()
        System.out.println("\n--- hashCode() ---");
        System.out.println("alice1 hashCode: " + alice1.hashCode());
        System.out.println("alice2 hashCode: " + alice2.hashCode());
        System.out.println("Equal objects must have same hashCode: " +
            (alice1.hashCode() == alice2.hashCode()));

        // getClass()
        System.out.println("\n--- getClass() ---");
        System.out.println("student class: " + student.getClass());
        System.out.println("Class name: " + student.getClass().getName());
        System.out.println("Simple name: " + student.getClass().getSimpleName());

        // clone()
        System.out.println("\n--- clone() ---");
        try {
            Student cloned = (Student) student.clone();
            System.out.println("Original: " + student);
            System.out.println("Cloned: " + cloned);
            System.out.println("Same object? " + (student == cloned)); // false
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone not supported: " + e.getMessage());
        }

        // instanceof
        System.out.println("\n--- instanceof ---");
        System.out.println("student instanceof Student: " + (student instanceof Student));
        System.out.println("student instanceof Object: " + (student instanceof Object));
        System.out.println("student instanceof String: " + (student instanceof String));

        // Wait/Notify (thread concepts preview)
        System.out.println("\n--- wait()/notify() (Preview) ---");
        System.out.println("These methods are for thread coordination");
        System.out.println("wait() - releases lock and waits");
        System.out.println("notify() - wakes one waiting thread");
        System.out.println("notifyAll() - wakes all waiting threads");
    }
}