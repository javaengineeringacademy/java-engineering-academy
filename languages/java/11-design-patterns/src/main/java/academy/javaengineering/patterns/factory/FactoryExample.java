package academy.javaengineering.patterns.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Demonstrates all flavors of the Factory design pattern.
 *
 * <h3>Factory Flavors:</h3>
 * <ol>
 *   <li>Simple Factory (Static Method)</li>
 *   <li>Factory Method (Subclassing)</li>
 *   <li>Parameterized Factory</li>
 *   <li>Factory Registry (Lambda-based)</li>
 * </ol>
 */
public class FactoryExample {

    // ========================================
    // Shared Interface
    // ========================================
    public interface Shape {
        void draw();
        String getType();
    }

    // ========================================
    // Concrete Products
    // ========================================
    public static class Circle implements Shape {
        private final double radius;
        
        public Circle(double radius) {
            this.radius = radius;
        }
        
        @Override
        public void draw() {
            System.out.println("Drawing Circle with radius " + radius);
        }
        
        @Override
        public String getType() { return "Circle"; }
    }

    public static class Rectangle implements Shape {
        private final double width;
        private final double height;
        
        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }
        
        @Override
        public void draw() {
            System.out.println("Drawing Rectangle " + width + "x" + height);
        }
        
        @Override
        public String getType() { return "Rectangle"; }
    }

    public static class Triangle implements Shape {
        private final double base;
        private final double height;
        
        public Triangle(double base, double height) {
            this.base = base;
            this.height = height;
        }
        
        @Override
        public void draw() {
            System.out.println("Drawing Triangle with base " + base + " and height " + height);
        }
        
        @Override
        public String getType() { return "Triangle"; }
    }

    // ========================================
    // Flavor 1: Simple Factory (Static Method)
    // ========================================
    static class SimpleShapeFactory {
        public static Shape create(String type) {
            return switch (type.toLowerCase()) {
                case "circle" -> new Circle(1.0);
                case "rectangle" -> new Rectangle(1.0, 1.0);
                case "triangle" -> new Triangle(1.0, 1.0);
                default -> throw new IllegalArgumentException("Unknown shape: " + type);
            };
        }
    }

    // ========================================
    // Flavor 2: Factory Method (Subclassing)
    // ========================================
    abstract static class ShapeFactoryMethod {
        public abstract Shape createShape();
        
        public void processShape() {
            Shape shape = createShape();
            System.out.print("Factory Method: ");
            shape.draw();
        }
    }
    
    static class CircleFactory extends ShapeFactoryMethod {
        private final double radius;
        
        public CircleFactory(double radius) {
            this.radius = radius;
        }
        
        @Override
        public Shape createShape() {
            return new Circle(radius);
        }
    }
    
    static class RectangleFactory extends ShapeFactoryMethod {
        private final double width;
        private final double height;
        
        public RectangleFactory(double width, double height) {
            this.width = width;
            this.height = height;
        }
        
        @Override
        public Shape createShape() {
            return new Rectangle(width, height);
        }
    }

    // ========================================
    // Flavor 3: Parameterized Factory
    // ========================================
    static class ParameterizedShapeFactory {
        public static Shape create(String type, Map<String, Double> params) {
            return switch (type.toLowerCase()) {
                case "circle" -> new Circle(params.getOrDefault("radius", 1.0));
                case "rectangle" -> new Rectangle(
                    params.getOrDefault("width", 1.0),
                    params.getOrDefault("height", 1.0)
                );
                case "triangle" -> new Triangle(
                    params.getOrDefault("base", 1.0),
                    params.getOrDefault("height", 1.0)
                );
                default -> throw new IllegalArgumentException("Unknown shape: " + type);
            };
        }
    }

    // ========================================
    // Flavor 4: Factory Registry (Lambda-based)
    // ========================================
    static class RegistryShapeFactory {
        private static final Map<String, Supplier<Shape>> registry = new HashMap<>();
        
        static {
            registry.put("circle", () -> new Circle(2.0));
            registry.put("rectangle", () -> new Rectangle(3.0, 4.0));
            registry.put("triangle", () -> new Triangle(5.0, 6.0));
        }
        
        public static void register(String type, Supplier<Shape> supplier) {
            registry.put(type, supplier);
        }
        
        public static Shape create(String type) {
            Supplier<Shape> supplier = registry.get(type.toLowerCase());
            if (supplier == null) {
                throw new IllegalArgumentException("Unknown shape: " + type);
            }
            return supplier.get();
        }
    }

    // ========================================
    // Main Method
    // ========================================
    public static void main(String[] args) {
        System.out.println("=== Factory Pattern - All 4 Flavors ===\n");
        
        // Flavor 1: Simple Factory
        System.out.println("--- 1. Simple Factory (Static Method) ---");
        Shape s1 = SimpleShapeFactory.create("circle");
        Shape s2 = SimpleShapeFactory.create("rectangle");
        Shape s3 = SimpleShapeFactory.create("triangle");
        s1.draw();
        s2.draw();
        s3.draw();
        
        // Flavor 2: Factory Method
        System.out.println("\n--- 2. Factory Method (Subclassing) ---");
        ShapeFactoryMethod f1 = new CircleFactory(5.0);
        ShapeFactoryMethod f2 = new RectangleFactory(4.0, 6.0);
        f1.processShape();
        f2.processShape();
        
        // Flavor 3: Parameterized Factory
        System.out.println("\n--- 3. Parameterized Factory ---");
        Map<String, Double> params1 = Map.of("radius", 3.5);
        Map<String, Double> params2 = Map.of("width", 5.0, "height", 7.0);
        Map<String, Double> params3 = Map.of("base", 4.0, "height", 6.0);
        
        Shape p1 = ParameterizedShapeFactory.create("circle", params1);
        Shape p2 = ParameterizedShapeFactory.create("rectangle", params2);
        Shape p3 = ParameterizedShapeFactory.create("triangle", params3);
        p1.draw();
        p2.draw();
        p3.draw();
        
        // Flavor 4: Factory Registry
        System.out.println("\n--- 4. Factory Registry (Lambda-based) ---");
        Shape r1 = RegistryShapeFactory.create("circle");
        Shape r2 = RegistryShapeFactory.create("rectangle");
        r1.draw();
        r2.draw();
        
        // Register custom shape
        RegistryShapeFactory.register("diamond", () -> new Shape() {
            @Override
            public void draw() {
                System.out.println("Drawing Diamond (custom registered)");
            }
            
            @Override
            public String getType() { return "Diamond"; }
        });
        
        Shape r3 = RegistryShapeFactory.create("diamond");
        r3.draw();
        
        System.out.println("\n=== Summary ===");
        System.out.println("Simple Factory:    Centralized creation, switch-based");
        System.out.println("Factory Method:    Subclass decides which class to instantiate");
        System.out.println("Parameterized:     Creates based on parameters");
        System.out.println("Registry:          Dynamic registration, lambda-based");
    }
}
