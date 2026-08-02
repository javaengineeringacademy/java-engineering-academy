package academy.javaengineering.patterns.factory;

public class FactoryExample {

    public interface Shape {
        void draw();
    }

    public static class Circle implements Shape {
        @Override
        public void draw() {
            System.out.println("Drawing Circle");
        }
    }

    public static class Rectangle implements Shape {
        @Override
        public void draw() {
            System.out.println("Drawing Rectangle");
        }
    }

    public static class ShapeFactory {
        public static Shape create(String type) {
            return switch (type.toLowerCase()) {
                case "circle" -> new Circle();
                case "rectangle" -> new Rectangle();
                default -> throw new IllegalArgumentException("Unknown shape: " + type);
            };
        }
    }

    public static void main(String[] args) {
        Shape circle = ShapeFactory.create("circle");
        circle.draw();

        Shape rectangle = ShapeFactory.create("rectangle");
        rectangle.draw();
    }
}
