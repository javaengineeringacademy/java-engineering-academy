package academy.javaengineering.oop.polymorphism;

/**
 * Triangle - Concrete implementation of Shape using Heron's formula.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Triangle extends Shape {

    private final double side1;
    private final double side2;
    private final double side3;

    public Triangle(double side1, double side2, double side3) {
        if (side1 <= 0 || side2 <= 0 || side3 <= 0) {
            throw new IllegalArgumentException("Sides must be positive");
        }
        if (side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1) {
            throw new IllegalArgumentException("Invalid triangle sides");
        }
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    public double getBase() { return side1; }

    @Override
    public double getArea() {
        double s = (side1 + side2 + side3) / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3)); // Heron's formula
    }

    @Override
    public double getPerimeter() {
        return side1 + side2 + side3;
    }
}