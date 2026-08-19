package academy.javaengineering.jvm.practices;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Profiling Exercises
 * Complete each exercise by implementing the required method.
 * Focus on JFR, async-profiler, JMH, and performance analysis.
 */
public class ProfilingExercises {

    /**
     * Exercise 1: Create a JFR recording programmatically
     * Write code that:
     * 1. Starts a JFR recording
     * 2. Enables relevant event categories
     * 3. Runs workload
     * 4. Stops recording and saves to file
     *
     * Event categories to enable:
     * - jdk.ExecutionSample (CPU profiling)
     * - jdk.GarbageCollection
     * - jdk.ObjectAllocationInNewTLAB
     */
    public static void createJfrRecording() {
        // TODO: Implement JFR recording
        // HINT: Use jdk.management.jfr.Recording (JDK 11+)
        // HINT: recording.enable(EventNames.ExecutionSample);
        // HINT: recording.start();
        // HINT: recording.dump(path);
        // HINT: recording.stop();

        System.out.println("Create a JFR recording and analyze it with JMC");
    }

    /**
     * Exercise 2: Write a microbenchmark using JMH
     * Create a JMH benchmark for String concatenation methods:
     * 1. String concatenation with +
     * 2. StringBuilder
     * 3. StringBuffer
     * 4. String.join()
     *
     * Compare performance characteristics
     */
    // TODO: Create JMH benchmark class
    // @BenchmarkMode(Mode.AverageTime)
    // @OutputTimeUnit(TimeUnit.NANOSECONDS)
    // @Warmup(iterations = 5, time = 1)
    // @Measurement(iterations = 5, time = 1)
    // @Fork(1)
    public static void jmhBenchmark() {
        System.out.println("Implement JMH benchmark for String concatenation");
        System.out.println("Compare: +, StringBuilder, StringBuffer, String.join");
    }

    /**
     * Exercise 3: Analyze thread dumps
     * Write code that:
     * 1. Creates multiple threads with different states
     * 2. Identifies thread states programmatically
     * 3. Detects potential issues (deadlock, starvation)
     *
     * Thread states to create:
     * - RUNNABLE (computing)
     * - BLOCKED (waiting for lock)
     * - WAITING (wait() call)
     * - TIMED_WAITING (sleep)
     */
    public static void analyzeThreadDumps() {
        // TODO: Create threads in different states
        final Object lock1 = new Object();
        final Object lock2 = new Object();

        // Thread 1: BLOCKED (waiting for lock1)
        Thread t1 = new Thread(() -> {
            synchronized (lock2) {
                try { Thread.sleep(100); } catch (InterruptedException e) { return; }
                synchronized (lock1) {
                    System.out.println("Thread 1 acquired lock1");
                }
            }
        }, "BLOCKED-THREAD");

        // Thread 2: WAITING
        Thread t2 = new Thread(() -> {
            synchronized (lock1) {
                try {
                    lock1.wait(); // Will wait forever
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "WAITING-THREAD");

        // Thread 3: TIMED_WAITING
        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(60000); // Sleep for 60 seconds
            } catch (InterruptedException e) {
                return;
            }
        }, "TIMED-WAITING-THREAD");

        // TODO: Start threads, analyze states, detect issues

        System.out.println("Create and analyze threads in different states");
        System.out.println("Use jstack or jcmd to see thread states");
    }

    /**
     * Exercise 4: Profile memory allocation
     * Write code that:
     * 1. Allocates objects of different sizes
     * 2. Tracks allocation patterns
     * 3. Identifies hot allocation points
     *
     * Use:
     * - Runtime.freeMemory() for rough tracking
     * - ManagementFactory.getMemoryMXBean() for details
     * - JFR ObjectAllocationInNewTLAB events
     */
    public static void profileMemoryAllocation() {
        // TODO: Implement memory allocation profiling
        Runtime rt = Runtime.getRuntime();
        long before = rt.freeMemory();

        // Allocate various object sizes
        List<byte[]> smallObjects = new ArrayList<>();
        List<byte[]> mediumObjects = new ArrayList<>();
        List<byte[]> largeObjects = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            smallObjects.add(new byte[64]);       // 64 bytes
        }
        for (int i = 0; i < 100; i++) {
            mediumObjects.add(new byte[1024]);    // 1KB
        }
        for (int i = 0; i < 10; i++) {
            largeObjects.add(new byte[1024 * 1024]); // 1MB
        }

        long after = rt.freeMemory();
        System.out.println("Memory used: " + ((before - after) / 1024) + " KB");
        System.out.println("Small objects: " + smallObjects.size());
        System.out.println("Medium objects: " + mediumObjects.size());
        System.out.println("Large objects: " + largeObjects.size());
    }

    /**
     * Exercise 5: Generate and analyze flame graph data
     * Write code that:
     * 1. Creates a complex call stack
     * 2. Generates stack trace data in collapsed format
     * 3. Analyzes which methods are hottest
     *
     * Output format for flame graphs:
     *   root;method1;method2;method3 count
     */
    public static void generateFlameGraphData() {
        // TODO: Implement flame graph data generation
        // Create a method that generates interesting stack traces
        // Collect and format stack traces

        Map<String, Integer> stackCounts = new HashMap<>();

        // Generate some stack traces
        for (int i = 0; i < 1000; i++) {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StringBuilder sb = new StringBuilder();
            for (int j = 2; j < Math.min(stack.length, 8); j++) {
                if (sb.length() > 0) sb.append(";");
                sb.append(stack[j].getClassName()).append(".").append(stack[j].getMethodName());
            }
            stackCounts.merge(sb.toString(), 1, Integer::sum);
        }

        // Print in collapsed format
        System.out.println("Flame graph data (collapsed format):");
        stackCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.println(e.getValue() + " " + e.getKey()));

        System.out.println("\nPaste into https://www.speedscope.app/ or use flamegraph.pl");
    }

    public static void main(String[] args) {
        System.out.println("=== Profiling Exercises ===\n");

        // Test Exercise 1
        System.out.println("Exercise 1: JFR Recording");
        createJfrRecording();

        // Test Exercise 2
        System.out.println("\nExercise 2: JMH Benchmark");
        jmhBenchmark();

        // Test Exercise 3
        System.out.println("\nExercise 3: Thread Dump Analysis");
        analyzeThreadDumps();

        // Test Exercise 4
        System.out.println("\nExercise 4: Memory Allocation Profiling");
        profileMemoryAllocation();

        // Test Exercise 5
        System.out.println("\nExercise 5: Flame Graph Data");
        generateFlameGraphData();
    }
}
