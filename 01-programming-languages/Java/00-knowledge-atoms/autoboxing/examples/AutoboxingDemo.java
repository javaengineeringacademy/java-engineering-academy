public class AutoboxingDemo {
    public static void main(String[] args) {
        // Autoboxing: primitive → wrapper
        Integer num1 = 42;          // int → Integer
        Double pi = 3.14;          // double → Double
        Boolean flag = true;       // boolean → Boolean
        Character ch = 'A';        // char → Character

        System.out.println("=== Autoboxing Examples ===");
        System.out.println("Integer num1: " + num1);
        System.out.println("Double pi: " + pi);
        System.out.println("Boolean flag: " + flag);
        System.out.println("Character ch: " + ch);

        // Unboxing: wrapper → primitive
        int value = num1;          // Integer → int
        double d = pi;             // Double → double
        boolean b = flag;          // Boolean → boolean
        char c = ch;               // Character → char

        System.out.println("\n=== Unboxing Examples ===");
        System.out.println("int value: " + value);
        System.out.println("double d: " + d);
        System.out.println("boolean b: " + b);
        System.out.println("char c: " + c);

        // Autoboxing in arithmetic
        Integer a = 10;
        Integer b2 = 20;
        int sum = a + b2;  // Both unboxed, then added
        System.out.println("\n=== Arithmetic with Autoboxing ===");
        System.out.println("a + b = " + sum);

        // Method overloading demonstration
        System.out.println("\n=== Method Overloading ===");
        process(42);                // Calls process(int)
        process(Integer.valueOf(42)); // Calls process(Integer)
    }

    public static void process(int value) {
        System.out.println("process(int): " + value);
    }

    public static void process(Integer value) {
        System.out.println("process(Integer): " + value);
    }
}
