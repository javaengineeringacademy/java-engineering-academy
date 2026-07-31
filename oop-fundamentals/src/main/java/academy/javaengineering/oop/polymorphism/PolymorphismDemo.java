package academy.javaengineering.oop.polymorphism;

/**
 * PolymorphismDemo - Demonstrates compile-time and runtime polymorphism.
 * 
 * <p><b>Polymorphism</b> (many forms) allows objects to take different forms:
 * <ul>
 *   <li><b>Compile-time (static)</b>: Method overloading - same name, different parameters</li>
 *   <li><b>Runtime (dynamic)</b>: Method overriding - subclass provides specific implementation</li>
 * </ul>
 * 
 * <p><b>Key Benefits:</b>
 * <ul>
 *   <li>Code flexibility and extensibility</li>
 *   <li>Cleaner, more maintainable code</li>
 *   <li>Support for open/closed principle</li>
 *   <li>Dynamic method dispatch at runtime</li>
 * </ul>
 * 
 * <p><b>Analogy:</b> A remote control (reference) can operate different devices (objects) -
 * TV, stereo, DVD player - each responds differently to the same "power on" command.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class PolymorphismDemo {

    private PolymorphismDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Polymorphism Demo ===\n");

        // Compile-time polymorphism (overloading)
        System.out.println("--- Compile-Time Polymorphism (Overloading) ---");
        Calculator calc = new Calculator();
        System.out.println("add(5, 3) = " + calc.add(5, 3));
        System.out.println("add(5.5, 3.2) = " + calc.add(5.5, 3.2));
        System.out.println("add(1, 2, 3) = " + calc.add(1, 2, 3));
        System.out.println("add(\"Hello\", \" World\") = " + calc.add("Hello", " World"));

        // Runtime polymorphism (overriding)
        System.out.println("\n--- Runtime Polymorphism (Overriding) ---");
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        Shape triangle = new Triangle(3.0, 4.0, 5.0);

        System.out.println("Circle area: " + circle.getArea());
        System.out.println("Rectangle area: " + rectangle.getArea());
        System.out.println("Triangle area: " + triangle.getArea());

        // Polymorphic method calls
        System.out.println("\n--- Polymorphic Method Calls ---");
        printShapeInfo(circle);
        printShapeInfo(rectangle);
        printShapeInfo(triangle);

        // Array of different types (polymorphic collection)
        System.out.println("\n--- Polymorphic Array ---");
        Shape[] shapes = {circle, rectangle, triangle};
        double totalArea = 0;
        for (Shape shape : shapes) {
            totalArea += shape.getArea();
            System.out.println(shape.getClass().getSimpleName() + ": " + shape.getArea());
        }
        System.out.println("Total area: " + totalArea);

        // instanceof for type checking
        System.out.println("\n--- Type Checking ---");
        for (Shape shape : shapes) {
            if (shape instanceof Circle c) { // Pattern matching
                System.out.println("Circle with radius: " + c.getRadius());
            } else if (shape instanceof Rectangle r) {
                System.out.println("Rectangle with dimensions: " + r.getWidth() + "x" + r.getHeight());
            } else if (shape instanceof Triangle t) {
                System.out.println("Triangle with base: " + t.getBase());
            }
        }

        // Demonstrating dynamic dispatch
        System.out.println("\n--- Dynamic Dispatch ---");
        Shape shape = getShape("circle");
        System.out.println("Got shape: " + shape.getClass().getSimpleName());
        System.out.println("Area: " + shape.getArea()); // Calls Circle's getArea()
    }

    static void printShapeInfo(Shape shape) {
        System.out.printf("%s [area=%.2f, perimeter=%.2f]%n",
            shape.getClass().getSimpleName(),
            shape.getArea(),
            shape.getPerimeter());
    }

    static Shape getShape(String type) {
        return switch (type.toLowerCase()) {
            case "circle" -> new Circle(5.0);
            case "rectangle" -> new Rectangle(4.0, 6.0);
            case "triangle" -> new Triangle(3.0, 4.0, 5.0);
            default -> throw new IllegalArgumentException("Unknown shape: " + type);
        };
    }
}