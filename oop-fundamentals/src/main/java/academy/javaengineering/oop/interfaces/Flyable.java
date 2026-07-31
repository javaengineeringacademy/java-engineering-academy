package academy.javaengineering.oop.interfaces;

/**
 * Flyable - Interface demonstrating basic interface features.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Flyable {

    void fly();

    default void land() {
        System.out.println("  Landing gracefully");
    }

    int getMaxAltitude();
}