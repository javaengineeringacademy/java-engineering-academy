package com.javaacademy.sprint1.basics;

/**
 * ProgramStructure - Demonstrates Java program anatomy and execution flow.
 *
 * <p><b>Real-world analogy:</b> A Java program is like a recipe book.
 * - The <b>class</b> is the book cover/title
 * - The <b>main method</b> is the "Start Here" page
 * - <b>Methods</b> are individual recipes
 * - <b>Variables</b> are ingredients
 * - <b>Statements</b> are cooking steps
 *
 * <p><b>Java Program Structure:</b>
 * <pre>
 * package declaration;        // Optional - organizes classes (like folders)
 * import statements;          // Optional - brings in other classes
 *
 * public class ClassName {    // Class declaration (exactly one public per file)
 *     // Fields (variables)     - State
 *     // Methods                - Behavior
 *     // Constructors           - Initialization
 *     // Inner classes          - Nested types
 * }
 * </pre>
 *
 * <p><b>Compilation & Execution:</b>
 * <ol>
 *   <li><b>Source code:</b> {@code .java} file (human-readable)</li>
 *   <li><b>Compile:</b> {@code javac FileName.java} → {@code .class} file (bytecode)</li>
 *   <li><b>Execute:</b> {@code java ClassName} → JVM runs bytecode</li>
 * </ol>
 *
 * <p><b>JVM Role:</b> Write Once, Run Anywhere (WORA)
 * <ul>
 *   <li>Bytecode is platform-independent</li>
 *   <li>JVM translates bytecode to machine code at runtime</li>
 *   <li>JIT (Just-In-Time) compiler optimizes hot paths</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class ProgramStructure {

    // Package-private field (default access)
    static int packagePrivateField = 10;

    // Private field - only accessible within this class
    private static String privateField = "Internal";

    // Public constant - accessible everywhere
    public static final String APP_NAME = "JavaAcademy";

    private ProgramStructure() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Main method - JVM entry point.
     * <p>
     * Signature requirements:
     * <ul>
     *   <li>{@code public} - JVM can call from outside</li>
     *   <li>{@code static} - Callable without instance</li>
     *   <li>{@code void} - No return value to JVM</li>
     *   <li>{@code main} - Exact name JVM looks for</li>
     *   <li>{@code String[] args} - Command-line arguments</li>
     * </ul>
     *
     * @param args command-line arguments (e.g., {@code java ProgramStructure arg1 arg2})
     */
    public static void main(String[] args) {
        System.out.println("=== Java Program Structure Demo ===");
        System.out.println("Application: " + APP_NAME);
        System.out.println("Package-private field: " + packagePrivateField);
        System.out.println("Private field (via method): " + getPrivateField());

        // Command-line arguments
        if (args.length > 0) {
            System.out.println("Arguments received: " + args.length);
            for (int i = 0; i < args.length; i++) {
                System.out.println("  args[" + i + "] = " + args[i]);
            }
        } else {
            System.out.println("No command-line arguments provided");
        }

        // Demonstrate class loading
        demonstrateClassLoading();

        // Expected output (no args):
        // === Java Program Structure Demo ===
        // Application: JavaAcademy
        // Package-private field: 10
        // Private field (via method): Internal
        // No command-line arguments provided
        // Class loaded: ProgramStructure
    }

    /**
     * Accessor for private field - demonstrates encapsulation.
     *
     * @return the private field value
     */
    private static String getPrivateField() {
        return privateField;
    }

    /**
     * Shows class loading behavior.
     */
    private static void demonstrateClassLoading() {
        System.out.println("Class loaded: " + ProgramStructure.class.getSimpleName());
        // Class loaded by Bootstrap → Extension → Application classloaders
    }
}