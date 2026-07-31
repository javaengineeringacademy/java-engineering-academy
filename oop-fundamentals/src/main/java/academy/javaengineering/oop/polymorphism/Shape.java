package academy.javaengineering.oop.polymorphism;

/**
 * Shape - Abstract base class for runtime polymorphism demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class Shape {

    public abstract double getArea();

    public abstract double getPerimeter();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[area=" + String.format("%.2f", getArea()) + "]";
    }
}