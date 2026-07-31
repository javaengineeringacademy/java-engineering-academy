package academy.javaengineering.oop.polymorphism;

/**
 * Circle - Concrete implementation of Shape.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Circle extends Shape {

    private final double radius;

    public Circle(double radius) {
        if (radius <= 0) throw new IllegalArgumentException("Radius must be positive");
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}