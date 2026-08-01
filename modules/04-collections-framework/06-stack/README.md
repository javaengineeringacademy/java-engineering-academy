# Stack

## Introduction

Stack is a legacy class that extends Vector to implement a stack (LIFO - Last In, First Out) data structure.

## Learning Objectives

- Create and use Stack
- Understand LIFO operations
- Know when to use Stack vs Deque
- Learn Stack's legacy methods

## Prerequisites

- Vector
- Basic data structure concepts

## Why This Matters

While Stack is legacy, understanding stacks is crucial for algorithms (recursion, parsing) and Stack is still found in legacy code.

## Syntax

```java
// Creating Stack
Stack<E> stack = new Stack<>();

// Stack operations
stack.push(element);  // Add to top
stack.pop();          // Remove and return top
stack.peek();         // View top without removing
stack.empty();        // Check if empty
stack.search(element); // Returns distance from top (1-based)
```

## Examples

```java
// Example 1: Basic Stack
Stack<String> stack = new Stack<>();
stack.push("Bottom");
stack.push("Middle");
stack.push("Top");

System.out.println(stack.peek());  // Top
System.out.println(stack.pop());   // Top
System.out.println(stack.pop());   // Middle
System.out.println(stack.size());  // 1

// Example 2: Balanced parentheses
public static boolean isBalanced(String expression) {
    Stack<Character> stack = new Stack<>();
    for (char c : expression.toCharArray()) {
        if (c == '(' || c == '[' || c == '{') {
            stack.push(c);
        } else if (c == ')' || c == ']' || c == '}') {
            if (stack.isEmpty()) return false;
            char top = stack.pop();
            if (!isMatchingPair(top, c)) return false;
        }
    }
    return stack.isEmpty();
}

// Example 3: Undo functionality
Stack<String> history = new Stack<>();
Stack<String> redo = new Stack<>();

public void execute(String action) {
    history.push(action);
    redo.clear();
}

public String undo() {
    if (history.isEmpty()) return null;
    String action = history.pop();
    redo.push(action);
    return action;
}
```

## Exercises

1. Implement a calculator that evaluates postfix expressions using a Stack.
2. Create a method that reverses a string using a Stack.
3. Implement browser-like back/forward navigation using two Stacks.

## Interview Questions

- What is the difference between Stack and Deque?
- Why is Stack considered legacy?
- How would you implement a stack using an ArrayList?

## Common Pitfalls

- Using Stack in new code (prefer ArrayDeque)
- Not checking empty() before pop() or peek()
- Using search() which is O(n) instead of contains()

## Best Practices

- Use ArrayDeque instead of Stack for new code
- Check empty() before pop() or peek()
- Consider using Deque interface for flexibility

## Real World Applications

- Undo/redo functionality
- Expression parsing and evaluation
- Browser navigation history
- Function call stack simulation

## References

- [Stack Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/Stack.html)
- [Deque Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Deque.html)

## Summary

In this topic, you learned about Stack and its LIFO operations. While legacy, stacks are fundamental data structures. For new code, prefer ArrayDeque. Practice with the exercises before learning about Queue.
