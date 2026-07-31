package academy.javaengineering.oop.interfaces;

/**
 * Quackable - Interface for quacking capability.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Quackable {

    void quack();

    default void silent() {
        System.out.println("  *silent nod*");
    }
}