package academy.javaengineering.oop.abstraction;

/**
 * Resizable - Interface demonstrating additional abstraction capability.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Resizable {

    double getArea();

    default void resize(double factor) {
        System.out.println("  Resizing by factor: " + factor);
    }
}