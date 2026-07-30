package com.javaacademy.sprint1.operators;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArithmeticOperatorsTest {

    @Test
    void testBasicOperations() {
        assertEquals(13, 10 + 3);
        assertEquals(7, 10 - 3);
        assertEquals(30, 10 * 3);
        assertEquals(3, 10 / 3);  // Integer division!
        assertEquals(1, 10 % 3);
    }

    @Test
    void testIntegerDivision() {
        assertEquals(3, 10 / 3);
        assertEquals(3.3333333333333335, 10.0 / 3);
        assertEquals(3.3333333333333335, 10 / 3.0);
    }

    @Test
    void testModulusWithNegatives() {
        assertEquals(1, 10 % 3);
        assertEquals(-1, -10 % 3);
        assertEquals(1, 10 % -3);
        assertEquals(-1, -10 % -3);
    }

    @Test
    void testIncrementDecrement() {
        int x = 5;
        assertEquals(6, ++x);  // pre-increment
        x = 5;
        assertEquals(5, x++);  // post-increment
        assertEquals(6, x);
    }

    @Test
    void testCompoundAssignment() {
        int z = 10;
        z += 5; assertEquals(15, z);
        z -= 3; assertEquals(12, z);
        z *= 2; assertEquals(24, z);
        z /= 4; assertEquals(6, z);
        z %= 5; assertEquals(1, z);
    }

    @Test
    void testOverflow() {
        assertEquals(Integer.MIN_VALUE, Integer.MAX_VALUE + 1);
        assertEquals(Integer.MAX_VALUE, Integer.MIN_VALUE - 1);
    }
}

class RelationalOperatorsTest {

    @Test
    void testComparisons() {
        assertFalse(10 == 20);
        assertTrue(10 != 20);
        assertFalse(10 > 20);
        assertTrue(10 < 20);
        assertFalse(10 >= 20);
        assertTrue(10 <= 20);
    }

    @Test
    void testFloatPrecision() {
        double d1 = 3.14;
        double d2 = 3.1400000000000001;
        assertFalse(d1 == d2);
        assertTrue(Math.abs(d1 - d2) < 1e-10);
    }

    @Test
    void testStringComparison() {
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");

        assertTrue(s1 == s2);
        assertFalse(s1 == s3);
        assertTrue(s1.equals(s3));
    }

    @Test
    void testNaN() {
        double nan = Double.NaN;
        assertFalse(nan == nan);
        assertTrue(Double.isNaN(nan));
    }
}

class LogicalOperatorsTest {

    @Test
    void testShortCircuit() {
        boolean called = false;
        boolean result = false && (called = true);
        assertFalse(result);
        assertFalse(called);  // Short-circuited!

        called = false;
        result = true || (called = true);
        assertTrue(result);
        assertFalse(called);  // Short-circuited!
    }

    @Test
    void testNonShortCircuit() {
        boolean called = false;
        boolean result = false & (called = true);
        assertFalse(result);
        assertTrue(called);  // Evaluated!

        called = false;
        result = true | (called = true);
        assertTrue(result);
        assertTrue(called);  // Evaluated!
    }

    @Test
    void testNullSafe() {
        String str = null;
        assertFalse(str != null && str.length() > 0);
    }

    @Test
    void testXor() {
        assertFalse(true ^ true);
        assertTrue(true ^ false);
        assertTrue(false ^ true);
        assertFalse(false ^ false);
    }
}

class BitwiseOperatorsTest {

    @Test
    void testBasic() {
        assertEquals(1, 5 & 3);   // 0101 & 0011 = 0001
        assertEquals(7, 5 | 3);   // 0101 | 0011 = 0111
        assertEquals(6, 5 ^ 3);   // 0101 ^ 0011 = 0110
        assertEquals(-6, ~5);     // ~0101 = ...1010 (two's complement)
    }

    @Test
    void testShifts() {
        assertEquals(10, 5 << 1);   // 5 * 2
        assertEquals(20, 5 << 2);   // 5 * 4
        assertEquals(2, 5 >> 1);    // 5 / 2
        
        int neg = -8;
        assertEquals(-4, neg >> 1);   // Sign preserved
        assertEquals(2147483644, neg >>> 1);  // Zero-filled
    }

    @Test
    void testFlags() {
        final int READ = 1 << 0;    // 0001
        final int WRITE = 1 << 1;   // 0010
        final int EXECUTE = 1 << 2; // 0100

        int perms = READ | WRITE;  // 0011 = 3
        assertTrue((perms & READ) != 0);
        assertTrue((perms & WRITE) != 0);
        assertFalse((perms & EXECUTE) != 0);

        perms |= EXECUTE;  // 0111 = 7
        assertTrue((perms & EXECUTE) != 0);

        perms &= ~WRITE;  // 0101 = 5
        assertFalse((perms & WRITE) != 0);
    }

    @Test
    void testXorSwap() {
        int x = 10, y = 20;
        x ^= y;
        y ^= x;
        x ^= y;
        assertEquals(20, x);
        assertEquals(10, y);
    }
}

class AssignmentOperatorsTest {

    @Test
    void testSimpleAssignment() {
        int x = 10;
        int y = (x = 20);
        assertEquals(20, x);
        assertEquals(20, y);
    }

    @Test
    void testChainedAssignment() {
        int a, b, c;
        a = b = c = 5;
        assertEquals(5, a);
        assertEquals(5, b);
        assertEquals(5, c);
    }

    @Test
    void testImplicitNarrowing() {
        byte b = 10;
        b += 5;  // OK: implicit cast
        assertEquals(15, b);

        short s = 100;
        s *= 2;
        assertEquals(200, s);
    }
}