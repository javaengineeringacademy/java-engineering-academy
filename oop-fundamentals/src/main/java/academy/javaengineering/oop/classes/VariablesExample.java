package academy.javaengineering.oop.classes;

/**
 * Demonstrates variable declaration and usage.
 */
public final class VariablesExample {

    public static void main(String[] args) {
        // Primitive types
        byte b = 100;
        short s = 30000;
        int i = 2_000_000_000;
        long l = 9_000_000_000L;
        float f = 3.14f;
        double d = 3.14159;
        char c = 'A';
        boolean flag = true;

        // Reference types
        String str = "Hello";
        String[] arr = {"a", "b", "c"};

        System.out.println("Primitive types:");
        System.out.printf("byte: %d, short: %d, int: %d, long: %d%n", b, s, i, l);
        System.out.printf("float: %.2f, double: %.5f, char: %c, boolean: %b%n", f, d, c, flag);
        
        System.out.println("\nReference types:");
        System.out.println("String: " + str);
        System.out.println("Array: " + java.util.Arrays.toString(arr));
    }
}