package academy.javaengineering.oop.memory;

public class WhyNotMemory {

    static class HeavyObject {
        int[] data = new int[1000];
    }

    static class LightObject {
        int value;
    }

    public static void main(String[] args) {
        System.out.println("=== Why Not OOP Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object Overhead
        System.out.println("--- Object Overhead ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        HeavyObject heavy = new HeavyObject();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("HeavyObject: " + (after - before) + " bytes");

        // 2. Memory vs Performance
        System.out.println("\n--- Memory vs Performance ---");
        System.out.println("OOP: more objects, more memory");
        System.out.println("Procedural: less memory, faster");
        System.out.println("Trade-off depends on use case");

        // 3. When Memory Matters
        System.out.println("\n--- When Memory Matters ---");
        System.out.println("1. Embedded systems");
        System.out.println("2. Real-time applications");
        System.out.println("3. High-performance computing");
        System.out.println("4. Mobile apps");
    }
}
