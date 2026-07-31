package academy.javaengineering.oop.solid;

/**
 * Circle3 - Concrete shape for Open/Closed Principle demonstration.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Circle3 extends Shape3 {

    private final double radius;

    public Circle3(double radius) {
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}