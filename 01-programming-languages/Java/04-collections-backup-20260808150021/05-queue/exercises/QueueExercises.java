package collections.queue.exercises;

import java.util.*;

/**
 * QUEUE EXERCISES — Advanced PriorityQueue and Deque operations.
 *
 * Complete each TODO. Run tests to verify your solutions.
 */
public class QueueExercises {

    // =========================================================================
    // EXERCISE 1: Implement Stack Using Queues
    // =========================================================================
    /**
     * Implement a stack (LIFO) using only two queues (FIFO).
     * Your MyStack class should support:
     *   - void push(int x)
     *   - int pop()
     *   - int top()
     *   - boolean empty()
     *
     * TODO: Implement the MyStack inner class
     */
    public static class MyStack {
        private final Queue<Integer> q1 = new LinkedList<>();
        private final Queue<Integer> q2 = new LinkedList<>();

        public void push(int x) {
            // TODO: Your code here
        }

        public int pop() {
            // TODO: Your code here
            return 0;
        }

        public int top() {
            // TODO: Your code here
            return 0;
        }

        public boolean empty() {
            // TODO: Your code here
            return true;
        }
    }

    // =========================================================================
    // EXERCISE 2: Implement Queue Using Stacks
    // =========================================================================
    /**
     * Implement a queue (FIFO) using only two stacks (LIFO).
     * Your MyQueue class should support:
     *   - void push(int x)
     *   - int pop()
     *   - int peek()
     *   - boolean empty()
     *
     * TODO: Implement the MyQueue inner class
     */
    public static class MyQueue {
        private final Stack<Integer> stackIn = new Stack<>();
        private final Stack<Integer> stackOut = new Stack<>();

        public void push(int x) {
            // TODO: Your code here
        }

        public int pop() {
            // TODO: Your code here
            return 0;
        }

        public int peek() {
            // TODO: Your code here
            return 0;
        }

        public boolean empty() {
            // TODO: Your code here
            return true;
        }
    }

    // =========================================================================
    // EXERCISE 3: Circular Tour (Gas Station)
    // =========================================================================
    /**
     * Given two arrays gas[i] and cost[i], find the starting gas station
     * index to complete a full circular tour. Return -1 if impossible.
     * Each unit of gas costs one unit of distance.
     *
     * gas =  [1,2,3,4,5]
     * cost = [3,4,5,1,2]
     * → Start at index 3 (gas=4, cost=1, surplus travels around)
     *
     * TODO: Implement this method
     */
    public static int circularTour(int[] gas, int[] cost) {
        // TODO: Your code here
        return -1;
    }

    // =========================================================================
    // EXERCISE 4: Reorganize String
    // =========================================================================
    /**
     * Given a string s, rearrange it so that no two adjacent characters
     * are the same. If not possible, return "". Use a PriorityQueue
     * to always pick the most frequent remaining character.
     *
     * Example: "aab" → "aba"
     * Example: "aaab" → ""
     *
     * TODO: Implement this method
     */
    public static String reorganizeString(String s) {
        // TODO: Your code here
        return "";
    }

    // =========================================================================
    // EXERCISE 5: Task Scheduler with Cooldown
    // =========================================================================
    /**
     * Given a list of tasks and a cooldown period n, schedule tasks so
     * that the same task is at least n units apart. Return the minimum
     * number of time units required.
     *
     * Example: tasks=["A","A","A","B","B","B"], n=2 → 8
     * Schedule: A B _ A B _ A B (_ = idle)
     *
     * TODO: Implement this method
     */
    public static int leastInterval(char[] tasks, int n) {
        // TODO: Your code here
        return 0;
    }
}
