package academy.javaengineering.oop.composition;

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
            System.out.println(type + " engine started");
        }
    }

    public void stop() {
        if (running) {
            running = false;
            System.out.println(type + " engine stopped");
        }
    }

    public boolean isRunning() { return running; }
    public String getType() { return type; }
    public int getHorsepower() { return horsepower; }
}
