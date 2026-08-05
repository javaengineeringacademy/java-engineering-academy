package academy.javaengineering.oop.inheritance;

/**
 * Dog class extending Animal - demonstrates single inheritance.
 */
public class Dog extends Animal {

    private String breed;

    public Dog(String name, int age, String breed) {
        super(name, age); // Call parent constructor
        this.breed = breed;
    }

    public String getBreed() { return breed; }

    /** Dog-specific behavior. */
    public void fetch(String item) {
        System.out.println(name + " fetches the " + item);
    }

    public void bark() {
        System.out.println(name + " says: Woof!");
    }

    @Override
    public String describe() {
        return super.describe() + " [" + breed + "]";
    }
}
