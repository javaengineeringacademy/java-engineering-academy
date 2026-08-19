package academy.javaengineering.jvm.memorymodel;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Solution 3: Lock-Free Stack Using CAS
 */
public class Solution3 {

    private static final AtomicReference<Node> top = new AtomicReference<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Lock-Free Stack ===\n");

        // Task 1 & 2: Push and Pop
        System.out.println("--- Task 1 & 2: Push and Pop ---");
        push(1);
        push(2);
        push(3);
        System.out.println("  Popped: " + pop());
        System.out.println("  Popped: " + pop());
        System.out.println("  Popped: " + pop());
        System.out.println("  Popped: " + pop()); // null

        // Task 3: Concurrent test
        System.out.println("\n--- Task 3: Concurrent Test ---");
        Thread[] producers = new Thread[5];
        Thread[] consumers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final int id = i;
            producers[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) push(id * 1000 + j);
            });
            consumers[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) pop();
            });
        }
        for (Thread t : producers) t.start();
        for (Thread t : consumers) t.start();
        for (Thread t : producers) t.join();
        for (Thread t : consumers) t.join();
        System.out.println("  Concurrent push/pop completed successfully");
        System.out.println("  Remaining: " + pop());
    }

    static void push(int value) {
        Node oldTop;
        Node newTop;
        do {
            oldTop = top.get();
            newTop = new Node(value, oldTop);
        } while (!top.compareAndSet(oldTop, newTop));
    }

    static Integer pop() {
        Node oldTop;
        Node newTop;
        do {
            oldTop = top.get();
            if (oldTop == null) return null;
            newTop = oldTop.next;
        } while (!top.compareAndSet(oldTop, newTop));
        return oldTop.value;
    }

    static class Node {
        final int value;
        final Node next;
        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}
