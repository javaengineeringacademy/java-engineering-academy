package academy.javaengineering.oop.interfaces;

/**
 * Swimmable - Interface for swimming capability.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Swimmable {

    void swim();

    default void dive() {
        System.out.println("  Diving deep!");
    }

    int getMaxDepth();
}