# Visitor Pattern

## Overview
The Visitor pattern represents an operation to be performed on elements of an object structure. It lets you define a new operation without changing the classes of the elements on which it operates.

## When to Use
- Many unrelated operations on object structures
- Class definitions rarely change but operations frequently change
- Need to group related operations without placing them in the class
- Compiler AST traversal, document processing

## Code Structure
```
Visitor (interface)         Element (interface)
    |                           |
ConcreteVisitor          accept(Visitor)
    |                           |
visit(ElementA)         ConcreteElementA
visit(ElementB)         ConcreteElementB
```

## Key Benefits
- Easy to add new operations
- Groups related operations together
- Can accumulate state while visiting elements
- Follows Open/Closed Principle

## Common Mistakes
- Breaking encapsulation by exposing element internals
- Creating circular dependencies between visitor and elements
- Over-complicating simple hierarchies

## Interview Questions
1. What is double dispatch in Visitor pattern?
2. How does Visitor differ from Strategy pattern?
3. What happens when new element types are added?
4. When would you NOT use the Visitor pattern?
