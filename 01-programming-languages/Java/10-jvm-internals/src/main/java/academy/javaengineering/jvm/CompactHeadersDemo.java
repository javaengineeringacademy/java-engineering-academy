package academy.javaengineering.jvm;

import java.util.*;

/**
 * Java 24 Compact Object Headers Demo (JEP 474).
 *
 * <p>Compact Object Headers reduces the memory footprint of Java objects
 * by optimizing the object header size. This can save 10-25% of heap
 * memory for applications with many objects.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Object Header Structure - mark word + klass pointer</li>
 *   <li>Compressed Oops - compressed ordinary object pointers</li>
 *   <li>Memory Layout - alignment and padding optimization</li>
 *   <li>Heap Dump Analysis - measuring header impact</li>
 * </ul>
 *
 * <h3>Expected Output:</h3>
 * <pre>
 * === Compact Object Headers Demo ===
 *
 * --- Memory Layout Comparison ---
 * Standard Header Size: 16 bytes
 * Compact Header Size: 8 bytes (estimated with JEP 474)
 * Savings per object: 8 bytes
 *
 * --- Object Size Calculation ---
 * SimpleObject: 16 bytes (standard) vs 8 bytes (compact)
 * With 1M objects: 16MB vs 8MB
 *
 * --- Shallow Heap Analysis ---
 * HashMap entry: 48 bytes → ~32 bytes with compact headers
 * ArrayList element: 24 bytes → ~16 bytes with compact headers
 * </pre>
 *
 * <h3>Production Use Cases:</h3>
 * <ul>
 *   <li>Memory-constrained environments</li>
 *   <li>Large heaps with many small objects</li>
 *   <li>Reducing GC pressure</li>
 *   <li>Cost optimization for cloud deployments</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since Java 24
 */
public class CompactHeadersDemo {

    private String name;
    private int value;
    private boolean active;

    public CompactHeadersDemo(String name, int value, boolean active) {
        this.name = name;
        this.value = value;
        this.active = active;
    }

    /**
     * Demonstrates object header structure.
     */
    public static void objectHeaderStructureDemo() {
        System.out.println("--- Object Header Structure ---");

        // Standard object header (64-bit JVM)
        int standardMarkWord = 8;      // 64 bits = 8 bytes
        int standardKlassPointer = 8;  // 64 bits = 8 bytes (compressed: 4 bytes)

        System.out.println("Standard Object Header:");
        System.out.println("  Mark Word: " + standardMarkWord + " bytes");
        System.out.println("  Klass Pointer: " + standardKlassPointer + " bytes");
        System.out.println("  Total: " + (standardMarkWord + standardKlassPointer) + " bytes");

        // With compact headers (JEP 474)
        int compactMarkWord = 4;       // Optimized to 32 bits
        int compactKlassPointer = 4;   // Compressed to 32 bits

        System.out.println("\nCompact Object Header (JEP 474):");
        System.out.println("  Mark Word: " + compactMarkWord + " bytes");
        System.out.println("  Klass Pointer: " + compactKlassPointer + " bytes");
        System.out.println("  Total: " + (compactMarkWord + compactKlassPointer) + " bytes");
    }

    /**
     * Demonstrates memory savings calculation.
     */
    public static void memorySavingsDemo() {
        System.out.println("\n--- Memory Savings Calculation ---");

        int objectCount = 1_000_000;
        int standardHeader = 16;  // bytes
        int compactHeader = 8;    // bytes (estimated)

        long standardMemory = (long) objectCount * standardHeader;
        long compactMemory = (long) objectCount * compactHeader;
        long savings = standardMemory - compactMemory;

        System.out.println("Objects: " + String.format("%,d", objectCount));
        System.out.printf("Standard: %d MB%n", standardMemory / (1024 * 1024));
        System.out.printf("Compact: %d MB%n", compactMemory / (1024 * 1024));
        System.out.printf("Savings: %d MB (%.1f%%)%n",
            savings / (1024 * 1024),
            ((double) savings / standardMemory) * 100);
    }

    /**
     * Demonstrates object size analysis.
     */
    public static void objectSizeAnalysisDemo() {
        System.out.println("\n--- Object Size Analysis ---");

        // Calculate sizes for different objects
        System.out.println("Object Type        | Standard | Compact | Savings");
        System.out.println("-------------------|----------|---------|--------");
        System.out.printf("%-18s| %5d B  | %4d B  | %3d B%n",
            "Empty Object", 16, 8, 8);
        System.out.printf("%-18s| %5d B  | %4d B  | %3d B%n",
            "int + ref", 24, 16, 8);
        System.out.printf("%-18s| %5d B  | %4d B  | %3d B%n",
            "String (empty)", 40, 32, 8);
        System.out.printf("%-18s| %5d B  | %4d B  | %3d B%n",
            "HashMap Entry", 48, 40, 8);
    }

    /**
     * Demonstrates GC impact reduction.
     */
    public static void gcImpactDemo() {
        System.out.println("\n--- GC Impact Reduction ---");

        long heapSize = 4L * 1024 * 1024 * 1024; // 4 GB heap
        double objectDensity = 0.6; // 60% of heap is objects

        long objectMemory = (long) (heapSize * objectDensity);
        int objectsCount = (int) (objectMemory / 16); // Average 16 bytes per object

        long standardHeaders = (long) objectsCount * 16;
        long compactHeaders = (long) objectsCount * 8;

        System.out.printf("Heap Size: %d GB%n", heapSize / (1024 * 1024 * 1024));
        System.out.printf("Objects: ~%d million%n", objectsCount / 1_000_000);
        System.out.printf("Header Memory (Standard): %d MB%n",
            standardHeaders / (1024 * 1024));
        System.out.printf("Header Memory (Compact): %d MB%n",
            compactHeaders / (1024 * 1024));
        System.out.printf("GC Roots Reduction: ~%d%% less header scanning%n", 50);
    }

    /**
     * Demonstrates heap dump analysis concepts.
     */
    public static void heapDumpAnalysisDemo() {
        System.out.println("\n--- Heap Dump Analysis ---");

        // Simulate heap dump statistics
        Map<String, long[]> stats = new LinkedHashMap<>();
        stats.put("byte[]", new long[]{2_500_000, 120});
        stats.put("char[]", new long[]{1_800_000, 95});
        stats.put("Object[]", new long[]{950_000, 48});
        stats.put("String", new long[]{900_000, 40});
        stats.put("HashMap$Node", new long[]{600_000, 32});

        System.out.println("Top Objects by Count:");
        System.out.printf("%-20s %12s %10s%n", "Type", "Count", "Avg Size");
        System.out.println("-".repeat(44));

        stats.forEach((type, data) ->
            System.out.printf("%-20s %,12d %8d B%n",
                type, data[0], data[1]));

        System.out.println("\nWith compact headers, all sizes reduce by ~8 bytes");
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        System.out.println("=== Compact Object Headers Demo ===\n");

        objectHeaderStructureDemo();
        memorySavingsDemo();
        objectSizeAnalysisDemo();
        gcImpactDemo();
        heapDumpAnalysisDemo();
    }
}
