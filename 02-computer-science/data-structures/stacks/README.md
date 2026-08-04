# Stacks

## Table of Contents

- [Overview](#overview)
- [Stack Operations](#stack-operations)
- [Implementations](#implementations)
  - [Array-Based Stack](#array-based-stack)
  - [Linked List Stack](#linked-list-stack)
- [Time Complexity](#time-complexity)
- [Applications](#applications)
  - [Expression Evaluation](#expression-evaluation)
  - [Balanced Parentheses](#balanced-parentheses)
  - [Function Call Stack](#function-call-stack)
  - [Undo/Redo](#undoredo)
  - [Browser Navigation](#browser-navigation)
- [Related Data Structures](#related-data-structures)

---

## Overview

A stack is a linear data structure that follows the **LIFO (Last In, First Out)** principle. The last element added is the first one to be removed.

```
Stack Operations:
                    
    ┌─────┐         push(4)       ┌─────┐
    │  3  │  ──────────────►      │  4  │  ← TOP
    ├─────┤                       ├─────┤
    │  2  │                       │  3  │
    ├─────┤                       ├─────┤
    │  1  │                       │  2  │
    └─────┘                       ├─────┤
                                  │  1  │
    [Stack]                       └─────┘

    pop()           ┌─────┐
    ◄─────────────  │  3  │  ← TOP (4 was removed)
                    ├─────┤
                    │  2  │
                    ├─────┤
                    │  1  │
                    └─────┘
```

### Key Characteristics

- **LIFO ordering** - Last element added is first removed
- **Limited access** - Only top element accessible
- **Push/Pop operations** - O(1) time complexity
- **No random access** - Cannot access middle elements directly

---

## Stack Operations

| Operation | Description | Time Complexity |
|-----------|-------------|-----------------|
| push(item) | Add item to top | O(1) |
| pop() | Remove and return top item | O(1) |
| peek() / top() | Return top item without removing | O(1) |
| is_empty() | Check if stack is empty | O(1) |
| size() | Return number of elements | O(1) |
| contains(item) | Check if item exists | O(n) |

---

## Implementations

### Array-Based Stack

```python
class ArrayStack:
    def __init__(self, capacity: int = 10):
        self._capacity = capacity
        self._data = [None] * capacity
        self._top = -1

    def push(self, item) -> None:
        if self.is_full():
            raise OverflowError("Stack is full")
        self._top += 1
        self._data[self._top] = item

    def pop(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        item = self._data[self._top]
        self._top -= 1
        return item

    def peek(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self._data[self._top]

    def is_empty(self) -> bool:
        return self._top == -1

    def is_full(self) -> bool:
        return self._top == self._capacity - 1

    def size(self) -> int:
        return self._top + 1

    def __repr__(self) -> str:
        return f"Stack({self._data[:self._top + 1]})"

# Dynamic array stack
class DynamicStack:
    def __init__(self):
        self._data = []

    def push(self, item) -> None:
        self._data.append(item)

    def pop(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self._data.pop()

    def peek(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self._data[-1]

    def is_empty(self) -> bool:
        return len(self._data) == 0

    def size(self) -> int:
        return len(self._data)
```

### Linked List Stack

```python
class Node:
    def __init__(self, data, next_node=None):
        self.data = data
        self.next = next_node

class LinkedListStack:
    def __init__(self):
        self._top = None
        self._size = 0

    def push(self, item) -> None:
        new_node = Node(item, self._top)
        self._top = new_node
        self._size += 1

    def pop(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        item = self._top.data
        self._top = self._top.next
        self._size -= 1
        return item

    def peek(self):
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self._top.data

    def is_empty(self) -> bool:
        return self._top is None

    def size(self) -> int:
        return self._size

    def __repr__(self) -> str:
        items = []
        current = self._top
        while current:
            items.append(str(current.data))
            current = current.next
        return " -> ".join(items) + " -> None"
```

---

## Time Complexity

| Operation | Array Stack | Linked List Stack |
|-----------|-------------|-------------------|
| Push | O(1) amortized* | O(1) |
| Pop | O(1) | O(1) |
| Peek | O(1) | O(1) |
| Is Empty | O(1) | O(1) |
| Search | O(n) | O(n) |
| Space | O(n) | O(n) + pointers |

*O(n) when resize is needed

---

## Applications

### Expression Evaluation

#### Postfix (Reverse Polish Notation) Evaluation

```python
def evaluate_postfix(expression: str) -> float:
    """Evaluate postfix expression."""
    stack = []
    operators = {'+', '-', '*', '/', '^'}

    for token in expression.split():
        if token not in operators:
            stack.append(float(token))
        else:
            b = stack.pop()
            a = stack.pop()
            if token == '+':
                stack.append(a + b)
            elif token == '-':
                stack.append(a - b)
            elif token == '*':
                stack.append(a * b)
            elif token == '/':
                stack.append(a / b)
            elif token == '^':
                stack.append(a ** b)

    return stack[0]

# Example: 3 4 + 5 * = (3 + 4) * 5 = 35
print(evaluate_postfix("3 4 + 5 *"))  # 35.0

# Example: 5 1 2 + 4 * + 3 - = 5 + ((1 + 2) * 4) - 3 = 14
print(evaluate_postfix("5 1 2 + 4 * + 3 -"))  # 14.0
```

#### Infix to Postfix Conversion

```python
def infix_to_postfix(expression: str) -> str:
    """Convert infix expression to postfix."""
    precedence = {'+': 1, '-': 1, '*': 2, '/': 2, '^': 3}
    right_associative = {'^'}
    stack = []
    output = []

    for token in expression.split():
        if token.isalnum():
            output.append(token)
        elif token == '(':
            stack.append(token)
        elif token == ')':
            while stack and stack[-1] != '(':
                output.append(stack.pop())
            stack.pop()  # Remove '('
        else:
            while (stack and stack[-1] != '(' and
                   (precedence[stack[-1]] > precedence[token] or
                    (precedence[stack[-1]] == precedence[token] and
                     token not in right_associative))):
                output.append(stack.pop())
            stack.append(token)

    while stack:
        output.append(stack.pop())

    return ' '.join(output)

# Example
print(infix_to_postfix("A + B * C"))  # A B C * +
print(infix_to_postfix("( A + B ) * C"))  # A B + C *
```

### Balanced Parentheses

```python
def is_balanced(expression: str) -> bool:
    """Check if parentheses are balanced."""
    stack = []
    matching = {')': '(', '}': '{', ']': '['}

    for char in expression:
        if char in '({[':
            stack.append(char)
        elif char in ')}]':
            if not stack or stack[-1] != matching[char]:
                return False
            stack.pop()

    return len(stack) == 0

# Examples
print(is_balanced("({[]})"))      # True
print(is_balanced("({[}]})"))     # False
print(is_balanced("((()))"))      # True
print(is_balanced("(()"))         # False
```

### Function Call Stack

```python
def factorial(n: int) -> int:
    """
    Recursive function demonstrating call stack:

    factorial(4) → 4 * factorial(3) → 4 * 3 * factorial(2) → ...
    """
    if n <= 1:
        return 1
    return n * factorial(n - 1)

# Call stack visualization:
# factorial(4):
# ┌────────────────────┐
# │ factorial(4)       │  n=4, waiting for factorial(3)
# ├────────────────────┤
# │ factorial(3)       │  n=3, waiting for factorial(2)
# ├────────────────────┤
# │ factorial(2)       │  n=2, waiting for factorial(1)
# ├────────────────────┤
# │ factorial(1)       │  n=1, returns 1
# └────────────────────┘
```

### Undo/Redo

```python
class UndoRedoManager:
    def __init__(self):
        self.undo_stack = []
        self.redo_stack = []

    def execute(self, action: str) -> None:
        self.undo_stack.append(action)
        self.redo_stack.clear()  # Clear redo on new action
        print(f"Executed: {action}")

    def undo(self) -> str:
        if not self.undo_stack:
            raise IndexError("Nothing to undo")
        action = self.undo_stack.pop()
        self.redo_stack.append(action)
        print(f"Undone: {action}")
        return action

    def redo(self) -> str:
        if not self.redo_stack:
            raise IndexError("Nothing to redo")
        action = self.redo_stack.pop()
        self.undo_stack.append(action)
        print(f"Redone: {action}")
        return action

    def can_undo(self) -> bool:
        return len(self.undo_stack) > 0

    def can_redo(self) -> bool:
        return len(self.redo_stack) > 0

# Usage
manager = UndoRedoManager()
manager.execute("Type 'Hello'")
manager.execute("Type 'World'")
manager.undo()  # Undone: Type 'World'
manager.redo()  # Redone: Type 'World'
```

### Browser Navigation

```python
class Browser:
    def __init__(self):
        self.back_stack = []
        self.forward_stack = []
        self.current = None

    def visit(self, url: str) -> None:
        if self.current:
            self.back_stack.append(self.current)
        self.current = url
        self.forward_stack.clear()
        print(f"Visited: {url}")

    def back(self) -> str:
        if not self.back_stack:
            raise IndexError("No pages in history")
        self.forward_stack.append(self.current)
        self.current = self.back_stack.pop()
        print(f"Went back to: {self.current}")
        return self.current

    def forward(self) -> str:
        if not self.forward_stack:
            raise IndexError("No forward pages")
        self.back_stack.append(self.current)
        self.current = self.forward_stack.pop()
        print(f"Went forward to: {self.current}")
        return self.current

    def current_page(self) -> str:
        return self.current

# Usage
browser = Browser()
browser.visit("google.com")
browser.visit("github.com")
browser.visit("stackoverflow.com")
browser.back()   # Went back to: github.com
browser.back()   # Went back to: google.com
browser.forward()  # Went forward to: github.com
```

---

## Related Data Structures

| Data Structure | Description | Access Pattern |
|----------------|-------------|----------------|
| **Queue** | FIFO - First In, First Out | Opposite of stack |
| **Deque** | Double-ended queue | Both ends |
| **Priority Queue** | Highest priority first | Priority-based |
| **Monotonic Stack** | Stack with ordering constraint | Next greater element |

---

## Summary

| Aspect | Description |
|--------|-------------|
| Principle | LIFO (Last In, First Out) |
| Key Operations | push, pop, peek |
| Time Complexity | O(1) for all primary operations |
| Space Complexity | O(n) |
| Best For | Reversal, nesting, backtracking |
| Avoid When | Need random access or FIFO |
