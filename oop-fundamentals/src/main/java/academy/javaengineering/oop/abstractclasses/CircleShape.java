package academy.javaengineering.oop.abstractclasses;

/**
 * CircleShape - Concrete implementation of abstract Shape2D class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CircleShape extends Shape2D {

    private final double radius;

    public CircleShape(double radius) {
        super("Circle");
        if (radius <= 0) throw new IllegalArgumentException("Radius must be positive");
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public double getRadius() {
        return radius;
    }
}