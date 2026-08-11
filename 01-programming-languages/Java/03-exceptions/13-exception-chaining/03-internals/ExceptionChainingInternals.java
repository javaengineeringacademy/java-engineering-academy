package academy.javaengineering.exceptions.internals;

/**
 * Demonstrates the internal mechanics of exception chaining in Java.
 *
 * <p>This class covers:
 * <ul>
 *   <li>initCause() mechanism and its constraints</li>
 *   <li>Cause chain traversal and structure</li>
 *   <li>Memory layout implications of chained exceptions</li>
 *   <li>fillInStackTrace() behavior with chained exceptions</li>
 *   <li>Performance cost of deep exception chains</li>
 * </ul>
 *
 * <p>Run this class to observe how the JVM manages exception internals.
 * No comments in implementation -- refer to the README for explanations.
 */
public class ExceptionChainingInternals {

    // ──────────────────────────────────────────────
    // 1. initCause() mechanism
    // ──────────────────────────────────────────────

    public static class LegacyException extends Exception {
        public LegacyException(String message) {
            super(message);
        }
    }

    public static void demoInitCauseMechanism() {
        System.out.println("=== Demo: initCause() Mechanism ===");

        LegacyException ex = new LegacyException("Something went wrong");
        System.out.println("Before initCause(): cause = " + ex.getCause());

        ex.initCause(new RuntimeException("Root problem"));
        System.out.println("After initCause():  cause = " + ex.getCause());
        System.out.println("Cause message:      " + ex.getCause().getMessage());

        try {
            ex.initCause(new RuntimeException("Second cause"));
        } catch (IllegalStateException e) {
            System.out.println("Second call threw:  " + e.getMessage());
        }

        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 2. Cause chain traversal
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

    public static void demoCauseChainTraversal() {
        System.out.println("=== Demo: Cause Chain Traversal ===");

        RuntimeException root = new RuntimeException("Disk failure");
        InfrastructureException infra = new InfrastructureException("Infrastructure down", root);
        BusinessException biz = new BusinessException("Business logic failed", infra);
        PresentationException pres = new PresentationException("Request failed", biz);

        System.out.println("Chain structure:");
        Throwable current = pres;
        int depth = 0;
        while (current != null) {
            String marker = (current.getCause() == null) ? " [END]" : "";
            System.out.printf("  Level %d: [%s] %s%s%n",
                    depth, current.getClass().getSimpleName(), current.getMessage(), marker);
            current = current.getCause();
            depth++;
        }

        System.out.println("\nTotal chain depth: " + depth);
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 3. Memory layout observation
    // ──────────────────────────────────────────────

    public static void demoMemoryLayout() {
        System.out.println("=== Demo: Memory Layout of Chained Exceptions ===");

        RuntimeException single = new RuntimeException("Single exception");
        System.out.println("Single exception identity hash: " + System.identityHashCode(single));
        System.out.println("Single exception cause:         " + single.getCause());

        RuntimeException outer = new RuntimeException("Outer");
        RuntimeException middle = new RuntimeException("Middle", outer);
        RuntimeException inner = new RuntimeException("Inner", middle);

        System.out.println("\nChained exceptions are distinct objects:");
        System.out.println("  Inner  hash: " + System.identityHashCode(inner));
        System.out.println("  Middle hash: " + System.identityHashCode(middle));
        System.out.println("  Outer  hash: " + System.identityHashCode(outer));

        System.out.println("\nCause references:");
        System.out.println("  inner.getCause()  == middle? " + (inner.getCause() == middle));
        System.out.println("  middle.getCause() == outer?  " + (middle.getCause() == outer));
        System.out.println("  outer.getCause()  == null?   " + (outer.getCause() == null));

        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 4. fillInStackTrace() behavior
    // ──────────────────────────────────────────────

    public static class FastException extends RuntimeException {
        public FastException(String message, Throwable cause) {
            super(message, cause, true, false);
        }
    }

    public static void demoFillInStackTraceBehavior() {
        System.out.println("=== Demo: fillInStackTrace() Behavior ===");

        RuntimeException normal = new RuntimeException("Normal exception");
        System.out.println("Normal exception stack trace depth: "
                + normal.getStackTrace().length);

        FastException fast = new FastException("Fast exception", new RuntimeException("cause"));
        System.out.println("Fast exception stack trace depth:   "
                + fast.getStackTrace().length);

        System.out.println("\nNormal exception full stack trace:");
        for (StackTraceElement ste : normal.getStackTrace()) {
            System.out.println("    at " + ste);
        }

        System.out.println("\nFast exception full stack trace:");
        for (StackTraceElement ste : fast.getStackTrace()) {
            System.out.println("    at " + ste);
        }

        System.out.println("\nNote: fast exception has empty stack trace due to writableStackTrace=false");
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 5. Deep chain performance
    // ──────────────────────────────────────────────

    public static RuntimeException buildChain(int depth) {
        RuntimeException cause = new RuntimeException("Root cause (level 0)");
        for (int i = 1; i < depth; i++) {
            cause = new RuntimeException("Level " + i, cause);
        }
        return cause;
    }

    public static int chainDepth(Throwable t) {
        int depth = 0;
        while (t != null) {
            depth++;
            t = t.getCause();
        }
        return depth;
    }

    public static void demoDeepChainPerformance() {
        System.out.println("=== Demo: Deep Chain Performance ===");

        int[] depths = {1, 5, 10, 50, 100};

        for (int depth : depths) {
            long startCreate = System.nanoTime();
            RuntimeException chain = buildChain(depth);
            long createTime = System.nanoTime() - startCreate;

            long startTraverse = System.nanoTime();
            int actualDepth = chainDepth(chain);
            long traverseTime = System.nanoTime() - startTraverse;

            System.out.printf("  Depth %3d: create=%6d ns, traverse=%6d ns, verified depth=%d%n",
                    depth, createTime, traverseTime, actualDepth);
        }

        System.out.println("\nObservation: creation time scales with depth (each fillInStackTrace call).");
        System.out.println("Observation: traversal is fast (simple pointer chasing).");
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 6. initCause() with constructor cause
    // ──────────────────────────────────────────────

    public static class DualInitException extends RuntimeException {
        public DualInitException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static void demoConstructorVsInitCause() {
        System.out.println("=== Demo: Constructor vs initCause() ===");

        RuntimeException original = new RuntimeException("Original error");

        DualInitException fromConstructor = new DualInitException("Via constructor", original);
        System.out.println("Constructor-based cause: " + fromConstructor.getCause().getMessage());

        DualInitException fromInitCause = new DualInitException("Via initCause", null);
        fromInitCause.initCause(original);
        System.out.println("initCause-based cause:   " + fromInitCause.getCause().getMessage());

        try {
            fromConstructor.initCause(new RuntimeException("Extra"));
        } catch (IllegalStateException e) {
            System.out.println("Calling initCause() on constructor-chained exception: " + e.getMessage());
        }

        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 7. Stack trace overlap in chains
    // ──────────────────────────────────────────────

    public static void methodC() {
        throw new InfrastructureException("Infrastructure failure",
                new RuntimeException("Root cause"));
    }

    public static void methodB() {
        try {
            methodC();
        } catch (InfrastructureException e) {
            throw new BusinessException("Business failed", e);
        }
    }

    public static void methodA() {
        try {
            methodB();
        } catch (BusinessException e) {
            throw new PresentationException("Presentation failed", e);
        }
    }

    public static void demoStackOverlap() {
        System.out.println("=== Demo: Stack Trace Overlap in Chains ===");

        try {
            methodA();
        } catch (PresentationException e) {
            System.out.println("Full stack trace with overlap annotation:");
            e.printStackTrace(System.out);
        }

        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 8. Thread safety of Throwable
    // ──────────────────────────────────────────────

    public static void demoThreadSafety() throws InterruptedException {
        System.out.println("=== Demo: Thread Safety of Throwable ===");

        RuntimeException shared = new RuntimeException("Shared across threads");
        shared.initCause(new RuntimeException("Cause set before sharing"));

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Throwable cause = shared.getCause();
                if (cause == null) {
                    System.out.println("ERROR: cause became null at iteration " + i);
                    return;
                }
            }
            System.out.println("Thread: read cause successfully 1000 times");
        });

        reader.start();
        reader.join();

        System.out.println("Main: cause is stable after construction");
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 9. Suppressed exceptions vs cause chain
    // ──────────────────────────────────────────────

    public static void demoSuppressedVsCause() {
        System.out.println("=== Demo: Suppressed Exceptions vs Cause Chain ===");

        RuntimeException cause = new RuntimeException("Original error");
        RuntimeException primary = new RuntimeException("Primary exception", cause);

        try {
            throw primary;
        } catch (RuntimeException e) {
            e.addSuppressed(new RuntimeException("Resource cleanup failed"));
            e.addSuppressed(new RuntimeException("Another cleanup failure"));

            System.out.println("Primary: " + e.getMessage());
            System.out.println("Cause:   " + e.getCause().getMessage());
            System.out.println("Suppressed count: " + e.getSuppressed().length);
            for (Throwable sup : e.getSuppressed()) {
                System.out.println("  Suppressed: " + sup.getMessage());
            }

            System.out.println("\nNote: suppressed exceptions are separate from the cause chain.");
            System.out.println("The cause chain is linear; suppressed exceptions form a list.");
        }

        System.out.println();
    }

    // ──────────────────────────────────────────────
    // 10. Identity and equality of chained exceptions
    // ──────────────────────────────────────────────

    public static void demoExceptionIdentity() {
        System.out.println("=== Demo: Exception Identity and Equality ===");

        RuntimeException ex1 = new RuntimeException("Same message");
        RuntimeException ex2 = new RuntimeException("Same message");
        RuntimeException ex3 = ex1;

        System.out.println("ex1 == ex2 (same message):     " + (ex1 == ex2));
        System.out.println("ex1.equals(ex2) (same message): " + ex1.equals(ex2));
        System.out.println("ex1 == ex3 (alias):             " + (ex1 == ex3));

        System.out.println("\nException identity is reference-based.");
        System.out.println("equals() uses reference equality (Object.equals), not message comparison.");
        System.out.println();
    }

    // ──────────────────────────────────────────────
    // Main method
    // ──────────────────────────────────────────────

    public static void main(String[] args) throws InterruptedException {
        demoInitCauseMechanism();
        demoCauseChainTraversal();
        demoMemoryLayout();
        demoFillInStackTraceBehavior();
        demoDeepChainPerformance();
        demoConstructorVsInitCause();
        demoStackOverlap();
        demoThreadSafety();
        demoSuppressedVsCause();
        demoExceptionIdentity();

        System.out.println("=== All internals demos completed ===");
    }
}
