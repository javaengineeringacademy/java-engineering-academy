package academy.javaengineering.oop.memory;

public class InnerClassesMemory {

    static class Outer {
        String outerField = "Outer";
        class Inner {
            void display() { System.out.println(outerField); }
        }
        static class StaticInner {}
    }

    public static void main(String[] args) {
        System.out.println("=== Inner Classes Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Inner Class Memory
        System.out.println("--- Inner Class Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Inner: " + (after - before) + " bytes");
        System.out.println("Contains reference to outer");

        // 2. Static Inner Memory
        System.out.println("\n--- Static Inner Memory ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Outer.StaticInner staticInner = new Outer.StaticInner();
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Static inner: " + (after - before) + " bytes");
        System.out.println("No outer reference");

        // 3. Anonymous Class Memory
        System.out.println("\n--- Anonymous Class Memory ---");
        System.out.println("Each anonymous class: separate .class file");
        System.out.println("Cost: ~16 bytes per instance");
        System.out.println("Use lambda for better performance");
    }
}
