package academy.javaengineering.patterns.creational;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {

    @Test
    void createCircleWithValidDimensions() {
        Shape shape = ShapeFactory.createShape("circle", 5.0);
        assertInstanceOf(Circle.class, shape);
        assertEquals("Circle", shape.getType());
        assertEquals(Math.PI * 25, shape.area(), 0.001);
    }

    @Test
    void createRectangleWithValidDimensions() {
        Shape shape = ShapeFactory.createShape("rectangle", 4.0, 6.0);
        assertInstanceOf(Rectangle.class, shape);
        assertEquals("Rectangle", shape.getType());
        assertEquals(24.0, shape.area(), 0.001);
    }

    @Test
    void createTriangleWithValidDimensions() {
        Shape shape = ShapeFactory.createShape("triangle", 3.0, 8.0);
        assertInstanceOf(Triangle.class, shape);
        assertEquals("Triangle", shape.getType());
        assertEquals(12.0, shape.area(), 0.001);
    }

    @Test
    void createWithNullTypeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ShapeFactory.createShape(null));
    }

    @Test
    void createWithUnknownTypeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ShapeFactory.createShape("pentagon"));
    }

    @Test
    void createWithInsufficientDimensionsThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ShapeFactory.createShape("rectangle", 5.0));
    }
}
