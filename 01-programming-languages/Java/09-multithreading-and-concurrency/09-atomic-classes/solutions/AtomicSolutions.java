package academy.javaengineering.concurrency.atomic.solutions;

import java.util.concurrent.atomic.*;

public class AtomicSolutions {
    public static void main(String[] args) throws InterruptedException {
        // Solution 1: AtomicInteger counter
        AtomicInteger counter = new AtomicInteger(0);
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) counter.incrementAndGet();
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Counter: " + counter.get());

        // Solution 2: Non-blocking stack
        static class Stack<E> {
            private final AtomicReference<Node<E>> top = new AtomicReference<>();

            void push(E item) {
                Node<E> oldTop, newTop;
                do {
                    oldTop = top.get();
                    newTop = new Node<>(item, oldTop);
                } while (!top.compareAndSet(oldTop, newTop));
            }

            E pop() {
                Node<E> oldTop, newTop;
                do {
                    oldTop = top.get();
                    if (oldTop == null) return null;
                    newTop = oldTop.next;
                } while (!top.compareAndSet(oldTop, newTop));
                return oldTop.item;
            }

            static class Node<E> {
                final E item;
                final Node<E> next;
                Node(E item, Node<E> next) { this.item = item; this.next = next; }
            }
        }

        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < 10; i++) stack.push(i);
        System.out.print("Stack: ");
        Integer val;
        while ((val = stack.pop()) != null) System.out.print(val + " ");
        System.out.println();
    }
}
