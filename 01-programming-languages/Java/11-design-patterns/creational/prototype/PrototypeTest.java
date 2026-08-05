package academy.javaengineering.patterns.creational;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrototypeTest {

    @Test
    void cloneCircleCreatesIndependentCopy() {
        Circle original = new Circle(5.0, "red");
        Circle cloned = original.clone();

        cloned.setColor("blue");
        cloned.setRadius(10.0);

        assertEquals("red", original.getColor());
        assertEquals(5.0, original.getRadius());
        assertEquals("blue", cloned.getColor());
        assertEquals(10.0, cloned.getRadius());
    }

    @Test
    void cloneRectangleCreatesIndependentCopy() {
        Rectangle original = new Rectangle(4.0, 6.0, "blue");
        Rectangle cloned = original.clone();

        cloned.setColor("green");
        cloned.setWidth(8.0);

        assertEquals("blue", original.getColor());
        assertEquals(4.0, original.getWidth());
        assertEquals("green", cloned.getColor());
        assertEquals(8.0, cloned.getWidth());
    }

    @Test
    void clonedObjectHasSameType() {
        Circle original = new Circle(3.0, "red");
        Shape cloned = original.clone();
        assertEquals("Circle", cloned.getType());
    }

    @Test
    void clonedCircleHasSameArea() {
        Circle original = new Circle(5.0, "red");
        Circle cloned = original.clone();
        assertEquals(original.area(), cloned.area());
    }

    @Test
    void clonedRectangleHasSameArea() {
        Rectangle original = new Rectangle(4.0, 6.0, "blue");
        Rectangle cloned = original.clone();
        assertEquals(original.area(), cloned.area());
    }
}
