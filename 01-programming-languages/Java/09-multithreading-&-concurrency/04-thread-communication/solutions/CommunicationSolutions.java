package academy.javaengineering.concurrency.communication.solutions;

public class CommunicationSolutions {
    public static void main(String[] args) throws InterruptedException {
        // Solution 1: Bounded buffer
        Object lock = new Object();
        java.util.LinkedList<Integer> buffer = new java.util.LinkedList<>();
        int capacity = 10;

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                synchronized (lock) {
                    while (buffer.size() == capacity) {
                        try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                    buffer.add(i);
                    System.out.println("Produced: " + i);
                    lock.notifyAll();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                synchronized (lock) {
                    while (buffer.isEmpty()) {
                        try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                    System.out.println("  Consumed: " + buffer.remove());
                    lock.notifyAll();
                }
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // Solution 2: Round-robin printing
        System.out.println("\nRound-robin:");
        int[] counter = {0};
        Object[] locks = {new Object(), new Object(), new Object()};
        Runnable[] printers = new Runnable[3];
        for (int i = 0; i < 3; i++) {
            final int id = i;
            printers[i] = () -> {
                for (int j = 0; j < 3; j++) {
                    synchronized (locks[id]) {
                        while (counter[0] % 3 != id) {
                            try { locks[id].wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                        }
                        System.out.println("Thread " + id + ": " + counter[0]);
                        counter[0]++;
                        for (Object l : locks) synchronized (l) { l.notifyAll(); }
                    }
                }
            };
        }
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(printers[i]);
            threads[i].start();
        }
        for (Thread t : threads) t.join();
    }
}
