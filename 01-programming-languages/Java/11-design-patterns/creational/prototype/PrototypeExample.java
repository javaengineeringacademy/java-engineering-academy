package academy.javaengineering.patterns.creational;

import java.util.ArrayList;
import java.util.List;

public class PrototypeExample {

    public static void main(String[] args) {
        Circle redCircle = new Circle(5.0, "red");
        Rectangle blueRect = new Rectangle(4.0, 6.0, "blue");

        System.out.println("=== Original Shapes ===");
        redCircle.draw();
        blueRect.draw();

        Circle clonedCircle = redCircle.clone();
        clonedCircle.setColor("green");
        clonedCircle.setRadius(3.0);

        Rectangle clonedRect = blueRect.clone();
        clonedRect.setColor("yellow");
        clonedRect.setWidth(8.0);

        System.out.println("\n=== After Cloning & Modifying ===");
        System.out.print("Original circle: ");
        redCircle.draw();
        System.out.print("Cloned circle:   ");
        clonedCircle.draw();
        System.out.print("Original rect:   ");
        blueRect.draw();
        System.out.print("Cloned rect:     ");
        clonedRect.draw();

        System.out.println("\n=== Cloning for Batch Operations ===");
        List<Shape> prototypes = List.of(redCircle, blueRect);
        List<Shape> batch = new ArrayList<>();
        for (Shape prototype : prototypes) {
            Shape clone = prototype.clone();
            clone.draw();
            batch.add(clone);
        }
    }
}
