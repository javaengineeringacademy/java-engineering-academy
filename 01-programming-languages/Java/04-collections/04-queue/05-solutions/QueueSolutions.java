package academy.javaengineering.collections.queue.solutions;

import java.util.*;

public class QueueSolutions {
    public static Queue<Integer> reverseQueue(Queue<Integer> queue) {
        Stack<Integer> stack = new Stack<>();
        while (!queue.isEmpty()) stack.push(queue.poll());
        while (!stack.isEmpty()) queue.offer(stack.pop());
        return queue;
    }
    public static boolean isValidParentheses(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') stack.push(c);
            else if (stack.isEmpty()) return false;
            else if (c == ')' && stack.pop() != '(') return false;
            else if (c == ']' && stack.pop() != '[') return false;
            else if (c == '}' && stack.pop() != '{') return false;
        }
        return stack.isEmpty();
    }
    public static void main(String[] args) {
        System.out.println(reverseQueue(new LinkedList<>(Arrays.asList(1,2,3))));
        System.out.println(isValidParentheses("()[]{}"));
        System.out.println(isValidParentheses("(]"));
    }
}
