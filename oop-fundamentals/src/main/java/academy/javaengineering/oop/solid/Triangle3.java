package academy.javaengineering.oop.solid;

/**
 * Triangle3 - Concrete shape for Open/Closed Principle demonstration.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Triangle3 extends Shape3 {

    private final double side1;
    private final double side2;
    private final double side3;

    public Triangle3(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    @Override
    public double getArea() {
        double s = (side1 + side2 + side3) / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }
}