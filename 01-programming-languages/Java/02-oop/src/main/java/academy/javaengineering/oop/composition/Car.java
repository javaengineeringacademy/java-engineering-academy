package academy.javaengineering.oop.composition;

public class Car {

    private final String make;
    private final String model;
    private final Engine engine;
    private final int year;

    public Car(String make, String model, int year, Engine engine) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.engine = engine;
    }

    public void start() {
        if (!engine.isRunning()) {
            engine.start();
            System.out.println(make + " " + model + " is ready");
        }
    }

    public void stop() {
        if (engine.isRunning()) {
            engine.stop();
            System.out.println(make + " " + model + " has stopped");
        }
    }

    public String getInfo() {
        return year + " " + make + " " + model + " [" + engine.getType() + "]";
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public Engine getEngine() { return engine; }
}
