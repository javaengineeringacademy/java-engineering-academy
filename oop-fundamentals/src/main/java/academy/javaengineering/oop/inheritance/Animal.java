package academy.javaengineering.oop.inheritance;

/**
 * Animal - Base class demonstrating inheritance fundamentals.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Animal {

    protected String name;
    private int energy;

    public Animal(String name) {
        this.name = name;
        this.energy = 100;
        System.out.println("  Animal constructor called for: " + name);
    }

    public void eat() {
        energy += 10;
        System.out.println(name + " is eating. Energy: " + energy);
    }

    public void sleep() {
        energy += 20;
        System.out.println(name + " is sleeping. Energy: " + energy);
    }

    public void sound() {
        System.out.println(name + " makes a sound");
    }

    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String toString() {
        return "Animal{name='" + name + "', energy=" + energy + "}";
    }
}