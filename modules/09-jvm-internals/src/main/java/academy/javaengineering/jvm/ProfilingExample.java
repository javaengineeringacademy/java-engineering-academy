package academy.javaengineering.jvm;

/**
 * Profiling Tools - JProfiler, VisualVM, async-profiler, JFR.
 */
public class ProfilingExample {

    public static class CpuProfiler {
        public void heavyComputation() {
            long sum = 0;
            for (int i = 0; i < 1000000; i++) { sum += Math.sqrt(i); }
            System.out.println("Sum: " + sum);
        }

        public void memoryAllocation() {
            java.util.List<String> list = new java.util.ArrayList<>();
            for (int i = 0; i < 10000; i++) { list.add("Item " + i); }
            System.out.println("List size: " + list.size());
        }
    }

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
