import java.util.Stack;

/**
 * Demonstrates Stack operations (LIFO data structure).
 *
 * <p>Stack extends Vector and represents a LIFO (Last-In-First-Out) stack.
 * It provides push, pop, peek, and search operations.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>LIFO (Last-In-First-Out) ordering</li>
 *   <li>push, pop, peek operations</li>
 *   <li>EmptyStackException when popping empty stack</li>
 *   <li>search returns 1-based position from top</li>
 *   <li>Legacy class — prefer Deque for stack behavior</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class StackDemo {

    public static void main(String[] args) {
        demonstrateBasicOperations();
        demonstratePeekAndSearch();
        demonstrateBracketValidation();
        demonstrateExpressionEvaluation();
    }

    /**
     * Demonstrates basic Stack operations.
     */
    private static void demonstrateBasicOperations() {
        System.out.println("=== Stack Basic Operations ===");

        Stack<String> stack = new Stack<>();

        // Push elements
        stack.push("Alice");
        stack.push("Bob");
        stack.push("Charlie");
        System.out.println("Stack: " + stack);
        System.out.println("Size: " + stack.size());

        // Peek (view top without removing)
        System.out.println("Peek: " + stack.peek());

        // Pop (remove and return top)
        System.out.println("Pop: " + stack.pop());
        System.out.println("After pop: " + stack);

        // Search (1-based from top)
        System.out.println("Search for Bob: " + stack.search("Bob"));
        System.out.println("Search for Alice: " + stack.search("Alice"));

        // Empty check
        System.out.println("Is empty: " + stack.empty());
        System.out.println();
    }

    /**
     * Demonstrates peek and search operations.
     */
    private static void demonstratePeekAndSearch() {
        System.out.println("=== Peek and Search ===");

        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.push(40);

        System.out.println("Stack: " + stack);
        System.out.println("Peek: " + stack.peek());
        System.out.println("Size after peek: " + stack.size());

        // Search returns distance from top (1-based)
        // Top is index 3 (value 40), so search(40) = 1
        // Bottom is index 0 (value 10), so search(10) = 4
        System.out.println("Search(30): " + stack.search(30));
        System.out.println("Search(10): " + stack.search(10));
        System.out.println("Search(99): " + stack.search(99)); // -1 if not found
        System.out.println();
    }

    /**
     * Demonstrates bracket validation using Stack.
     */
    private static void demonstrateBracketValidation() {
        System.out.println("=== Bracket Validation ===");

        String[] testCases = {"([])", "([)]", "{[()]}", "((()))", ""};

        for (String test : testCases) {
            System.out.println("\"" + test + "\" -> " + isBalanced(test));
        }
        System.out.println();
    }

    /**
     * Validates balanced brackets using Stack.
     */
    private static boolean isBalanced(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                    (c == ']' && top != '[') ||
                    (c == '}' && top != '{')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * Demonstrates simple expression evaluation with Stack.
     */
    private static void demonstrateExpressionEvaluation() {
        System.out.println("=== Reverse Polish Notation ===");

        // Evaluate: 3 4 + 2 * = (3+4)*2 = 14
        String[] tokens = {"3", "4", "+", "2", "*"};
        int result = evaluateRPN(tokens);
        System.out.println("Expression: 3 4 + 2 * = " + result);

        // Evaluate: 5 1 2 + 4 * + 3 - = 5+((1+2)*4)-3 = 14
        String[] tokens2 = {"5", "1", "2", "+", "4", "*", "+", "3", "-"};
        int result2 = evaluateRPN(tokens2);
        System.out.println("Expression: 5 1 2 + 4 * + 3 - = " + result2);
    }

    /**
     * Evaluates a Reverse Polish Notation expression.
     */
    private static int evaluateRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for (String token : tokens) {
            switch (token) {
                case "+" -> stack.push(stack.pop() + stack.pop());
                case "-" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a - b);
                }
                case "*" -> stack.push(stack.pop() * stack.pop());
                default -> stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }
}
