import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Functional Interface Tests")
class FunctionalInterfaceTest {

    private MyFunction<String, Integer> lengthFunction;

    @BeforeEach
    void setUp() {
        lengthFunction = String::length;
    }

    @Test
    @DisplayName("apply works")
    void apply() {
        assertEquals(5, lengthFunction.apply("Hello"));
    }

    @Test
    @DisplayName("andThen chains functions")
    void andThen() {
        MyFunction<String, Boolean> isEmpty = s -> s.isEmpty();
        MyFunction<String, Boolean> check = lengthFunction.andThen(len -> len == 0);
        assertTrue(check.apply(""));
        assertFalse(check.apply("Hello"));
    }

    @Test
    @DisplayName("compose chains functions")
    void compose() {
        MyFunction<String, Integer> strLength = s -> s.length();
        MyFunction<Integer, Integer> doubleIt = n -> n * 2;
        MyFunction<String, Integer> combined = doubleIt.compose(strLength);
        assertEquals(10, combined.apply("Hello"));
    }

    @Test
    @DisplayName("identity returns same input")
    void identity() {
        MyFunction<String, String> id = MyFunction.identity();
        assertEquals("test", id.apply("test"));
        assertEquals(42, MyFunction.<Integer>identity().apply(42));
    }

    @Test
    @DisplayName("Lambda implementation")
    void lambdaImpl() {
        MyFunction<Integer, String> toString = n -> "Number: " + n;
        assertEquals("Number: 5", toString.apply(5));
    }

    @Test
    @DisplayName("Method reference implementation")
    void methodReference() {
        MyFunction<String, String> upper = String::toUpperCase;
        assertEquals("HELLO", upper.apply("Hello"));
    }
}