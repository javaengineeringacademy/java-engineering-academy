package list.stack.examples;

import java.util.*;

public class StackExample {

    public static void main(String[] args) {
        example1_BasicStackOperations();
        example2_PushPopPeek();
        example3_StackSearch();
        example4_BracketValidation();
        example5_StackWithDeque();
    }

    static void example1_BasicStackOperations() {
        System.out.println("=== Example 1: Basic Stack Operations ===");
        Stack<String> stack = new Stack<>();
        stack.push("Java");
        stack.push("Python");
        stack.push("C++");
        System.out.println("Stack: " + stack);
        System.out.println("Size: " + stack.size());
    }

    static void example2_PushPopPeek() {
        System.out.println("\n=== Example 2: Push, Pop, Peek ===");
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Stack: " + stack);
        System.out.println("Peek: " + stack.peek());
        System.out.println("Pop: " + stack.pop());
        System.out.println("After pop: " + stack);
        System.out.println("Empty: " + stack.isEmpty());
    }

    static void example3_StackSearch() {
        System.out.println("\n=== Example 3: Stack Search ===");
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        stack.push("D");
        System.out.println("Stack: " + stack);
        System.out.println("Search C: " + stack.search("C"));
        System.out.println("Search A: " + stack.search("A"));
        System.out.println("Search Z: " + stack.search("Z"));
    }

    static void example4_BracketValidation() {
        System.out.println("\n=== Example 4: Bracket Validation ===");
        String[] tests = {"{[()]}", "{[(])}", "((()))"};
        for (String test : tests) {
            System.out.println(test + " valid? " + isValidBrackets(test));
        }
    }

    static boolean isValidBrackets(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == '}' && top != '{') return false;
                if (c == ']' && top != '[') return false;
            }
        }
        return stack.isEmpty();
    }

    static void example5_StackWithDeque() {
        System.out.println("\n=== Example 5: Preferred Stack Implementation (Deque) ===");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        System.out.println("Stack: " + stack);
        System.out.println("Pop: " + stack.pop());
        System.out.println("After pop: " + stack);
    }
}
