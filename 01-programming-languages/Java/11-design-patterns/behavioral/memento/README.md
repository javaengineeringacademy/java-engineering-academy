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

## Performance

Memento creation cost depends on state size — shallow copy is O(1), deep copy is O(n) for collections. Storing many mementos consumes O(n × state_size) memory. For large objects, use incremental snapshots (only store deltas). Game saves and document history benefit from compression. Command pattern is often combined for undo without full state snapshots.

## Examples

```java
// Text editor with undo/redo
class EditorMemento {
    private final String content;
    private final int cursorPosition;
    
    EditorMemento(String content, int cursorPosition) {
        this.content = content;
        this.cursorPosition = cursorPosition;
    }
    
    String getContent() { return content; }
    int getCursorPosition() { return cursorPosition; }
}

class Editor {
    private String content = "";
    private int cursorPosition = 0;
    
    void type(String text) {
        content = content.substring(0, cursorPosition) + text + 
                  content.substring(cursorPosition);
        cursorPosition += text.length();
    }
    
    EditorMemento save() {
        return new EditorMemento(content, cursorPosition);
    }
    
    void restore(EditorMemento memento) {
        this.content = memento.getContent();
        this.cursorPosition = memento.getCursorPosition();
    }
    
    String getContent() { return content; }
    int getCursorPosition() { return cursorPosition; }
}

class History {
    private final Deque<EditorMemento> undoStack = new ArrayDeque<>();
    private final Deque<EditorMemento> redoStack = new ArrayDeque<>();
    
    void push(EditorMemento memento) {
        undoStack.push(memento);
        redoStack.clear();
    }
    
    EditorMemento undo() {
        if (undoStack.size() > 1) {
            redoStack.push(undoStack.pop());
            return undoStack.peek();
        }
        return undoStack.peek();
    }
    
    EditorMemento redo() {
        if (!redoStack.isEmpty()) {
            EditorMemento memento = redoStack.pop();
            undoStack.push(memento);
            return memento;
        }
        return null;
    }
}

// Usage
Editor editor = new Editor();
History history = new History();
history.push(editor.save());

editor.type("Hello");
history.push(editor.save());

editor.type(" World");
System.out.println(editor.getContent()); // Hello World

editor.restore(history.undo());
System.out.println(editor.getContent()); // Hello
```

## Internal Working

The originator (Editor) creates mementos containing its internal state. The caretaker (History) stores mementos without inspecting or modifying them. When restoration is needed, the originator receives a memento and restores its state from it. The memento is opaque to the caretaker — only the originator can read its contents. This preserves encapsulation while enabling state externalization.

## Why This Concept Exists

Direct field access breaks encapsulation — external code cannot be trusted to restore state correctly. Memento lets the originator capture and restore its own state without exposing internals. The caretaker stores mementos as opaque objects. This is essential for undo/redo, transaction rollback, and game save systems. The pattern separates state storage from state management.

## Pitfalls

1. **Memory consumption**: Storing full state snapshots for large objects is expensive
2. **Encapsulation leakage**: If memento exposes originator internals, the pattern fails
3. **Memento size**: Deep copy of large collections can be slow and memory-intensive
4. **Concurrency**: Mementos are not thread-safe — synchronize access in multi-threaded contexts
5. **Overuse**: Simple objects with few fields don't need memento — direct copy is simpler

## References

- [Refactoring.Guru - Memento Pattern](https://refactoring.guru/design-patterns/memento)
- [Head First Design Patterns - Memento Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java Serializable as Memento](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/Serializable.html)
