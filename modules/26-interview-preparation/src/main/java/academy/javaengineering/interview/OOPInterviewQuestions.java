package academy.javaengineering.interview;

import java.util.*;

/**
 * OOP Interview Questions - OOP concepts interview prep.
 */
public class OOPInterviewQuestions {

    public interface Shape {
        double area();
        double perimeter();
        String getType();
    }

    public static class Circle implements Shape {
        private final double radius;
        public Circle(double radius) { this.radius = radius; }
        @Override public double area() { return Math.PI * radius * radius; }
        @Override public double perimeter() { return 2 * Math.PI * radius; }
        @Override public String getType() { return "Circle"; }
        public double getRadius() { return radius; }
    }

    public static class Rectangle implements Shape {
        private final double width;
        private final double height;
        public Rectangle(double width, double height) { this.width = width; this.height = height; }
        @Override public double area() { return width * height; }
        @Override public double perimeter() { return 2 * (width + height); }
        @Override public String getType() { return "Rectangle"; }
    }

    public static void main(String[] args) {
        List<Shape> shapes = List.of(new Circle(5), new Rectangle(4, 6));
        for (Shape shape : shapes) {
            System.out.println(shape.getType() + " - Area: " + shape.area());
        }
    }
}
