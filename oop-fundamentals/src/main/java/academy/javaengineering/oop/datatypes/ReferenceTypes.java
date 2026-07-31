package academy.javaengineering.oop.datatypes;

/**
 * Demonstrates reference types (objects, arrays, strings).
 */
public final class ReferenceTypes {

    public static void main(String[] args) {
        // String - most common reference type
        String str1 = "Hello";
        String str2 = "Hello";
        String str3 = new String("Hello");

        System.out.println("=== Reference Types Demo ===");
        System.out.println("str1: " + str1);
        System.out.println("str2: " + str2);
        System.out.println("str3: " + str3);
        System.out.println("str1 == str2: " + (str1 == str2)); // true (String pool)
        System.out.println("str1 == str3: " + (str1 == str3)); // false (different objects)
        System.out.println("str1.equals(str3): " + str1.equals(str3)); // true (content)

        // Wrapper classes
        Integer wrapperInt = 42;
        int primitiveInt = wrapperInt; // Unboxing
        Integer wrapperAgain = primitiveInt; // Autoboxing

        System.out.println("\n--- Wrapper Classes ---");
        System.out.println("Integer: " + wrapperInt + " (class: " + wrapperInt.getClass().getSimpleName() + ")");
        System.out.println("Double: " + 3.14 + " (class: " + Double.class.getSimpleName() + ")");

        // Wrapper utility methods
        System.out.println("Integer.toString(42): " + Integer.toString(42));
        System.out.println("Integer.parseInt(\"123\"): " + Integer.parseInt("123"));
        System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE);
        System.out.println("Double.isNaN(0.0/0.0): " + Double.isNaN(0.0 / 0.0));

        // null reference
        String nullRef = null;
        System.out.println("\n--- null Reference ---");
        System.out.println("nullRef: " + nullRef);
        System.out.println("nullRef == null: " + (nullRef == null));
        
        // NullPointerException demo (commented out)
        // System.out.println(nullRef.length()); // Throws NPE!

        // Arrays are reference types
        int[] numbers = {1, 2, 3};
        int[] sameNumbers = numbers;
        int[] copyNumbers = numbers.clone();
        
        System.out.println("\n--- Arrays (Reference Types) ---");
        System.out.println("numbers == sameNumbers: " + (numbers == sameNumbers)); // true
        System.out.println("numbers == copyNumbers: " + (numbers == copyNumbers)); // false
        System.out.println("Arrays.equals(numbers, copyNumbers): " + java.util.Arrays.equals(numbers, copyNumbers)); // true
    }
}