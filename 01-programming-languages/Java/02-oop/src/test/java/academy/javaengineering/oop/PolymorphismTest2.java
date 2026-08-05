package academy.javaengineering.oop;

import academy.javaengineering.oop.polymorphism.Shape;
import academy.javaengineering.oop.polymorphism.Circle;
import academy.javaengineering.oop.polymorphism.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Polymorphism Tests")
class PolymorphismTest {

    @Test
    @DisplayName("Circle calculates area correctly")
    void circleArea() {
        Circle circle = new Circle("red", 5.0);
        assertEquals(Math.PI * 25, circle.area(), 0.001);
    }

    @Test
    @DisplayName("Rectangle calculates area correctly")
    void rectangleArea() {
        Rectangle rect = new Rectangle("blue", 4.0, 6.0);
        assertEquals(24.0, rect.area(), 0.001);
    }

    @Test
    @DisplayName("Polymorphic array calls correct area()")
    void polymorphicArray() {
        Shape[] shapes = {
            new Circle("red", 5.0),
            new Rectangle("blue", 4.0, 6.0),
            new Shape("green")
        };

        assertEquals(Math.PI * 25, shapes[0].area(), 0.001);
        assertEquals(24.0, shapes[1].area(), 0.001);
        assertEquals(0.0, shapes[2].area(), 0.001);
    }

    @Test
    @DisplayName("Runtime type determines method called")
    void runtimeDispatch() {
        Shape shape = new Circle("yellow", 3.0);
        assertTrue(shape.area() > 0);
        // Runtime type is Circle, not Shape
        assertTrue(shape instanceof Circle);
    }

    @Test
    @DisplayName("Upcasting and downcasting")
    void casting() {
        Shape shape = new Rectangle("purple", 5.0, 3.0);
        // Upcast - automatic
        assertTrue(shape instanceof Shape);
        // Downcast - explicit
        Rectangle rect = (Rectangle) shape;
        assertEquals(5.0, rect.getWidth());
    }

    @Test
    @DisplayName("Describe works polymorphically")
    void describe() {
        Circle circle = new Circle("red", 2.0);
        String desc = circle.describe();
        assertTrue(desc.contains("Circle"));
        assertTrue(desc.contains("red"));
    }
}
