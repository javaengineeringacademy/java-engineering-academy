package academy.javaengineering.fundamentals.variables;

/**
 * Demonstrates how variables are stored in JVM memory.
 */
public class VariablesMemory {

    public static void main(String[] args) {
        System.out.println("=== Variables Memory Demo ===\n");

        // 1. Stack memory for primitives
        System.out.println("--- Stack Memory (Primitives) ---");
        int localInt = 42;
        double localDouble = 3.14;
        boolean localBool = true;
        char localChar = 'A';
        byte localByte = 100;
        short localShort = 1000;
        float localFloat = 2.71f;
        long localLong = 999999L;

        System.out.println("int    (4 bytes on stack): " + localInt);
        System.out.println("double (8 bytes on stack): " + localDouble);
        System.out.println("boolean(1 byte on stack):  " + localBool);
        System.out.println("char   (2 bytes on stack): " + localChar);
        System.out.println("byte   (1 byte on stack):  " + localByte);
        System.out.println("short  (2 bytes on stack): " + localShort);
        System.out.println("float  (4 bytes on stack): " + localFloat);
        System.out.println("long   (8 bytes on stack): " + localLong);

        // 2. Heap memory for objects
        System.out.println("\n--- Heap Memory (Objects) ---");
        String name = "Java";
        int[] numbers = {1, 2, 3, 4, 5};
        Integer boxedInt = 42;

        System.out.println("String reference on stack, object on heap: " + name);
        System.out.println("int[] reference on stack, array on heap: " + numbers);
        System.out.println("Integer reference on stack, object on heap: " + boxedInt);

        // 3. String pool memory
        System.out.println("\n--- String Pool Memory ---");
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");

        System.out.println("s1 and s2 share pool entry: " + (s1 == s2));
        System.out.println("s3 is a separate heap object: " + (s1 == s3));

        // 4. Autoboxing memory overhead
        System.out.println("\n--- Autoboxing Memory Overhead ---");
        int primitiveValue = 42;
        Integer boxedValue = 42;
        System.out.println("Primitive int: 4 bytes on stack");
        System.out.println("Boxed Integer: 8 bytes reference (stack) + 16+ bytes object (heap)");

        // 5. Method call stack frames
        System.out.println("\n--- Method Call Stack Frames ---");
        demonstrateStackFrame(10, 20);
        demonstrateStackFrame(100, 200);

        System.out.println("\n=== Memory Demo Complete ===");
    }

    static void demonstrateStackFrame(int a, int b) {
        int sum = a + b;
        System.out.println("Stack frame: a=" + a + ", b=" + b + ", sum=" + sum + " (all local to this frame)");
    }
}
