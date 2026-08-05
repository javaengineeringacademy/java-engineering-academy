package academy.javaengineering.patterns.creational;

public class Circle implements Shape {
    private final double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Circle with radius: " + radius);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String getType() {
        return "Circle";
    }

    public double getRadius() {
        return radius;
    }
}
