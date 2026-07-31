package academy.javaengineering.oop.inheritance;

/**
 * Dog - Subclass of Animal demonstrating inheritance and method overriding.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Dog extends Animal {

    protected String breed;

    public Dog(String name, String breed) {
        super(name); // Must be first statement
        this.breed = breed;
        System.out.println("  Dog constructor called for: " + name);
    }

    public void bark() {
        System.out.println(name + " says: Woof! Woof!");
    }

    public void fetch() {
        System.out.println(name + " is fetching the ball!");
    }

    @Override
    public void sound() {
        System.out.println(name + " barks: Woof!");
    }

    public String getBreed() {
        return breed;
    }

    @Override
    public String toString() {
        return "Dog{name='" + name + "', breed='" + breed + "'}";
    }
}