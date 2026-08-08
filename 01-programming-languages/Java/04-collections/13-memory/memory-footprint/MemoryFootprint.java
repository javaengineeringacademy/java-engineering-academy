import java.util.*;
import java.util.stream.*;

/**
 * Memory Footprint of Collections
 * Memory measurement, JOL usage, collection memory overhead.
 */
public class MemoryFootprint {

    public static void main(String[] args) {
        System.out.println("=== Memory Footprint of Collections ===\n");

        arrayListMemory();
        linkedListMemory();
        hashmapMemory();
        memoryMeasurementTools();
        memoryOptimization();

        System.out.println("\n=== Complete ===");
    }

    // --- ArrayList memory ---
    static void arrayListMemory() {
        System.out.println("--- ArrayList Memory ---");

        // ArrayList structure:
        // - Object header: 12 bytes
        // - int size: 4 bytes
        // - Object[] elementData reference: 4 bytes
        // - Padding to 8-byte boundary

        // Internal Object[] array:
        // - Object header: 12 bytes
        // - int length: 4 bytes
        // - References: 4 bytes each

        System.out.println("ArrayList<Integer>(1000) memory breakdown:");
        System.out.println("  ArrayList object: 16 bytes (header + fields)");
        System.out.println("  Internal Object[] array: 16 + (capacity * 4) bytes");
        System.out.println("  Integer objects: 1000 * 16 bytes each");
        System.out.println("  Total: ~" + (16 + 16 + 1000 * 4 + 1000 * 16) + " bytes");

        // vs ArrayList<Integer> with initial capacity
        System.out.println("\nWith initial capacity 1000:");
        System.out.println("  No resizing overhead");
        System.out.println("  Same memory footprint");

        // vs ArrayList<Integer> with growth
        System.out.println("\nGrowth overhead:");
        System.out.println("  Each resize creates new array");
        System.out.println("  Old array becomes garbage");
        System.out.println("  GC pressure increases");

        System.out.println();
    }

    // --- LinkedList memory ---
    static void linkedListMemory() {
        System.out.println("--- LinkedList Memory ---");

        // LinkedList structure:
        // - Node objects for each element
        // - Each Node has: header + prev + next + item
        // - Total per node: ~32 bytes

        int size = 1000;
        System.out.println("LinkedList<Integer>(" + size + ") memory breakdown:");
        System.out.println("  LinkedList object: 24 bytes (header + first/last)");
        System.out.println("  Per node: 32 bytes (header + prev + next + item)");
        System.out.println("  Node memory: " + (size * 32) + " bytes");
        System.out.println("  Integer objects: " + (size * 16) + " bytes");
        System.out.println("  Total: ~" + (24 + size * 32 + size * 16) + " bytes");

        System.out.println("\nComparison with ArrayList:");
        int arrayListTotal = 16 + 16 + size * 4 + size * 16;
        int linkedListTotal = 24 + size * 32 + size * 16;
        System.out.println("  ArrayList: ~" + arrayListTotal + " bytes");
        System.out.println("  LinkedList: ~" + linkedListTotal + " bytes");
        System.out.println("  LinkedList uses " +
            String.format("%.1fx", (double) linkedListTotal / arrayListTotal) +
            " more memory");

        System.out.println();
    }

    // --- HashMap memory ---
    static void hashmapMemory() {
        System.out.println("--- HashMap Memory ---");

        // HashMap structure:
        // - Object header
        // - Node[] table reference
        // - int size, threshold
        // - float loadFactor
        // - int modCount

        int size = 1000;
        System.out.println("HashMap<String, Integer>(" + size + ") memory breakdown:");
        System.out.println("  HashMap object: ~48 bytes");
        System.out.println("  Node[] table: 16 + (capacity * 4) bytes");
        System.out.println("  Per entry (Node): 32 bytes");
        System.out.println("  Key objects: String ~48 bytes each");
        System.out.println("  Value objects: Integer ~16 bytes each");

        // Calculate with typical load factor 0.75
        int capacity = 2048; // Next power of 2 after 1000/0.75
        int nodeMemory = size * 32;
        int keyMemory = size * 48;
        int valueMemory = size * 16;
        int tableMemory = 16 + capacity * 4;
        int total = 48 + tableMemory + nodeMemory + keyMemory + valueMemory;

        System.out.println("\nWith capacity " + capacity + " (load factor 0.75):");
        System.out.println("  HashMap object: 48 bytes");
        System.out.println("  Node[] table: " + tableMemory + " bytes");
        System.out.println("  Nodes: " + nodeMemory + " bytes");
        System.out.println("  Keys: " + keyMemory + " bytes");
        System.out.println("  Values: " + valueMemory + " bytes");
        System.out.println("  Total: ~" + total + " bytes");
        System.out.println("  Per entry: ~" + (total / size) + " bytes");

        System.out.println();
    }

    // --- Memory measurement tools ---
    static void memoryMeasurementTools() {
        System.out.println("--- Memory Measurement Tools ---");

        // 1. Runtime.freeMemory()
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.freeMemory();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("item" + i);
        }
        long after = runtime.freeMemory();
        System.out.println("Runtime.freeMemory() diff: " + (before - after) + " bytes");

        // 2. JMH (Java Microbenchmark Harness)
        System.out.println("\nJMH annotations:");
        System.out.println("  @BenchmarkMode(Mode.AverageTime)");
        System.out.println("  @OutputTimeUnit(TimeUnit.NANOSECONDS)");
        System.out.println("  @Warmup(iterations = 5)");
        System.out.println("  @Measurement(iterations = 10)");

        // 3. JOL (Java Object Layout)
        System.out.println("\nJOL (Java Object Layout) tool:");
        System.out.println("  - Analyzes object memory layout");
        System.out.println("  - Shows field offsets and sizes");
        System.out.println("  - Detects padding and alignment");
        System.out.println("  - Usage: java -jar jol-core.jar estimate <class>");

        // 4. VisualVM / JProfiler
        System.out.println("\nProfiling tools:");
        System.out.println("  - VisualVM: Heap dump analysis");
        System.out.println("  - JProfiler: Memory profiling");
        System.out.println("  - YourKit: Object allocation tracking");
        System.out.println("  - Eclipse MAT: Heap dump analysis");

        System.out.println();
    }

    // --- Memory optimization ---
    static void memoryOptimization() {
        System.out.println("--- Memory Optimization ---");

        // 1. Use primitive collections
        System.out.println("1. Use primitive collections:");
        System.out.println("   - Eclipse Collections: IntArrayList, LongArrayList");
        System.out.println("   - Trove: TLongArrayList, TIntArrayList");
        System.out.println("   - HPPC: LongArrayList, IntArrayList");

        // 2. Use compact data structures
        System.out.println("\n2. Compact data structures:");
        System.out.println("   - ArrayDeque over LinkedList for queues");
        System.out.println("   - ArrayList over LinkedList for lists");
        System.out.println("   - EnumMap over HashMap for enum keys");

        // 3. Pre-size collections
        System.out.println("\n3. Pre-size collections:");
        System.out.println("   - new ArrayList<>(expectedSize)");
        System.out.println("   - new HashMap<>(expectedSize / 0.75 + 1)");
        System.out.println("   - Avoids resizing and wasted space");

        // 4. Use appropriate collection
        System.out.println("\n4. Choose right collection:");
        System.out.println("   - Set: HashSet for O(1), TreeSet for sorted");
        System.out.println("   - Map: HashMap for O(1), TreeMap for sorted");
        System.out.println("   - List: ArrayList for random access");

        // Example
        System.out.println("\nExample optimization:");
        System.out.println("  // Bad");
        System.out.println("  List<Integer> list = new ArrayList<>();");
        System.out.println("  for (int i = 0; i < 10000; i++) list.add(i);");
        System.out.println("  // Good");
        System.out.println("  List<Integer> list = new ArrayList<>(10000);");
        System.out.println("  for (int i = 0; i < 10000; i++) list.add(i);");

        System.out.println("\n=== Complete ===");
    }
}
