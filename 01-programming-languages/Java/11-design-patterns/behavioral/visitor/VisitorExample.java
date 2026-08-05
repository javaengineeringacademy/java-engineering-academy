package academy.javaengineering.patterns.behavioral.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Real-world example demonstrating the Visitor pattern.
 * Shows visitors operating on different element types.
 */
public class VisitorExample {

    public static void main(String[] args) {
        List<Element> elements = new ArrayList<>();
        elements.add(new ConcreteElementA("Alpha"));
        elements.add(new ConcreteElementA("Beta"));
        elements.add(new ConcreteElementB(100));
        elements.add(new ConcreteElementB(200));

        ConcreteVisitor visitor = new ConcreteVisitor();

        System.out.println("=== Visitor Processing Elements ===");
        for (Element element : elements) {
            element.accept(visitor);
        }

        System.out.println("\n=== Visitor Results ===");
        System.out.println(visitor.getResult());
    }
}
