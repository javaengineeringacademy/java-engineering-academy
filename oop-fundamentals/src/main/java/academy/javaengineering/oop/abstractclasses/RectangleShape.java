package academy.javaengineering.oop.abstractclasses;

/**
 * RectangleShape - Concrete implementation of abstract Shape2D class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class RectangleShape extends Shape2D {

    private final double width;
    private final double height;

    public RectangleShape(double width, double height) {
        super("Rectangle");
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Dimensions must be positive");
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}