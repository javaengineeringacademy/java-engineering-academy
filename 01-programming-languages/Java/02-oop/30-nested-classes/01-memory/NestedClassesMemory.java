package academy.javaengineering.oop.memory;

public class NestedClassesMemory {

    static class Outer {
        String outerField = "Outer";
        class Inner {}
        static class StaticNested {}
    }

    public static void main(String[] args) {
        System.out.println("=== Nested Classes Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Inner vs Static Nested
        System.out.println("--- Inner vs Static Nested ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Inner: " + (after - before) + " bytes");
        System.out.println("Contains outer reference");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Outer.StaticNested nested = new Outer.StaticNested();
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Static nested: " + (after - before) + " bytes");
        System.out.println("No outer reference");

        // 2. Memory Recommendation
        System.out.println("\n--- Memory Recommendation ---");
        System.out.println("Use static nested when possible");
        System.out.println("Saves 8 bytes per instance");
        System.out.println("Better for garbage collection");
    }
}
