package academy.javaengineering.oop.polymorphism;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for polymorphism demonstrations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
class PolymorphismTest {

    @Test
    void shouldCalculateCircleArea() {
        Shape circle = new Circle(5.0);
        assertEquals(Math.PI * 25, circle.getArea(), 0.001);
    }

    @Test
    void shouldCalculateRectangleArea() {
        Shape rect = new Rectangle(4.0, 6.0);
        assertEquals(24.0, rect.getArea(), 0.001);
    }

    @Test
    void shouldCalculateTriangleArea() {
        Shape triangle = new Triangle(3.0, 4.0, 5.0);
        assertEquals(6.0, triangle.getArea(), 0.001);
    }

    @Test
    void shouldSupportPolymorphicArray() {
        Shape[] shapes = {
            new Circle(5.0),
            new Rectangle(4.0, 6.0),
            new Triangle(3.0, 4.0, 5.0)
        };
        
        double totalArea = 0;
        for (Shape shape : shapes) {
            totalArea += shape.getArea();
        }
        
        assertTrue(totalArea > 0);
        assertEquals(3, shapes.length);
    }

    @Test
    void shouldOverloadMethods() {
        Calculator calc = new Calculator();
        
        assertEquals(8, calc.add(5, 3));
        assertEquals(8.7, calc.add(5.5, 3.2), 0.001);
        assertEquals(6, calc.add(1, 2, 3));
        assertEquals("Hello World", calc.add("Hello", " World"));
    }

    @Test
    void shouldSupportPatternMatching() {
        Shape circle = new Circle(5.0);
        
        if (circle instanceof Circle c) {
            assertEquals(5.0, c.getRadius(), 0.001);
        } else {
            fail("Pattern matching failed");
        }
    }

    @Test
    void shouldValidateTriangleSides() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Triangle(1, 2, 10)); // Invalid triangle
    }
}