package academy.javaengineering.oop.interfaces;

/**
 * Bird - Concrete implementation of Flyable interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Bird implements Flyable {

    private final String name;

    public Bird(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.println("  " + name + " is flying with wings!");
    }

    @Override
    public int getMaxAltitude() {
        return 1000;
    }

    @Override
    public String toString() {
        return "Bird{name='" + name + "'}";
    }
}