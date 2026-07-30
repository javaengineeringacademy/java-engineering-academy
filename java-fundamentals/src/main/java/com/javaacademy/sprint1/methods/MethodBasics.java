package com.javaacademy.sprint1.methods;

/**
 * MethodBasics - Demonstrates method declaration, parameters, return values, and invocation.
 * 
 * <p><b>Method Signature:</b> {@code returnType methodName(parameterList)}
 * <ul>
 *   <li><b>Modifiers:</b> public, private, protected, static, final, abstract, synchronized</li>
 *   <li><b>Return type:</b> void, primitive, reference type</li>
 *   <li><b>Method name:</b> camelCase, verb-noun convention</li>
 *   <li><b>Parameters:</b> zero or more (type name, ...)</li>
 *   <li><b>Exception list:</b> throws clause</li>
 *   <li><b>Body:</b> statements in braces</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Method = recipe
 * - Parameters = ingredients
 * - Return value = finished dish
 * - Body = cooking instructions
 * - Calling method = following recipe
 * 
 * <p><b>Pass-by-Value:</b> Java is ALWAYS pass-by-value.
 * - Primitives: copy of value
 * - References: copy of reference (both point to same object)
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class MethodBasics {

    private MethodBasics() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Method Basics ===\n");

        // Static method call
        System.out.println("--- Static Methods ---");
        greet("Alice");
        int sum = add(10, 20);
        System.out.println("Sum: " + sum);

        // Method with multiple parameters
        System.out.println("\n--- Multiple Parameters ---");
        printInfo("Bob", 25, "Engineer");

        // Return values
        System.out.println("\n--- Return Values ---");
        double area = circleArea(5.0);
        System.out.println("Circle area (r=5): " + area);

        // Method chaining (builder pattern)
        System.out.println("\n--- Method Chaining ---");
        StringBuilder sb = new StringBuilder();
        sb.append("Hello").append(" ").append("World");
        System.out.println(sb);

        // Pass-by-value demo
        System.out.println("\n--- Pass-by-Value ---");
        int primitive = 10;
        modifyPrimitive(primitive);
        System.out.println("After modifyPrimitive: " + primitive); // Still 10!

        StringBuilder ref = new StringBuilder("Original");
        modifyReference(ref);
        System.out.println("After modifyReference: " + ref); // Changed!

        // Void method
        System.out.println("\n--- Void Method ---");
        printSeparator();

        // Expected output demonstrates method basics
    }

    // Method with no parameters, no return
    static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }

    // Method with return value
    static int add(int a, int b) {
        return a + b;
    }

    // Multiple parameters
    static void printInfo(String name, int age, String job) {
        System.out.printf("Name: %s, Age: %d, Job: %s%n", name, age, job);
    }

    // Calculation method
    static double circleArea(double radius) {
        return Math.PI * radius * radius;
    }

    // Demonstrates pass-by-value for primitives
    static void modifyPrimitive(int value) {
        value = 20; // Modifies local copy only
    }

    // Demonstrates pass-by-value for references (copy of reference)
    static void modifyReference(StringBuilder sb) {
        sb.append(" - Modified"); // Modifies SAME object!
    }

    // Void method (no return)
    static void printSeparator() {
        System.out.println("====================");
    }
}