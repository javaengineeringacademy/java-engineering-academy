package academy.javaengineering.interview;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JavaInterviewQuestionsTest {

    private final JavaInterviewQuestions q = new JavaInterviewQuestions();

    @Test
    void shouldIdentifyPrime() {
        assertTrue(q.isPrime(17));
        assertFalse(q.isPrime(1));
        assertFalse(q.isPrime(4));
    }

    @Test
    void shouldReverseString() {
        assertEquals("olleh", q.reverseString("hello"));
        assertEquals("", q.reverseString(""));
    }

    @Test
    void shouldCheckPalindrome() {
        assertTrue(q.isPalindrome("racecar"));
        assertFalse(q.isPalindrome("hello"));
    }

    @Test
    void shouldCalculateFibonacci() {
        assertEquals(0, q.fibonacci(0));
        assertEquals(1, q.fibonacci(1));
        assertEquals(55, q.fibonacci(10));
    }

    @Test
    void shouldFindDuplicates() {
        assertEquals(List.of(2, 3), q.findDuplicates(new int[]{1, 2, 3, 2, 4, 3}));
    }
}
