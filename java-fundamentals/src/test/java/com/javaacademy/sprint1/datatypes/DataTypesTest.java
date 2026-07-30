package com.javaacademy.sprint1.datatypes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTypesTest {

    @Test
    void testPrimitiveLimits() {
        assertEquals(-128, Byte.MIN_VALUE);
        assertEquals(127, Byte.MAX_VALUE);
        assertEquals(-32768, Short.MIN_VALUE);
        assertEquals(32767, Short.MAX_VALUE);
        assertEquals(-2147483648, Integer.MIN_VALUE);
        assertEquals(2147483647, Integer.MAX_VALUE);
    }

    @Test
    void testPrimitiveDefaults() {
        DefaultsDemo d = new DefaultsDemo();
        assertEquals(0, d.byteField);
        assertEquals(0, d.shortField);
        assertEquals(0, d.intField);
        assertEquals(0L, d.longField);
        assertEquals(0.0f, d.floatField);
        assertEquals(0.0d, d.doubleField);
        assertEquals('\u0000', d.charField);
        assertEquals(false, d.booleanField);
        assertNull(d.stringField);
    }

    @Test
    void testLiterals() {
        assertEquals(10, 0b1010);
        assertEquals(10, 012);
        assertEquals(255, 0xFF);
    }

    static class DefaultsDemo {
        byte byteField;
        short shortField;
        int intField;
        long longField;
        float floatField;
        double doubleField;
        char charField;
        boolean booleanField;
        String stringField;
    }
}

class ReferenceTypesTest {

    @Test
    void testStringEquality() {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");

        assertTrue(s1 == s2);
        assertFalse(s1 == s3);
        assertTrue(s1.equals(s3));
    }

    @Test
    void testWrapperAutoboxing() {
        Integer wrapped = 42;
        int unwrapped = wrapped;
        assertEquals(42, unwrapped);
    }

    @Test
    void testNullReference() {
        String nullRef = null;
        assertNull(nullRef);
        assertThrows(NullPointerException.class, () -> nullRef.length());
    }

    @Test
    void testArrayReference() {
        int[] a = {1, 2, 3};
        int[] b = a;
        int[] c = a.clone();

        assertTrue(a == b);
        assertFalse(a == c);
        assertArrayEquals(a, c);
    }
}

class TypeCastingTest {

    @Test
    void testWidening() {
        byte b = 100;
        short s = b;
        int i = s;
        long l = i;
        float f = l;
        double d = f;
        assertEquals(100.0, d);
    }

    @Test
    void testNarrowing() {
        double d = 123.456;
        float f = (float) d;
        long l = (long) f;
        int i = (int) l;
        short s = (short) i;
        byte b = (byte) s;
        assertEquals(123, b);
    }

    @Test
    void testOverflow() {
        int large = 130;
        byte overflow = (byte) large;
        assertEquals(-126, overflow);
    }

    @Test
    void testTruncation() {
        double pi = 3.14159;
        int truncated = (int) pi;
        assertEquals(3, truncated);
    }

    @Test
    void testCompoundAssignmentImplicitCast() {
        byte b = 10;
        b += 5;
        assertEquals(15, b);
    }
}