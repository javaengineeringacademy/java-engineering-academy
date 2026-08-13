package academy.javaengineering.oop.internals;

public class InterfacesInternals {

    interface Drawable {
        void draw();
        default void print() {
            System.out.println("Printing drawable");
        }
    }

    interface Resizable {
        void resize(int factor);
    }

    static class Circle implements Drawable, Resizable {
        double radius;

        Circle(double radius) { this.radius = radius; }

        @Override
        public void draw() {
            System.out.println("Drawing circle with radius " + radius);
        }

        @Override
        public void resize(int factor) {
            radius *= factor;
            System.out.println("Resized to " + radius);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Interfaces Internals ===\n");

        // 1. Interface as Contract
        System.out.println("--- Interface Contract ---");
        Circle circle = new Circle(5);
        circle.draw();
        circle.resize(2);
        System.out.println("Circle implements Drawable and Resizable");

        // 2. Multiple Interfaces
        System.out.println("\n--- Multiple Interfaces ---");
        System.out.println("Java allows implementing multiple interfaces");
        System.out.println("Class can extend one, implement many");
        System.out.println("Diamond problem: use default methods");

        // 3. Default Methods
        System.out.println("\n--- Default Methods ---");
        System.out.println("Java 8+: default method in interface");
        System.out.println("Provides implementation");
        System.out.println("Backward compatible");

        // 4. Interface vs Abstract Class
        System.out.println("\n--- Interface vs Abstract Class ---");
        System.out.println("Interface: contract only");
        System.out.println("Abstract class: partial implementation");
        System.out.println("Interface: multiple inheritance");
        System.out.println("Abstract class: single inheritance");
    }
}
