package academy.javaengineering.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Operators}.
 */
class OperatorsTest {

    @Test
    @DisplayName("Arithmetic operations produce correct results")
    void testArithmeticOperators() {
        assertEquals(12, 7 + 5);
        assertEquals(2, 7 - 5);
        assertEquals(35, 7 * 5);
        assertEquals(1, 7 / 5);
        assertEquals(2, 7 % 5);
    }

    @Test
    @DisplayName("Integer division truncates toward zero")
    void testIntegerDivision() {
        assertEquals(2, 7 / 3);
        assertEquals(2, 8 / 3);
        assertEquals(3, 9 / 3);
    }

    @Test
    @DisplayName("Floating point division preserves precision")
    void testFloatingPointDivision() {
        assertEquals(2.333, 7.0 / 3.0, 0.001);
        assertEquals(2.5, 5.0 / 2.0, 0.001);
    }

    @Test
    @DisplayName("Modulo operation returns remainder")
    void testModulo() {
        assertEquals(1, 7 % 3);
        assertEquals(0, 9 % 3);
        assertEquals(2, 8 % 3);
    }

    @Test
    @DisplayName("Increment and decrement operators work correctly")
    void testIncrementDecrement() {
        int a = 5;
        assertEquals(5, a++);
        assertEquals(6, a);
        assertEquals(5, --a);
        assertEquals(5, a);
    }

    @Test
    @DisplayName("Relational operators return correct boolean values")
    void testRelationalOperators() {
        assertTrue(10 > 5);
        assertFalse(10 > 20);
        assertTrue(10 < 20);
        assertFalse(10 < 5);
        assertTrue(10 >= 10);
        assertTrue(10 <= 10);
        assertTrue(10 == 10);
        assertTrue(10 != 5);
    }

    @Test
    @DisplayName("Logical AND operator works correctly")
    void testLogicalAnd() {
        assertTrue(true && true);
        assertFalse(true && false);
        assertFalse(false && true);
        assertFalse(false && false);
    }

    @Test
    @DisplayName("Logical OR operator works correctly")
    void testLogicalOr() {
        assertTrue(true || true);
        assertTrue(true || false);
        assertTrue(false || true);
        assertFalse(false || false);
    }

    @Test
    @DisplayName("Logical NOT operator works correctly")
    void testLogicalNot() {
        assertFalse(!true);
        assertTrue(!false);
    }

    @Test
    @DisplayName("Short-circuit evaluation prevents exceptions")
    void testShortCircuit() {
        int x = 0;
        // This should NOT throw ArithmeticException due to short-circuit
        boolean result = (x != 0) && (10 / x > 1);
        assertFalse(result);
    }

    @Test
    @DisplayName("Bitwise AND works correctly")
    void testBitwiseAnd() {
        assertEquals(0b1000, 0b1010 & 0b1100);
        assertEquals(0, 0b1010 & 0b0101);
    }

    @Test
    @DisplayName("Bitwise OR works correctly")
    void testBitwiseOr() {
        assertEquals(0b1110, 0b1010 | 0b1100);
        assertEquals(0b1111, 0b1010 | 0b0101);
    }

    @Test
    @DisplayName("Bitwise XOR works correctly")
    void testBitwiseXor() {
        assertEquals(0b0110, 0b1010 ^ 0b1100);
        assertEquals(0b1111, 0b1010 ^ 0b0101);
    }

    @Test
    @DisplayName("Left and right shift work correctly")
    void testShiftOperators() {
        assertEquals(32, 16 << 1);
        assertEquals(4, 16 >> 2);
        assertEquals(4, 16 >>> 2);
    }

    @Test
    @DisplayName("Ternary operator selects correct value")
    void testTernaryOperator() {
        int a = 10, b = 20;
        assertEquals(20, (a > b) ? a : b);
        assertEquals(10, (a < b) ? a : b);
    }

    @Test
    @DisplayName("Compound assignment operators work correctly")
    void testCompoundAssignment() {
        int x = 10;
        x += 5;
        assertEquals(15, x);
        x -= 3;
        assertEquals(12, x);
        x *= 2;
        assertEquals(24, x);
        x /= 4;
        assertEquals(6, x);
        x %= 4;
        assertEquals(2, x);
    }

    @Test
    @DisplayName("Bitwise assignment operators work correctly")
    void testBitwiseAssignment() {
        int x = 0b1010;
        x &= 0b1100;
        assertEquals(0b1000, x);
        x |= 0b0011;
        assertEquals(0b1011, x);
        x ^= 0b0001;
        assertEquals(0b1010, x);
    }

    @Test
    @DisplayName("instanceof returns true for correct type")
    void testInstanceof() {
        Object str = "Hello";
        Object num = 42;
        assertTrue(str instanceof String);
        assertTrue(num instanceof Integer);
        assertFalse(str instanceof Integer);
    }

    @Test
    @DisplayName("Pattern matching instanceof extracts value")
    void testPatternMatching() {
        Object obj = "Hello, World!";
        if (obj instanceof String s) {
            assertEquals("HELLO, WORLD!", s.toUpperCase());
        } else {
            fail("Should have matched String pattern");
        }
    }

    @Test
    @DisplayName("instanceof with condition works in pattern matching")
    void testPatternMatchingWithCondition() {
        Object obj = 42;
        if (obj instanceof Integer i && i > 10) {
            assertEquals(42, i);
        } else {
            fail("Should have matched Integer pattern with condition");
        }
    }

    @Test
    @DisplayName("instanceof does not match null")
    void testInstanceofNull() {
        Object nullObj = null;
        assertFalse(nullObj instanceof String);
    }
}
