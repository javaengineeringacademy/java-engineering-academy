package academy.javaengineering.patterns.enterprise.null_object;

/**
 * Null Object implementation of Animal.
 * Provides safe no-op behavior so callers never need null checks.
 */
public class NullAnimal implements Animal {

    private static final NullAnimal INSTANCE = new NullAnimal();

    private NullAnimal() {}

    public static NullAnimal getInstance() {
        return INSTANCE;
    }

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public void speak() {
        // Intentionally empty — no-op
    }

    @Override
    public boolean isReal() {
        return false;
    }

    @Override
    public int getLegs() {
        return 0;
    }

    @Override
    public String toString() {
        return "NullAnimal{}";
    }
}
