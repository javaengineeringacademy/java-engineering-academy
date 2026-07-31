package academy.javaengineering.oop.abstraction;

/**
 * Circle2D - Concrete implementation of Drawable and Resizable interfaces.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Circle2D implements Drawable, Resizable {

    private final double radius;

    public Circle2D(double radius) {
        if (radius <= 0) throw new IllegalArgumentException("Radius must be positive");
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("  Drawing circle with radius: " + radius);
    }

    @Override
    public String getDescription() {
        return "Circle(r=" + radius + ")";
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getRadius() {
        return radius;
    }
}