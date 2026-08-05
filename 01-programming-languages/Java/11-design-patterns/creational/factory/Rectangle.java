package academy.javaengineering.patterns.creational;

public class Rectangle implements Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing Rectangle: " + width + " x " + height);
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String getType() {
        return "Rectangle";
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
