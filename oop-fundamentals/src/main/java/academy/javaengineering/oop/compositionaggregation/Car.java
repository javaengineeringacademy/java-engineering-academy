package academy.javaengineering.oop.compositionaggregation;

/**
 * Car - Demonstrates composition (owns its Engine).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Car {

    private final String make;
    private final String model;
    private final Engine engine; // Created internally = Composition

    public Car(String make, String model, Engine engine) {
        this.make = make;
        this.model = model;
        this.engine = engine; // Engine lifecycle tied to Car
    }

    public void start() {
        System.out.println("Starting " + make + " " + model);
        engine.start();
    }

    public void displayInfo() {
        System.out.println("  Car: " + make + " " + model);
        System.out.println("  Engine: " + engine.getSpecification());
        System.out.println("  Running: " + engine.isRunning());
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public Engine getEngine() { return engine; }
}