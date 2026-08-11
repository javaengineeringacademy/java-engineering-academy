package academy.javaengineering.exceptions.chaining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Comprehensive demo of exception chaining in Java.
 *
 * <p>This class demonstrates:
 * <ul>
 *   <li>Basic exception chaining with constructors</li>
 *   <li>Using initCause() for chaining</li>
 *   <li>Cause retrieval and traversal</li>
 *   <li>Exception translation pattern</li>
 *   <li>Root cause analysis</li>
 *   <li>Production patterns</li>
 * </ul>
 *
 * <p>Google Java Style: no comments, clean formatting.
 */
public class ExceptionChaining {

    // ──────────────────────────────────────────────
    // 1. Basic exception chaining with constructors
    // ──────────────────────────────────────────────

    public static class ServiceException extends Exception {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DataAccessException extends RuntimeException {
        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DomainException extends RuntimeException {
        public DomainException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // 2. Demo: Basic chaining
    // ──────────────────────────────────────────────

    public static void demoBasicChaining() {
        System.out.println("=== Demo: Basic Exception Chaining ===");
        try {
            throw new IOException("Disk full");
        } catch (IOException e) {
            try {
                throw new ServiceException("Failed to write data", e);
            } catch (ServiceException e2) {
                System.out.println("Caught: " + e2.getMessage());
                System.out.println("Cause: " + e2.getCause().getMessage());
                System.out.println("Full stack trace:");
                e2.printStackTrace(System.out);
            }
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 3. Demo: initCause()
    // ──────────────────────────────────────────────

    public static void demoInitCause() {
        System.out.println("=== Demo: initCause() ===");
        try {
            throw new IOException("Connection timeout");
        } catch (IOException e) {
            ServiceException ex = new ServiceException("Service unavailable");
            ex.initCause(e);
            System.out.println("Caught: " + ex.getMessage());
            System.out.println("Cause: " + ex.getCause().getMessage());
            ex.printStackTrace(System.out);
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 4. Demo: Cause traversal
    // ──────────────────────────────────────────────

    public static void demoCauseTraversal() {
        System.out.println("=== Demo: Cause Traversal ===");
        try {
            try {
                try {
                    throw new IOException("Low-level I/O error");
                } catch (IOException e) {
                    throw new DataAccessException("Data access failed", e);
                }
            } catch (DataAccessException e) {
                throw new ServiceException("Service failed", e);
            }
        } catch (ServiceException e) {
            System.out.println("Full cause chain:");
            Throwable cause = e;
            int depth = 0;
            while (cause != null) {
                System.out.printf("  Cause #%d: [%s] %s%n",
                        depth, cause.getClass().getSimpleName(), cause.getMessage());
                cause = cause.getCause();
                depth++;
            }
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 5. Demo: Root cause analysis
    // ──────────────────────────────────────────────

    public static Throwable getRootCause(Throwable e) {
        Throwable cause = e.getCause();
        while (cause != null) {
            Throwable next = cause.getCause();
            if (next == null) {
                return cause;
            }
            cause = next;
        }
        return e;
    }

    public static void demoRootCause() {
        System.out.println("=== Demo: Root Cause Analysis ===");
        try {
            try {
                try {
                    throw new IOException("Network error");
                } catch (IOException e) {
                    throw new DataAccessException("Database error", e);
                }
            } catch (DataAccessException e) {
                throw new ServiceException("Service error", e);
            }
        } catch (ServiceException e) {
            Throwable root = getRootCause(e);
            System.out.println("Root cause: " + root.getClass().getSimpleName()
                    + " - " + root.getMessage());
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 6. Demo: Exception translation
    // ──────────────────────────────────────────────

    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserRepository {
        public User findById(long id) throws UserNotFoundException {
            try {
                simulateDatabaseCall(id);
                return new User(id, "User " + id);
            } catch (IOException e) {
                throw new UserNotFoundException("User not found: " + id, e);
            }
        }

        private void simulateDatabaseCall(long id) throws IOException {
            if (id < 0) {
                throw new IOException("Invalid ID: " + id);
            }
            if (id == 999) {
                throw new IOException("Connection refused");
            }
        }
    }

    public static void demoExceptionTranslation() {
        System.out.println("=== Demo: Exception Translation ===");
        UserRepository repo = new UserRepository();
        try {
            repo.findById(999);
        } catch (UserNotFoundException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
            System.out.println("Root cause: " + getRootCause(e).getMessage());
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 7. Demo: Finding specific exception in chain
    // ──────────────────────────────────────────────

    public static <T extends Throwable> T findCauseInChain(
            Throwable e, Class<T> type) {
        Throwable cause = e;
        while (cause != null) {
            if (type.isInstance(cause)) {
                return type.cast(cause);
            }
            cause = cause.getCause();
        }
        return null;
    }

    public static void demoFindCause() {
        System.out.println("=== Demo: Finding Cause in Chain ===");
        try {
            try {
                try {
                    throw new IOException("Network error");
                } catch (IOException e) {
                    throw new DataAccessException("Database error", e);
                }
            } catch (DataAccessException e) {
                throw new ServiceException("Service error", e);
            }
        } catch (ServiceException e) {
            IOException io = findCauseInChain(e, IOException.class);
            System.out.println("Found IOException: " + (io != null ? io.getMessage() : "not found"));

            DataAccessException da = findCauseInChain(e, DataAccessException.class);
            System.out.println("Found DataAccessException: " + (da != null ? da.getMessage() : "not found"));
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 8. Demo: Composite exception (multiple causes)
    // ──────────────────────────────────────────────

    public static class CompositeException extends RuntimeException {
        private final List<Throwable> causes;

        public CompositeException(String message, List<Throwable> causes) {
            super(message);
            this.causes = Collections.unmodifiableList(new ArrayList<>(causes));
        }

        public List<Throwable> getCauses() {
            return causes;
        }

        @Override
        public void printStackTrace() {
            super.printStackTrace();
            for (int i = 0; i < causes.size(); i++) {
                System.out.println("  Cause #" + i + ":");
                causes.get(i).printStackTrace();
            }
        }
    }

    public static void demoCompositeException() {
        System.out.println("=== Demo: Composite Exception ===");
        List<Throwable> causes = new ArrayList<>();
        causes.add(new IOException("Error in task 1"));
        causes.add(new IOException("Error in task 2"));
        causes.add(new IOException("Error in task 3"));

        CompositeException ce = new CompositeException("Multiple tasks failed", causes);
        System.out.println("Message: " + ce.getMessage());
        System.out.println("Number of causes: " + ce.getCauses().size());
        for (Throwable cause : ce.getCauses()) {
            System.out.println("  - " + cause.getMessage());
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 9. Demo: Exception translation at layers
    // ──────────────────────────────────────────────

    public static class InfrastructureException extends RuntimeException {
        public InfrastructureException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class PresentationException extends RuntimeException {
        public PresentationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void demoLayeredTranslation() {
        System.out.println("=== Demo: Layered Exception Translation ===");
        try {
            try {
                try {
                    throw new IOException("Disk failure");
                } catch (IOException e) {
                    throw new InfrastructureException("Infrastructure failure", e);
                }
            } catch (InfrastructureException e) {
                throw new BusinessException("Business logic error", e);
            }
        } catch (BusinessException e) {
            System.out.println("Caught in presentation layer:");
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  Cause: " + e.getCause().getMessage());
            System.out.println("  Root cause: " + getRootCause(e).getMessage());
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 10. Demo: Exception logging with context
    // ──────────────────────────────────────────────

    public static void logExceptionWithChain(Throwable e, String context) {
        System.out.println("[LOG] Context: " + context);
        Throwable current = e;
        int depth = 0;
        while (current != null) {
            System.out.printf("[LOG] Cause #%d: [%s] %s%n",
                    depth, current.getClass().getSimpleName(), current.getMessage());
            current = current.getCause();
            depth++;
        }
    }

    public static void demoExceptionLogging() {
        System.out.println("=== Demo: Exception Logging with Context ===");
        try {
            throw new IOException("Connection lost");
        } catch (IOException e) {
            try {
                throw new ServiceException("Service unavailable", e);
            } catch (ServiceException e2) {
                logExceptionWithChain(e2, "user-service");
            }
        }
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // Helper classes
    // ──────────────────────────────────────────────

    public static class User {
        private final long id;
        private final String name;

        public User(long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }

    // ──────────────────────────────────────────────
    // Main method — run all demos
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        demoBasicChaining();
        demoInitCause();
        demoCauseTraversal();
        demoRootCause();
        demoExceptionTranslation();
        demoFindCause();
        demoCompositeException();
        demoLayeredTranslation();
        demoExceptionLogging();

        System.out.println("=== All demos completed ===");
    }
}
