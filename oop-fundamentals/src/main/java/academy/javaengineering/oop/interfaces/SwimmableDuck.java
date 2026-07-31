package academy.javaengineering.oop.interfaces;

/**
 * SwimmableDuck - Demonstrates multiple interface implementation.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SwimmableDuck implements Flyable, Swimmable, Quackable {

    private final String name;

    public SwimmableDuck(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.println("  " + name + " is flying low over the water!");
    }

    @Override
    public int getMaxAltitude() {
        return 100;
    }

    @Override
    public void swim() {
        System.out.println("  " + name + " is swimming gracefully!");
    }

    @Override
    public int getMaxDepth() {
        return 5;
    }

    @Override
    public void quack() {
        System.out.println("  " + name + " says: Quack! Quack!");
    }

    @Override
    public String toString() {
        return "SwimmableDuck{name='" + name + "'}";
    }
}