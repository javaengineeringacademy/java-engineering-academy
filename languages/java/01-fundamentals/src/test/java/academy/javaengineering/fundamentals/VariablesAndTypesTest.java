package academy.javaengineering.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link VariablesAndTypes}.
 */
class VariablesAndTypesTest {

    @Test
    @DisplayName("Primitive byte has correct size and range")
    void testByteRange() {
        byte min = Byte.MIN_VALUE;
        byte max = Byte.MAX_VALUE;
        assertEquals(-128, min);
        assertEquals(127, max);
    }

    @Test
    @DisplayName("Primitive int has correct size and range")
    void testIntRange() {
        assertEquals(-2147483648, Integer.MIN_VALUE);
        assertEquals(2147483647, Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Primitive long has correct size")
    void testLongRange() {
        assertEquals(8, Long.BYTES);
        assertEquals(Long.MIN_VALUE, -9223372036854775808L);
        assertEquals(Long.MAX_VALUE, 9223372036854775807L);
    }

    @Test
    @DisplayName("Primitive double has correct size")
    void testDoubleSize() {
        assertEquals(8, Double.BYTES);
        assertEquals(64, Double.SIZE);
    }

    @Test
    @DisplayName("Primitive float has correct size")
    void testFloatSize() {
        assertEquals(4, Float.BYTES);
        assertEquals(32, Float.SIZE);
    }

    @Test
    @DisplayName("Autoboxing and unboxing work correctly")
    void testAutoboxing() {
        Integer boxed = 42;
        int unboxed = boxed;
        assertEquals(42, unboxed);
        assertEquals(Integer.valueOf(42), boxed);
    }

    @Test
    @DisplayName("Wrapper class parsing works")
    void testWrapperParsing() {
        assertEquals(123, Integer.parseInt("123"));
        assertEquals(3.14, Double.parseDouble("3.14"), 0.001);
        assertEquals(true, Boolean.parseBoolean("true"));
        assertEquals(false, Boolean.parseBoolean("anything"));
    }

    @Test
    @DisplayName("Wrapper class valueOf works")
    void testWrapperValueOf() {
        assertEquals(Integer.valueOf(42), Integer.valueOf("42"));
        assertEquals(Double.valueOf(3.14), Double.valueOf("3.14"), 0.001);
        assertEquals(Long.valueOf(100L), Long.valueOf("100"));
    }

    @Test
    @DisplayName("Widening casting works implicitly")
    void testWideningCast() {
        byte b = 10;
        int i = b;
        long l = i;
        float f = l;
        double d = f;

        assertEquals(10, i);
        assertEquals(10L, l);
        assertEquals(10.0f, f);
        assertEquals(10.0, d);
    }

    @Test
    @DisplayName("Narrowing casting requires explicit cast")
    void testNarrowingCast() {
        double pi = 3.14159;
        int truncated = (int) pi;
        assertEquals(3, truncated);

        int big = 256;
        byte overflow = (byte) big;
        assertEquals(0, overflow);
    }

    @Test
    @DisplayName("Character wrapper methods work correctly")
    void testCharacterMethods() {
        assertTrue(Character.isLetter('A'));
        assertTrue(Character.isDigit('5'));
        assertFalse(Character.isLetterOrDigit(' '));
        assertEquals('a', Character.toLowerCase('A'));
        assertEquals('Z', Character.toUpperCase('z'));
    }

    @Test
    @DisplayName("Integer utility methods work correctly")
    void testIntegerMethods() {
        assertEquals("ff", Integer.toHexString(255));
        assertEquals("11111111", Integer.toBinaryString(255));
        assertEquals(8, Integer.bitCount(255));
        assertTrue(Integer.MAX_VALUE > 0);
    }

    @Test
    @DisplayName("Char to int conversion gives ASCII value")
    void testCharToAscii() {
        char ch = 'A';
        int ascii = ch;
        assertEquals(65, ascii);

        char fromAscii = (char) 97;
        assertEquals('a', fromAscii);
    }

    @Test
    @DisplayName("Boolean wrapper class works correctly")
    void testBooleanWrapper() {
        Boolean b1 = true;
        Boolean b2 = false;
        assertTrue(b1);
        assertFalse(b2);
        assertEquals(Boolean.TRUE, b1);
        assertEquals(Boolean.FALSE, b2);
    }

    @Test
    @DisplayName("VariablesAndTypes constructor creates valid object")
    void testConstructor() {
        VariablesAndTypes obj = new VariablesAndTypes("Test", 25);
        assertEquals("Test", obj.getName());
        assertEquals(25, obj.getAge());
    }

    @Test
    @DisplayName("VariablesAndTypes setters update fields")
    void testSetters() {
        VariablesAndTypes obj = new VariablesAndTypes("Original", 20);
        obj.setName("Updated");
        obj.setAge(30);
        assertEquals("Updated", obj.getName());
        assertEquals(30, obj.getAge());
    }

    @Test
    @DisplayName("Constants are accessible and correct")
    void testConstants() {
        assertEquals(3.141592653589793, VariablesAndTypes.PI, 0.000001);
        assertEquals("Hello, Java!", VariablesAndTypes.GREETING);
        assertEquals(Integer.MAX_VALUE, VariablesAndTypes.MAX_VALUE);
    }
}
