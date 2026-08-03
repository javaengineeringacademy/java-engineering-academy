package academy.javaengineering.patterns.factory;

/**
 * Demonstrates the Factory design pattern for object creation.
 *
 * <p>The Factory pattern provides an interface for creating objects without
 * specifying their concrete classes. A factory method centralizes object creation
 * logic and returns the appropriate implementation based on input.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Factory method for centralized object creation</li>
 *   <li>Interface-based programming</li>
 *   <li>Switch-based dispatching</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class FactoryExample {

    /**
     * Shape interface defining the contract for all shapes.
     */
    public interface Shape {
        /**
         * Draws the shape.
         */
        void draw();
    }

    /**
     * Circle implementation of Shape.
     */
    public static class Circle implements Shape {
        @Override
        public void draw() {
            System.out.println("Drawing Circle");
        }
    }

    /**
     * Rectangle implementation of Shape.
     */
    public static class Rectangle implements Shape {
        @Override
        public void draw() {
            System.out.println("Drawing Rectangle");
        }
    }

    /**
     * Factory class responsible for creating Shape instances.
     */
    public static class ShapeFactory {
        /**
         * Creates a Shape based on the specified type.
         *
         * @param type the shape type ("circle" or "rectangle")
         * @return the created Shape
         * @throws IllegalArgumentException if type is unknown
         */
        public static Shape create(String type) {
            return switch (type.toLowerCase()) {
                case "circle" -> new Circle();
                case "rectangle" -> new Rectangle();
                default -> throw new IllegalArgumentException("Unknown shape: " + type);
            };
        }
    }

    /**
     * Demonstrates factory pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Shape circle = ShapeFactory.create("circle");
        circle.draw();

        Shape rectangle = ShapeFactory.create("rectangle");
        rectangle.draw();
    }
}
