package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Method Overriding ===\n");

        // WHY: Overriding enables runtime polymorphism - same interface, different behavior
        // INTERNAL: invokevirtual uses vtable to find method at runtime
        // ENGINEERING: @Override annotation catches errors at compile time

        Vehicle car = new Car("Toyota", "Camry");
        Vehicle truck = new Truck("Ford", "F-150", 1000);

        car.start();
        car.describe();
        truck.start();
        truck.describe();

        // TRADE-OFF: Overriding vs hiding (static methods)
        // Overriding: polymorphic, runtime dispatch
        // Hiding: compile-time, based on declared type
        System.out.println("Car maxSpeed: " + car.maxSpeed());
        System.out.println("Truck maxSpeed: " + truck.maxSpeed());
    }
}

class Vehicle {
    protected String make;
    protected String model;

    Vehicle(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public void start() {
        System.out.println(make + " " + model + " starting...");
    }

    public void describe() {
        System.out.println("Vehicle: " + make + " " + model);
    }

    public int maxSpeed() { return 120; }
}

class Car extends Vehicle {
    Car(String make, String model) { super(make, model); }

    @Override
    public void start() {
        System.out.println(make + " " + model + " engine purring...");
    }

    @Override
    public int maxSpeed() { return 180; }
}

class Truck extends Vehicle {
    private final int payload;

    Truck(String make, String model, int payload) {
        super(make, model);
        this.payload = payload;
    }

    @Override
    public void describe() {
        super.describe();
        System.out.println("  Payload: " + payload + " lbs");
    }

    @Override
    public int maxSpeed() { return 100; }
}
