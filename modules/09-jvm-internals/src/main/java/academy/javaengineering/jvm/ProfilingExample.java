package academy.javaengineering.jvm;

/**
 * Profiling Tools - JProfiler, VisualVM, async-profiler, JFR.
 */
public class ProfilingExample {

    public static class CpuProfiler {
        public void heavyComputation() {
            long sum = 0;
            for (int i = 0; i < 1000000; i++) {
                sum += Math.sqrt(i);
            }
            System.out.println("Sum: " + sum);
        }

        public void memoryAllocation() {
            java.util.List<String> list = new java.util.ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                list.add("Item " + i);
            }
            System.out.println("List size: " + list.size());
        }
    }

    public static class MemoryProfiler {
        private byte[] data;

        public void allocateMemory(int size) {
            data = new byte[size];
            System.out.println("Allocated " + size + " bytes");
        }

        public void releaseMemory() {
            data = null;
            System.out.println("Released memory");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Profiling Tools Demo ===");

        CpuProfiler cpuProfiler = new CpuProfiler();
        cpuProfiler.heavyComputation();
        cpuProfiler.memoryAllocation();

        MemoryProfiler memoryProfiler = new MemoryProfiler();
        memoryProfiler.allocateMemory(1024 * 1024);
        memoryProfiler.releaseMemory();

        Runtime runtime = Runtime.getRuntime();
        System.out.println("\n=== JVM Memory Info ===");
        System.out.println("Max Memory: " + runtime.maxMemory() / 1024 / 1024 + " MB");
        System.out.println("Total Memory: " + runtime.totalMemory() / 1024 / 1024 + " MB");
        System.out.println("Free Memory: " + runtime.freeMemory() / 1024 / 1024 + " MB");

        System.out.println("\n=== Available Profiling Tools ===");
        System.out.println("1. JProfiler");
        System.out.println("2. VisualVM");
        System.out.println("3. async-profiler");
        System.out.println("4. Java Flight Recorder (JFR)");
        System.out.println("5. Java Mission Control (JMC)");
    }
}
