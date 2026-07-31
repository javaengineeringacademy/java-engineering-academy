package academy.javaengineering.oop.solid;

/**
 * ShapeAreaCalculator - Demonstrates Open/Closed Principle.
 * 
 * <p>Open for extension: Can add new shapes by creating new Shape3 subclasses.
 * Closed for modification: No need to modify this class when adding shapes.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ShapeAreaCalculator {

    public void calculate(Shape3 shape) {
        System.out.printf("  %s area: %.2f%n",
            shape.getClass().getSimpleName(), shape.getArea());
    }
}