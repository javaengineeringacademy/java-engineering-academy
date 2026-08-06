package jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * ProfilingDemo - JMH benchmark example and profiling tools
 *
 * Covers:
 * - JMH (Java Microbenchmark Harness) basics
 * - JVM profiling tools
 * - Performance measurement techniques
 * - Common profiling scenarios
 */
public class ProfilingDemo {

    public static void main(String[] args) {
        System.out.println("=== JMH Overview ===");
        jmhOverview();

        System.out.println("\n=== JVM Profiling Tools ===");
        profilingTools();

        System.out.println("\n=== Using Management Beans ===");
        managementBeans();

        System.out.println("\n=== Performance Measurement ===");
        performanceMeasurement();
    }

    static void jmhOverview() {
        System.out.println("JMH (Java Microbenchmark Harness):");
        System.out.println();
        System.out.println("Setup:");
        System.out.println("  @BenchmarkMode(Mode.AverageTime)");
        System.out.println("  @OutputTimeUnit(TimeUnit.NANOSECONDS)");
        System.out.println("  @State(Scope.Thread)");
        System.out.println("  public class MyBenchmark {");
        System.out.println("      @Benchmark");
        System.out.println("      public void testMethod() {");
        System.out.println("          // Code to benchmark");
        System.out.println("      }");
        System.out.println("  }");
        System.out.println();
        System.out.println("Key annotations:");
        System.out.println("  @Benchmark      - Marks a benchmark method");
        System.out.println("  @Setup          - Setup code before benchmark");
        System.out.println("  @TearDown       - Cleanup after benchmark");
        System.out.println("  @Param          - Parameterized benchmarks");
        System.out.println();
        System.out.println("Run with:");
        System.out.println("  mvn clean install");
        System.out.println("  java -jar target/benchmarks.jar");
    }

    static void profilingTools() {
        System.out.println("Profiling Tools:");
        System.out.println();
        System.out.println("1. VisualVM");
        System.out.println("   - CPU and memory profiling");
        System.out.println("   - Thread monitoring");
        System.out.println("   - Heap dumps");
        System.out.println();
        System.out.println("2. JProfiler");
        System.out.println("   - Commercial tool");
        System.out.println("   - Advanced profiling features");
        System.out.println();
        System.out.println("3. YourKit");
        System.out.println("   - Commercial tool");
        System.out.println("   - Low overhead profiling");
        System.out.println();
        System.out.println("4. async-profiler");
        System.out.println("   - Open source");
        System.out.println("   - Low overhead");
        System.out.println("   - CPU and allocation profiling");
        System.out.println();
        System.out.println("5. JFR (Java Flight Recorder)");
        System.out.println("   - Built into JDK");
        System.out.println("   - Low overhead");
        System.out.println("   - Production-ready");
        System.out.println();
        System.out.println("6. jcmd and jstat");
        System.out.println("   - Command-line tools");
        System.out.println("   - Built into JDK");
    }

    static void managementBeans() {
        // MemoryMXBean
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();

        System.out.println("=== Memory Usage ===");
        System.out.println("Heap Memory:");
        System.out.println("  Init: " + (heapUsage.getInit() / 1024 / 1024) + " MB");
        System.out.println("  Used: " + (heapUsage.getUsed() / 1024 / 1024) + " MB");
        System.out.println("  Committed: " + (heapUsage.getCommitted() / 1024 / 1024) + " MB");
        System.out.println("  Max: " + (heapUsage.getMax() / 1024 / 1024) + " MB");

        System.out.println("\nNon-Heap Memory:");
        System.out.println("  Init: " + (nonHeapUsage.getInit() / 1024 / 1024) + " MB");
        System.out.println("  Used: " + (nonHeapUsage.getUsed() / 1024 / 1024) + " MB");
        System.out.println("  Committed: " + (nonHeapUsage.getCommitted() / 1024 / 1024) + " MB");

        // ThreadMXBean
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        System.out.println("\n=== Thread Info ===");
        System.out.println("Thread count: " + threadBean.getThreadCount());
        System.out.println("Peak thread count: " + threadBean.getPeakThreadCount());
        System.out.println("Daemon thread count: " + threadBean.getDaemonThreadCount());
    }

    static void performanceMeasurement() {
        System.out.println("Performance Measurement Techniques:");
        System.out.println();

        // Simple timing
        long startTime = System.nanoTime();
        // Simulate work
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
        long endTime = System.nanoTime();
        System.out.println("Simple timing: " + (endTime - startTime) + " ns");

        // Multiple iterations for accuracy
        int iterations = 100;
        long totalTime = 0;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            for (int j = 0; j < 100000; j++) {
                Math.sqrt(j);
            }
            totalTime += System.nanoTime() - start;
        }
        System.out.println("Average over " + iterations + " iterations: " +
            (totalTime / iterations / 1000) + " us");

        // Memory measurement
        Runtime runtime = Runtime.getRuntime();
        List<Object> objects = new ArrayList<>();

        long before = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < 1000; i++) {
            objects.add(new Object());
        }
        long after = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Memory allocated for 1000 objects: " +
            (after - before) + " bytes");

        objects.clear();
        System.gc();
    }
}