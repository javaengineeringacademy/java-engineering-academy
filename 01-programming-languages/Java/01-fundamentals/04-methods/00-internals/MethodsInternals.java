package academy.javaengineering.fundamentals.methods;

/**
 * Demonstrates method execution internals in Java.
 */
public class MethodsInternals {

    public static void main(String[] args) {
        System.out.println("=== Methods Internals Demo ===\n");

        // 1. Static vs instance method
        System.out.println("--- Static vs Instance Methods ---");
        int staticResult = MethodsInternals.addStatic(3, 5);
        System.out.println("Static method call: addStatic(3, 5) = " + staticResult);

        MethodsInternals calculator = new MethodsInternals();
        int instanceResult = calculator.addInstance(3, 5);
        System.out.println("Instance method call: addInstance(3, 5) = " + instanceResult);

        // 2. Method overloading
        System.out.println("\n--- Method Overloading ---");
        System.out.println("add(3, 5) = " + add(3, 5));
        System.out.println("add(3.0, 5.0) = " + add(3.0, 5.0));
        System.out.println("add(3, 5, 7) = " + add(3, 5, 7));
        System.out.println("add(\"Hello\", \"World\") = " + add("Hello", "World"));

        // 3. Recursion stack frames
        System.out.println("\n--- Recursion Stack Frames ---");
        System.out.println("factorial(5) = " + factorial(5));
        System.out.println("Each recursive call adds a stack frame");

        // 4. Method with varargs
        System.out.println("\n--- Varargs Method ---");
        System.out.println("average(1, 2, 3, 4, 5) = " + average(1, 2, 3, 4, 5));
        System.out.println("average(10) = " + average(10));

        System.out.println("\n=== Internals Demo Complete ===");
    }

    static int addStatic(int a, int b) {
        return a + b;
    }

    int addInstance(int a, int b) {
        return a + b;
    }

    static int add(int a, int b) { return a + b; }
    static double add(double a, double b) { return a + b; }
    static int add(int a, int b, int c) { return a + b + c; }
    static String add(String a, String b) { return a + " " + b; }

    static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    static double average(int... numbers) {
        int sum = 0;
        for (int n : numbers) sum += n;
        return (double) sum / numbers.length;
    }
}
