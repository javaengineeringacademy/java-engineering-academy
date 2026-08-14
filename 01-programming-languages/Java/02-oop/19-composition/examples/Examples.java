package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Composition Patterns ===\n");

        // WHY: Composition > Inheritance for code reuse. More flexible, less coupled.
        // INTERNAL: Objects contain references to other objects, delegate behavior
        // ENGINEERING: Prefer "has-a" over "is-a" for most relationships

        Engine engine = new Engine("V6", 300);
        Transmission transmission = new Transmission("Automatic", 8);
        Car car = new Car("Toyota", "Camry", engine, transmission);

        car.start();
        car加速();
        car.stop();

        // TRADE-OFF: Composition requires more code but is more flexible
        // Inheritance: simpler, but rigid (IS-A relationship)
        // Composition: flexible, can change behavior at runtime
    }
}

class Engine {
    private final String type;
    private final int horsepower;

    Engine(String type, int horsepower) {
        this.type = type;
        this.horsepower = horsepower;
    }

    public void start() { System.out.println(type + " " + horsepower + "HP engine starting..."); }
    public void stop() { System.out.println("Engine stopped."); }
    public int getHorsepower() { return horsepower; }
}

class Transmission {
    private final String type;
    private final int gears;

    Transmission(String type, int gears) {
        this.type = type;
        this.gears = gears;
    }

    public void shiftUp() { System.out.println("Shifting up (max " + gears + " gears)"); }
}

class Car {
    private final String make;
    private final String model;
    private final Engine engine;
    private final Transmission transmission;

    Car(String make, String model, Engine engine, Transmission transmission) {
        this.make = make;
        this.model = model;
        this.engine = engine;
        this.transmission = transmission;
    }

    public void start() {
        System.out.println(make + " " + model + " starting...");
        engine.start();
    }

    public void 加速() {
        System.out.println("Accelerating with " + engine.getHorsepower() + "HP");
        transmission.shiftUp();
    }

    public void stop() {
        System.out.println("Stopping " + make + " " + model);
        engine.stop();
    }
}
