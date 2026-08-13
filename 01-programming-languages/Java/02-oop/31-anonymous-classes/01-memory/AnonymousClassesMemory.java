package academy.javaengineering.oop.memory;

public class AnonymousClassesMemory {

    interface Greeting { void greet(String name); }

    public static void main(String[] args) {
        System.out.println("=== Anonymous Classes Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Anonymous Class Size
        System.out.println("--- Anonymous Class Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Greeting greeting = new Greeting() {
            @Override
            public void greet(String name) { System.out.println("Hello"); }
        };
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Anonymous class: " + (after - before) + " bytes");

        // 2. Class File Generation
        System.out.println("\n--- Class File Generation ---");
        System.out.println("Each anonymous class: separate .class file");
        System.out.println("Outer$1.class, Outer$2.class");
        System.out.println("Increases jar size");

        // 3. Lambda vs Anonymous
        System.out.println("\n--- Lambda vs Anonymous ---");
        System.out.println("Lambda: shared class via invokedynamic");
        System.out.println("Anonymous: separate class per instance");
        System.out.println("Lambda: better memory performance");
    }
}
