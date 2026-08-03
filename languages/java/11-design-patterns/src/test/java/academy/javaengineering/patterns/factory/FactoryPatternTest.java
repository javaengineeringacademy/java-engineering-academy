package academy.javaengineering.patterns.factory;

import academy.javaengineering.patterns.factory.FactoryExample.Shape;
import academy.javaengineering.patterns.factory.FactoryExample.Circle;
import academy.javaengineering.patterns.factory.FactoryExample.Rectangle;
import academy.javaengineering.patterns.factory.FactoryExample.SimpleShapeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryPatternTest {

    @Test
    @DisplayName("Should create Circle when type is 'circle'")
    void shouldCreateCircle() {
        Shape shape = SimpleShapeFactory.create("circle");
        assertNotNull(shape, "Created shape should not be null");
        assertInstanceOf(Circle.class, shape, "Should be a Circle instance");
    }

    @Test
    @DisplayName("Should create Rectangle when type is 'rectangle'")
    void shouldCreateRectangle() {
        Shape shape = SimpleShapeFactory.create("rectangle");
        assertNotNull(shape, "Created shape should not be null");
        assertInstanceOf(Rectangle.class, shape, "Should be a Rectangle instance");
    }

    @Test
    @DisplayName("Should be case-insensitive for circle")
    void shouldHandleUpperCaseCircle() {
        Shape shape = SimpleShapeFactory.create("CIRCLE");
        assertInstanceOf(Circle.class, shape);
    }

    @Test
    @DisplayName("Should be case-insensitive for rectangle")
    void shouldHandleMixedCaseRectangle() {
        Shape shape = SimpleShapeFactory.create("ReCtAnGlE");
        assertInstanceOf(Rectangle.class, shape);
    }

    @Test
    @DisplayName("Should create Triangle when type is 'triangle'")
    void shouldCreateTriangle() {
        Shape shape = SimpleShapeFactory.create("triangle");
        assertNotNull(shape, "Created shape should not be null");
        assertInstanceOf(FactoryExample.Triangle.class, shape, "Should be a Triangle instance");
    }

    @Test
    @DisplayName("Should throw exception for unknown shape type")
    void shouldThrowForUnknownType() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpleShapeFactory.create("pentagon"),
                "Unknown shape should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Should include shape name in exception message")
    void shouldIncludeShapeNameInException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> SimpleShapeFactory.create("pentagon"));
        assertTrue(ex.getMessage().contains("pentagon"),
                "Exception message should mention the unknown shape type");
    }

    @Test
    @DisplayName("Should create new instance each time (not singleton)")
    void shouldCreateNewInstances() {
        Shape s1 = SimpleShapeFactory.create("circle");
        Shape s2 = SimpleShapeFactory.create("circle");
        assertNotSame(s1, s2, "Factory should create a new object each time");
    }

    @Test
    @DisplayName("Should throw exception for empty string")
    void shouldThrowForEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> SimpleShapeFactory.create(""));
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void shouldThrowForNullInput() {
        assertThrows(NullPointerException.class,
                () -> SimpleShapeFactory.create(null),
                "Null input should cause NullPointerException from toLowerCase()");
    }

    @Test
    @DisplayName("All created shapes should implement Shape interface")
    void allShapesShouldImplementShape() {
        Shape circle = SimpleShapeFactory.create("circle");
        Shape rectangle = SimpleShapeFactory.create("rectangle");
        assertInstanceOf(Shape.class, circle);
        assertInstanceOf(Shape.class, rectangle);
    }
}
