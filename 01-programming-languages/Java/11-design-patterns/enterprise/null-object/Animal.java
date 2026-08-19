package academy.javaengineering.patterns.enterprise.null_object;

/**
 * Animal interface defining the contract for all animal implementations,
 * including the null object.
 */
public interface Animal {

    String getName();

    void speak();

    boolean isReal();

    int getLegs();
}
