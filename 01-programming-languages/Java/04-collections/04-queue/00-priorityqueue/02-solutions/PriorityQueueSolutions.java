package academy.javaengineering.collections.queue.priorityqueue.solutions;

import java.util.*;

public class PriorityQueueSolutions {
    public static List<Integer> kLargest(int[] arr, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int n : arr) { pq.add(n); if (pq.size() > k) pq.poll(); }
        return new ArrayList<>(pq);
    }
    public static String mergeSorted(String[] lists) {
        StringBuilder sb = new StringBuilder();
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String s : lists) pq.add(s);
        while (!pq.isEmpty()) sb.append(pq.poll());
        return sb.toString();
    }
    public static void main(String[] args) {
        System.out.println(kLargest(new int[]{3,1,5,12,2,11}, 3));
        System.out.println(mergeSorted(new String[]{"3","1","5","2","4"}));
    }
}
