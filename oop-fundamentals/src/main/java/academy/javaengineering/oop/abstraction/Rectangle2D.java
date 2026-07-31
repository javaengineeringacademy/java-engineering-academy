package academy.javaengineering.oop.abstraction;

/**
 * Rectangle2D - Concrete implementation of Drawable and Resizable interfaces.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Rectangle2D implements Drawable, Resizable {

    private final double width;
    private final double height;

    public Rectangle2D(double width, double height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Dimensions must be positive");
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("  Drawing rectangle " + width + "x" + height);
    }

    @Override
    public String getDescription() {
        return "Rectangle(" + width + "x" + height + ")";
    }

    @Override
    public double getArea() {
        return width * height;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}