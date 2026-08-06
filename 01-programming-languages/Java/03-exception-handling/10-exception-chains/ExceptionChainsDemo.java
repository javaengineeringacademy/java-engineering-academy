package exceptionchains;

/**
 * Exception Chaining Demo - Complete Guide
 * 
 * Exception chaining connects exceptions together, preserving the full
 * causal chain from low-level to high-level exceptions.
 */
public class ExceptionChainsDemo {

    // ==========================================
    // SECTION 1: Basic Exception Chaining
    // ==========================================
    static class ChainingBasics {

        // Simple exception chaining with constructor
        static void demonstrateBasicChaining() {
            System.out.println("=== Basic Exception Chaining ===\n");

            try {
                try {
                    // Low-level exception
                    throw new ArithmeticException("Division by zero");
                } catch (ArithmeticException e) {
                    // Chain it into a higher-level exception
                    throw new RuntimeException("Data processing failed", e);
                }
            } catch (RuntimeException e) {
                System.out.println("Caught: " + e.getMessage());
                System.out.println("Cause: " + e.getCause());
                System.out.println("Cause message: " + e.getCause().getMessage());
            }
        }

        // Chaining with initCause() method
        static void demonstrateInitCause() {
            System.out.println("\n=== initCause() Chaining ===\n");

            try {
                try {
                    throw new NumberFormatException("Invalid number format");
                } catch (NumberFormatException original) {
                    RuntimeException wrapper = new RuntimeException("Configuration error");
                    wrapper.initCause(original); // Set cause after construction
                    throw wrapper;
                }
            } catch (RuntimeException e) {
                System.out.println("Wrapped: " + e.getMessage());
                System.out.println("Original: " + e.getCause().getMessage());
            }
        }

        // Deep chaining - multiple levels
        static void demonstrateDeepChaining() {
            System.out.println("\n=== Deep Chaining (3 levels) ===\n");

            try {
                try {
                    try {
                        try {
                            // Deepest level
                            throw new java.io.FileNotFoundException("config.properties");
                        } catch (java.io.FileNotFoundException e) {
                            // Middle layer
                            throw new RuntimeException("Failed to load configuration", e);
                        }
                    } catch (RuntimeException e) {
                        // Application layer
                        throw new IllegalStateException("Application startup failed", e);
                    }
                } catch (IllegalStateException e) {
                    // Top layer
                    throw new Exception("System initialization error", e);
                }
            } catch (Exception e) {
                System.out.println("Top-level: " + e.getMessage());
                System.out.println("  -> " + e.getCause().getMessage());
                System.out.println("    -> " + e.getCause().getCause().getMessage());
                System.out.println("      -> " + e.getCause().getCause().getCause().getMessage());
            }
        }
    }

    // ==========================================
    // SECTION 2: Cause Exception Handling
    // ==========================================
    static class CauseExceptionHandling {

        // Examining the cause chain
        static void demonstrateCauseInspection() {
            System.out.println("\n=== Cause Chain Inspection ===\n");

            Exception root = new RuntimeException("Root cause");
            Exception middle = new RuntimeException("Middle layer", root);
            Exception top = new RuntimeException("Top layer", middle);

            // Print full stack trace with cause
            System.out.println("Full exception with cause chain:");
            top.printStackTrace(System.out);

            // Manual traversal
            System.out.println("\n--- Manual Cause Traversal ---");
            Throwable current = top;
            int depth = 0;
            while (current != null) {
                System.out.println("  Level " + depth + ": " + current.getClass().getSimpleName()
                        + " - " + current.getMessage());
                current = current.getCause();
                depth++;
            }
        }

        // Finding root cause
        static Throwable findRootCause(Throwable throwable) {
            Throwable cause = throwable;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            return cause;
        }

        // Check if cause chain contains specific exception type
        static <T extends Throwable> boolean containsCause(Throwable throwable, Class<T> causeType) {
            Throwable current = throwable;
            while (current != null) {
                if (causeType.isInstance(current)) {
                    return true;
                }
                current = current.getCause();
            }
            return false;
        }

        static void demonstrateRootCauseFinding() {
            System.out.println("\n=== Root Cause Finding ===\n");

            Exception deep = new RuntimeException("Deep error",
                    new java.io.IOException("File not found",
                            new SecurityException("Access denied")));

            Throwable root = findRootCause(deep);
            System.out.println("Exception: " + deep.getMessage());
            System.out.println("Root cause: " + root.getClass().getSimpleName() + " - " + root.getMessage());

            System.out.println("\nContains IOException? " + containsCause(deep, java.io.IOException.class));
            System.out.println("Contains SecurityException? " + containsCause(deep, SecurityException.class));
            System.out.println("Contains ArithmeticException? " + containsCause(deep, ArithmeticException.class));
        }

        // Handling specific causes differently
        static void demonstrateCauseSpecificHandling() {
            System.out.println("\n=== Cause-Specific Handling ===\n");

            Exception[] testCases = {
                    new RuntimeException("DB error", new java.sql.SQLException("Connection timeout")),
                    new RuntimeException("File error", new java.io.FileNotFoundException("data.csv")),
                    new RuntimeException("Network error", new java.net.ConnectException("Server unreachable"))
            };

            for (Exception ex : testCases) {
                try {
                    throw ex;
                } catch (Exception e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof java.sql.SQLException) {
                        System.out.println("Database issue: " + cause.getMessage());
                    } else if (cause instanceof java.io.FileNotFoundException) {
                        System.out.println("File issue: " + cause.getMessage());
                    } else if (cause instanceof java.net.ConnectException) {
                        System.out.println("Network issue: " + cause.getMessage());
                    } else {
                        System.out.println("Unknown issue: " + cause.getMessage());
                    }
                }
            }
        }
    }

    // ==========================================
    // SECTION 3: Custom Exception Hierarchies
    // ==========================================
    static class ExceptionHierarchy {

        // Base application exception
        static class AppException extends Exception {
            private final String errorCode;

            public AppException(String message, String errorCode) {
                super(message);
                this.errorCode = errorCode;
            }

            public AppException(String message, String errorCode, Throwable cause) {
                super(message, cause);
                this.errorCode = errorCode;
            }

            public String getErrorCode() {
                return errorCode;
            }
        }

        // Validation exceptions
        static class ValidationException extends AppException {
            private final String field;

            public ValidationException(String field, String message) {
                super("Validation failed for '" + field + "': " + message, "VALIDATION_ERROR");
                this.field = field;
            }

            public String getField() {
                return field;
            }
        }

        static class RequiredFieldException extends ValidationException {
            public RequiredFieldException(String field) {
                super(field, "is required");
            }
        }

        static class InvalidFormatException extends ValidationException {
            public InvalidFormatException(String field, String expectedFormat) {
                super(field, "must match format: " + expectedFormat);
            }
        }

        // Data access exceptions
        static class DataAccessException extends AppException {
            public DataAccessException(String message, Throwable cause) {
                super(message, "DATA_ACCESS_ERROR", cause);
            }
        }

        static class EntityNotFoundException extends DataAccessException {
            public EntityNotFoundException(String entityType, Object id) {
                super(entityType + " not found with id: " + id, null);
            }
        }

        static class DuplicateEntityException extends DataAccessException {
            public DuplicateEntityException(String entityType, String field, Object value) {
                super(entityType + " already exists with " + field + ": " + value, null);
            }
        }

        // Service layer exceptions
        static class ServiceException extends AppException {
            public ServiceException(String message, Throwable cause) {
                super(message, "SERVICE_ERROR", cause);
            }
        }

        static class AuthorizationException extends ServiceException {
            public AuthorizationException(String action) {
                super("Not authorized to perform: " + action, null);
            }
        }

        static void demonstrateHierarchy() {
            System.out.println("\n=== Custom Exception Hierarchy ===\n");

            // Test validation exceptions
            try {
                throw new RequiredFieldException("email");
            } catch (ValidationException e) {
                System.out.println("Caught: " + e.getClass().getSimpleName());
                System.out.println("  Field: " + e.getField());
                System.out.println("  Error: " + e.getMessage());
                System.out.println("  Code: " + e.getErrorCode());
            }

            // Test data access exceptions
            try {
                throw new EntityNotFoundException("User", 42L);
            } catch (DataAccessException e) {
                System.out.println("\nCaught: " + e.getClass().getSimpleName());
                System.out.println("  Message: " + e.getMessage());
                System.out.println("  Code: " + e.getErrorCode());
            }

            // Test service exceptions
            try {
                throw new AuthorizationException("DELETE操作");
            } catch (ServiceException e) {
                System.out.println("\nCaught: " + e.getClass().getSimpleName());
                System.out.println("  Message: " + e.getMessage());
                System.out.println("  Code: " + e.getErrorCode());
            }
        }

        // Exception hierarchy with cause chaining
        static void demonstrateHierarchyChaining() {
            System.out.println("\n=== Hierarchy with Cause Chaining ===\n");

            try {
                try {
                    // Simulate low-level database error
                    throw new java.sql.SQLException("Connection pool exhausted");
                } catch (java.sql.SQLException e) {
                    throw new DataAccessException("Failed to fetch user data", e);
                }
            } catch (DataAccessException e) {
                System.out.println("Application exception: " + e.getMessage());
                System.out.println("Root cause: " + e.getCause().getMessage());
                System.out.println("Error code: " + e.getErrorCode());
            }

            try {
                try {
                    // Simulate validation with chained cause
                    throw new IllegalArgumentException("Invalid email format");
                } catch (IllegalArgumentException e) {
                    throw new ValidationException("email", "must be valid format", e);
                }
            } catch (ValidationException e) {
                System.out.println("\nValidation: " + e.getMessage());
                System.out.println("Root cause: " + e.getCause().getMessage());
            }
        }
    }

    // ==========================================
    // SECTION 4: Exception Wrapping Patterns
    // ==========================================
    static class WrappingPatterns {

        // Checked to unchecked wrapping
        static class DatabaseWrapper {

            static void executeQuery(String sql) throws DataAccessException {
                try {
                    // Simulate checked exception from JDBC
                    if (sql == null) {
                        throw new java.sql.SQLException("SQL cannot be null");
                    }
                    System.out.println("Executing: " + sql);
                } catch (java.sql.SQLException e) {
                    throw new DataAccessException("Query execution failed", e);
                }
            }
        }

        // Third-party to domain exception conversion
        static class ThirdPartyAdapter {

            static void processOrder(String orderId) throws AppException {
                try {
                    // Simulate third-party library throwing its own exceptions
                    if (orderId == null) {
                        throw new IllegalArgumentException("Order ID required");
                    }
                    if (!orderId.startsWith("ORD")) {
                        throw new IllegalStateException("Invalid order prefix");
                    }
                    System.out.println("Processing order: " + orderId);
                } catch (IllegalArgumentException e) {
                    throw new ValidationException("orderId", e.getMessage());
                } catch (IllegalStateException e) {
                    throw new ServiceException("Order processing failed", e);
                }
            }
        }

        static void demonstrateWrapping() {
            System.out.println("\n=== Exception Wrapping Patterns ===\n");

            // Database wrapping
            try {
                DatabaseWrapper.executeQuery(null);
            } catch (DataAccessException e) {
                System.out.println("Wrapped SQL error: " + e.getMessage());
                System.out.println("Original: " + e.getCause().getClass().getSimpleName());
            }

            // Third-party adapter
            try {
                ThirdPartyAdapter.processOrder("INVALID");
            } catch (AppException e) {
                System.out.println("\nConverted error: " + e.getMessage());
                System.out.println("Code: " + e.getErrorCode());
            }
        }
    }

    // ==========================================
    // SECTION 5: Exception Best Practices
    // ==========================================
    static class BestPractices {

        // Practice 1: Always preserve cause
        static void preserveCause() {
            System.out.println("=== Practice: Preserve the Cause ===\n");
            try {
                try {
                    throw new java.io.IOException("Disk full");
                } catch (java.io.IOException e) {
                    // GOOD: Preserve the cause
                    throw new RuntimeException("Write failed", e);
                    // BAD: Losing the cause
                    // throw new RuntimeException("Write failed");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Cause preserved: " + (e.getCause() != null));
            }
        }

        // Practice 2: Meaningful wrapping messages
        static void meaningfulMessages() {
            System.out.println("\n=== Practice: Meaningful Wrapping Messages ===\n");
            try {
                try {
                    throw new java.sql.SQLException("Timeout after 30s");
                } catch (java.sql.SQLException e) {
                    // GOOD: Context-specific message
                    throw new DataAccessException(
                            "Failed to load user profile for user_id=123", e);
                }
            } catch (DataAccessException e) {
                System.out.println("Contextual: " + e.getMessage());
                System.out.println("Cause: " + e.getCause().getMessage());
            }
        }

        // Practice 3: Exception hierarchy design
        static void hierarchyDesign() {
            System.out.println("\n=== Practice: Exception Hierarchy Design ===\n");
            System.out.println("Design principles:");
            System.out.println("  1. Base exception extends Exception (checked) or RuntimeException (unchecked)");
            System.out.println("  2. Group by error type (validation, data access, service)");
            System.out.println("  3. Include error code for programmatic handling");
            System.out.println("  4. Preserve cause chain in all constructors");
            System.out.println("  5. Add domain-specific fields (field name, entity type)");
        }

        static void demonstrateAll() {
            preserveCause();
            meaningfulMessages();
            hierarchyDesign();
        }
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║    EXCEPTION CHAINS DEMO                ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        ChainingBasics.demonstrateBasicChaining();
        ChainingBasics.demonstrateInitCause();
        ChainingBasics.demonstrateDeepChaining();

        CauseExceptionHandling.demonstrateCauseInspection();
        CauseExceptionHandling.demonstrateRootCauseFinding();
        CauseExceptionHandling.demonstrateCauseSpecificHandling();

        ExceptionHierarchy.demonstrateHierarchy();
        ExceptionHierarchy.demonstrateHierarchyChaining();

        WrappingPatterns.demonstrateWrapping();

        BestPractices.demonstrateAll();

        System.out.println("\nAll exception chaining demos complete!");
    }
}
