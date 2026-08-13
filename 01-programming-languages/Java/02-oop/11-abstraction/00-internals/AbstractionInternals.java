package academy.javaengineering.oop.internals;

public class AbstractionInternals {

    abstract static class Vehicle {
        String brand;

        Vehicle(String brand) {
            this.brand = brand;
        }

        abstract void start();
        abstract void stop();

        void display() {
            System.out.println("Brand: " + brand);
        }
    }

    static class Car extends Vehicle {
        Car(String brand) { super(brand); }

        @Override
        void start() {
            System.out.println("Car starting with key");
        }

        @Override
        void stop() {
            System.out.println("Car stopping");
        }
    }

    static class ElectricCar extends Vehicle {
        ElectricCar(String brand) { super(brand); }

        @Override
        void start() {
            System.out.println("Electric car starting silently");
        }

        @Override
        void stop() {
            System.out.println("Electric car stopping");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Abstraction Internals ===\n");

        // 1. Abstract Class
        System.out.println("--- Abstract Class ---");
        // Vehicle v = new Vehicle("Test"); // COMPILE ERROR
        Vehicle car = new Car("Toyota");
        car.start();
        car.display();

        // 2. Abstract Methods
        System.out.println("\n--- Abstract Methods ---");
        System.out.println("No body - must be implemented");
        System.out.println("Forces subclasses to provide");
        System.out.println("Enables polymorphism");

        // 3. Abstraction vs Encapsulation
        System.out.println("\n--- Abstraction vs Encapsulation ---");
        System.out.println("Abstraction: what to do");
        System.out.println("Encapsulation: how to do");
        System.out.println("Abstraction hides complexity");
    }
}
