package academy.javaengineering.jvm.memorymodel;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Exercise 3: Lock-Free Stack Using CAS
 *
 * Task: Implement a lock-free stack using compare-and-swap (CAS) operations.
 * This demonstrates how the JMM's memory ordering guarantees enable lock-free algorithms.
 */
public class Exercise3 {

    private static final AtomicReference<Node> top = new AtomicReference<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Lock-Free Stack ===\n");

        // Task 1: Implement push using CAS
        System.out.println("--- Task 1: Push Operation ---");
        // TODO: Implement push() using CAS loop

        // Task 2: Implement pop using CAS
        System.out.println("\n--- Task 2: Pop Operation ---");
        // TODO: Implement pop() using CAS loop with retry

        // Task 3: Test with multiple threads
        System.out.println("\n--- Task 3: Concurrent Test ---");
        // TODO: Launch multiple threads pushing and popping
        // TODO: Verify correctness

        System.out.println("\n[Complete the TODO sections above]");
    }

    static class Node {
        final int value;
        final Node next;

        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    // TODO: Implement push()
    static void push(int value) {
        // 1. Read current top
        // 2. Create new node pointing to current top
        // 3. CAS to set new node as top
        // 4. If CAS fails, retry
    }

    // TODO: Implement pop()
    static Integer pop() {
        // 1. Read current top
        // 2. If null, return null
        // 3. Read next node
        // 4. CAS to set next as top
        // 5. If CAS fails, retry
        return null;
    }
}
