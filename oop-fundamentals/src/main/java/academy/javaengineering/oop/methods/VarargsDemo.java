package academy.javaengineering.oop.methods;

/**
 * Demonstrates method references.
 */
public final class VarargsDemo {

    public static void main(String[] args) {
        printNumbers(1, 2, 3);
        printNumbers(10, 20, 30, 40, 50);
        printNumbers(); // Empty varargs
        
        // Passing array
        int[] numbers = {1, 2, 3, 4, 5};
        printNumbers(numbers);
    }

    static void printNumbers(int... numbers) {
        System.out.print("Numbers: ");
        for (int n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println("(count: " + numbers.length + ")");
    }

    // Overloading with varargs
    static void greet(String greeting, String... names) {
        for (String name : names) {
            System.out.println(greeting + ", " + name + "!");
        }
    }

    // Method with required parameter + varargs
    static double average(double first, double... rest) {
        double sum = first;
        for (double v : rest) sum += v;
        return sum / (1 + rest.length);
    }
}