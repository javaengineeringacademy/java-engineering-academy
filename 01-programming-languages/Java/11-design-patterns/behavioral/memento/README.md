# Memento Pattern

## Overview
The Memento pattern captures and externalizes an object's internal state so it can be restored later without violating encapsulation. It provides undo/redo capabilities.

## When to Use
- Need to save and restore object state
- Undo/Redo functionality required
- Direct access to fields would break encapsulation
- Text editors, game saves, transaction rollback

## Code Structure
```
Editor (Originator)       History (Caretaker)
    |                         |
save() -> Memento         stores Mementos
restore(Memento)          manages undo/redo
```

## Key Benefits
- Preserves encapsulation
- Simplifies originator by externalizing state
- Easy to implement undo/redo
- Mementos are independent of each other

## Common Mistakes
- Storing too much state causing memory issues
- Not implementing proper state isolation
- Exposing memento internals to other objects

## Interview Questions
1. What are the three roles in the Memento pattern?
2. How does Memento preserve encapsulation?
3. What is the difference between Memento and Command?
4. How would you implement redo functionality?
