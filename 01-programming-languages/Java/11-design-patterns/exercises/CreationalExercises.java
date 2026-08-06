package academy.javaengineering.exercises;

import java.util.*;
import java.util.function.*;

/**
 * Exercises: Creational Design Patterns (Factory, Builder, Singleton)
 *
 * Complete the TODO sections below.
 */
public class CreationalExercises {

    // TODO 1: Implement a Factory pattern for creating different Shapes
    // Create a Shape interface with area() and perimeter() methods
    // Implement Circle, Rectangle, Triangle
    // ShapeFactory.create("circle", radius) etc.
    public interface Shape {
        double area();
        double perimeter();
        String getType();
    }

    public static class Circle implements Shape {
        private final double radius;

        public Circle(double radius) {
            this.radius = radius;
        }

        @Override public double area() { return Math.PI * radius * radius; }
        @Override public double perimeter() { return 2 * Math.PI * radius; }
        @Override public String getType() { return "circle"; }
        public double getRadius() { return radius; }
    }

    public static class Rectangle implements Shape {
        private final double width, height;

        public Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override public double area() { return width * height; }
        @Override public double perimeter() { return 2 * (width + height); }
        @Override public String getType() { return "rectangle"; }
    }

    public static class ShapeFactory {
        public static Shape create(String type, double... dimensions) {
            // TODO: implement factory method
            // "circle" -> Circle(dimensions[0])
            // "rectangle" -> Rectangle(dimensions[0], dimensions[1])
            // throw IllegalArgumentException for unknown type
            return null;
        }
    }

    // TODO 2: Implement a Builder pattern for a House
    public static class House {
        private final String foundation;
        private final String structure;
        private final String roof;
        private final boolean hasGarden;
        private final boolean hasGarage;

        private House(Builder builder) {
            this.foundation = builder.foundation;
            this.structure = builder.structure;
            this.roof = builder.roof;
            this.hasGarden = builder.hasGarden;
            this.hasGarage = builder.hasGarage;
        }

        public String getFoundation() { return foundation; }
        public String getStructure() { return structure; }
        public String getRoof() { return roof; }
        public boolean hasGarden() { return hasGarden; }
        public boolean hasGarage() { return hasGarage; }

        public static class Builder {
            private String foundation;
            private String structure;
            private String roof;
            private boolean hasGarden = false;
            private boolean hasGarage = false;

            // TODO: implement builder methods
            public Builder foundation(String foundation) {
                // TODO: implement
                return this;
            }

            public Builder structure(String structure) {
                // TODO: implement
                return this;
            }

            public Builder roof(String roof) {
                // TODO: implement
                return this;
            }

            public Builder withGarden() {
                // TODO: implement
                return this;
            }

            public Builder withGarage() {
                // TODO: implement
                return this;
            }

            public House build() {
                // TODO: implement - validate required fields
                return new House(this);
            }
        }
    }

    // TODO 3: Implement Singleton pattern (thread-safe)
    public static class DatabaseConnection {
        private static volatile DatabaseConnection instance;
        private final String connectionId;

        private DatabaseConnection() {
            this.connectionId = UUID.randomUUID().toString();
        }

        public static DatabaseConnection getInstance() {
            // TODO: implement thread-safe lazy initialization
            return null;
        }

        public String getConnectionId() {
            return connectionId;
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        CreationalExercises exercises = new CreationalExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== CreationalExercises Tests ===\n");

        // Test 1 - Factory
        total++;
        Shape circle = ShapeFactory.create("circle", 5.0);
        if (circle != null && "circle".equals(circle.getType())
            && Math.abs(circle.area() - 78.54) < 0.1) {
            System.out.println("Test 1a PASSED: ShapeFactory circle");
            passed++;
        } else {
            System.out.println("Test 1a FAILED: ShapeFactory circle");
        }

        total++;
        Shape rect = ShapeFactory.create("rectangle", 4.0, 5.0);
        if (rect != null && "rectangle".equals(rect.getType()) && rect.area() == 20.0) {
            System.out.println("Test 1b PASSED: ShapeFactory rectangle");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: ShapeFactory rectangle");
        }

        // Test 2 - Builder
        total++;
        House house = new House.Builder()
            .foundation("concrete")
            .structure("wood")
            .roof("tile")
            .withGarden()
            .withGarage()
            .build();
        if ("concrete".equals(house.getFoundation())
            && "wood".equals(house.getStructure())
            && "tile".equals(house.getRoof())
            && house.hasGarden() && house.hasGarage()) {
            System.out.println("Test 2 PASSED: House Builder");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: House Builder");
        }

        // Test 3 - Singleton
        total++;
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        if (db1 != null && db1 == db2 && db1.getConnectionId().equals(db2.getConnectionId())) {
            System.out.println("Test 3 PASSED: Singleton");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: Singleton - different instances");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
