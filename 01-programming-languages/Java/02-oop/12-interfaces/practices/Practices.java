package academy.javaengineering.oop.practices;

/**
 * Practice: Interfaces in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Defining an interface with abstract methods
 * - Implementing an interface with a class
 * - Using default methods in interfaces
 * - Using static methods in interfaces
 * - Programming to the interface (polymorphism)
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 12-interfaces ===\n");

        // Test Exercise 1: Implement the Drawable interface
        Shape circle = new Circle(5.0);
        double area = circle.area();
        System.out.println("Exercise 1 - Circle area: "
            + (Math.abs(area - Math.PI * 25) < 0.01 ? "PASS" : "FAIL (expected ~78.54, got " + area + ")"));

        // Test Exercise 2: Use the default method
        String desc = circle.description();
        System.out.println("Exercise 2 - default description(): "
            + (desc != null && desc.contains("Circle") && desc.contains("area") ? "PASS" : "FAIL"));

        // Test Exercise 3: Static factory method
        Shape rect = Shapes.createRectangle(4.0, 6.0);
        System.out.println("Exercise 3 - Shapes.createRectangle(): "
            + (Math.abs(rect.area() - 24.0) < 0.01 ? "PASS" : "FAIL"));

        // Test Exercise 4: Implement a second class with same interface
        Shape tri = new Triangle(3.0, 4.0);
        System.out.println("Exercise 4 - Triangle area: "
            + (Math.abs(tri.area() - 6.0) < 0.01 ? "PASS" : "FAIL"));

        // Test Exercise 5: Polymorphism - treat all shapes the same
        Shape[] shapes = {circle, rect, tri};
        double total = totalArea(shapes);
        System.out.println("Exercise 5 - totalArea(): "
            + (total > 0 ? "PASS" : "FAIL"));
    }

    // TODO 1: Implement this method to sum the areas of all shapes in the array
    // Hint: loop through the array and call .area() on each Shape
    static double totalArea(Shape[] shapes) {
        // YOUR CODE HERE
        return 0;
    }
}

/**
 * TODO 2: Complete the Shape interface with:
 * - abstract double area();
 * - default String description() that returns "Shape with area: " + area()
 */
interface Shape {
    // YOUR CODE HERE
}

/**
 * TODO 3: Complete the Shapes utility class with:
 * - static Shape createRectangle(double width, double height) returning a Rectangle
 */
class Shapes {
    // YOUR CODE HERE
}

/**
 * TODO 4: Complete Circle implementing Shape
 * - Constructor takes radius
 * - area() returns Math.PI * radius * radius
 */
class Circle implements Shape {
    private double radius;

    // YOUR CODE HERE
}

/**
 * TODO 5: Complete Rectangle implementing Shape
 * - Constructor takes width and height
 * - area() returns width * height
 */
class Rectangle implements Shape {
    private double width, height;

    // YOUR CODE HERE
}

/**
 * TODO 6: Complete Triangle implementing Shape
 * - Constructor takes base and height
 * - area() returns 0.5 * base * height
 */
class Triangle implements Shape {
    private double base, height;

    // YOUR CODE HERE
}
