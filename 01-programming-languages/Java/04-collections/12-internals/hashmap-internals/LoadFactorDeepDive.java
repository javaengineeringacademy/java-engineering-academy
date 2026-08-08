import java.util.HashMap;
import java.util.Map;

/**
 * LoadFactorDeepDive - Understanding HashMap Internals
 * 
 * This class explores the deep internals of HashMap's load factor,
 * bucket sizing, treeification thresholds, and resizing strategies.
 * 
 * Key concepts:
 * - Why load factor is 0.75 (not 0.5 or 1.0)
 * - Why bucket count must be power of 2
 * - Why treeification threshold is 8
 * - Why untreeification threshold is 6
 * - Resizing strategy and its implications
 * - Memory vs performance trade-offs
 * - Historical changes across JDK versions
 */
public class LoadFactorDeepDive {

    /**
     * Demonstrates why load factor is 0.75.
     * Trade-off between space (memory) and time (collisions).
     */
    public static void whyLoadFactor075() {
        System.out.println("=== Why Load Factor 0.75? ===");
        System.out.println();

        System.out.println("Load Factor (LF) = entries / buckets");
        System.out.println();

        System.out.println("LF = 0.5 (Conservative):");
        System.out.println("  - More memory (50% wasted)");
        System.out.println("  - Fewer collisions");
        System.out.println("  - Faster lookups");
        System.out.println("  - More frequent resizing");
        System.out.println();

        System.out.println("LF = 0.75 (Balanced):");
        System.out.println("  - Good balance of space/time");
        System.out.println("  - Poisson distribution optimization");
        System.out.println("  - Average 0.5 collisions per bucket at LF=0.75");
        System.out.println("  - Chosen by Doug Lea based on analysis");
        System.out.println();

        System.out.println("LF = 1.0 (Aggressive):");
        System.out.println("  - Minimal memory waste");
        System.out.println("  - More collisions");
        System.out.println("  - Slower lookups (O(n) worst case)");
        System.out.println("  - May trigger treeification early");
        System.out.println();

        System.out.println("Mathematical basis:");
        System.out.println("  At LF=0.75, probability of k keys in a bucket:");
        System.out.println("  P(k) ≈ (0.75^k * e^-0.75) / k!");
        System.out.println("  P(0) ≈ 0.47, P(1) ≈ 0.35, P(2) ≈ 0.13");
        System.out.println("  Most buckets have 0-1 entries");
    }

    /**
     * Demonstrates why bucket count is power of 2.
     * Enables efficient modulo using bitwise AND.
     */
    public static void whyPowerOf2() {
        System.out.println("\n=== Why Bucket Count is Power of 2 ===");
        System.out.println();

        int capacity = 16; // Power of 2
        int hash = 0x12345678; // Sample hash

        System.out.println("Capacity: " + capacity + " (power of 2)");
        System.out.println("Hash: " + Integer.toHexString(hash));
        System.out.println();

        // Modulo operation
        int indexModulo = hash % capacity;
        System.out.println("Index (modulo): " + hash + " % " + capacity + " = " + indexModulo);

        // Bitwise AND (optimized)
        int indexAnd = hash & (capacity - 1);
        System.out.println("Index (bitwise): " + hash + " & " + (capacity - 1) + " = " + indexAnd);

        System.out.println();
        System.out.println("Why bitwise AND is faster:");
        System.out.println("  - Modulo requires division (slow)");
        System.out.println("  - Bitwise AND is single CPU instruction");
        System.out.println("  - Power of 2 ensures capacity - 1 is all 1s");
        System.out.println("  - Example: 16 - 1 = 15 = 0b1111");
    }

    /**
     * Demonstrates why treeification threshold is 8.
     * Based on Poisson distribution analysis.
     */
    public static void whyTreeifyThreshold8() {
        System.out.println("\n=== Why Treeification Threshold is 8 ===");
        System.out.println();

        System.out.println("Treeification converts linked list to Red-Black tree.");
        System.out.println("Threshold = 8 based on Poisson distribution:");
        System.out.println();

        // Calculate Poisson probabilities at LF=0.75
        double lambda = 0.75;
        System.out.println("At load factor 0.75, probability of k entries per bucket:");
        for (int k = 0; k <= 10; k++) {
            double probability = Math.pow(lambda, k) * Math.exp(-lambda) / factorial(k);
            System.out.printf("  P(%d) = %.6f%n", k, probability);
        }

        System.out.println();
        System.out.println("Key observations:");
        System.out.println("  - P(8) ≈ 0.000001 (1 in 1 million)");
        System.out.println("  - P(9) ≈ 0.0000001 (1 in 10 million)");
        System.out.println("  - Treeification is extremely rare");
        System.out.println("  - Most buckets have 0-1 entries");
        System.out.println();
        System.out.println("Why not lower threshold (e.g., 4)?");
        System.out.println("  - Trees have higher overhead");
        System.out.println("  - Linked lists are faster for small N");
        System.out.println("  - Trees only help for N > 8");
    }

    /**
     * Calculates factorial for Poisson distribution.
     */
    private static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Demonstrates why untreeification threshold is 6.
     * Must be less than treeify threshold (hysteresis).
     */
    public static void whyUntreeifyThreshold6() {
        System.out.println("\n=== Why Untreeification Threshold is 6 ===");
        System.out.println();

        System.out.println("Untreeification converts Red-Black tree back to linked list.");
        System.out.println();

        System.out.println("Why 6 (not 8)?");
        System.out.println("  - Hysteresis prevents oscillation");
        System.out.println("  - If threshold were 8, entries would oscillate:");
        System.out.println("    - 8 entries → treeify");
        System.out.println("    - 7 entries → untreeify");
        System.out.println("    - 8 entries → treeify (again!)");
        System.out.println();
        System.out.println("  - With gap (8 vs 6):");
        System.out.println("    - 8 entries → treeify");
        System.out.println("    - 7 entries → stay tree");
        System.out.println("    - 6 entries → untreeify");
        System.out.println("    - 7 entries → stay list");
        System.out.println("    - 8 entries → treeify");
        System.out.println();
        System.out.println("  - Reduces expensive tree/list conversions");
        System.out.println("  - Stable performance under fluctuating loads");
    }

    /**
     * Demonstrates resizing strategy.
     * HashMap doubles capacity when load factor exceeded.
     */
    public static void resizingStrategy() {
        System.out.println("\n=== Resizing Strategy ===");
        System.out.println();

        System.out.println("When entries > capacity * loadFactor:");
        System.out.println("  1. Create new array with double capacity");
        System.out.println("  2. Rehash all entries");
        System.out.println("  3. Replace old array");
        System.out.println();

        System.out.println("Resizing example:");
        System.out.println("  Initial capacity: 16");
        System.out.println("  Load factor: 0.75");
        System.out.println("  Resize when entries > 12 (16 * 0.75)");
        System.out.println("  New capacity: 32");
        System.out.println("  Next resize at 24 entries (32 * 0.75)");
        System.out.println();

        System.out.println("Rehashing cost:");
        System.out.println("  - O(n) time complexity");
        System.out.println("  - All entries must be repositioned");
        System.out.println("  - Can cause latency spikes");
        System.out.println("  - Mitigated by proper initial capacity");
    }

    /**
     * Demonstrates memory vs performance trade-offs.
     */
    public static void memoryPerformanceTradeoffs() {
        System.out.println("\n=== Memory vs Performance Trade-offs ===");
        System.out.println();

        System.out.println("Lower Load Factor (e.g., 0.5):");
        System.out.println("  Pros:");
        System.out.println("    - Fewer collisions");
        System.out.println("    - Faster lookups");
        System.out.println("    - More predictable performance");
        System.out.println("  Cons:");
        System.out.println("    - 50% memory waste");
        System.out.println("    - More frequent resizing");
        System.out.println("    - Higher memory footprint");
        System.out.println();

        System.out.println("Higher Load Factor (e.g., 1.0):");
        System.out.println("  Pros:");
        System.out.println("    - Minimal memory waste");
        System.out.println("    - Less frequent resizing");
        System.out.println("    - Lower memory footprint");
        System.out.println("  Cons:");
        System.out.println("    - More collisions");
        System.out.println("    - Slower lookups");
        System.out.println("    - May trigger treeification");
        System.out.println();

        System.out.println("Recommendations:");
        System.out.println("  - Use default (0.75) for most cases");
        System.out.println("  - Lower (0.5) for read-heavy workloads");
        System.out.println("  - Higher (0.9) for memory-constrained systems");
        System.out.println("  - Set initial capacity to avoid resizing");
    }

    /**
     * Demonstrates historical changes across JDK versions.
     */
    public static void jdkVersionChanges() {
        System.out.println("\n=== Historical Changes Across JDK Versions ===");
        System.out.println();

        System.out.println("JDK 1.2 (1998):");
        System.out.println("  - HashMap introduced");
        System.out.println("  - Linked list for collisions");
        System.out.println("  - Load factor: 0.75");
        System.out.println("  - No treeification");
        System.out.println();

        System.out.println("JDK 1.4 (2002):");
        System.out.println("  - Performance improvements");
        System.out.println("  - Same basic structure");
        System.out.println();

        System.out.println("JDK 7 (2011):");
        System.out.println("  - Header entries (linked list)");
        System.out.println("  - Improved hash function");
        System.out.println("  - No structural changes");
        System.out.println();

        System.out.println("JDK 8 (2014):");
        System.out.println("  - TREEIFY_THRESHOLD = 8");
        System.out.println("  - UNTREEIFY_THRESHOLD = 6");
        System.out.println("  - MIN_TREEIFY_CAPACITY = 64");
        System.out.println("  - Treeification for long chains");
        System.out.println("  - O(log n) worst case instead of O(n)");
        System.out.println();

        System.out.println("JDK 9 (2017):");
        System.out.println("  - Factory methods (Map.of())");
        System.out.println("  - Same internal structure");
        System.out.println();

        System.out.println("JDK 11 (2018):");
        System.out.println("  - No structural changes");
        System.out.println("  - Performance optimizations");
        System.out.println();

        System.out.println("JDK 17 (2021):");
        System.out.println("  - Sealed classes (not for HashMap)");
        System.out.println("  - Same HashMap internals");
        System.out.println();

        System.out.println("JDK 21 (2023):");
        System.out.println("  - Virtual threads");
        System.out.println("  - HashMap unchanged");
        System.out.println("  - Still uses 0.75 load factor");
    }

    /**
     * Demonstrates practical tuning guidelines.
     */
    public static void practicalTuning() {
        System.out.println("\n=== Practical Tuning Guidelines ===");
        System.out.println();

        System.out.println("Setting Initial Capacity:");
        System.out.println("  Expected entries / loadFactor = initial capacity");
        System.out.println("  Example: 1000 entries → 1000 / 0.75 = 1333");
        System.out.println("  Round up to next power of 2: 2048");
        System.out.println("  new HashMap<>(2048)");
        System.out.println();

        System.out.println("When to Change Load Factor:");
        System.out.println("  - Read-heavy: Use 0.5 (fewer collisions)");
        System.out.println("  - Write-heavy: Use 0.9 (less resizing)");
        System.out.println("  - Memory-constrained: Use 0.9");
        System.out.println("  - Latency-sensitive: Use 0.5");
        System.out.println();

        System.out.println("Monitoring HashMap Performance:");
        System.out.println("  - Track collision rate");
        System.out.println("  - Monitor resize events");
        System.out.println("  - Profile lookup times");
        System.out.println("  - Check memory usage");
        System.out.println();

        System.out.println("Common Mistakes:");
        System.out.println("  - Not setting initial capacity");
        System.out.println("  - Using default for known sizes");
        System.out.println("  - Ignoring resize cost");
        System.out.println("  - Not considering load factor");
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        System.out.println("HashMap Load Factor Deep Dive");
        System.out.println("=============================");

        whyLoadFactor075();
        whyPowerOf2();
        whyTreeifyThreshold8();
        whyUntreeifyThreshold6();
        resizingStrategy();
        memoryPerformanceTradeoffs();
        jdkVersionChanges();
        practicalTuning();

        System.out.println("\n=== Key Takeaways ===");
        System.out.println("1. Load factor 0.75 is mathematically optimal");
        System.out.println("2. Power of 2 enables fast bitwise modulo");
        System.out.println("3. Treeify threshold 8 based on Poisson distribution");
        System.out.println("4. Untreeify threshold 6 prevents oscillation");
        System.out.println("5. Resize doubles capacity, rehashes all entries");
        System.out.println("6. Lower LF = faster but more memory");
        System.out.println("7. Higher LF = slower but less memory");
        System.out.println("8. Set initial capacity to avoid resizing");
    }
}
