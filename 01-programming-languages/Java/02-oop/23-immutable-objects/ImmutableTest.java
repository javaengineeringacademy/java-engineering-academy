import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ImmutablePoint Tests")
class ImmutableTest {

    private ImmutablePoint point;

    @BeforeEach
    void setUp() {
        point = new ImmutablePoint(3, 4);
    }

    @Test
    @DisplayName("Constructor sets coordinates")
    void constructor() {
        assertEquals(3, point.getX());
        assertEquals(4, point.getY());
    }

    @Test
    @DisplayName("translate returns new point")
    void translate() {
        ImmutablePoint moved = point.translate(2, 3);
        assertEquals(5, moved.getX());
        assertEquals(7, moved.getY());
        assertEquals(3, point.getX()); // Original unchanged
    }

    @Test
    @DisplayName("distanceTo calculates distance")
    void distanceTo() {
        ImmutablePoint other = new ImmutablePoint(0, 0);
        assertEquals(5.0, point.distanceTo(other), 0.001);
    }

    @Test
    @DisplayName("equals and hashCode work")
    void equality() {
        ImmutablePoint same = new ImmutablePoint(3, 4);
        ImmutablePoint different = new ImmutablePoint(1, 2);

        assertEquals(point, same);
        assertEquals(point.hashCode(), same.hashCode());
        assertNotEquals(point, different);
    }

    @Test
    @DisplayName("toString returns coordinate format")
    void toStringFormat() {
        assertEquals("(3, 4)", point.toString());
    }

    @Test
    @DisplayName("Class is final")
    void isFinal() {
        assertTrue(java.lang.reflect.Modifier.isFinal(ImmutablePoint.class.getModifiers()));
    }

    @Test
    @DisplayName("Fields are final")
    void fieldsAreFinal() throws Exception {
        var xField = ImmutablePoint.class.getDeclaredField("x");
        var yField = ImmutablePoint.class.getDeclaredField("y");
        assertTrue(java.lang.reflect.Modifier.isFinal(xField.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(yField.getModifiers()));
    }
}