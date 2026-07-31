package academy.javaengineering.oop.inheritance;

/**
 * Puppy - Subclass of Dog demonstrating multilevel inheritance.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Puppy extends Dog {

    private int monthsOld;

    public Puppy(String name, String breed, int monthsOld) {
        super(name, breed); // Calls Dog constructor, which calls Animal constructor
        this.monthsOld = monthsOld;
        System.out.println("  Puppy constructor called for: " + name);
    }

    public void play() {
        System.out.println(name + " is playing excitedly!");
    }

    public void cuddle() {
        System.out.println(name + " wants cuddles!");
    }

    @Override
    public void sound() {
        System.out.println(name + " yips: Yip yip!");
    }

    public int getMonthsOld() {
        return monthsOld;
    }

    @Override
    public String toString() {
        return "Puppy{name='" + name + "', breed='" + breed + "', months=" + monthsOld + "}";
    }
}