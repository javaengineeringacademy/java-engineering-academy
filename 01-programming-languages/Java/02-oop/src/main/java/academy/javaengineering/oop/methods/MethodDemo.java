package academy.javaengineering.oop.methods;

/**
 * Demonstrates method types: instance, static, overloaded, and final.
 *
 * <p>Methods define the behavior of objects. Java supports several
 * method categories for different use cases.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Instance methods - operate on object state</li>
 *   <li>Static methods - belong to the class, no object needed</li>
 *   <li>Method overloading - same name, different parameters</li>
 *   <li>Final methods - cannot be overridden</li>
 * </ul>
 */
public class MethodDemo {

    private static int instanceCount = 0;
    private String name;

    public MethodDemo(String name) {
        this.name = name;
        instanceCount++;
    }

    // Instance method
    public String greet() {
        return "Hello, I'm " + name;
    }

    // Overloaded greet with custom greeting
    public String greet(String greeting) {
        return greeting + ", I'm " + name;
    }

    // Static method
    public static int getInstanceCount() {
        return instanceCount;
    }

    // Static overloaded method
    public static int add(int a, int b) {
        return a + b;
    }

    public static double add(double a, double b) {
        return a + b;
    }

    // Varargs method
    public static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }

    // Final method - cannot be overridden
    public final String getClassName() {
        return this.getClass().getSimpleName();
    }

    public String getName() { return name; }

    public static void main(String[] args) {
        System.out.println("=== Method Types Demo ===\n");

        // Instance methods
        MethodDemo obj = new MethodDemo("Alice");
        System.out.println(obj.greet());
        System.out.println(obj.greet("Good morning"));

        // Static methods
        System.out.println("\nInstance count: " + MethodDemo.getInstanceCount());
        System.out.println("Add ints: " + MethodDemo.add(5, 3));
        System.out.println("Add doubles: " + MethodDemo.add(2.5, 3.7));

        // Varargs
        System.out.println("Sum: " + MethodDemo.sum(1, 2, 3, 4, 5));
    }
}
