package academy.javaengineering.patterns.creational;

import java.util.ArrayList;
import java.util.List;

public class FactoryExample {

    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();

        shapes.add(ShapeFactory.createShape("circle", 5.0));
        shapes.add(ShapeFactory.createShape("rectangle", 4.0, 6.0));
        shapes.add(ShapeFactory.createShape("triangle", 3.0, 8.0));
        shapes.add(ShapeFactory.createCircle(3.5));
        shapes.add(ShapeFactory.createRectangle(2.0, 10.0));

        System.out.println("=== Shape Gallery ===");
        for (Shape shape : shapes) {
            System.out.printf("%s - Area: %.2f%n", shape.getType(), shape.area());
            shape.draw();
        }

        double totalArea = shapes.stream()
                .mapToDouble(Shape::area)
                .sum();
        System.out.printf("%nTotal area of all shapes: %.2f%n", totalArea);
    }
}
