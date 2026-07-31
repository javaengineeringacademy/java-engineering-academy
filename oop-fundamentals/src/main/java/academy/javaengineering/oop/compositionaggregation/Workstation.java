package academy.javaengineering.oop.compositionaggregation;

/**
 * Workstation - Demonstrates composition with Computer.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Workstation {

    private final String name;
    private final Computer computer;

    public Workstation(String name, Computer computer) {
        this.name = name;
        this.computer = computer;
    }

    public void displayConfiguration() {
        System.out.println("Workstation: " + name);
        System.out.println("  " + computer.getSpecification());
    }

    public String getName() { return name; }
    public Computer getComputer() { return computer; }
}