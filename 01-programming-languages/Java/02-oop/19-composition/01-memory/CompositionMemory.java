package academy.javaengineering.oop.memory;

public class CompositionMemory {

    static class Engine {
        void start() { System.out.println("Started"); }
    }

    static class Car {
        Engine engine;
        Car() { this.engine = new Engine(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Composition Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object Graph
        System.out.println("--- Object Graph ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Car car = new Car();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Car object: " + (after - before) + " bytes");
        System.out.println("Contains reference to Engine");

        // 2. Reference vs Object
        System.out.println("\n--- Reference vs Object ---");
        System.out.println("Car: 8 bytes for engine reference");
        System.out.println("Engine: separate object in heap");
        System.out.println("Total: Car + Engine objects");

        // 3. GC Impact
        System.out.println("\n--- GC Impact ---");
        System.out.println("Composition: separate GC roots");
        System.out.println("Engine collected independently");
        System.out.println("Better memory management");
    }
}
