package academy.javaengineering.oop.compositionaggregation;

/**
 * Engine - Part of Car composition relationship.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Engine {

    private final String type;
    private final int horsepower;
    private boolean running;

    public Engine(String type, int horsepower) {
        this.type = type;
        this.horsepower = horsepower;
        this.running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            System.out.println("  Engine started: " + type + " " + horsepower + "HP");
        }
    }

    public void stop() {
        if (running) {
            running = false;
            System.out.println("  Engine stopped");
        }
    }

    public String getSpecification() {
        return type + " " + horsepower + "HP";
    }

    public boolean isRunning() { return running; }
    public String getType() { return type; }
    public int getHorsepower() { return horsepower; }
}