package list.stack.solutions;

import java.util.*;

public class StackSolutions {

    public static String reverseString(String str) {
        Stack<Character> stack = new Stack<>();
        for (char c : str.toCharArray()) {
            stack.push(c);
        }
        StringBuilder reversed = new StringBuilder();
        while (!stack.isEmpty()) {
            reversed.append(stack.pop());
        }
        return reversed.toString();
    }

    public static int evaluatePostfix(String expression) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = expression.split(" ");
        for (String token : tokens) {
            if (token.matches("-?\\d+")) {
                stack.push(Integer.parseInt(token));
            } else {
                int b = stack.pop();
                int a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                }
            }
        }
        return stack.pop();
    }

    public static String decimalToBinary(int decimal) {
        if (decimal == 0) return "0";
        Stack<Integer> stack = new Stack<>();
        while (decimal > 0) {
            stack.push(decimal % 2);
            decimal /= 2;
        }
        StringBuilder binary = new StringBuilder();
        while (!stack.isEmpty()) {
            binary.append(stack.pop());
        }
        return binary.toString();
    }

    public static boolean isBalanced(String expression) {
        Stack<Character> stack = new Stack<>();
        for (char c : expression.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if ((c == ')' && top != '(') || (c == '}' && top != '{') || (c == ']' && top != '[')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static Stack<Integer> sortStack(Stack<Integer> stack) {
        Stack<Integer> tempStack = new Stack<>();
        while (!stack.isEmpty()) {
            int temp = stack.pop();
            while (!tempStack.isEmpty() && tempStack.peek() > temp) {
                stack.push(tempStack.pop());
            }
            tempStack.push(temp);
        }
        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
        return stack;
    }

    public static void main(String[] args) {
        System.out.println("Reverse: " + reverseString("Hello"));
        System.out.println("Postfix 3 4 + 2 * = " + evaluatePostfix("3 4 + 2 *"));
        System.out.println("Binary of 10: " + decimalToBinary(10));
        System.out.println("Balanced {[]}: " + isBalanced("{[]}"));
        System.out.println("Balanced {(]): " + isBalanced("{(])"));

        Stack<Integer> sortStack = new Stack<>();
        sortStack.push(3);
        sortStack.push(1);
        sortStack.push(4);
        sortStack.push(2);
        System.out.println("Sorted stack: " + sortStack(sortStack));
    }
}
