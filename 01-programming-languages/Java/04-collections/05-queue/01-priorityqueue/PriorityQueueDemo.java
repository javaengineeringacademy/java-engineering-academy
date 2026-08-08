import java.util.*;

/**
 * Demonstrates PriorityQueue for priority-based processing.
 * PriorityQueue provides O(log n) for offer/poll/peek operations.
 */
public class PriorityQueueDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstrateCustomComparator();
        demonstrateAdvancedPatterns();
    }

    /**
     * Demonstrates basic PriorityQueue operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== PriorityQueue Basic Operations ===");

        // Create with natural ordering (min-heap)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(2);
        minHeap.offer(8);
        minHeap.offer(1);
        minHeap.offer(5);

        System.out.println("Min-heap: " + minHeap);
        System.out.println("Poll order: ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();

        // Create max-heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(2);
        maxHeap.offer(8);
        maxHeap.offer(1);

        System.out.println("\nMax-heap poll order: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Demonstrates custom comparator for PriorityQueue.
     */
    private static void demonstrateCustomComparator() {
        System.out.println("=== Custom Comparator ===");

        // Task queue with priority
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
            Comparator.comparingInt(Task::priority).thenComparing(Task::createdAt)
        );

        taskQueue.offer(new Task("Low priority", 1, new Date()));
        taskQueue.offer(new Task("High priority", 3, new Date()));
        taskQueue.offer(new Task("Medium priority", 2, new Date()));

        System.out.println("Processing tasks by priority:");
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println("  " + task.description() + " (priority: " + task.priority() + ")");
        }
        System.out.println();
    }

    /**
     * Demonstrates advanced PriorityQueue patterns.
     */
    private static void demonstrateAdvancedPatterns() {
        System.out.println("=== Advanced Patterns ===");

        // Pattern 1: Find k-th largest element
        System.out.println("K-th largest element:");
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println("  " + k + "-th largest: " + findKthLargest(nums, k));

        // Pattern 2: Top K elements
        System.out.println("\nTop 3 elements:");
        int[] data = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        List<Integer> topK = findTopK(data, 3);
        System.out.println("  " + topK);

        // Pattern 3: Merge k sorted lists
        System.out.println("\nMerge k sorted lists:");
        List<List<Integer>> lists = List.of(
            List.of(1, 4, 7),
            List.of(2, 5, 8),
            List.of(3, 6, 9)
        );
        List<Integer> merged = mergeKSortedLists(lists);
        System.out.println("  " + merged);
    }

    /**
     * Finds the k-th largest element using a min-heap.
     */
    static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }

    /**
     * Finds the top k elements.
     */
    static List<Integer> findTopK(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return new ArrayList<>(minHeap);
    }

    /**
     * Merges k sorted lists using a priority queue.
     */
    static List<Integer> mergeKSortedLists(List<List<Integer>> lists) {
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        List<Integer> result = new ArrayList<>();

        // Initialize heap with first element from each list
        for (int i = 0; i < lists.size(); i++) {
            if (!lists.get(i).isEmpty()) {
                minHeap.offer(new int[]{lists.get(i).get(0), i, 0});
            }
        }

        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int value = current[0];
            int listIndex = current[1];
            int elementIndex = current[2];

            result.add(value);

            // Add next element from same list
            if (elementIndex + 1 < lists.get(listIndex).size()) {
                minHeap.offer(new int[]{
                    lists.get(listIndex).get(elementIndex + 1),
                    listIndex,
                    elementIndex + 1
                });
            }
        }

        return result;
    }

    record Task(String description, int priority, Date createdAt) {}
}
