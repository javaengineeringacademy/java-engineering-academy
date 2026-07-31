package academy.javaengineering.oop.abstraction;

/**
 * Vehicle - Abstract class demonstrating abstraction with partial implementation.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class Vehicle {

    protected final String make;
    protected final String model;
    protected final int year;
    private boolean running;

    protected Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.running = false;
    }

    // Abstract methods - MUST be implemented by subclasses
    public abstract void start();
    public abstract void stop();
    public abstract double getFuelEfficiency();

    // Concrete methods - shared implementation
    public boolean isRunning() {
        return running;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    public void displayInfo() {
        System.out.printf("  %d %s %s (Running: %s)%n", year, make, model, running);
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    @Override
    public String toString() {
        return year + " " + make + " " + model;
    }
}