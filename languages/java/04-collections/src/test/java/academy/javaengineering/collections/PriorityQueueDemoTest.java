package academy.javaengineering.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PriorityQueueDemoTest {

    private PriorityQueue<Integer> minHeap;

    @BeforeEach
    void setUp() {
        minHeap = new PriorityQueue<>();
    }

    @Nested
    @DisplayName("Min-Heap Tests")
    class MinHeapTests {

        @Test
        @DisplayName("Should poll in ascending order")
        void testPollAscendingOrder() {
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
        @DisplayName("Should maintain heap property after multiple operations")
        void testHeapProperty() {
            minHeap.offer(10);
            minHeap.offer(3);
            minHeap.offer(7);
            minHeap.offer(1);

            assertEquals(Integer.valueOf(1), minHeap.peek());
            minHeap.offer(0);
            assertEquals(Integer.valueOf(0), minHeap.peek());
        }

        @Test
        @DisplayName("Should handle duplicates")
        void testDuplicates() {
            minHeap.offer(5);
            minHeap.offer(5);
            minHeap.offer(3);

            assertEquals(3, minHeap.size());
            assertEquals(3, minHeap.poll());
            assertEquals(5, minHeap.poll());
            assertEquals(5, minHeap.poll());
        }
    }

    @Nested
    @DisplayName("Max-Heap Tests")
    class MaxHeapTests {

        @Test
        @DisplayName("Should poll in descending order with reverse comparator")
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
    }

    @Nested
    @DisplayName("Custom Comparator Tests")
    class CustomComparatorTests {

        @Test
        @DisplayName("Should sort strings by length")
        void testSortByLength() {
            PriorityQueue<String> byLength = new PriorityQueue<>(
                Comparator.comparingInt(String::length)
            );
            byLength.offer("Banana");
            byLength.offer("Apple");
            byLength.offer("Fig");

            assertEquals("Fig", byLength.poll());
            assertEquals("Apple", byLength.poll());
            assertEquals("Banana", byLength.poll());
        }

        @Test
        @DisplayName("Should find k-th largest element")
        void testFindKthLargest() {
            int[] nums = {3, 2, 1, 5, 6, 4};
            int k = 2;
            int result = PriorityQueueDemo.findKthLargest(nums, k);
            assertEquals(5, result);
        }

        @Test
        @DisplayName("Should find top k elements")
        void testFindTopK() {
            int[] data = {3, 1, 4, 1, 5, 9, 2, 6};
            List<Integer> top3 = PriorityQueueDemo.findTopK(data, 3);
            assertEquals(3, top3.size());
            assertTrue(top3.containsAll(List.of(5, 6, 9)));
        }

        @Test
        @DisplayName("Should merge k sorted lists")
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
}
