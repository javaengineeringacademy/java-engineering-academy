package academy.javaengineering.jvm.gc_algorithms;

/**
 * Exercise 3: GC Algorithm Analysis
 *
 * Task: Analyze GC log output and identify which algorithm was used
 * and what tuning parameters would improve performance.
 */
public class Exercise3 {

    public static void main(String[] args) {
        System.out.println("=== GC Algorithm Analysis ===\n");

        // TODO: Generate GC log output
        // TODO: Identify GC algorithm from log format
        // TODO: Calculate pause time statistics
        // TODO: Suggest tuning parameters

        System.out.println("Run with -Xlog:gc*:file=gc.log and analyze the output.");
        System.out.println("Look for:");
        System.out.println("- GC pause times (should be < target)");
        System.out.println("- GC frequency (too frequent = need more memory)");
        System.out.println("- Allocation rate (high = objects created too fast)");
        System.out.println("- Promotion rate (high = objects live too long)");
    }
}
