package academy.javaengineering.oop.internals;

public class AbstractClassesInternals {

    abstract static class Shape {
        String color;

        Shape(String color) {
            this.color = color;
        }

        abstract double getArea();
        abstract double getPerimeter();

        void display() {
            System.out.println("Color: " + color);
            System.out.println("Area: " + getArea());
        }
    }

    static class Circle extends Shape {
        double radius;

        Circle(String color, double radius) {
            super(color);
            this.radius = radius;
        }

        @Override
        double getArea() {
            return Math.PI * radius * radius;
        }

        @Override
        double getPerimeter() {
            return 2 * Math.PI * radius;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Abstract Classes Internals ===\n");

        // 1. Abstract Class Usage
        System.out.println("--- Abstract Class Usage ---");
        Circle circle = new Circle("Red", 5);
        circle.display();
        System.out.println("Perimeter: " + circle.getPerimeter());

        // 2. Template Method Pattern
        System.out.println("\n--- Template Method ---");
        System.out.println("Abstract class defines algorithm");
        System.out.println("Subclasses implement specific steps");
        System.out.println("Code reusability + flexibility");

        // 3. State vs Behavior
        System.out.println("\n--- State vs Behavior ---");
        System.out.println("Abstract class: state + behavior");
        System.out.println("Interface: behavior only (mostly)");
        System.out.println("Abstract class: partial implementation");
    }
}
