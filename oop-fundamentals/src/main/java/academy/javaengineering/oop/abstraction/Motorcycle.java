package academy.javaengineering.oop.abstraction;

/**
 * Motorcycle - Concrete implementation of abstract Vehicle class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Motorcycle extends Vehicle {

    private final boolean hasSidecar;

    public Motorcycle(String make, String model, int year) {
        this(make, model, year, false);
    }

    public Motorcycle(String make, String model, int year, boolean hasSidecar) {
        super(make, model, year);
        this.hasSidecar = hasSidecar;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            setRunning(true);
            System.out.println("  " + this + " roars to life!");
        } else {
            System.out.println("  " + this + " is already running");
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            setRunning(false);
            System.out.println("  " + this + " engine silenced");
        } else {
            System.out.println("  " + this + " is already off");
        }
    }

    @Override
    public double getFuelEfficiency() {
        return 45.0;
    }

    public boolean hasSidecar() {
        return hasSidecar;
    }
}