public class Motorcycle extends Vehicle {

    private final boolean hasSidecar;

    public Motorcycle(String make, String model, int year, boolean hasSidecar) {
        super(make, model, year);
        this.hasSidecar = hasSidecar;
    }

    @Override
    public double fuelEfficiency() {
        return 50.0;
    }

    @Override
    public String start() {
        return "Motorcycle engine starting with electric start";
    }

    public boolean hasSidecar() { return hasSidecar; }
}