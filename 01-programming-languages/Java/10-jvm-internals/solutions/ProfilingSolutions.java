package academy.javaengineering.jvm.solutions;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Profiling Solutions - Complete implementations
 */
public class ProfilingSolutions {

    /**
     * Exercise 1 Solution: JFR recording
     */
    public static void createJfrRecording() {
        System.out.println("=== JFR Recording Demo ===");
        System.out.println("Note: Requires JDK 11+ and jdk.management.jfr module\n");

        try {
            // Check if JFR is available
            Class<?> recordingClass = Class.forName("jdk.management.jfr.Recording");
            System.out.println("JFR is available");

            // Programmatic recording approach
            System.out.println("\nTo create JFR recording:");
            System.out.println("1. Command line:");
            System.out.println("   jcmd <pid> JFR.start settings=profile duration=60s filename=rec.jfr");
            System.out.println();
            System.out.println("2. Programmatic (JDK 11+):");
            System.out.println("   Recording rec = new Recording();");
            System.out.println("   rec.enable(EventNames.ExecutionSample);");
            System.out.println("   rec.enable(EventNames.GarbageCollection);");
            System.out.println("   rec.enable(EventNames.ObjectAllocationInNewTLAB);");
            System.out.println("   rec.start();");
            System.out.println("   // ... workload ...");
            System.out.println("   rec.dump(Paths.get(\"recording.jfr\"));");
            System.out.println("   rec.stop();");
            System.out.println();
            System.out.println("3. Analyze with JDK Mission Control (JMC)");

        } catch (ClassNotFoundException e) {
            System.out.println("JFR module not available on this JDK");
            System.out.println("Use JDK 11+ for programmatic JFR");
        }
    }

    /**
     * Exercise 2 Solution: JMH benchmark
     */
    // @BenchmarkMode(Mode.AverageTime)
    // @OutputTimeUnit(TimeUnit.NANOSECONDS)
    // @Warmup(iterations = 5, time = 1)
    // @Measurement(iterations = 5, time = 1)
    // @Fork(1)
    public static void jmhBenchmark() {
        System.out.println("=== JMH Benchmark Demo ===");
        System.out.println("Note: Full JMH requires separate project setup\n");

        // Simulate benchmark
        String[] results = new String[4];

        // Test 1: String concatenation
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            String s = "";
            for (int j = 0; j < 10; j++) {
                s += "test";
            }
        }
        long concatTime = System.nanoTime() - start;
        results[0] = "String +: " + (concatTime / 100000) + " ns/op";

        // Test 2: StringBuilder
        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                sb.append("test");
            }
            String s = sb.toString();
        }
        long builderTime = System.nanoTime() - start;
        results[1] = "StringBuilder: " + (builderTime / 100000) + " ns/op";

        // Test 3: StringBuffer
        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < 10; j++) {
                sb.append("test");
            }
            String s = sb.toString();
        }
        long bufferTime = System.nanoTime() - start;
        results[2] = "StringBuffer: " + (bufferTime / 100000) + " ns/op";

        // Test 4: String.join
        start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            String[] parts = new String[10];
            Arrays.fill(parts, "test");
            String s = String.join("", parts);
        }
        long joinTime = System.nanoTime() - start;
        results[3] = "String.join: " + (joinTime / 100000) + " ns/op";

        System.out.println("String concatenation benchmark:");
        for (String result : results) {
            System.out.println("  " + result);
        }
        System.out.println("\nStringBuilder is fastest for repeated concatenation");
    }

    /**
     * Exercise 3 Solution: Thread dump analysis
     */
    public static void analyzeThreadDump() {
        System.out.println("=== Thread Dump Analysis ===\n");

        // Create threads in different states
        final Object lock = new Object();

        // Thread 1: RUNNABLE (computing)
        Thread t1 = new Thread(() -> {
            long sum = 0;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sum += i;
            }
        }, "COMPUTING-THREAD");

        // Thread 2: WAITING
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "WAITING-THREAD");

        // Thread 3: TIMED_WAITING
        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                return;
            }
        }, "SLEEPING-THREAD");

        // Start all threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to reach their states
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Analyze thread states
        Map<Thread, StackTraceElement[]> dumps = Thread.getAllStackTraces();
        Map<Thread.State, Integer> stateCounts = new HashMap<>();

        for (Thread.State state : Thread.State.values()) {
            stateCounts.put(state, 0);
        }

        System.out.println("Thread Analysis:");
        for (Map.Entry<Thread, StackTraceElement[]> entry : dumps.entrySet()) {
            Thread thread = entry.getKey();
            Thread.State state = thread.getState();
            stateCounts.merge(state, 1, Integer::sum);

            if (thread.getName().contains("THREAD")) {
                System.out.printf("  %s: %s (priority=%d, daemon=%b)%n",
                        thread.getName(), state, thread.getPriority(), thread.isDaemon());
            }
        }

        System.out.println("\nState Summary:");
        stateCounts.forEach((state, count) -> {
            if (count > 0) {
                System.out.println("  " + state + ": " + count);
            }
        });

        // Detect deadlocks
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = tmx.findDeadlockedThreads();
        if (deadlockedThreads != null) {
            System.out.println("\nDEADLOCK DETECTED! Threads: " + deadlockedThreads.length);
        } else {
            System.out.println("\nNo deadlocks detected");
        }

        // Cleanup
        t1.interrupt();
        try {
            synchronized (lock) {
                lock.notifyAll();
            }
        } catch (Exception e) {}
        t3.interrupt();
    }

    /**
     * Exercise 4 Solution: Memory allocation profiling
     */
    public static void profileMemoryAllocation() {
        System.out.println("=== Memory Allocation Profiling ===\n");

        Runtime rt = Runtime.getRuntime();
        long before = rt.freeMemory();
        long beforeTotal = rt.totalMemory();

        // Allocate objects of different sizes
        List<byte[]> smallObjects = new ArrayList<>();
        List<byte[]> mediumObjects = new ArrayList<>();
        List<byte[]> largeObjects = new ArrayList<>();

        System.out.println("Allocating objects...");

        // Small allocations
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            smallObjects.add(new byte[64]); // 64 bytes each = 64KB total
        }
        long smallTime = System.nanoTime() - start;

        // Medium allocations
        start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            mediumObjects.add(new byte[1024]); // 1KB each = 100KB total
        }
        long mediumTime = System.nanoTime() - start;

        // Large allocations
        start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            largeObjects.add(new byte[1024 * 1024]); // 1MB each = 10MB total
        }
        long largeTime = System.nanoTime() - start;

        long after = rt.freeMemory();

        System.out.println("Allocation Results:");
        System.out.printf("  Small (1000 x 64B):  %d ms, %d KB%n",
                smallTime / 1000000, 64 * 1000 / 1024);
        System.out.printf("  Medium (100 x 1KB):  %d ms, %d KB%n",
                mediumTime / 1000000, 100 * 1024 / 1024);
        System.out.printf("  Large (10 x 1MB):    %d ms, %d KB%n",
                largeTime / 1000000, 10 * 1024 * 1024 / 1024);

        System.out.println("\nTotal memory used: " + ((before - after) / 1024) + " KB");
        System.out.println("Note: Large allocations are slower due to TLAB threshold");

        // Cleanup
        smallObjects.clear();
        mediumObjects.clear();
        largeObjects.clear();
        System.gc();
    }

    /**
     * Exercise 5 Solution: Flame graph data generation
     */
    public static void generateFlameGraphData() {
        System.out.println("=== Flame Graph Data ===\n");

        Map<String, Integer> stackCounts = new HashMap<>();

        // Generate diverse stack traces
        for (int i = 0; i < 1000; i++) {
            methodA(i);
        }

        // Collect sample stack traces
        for (int i = 0; i < 100; i++) {
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
        System.out.println("# count stack");
        stackCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(15)
                .forEach(e -> System.out.println(e.getValue() + " " + e.getKey()));

        System.out.println("\nInstructions:");
        System.out.println("1. Copy the above output to a file");
        System.out.println("2. Use: flamegraph.pl stacks.txt > flame.svg");
        System.out.println("3. Or paste into https://www.speedscope.app/");
    }

    private static void methodA(int depth) {
        if (depth > 0) {
            methodB(depth);
        }
    }

    private static void methodB(int depth) {
        if (depth > 0) {
            methodC(depth);
        }
    }

    private static void methodC(int depth) {
        // Leaf method
        Math.sin(depth);
    }

    public static void main(String[] args) {
        System.out.println("=== Profiling Solutions ===\n");

        // Exercise 1
        createJfrRecording();

        // Exercise 2
        System.out.println("\n---");
        jmhBenchmark();

        // Exercise 3
        System.out.println("\n---");
        analyzeThreadDump();

        // Exercise 4
        System.out.println("\n---");
        profileMemoryAllocation();

        // Exercise 5
        System.out.println("\n---");
        generateFlameGraphData();
    }
}
