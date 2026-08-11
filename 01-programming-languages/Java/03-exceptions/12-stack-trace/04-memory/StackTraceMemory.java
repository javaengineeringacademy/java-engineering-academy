package academy.javaengineering.exceptions.stacktrace.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates stack trace memory costs and lazy vs eager population.
 */
public class StackTraceMemory {

    /**
     * Default eager exception — trace built at construction.
     */
    static class EagerException extends RuntimeException {
        EagerException(String message) {
            super(message);
        }
    }

    /**
     * Lazy exception — trace built only when getStackTrace() is called.
     */
    static class LazyException extends RuntimeException {
        private StackTraceElement[] lazyTrace;
        private boolean traceAccessed = false;

        LazyException(String message) {
            super(message);
            // Intentionally do NOT call super.fillInStackTrace()
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this; // Skip stack walk
        }

        @Override
        public StackTraceElement[] getStackTrace() {
            if (!traceAccessed) {
                traceAccessed = true;
                // Walk the stack lazily
                Thread thread = Thread.currentThread();
                StackTraceElement[] currentTrace = thread.getStackTrace();
                // Skip frames from this class and Throwable
                lazyTrace = new StackTraceElement[currentTrace.length];
                System.arraycopy(currentTrace, 0, lazyTrace, 0, currentTrace.length);
            }
            return lazyTrace;
        }
    }

    /**
     * Lightweight exception — trace completely skipped.
     */
    static class LightweightException extends RuntimeException {
        LightweightException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    /**
     * Estimates memory used by a stack trace array.
     */
    static long estimateTraceMemory(Throwable t) {
        StackTraceElement[] trace = t.getStackTrace();
        long bytes = 0;
        // Array object header + references
        bytes += 16 + trace.length * 8L;
        for (StackTraceElement frame : trace) {
            // Each StackTraceElement object
            bytes += 16; // object header
            bytes += 4;  // lineNumber (int)
            bytes += 8;  // padding
            // Strings (approximate)
            bytes += 40 + frame.getClassName().length() * 2L;
            bytes += 40 + frame.getMethodName().length() * 2L;
            String fileName = frame.getFileName();
            if (fileName != null) {
                bytes += 40 + fileName.length() * 2L;
            }
        }
        return bytes;
    }

    /**
     * Demonstrates memory savings of different exception types.
     */
    static void compareMemoryUsage() {
        System.out.println("=== Memory Comparison ===");

        EagerException eager = new EagerException("eager");
        long eagerBytes = estimateTraceMemory(eager);
        System.out.printf("Eager exception:  ~%,d bytes, %d frames%n",
                eagerBytes, eager.getStackTrace().length);

        LightweightException lightweight = new LightweightException("lightweight");
        long lightBytes = estimateTraceMemory(lightweight);
        System.out.printf("Lightweight:      ~%,d bytes, %d frames%n",
                lightBytes, lightweight.getStackTrace().length);

        LazyException lazy = new LazyException("lazy");
        System.out.printf("Lazy (before):    ~,d bytes (not accessed)%n", 0L);
        // Force trace access
        lazy.getStackTrace();
        long lazyBytes = estimateTraceMemory(lazy);
        System.out.printf("Lazy (after):     ~%,d bytes, %d frames%n",
                lazyBytes, lazy.getStackTrace().length);
    }

    /**
     * Demonstrates memory pressure from exception accumulation.
     */
    static void demonstrateAccumulation() {
        System.out.println("\n=== Exception Accumulation ===");
        int count = 10_000;
        long before = Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();

        List<Throwable> exceptions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            exceptions.add(new RuntimeException("error-" + i));
        }

        long after = Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory();
        System.out.printf("Accumulated %,d exceptions%n", count);
        System.out.printf("Approximate memory used: ~%,d bytes%n",
                (after - before));

        exceptions.clear();
        System.out.println("Cleared list — exceptions eligible for GC");
    }

    public static void main(String[] args) {
        compareMemoryUsage();
        demonstrateAccumulation();
    }
}
