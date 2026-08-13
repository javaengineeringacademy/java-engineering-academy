package academy.javaengineering.oop.internals;

public class CompositionInternals {

    static class Engine {
        void start() {
            System.out.println("Engine started");
        }

        void stop() {
            System.out.println("Engine stopped");
        }
    }

    static class Car {
        private Engine engine; // Composition

        Car() {
            this.engine = new Engine(); // Car owns Engine
        }

        void start() {
            System.out.println("Car starting...");
            engine.start();
        }

        void stop() {
            System.out.println("Car stopping...");
            engine.stop();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Composition Internals ===\n");

        // 1. HAS-A Relationship
        System.out.println("--- HAS-A Relationship ---");
        Car car = new Car();
        car.start();
        car.stop();
        System.out.println("Car HAS-A Engine");

        // 2. Composition vs Inheritance
        System.out.println("\n--- Composition vs Inheritance ---");
        System.out.println("Composition: HAS-A (car has engine)");
        System.out.println("Inheritance: IS-A (dog is animal)");
        System.out.println("Prefer composition over inheritance");

        // 3. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("1. Loose coupling");
        System.out.println("2. Easier to test");
        System.out.println("3. More flexible");
        System.out.println("4. Better encapsulation");
    }
}
