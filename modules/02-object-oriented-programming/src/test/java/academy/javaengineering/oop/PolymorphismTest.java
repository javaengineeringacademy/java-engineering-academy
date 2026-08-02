package academy.javaengineering.oop;

import academy.javaengineering.oop.`04-polymorphism`.PolymorphismExample.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Polymorphism Tests")
class PolymorphismTest {

    @Test
    @DisplayName("Circle area calculation")
    void circleArea() {
        Circle circle = new Circle(5.0);
        assertEquals(Math.PI * 25, circle.area(), 0.001);
        assertEquals(2 * Math.PI * 5, circle.perimeter(), 0.001);
        assertEquals("Circle", circle.getName());
    }

    @Test
    @DisplayName("Rectangle area and perimeter")
    void rectangleArea() {
        Rectangle rect = new Rectangle(4.0, 6.0);
        assertEquals(24.0, rect.area(), 0.001);
        assertEquals(20.0, rect.perimeter(), 0.001);
        assertFalse(rect.isSquare());
    }

    @Test
    @DisplayName("Square detection")
    void squareDetection() {
        Rectangle square = new Rectangle(5.0, 5.0);
        assertTrue(square.isSquare());
        assertEquals(25.0, square.area(), 0.001);
    }

    @Test
    @DisplayName("Triangle area (Heron's formula)")
    void triangleArea() {
        Triangle tri = new Triangle(3.0, 4.0, 5.0);
        assertEquals(6.0, tri.area(), 0.001);
        assertEquals(12.0, tri.perimeter(), 0.001);
    }

    @Test
    @DisplayName("Polymorphic total area calculation")
    void totalArea() {
        Shape[] shapes = {
                new Circle(1.0),        // area = pi
                new Rectangle(2.0, 3.0) // area = 6
        };
        double expected = Math.PI + 6.0;
        assertEquals(expected, ShapeAnalyzer.totalArea(shapes), 0.001);
    }

    @Test
    @DisplayName("Find largest shape")
    void findLargest() {
        Shape small = new Circle(1.0);
        Shape large = new Rectangle(10.0, 10.0);
        Shape[] shapes = {small, large};

        Shape largest = ShapeAnalyzer.findLargest(shapes);
        assertEquals(large, largest);
    }

    @Test
    @DisplayName("Shape details via pattern matching")
    void shapeDetails() {
        assertEquals("Circle(r=5.00)", ShapeAnalyzer.shapeDetails(new Circle(5.0)));
        assertEquals("Square(s=4.00)", ShapeAnalyzer.shapeDetails(new Rectangle(4.0, 4.0)));
        assertEquals("Rectangle(3.00 x 6.00)", ShapeAnalyzer.shapeDetails(new Rectangle(3.0, 6.0)));
    }

    @Test
    @DisplayName("Payment processor overloading")
    void paymentProcessor() {
        PaymentProcessor processor = new PaymentProcessor();
        String result1 = processor.processPayment(100.0);
        assertTrue(result1.contains("USD"));

        String result2 = processor.processPayment(100.0, "EUR");
        assertTrue(result2.contains("EUR"));

        String result3 = processor.processPayment(100.0, "USD", 10.0);
        assertTrue(result3.contains("discount"));
    }
}
