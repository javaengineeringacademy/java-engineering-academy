package academy.javaengineering.collections.queue.memory;

import java.util.*;
import java.util.concurrent.*;

public class QueueMemory {

    public static void main(String[] args) {
        System.out.println("=== Queue Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. LinkedList vs ArrayDeque
        System.out.println("--- Queue Implementations Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Queue<Integer> linked = new LinkedList<>();
        for (int i = 0; i < 10000; i++) linked.offer(i);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("LinkedList 10K: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Queue<Integer> array = new ArrayDeque<>();
        for (int i = 0; i < 10000; i++) array.offer(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("ArrayDeque 10K: " + (after - before) + " bytes");

        // 2. PriorityQueue heap overhead
        System.out.println("\n--- PriorityQueue Overhead ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < 10000; i++) pq.offer(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("PriorityQueue 10K: " + (after - before) + " bytes");
        System.out.println("Heap uses array storage");

        // 3. BlockingQueue capacity
        System.out.println("\n--- BlockingQueue Capacity ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        BlockingQueue<Integer> blocking = new ArrayBlockingQueue<>(10000);
        for (int i = 0; i < 10000; i++) blocking.offer(i);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("ArrayBlockingQueue 10K: " + (after - before) + " bytes");
        System.out.println("Fixed capacity prevents overflow");

        // 4. Node overhead in LinkedList
        System.out.println("\n--- Node Overhead ---");
        System.out.println("LinkedList.Node: ~24 bytes per element");
        System.out.println("  - E item (8 ref)");
        System.out.println("  - Node next (8 ref)");
        System.out.println("  - Node prev (8 ref)");
        System.out.println("ArrayDeque: ~8 bytes per element (array)");
    }
}
