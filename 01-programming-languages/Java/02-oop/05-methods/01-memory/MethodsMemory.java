package academy.javaengineering.oop.memory;

public class MethodsMemory {

    static class Calculator {
        int add(int a, int b) { return a + b; }
        void printSum(int a, int b) { System.out.println(a + b); }
    }

    public static void main(String[] args) {
        System.out.println("=== Methods Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Method Stack Frame
        System.out.println("--- Method Stack Frame ---");
        System.out.println("Each method call creates a frame");
        System.out.println("Frame contains: local variables, params");
        System.out.println("Frame pushed on call, popped on return");

        // 2. Parameter Passing
        System.out.println("\n--- Parameter Passing ---");
        System.out.println("Primitives: value copied to frame");
        System.out.println("Objects: reference copied to frame");
        System.out.println("Both passed by value");

        // 3. Recursion Memory
        System.out.println("\n--- Recursion Memory ---");
        System.out.println("Each recursive call adds a frame");
        System.out.println("Stack overflow if too deep");
        System.out.println("Tail recursion optimization (Java doesn't support)");

        // 4. Method Memory Overhead
        System.out.println("\n--- Method Overhead ---");
        System.out.println("Method metadata: ~16 bytes per method");
        System.out.println("Method table: stored in class");
        System.out.println("Virtual dispatch: ~2-3 cycles");
    }
}
