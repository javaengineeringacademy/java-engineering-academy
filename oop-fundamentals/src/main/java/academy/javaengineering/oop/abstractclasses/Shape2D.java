package academy.javaengineering.oop.abstractclasses;

/**
 * Shape2D - Abstract class demonstrating abstract methods and shared state.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class Shape2D {

    protected final String name;
    private static int shapeCount = 0;

    protected Shape2D(String name) {
        this.name = name;
        shapeCount++;
    }

    // Abstract methods - must be implemented by subclasses
    public abstract double getArea();
    public abstract double getPerimeter();

    // Concrete methods - shared implementation
    public String getName() {
        return name;
    }

    public static int getShapeCount() {
        return shapeCount;
    }

    @Override
    public String toString() {
        return name + "[area=" + String.format("%.2f", getArea()) + "]";
    }
}