package queue.exercises;

import java.util.*;

public class QueueExercises {

    // TODO 1: Write a method that reverses a Queue using recursion.
    public static <T> void reverseQueue(Queue<T> queue) {
        // Your code here
    }

    // TODO 2: Write a method that interleaves two queues alternately.
    public static <T> Queue<T> interleave(Queue<T> q1, Queue<T> q2) {
        // Your code here
        return null;
    }

    // TODO 3: Write a method that finds the minimum element in a Queue
    //         without modifying the original queue.
    public static Integer findMin(Queue<Integer> queue) {
        // Your code here
        return null;
    }

    // TODO 4: Write a method that sorts a Queue using only another Queue as auxiliary storage.
    public static Queue<Integer> sortQueue(Queue<Integer> queue) {
        // Your code here
        return null;
    }

    // TODO 5: Write a method that implements a queue using two stacks.
    public static class QueueUsingStacks<T> {
        private Stack<T> stack1 = new Stack<>();
        private Stack<T> stack2 = new Stack<>();

        public void enqueue(T item) {
            // Your code here
        }

        public T dequeue() {
            // Your code here
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("Run the solutions to verify your answers.");
    }
}
