package academy.javaengineering.fundamentals.keywords;

/**
 * Demonstrates keyword memory usage patterns.
 */
public class KeywordsMemory {

    // Static field: stored in Method Area
    static int staticCounter = 0;
    
    // Final field: immutable after construction
    static final String CLASS_NAME = "KeywordsMemory";

    // Instance field: stored in Heap (per instance)
    int instanceValue;

    public KeywordsMemory(int value) {
        this.instanceValue = value;
    }

    public static void main(String[] args) {
        System.out.println("=== Keywords Memory Demo ===\n");

        // 1. Static vs instance memory
        System.out.println("--- Static vs Instance Memory ---");
        staticCounter = 10;
        KeywordsMemory obj1 = new KeywordsMemory(100);
        KeywordsMemory obj2 = new KeywordsMemory(200);
        System.out.println("Static field: " + staticCounter + " (one copy)");
        System.out.println("obj1.instanceValue: " + obj1.instanceValue);
        System.out.println("obj2.instanceValue: " + obj2.instanceValue);

        // 2. Final fields
        System.out.println("\n--- Final Fields ---");
        System.out.println("CLASS_NAME: " + CLASS_NAME + " (compile-time constant)");

        // 3. Enum memory
        System.out.println("\n--- Enum Memory ---");
        Status status = Status.ACTIVE;
        System.out.println("Status.ACTIVE: " + status);
        System.out.println("Enum constants: stored in Method Area");

        // 4. Record memory
        System.out.println("\n--- Record Memory ---");
        Point p = new Point(10, 20);
        System.out.println("Point record: " + p);
        System.out.println("Record: final class with final fields");

        System.out.println("\n=== Memory Demo Complete ===");
    }

    enum Status { ACTIVE, INACTIVE, PENDING }
    record Point(int x, int y) {}
}
