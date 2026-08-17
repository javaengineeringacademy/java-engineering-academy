package academy.javaengineering.collections.queue.deque.solutions;

import java.util.*;

public class DequeSolutions {
    public static boolean isPalindrome(String s) {
        ArrayDeque<Character> deque = new ArrayDeque<>();
        for (char c : s.toCharArray()) deque.addLast(c);
        while (deque.size() > 1) {
            if (!deque.pollFirst().equals(deque.pollLast())) return false;
        }
        return true;
    }
    public static void reverseArrayDeque(ArrayDeque<Integer> deque) {
        ArrayDeque<Integer> temp = new ArrayDeque<>();
        while (!deque.isEmpty()) temp.addFirst(deque.poll());
        deque.addAll(temp);
    }
    public static void main(String[] args) {
        System.out.println(isPalindrome("racecar"));
        ArrayDeque<Integer> d = new ArrayDeque<>(Arrays.asList(1,2,3,4,5));
        reverseArrayDeque(d);
        System.out.println(d);
    }
}
