package academy.javaengineering.jvm;

/**
 * Demonstrates profiling tools and techniques for JVM performance analysis.
 *
 * <p>This class shows CPU and memory profiling patterns using tools like
 * JProfiler, VisualVM, and Java Flight Recorder.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>CPU profiling for hotspot detection</li>
 *   <li>Memory profiling for allocation tracking</li>
 *   <li>Java Flight Recorder (JFR) integration</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ProfilingExample {

    /**
     * CPU and memory profiling demonstration class.
     */
    public static class CpuProfiler {
        /**
         * Performs CPU-intensive computation for profiling.
         */
        public void heavyComputation() {
            long sum = 0;
            for (int i = 0; i < 1000000; i++) { sum += Math.sqrt(i); }
            System.out.println("Sum: " + sum);
        }

        /**
         * Performs memory allocation for profiling.
         */
        public void memoryAllocation() {
            java.util.List<String> list = new java.util.ArrayList<>();
            for (int i = 0; i < 10000; i++) { list.add("Item " + i); }
            System.out.println("List size: " + list.size());
        }
    }

    /**
     * Demonstrates profiling concepts.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Profiling Tools Demo ===");
        CpuProfiler profiler = new CpuProfiler();
        profiler.heavyComputation();
        profiler.memoryAllocation();
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");
        System.out.println("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
        System.out.println("Tools: JProfiler, VisualVM, async-profiler, Java Flight Recorder (JFR), Java Mission Control (JMC)");
    }
}
