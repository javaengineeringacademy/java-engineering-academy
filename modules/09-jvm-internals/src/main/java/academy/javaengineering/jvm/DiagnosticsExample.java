package academy.javaengineering.jvm;

/**
 * Demonstrates JVM diagnostics tools and techniques.
 *
 * <p>This class provides examples of thread dumps, heap dumps, and JVM monitoring
 * for diagnosing application issues.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Thread dump analysis</li>
 *   <li>Heap dump inspection</li>
 *   <li>JVM diagnostic commands</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class DiagnosticsExample {

    /**
     * Demonstrates thread dump information retrieval.
     */
    public static class ThreadDumpExample {
        /**
         * Prints information about the current thread.
         */
        public void demonstrateThreads() {
            Thread mainThread = Thread.currentThread();
            System.out.println("Main Thread: " + mainThread.getName());
            System.out.println("Thread ID: " + mainThread.getId());
            System.out.println("Thread State: " + mainThread.getState());
            System.out.println("Priority: " + mainThread.getPriority());
        }
    }

    /**
     * Demonstrates heap dump information retrieval.
     */
    public static class HeapDumpExample {
        private java.util.Map<String, Object> data = new java.util.HashMap<>();

        /**
         * Adds data to the heap for demonstration.
         *
         * @param key the data key
         * @param value the data value
         */
        public void addData(String key, Object value) { data.put(key, value); }

        /**
         * Prints current heap memory information.
         */
        public void printHeapInfo() {
            Runtime runtime = Runtime.getRuntime();
            System.out.println("Heap Size: " + runtime.totalMemory() / 1024 + " KB");
            System.out.println("Used Heap: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 + " KB");
        }
    }

    /**
     * Demonstrates JVM diagnostics concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== JVM Diagnostics Demo ===");
        new ThreadDumpExample().demonstrateThreads();
        HeapDumpExample heapDump = new HeapDumpExample();
        heapDump.addData("key1", "value1");
        heapDump.addData("key2", 42);
        heapDump.printHeapInfo();
        System.out.println("Commands: jstack <pid>, jmap -heap <pid>, jmap -dump <pid>, jcmd <pid> JFR.start");
    }
}
