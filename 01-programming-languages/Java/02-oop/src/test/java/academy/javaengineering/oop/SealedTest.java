package academy.javaengineering.oop;

import academy.javaengineering.oop.sealed.Circle;
import academy.javaengineering.oop.sealed.Rectangle;
import academy.javaengineering.oop.sealed.Shape;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sealed Class Tests")
class SealedTest {

    @Test
    @DisplayName("Circle extends sealed Shape")
    void circleArea() {
        Shape shape = new Circle("red", 5.0);
        assertEquals(Math.PI * 25, shape.area(), 0.001);
    }

    @Test
    @DisplayName("Rectangle extends sealed Shape")
    void rectangleArea() {
        Shape shape = new Rectangle("blue", 4.0, 6.0);
        assertEquals(24.0, shape.area(), 0.001);
    }

    @Test
    @DisplayName("Pattern matching with sealed classes")
    void patternMatching() {
        Shape shape = new Circle("green", 3.0);
        String desc = switch (shape) {
            case Circle c -> "Circle with radius " + c.getRadius();
            case Rectangle r -> "Rectangle " + r.getWidth() + "x" + r.getHeight();
        };
        assertTrue(desc.contains("Circle"));
    }

    @Test
    @DisplayName("Shape color is accessible")
    void color() {
        Shape shape = new Circle("red", 5.0);
        assertEquals("red", shape.getColor());
    }

    @Test
    @DisplayName("Permitted subclasses are final")
    void subclassesAreFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(Circle.class.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(Rectangle.class.getModifiers()));
    }

    @Test
    @DisplayName("Sealed class is not final")
    void sealedNotFinal() {
        assertFalse(java.lang.reflect.Modifier.isFinal(Shape.class.getModifiers()));
    }
}
