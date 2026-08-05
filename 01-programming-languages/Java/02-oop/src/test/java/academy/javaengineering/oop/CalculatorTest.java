package academy.javaengineering.oop;

import academy.javaengineering.oop.methodoverloading.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator Overloading Tests")
class CalculatorTest {

    private Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    @Test
    @DisplayName("Add two ints")
    void addInts() {
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    @DisplayName("Add two doubles")
    void addDoubles() {
        assertEquals(5.5, calc.add(2.3, 3.2), 0.001);
    }

    @Test
    @DisplayName("Add three ints")
    void addThreeInts() {
        assertEquals(10, calc.add(2, 3, 5));
    }

    @Test
    @DisplayName("Concatenate strings")
    void addStrings() {
        assertEquals("Hello World", calc.add("Hello ", "World"));
    }

    @Test
    @DisplayName("Varargs sum")
    void varargsSum() {
        assertEquals(15, calc.add(1, 2, 3, 4, 5));
        assertEquals(0, calc.add());
    }

    @Test
    @DisplayName("Method resolution works correctly")
    void methodResolution() {
        assertEquals(5, calc.add(2, 3));
        assertEquals(5.5, calc.add(2.0, 3.5), 0.001);
    }
}
