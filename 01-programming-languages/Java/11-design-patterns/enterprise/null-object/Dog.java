package academy.javaengineering.patterns.enterprise.null_object;

/**
 * Real Animal implementation — a Dog that performs actual behavior.
 */
public class Dog implements Animal {

    private final String name;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void speak() {
        System.out.println(name + " says: Woof!");
    }

    @Override
    public boolean isReal() {
        return true;
    }

    @Override
    public int getLegs() {
        return 4;
    }

    @Override
    public String toString() {
        return "Dog{name='" + name + "'}";
    }
}
