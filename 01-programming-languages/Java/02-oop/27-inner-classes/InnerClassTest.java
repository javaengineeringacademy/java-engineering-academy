import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Inner Class Tests")
class InnerClassTest {

    private Outer outer;

    @BeforeEach
    void setUp() {
        outer = new Outer("Hello");
    }

    @Test
    @DisplayName("Inner class created from outer instance")
    void innerCreation() {
        Outer.Inner inner = outer.new Inner();
        assertNotNull(inner);
    }

    @Test
    @DisplayName("Inner class accesses outer field")
    void innerAccess() {
        Outer.Inner inner = outer.new Inner();
        assertTrue(inner.getInnerInfo().contains("Hello"));
    }

    @Test
    @DisplayName("Inner class can reference outer class")
    void innerOuterReference() {
        Outer.Inner inner = outer.new Inner();
        assertEquals("Outer", inner.getOuterClass());
    }

    @Test
    @DisplayName("Static nested class creation")
    void staticNestedCreation() {
        Outer.StaticNested nested = new Outer.StaticNested("World");
        assertNotNull(nested);
        assertEquals("StaticNested: World", nested.getInfo());
    }

    @Test
    @DisplayName("Static nested class independent of outer")
    void staticNestedIndependent() {
        Outer.StaticNested nested1 = new Outer.StaticNested("A");
        Outer.StaticNested nested2 = new Outer.StaticNested("B");
        assertNotSame(nested1, nested2);
    }

    @Test
    @DisplayName("Outer field accessible")
    void outerField() {
        assertEquals("Hello", outer.getOuterField());
    }
}