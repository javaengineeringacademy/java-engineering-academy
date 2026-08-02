package academy.javaengineering.solid;

/**
 * Demonstrates Open/Closed Principle (OCP).
 * Software entities should be open for extension, closed for modification.
 */
public class OpenClosedExample {

    // Bad: Must modify class to add new shapes
    static class AreaCalculator {
        public double calculate(Object shape) {
            if (shape instanceof Circle) {
                Circle c = (Circle) shape;
                return Math.PI * c.radius * c.radius;
            } else if (shape instanceof Rectangle) {
                Rectangle r = (Rectangle) shape;
                return r.width * r.height;
            }
            return 0;
        }
    }

    // Good: Open for extension, closed for modification
    interface Shape {
        double area();
    }

    static class Circle implements Shape {
        double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    static class Rectangle implements Shape {
        double width, height;

        Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public double area() {
            return width * height;
        }
    }

    static class Triangle implements Shape {
        double base, height;

        Triangle(double base, double height) {
            this.base = base;
            this.height = height;
        }

        @Override
        public double area() {
            return 0.5 * base * height;
        }
    }

    static class BetterAreaCalculator {
        public double calculate(Shape shape) {
            return shape.area();
        }
    }
}
