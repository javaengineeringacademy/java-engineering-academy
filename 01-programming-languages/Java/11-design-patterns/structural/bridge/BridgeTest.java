package academy.javaengineering.patterns.structural.bridge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BridgeTest {

    @Test
    void testRedCircle() {
        Shape shape = new Circle(new RedColor());
        assertEquals("circle", shape.getShapeName());
    }

    @Test
    void testBlueSquare() {
        Shape shape = new Square(new BlueColor());
        assertEquals("square", shape.getShapeName());
    }

    @Test
    void testRedColor() {
        Color color = new RedColor();
        assertEquals("red", color.getColorName());
    }

    @Test
    void testBlueColor() {
        Color color = new BlueColor();
        assertEquals("blue", color.getColorName());
    }

    @Test
    void testShapeInterface() {
        Shape shape = new Circle(new RedColor());
        assertTrue(shape instanceof Shape);
    }
}
