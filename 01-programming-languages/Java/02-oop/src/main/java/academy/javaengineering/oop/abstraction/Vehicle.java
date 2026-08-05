package academy.javaengineering.oop.abstraction;

public abstract class Vehicle {

    protected String make;
    protected String model;
    protected int year;

    public Vehicle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    public abstract double fuelEfficiency();
    public abstract String start();

    public String getInfo() {
        return year + " " + make + " " + model;
    }

    public boolean isNewerThan(Vehicle other) {
        return this.year > other.year;
    }

    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    @Override
    public String toString() {
        return "%s{make='%s', model='%s', year=%d}".formatted(
                getClass().getSimpleName(), make, model, year);
    }
}
