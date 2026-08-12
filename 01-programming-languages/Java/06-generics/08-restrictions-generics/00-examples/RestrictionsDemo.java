package academy.javaengineering.generics.restrictions-generics.examples;

import java.util.ArrayList;
import java.util.List;

/**
 * Restrictions Demo - Working examples of generic restrictions.
 */
public class RestrictionsDemo {

    static class Box<T> {
        private T value;
        public Box(T value) { this.value = value; }
        public T getValue() { return value; }
    }

    public static void main(String[] args) {
        System.out.println("=== Restrictions Demo ===\n");

        // 1. Primitive types not allowed
        System.out.println("1. Primitive Types:");
        // Box<int> intBox = new Box<>(42);  // COMPILE ERROR
        Box<Integer> integerBox = new Box<>(42);  // OK
        System.out.println("  Use Integer instead of int: " + integerBox.getValue());

        // 2. Cannot use new T()
        System.out.println("\n2. Cannot Create Instances:");
        System.out.println("  new T() is not allowed because T is erased");
        System.out.println("  Use Class<T> or factory methods instead");

        // 3. Cannot create generic arrays
        System.out.println("\n3. Cannot Create Generic Arrays:");
        // Box<String>[] boxes = new Box<String>[10];  // COMPILE ERROR
        System.out.println("  Use List<Box<String>> instead");

        // 4. Cannot use instanceof
        System.out.println("\n4. Cannot Use instanceof:");
        Box<String> box = new Box<>("Hello");
        // if (box instanceof Box<String>) { }  // COMPILE ERROR
        if (box instanceof Box) {  // OK
            System.out.println("  Use raw type: box instanceof Box");
        }

        // 5. Static members cannot use type parameters
        System.out.println("\n5. Static Context:");
        System.out.println("  static T value;  // COMPILE ERROR");
        System.out.println("  Type parameters are per-instance");
    }
}
