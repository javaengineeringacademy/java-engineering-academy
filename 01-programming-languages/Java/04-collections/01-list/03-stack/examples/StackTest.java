package list.stack.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class StackTest {

    @Test
    void testAddAndSize() {
        Stack<String> stack = new Stack<>();
        stack.push("Java");
        stack.push("Python");
        stack.push("C++");
        assertEquals(3, stack.size());
    }

    @Test
    void testPushPopPeek() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        assertEquals(30, stack.peek());
        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(1, stack.size());
    }

    @Test
    void testContains() {
        Stack<String> stack = new Stack<>(Arrays.asList("A", "B", "C"));
        assertTrue(stack.contains("B"));
        assertFalse(stack.contains("Z"));
    }

    @Test
    void testIteration() {
        Stack<Integer> stack = new Stack<>(Arrays.asList(1, 2, 3, 4, 5));
        int sum = 0;
        for (int n : stack) sum += n;
        assertEquals(15, sum);
    }

    @Test
    void testEdgeCases() {
        Stack<String> stack = new Stack<>();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    void testSearch() {
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        assertEquals(2, stack.search("B"));
        assertEquals(-1, stack.search("Z"));
    }

    @Test
    void testEmpty() {
        Stack<String> stack = new Stack<>();
        assertTrue(stack.empty());
        stack.push("A");
        assertFalse(stack.empty());
    }

    @Test
    void testPopOnEmptyThrows() {
        Stack<String> stack = new Stack<>();
        assertThrows(EmptyStackException.class, stack::pop);
    }

    @Test
    void testPeekOnEmptyThrows() {
        Stack<String> stack = new Stack<>();
        assertThrows(EmptyStackException.class, stack::peek);
    }
}
