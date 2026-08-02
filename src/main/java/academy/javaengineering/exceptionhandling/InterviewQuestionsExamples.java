package academy.javaengineering.exceptionhandling;

/**
 * Interview Questions Examples
 * 
 * Demonstrates common interview questions about exception handling.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class InterviewQuestionsExamples {

    /**
     * Demonstrates interview question 1: Error vs Exception.
     */
    public static void errorVsException() {
        System.out.println("=== Interview Question 1: Error vs Exception ===\n");
        
        System.out.println("Error:");
        System.out.println("- Represents serious problems");
        System.out.println("- Should not be caught by applications");
        System.out.println("- Examples: OutOfMemoryError, StackOverflowError");
        System.out.println("- System-level issues");
        
        System.out.println("\nException:");
        System.out.println("- Represents conditions applications can handle");
        System.out.println("- Should be caught and handled");
        System.out.println("- Examples: IOException, NullPointerException");
        System.out.println("- Application-level issues");
        
        System.out.println();
    }

    /**
     * Demonstrates interview question 2: Checked vs Unchecked.
     */
    public static void checkedVsUnchecked() {
        System.out.println("=== Interview Question 2: Checked vs Unchecked Exceptions ===\n");
        
        System.out.println("Checked Exceptions:");
        System.out.println("- Must be declared or caught");
        System.out.println("- Compiler enforces handling");
        System.out.println("- Represent recoverable conditions");
        System.out.println("- Extend Exception but not RuntimeException");
        System.out.println("- Examples: IOException, SQLException");
        
        System.out.println("\nUnchecked Exceptions:");
        System.out.println("- Don't need to be declared");
        System.out.println("- Extend RuntimeException");
        System.out.println("- Represent programming errors");
        System.out.println("- Examples: NullPointerException, IllegalArgumentException");
        
        System.out.println();
    }

    /**
     * Demonstrates interview question 3: Exception propagation.
     */
    public static void exceptionPropagation() {
        System.out.println("=== Interview Question 3: Exception Propagation ===\n");
        
        System.out.println("When an exception is thrown:");
        System.out.println("1. JVM creates exception object");
        System.out.println("2. Normal flow stops");
        System.out.println("3. JVM searches for handler");
        System.out.println("4. If found, execution continues there");
        System.out.println("5. If not, exception propagates up");
        System.out.println("6. Process repeats until handler found");
        System.out.println("7. If no handler, program terminates");
        
        System.out.println();
    }

    /**
     * Demonstrates interview question 4: Finally block behavior.
     */
    public static void finallyBehavior() {
        System.out.println("=== Interview Question 4: Finally Block Behavior ===\n");
        
        System.out.println("Finally block:");
        System.out.println("- Always executes (with exceptions)");
        System.out.println("- Executes even if exception is thrown");
        System.out.println("- Executes even if return statement is in try");
        System.out.println("- Does NOT execute if System.exit() is called");
        System.out.println("- Does NOT execute if JVM crashes");
        System.out.println("- Does NOT execute if thread is killed");
        
        System.out.println();
    }

    /**
     * Demonstrates interview question 5: Exception chaining.
     */
    public static void exceptionChaining() {
        System.out.println("=== Interview Question 5: Exception Chaining ===\n");
        
        System.out.println("Exception chaining:");
        System.out.println("- Wrapping one exception inside another");
        System.out.println("- Preserves the original cause");
        System.out.println("- Important for debugging");
        System.out.println("- Use Throwable constructor with cause");
        System.out.println("- Use addSuppressed() for suppressed exceptions");
        
        System.out.println();
    }

    /**
     * Demonstrates interview question 6: Multi-catch block.
     */
    public static void multiCatchBlock() {
        System.out.println("=== Interview Question 6: Multi-Catch Block ===\n");
        
        System.out.println("Multi-catch block (Java 7+):");
        System.out.println("- Catch multiple exception types in one block");
        System.out.println("- Use pipe (|) operator");
        System.out.println("- Use when handling is identical");
        System.out.println("- Exception variable is implicitly final");
        System.out.println("- Reduces code duplication");
        
        System.out.println();
    }

    /**
     * Demonstrates interview question 7: Try-with-resources.
     */
    public static void tryWithResources() {
        System.out.println("=== Interview Question 7: Try-With-Resources ===\n");
        
        System.out.println("Try-with-resources (Java 7+):");
        System.out.println("- Automatic resource management");
        System.out.println("- Resources must implement AutoCloseable");
        System.out.println("- Resources closed in reverse order");
        System.out.println("- Suppressed exceptions preserved");
        System.out.println("- Reduces boilerplate code");
        System.out.println("- Prevents resource leaks");
        
        System.out.println();
    }

    /**
     * Demonstrates interview question 8: Custom exceptions.
     */
    public static void customExceptions() {
        System.out.println("=== Interview Question 8: Custom Exceptions ===\n");
        
        System.out.println("When to create custom exceptions:");
        System.out.println("- Need to distinguish error types");
        System.out.println("- Need domain-specific information");
        System.out.println("- Need custom behavior");
        System.out.println("- Standard exceptions don't fit");
        
        System.out.println("\nBest practices:");
        System.out.println("- Follow naming conventions");
        System.out.println("- Provide meaningful messages");
        System.out.println("- Include contextual information");
        System.out.println("- Override toString()");
        System.out.println("- Document exceptions");
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        errorVsException();
        checkedVsUnchecked();
        exceptionPropagation();
        finallyBehavior();
        exceptionChaining();
        multiCatchBlock();
        tryWithResources();
        customExceptions();
    }
}
