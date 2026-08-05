public class Car extends Vehicle {

    private final int doors;

    public Car(String make, String model, int year, int doors) {
        super(make, model, year);
        this.doors = doors;
    }

    @Override
    public double fuelEfficiency() {
        return 30.0;
    }

    @Override
    public String start() {
        return "Car engine starting with key ignition";
    }

    public int getDoors() { return doors; }
}