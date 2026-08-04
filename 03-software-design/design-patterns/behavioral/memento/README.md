# Memento Pattern

The Memento pattern captures and externalizes an object's internal state so it can be restored later without violating encapsulation.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Memento](#basic-memento)
3. [Undo with Memento](#undo-with-memento)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Memento?

Memento captures object state and stores it externally for later restoration.

```
Originator ──▶ Memento (state)
      │
   save/restore
      │
Caretaker ──▶ List<Memento>
```

### When to Use

- Undo/redo functionality
- State snapshots
- Transaction rollback

---

## Basic Memento

### Text Editor

```java
// Originator
public class Editor {
    private String content;
    private int cursorPosition;

    public void type(String words) {
        content += words;
        cursorPosition += words.length();
    }

    public String getContent() { return content; }
    public int getCursorPosition() { return cursorPosition; }

    public Memento save() {
        return new Memento(content, cursorPosition);
    }

    public void restore(Memento memento) {
        this.content = memento.getContent();
        this.cursorPosition = memento.getCursorPosition();
    }
}

// Memento
public class Memento {
    private final String content;
    private final int cursorPosition;

    public Memento(String content, int cursorPosition) {
        this.content = content;
        this.cursorPosition = cursorPosition;
    }

    public String getContent() { return content; }
    public int getCursorPosition() { return cursorPosition; }
}

// Caretaker
public class History {
    private final Stack<Memento> mementos = new Stack<>();

    public void push(Memento memento) {
        mementos.push(memento);
    }

    public Memento pop() {
        if (mementos.isEmpty()) throw new RuntimeException("No history");
        return mementos.pop();
    }

    public boolean hasHistory() { return !mementos.isEmpty(); }
}

// Usage
Editor editor = new Editor();
History history = new History();

editor.type("Hello");
history.push(editor.save());

editor.type(" World");
System.out.println(editor.getContent());  // "Hello World"

editor.restore(history.pop());
System.out.println(editor.getContent());  // "Hello"
```

---

## Undo with Memento

### Multiple Levels of Undo

```java
public class UndoManager {
    private final List<Memento> states = new ArrayList<>();
    private int current = -1;

    public void saveState(Editor editor) {
        // Remove future states if we're not at the end
        states.subList(current + 1, states.size()).clear();
        states.add(editor.save());
        current++;
    }

    public boolean canUndo() { return current > 0; }
    public boolean canRedo() { return current < states.size() - 1; }

    public void undo(Editor editor) {
        if (canUndo()) {
            current--;
            editor.restore(states.get(current));
        }
    }

    public void redo(Editor editor) {
        if (canRedo()) {
            current++;
            editor.restore(states.get(current));
        }
    }
}

// Usage
Editor editor = new Editor();
UndoManager manager = new UndoManager();

manager.saveState(editor);
editor.type("Hello");
manager.saveState(editor);
editor.type(" World");
manager.saveState(editor);

System.out.println(editor.getContent());  // "Hello World"

manager.undo(editor);
System.out.println(editor.getContent());  // "Hello"

manager.undo(editor);
System.out.println(editor.getContent());  // ""

manager.redo(editor);
System.out.println(editor.getContent());  // "Hello"
```

---

## Best Practices

### Do

```java
// 1. Keep mementos immutable
public class Memento {
    private final String state;  // final = immutable
    public Memento(String state) { this.state = state; }
    public String getState() { return state; }
}

// 2. Limit history size
if (states.size() > MAX_HISTORY) {
    states.remove(0);
}
```

### Don't

```java
// 1. Don't expose memento internals
// Memento should be opaque to caretaker

// 2. Don't store too many states
// Memory can grow quickly
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Memento** | Captures object state |
| **Originator** | Creates and restores from memento |
| **Caretaker** | Stores mementos |
| **Encapsulation** | State is externalized safely |
| **Immutable** | Mementos should be immutable |
| **Use Cases** | Undo, snapshots, rollback |
