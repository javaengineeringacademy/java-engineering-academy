package academy.javaengineering.interview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Interview Preparation Tests")
class InterviewTest {

    @Test
    @DisplayName("Fibonacci should calculate correctly")
    void testFibonacci() {
        var algo = new InterviewAlgorithms();
        assertEquals(0, algo.fibonacci(0));
        assertEquals(1, algo.fibonacci(1));
        assertEquals(5, algo.fibonacci(5));
        assertEquals(55, algo.fibonacci(10));
    }

    @Test
    @DisplayName("Is anagram should work correctly")
    void testIsAnagram() {
        var algo = new InterviewAlgorithms();
        assertTrue(algo.isAnagram("listen", "silent"));
        assertTrue(algo.isAnagram("Astronomer", "Moon starer"));
        assertFalse(algo.isAnagram("hello", "world"));
    }

    @Test
    @DisplayName("Stack should work correctly")
    void testStack() {
        var stack = new DataStructures.Stack<Integer>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        
        assertEquals(3, stack.size());
        assertEquals(3, stack.peek());
        assertEquals(3, stack.pop());
        assertEquals(2, stack.peek());
    }
}
