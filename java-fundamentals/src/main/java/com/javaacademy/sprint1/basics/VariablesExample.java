package com.javaacademy.sprint1.basics;

/**
 * VariablesExample - Demonstrates variable declaration, initialization, and scope.
 *
 * <p><b>Real-world analogy:</b> Variables are like labeled boxes where you store values.
 * The label (name) tells you what's inside, the type determines what <i>kind</i> of
 * item fits in the box, and the value is the actual content.
 *
 * <p><b>Key concepts:</b>
 * <ul>
 *   <li><b>Declaration:</b> {@code int age;} - reserve space, assign name</li>
 *   <li><b>Initialization:</b> {@code int age = 25;} - put value in box</li>
 *   <li><b>Assignment:</b> {@code age = 26;} - replace value</li>
 *   <li><b>Scope:</b> Where the variable is visible (block, method, class)</li>
 *   <li><b>Lifetime:</b> When memory is allocated/freed</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class VariablesExample {

    // Class-level (instance) variable - default value: 0
    private static int classLevelVariable = 100;

    /** Constant - by convention UPPER_SNAKE_CASE, static final */
    private static final double PI = 3.14159;

    private VariablesExample() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Demonstrates variable types and scopes.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        // Local variables - must be initialized before use
        int localInt = 42;
        double localDouble = 3.14;
        boolean localBoolean = true;
        char localChar = 'A';
        String localString = "Java";

        // Type inference (Java 10+) - var infers type from initializer
        var inferredInt = 100;       // int
        var inferredString = "Hello"; // String
        var inferredDouble = 2.5;     // double

        // Multiple declaration (same type)
        int a = 1, b = 2, c = 3;

        // Scope demonstration
        demonstrateScope();

        // Constants
        System.out.println("PI = " + PI);

        // Expected output:
        // Local int: 42
        // Local double: 3.14
        // Local boolean: true
        // Local char: A
        // Local String: Java
        // Inferred int: 100
        // Inferred String: Hello
        // Inferred double: 2.5
        // Multiple: a=1, b=2, c=3
        // Block scope variable: 999
        // PI = 3.14159
        System.out.println("Local int: " + localInt);
        System.out.println("Local double: " + localDouble);
        System.out.println("Local boolean: " + localBoolean);
        System.out.println("Local char: " + localChar);
        System.out.println("Local String: " + localString);
        System.out.println("Inferred int: " + inferredInt);
        System.out.println("Inferred String: " + inferredString);
        System.out.println("Inferred double: " + inferredDouble);
        System.out.println("Multiple: a=" + a + ", b=" + b + ", c=" + c);
    }

    /**
     * Demonstrates block scope - variables die at closing brace.
     */
    private static void demonstrateScope() {
        int methodScope = 10; // Method scope

        if (true) {
            int blockScope = 999; // Block scope - only visible inside {}
            System.out.println("Block scope variable: " + blockScope);
        }
        // System.out.println(blockScope); // COMPILE ERROR - out of scope

        // Method scope variable still accessible
        System.out.println("Method scope variable: " + methodScope);
    }
}