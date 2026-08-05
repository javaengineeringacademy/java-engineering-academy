package academy.javaengineering.patterns.creational;

public class ShapeFactory {

    public static Shape createShape(String type, double... dimensions) {
        if (type == null) {
            throw new IllegalArgumentException("Shape type cannot be null");
        }

        return switch (type.toLowerCase()) {
            case "circle" -> {
                if (dimensions.length < 1) throw new IllegalArgumentException("Circle requires radius");
                yield new Circle(dimensions[0]);
            }
            case "rectangle" -> {
                if (dimensions.length < 2) throw new IllegalArgumentException("Rectangle requires width and height");
                yield new Rectangle(dimensions[0], dimensions[1]);
            }
            case "triangle" -> {
                if (dimensions.length < 2) throw new IllegalArgumentException("Triangle requires base and height");
                yield new Triangle(dimensions[0], dimensions[1]);
            }
            default -> throw new IllegalArgumentException("Unknown shape: " + type);
        };
    }

    public static Shape createCircle(double radius) {
        return new Circle(radius);
    }

    public static Shape createRectangle(double width, double height) {
        return new Rectangle(width, height);
    }

    public static Shape createTriangle(double base, double height) {
        return new Triangle(base, height);
    }
}
