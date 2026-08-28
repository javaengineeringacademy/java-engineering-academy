package jvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * AsyncProfilerExample - async-profiler usage and integration
 *
 * Demonstrates:
 * - async-profiler command-line usage
 * - CPU, allocation, and lock profiling
 * - Flame graph generation
 * - Wall-clock profiling
 * - Integration with Java applications
 * - Profiling output analysis
 *
 * Prerequisites:
 * - Download async-profiler from https://github.com/async-profiler/async-profiler
 * - Extract and set PROFILER_HOME environment variable
 *
 * Run: java -agentpath:$PROFILER_HOME/lib/libasyncProfiler.so=start,file=profile.jfr AsyncProfilerExample
 */
public class AsyncProfilerExample {

    public static void main(String[] args) throws Exception {
        System.out.println("=== async-profiler Example ===\n");

        long pid = ProcessHandle.current().pid();
        System.out.println("Current JVM PID: " + pid);
        System.out.println();

        printCommandReference(pid);

        System.out.println("=== Profiling Simulation ===\n");
        runProfilingWorkload();

        System.out.println("\n=== Output Formats ===");
        System.out.println("async-profiler supports multiple output formats:");
        System.out.println("  .html  - Interactive flame graph (default)");
        System.out.println("  .jfr   - Java Flight Recorder format");
        System.out.println("  .collapsed - Collapsed stack format (for FlameGraph tool)");
        System.out.println("  .traces - Call traces in text format");
        System.out.println("  .flat   - Flat profile in text format");
        System.out.println();

        printAnalysisTips();
    }

    static void printCommandReference(long pid) {
        System.out.println("=== async-profiler Commands ===\n");

        System.out.println("CPU Profiling:");
        System.out.println("  ./profiler.sh -d 30 -f cpu_profile.html " + pid);
        System.out.println("  ./profiler.sh -d 30 -o collapsed -f cpu.txt " + pid);
        System.out.println();

        System.out.println("Allocation Profiling (heap allocations):");
        System.out.println("  ./profiler.sh -d 30 -e alloc -f alloc_profile.html " + pid);
        System.out.println();

        System.out.println("Lock Profiling (monitor contention):");
        System.out.println("  ./profiler.sh -d 30 -e lock -f lock_profile.html " + pid);
        System.out.println();

        System.out.println("Wall-Clock Profiling (includes blocked time):");
        System.out.println("  ./profiler.sh -d 30 -e wall -f wall_profile.html " + pid);
        System.out.println();

        System.out.println("CPU Profiling with JFR output:");
        System.out.println("  ./profiler.sh -d 30 -e cpu -f profile.jfr " + pid);
        System.out.println();

        System.out.println("Start/Stop Profiling:");
        System.out.println("  ./profiler.sh start " + pid);
        System.out.println("  // ... run code to profile ...");
        System.out.println("  ./profiler.sh stop -f profile.html " + pid);
        System.out.println();

        System.out.println("Attach to running process:");
        System.out.println("  ./profiler.sh -d 10 -f profile.html -t " + pid);
        System.out.println("  // -t flag includes thread dumps in flame graph");
        System.out.println();

        System.out.println("Filter by thread:");
        System.out.println("  ./profiler.sh -d 10 -f profile.html --filter thread=main " + pid);
        System.out.println();

        System.out.println("Lower overhead (reduced sampling rate):");
        System.out.println("  ./profiler.sh -d 30 -i 10ms -f profile.html " + pid);
        System.out.println("  // -i 10ms sets sampling interval to 10ms (default: 1ms)");
        System.out.println();
    }

    static void runProfilingWorkload() {
        System.out.println("Running workload for profiling demonstration...\n");

        // CPU-intensive workload
        long start = System.nanoTime();
        double result = 0;
        for (int i = 0; i < 5_000_000; i++) {
            result += Math.sin(i * 0.001) * Math.cos(i * 0.001);
        }
        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.printf("CPU work (math): %d ms, result=%.2f%n", elapsed, result);

        // Allocation-heavy workload
        start = System.nanoTime();
        java.util.List<String> strings = new java.util.ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            strings.add("item-" + i + "-suffix");
        }
        elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.printf("Allocation work (string creation): %d ms, %d strings%n",
                elapsed, strings.size());

        // Synchronized work (for lock profiling)
        start = System.nanoTime();
        Object lock = new Object();
        java.util.concurrent.atomic.AtomicLong counter =
                new java.util.concurrent.atomic.AtomicLong(0);
        Thread[] threads = new Thread[4];
        for (int t = 0; t < threads.length; t++) {
            threads[t] = new Thread(() -> {
                for (int i = 0; i < 100_000; i++) {
                    synchronized (lock) {
                        counter.incrementAndGet();
                    }
                }
            });
            threads[t].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.printf("Lock contention work: %d ms, counter=%d%n",
                elapsed, counter.get());

        // IO-like work (simulated)
        start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50_000; i++) {
            sb.append("Line ").append(i).append(": data payload\n");
        }
        String output = sb.toString();
        elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.printf("String building work: %d ms, output length=%d%n",
                elapsed, output.length());

        System.out.println("\nWorkload complete. Run async-profiler against this JVM to capture profiles.");
    }

    static void printAnalysisTips() {
        System.out.println("=== Flame Graph Analysis Tips ===\n");
        System.out.println("Reading flame graphs:");
        System.out.println("  - X-axis: proportion of samples (wider = more time)");
        System.out.println("  - Y-axis: stack depth (deeper = more nested calls)");
        System.out.println("  - Color: arbitrary (usually warm colors = more samples)");
        System.out.println("  - Click to zoom into a sub-tree");
        System.out.println();
        System.out.println("What to look for:");
        System.out.println("  - Wide frames at the top = CPU hot methods");
        System.out.println("  - Deep stacks = potential optimization targets");
        System.out.println("  - GC frames dominating = memory pressure");
        System.out.println("  - Lock contention frames = synchronization bottlenecks");
        System.out.println();
        System.out.println("async-profiler vs other profilers:");
        System.out.println("  + No safepoint bias (uses perf_events / unwind info)");
        System.out.println("  + Very low overhead (<1% in most cases)");
        System.out.println("  + Supports CPU, allocation, lock profiling");
        System.out.println("  + Generates flame graphs directly");
        System.out.println("  - Requires native library (platform-specific)");
        System.out.println("  - Limited to Linux (perf_events) and macOS (unwind info)");
    }
}
