package academy.javaengineering.oop.abstraction;

/**
 * Drawable - Interface demonstrating abstraction through pure contracts.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Drawable {

    void draw();

    default void erase() {
        System.out.println("  Erasing drawing...");
    }

    String getDescription();
}