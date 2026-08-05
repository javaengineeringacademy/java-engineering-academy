import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MethodDemo Tests")
class MethodDemoTest {

    private MethodDemo obj;

    @BeforeEach
    void setUp() {
        obj = new MethodDemo("Alice");
    }

    @Test
    @DisplayName("Instance method returns correct greeting")
    void instanceMethod() {
        assertEquals("Hello, I'm Alice", obj.greet());
    }

    @Test
    @DisplayName("Overloaded method with custom greeting")
    void overloadedMethod() {
        assertEquals("Hi, I'm Alice", obj.greet("Hi"));
    }

    @Test
    @DisplayName("Static method works without instance")
    void staticMethod() {
        int count = MethodDemo.getInstanceCount();
        assertTrue(count >= 0);
    }

    @Test
    @DisplayName("Static add works with ints and doubles")
    void staticOverloading() {
        assertEquals(8, MethodDemo.add(5, 3));
        assertEquals(6.2, MethodDemo.add(2.5, 3.7), 0.001);
    }

    @Test
    @DisplayName("Varargs method sums multiple values")
    void varargs() {
        assertEquals(15, MethodDemo.sum(1, 2, 3, 4, 5));
        assertEquals(0, MethodDemo.sum());
        assertEquals(10, MethodDemo.sum(10));
    }

    @Test
    @DisplayName("Final method returns class name")
    void finalMethod() {
        assertEquals("MethodDemo", obj.getClassName());
    }

    @Test
    @DisplayName("getName returns constructor value")
    void getName() {
        assertEquals("Alice", obj.getName());
    }
}