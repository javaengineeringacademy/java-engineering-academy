package academy.javaengineering.exceptions.exception;

/**
 * Demonstrates the Exception API: constructors, message, cause, stack trace, and chaining.
 *
 * <p>Exception is the base class for all checked exceptions in Java.
 * It extends Throwable and represents a condition that a reasonable
 * application might want to catch.</p>
 */
public class ExceptionDemo {

    // ============================================================
    // Custom exceptions to demonstrate various construction patterns
    // ============================================================

    /**
     * A checked exception representing a validation failure.
     */
    static class ValidationException extends Exception {
        private final String field;

        public ValidationException(String field, String message) {
            super(message);
            this.field = field;
        }

        public ValidationException(String field, String message, Throwable cause) {
            super(message, cause);
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    /**
     * A checked exception representing a service-level failure.
     */
    static class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }

        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ============================================================
    // Constructors
    // ============================================================

    /** No-arg constructor. Message is null. */
    static Exception noArgConstructor() {
        return new Exception();
    }

    /** Constructor with message only. */
    static Exception messageConstructor(String message) {
        return new Exception(message);
    }

    /** Constructor with message and cause. */
    static Exception messageAndCauseConstructor(String message, Throwable cause) {
        return new Exception(message, cause);
    }

    /** Constructor with cause only. */
    static Exception causeConstructor(Throwable cause) {
        return new Exception(cause);
    }

    // ============================================================
    // Message and localization
    // ============================================================

    /**
     * Demonstrates getMessage() vs getLocalizedMessage().
     *
     * <p>getMessage() returns the detail message. getLocalizedMessage()
     * returns a locale-specific version. The default implementation of
     * getLocalizedMessage() simply delegates to getMessage(), so you
     * must override it for actual localization.</p>
     */
    static void demonstrateMessage() {
        Exception e = new Exception("Disk full");

        // getMessage() returns the detail message
        String message = e.getMessage();

        // getLocalizedMessage() defaults to getMessage()
        String localized = e.getLocalizedMessage();

        System.out.println("getMessage(): " + message);
        System.out.println("getLocalizedMessage(): " + localized);
    }

    // ============================================================
    // Cause and chaining
    // ============================================================

    /**
     * Demonstrates exception chaining using constructors.
     *
     * <p>Exception chaining preserves the original exception as the cause
     * of a new exception. This is useful at architectural boundaries where
     * you translate exception types while preserving diagnostic information.</p>
     */
    static ServiceException chainWithConstructors() {
        try {
            throw new java.io.FileNotFoundException("config.yaml");
        } catch (java.io.FileNotFoundException e) {
            // Constructor-based chaining (preferred)
            return new ServiceException("Failed to load configuration", e);
        }
    }

    /**
     * Demonstrates exception chaining using initCause().
     *
     * <p>initCause() can only be called once. Calling it again throws
     * IllegalStateException. This prevents accidentally overwriting
     * the original cause.</p>
     */
    static ServiceException chainWithInitCause() {
        ServiceException result = new ServiceException("Database operation failed");
        try {
            throw new java.sql.SQLException("Connection refused");
        } catch (java.sql.SQLException e) {
            result.initCause(e);
        }
        return result;
    }

    // ============================================================
    // Stack trace manipulation
    // ============================================================

    /**
     * Demonstrates fillInStackTrace() and getStackTrace().
     *
     * <p>fillInStackTrace() is called automatically in the Throwable
     * constructor. It returns the same throwable with a completed stack
     * trace. You can override it in performance-critical code that does
     * not need stack traces.</p>
     */
    static void demonstrateStackTrace() {
        Exception e = new Exception("Stack trace demo");

        // getStackTrace() returns an array of StackTraceElement
        StackTraceElement[] trace = e.getStackTrace();

        System.out.println("Stack depth: " + trace.length);
        for (StackTraceElement element : trace) {
            System.out.printf("  %s.%s(%s:%d)%n",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber());
        }
    }

    /**
     * Demonstrates replacing the stack trace with setStackTrace().
     *
     * <p>This is useful when you want to provide a cleaner stack trace
     * to callers, removing internal framework frames.</p>
     */
    static Exception replaceStackTrace() {
        Exception e = new Exception("Cleaned exception");
        e.setStackTrace(new StackTraceElement[]{
                new StackTraceElement("MyClass", "myMethod", "MyClass.java", 42)
        });
        return e;
    }

    // ============================================================
    // toString output
    // ============================================================

    /**
     * Demonstrates toString() output for exceptions.
     *
     * <p>The default toString() returns the class name followed by the
     * message: "className: message". If the message is null, it returns
     * just the class name.</p>
     */
    static void demonstrateToString() {
        Exception withMessage = new Exception("Something went wrong");
        Exception withoutMessage = new Exception();

        System.out.println("With message: " + withMessage);
        System.out.println("Without message: " + withoutMessage);
    }

    // ============================================================
    // Suppressed exceptions
    // ============================================================

    /**
     * Demonstrates suppressed exceptions from try-with-resources.
     *
     * <p>When an exception is thrown from a try block and the close()
     * method also throws, the close exception is added as a suppressed
     * exception. Both are available to the catch block.</p>
     */
    static void demonstrateSuppressedExceptions() {
        AutoCloseable resource = new AutoCloseable() {
            @Override
            public void close() throws Exception {
                throw new Exception("Close failed");
            }
        };

        try {
            try (resource) {
                throw new Exception("Primary failure");
            }
        } catch (Exception e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed: " + suppressed.getMessage());
            }
        }
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) {
        System.out.println("=== Constructors ===");
        System.out.println("No-arg: " + noArgConstructor());
        System.out.println("Message: " + messageConstructor("File not found"));
        System.out.println("Cause: " + causeConstructor(new RuntimeException("root")));
        System.out.println("Message+Cause: " + messageAndCauseConstructor("wrapped", new RuntimeException("root")));

        System.out.println("\n=== Message ===");
        demonstrateMessage();

        System.out.println("\n=== Cause Chaining ===");
        ServiceException chained = chainWithConstructors();
        System.out.println("Exception: " + chained);
        System.out.println("Cause: " + chained.getCause());

        System.out.println("\n=== initCause ===");
        ServiceException fromInitCause = chainWithInitCause();
        System.out.println("Exception: " + fromInitCause);
        System.out.println("Cause: " + fromInitCause.getCause());

        System.out.println("\n=== Stack Trace ===");
        demonstrateStackTrace();

        System.out.println("\n=== Replaced Stack Trace ===");
        Exception replaced = replaceStackTrace();
        for (StackTraceElement element : replaced.getStackTrace()) {
            System.out.printf("  %s.%s(%s:%d)%n",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber());
        }

        System.out.println("\n=== toString ===");
        demonstrateToString();

        System.out.println("\n=== Suppressed Exceptions ===");
        demonstrateSuppressedExceptions();
    }
}
