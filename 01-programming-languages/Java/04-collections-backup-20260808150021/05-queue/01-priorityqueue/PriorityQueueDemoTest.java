import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class PriorityQueueDemoTest {

    @Test
    void testPollAscendingOrder() {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(2);
        minHeap.offer(8);
        minHeap.offer(1);

        assertEquals(1, minHeap.poll());
        assertEquals(2, minHeap.poll());
        assertEquals(5, minHeap.poll());
        assertEquals(8, minHeap.poll());
    }

    @Test
    void testHeapProperty() {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(10);
        minHeap.offer(3);
        minHeap.offer(7);
        minHeap.offer(1);

        assertEquals(Integer.valueOf(1), minHeap.peek());
        minHeap.offer(0);
        assertEquals(Integer.valueOf(0), minHeap.peek());
    }

    @Test
    void testPollDescendingOrder() {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(2);
        maxHeap.offer(8);
        maxHeap.offer(1);

        assertEquals(8, maxHeap.poll());
        assertEquals(5, maxHeap.poll());
        assertEquals(2, maxHeap.poll());
        assertEquals(1, maxHeap.poll());
    }

    @Test
    void testFindKthLargest() {
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        int result = PriorityQueueDemo.findKthLargest(nums, k);
        assertEquals(5, result);
    }

    @Test
    void testFindTopK() {
        int[] data = {3, 1, 4, 1, 5, 9, 2, 6};
        List<Integer> top3 = PriorityQueueDemo.findTopK(data, 3);
        assertEquals(3, top3.size());
        assertTrue(top3.containsAll(List.of(5, 6, 9)));
    }

    @Test
    void testMergeKSortedLists() {
        List<List<Integer>> lists = List.of(
            List.of(1, 4, 7),
            List.of(2, 5, 8),
            List.of(3, 6, 9)
        );
        List<Integer> merged = PriorityQueueDemo.mergeKSortedLists(lists);
        assertEquals(9, merged.size());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), merged);
    }
}
