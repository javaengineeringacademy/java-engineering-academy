/**
 * Circle extending Shape - demonstrates runtime polymorphism.
 */
public class Circle extends Shape {

    private final double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    public double getRadius() { return radius; }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String describe() {
        return "Circle[radius=%.2f, %s]".formatted(radius, super.describe());
    }
}