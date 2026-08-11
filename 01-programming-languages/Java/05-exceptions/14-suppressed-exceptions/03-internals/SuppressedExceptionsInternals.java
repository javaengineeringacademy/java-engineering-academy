package academy.javaengineering.exceptions.suppressed.internals;

/**
 * Demonstrates how the JVM handles suppressed exceptions internally,
 * including bytecode generation for TWR and the Throwable suppressed list.
 */
public class SuppressedExceptionsInternals {

    public static void main(String[] args) {
        demoTwrBytecodeSimulation();
        System.out.println("---");
        demoMultipleResourcesBytecodeSimulation();
        System.out.println("---");
        demoSelfSuppressionGuard();
        System.out.println("---");
        demoSuppressedArrayStorage();
    }

    /**
     * Simulates how TWR handles close() exceptions at the bytecode level.
     * The compiler generates synthetic code similar to this.
     */
    static void demoTwrBytecodeSimulation() {
        System.out.println("== TWR Bytecode Simulation ==");
        try {
            // Simulate the synthetic try-finally pattern
            Throwable primary = null;
            try {
                throw new RuntimeException("primary error");
            } catch (RuntimeException e) {
                primary = e;
            }

            // Simulate close() call
            Throwable closeException = null;
            try {
                throw new RuntimeException("close() failed");
            } catch (RuntimeException e) {
                closeException = e;
            }

            if (primary != null) {
                if (closeException != null) {
                    primary.addSuppressed(closeException);
                }
                throw primary;
            } else {
                throw closeException;
            }
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        }
    }

    /**
     * Simulates multiple resources being closed in reverse order.
     */
    static void demoMultipleResourcesBytecodeSimulation() {
        System.out.println("== Multiple Resources Bytecode Simulation ==");
        try {
            Throwable primary = null;

            // Resource A close (processed last in reverse order)
            Throwable closeA = null;
            try {
                throw new RuntimeException("Resource A close() failed");
            } catch (RuntimeException e) {
                closeA = e;
            }

            // Resource B close (processed first in reverse order)
            Throwable closeB = null;
            try {
                throw new RuntimeException("Resource B close() failed");
            } catch (RuntimeException e) {
                closeB = e;
            }

            // Simulate try block exception
            primary = new RuntimeException("try block failed");

            // Add suppressed in reverse order
            if (closeA != null) {
                primary.addSuppressed(closeA);
            }
            if (closeB != null) {
                primary.addSuppressed(closeB);
            }

            throw primary;
        } catch (RuntimeException e) {
            System.out.println("Primary: " + e.getMessage());
            System.out.println("Suppressed count: " + e.getSuppressed().length);
            for (Throwable s : e.getSuppressed()) {
                System.out.println("  Suppressed: " + s.getMessage());
            }
        }
    }

    /**
     * Demonstrates that self-suppression is prevented by IllegalArgumentException.
     */
    static void demoSelfSuppressionGuard() {
        System.out.println("== Self-Suppression Guard ==");
        RuntimeException self = new RuntimeException("self");
        try {
            self.addSuppressed(self);
            System.out.println("ERROR: Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("Self-suppression prevented: " + e.getMessage());
        }
    }

    /**
     * Demonstrates how suppressed exceptions are stored as an array on Throwable.
     */
    static void demoSuppressedArrayStorage() {
        System.out.println("== Suppressed Array Storage ==");
        RuntimeException primary = new RuntimeException("primary");

        // Initially no suppressed exceptions
        System.out.println("Initial suppressed count: " + primary.getSuppressed().length);

        // Add three suppressed exceptions
        primary.addSuppressed(new RuntimeException("s1"));
        primary.addSuppressed(new RuntimeException("s2"));
        primary.addSuppressed(new RuntimeException("s3"));

        System.out.println("After adding 3 suppressed: " + primary.getSuppressed().length);

        // Verify each is accessible
        for (Throwable s : primary.getSuppressed()) {
            System.out.println("  Suppressed: " + s.getMessage());
        }
    }
}
