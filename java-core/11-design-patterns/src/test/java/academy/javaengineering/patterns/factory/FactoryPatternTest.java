package academy.javaengineering.patterns.factory;

import academy.javaengineering.patterns.factory.FactoryExample.Shape;
import academy.javaengineering.patterns.factory.FactoryExample.Circle;
import academy.javaengineering.patterns.factory.FactoryExample.Rectangle;
import academy.javaengineering.patterns.factory.FactoryExample.ShapeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryPatternTest {

    @Test
    @DisplayName("Should create Circle when type is 'circle'")
    void shouldCreateCircle() {
        Shape shape = ShapeFactory.create("circle");
        assertNotNull(shape, "Created shape should not be null");
        assertInstanceOf(Circle.class, shape, "Should be a Circle instance");
    }

    @Test
    @DisplayName("Should create Rectangle when type is 'rectangle'")
    void shouldCreateRectangle() {
        Shape shape = ShapeFactory.create("rectangle");
        assertNotNull(shape, "Created shape should not be null");
        assertInstanceOf(Rectangle.class, shape, "Should be a Rectangle instance");
    }

    @Test
    @DisplayName("Should be case-insensitive for circle")
    void shouldHandleUpperCaseCircle() {
        Shape shape = ShapeFactory.create("CIRCLE");
        assertInstanceOf(Circle.class, shape);
    }

    @Test
    @DisplayName("Should be case-insensitive for rectangle")
    void shouldHandleMixedCaseRectangle() {
        Shape shape = ShapeFactory.create("ReCtAnGlE");
        assertInstanceOf(Rectangle.class, shape);
    }

    @Test
    @DisplayName("Should throw exception for unknown shape type")
    void shouldThrowForUnknownType() {
        assertThrows(IllegalArgumentException.class,
                () -> ShapeFactory.create("triangle"),
                "Unknown shape should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Should include shape name in exception message")
    void shouldIncludeShapeNameInException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ShapeFactory.create("pentagon"));
        assertTrue(ex.getMessage().contains("pentagon"),
                "Exception message should mention the unknown shape type");
    }

    @Test
    @DisplayName("Should create new instance each time (not singleton)")
    void shouldCreateNewInstances() {
        Shape s1 = ShapeFactory.create("circle");
        Shape s2 = ShapeFactory.create("circle");
        assertNotSame(s1, s2, "Factory should create a new object each time");
    }

    @Test
    @DisplayName("Should throw exception for empty string")
    void shouldThrowForEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> ShapeFactory.create(""));
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void shouldThrowForNullInput() {
        assertThrows(NullPointerException.class,
                () -> ShapeFactory.create(null),
                "Null input should cause NullPointerException from toLowerCase()");
    }

    @Test
    @DisplayName("All created shapes should implement Shape interface")
    void allShapesShouldImplementShape() {
        Shape circle = ShapeFactory.create("circle");
        Shape rectangle = ShapeFactory.create("rectangle");
        assertInstanceOf(Shape.class, circle);
        assertInstanceOf(Shape.class, rectangle);
    }
}
