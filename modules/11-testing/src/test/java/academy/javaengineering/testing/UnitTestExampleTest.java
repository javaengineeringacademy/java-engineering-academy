package academy.javaengineering.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitTestExampleTest {

    private final UnitTestExample example = new UnitTestExample();

    @Test
    void shouldCalculateFactorial() {
        assertEquals(120, example.factorial(5));
        assertEquals(1, example.factorial(0));
        assertEquals(1, example.factorial(1));
    }

    @Test
    void shouldThrowExceptionForNegativeFactorial() {
        assertThrows(IllegalArgumentException.class, () -> example.factorial(-1));
    }

    @Test
    void shouldCalculateFibonacci() {
        assertEquals(0, example.fibonacci(0));
        assertEquals(1, example.fibonacci(1));
        assertEquals(13, example.fibonacci(7));
    }

    @Test
    void shouldIdentifyPrime() {
        assertTrue(example.isPrime(13));
        assertTrue(example.isPrime(2));
        assertFalse(example.isPrime(1));
        assertFalse(example.isPrime(4));
    }

    @Test
    void shouldFilterEvenNumbers() {
        assertEquals(List.of(2, 4, 6), example.filterEven(List.of(1, 2, 3, 4, 5, 6)));
    }
}
