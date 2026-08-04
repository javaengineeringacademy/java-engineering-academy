package academy.javaengineering.fundamentals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Methods}.
 */
class MethodsTest {

    @Test
    @DisplayName("add method returns correct sum")
    void testAdd() {
        assertEquals(30, Methods.add(10, 20));
        assertEquals(0, Methods.add(-5, 5));
        assertEquals(-10, Methods.add(-3, -7));
    }

    @Test
    @DisplayName("greet method produces output")
    void testGreet() {
        // greet is void, just verify it doesn't throw
        assertDoesNotThrow(() -> Methods.greet("Test"));
    }

    @Test
    @DisplayName("findMax returns the larger value")
    void testFindMax() {
        assertEquals(20, Methods.findMax(10, 20));
        assertEquals(20, Methods.findMax(20, 10));
        assertEquals(10, Methods.findMax(10, 10));
    }

    @Test
    @DisplayName("findMax handles negative numbers")
    void testFindMaxNegative() {
        assertEquals(-1, Methods.findMax(-5, -1));
        assertEquals(0, Methods.findMax(-10, 0));
    }

    @Test
    @DisplayName("getRange creates correct array")
    void testGetRange() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Methods.getRange(1, 5));
        assertArrayEquals(new int[]{5}, Methods.getRange(5, 5));
        assertArrayEquals(new int[]{10, 11, 12}, Methods.getRange(10, 12));
    }

    @Test
    @DisplayName("findMinAndMax returns correct values")
    void testFindMinAndMax() {
        int[] result = Methods.findMinAndMax(new int[]{3, 1, 4, 1, 5, 9, 2, 6});
        assertEquals(1, result[0]);
        assertEquals(9, result[1]);
    }

    @Test
    @DisplayName("validateAge returns correct category")
    void testValidateAge() {
        assertEquals("Minor", Methods.validateAge(15));
        assertEquals("Adult", Methods.validateAge(18));
        assertEquals("Adult", Methods.validateAge(65));
        assertEquals("Invalid: negative age", Methods.validateAge(-1));
    }

    @Test
    @DisplayName("Sum varargs works with different argument counts")
    void testSumVarargs() {
        assertEquals(0, Methods.sum());
        assertEquals(5, Methods.sum(5));
        assertEquals(15, Methods.sum(1, 2, 3, 4, 5));
        assertEquals(0, Methods.sum(1, -1, 2, -2));
    }

    @Test
    @DisplayName("printStudentInfo handles varargs correctly")
    void testPrintStudentInfo() {
        // Just verify it doesn't throw
        assertDoesNotThrow(() -> Methods.printStudentInfo("Alice", 95, 88, 92));
        assertDoesNotThrow(() -> Methods.printStudentInfo("Bob"));
    }

    @Test
    @DisplayName("average calculates correctly with varargs")
    void testAverage() {
        assertEquals(3.0, Methods.average(1, 2, 3, 4, 5), 0.001);
        assertEquals(5.0, Methods.average(5));
        assertEquals(0.0, Methods.average(), 0.001);
    }

    @Test
    @DisplayName("Overloaded multiply works with different types")
    void testOverloadedMultiply() {
        assertEquals(12, Methods.multiply(3, 4));
        assertEquals(7.0, Methods.multiply(3.5, 2.0), 0.001);
        assertEquals("HaHaHa", Methods.multiply("Ha", 3));
    }

    @Test
    @DisplayName("Overloaded max works with different parameter counts")
    void testOverloadedMax() {
        assertEquals(20, Methods.max(10, 20));
        assertEquals(30, Methods.max(10, 20, 30));
        assertEquals(40, Methods.max(10, 20, 30, 40));
    }

    @Test
    @DisplayName("Factorial calculates correctly")
    void testFactorial() {
        assertEquals(1, Methods.factorial(0));
        assertEquals(1, Methods.factorial(1));
        assertEquals(120, Methods.factorial(5));
        assertEquals(3628800, Methods.factorial(10));
    }

    @Test
    @DisplayName("Fibonacci calculates correctly")
    void testFibonacci() {
        assertEquals(0, Methods.fibonacci(0));
        assertEquals(1, Methods.fibonacci(1));
        assertEquals(1, Methods.fibonacci(2));
        assertEquals(5, Methods.fibonacci(5));
        assertEquals(55, Methods.fibonacci(10));
    }

    @Test
    @DisplayName("Power calculates correctly")
    void testPower() {
        assertEquals(1, Methods.power(2, 0));
        assertEquals(8, Methods.power(2, 3));
        assertEquals(1024, Methods.power(2, 10));
        assertEquals(81, Methods.power(3, 4));
    }

    @Test
    @DisplayName("Reverse string works correctly")
    void testReverse() {
        assertEquals("olleh", Methods.reverse("hello"));
        assertEquals("a", Methods.reverse("a"));
        assertEquals("", Methods.reverse(""));
        assertEquals("dcba", Methods.reverse("abcd"));
    }

    @Test
    @DisplayName("IsPalindrome correctly identifies palindromes")
    void testIsPalindrome() {
        assertTrue(Methods.isPalindrome("racecar"));
        assertTrue(Methods.isPalindrome("level"));
        assertTrue(Methods.isPalindrome("a"));
        assertTrue(Methods.isPalindrome(""));
        assertFalse(Methods.isPalindrome("hello"));
        assertFalse(Methods.isPalindrome("ab"));
    }

    @Test
    @DisplayName("Sum of digits calculates correctly")
    void testSumOfDigits() {
        assertEquals(15, Methods.sumOfDigits(12345));
        assertEquals(1, Methods.sumOfDigits(1));
        assertEquals(0, Methods.sumOfDigits(0));
        assertEquals(15, Methods.sumOfDigits(-12345));
    }

    @Test
    @DisplayName("Instance methods maintain state")
    void testInstanceMethods() {
        Methods obj1 = new Methods("Alice");
        Methods obj2 = new Methods("Bob");

        assertEquals("Alice", obj1.getName());
        assertEquals("Bob", obj2.getName());
        assertEquals(0, obj1.getCallCount());

        obj1.incrementCalls();
        obj1.incrementCalls();
        obj2.incrementCalls();

        assertEquals(2, obj1.getCallCount());
        assertEquals(1, obj2.getCallCount());
    }

    @Test
    @DisplayName("Static factory method creates valid object")
    void testStaticFactory() {
        Methods obj = Methods.create("Factory");
        assertEquals("Factory", obj.getName());
    }

    @Test
    @DisplayName("Find all indices returns correct positions")
    void testFindAll() {
        int[] arr = {1, 3, 5, 3, 7, 3, 9};
        // findAll prints to stdout, just verify it doesn't throw
        assertDoesNotThrow(() -> academy.javaengineering.fundamentals.ArraysDemo.findAll(arr, 3));
    }
}
