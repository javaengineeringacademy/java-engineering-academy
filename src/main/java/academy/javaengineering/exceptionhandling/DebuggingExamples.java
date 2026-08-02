package academy.javaengineering.exceptionhandling;

import java.util.logging.Logger;

/**
 * Debugging Examples
 * 
 * Demonstrates debugging techniques for exception handling.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class DebuggingExamples {

    private static final Logger logger = Logger.getLogger(DebuggingExamples.class.getName());

    /**
     * Demonstrates stack trace analysis.
     */
    public static void stackTraceAnalysis() {
        System.out.println("=== Stack Trace Analysis ===\n");
        
        try {
            riskyOperation();
        } catch (Exception e) {
            System.out.println("Exception Type: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            System.out.println("\nFull Stack Trace:");
            e.printStackTrace();
            
            System.out.println("\nAnalyzing Stack Trace:");
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (int i = 0; i < Math.min(3, stackTrace.length); i++) {
                StackTraceElement element = stackTrace[i];
                System.out.printf("  %d. %s.%s() at %s:%d%n",
                    i + 1, element.getClassName(), element.getMethodName(),
                    element.getFileName(), element.getLineNumber());
            }
        }
        
        System.out.println();
    }

    static void riskyOperation() throws Exception {
        methodA();
    }

    static void methodA() throws Exception {
        methodB();
    }

    static void methodB() throws Exception {
        methodC();
    }

    static void methodC() throws Exception {
        throw new Exception("Error in methodC");
    }

    /**
     * Demonstrates exception cause chain.
     */
    public static void causeChain() {
        System.out.println("=== Exception Cause Chain ===\n");
        
        try {
            processData();
        } catch (DataException e) {
            System.out.println("Exception: " + e.getMessage());
            
            Throwable cause = e.getCause();
            int depth = 1;
            while (cause != null) {
                System.out.printf("  Cause %d: [%s] %s%n", 
                    depth, cause.getClass().getSimpleName(), cause.getMessage());
                cause = cause.getCause();
                depth++;
            }
        }
        
        System.out.println();
    }

    static void processData() throws DataException {
        try {
            parseData();
        } catch (ParseException e) {
            throw new DataException("Data processing failed", e);
        }
    }

    static void parseData() throws ParseException {
        try {
            Integer.parseInt("invalid");
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid format", e);
        }
    }

    /**
     * Demonstrates suppressed exceptions.
     */
    public static void suppressedExceptions() {
        System.out.println("=== Suppressed Exceptions ===\n");
        
        Exception originalException = null;
        
        try {
            try {
                throw new RuntimeException("Original exception");
            } finally {
                try {
                    throw new RuntimeException("Exception in finally");
                } catch (RuntimeException suppressed) {
                    if (originalException != null) {
                        originalException.addSuppressed(suppressed);
                    }
                    throw suppressed;
                }
            }
        } catch (RuntimeException e) {
            originalException = e;
            System.out.println("Main exception: " + e.getMessage());
            
            if (originalException.getSuppressed().length > 0) {
                System.out.println("Suppressed exceptions:");
                for (Throwable suppressed : originalException.getSuppressed()) {
                    System.out.println("  - " + suppressed.getMessage());
                }
            }
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception logging.
     */
    public static void exceptionLogging() {
        System.out.println("=== Exception Logging ===\n");
        
        try {
            riskyOperation();
        } catch (Exception e) {
            // Log with different levels
            logger.severe("SEVERE: " + e.getMessage());
            logger.warning("WARNING: " + e.getMessage());
            logger.info("INFO: " + e.getMessage());
            
            // Log with context
            logger.severe(String.format(
                "Operation failed - Type: %s, Message: %s, Class: %s",
                e.getClass().getSimpleName(),
                e.getMessage(),
                e.getClass().getPackageName()));
        }
        
        System.out.println();
    }

    /**
     * Demonstrates exception debugging tips.
     */
    public static void debuggingTips() {
        System.out.println("=== Exception Debugging Tips ===\n");
        
        System.out.println("1. Read the stack trace from top to bottom");
        System.out.println("2. Find your code in the stack trace first");
        System.out.println("3. Check the exception message for context");
        System.out.println("4. Follow the cause chain to find root cause");
        System.out.println("5. Check for suppressed exceptions");
        System.out.println("6. Verify resource cleanup in finally blocks");
        System.out.println("7. Check thread state for concurrency issues");
        System.out.println("8. Use debugger breakpoints on exception-throwing code");
        System.out.println("9. Add logging before and after risky operations");
        System.out.println("10. Use exception filters in your IDE");
        
        System.out.println();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        stackTraceAnalysis();
        causeChain();
        suppressedExceptions();
        exceptionLogging();
        debuggingTips();
    }

    // Supporting exception classes

    static class DataException extends Exception {
        DataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class ParseException extends Exception {
        ParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
