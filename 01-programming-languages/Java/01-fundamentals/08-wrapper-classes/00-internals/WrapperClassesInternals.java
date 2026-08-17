package academy.javaengineering.fundamentals.wrapperclasses;

/**
 * Demonstrates wrapper class internals in Java.
 */
public class WrapperClassesInternals {

    public static void main(String[] args) {
        System.out.println("=== Wrapper Classes Internals Demo ===\n");

        // 1. Autoboxing
        System.out.println("--- Autoboxing ---");
        Integer boxed = 42;  // Autoboxing: Integer.valueOf(42)
        int primitive = boxed;  // Unboxing: boxed.intValue()
        System.out.println("Autoboxed: " + boxed);
        System.out.println("Unboxed: " + primitive);

        // 2. Integer cache
        System.out.println("\n--- Integer Cache (-128 to 127) ---");
        Integer a = 127;
        Integer b = 127;
        Integer c = 128;
        Integer d = 128;
        System.out.println("127 == 127: " + (a == b) + " (cached)");
        System.out.println("128 == 128: " + (c == d) + " (new objects)");

        // 3. Constructor vs valueOf
        System.out.println("\n--- Constructor vs valueOf ---");
        Integer e = Integer.valueOf(42);
        Integer f = new Integer(42);
        System.out.println("valueOf: " + e);
        System.out.println("new: " + f);

        // 4. Wrapper arithmetic
        System.out.println("\n--- Wrapper Arithmetic ---");
        Integer x = 10;
        Integer y = 20;
        int sum = x + y;  // Unbox, add, store
        System.out.println(x + " + " + y + " = " + sum);

        // 5. Null safety
        System.out.println("\n--- Null Safety ---");
        Integer nullValue = null;
        try {
            int unsafe = nullValue;  // NPE
        } catch (NullPointerException e2) {
            System.out.println("NPE on null unboxing: " + e2.getMessage());
        }

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
