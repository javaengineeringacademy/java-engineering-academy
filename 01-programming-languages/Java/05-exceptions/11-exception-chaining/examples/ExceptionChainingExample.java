package academy.javaengineering.exceptions.chaining.examples;

import java.io.IOException;

/**
 * Exception chaining example demonstrating cause chain traversal,
 * exception translation, and root cause analysis.
 *
 * <p>Google Java Style: no comments, clean formatting.
 */
public class ExceptionChainingExample {

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

    public static class DomainException extends RuntimeException {
        public DomainException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ──────────────────────────────────────────────
    // 1. Basic chaining example
    // ──────────────────────────────────────────────

    public static void basicChaining() {
        System.out.println("=== Basic Chaining ===");
        try {
            throw new IOException("File not found");
        } catch (IOException e) {
            throw new InfrastructureException("Failed to read file", e);
        }
    }

    // ──────────────────────────────────────────────
    // 2. Exception translation example
    // ──────────────────────────────────────────────

    public static void exceptionTranslation() {
        System.out.println("=== Exception Translation ===");
        try {
            try {
                throw new IOException("Network timeout");
            } catch (IOException e) {
                throw new BusinessException("Service call failed", e);
            }
        } catch (BusinessException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // 3. Root cause analysis
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

    public static void rootCauseAnalysis() {
        System.out.println("=== Root Cause Analysis ===");
        try {
            try {
                try {
                    throw new IOException("Low-level error");
                } catch (IOException e) {
                    throw new InfrastructureException("Infrastructure error", e);
                }
            } catch (InfrastructureException e) {
                throw new BusinessException("Business error", e);
            }
        } catch (BusinessException e) {
            Throwable root = getRootCause(e);
            System.out.println("Root cause: " + root.getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // 4. Finding specific cause in chain
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

    public static void findingCause() {
        System.out.println("=== Finding Cause in Chain ===");
        try {
            try {
                try {
                    throw new IOException("Connection refused");
                } catch (IOException e) {
                    throw new InfrastructureException("Infrastructure failure", e);
                }
            } catch (InfrastructureException e) {
                throw new BusinessException("Business error", e);
            }
        } catch (BusinessException e) {
            IOException io = findCauseInChain(e, IOException.class);
            System.out.println("Found IOException: " + io.getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // 5. Multi-layer translation
    // ──────────────────────────────────────────────

    public static void multiLayerTranslation() {
        System.out.println("=== Multi-Layer Translation ===");
        try {
            try {
                try {
                    try {
                        throw new IOException("Disk failure");
                    } catch (IOException e) {
                        throw new InfrastructureException("Infrastructure failed", e);
                    }
                } catch (InfrastructureException e) {
                    throw new BusinessException("Business failed", e);
                }
            } catch (BusinessException e) {
                throw new DomainException("Domain error", e);
            }
        } catch (DomainException e) {
            System.out.println("Domain error: " + e.getMessage());
            System.out.println("Root cause: " + getRootCause(e).getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // Main
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            basicChaining();
        } catch (InfrastructureException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
        System.out.println();

        exceptionTranslation();
        System.out.println();

        rootCauseAnalysis();
        System.out.println();

        findingCause();
        System.out.println();

        multiLayerTranslation();
        System.out.println();
    }
}
