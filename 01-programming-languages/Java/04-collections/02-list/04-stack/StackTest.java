import static org.junit.jupiter.api.Assertions.*;

import java.util.EmptyStackException;
import java.util.Stack;

import org.junit.jupiter.api.Test;

class StackTest {

    @Test
    void testPushAndPop() {
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        assertEquals("B", stack.pop());
        assertEquals("A", stack.pop());
    }

    @Test
    void testPeek() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    void testPeekDoesNotRemove() {
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.peek();
        assertEquals(1, stack.size());
    }

    @Test
    void testPopEmptyStackThrowsException() {
        Stack<String> stack = new Stack<>();
        assertThrows(EmptyStackException.class, stack::pop);
    }

    @Test
    void testPeekEmptyStackThrowsException() {
        Stack<String> stack = new Stack<>();
        assertThrows(EmptyStackException.class, stack::peek);
    }

    @Test
    void testEmpty() {
        Stack<String> stack = new Stack<>();
        assertTrue(stack.empty());
        stack.push("A");
        assertFalse(stack.empty());
    }

    @Test
    void testSearch() {
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        assertEquals(1, stack.search("C"));
        assertEquals(3, stack.search("A"));
        assertEquals(-1, stack.search("Z"));
    }

    @Test
    void testSize() {
        Stack<Integer> stack = new Stack<>();
        assertEquals(0, stack.size());
        stack.push(1);
        assertEquals(1, stack.size());
        stack.push(2);
        assertEquals(2, stack.size());
    }

    @Test
    void testLIFOOrder() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    void testPushMultipleElements() {
        Stack<String> stack = new Stack<>();
        stack.push("X");
        stack.push("Y");
        stack.push("Z");
        assertEquals(3, stack.size());
        assertEquals("Z", stack.pop());
    }

    @Test
    void testClear() {
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        stack.clear();
        assertEquals(0, stack.size());
        assertTrue(stack.empty());
    }
}
