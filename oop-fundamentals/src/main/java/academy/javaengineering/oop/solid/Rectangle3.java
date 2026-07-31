package academy.javaengineering.oop.solid;

/**
 * Rectangle3 - Concrete shape for Open/Closed Principle demonstration.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Rectangle3 extends Shape3 {

    private final double width;
    private final double height;

    public Rectangle3(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }
}