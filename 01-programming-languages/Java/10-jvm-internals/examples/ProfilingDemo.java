package academy.javaengineering.jvm.examples;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Profiling Demo
 * Demonstrates profiling tools, JFR, async-profiler, JMH,
 * flame graphs, and performance analysis techniques.
 */
public class ProfilingDemo {

    private static final int WARMUP_ITERATIONS = 10000;
    private static final int MEASURE_ITERATIONS = 100000;

    /**
     * DEMO 1: JFR (Java Flight Recorder)
     */
    public static void demonstrateJFR() {
        System.out.println("=== Java Flight Recorder (JFR) ===");
        System.out.println("Low-overhead profiling built into JDK (since Java 11)");
        System.out.println("Production-safe: <1% overhead");
        System.out.println();

        System.out.println("Command-line usage:");
        System.out.println("  jcmd <pid> JFR.start duration=60s filename=recording.jfr");
        System.out.println("  jcmd <pid> JFR.start settings=profile duration=30s");
        System.out.println("  java -XX:StartFlightRecording=duration=60s,filename=rec.jfr MyApp");
        System.out.println("  java -XX:StartFlightRecording=duration=0,filename=rec.jfr MyApp");

        System.out.println("\nEvent categories:");
        System.out.println("  - jdk.ExecutionSample (CPU profiling)");
        System.out.println("  - jdk.GarbageCollection (GC events)");
        System.out.println("  - jdk.ObjectAllocationInNewTLAB (allocation profiling)");
        System.out.println("  - jdk.JavaMonitorWait (lock contention)");
        System.out.println("  - jdk.SocketRead/SocketWrite (I/O profiling)");
        System.out.println("  - jdk.ThreadStart/ThreadEnd");
        System.out.println("  - jdk.ExceptionThrow");

        System.out.println("\nProgrammatic recording:");
        System.out.println("  Recording rec = new Recording();");
        System.out.println("  rec.enable(EventNames.GarbageCollection);");
        System.out.println("  rec.enable(EventNames.ExecutionSample);");
        System.out.println("  rec.start();");
        System.out.println("  // ... your code ...");
        System.out.println("  rec.dump(Paths.get(\"recording.jfr\"));");

        System.out.println("\nAnalyze with:");
        System.out.println("  - JDK Mission Control (JMC)");
        System.out.println("  - jfr print --events jdk.ExecutionSample recording.jfr");
    }

    /**
     * DEMO 2: async-profiler
     */
    public static void demonstrateAsyncProfiler() {
        System.out.println("\n=== async-profiler ===");
        System.out.println("Low-overhead sampling profiler using perf_events and asyncGetCallTrace");
        System.out.println("No safepoint bias (unlike -XX:+PreserveFramePointer)");
        System.out.println();

        System.out.println("Installation:");
        System.out.println("  brew install async-profiler");
        System.out.println("  Or download from GitHub: https://github.com/async-profiler/async-profiler");

        System.out.println("\nUsage:");
        System.out.println("  # CPU profiling (wall clock)");
        System.out.println("  asprof -d 30 -f cpu_profile.html <pid>");
        System.out.println("");
        System.out.println("  # Alloc profiling");
        System.out.println("  asprof -e alloc -d 30 -f alloc_profile.html <pid>");
        System.out.println("");
        System.out.println("  # Lock profiling");
        System.out.println("  asprof -e lock -d 30 -f lock_profile.html <pid>");
        System.out.println("");
        System.out.println("  # Wall-clock profiling");
        System.out.println("  asprof -e wall -d 30 -f wall_profile.html <pid>");

        System.out.println("\nOutput formats:");
        System.out.println("  - HTML flame graphs (default)");
        System.out.println("  - JMH-compatible collapsed stacks");
        System.out.println("  - pprof format");
        System.out.println("  - Java method traces");
    }

    /**
     * DEMO 3: JMH (Java Microbenchmark Harness)
     */
    public static void demonstrateJMH() {
        System.out.println("\n=== JMH (Java Microbenchmark Harness) ===");
        System.out.println("For writing correct microbenchmarks");
        System.out.println();

        System.out.println("Setup (Maven):");
        System.out.println("  <dependency>");
        System.out.println("    <groupId>org.openjdk.jmh</groupId>");
        System.out.println("    <artifactId>jmh-core</artifactId>");
        System.out.println("    <version>1.37</version>");
        System.out.println("  </dependency>");

        System.out.println("\nExample JMH benchmark:");
        System.out.println("  @BenchmarkMode(Mode.AverageTime)");
        System.out.println("  @OutputTimeUnit(TimeUnit.NANOSECONDS)");
        System.out.println("  @Warmup(iterations = 5, time = 1)");
        System.out.println("  @Measurement(iterations = 5, time = 1)");
        System.out.println("  @Fork(1)");
        System.out.println("  public class MyBenchmark {");
        System.out.println("    @Benchmark");
        System.out.println("    public int testMethod() {");
        System.out.println("      return Integer.bitCount(42);");
        System.out.println("    }");
        System.out.println("  }");

        System.out.println("\nRun: mvn clean install && java -jar target/benchmarks.jar");
        System.out.println("\nKey annotations:");
        System.out.println("  @Benchmark    - marks benchmark method");
        System.out.println("  @Setup        - setup before benchmark");
        System.out.println("  @TearDown     - cleanup after benchmark");
        System.out.println("  @State        - shared state across threads");
        System.out.println("  @Param        - parameterized benchmarks");
        System.out.println("  @CompilerControl - control inlining/C2");
    }

    /**
     * DEMO 4: Flame Graphs
     */
    public static void demonstrateFlameGraphs() {
        System.out.println("\n=== Flame Graphs ===");
        System.out.println("Visualization of stack trace profiles");
        System.out.println();
        System.out.println("How to read flame graphs:");
        System.out.println("  X-axis: stack depth (left = leaf, right = root)");
        System.out.println("  Y-axis: percentage of samples (wider = more time)");
        System.out.println("  Color: typically random (not meaningful)");
        System.out.println();
        System.out.println("What to look for:");
        System.out.println("  - Wide bars at the top = hot methods");
        System.out.println("  - Tall stacks = deep call chains");
        System.out.println("  - Missing frames = compiled (optimized away)");
        System.out.println("  - Locked (gray) frames = JVM internals");

        System.out.println("\nGenerate flame graphs:");
        System.out.println("  async-profiler: asprof -f flame.html <pid>");
        System.out.println("  jcmd: jcmd <pid> Compiler.perfmap");
        System.out.println("  Convert collapsed: flamegraph.pl stack.txt > flame.svg");
    }

    /**
     * DEMO 5: Memory Profiling
     */
    public static void demonstrateMemoryProfiling() {
        System.out.println("\n=== Memory Profiling ===");
        System.out.println("Tools for memory analysis:");
        System.out.println();
        System.out.println("1. JFR allocation profiling:");
        System.out.println("   -XX:StartFlightRecording=filename=rec.jfr");
        System.out.println("   Enables jdk.ObjectAllocationInNewTLAB events");
        System.out.println();
        System.out.println("2. async-profiler allocation profiling:");
        System.out.println("   asprof -e alloc -f alloc.html <pid>");
        System.out.println();
        System.out.println("3. VisualVM heap dump analysis:");
        System.out.println("   jmap -dump:live,format=b,file=heap.hprof <pid>");
        System.out.println("   Open in VisualVM or Eclipse MAT");
        System.out.println();
        System.out.println("4. JHat (JDK 8, removed in 9):");
        System.out.println("   jhat heap.hprof (web server on port 7000)");
        System.out.println();
        System.out.println("5. OQL (Object Query Language):");
        System.out.println("   SELECT s FROM java.lang.String s WHERE s.count > 100");

        // Demonstrate allocation tracking
        System.out.println("\nManual allocation tracking:");
        System.out.println("  Runtime.freeMemory() - before/after");
        System.out.println("  ManagementFactory.getMemoryMXBean()");
        System.out.println("  ManagementFactory.getGarbageCollectorMXBeans()");
    }

    /**
     * DEMO 6: Thread Profiling
     */
    public static void demonstrateThreadProfiling() {
        System.out.println("\n=== Thread Profiling ===");
        System.out.println("Tools:");
        System.out.println("  jstack <pid>            - thread dump");
        System.out.println("  jcmd <pid> Thread.print  - thread dump");
        System.out.println("  jconsole                - thread monitoring");
        System.out.println("  VisualVM                - thread visualization");

        System.out.println("\nThread states to watch:");
        System.out.println("  RUNNABLE     - actively executing");
        System.out.println("  BLOCKED      - waiting for monitor lock");
        System.out.println("  WAITING      - wait()/join()/park()");
        System.out.println("  TIMED_WAITING - sleep()/wait(timeout)");
        System.out.println("  NEW          - created but not started");
        System.out.println("  TERMINATED   - completed execution");

        System.out.println("\nDetect thread leaks:");
        System.out.println("  jcmd <pid> Thread.print | grep RUNNABLE | wc -l");

        // Show thread info
        System.out.println("\nCurrent thread:");
        Thread current = Thread.currentThread();
        System.out.println("  Name: " + current.getName());
        System.out.println("  State: " + current.getState());
        System.out.println("  Priority: " + current.getPriority());
        System.out.println("  Is daemon: " + current.isDaemon());
    }

    /**
     * DEMO 7: CPU Sampling
     */
    public static void demonstrateCPUSampling() {
        System.out.println("\n=== CPU Sampling ===");
        System.out.println("Methods:");
        System.out.println("  1. Timer-based sampling (jprofiler, YourKit)");
        System.out.println("  2. perf_events (Linux, async-profiler)");
        System.out.println("  3. dtrace (Solaris/macOS)");
        System.out.println("  4. -XX:+PreserveFramePointer + BPF");
        System.out.println();
        System.out.println("JFR CPU profiling:");
        System.out.println("  jcmd <pid> JFR.start settings=profile");
        System.out.println("  jcmd <pid> JFR.stop");

        // Simple CPU-intensive work
        System.out.println("\nDemo: measuring CPU time");
        long start = System.nanoTime();
        double result = 0;
        for (int i = 0; i < 1_000_000; i++) {
            result += Math.sin(i) * Math.cos(i);
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("  1M trig operations: " + (elapsed / 1000) + " μs");
        System.out.println("  Result: " + result);
    }

    /**
     * DEMO 8: Profiling Overhead
     */
    public static void demonstrateOverhead() {
        System.out.println("\n=== Profiling Overhead Comparison ===");
        System.out.println("┌──────────────────┬────────────┬──────────────────────────┐");
        System.out.println("│ Tool             │ Overhead   │ Notes                    │");
        System.out.println("├──────────────────┼────────────┼──────────────────────────┤");
        System.out.println("│ JFR (default)    │ <1%        │ Production safe          │");
        System.out.println("│ JFR (profile)    │ 2-5%       │ More detailed            │");
        System.out.println("│ async-profiler   │ 1-3%       │ Sampling, minimal bias   │");
        System.out.println("│ jstack           │ <1%        │ Point-in-time snapshot   │");
        System.out.println("│ jmap dump        │ 5-20%      │ Stop-the-world           │");
        System.out.println("│ hprof            │ 10-50%     │ Never in production!     │");
        System.out.println("│ VisualVM         │ 5-15%      │ Sampling mode better     │");
        System.out.println("│ JVMTI agents     │ Variable   │ Depends on agent         │");
        System.out.println("└──────────────────┴────────────┴──────────────────────────┘");

        System.out.println("\nRecommendations:");
        System.out.println("  - Production: JFR (always on)");
        System.out.println("  - Staging: async-profiler + JFR");
        System.out.println("  - Development: VisualVM, JProfiler");
        System.out.println("  - Never: hprof in production");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      PROFILING TOOLS DEMO           ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateJFR();
        demonstrateAsyncProfiler();
        demonstrateJMH();
        demonstrateFlameGraphs();
        demonstrateMemoryProfiling();
        demonstrateThreadProfiling();
        demonstrateCPUSampling();
        demonstrateOverhead();

        // Quick live demo
        System.out.println("\n=== Live Thread Dump ===");
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        System.out.println("Thread count: " + tmx.getThreadCount());
        System.out.println("Peak thread count: " + tmx.getPeakThreadCount());
        System.out.println("Daemon thread count: " + tmx.getDaemonThreadCount());
        System.out.println("Total started: " + tmx.getTotalStartedThreadCount());
    }
}
