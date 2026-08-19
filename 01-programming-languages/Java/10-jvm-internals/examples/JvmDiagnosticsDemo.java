package academy.javaengineering.jvm.examples;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * JVM Diagnostics Demo
 * Demonstrates jcmd, jstack, jmap, jstat, jinfo, and other
 * diagnostic tools for JVM troubleshooting.
 */
public class JvmDiagnosticsDemo {

    private static final List<byte[]> memoryConsumer = new ArrayList<>();

    /**
     * DEMO 1: jcmd (Java Diagnostic Command)
     * Unified diagnostic tool replacing jps, jstack, jmap, jinfo, jstat
     */
    public static void demonstrateJcmd() {
        System.out.println("=== jcmd (Java Diagnostic Command) ===");
        System.out.println("Most versatile diagnostic tool (JDK 9+)");
        System.out.println();

        System.out.println("Basic usage:");
        System.out.println("  jcmd                     # List all JVM processes");
        System.out.println("  jcmd <pid> help          # List available commands");
        System.out.println("  jcmd <pid> VM.version    # JVM version");
        System.out.println("  jcmd <pid> VM.flags      # JVM flags");
        System.out.println("  jcmd <pid> GC.heap_info  # Heap information");
        System.out.println();

        System.out.println("Threading:");
        System.out.println("  jcmd <pid> Thread.print             # Thread dump");
        System.out.println("  jcmd <pid> Thread.print -l          # Locks info");
        System.out.println("  jcmd <pid> Thread.print -e          # Extended info");
        System.out.println();

        System.out.println("GC:");
        System.out.println("  jcmd <pid> GC.run                   # Trigger GC");
        System.out.println("  jcmd <pid> GC.heap_info             # Heap details");
        System.out.println("  jcmd <pid> GC.class_stats           # Class stats");
        System.out.println("  jcmd <pid> GC.class_histogram       # Class histogram");
        System.out.println("  jcmd <pid> GC.heap_dump heap.hprof  # Heap dump");
        System.out.println();

        System.out.println("VM:");
        System.out.println("  jcmd <pid> VM.flags                 # Non-default flags");
        System.out.println("  jcmd <pid> VM.flags -all            # All flags");
        System.out.println("  jcmd <pid> VM.system_properties     # System properties");
        System.out.println("  jcmd <pid> VM.command_line           # Command line");
        System.out.println("  jcmd <pid> VM.version               # Version info");
        System.out.println();

        System.out.println("Performance:");
        System.out.println("  jcmd <pid> Compiler.codecache       # Code cache info");
        System.out.println("  jcmd <pid> Compiler.codelist        # Compiled methods");
        System.out.println("  jcmd <pid> PerfCounter.print        # Perf counters");
        System.out.println();

        System.out.println("JFR:");
        System.out.println("  jcmd <pid> JFR.start settings=profile");
        System.out.println("  jcmd <pid> JFR.dump filename=rec.jfr");
        System.out.println("  jcmd <pid> JFR.stop");
    }

    /**
     * DEMO 2: jstack
     * Thread dump analysis
     */
    public static void demonstrateJstack() {
        System.out.println("\n=== jstack (Thread Dump) ===");
        System.out.println("Usage:");
        System.out.println("  jstack <pid>                # Thread dump");
        System.out.println("  jstack -l <pid>             # Lock info");
        System.out.println("  jstack -e <pid>             # Extended info");
        System.out.println("  jstack -F <pid>             # Force dump (if hung)");
        System.out.println();

        System.out.println("Thread dump analysis:");
        System.out.println("  Look for:");
        System.out.println("    - BLOCKED threads (deadlock, lock contention)");
        System.out.println("    - Long WAITING/TIMED_WAITING (slow operations)");
        System.out.println("    - High CPU threads (stuck in computation)");
        System.out.println("    - Thread count (thread leak?)");
        System.out.println();

        System.out.println("Deadlock detection:");
        System.out.println("  jstack will automatically detect deadlocks");
        System.out.println("  Look for: 'Found one Java-level deadlock'");
        System.out.println();

        System.out.println("Online thread dump analysis:");
        System.out.println("  https://fastthread.io/");
        System.out.println("  https://spotify.github.io/thread-dump-analyzer/");

        // Show current threads
        System.out.println("\nCurrent JVM threads:");
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        long blocked = threads.stream()
                .filter(t -> t.getState() == Thread.State.BLOCKED).count();
        long waiting = threads.stream()
                .filter(t -> t.getState() == Thread.State.WAITING).count();
        System.out.println("  Total: " + threads.size());
        System.out.println("  BLOCKED: " + blocked);
        System.out.println("  WAITING: " + waiting);
    }

    /**
     * DEMO 3: jmap
     * Memory map and heap dump
     */
    public static void demonstrateJmap() {
        System.out.println("\n=== jmap (Memory Map) ===");
        System.out.println("Usage:");
        System.out.println("  jmap <pid>                    # Process memory map");
        System.out.println("  jmap -heap <pid>              # Heap summary");
        System.out.println("  jmap -histo <pid>             # Object histogram");
        System.out.println("  jmap -histo:live <pid>        # Live objects only");
        System.out.println("  jmap -dump:format=b,file=heap.hprof <pid>");
        System.out.println("  jmap -dump:live,format=b,file=heap.hprof <pid>");
        System.out.println();

        System.out.println("Heap dump analysis:");
        System.out.println("  - Eclipse Memory Analyzer (MAT)");
        System.out.println("  - VisualVM");
        System.out.println("  - JProfiler");
        System.out.println("  - YourKit");
        System.out.println();

        System.out.println("Memory leak suspects (MAT):");
        System.out.println("  1. Leak Suspects Report");
        System.out.println("  2. Dominator Tree");
        System.out.println("  3. Histogram (group by package)");
        System.out.println("  4. OQL queries");
        System.out.println();

        System.out.println("⚠️  jmap -dump causes STW pause!");
        System.out.println("   Use -dump:live for smaller dumps");
        System.out.println("   Better alternative: jcmd <pid> GC.heap_dump");
    }

    /**
     * DEMO 4: jstat
     * JVM statistics monitoring
     */
    public static void demonstrateJstat() {
        System.out.println("\n=== jstat (JVM Statistics) ===");
        System.out.println("Usage: jstat -<option> <pid> [interval] [count]");
        System.out.println();

        System.out.println("GC statistics:");
        System.out.println("  jstat -gc <pid>              # GC stats (KB)");
        System.out.println("  jstat -gcutil <pid>          # GC utilization (%)");
        System.out.println("  jstat -gccapacity <pid>      # GC capacity (KB)");
        System.out.println();

        System.out.println("Class statistics:");
        System.out.println("  jstat -class <pid>           # Class loader stats");
        System.out.println("  jstat -compiler <pid>        # JIT compiler stats");
        System.out.println("  jstat -printcompilation <pid> # Compilation details");
        System.out.println();

        System.out.println("Output columns (gcutil):");
        System.out.println("  S0     - Survivor 0 usage (%)");
        System.out.println("  S1     - Survivor 1 usage (%)");
        System.out.println("  E      - Eden usage (%)");
        System.out.println("  O      - Old usage (%)");
        System.out.println("  M      - Metaspace usage (%)");
        System.out.println("  CCS    - Compressed class space (%)");
        System.out.println("  YGC    - Young GC count");
        System.out.println("  YGCT   - Young GC time (sec)");
        System.out.println("  FGC    - Full GC count");
        System.out.println("  FGCT   - Full GC time (sec)");
        System.out.println("  GCT    - Total GC time (sec)");
        System.out.println();

        System.out.println("Continuous monitoring:");
        System.out.println("  jstat -gcutil <pid> 1000 10  # 1 sec interval, 10 samples");
    }

    /**
     * DEMO 5: jinfo
     * JVM configuration info
     */
    public static void demonstrateJinfo() {
        System.out.println("\n=== jinfo (JVM Info) ===");
        System.out.println("Usage:");
        System.out.println("  jinfo <pid>                 # All system properties and flags");
        System.out.println("  jinfo -flags <pid>          # VM flags only");
        System.out.println("  jinfo -sysprops <pid>       # System properties only");
        System.out.println();

        System.out.println("Dynamic flag modification (some flags only):");
        System.out.println("  jinfo -flag +PrintGCDetails <pid>   # Enable");
        System.out.println("  jinfo -flag -PrintGCDetails <pid>   # Disable");
        System.out.println("  jinfo -flag MaxHeapSize=2g <pid>    # Modify");

        System.out.println("\nSystem properties accessible via jinfo:");
        System.out.println("  java.version, java.home, java.class.path");
        System.out.println("  user.dir, user.name, os.name, os.arch");
        System.out.println("  sun.java.command, file.encoding");
    }

    /**
     * DEMO 6: VisualVM
     */
    public static void demonstrateVisualVM() {
        System.out.println("\n=== VisualVM ===");
        System.out.println("GUI tool for monitoring, profiling, diagnostics");
        System.out.println("Download: https://visualvm.github.io/");
        System.out.println();

        System.out.println("Features:");
        System.out.println("  - CPU/Memory profiling");
        System.out.println("  - Thread dump analysis");
        System.out.println("  - Heap dump analysis");
        System.out.println("  - MBean browser");
        System.out.println("  - Sampler (CPU & Memory)");
        System.out.println("  - Plugin support (VisualGC, BTrace)");

        System.out.println("\nRemote monitoring:");
        System.out.println("  1. Start JMX agent on target JVM");
        System.out.println("  2. Connect via VisualVM");
        System.out.println("  3. Monitor remotely");
    }

    /**
     * DEMO 7: Diagnostic Flags
     */
    public static void demonstrateDiagnosticFlags() {
        System.out.println("\n=== Diagnostic Flags ===");
        System.out.println("Useful flags for debugging:");
        System.out.println();
        System.out.println("Memory:");
        System.out.println("  -XX:+HeapDumpOnOutOfMemoryError");
        System.out.println("  -XX:HeapDumpPath=/path/to/dumps/");
        System.out.println("  -XX:OnOutOfMemoryError='kill -9 %p'");
        System.out.println();
        System.out.println("GC:");
        System.out.println("  -XX:+PrintGCDetails -XX:+PrintGCDateStamps");
        System.out.println("  -XX:+PrintGCTimeStamps -Xlog:gc*");
        System.out.println("  -XX:+PrintTenuringDistribution");
        System.out.println();
        System.out.println("Classloading:");
        System.out.println("  -XX:+TraceClassLoading -XX:+TraceClassUnloading");
        System.out.println("  -verbose:class");
        System.out.println();
        System.out.println("Threading:");
        System.out.println("  -XX:+PrintCompilation");
        System.out.println("  -XX:+PrintDeoptimizationEvents");
        System.out.println();
        System.out.println("Safety:");
        System.out.println("  -XX:+CheckEndorsedExtDirs");
        System.out.println("  -XX:+UseSplitVerifier");
        System.out.println("  -XX:-UseCompressedOops (debugging)");
    }

    /**
     * DEMO 8: Common Diagnostic Patterns
     */
    public static void demonstrateDiagnosticPatterns() {
        System.out.println("\n=== Common Diagnostic Patterns ===");

        System.out.println("1. High CPU usage:");
        System.out.println("   top -Hp <pid>              # Find hot thread");
        System.out.println("   printf '%x\\n' <tid>       # Convert to hex");
        System.out.println("   jstack <pid> | grep <hex>  # Find stack trace");

        System.out.println("\n2. Memory leak:");
        System.out.println("   jstat -gcutil <pid> 1000   # Watch Old gen grow");
        System.out.println("   jmap -histo:live <pid>     # Large object counts");
        System.out.println("   jmap -dump:live <pid>      # Heap dump");

        System.out.println("\n3. Deadlock:");
        System.out.println("   jstack <pid>               # Look for deadlock message");
        System.out.println("   jcmd <pid> Thread.print -l # Lock details");

        System.out.println("\n4. OutOfMemoryError:");
        System.out.println("   java -XX:+HeapDumpOnOutOfMemoryError MyApp");
        System.out.println("   Analyze dump with MAT/VisualVM");

        System.out.println("\n5. Slow startup:");
        System.out.println("   -XX:+TraceClassLoading     # See loaded classes");
        System.out.println("   -Xlog:class*=info          # JDK 9+ logging");
        System.out.println("   -XX:+PrintCompilation      # JIT activity");
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   JVM DIAGNOSTICS TOOLS DEMO        ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        demonstrateJcmd();
        demonstrateJstack();
        demonstrateJmap();
        demonstrateJstat();
        demonstrateJinfo();
        demonstrateVisualVM();
        demonstrateDiagnosticFlags();
        demonstrateDiagnosticPatterns();

        // Print PID for live demos
        long pid = ProcessHandle.current().pid();
        System.out.println("\n=== This JVM's PID: " + pid + " ===");
        System.out.println("Try running: jcmd " + pid + " VM.version");
    }
}
