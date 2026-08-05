/**
 * Rectangle extending Shape - demonstrates runtime polymorphism.
 */
public class Rectangle extends Shape {

    private final double width;
    private final double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String describe() {
        return "Rectangle[%.2fx%.2f, %s]".formatted(width, height, super.describe());
    }
}