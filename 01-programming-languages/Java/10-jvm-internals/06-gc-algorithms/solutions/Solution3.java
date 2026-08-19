package academy.javaengineering.jvm.gc_algorithms;

/**
 * Solution 3: GC Algorithm Analysis
 */
public class Solution3 {

    public static void main(String[] args) {
        System.out.println("=== GC Log Analysis Guide ===\n");

        System.out.println("Step 1: Enable detailed GC logging:");
        System.out.println("  java -Xlog:gc*:file=gc.log:time,uptime,level,tags -jar app.jar\n");

        System.out.println("Step 2: Identify GC algorithm from log format:");
        System.out.println("  G1: [GC pause (G1 Evacuation Pause) ...]");
        System.out.println("  ZGC: [GC pause (Pause Mark Start) ...]");
        System.out.println("  Shenandoah: [GC pause (pause reason) ...]\n");

        System.out.println("Step 3: Key metrics to extract:");
        System.out.println("  - Pause times: Look for 'pause' in log entries");
        System.out.println("  - GC frequency: Count GC events per time period");
        System.out.println("  - Memory before/after: [GC ... GC->free] patterns");
        System.out.println("  - Allocation rate: Memory growth between GCs\n");

        System.out.println("Step 4: Tuning recommendations:");
        System.out.println("  If pauses too long: Increase heap, tune region size");
        System.out.println("  If GC too frequent: Increase heap, reduce allocation rate");
        System.out.println("  If Full GC occurring: Reduce IHOP, increase Old gen");
        System.out.println("  If promotion failures: Increase MaxTenuringThreshold\n");

        System.out.println("Tools for analysis:");
        System.out.println("  - GCEasy (gceasy.io): Online GC log analyzer");
        System.out.println("  - GCViewer: Desktop GC log viewer");
        System.out.println("  - JClarity Censum: Commercial GC analysis");
    }
}
