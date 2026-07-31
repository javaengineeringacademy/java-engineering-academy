package academy.javaengineering.oop.abstraction;

/**
 * Car - Concrete implementation of abstract Vehicle class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Car extends Vehicle {

    private final int doors;

    public Car(String make, String model, int year) {
        this(make, model, year, 4);
    }

    public Car(String make, String model, int year, int doors) {
        super(make, model, year);
        this.doors = doors;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            setRunning(true);
            System.out.println("  " + this + " engine started with a smooth purr");
        } else {
            System.out.println("  " + this + " is already running");
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            setRunning(false);
            System.out.println("  " + this + " engine stopped");
        } else {
            System.out.println("  " + this + " is already off");
        }
    }

    @Override
    public double getFuelEfficiency() {
        return 30.5; // miles per gallon
    }

    public int getDoors() {
        return doors;
    }
}