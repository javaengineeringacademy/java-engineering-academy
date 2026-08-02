package academy.javaengineering.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodingInterviewExamplesTest {

    private final CodingInterviewExamples e = new CodingInterviewExamples();

    @Test
    void shouldFindTwoSum() {
        int[] result = e.twoSum(new int[]{2, 7, 11, 15}, 9);
        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    void shouldFindMaxSubArray() {
        assertEquals(6, e.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    @Test
    void shouldCalculateClimbingStairs() {
        assertEquals(5, e.climbingStairs(5));
        assertEquals(2, e.climbingStairs(2));
    }

    @Test
    void shouldDetectCycle() {
        CodingInterviewExamples.ListNode head = new CodingInterviewExamples.ListNode(1);
        head.next = new CodingInterviewExamples.ListNode(2);
        head.next.next = head;
        assertTrue(e.hasCycle(head));
    }

    @Test
    void shouldReturnFalseForNoCycle() {
        CodingInterviewExamples.ListNode head = new CodingInterviewExamples.ListNode(1);
        head.next = new CodingInterviewExamples.ListNode(2);
        assertFalse(e.hasCycle(head));
    }
}
