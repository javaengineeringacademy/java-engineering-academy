package academy.javaengineering.patterns.creational;

public class Circle implements Shape {
    private double radius;
    private String color;

    public Circle(double radius, String color) {
        this.radius = radius;
        this.color = color;
    }

    public Circle(Circle source) {
        this.radius = source.radius;
        this.color = source.color;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Circle: radius=" + radius + ", color=" + color);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String getType() {
        return "Circle";
    }

    @Override
    public Circle clone() {
        return new Circle(this);
    }

    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
