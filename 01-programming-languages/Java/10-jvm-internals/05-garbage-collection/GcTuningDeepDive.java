package academy.javaengineering.jvm.garbagecollection;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

/**
 * GC Tuning Deep Dive
 * Covers G1, ZGC, Shenandoah tuning, GC logging, and log analysis.
 */
public class GcTuningDeepDive {

    private static final int ALLOCATION_SIZE = 1024 * 1024; // 1MB

    public static void main(String[] args) {
        System.out.println("=== GC Tuning Deep Dive ===\n");

        // 1. G1 Tuning
        demonstrateG1Tuning();

        // 2. ZGC Tuning
        demonstrateZGCTuning();

        // 3. Shenandoah Tuning
        demonstrateShenandoahTuning();

        // 4. GC Logging
        demonstrateGCLogging();

        // 5. GC Log Analysis Tools
        demonstrateLogAnalysis();

        // 6. Tuning Methodology
        demonstrateTuningMethodology();
    }

    /**
     * G1 GC Tuning Parameters
     */
    private static void demonstrateG1Tuning() {
        System.out.println("--- 1. G1 GC Tuning ---");

        System.out.println("Key G1 Parameters:");
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ Parameter                    │ Default   │ Description     │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│ -XX:MaxGCPauseMillis         │ 200ms     │ Target max pause│");
        System.out.println("│ -XX:G1HeapRegionSize         │ Auto      │ Region size     │");
        System.out.println("│ -XX:InitiatingHeapOccupancy │ 45%       │ IHOP threshold  │");
        System.out.println("│ -XX:G1ReservePercent         │ 10%       │ Reserve for     │");
        System.out.println("│                              │           │ to-space        │");
        System.out.println("│ -XX:G1NewSizePercent         │ 5%        │ Min young gen   │");
        System.out.println("│ -XX:G1MaxNewSizePercent      │ 60%       │ Max young gen   │");
        System.out.println("│ -XX:G1MixedGCCountTarget    │ 8         │ Mixed GC target │");
        System.out.println("│ -XX:G1HeapWastePercent       │ 5%        │ Waste threshold │");
        System.out.println("│ -XX:G1MixedGCLiveThreshold  │ 85%       │ Live threshold  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        System.out.println("G1 Tuning Strategy:");
        System.out.println("  1. Start with -XX:MaxGCPauseMillis=200");
        System.out.println("  2. Monitor GC logs for pause times");
        System.out.println("  3. If pauses too long:");
        System.out.println("     - Increase MaxGCPauseMillis");
        System.out.println("     - Increase G1HeapRegionSize");
        System.out.println("     - Decrease IHOP");
        System.out.println("  4. If throughput too low:");
        System.out.println("     - Decrease MaxGCPauseMillis");
        System.out.println("     - Increase G1NewSizePercent");
        System.out.println("     - Increase IHOP\n");

        System.out.println("G1 GC Flags:");
        System.out.println("  -XX:+UseG1GC                  (enable G1)");
        System.out.println("  -XX:MaxGCPauseMillis=200      (target pause)");
        System.out.println("  -XX:G1HeapRegionSize=16m      (region size)");
        System.out.println("  -XX:InitiatingHeapOccupancyPercent=45");
        System.out.println("  -XX:G1ReservePercent=10");
        System.out.println("  -XX:G1NewSizePercent=5");
        System.out.println("  -XX:G1MaxNewSizePercent=60");
        System.out.println("  -XX:G1MixedGCCountTarget=8");
        System.out.println("  -XX:G1HeapWastePercent=5\n");

        simulateGCActivity("G1 GC Tuning");
    }

    /**
     * ZGC Tuning Parameters
     */
    private static void demonstrateZGCTuning() {
        System.out.println("--- 2. ZGC Tuning ---");

        System.out.println("Key ZGC Parameters:");
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ Parameter                    │ Default   │ Description     │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│ -XX:+UseZGC                  │ false     │ Enable ZGC      │");
        System.out.println("│ -XX:ZCollectionInterval     │ 5 sec     │ Cycle interval  │");
        System.out.println("│ -XX:ZAllocationSpikeTolerance│ 2.0       │ Allocation      │");
        System.out.println("│                              │           │ spike tolerance │");
        System.out.println("│ -XX:SoftMaxHeapSize         │ 0         │ Soft heap limit │");
        System.out.println("│ -XX:ZFragmentationLimit     │ 25        │ Fragmentation   │");
        System.out.println("│                              │           │ limit           │");
        System.out.println("│ -XX:ZUncommitDelay          │ 300000 ms │ Uncommit delay  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        System.out.println("ZGC Tuning Strategy:");
        System.out.println("  1. Set -Xmx to desired max heap");
        System.out.println("  2. Set -XX:SoftMaxHeapSize for soft limit");
        System.out.println("  3. Monitor ZGC cycle logs");
        System.out.println("  4. Adjust ZCollectionInterval if cycles too frequent");
        System.out.println("  5. Adjust ZAllocationSpikeTolerance for allocation spikes\n");

        System.out.println("ZGC Flags:");
        System.out.println("  -XX:+UseZGC                   (enable ZGC)");
        System.out.println("  -XX:+ZGenerational             (enable generational ZGC, Java 21+)");
        System.out.println("  -XX:ZCollectionInterval=5     (seconds between cycles)");
        System.out.println("  -XX:ZAllocationSpikeTolerance=2.0");
        System.out.println("  -XX:SoftMaxHeapSize=8g        (soft limit)");
        System.out.println("  -XX:ZUncommitDelay=300000     (ms)");
        System.out.println("  -XX:ZFragmentationLimit=25");
        System.out.println("  -Xmx16g                       (max heap)\n");

        System.out.println("ZGC Generational Mode (Java 21+):");
        System.out.println("  -XX:+ZGenerational             (enable generational ZGC)");
        System.out.println("  - Better throughput for young objects");
        System.out.println("  - Lower memory overhead");
        System.out.println("  - Recommended for most applications\n");
    }

    /**
     * Shenandoah GC Tuning Parameters
     */
    private static void demonstrateShenandoahTuning() {
        System.out.println("--- 3. Shenandoah GC Tuning ---");

        System.out.println("Key Shenandoah Parameters:");
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ Parameter                    │ Default   │ Description     │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│ -XX:+UseShenandoahGC         │ false     │ Enable Shenand. │");
        System.out.println("│ -XX:ShenandoahGCHeuristics   │ adaptive  │ Heuristic mode  │");
        System.out.println("│ -XX:ShenandoahMinFreeThreshold│ 10%      │ Min free for    │");
        System.out.println("│                              │           │ triggering GC   │");
        System.out.println("│ -XX:ShenandoahUncommitDelay  │ 300000 ms │ Uncommit delay  │");
        System.out.println("│ -XX:ShenandoahGuaranteedGCInterval│ 300k│ Guaranteed GC   │");
        System.out.println("│ -XX:ShenandoahCommitInterval │ 1000 ms   │ Commit interval │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        System.out.println("Shenandoah Heuristic Modes:");
        System.out.println("  adaptive:   Automatic tuning (default)");
        System.out.println("  compact:    Always compact (lowest pause)");
        System.out.println("  static:     Static threshold");
        System.out.println("  aggressive: More frequent collection");
        System.out.println("  passive:    Only at safepoints (testing)\n");

        System.out.println("Shenandoah Flags:");
        System.out.println("  -XX:+UseShenandoahGC              (enable Shenandoah)");
        System.out.println("  -XX:ShenandoahGCHeuristics=adaptive");
        System.out.println("  -XX:ShenandoahMinFreeThreshold=10");
        System.out.println("  -XX:ShenandoahGuaranteedGCInterval=300000");
        System.out.println("  -XX:ShenandoahUncommitDelay=300000");
        System.out.println("  -XX:ShenandoahCommitInterval=1000\n");

        System.out.println("Shenandoah Tuning Strategy:");
        System.out.println("  1. Start with adaptive heuristics");
        System.out.println("  2. Monitor pause times in GC logs");
        System.out.println("  3. If pauses too long:");
        System.out.println("     - Use compact heuristics");
        System.out.println("     - Increase MinFreeThreshold");
        System.out.println("  4. If throughput too low:");
        System.out.println("     - Use aggressive heuristics");
        System.out.println("     - Decrease MinFreeThreshold\n");
    }

    /**
     * GC Logging Configuration
     */
    private static void demonstrateGCLogging() {
        System.out.println("--- 4. GC Logging ---");

        System.out.println("GC Logging is essential for tuning and debugging.\n");

        System.out.println("Java 9+ Unified Logging (Xlog):");
        System.out.println("  Basic GC logging:");
        System.out.println("    java -Xlog:gc* MyApp\n");

        System.out.println("  GC logging to file:");
        System.out.println("    java -Xlog:gc*:file=gc.log MyApp\n");

        System.out.println("  Detailed GC logging:");
        System.out.println("    java -Xlog:gc*:file=gc.log:time,uptime,level,tags MyApp\n");

        System.out.println("  GC logging with rotation:");
        System.out.println("    java -Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=5,filesize=10m MyApp\n");

        System.out.println("Java 8 GC Logging (deprecated):");
        System.out.println("    java -XX:+PrintGCDetails -XX:+PrintGCDateStamps \\");
        System.out.println("         -Xloggc:gc.log -XX:+PrintHeapAtGC MyApp\n");

        System.out.println("GC Log Output Example:");
        System.out.println("  [0.123s][info][gc] GC(0) Pause Young (Normal) 12ms");
        System.out.println("  [0.123s][info][gc] GC(0)   Eden regions: 8->2(8)");
        System.out.println("  [0.123s][info][gc] GC(0)   Survivor regions: 0->1(1)");
        System.out.println("  [0.123s][info][gc] GC(0)   Old regions: 0->0(10)");
        System.out.println("  [0.123s][info][gc] GC(0)   Heap: 50.0M->12.5M(256.0M)\n");

        System.out.println("Important GC Log Events:");
        System.out.println("  - Pause Young: Minor GC");
        System.out.println("  - Pause Full: Full GC");
        System.out.println("  - Concurrent Mark Start/End: G1/ZGC concurrent phase");
        System.out.println("  - Evacuation Pause: G1 evacuation");
        System.out.println("  - Pause Init Mark: ZGC/Shenandoah init mark\n");

        System.out.println("GC Log Analysis Metrics:");
        System.out.println("  - Pause duration: Time spent in GC");
        System.out.println("  - Pause frequency: How often GC runs");
        System.out.println("  - Heap before/after: Memory usage");
        System.out.println("  - Promotion rate: Objects promoted to Old Gen");
        System.out.println("  - Allocation rate: Objects allocated in Eden\n");
    }

    /**
     * GC Log Analysis Tools
     */
    private static void demonstrateLogAnalysis() {
        System.out.println("--- 5. GC Log Analysis Tools ---");

        System.out.println("Free Tools:");
        System.out.println("  1. GCEasy (https://gceasy.io)");
        System.out.println("     - Online GC log analyzer");
        System.out.println("     - Visualizes GC pauses");
        System.out.println("     - Provides recommendations\n");

        System.out.println("  2. GCViewer (https://github.com/chewiebug/GCViewer)");
        System.out.println("     - Desktop application");
        System.out.println("     - Real-time visualization");
        System.out.println("     - Supports multiple GC formats\n");

        System.out.println("  3. JClarity Censum (Commercial)");
        System.out.println("     - Advanced analysis");
        System.out.println("     - Machine learning recommendations");
        System.out.println("     - Production monitoring\n");

        System.out.println("Command-line Analysis:");
        System.out.println("  # Extract pause times from GC log");
        System.out.println("  grep -E 'Pause (Young|Full)' gc.log | awk '{print $NF}'\n");

        System.out.println("  # Count GC events");
        System.out.println("  grep -c 'GC' gc.log\n");

        System.out.println("  # Average pause time");
        System.out.println("  grep -oP 'Pause Young.*?\\d+ms' gc.log | awk -F'ms' '{sum+=$1; count++} END {print sum/count}'\n");

        System.out.println("GC Log Interpretation:");
        System.out.println("  Good signs:");
        System.out.println("    - Short pause times (< target)");
        System.out.println("    - Consistent pause frequency");
        System.out.println("    - Low promotion rate");
        System.out.println("    - Heap usage stable\n");

        System.out.println("  Bad signs:");
        System.out.println("    - Long pause times (> target)");
        System.out.println("    - Increasing pause frequency");
        System.out.println("    - High promotion rate");
        System.out.println("    - Heap usage growing steadily\n");
    }

    /**
     * GC Tuning Methodology
     */
    private static void demonstrateTuningMethodology() {
        System.out.println("--- 6. GC Tuning Methodology ---");

        System.out.println("Step-by-step tuning process:");
        System.out.println("  1. Set clear performance goals");
        System.out.println("     - Max pause time target");
        System.out.println("     - Throughput target (e.g., 95%)");
        System.out.println("     - Memory footprint target\n");

        System.out.println("  2. Enable GC logging");
        System.out.println("    java -Xlog:gc*:file=gc.log:time,uptime,level,tags MyApp\n");

        System.out.println("  3. Run representative workload");
        System.out.println("    - Simulate production traffic");
        System.out.println("    - Run for sufficient time (hours)");
        System.out.println("    - Include warm-up period\n");

        System.out.println("  4. Analyze GC logs");
        System.out.println("    - Check pause times");
        System.out.println("    - Check throughput");
        System.out.println("    - Check memory usage\n");

        System.out.println("  5. Tune parameters iteratively");
        System.out.println("    - Change ONE parameter at a time");
        System.out.println("    - Measure impact");
        System.out.println("    - Repeat until goals are met\n");

        System.out.println("Common Tuning Scenarios:");
        System.out.println("  Scenario 1: Long GC pauses");
        System.out.println("    - Increase MaxGCPauseMillis");
        System.out.println("    - Increase heap size");
        System.out.println("    - Use ZGC or Shenandoah\n");

        System.out.println("  Scenario 2: Frequent GC");
        System.out.println("    - Increase heap size");
        System.out.println("    - Reduce allocation rate");
        System.out.println("    - Tune IHOP (G1)\n");

        System.out.println("  Scenario 3: High CPU during GC");
        System.out.println("    - Reduce GC threads");
        System.out.println("    - Use less aggressive collector");
        System.out.println("    - Optimize application\n");

        System.out.println("  Scenario 4: OutOfMemoryError");
        System.out.println("    - Increase heap size");
        System.out.println("    - Fix memory leak");
        System.out.println("    - Analyze heap dump\n");

        System.out.println("Monitoring Commands:");
        System.out.println("  jstat -gcutil <pid> 1000      (GC stats every 1s)");
        System.out.println("  jcmd <pid> GC.heap_info      (heap information)");
        System.out.println("  jcmd <pid> VM.flags           (JVM flags)");
        System.out.println("  jcmd <pid> GC.run             (force GC)");
        System.out.println("  jmap -heap <pid>              (heap map)\n");
    }

    private static void simulateGCActivity(String label) {
        System.out.println("  Simulating GC activity for " + label + "...");
        List<byte[]> memory = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            memory.add(new byte[ALLOCATION_SIZE]);
            System.out.println("    Allocated " + (i + 1) + " MB");
        }

        // Print current GC stats
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("    " + gcBean.getName() + ": Collections=" +
                    gcBean.getCollectionCount() + ", Time=" +
                    gcBean.getCollectionTime() + "ms");
        }

        memory.clear();
        System.out.println("    Released memory\n");
    }
}
