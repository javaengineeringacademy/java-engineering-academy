package academy.javaengineering.collections.queue.blockingqueue.solutions;

import java.util.concurrent.*;

public class BlockingQueueSolutions {
    public static void producerConsumer(int items) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        new Thread(() -> {
            try {
                for (int i = 0; i < items; i++) { queue.put(i); Thread.sleep(100); }
            } catch (InterruptedException e) {}
        }).start();
        new Thread(() -> {
            try {
                for (int i = 0; i < items; i++) System.out.println("Consumed: " + queue.take());
            } catch (InterruptedException e) {}
        }).start();
    }
    public static void main(String[] args) {
        producerConsumer(5);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }
}
