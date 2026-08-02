package academy.javaengineering.solid;

/**
 * Demonstrates Liskov Substitution Principle (LSP).
 * Subtypes must be substitutable for their base types.
 */
public class LiskovSubstitutionExample {

    // Bad: Square violates LSP by changing behavior
    static class Rectangle {
        protected int width;
        protected int height;

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getArea() {
            return width * height;
        }
    }

    static class Square extends Rectangle {
        @Override
        public void setWidth(int width) {
            this.width = width;
            this.height = width;
        }

        @Override
        public void setHeight(int height) {
            this.width = height;
            this.height = height;
        }
    }

    // Good: Proper abstraction
    interface Shape {
        int area();
    }

    static class BetterRectangle implements Shape {
        private final int width;
        private final int height;

        BetterRectangle(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public int area() {
            return width * height;
        }
    }

    static class BetterSquare implements Shape {
        private final int side;

        BetterSquare(int side) {
            this.side = side;
        }

        @Override
        public int area() {
            return side * side;
        }
    }

    static void printArea(Shape shape) {
        System.out.println("Area: " + shape.area());
    }
}
