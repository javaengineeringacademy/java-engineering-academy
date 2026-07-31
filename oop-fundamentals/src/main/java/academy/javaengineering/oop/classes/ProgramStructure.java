package academy.javaengineering.oop.classes;

/**
 * Demonstrates program structure and execution flow.
 */
public final class ProgramStructure {

    public static void main(String[] args) {
        System.out.println("=== Java Program Structure ===");
        
        // Class declaration
        System.out.println("1. Package declaration (optional)");
        System.out.println("2. Import statements");
        System.out.println("3. Class declaration");
        System.out.println("4. Main method - entry point");
        
        // Compilation process
        System.out.println("\n--- Compilation Process ---");
        System.out.println("Source (.java) -> javac -> Bytecode (.class) -> JVM");
        
        // JVM execution
        System.out.println("\n--- JVM Execution ---");
        System.out.println("Class Loader -> Bytecode Verifier -> Interpreter/JIT -> Native Code");
        
        System.out.println("\n--- Expected Output ---");
        System.out.println("=== Java Program Structure ===");
        System.out.println("1. Package declaration (optional)");
        System.out.println("2. Import statements");
        System.out.println("3. Class declaration");
        System.out.println("4. Main method - entry point");
    }
}