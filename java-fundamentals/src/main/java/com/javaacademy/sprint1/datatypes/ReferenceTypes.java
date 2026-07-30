package com.javaacademy.sprint1.datatypes;

/**
 * ReferenceTypes - Demonstrates reference types (objects, arrays, strings).
 * 
 * <p><b>Real-world analogy:</b> 
 * - Primitives = The actual value written on a piece of paper
 * - References = The address of a house where the value lives
 * - Variables of reference type hold the <b>address</b> (memory reference), not the object itself
 * 
 * <p><b>Key differences:</b>
 * <table border="1">
 * <tr><th>Aspect</th><th>Primitive</th><th>Reference</th></tr>
 * <tr><td>Stores</td><td>Actual value</td><td>Memory address (reference)</td></tr>
 * <tr><td>Memory</td><td>Stack</td><td>Heap (object) + Stack (reference)</td></tr>
 * <tr><td>Default</td><td>Zero/false</td><td>null</td></tr>
 * <tr><td>Assignment</td><td>Copies value</td><td>Copies reference (both point to same object)</td></tr>
 * <tr><td>Equality</td><td>== compares values</td><td>== compares addresses, .equals() compares content</td></tr>
 * <tr><td>Methods</td><td>None</td><td>Has methods (toString, equals, hashCode...)</td></tr>
 * </table>
 * 
 * <p><b>Wrapper Classes:</b> Each primitive has a wrapper:
 * <ul>
 *   <li>byte → Byte, short → Short, int → Integer, long → Long</li>
 *   <li>float → Float, double → Double, char → Character, boolean → Boolean</li>
 * </ul>
 * 
 * <p><b>Autoboxing/Unboxing:</b> Automatic conversion between primitives and wrappers (Java 5+)
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class ReferenceTypes {

    private ReferenceTypes() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Demonstrates reference types and wrapper classes.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        // String - most common reference type (immutable)
        String str1 = "Hello";
        String str2 = new String("Hello"); // Different object, same content
        
        System.out.println("=== Reference Types Demo ===");
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        System.out.println("str1 == str2: " + (str1 == str2));       // false (different objects)
        System.out.println("str1.equals(str2): " + str1.equals(str2)); // true (same content)

        // Wrapper classes
        Integer wrapperInt = 42;        // Autoboxing: int → Integer
        int primitiveInt = wrapperInt;  // Unboxing: Integer → int
        
        Double wrapperDouble = 3.14;
        double primitiveDouble = wrapperDouble;

        System.out.println("\n=== Wrapper Classes ===");
        System.out.println("Integer: " + wrapperInt + " (class: " + wrapperInt.getClass().getSimpleName() + ")");
        System.out.println("Double:  " + wrapperDouble + " (class: " + wrapperDouble.getClass().getSimpleName() + ")");

        // Wrapper utility methods
        System.out.println("Integer.toString(42): " + Integer.toString(42));
        System.out.println("Integer.parseInt(\"123\"): " + Integer.parseInt("123"));
        System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE);
        System.out.println("Double.isNaN(0.0/0.0): " + Double.isNaN(0.0 / 0.0));

        // null - reference pointing to nothing
        String nullRef = null;
        System.out.println("\n=== null Reference ===");
        System.out.println("nullRef: " + nullRef);
        System.out.println("nullRef == null: " + (nullRef == null));
        
        // NullPointerException demo (commented out)
        // System.out.println(nullRef.length()); // Throws NPE!

        // Arrays are reference types
        int[] numbers = {1, 2, 3};
        int[] sameNumbers = numbers;      // Same reference
        int[] copyNumbers = numbers.clone(); // Different object, same content
        
        System.out.println("\n=== Arrays (Reference Types) ===");
        System.out.println("numbers == sameNumbers: " + (numbers == sameNumbers));        // true
        System.out.println("numbers == copyNumbers: " + (numbers == copyNumbers));        // false
        System.out.println("Arrays.equals: " + java.util.Arrays.equals(numbers, copyNumbers)); // true

        // Expected output:
        // === Reference Types Demo ===
        // str1: Hello
        // str2: Hello
        // str1 == str2: false
        // str1.equals(str2): true
        // ...
    }
}