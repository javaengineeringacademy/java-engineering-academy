package academy.javaengineering.oop.abstraction;

/**
 * Camera - Interface for camera functionality.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Camera {

    void takePhoto();

    default void recordVideo() {
        System.out.println("  Recording video...");
    }

    int getResolution();
}