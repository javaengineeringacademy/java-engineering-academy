package academy.javaengineering.interview;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CollectionsInterviewQuestionsTest {

    private final CollectionsInterviewQuestions q = new CollectionsInterviewQuestions();

    @Test
    void shouldCheckAnagram() {
        assertTrue(q.isAnagram("listen", "silent"));
        assertFalse(q.isAnagram("hello", "world"));
    }

    @Test
    void shouldFindIntersection() {
        List<Integer> result = q.intersection(List.of(1, 2, 3, 4), List.of(3, 4, 5, 6));
        assertEquals(List.of(3, 4), result);
    }

    @Test
    void shouldCalculateFrequency() {
        var freq = q.frequencyMap(List.of("a", "b", "a", "c"));
        assertEquals(2L, freq.get("a"));
        assertEquals(1L, freq.get("b"));
    }
}
