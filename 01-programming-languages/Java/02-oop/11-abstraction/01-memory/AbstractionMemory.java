package academy.javaengineering.oop.memory;

public class AbstractionMemory {

    static abstract class Vehicle {
        String brand;
        abstract void start();
    }

    static class Car extends Vehicle {
        Car(String brand) { this.brand = brand; }
        void start() { System.out.println("Starting"); }
    }

    public static void main(String[] args) {
        System.out.println("=== Abstraction Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Abstract Class Memory
        System.out.println("--- Abstract Class ---");
        System.out.println("Cannot instantiate abstract class");
        System.out.println("No object memory for abstract class");
        System.out.println("Only concrete subclasses use heap");

        // 2. Abstract Method Memory
        System.out.println("\n--- Abstract Method ---");
        System.out.println("No bytecode for abstract method");
        System.out.println("Stored in method table only");
        System.out.println("Cost: ~8 bytes per class (vtable entry)");

        // 3. Concrete Subclass Size
        System.out.println("\n--- Concrete Subclass ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Car car = new Car("Tesla");
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Car object: " + (after - before) + " bytes");
        System.out.println("Contains: Vehicle fields + Car fields");
    }
}
