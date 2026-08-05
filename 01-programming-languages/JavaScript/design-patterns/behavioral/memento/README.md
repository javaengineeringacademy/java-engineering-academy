# Memento Pattern (JavaScript)

## Overview

The Memento pattern provides the ability to restore an object to its previous state.
JavaScript's object copying and closures make memento implementations straightforward.

## When to Use

- Need to save and restore object state
- Implementing undo/redo functionality
- Capturing snapshots without exposing internals
- Transaction rollback mechanisms

## JavaScript Implementation

### Basic Memento

```javascript
class Memento {
  constructor(state) {
    this.state = JSON.parse(JSON.stringify(state));
    this.timestamp = Date.now();
  }

  getState() {
    return JSON.parse(JSON.stringify(this.state));
  }
}

class Originator {
  constructor() {
    this.state = {};
  }

  setState(state) {
    this.state = state;
  }

  getState() {
    return { ...this.state };
  }

  save() {
    return new Memento(this.state);
  }

  restore(memento) {
    this.state = memento.getState();
  }
}
```

### Caretaker

```javascript
class Caretaker {
  constructor(originator) {
    this.originator = originator;
    this.history = [];
    this.currentIndex = -1;
  }

  save() {
    this.history.push(this.originator.save());
    this.currentIndex++;
  }

  undo() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.originator.restore(this.history[this.currentIndex]);
    }
  }

  redo() {
    if (this.currentIndex < this.history.length - 1) {
      this.currentIndex++;
      this.originator.restore(this.history[this.currentIndex]);
    }
  }
}
```

### Snapshot Pattern

```javascript
function createSnapshot(initialState) {
  let state = { ...initialState };
  const history = [];

  return {
    getState: () => ({ ...state }),
    setState: (newState) => {
      history.push({ ...state });
      state = { ...newState };
    },
    undo: () => {
      if (history.length > 0) {
        state = history.pop();
      }
    }
  };
}
```

### Deep Memento

```javascript
function createDeepMemento(obj) {
  return {
    save: () => JSON.parse(JSON.stringify(obj)),
    restore: (memento) => JSON.parse(JSON.stringify(memento))
  };
}
```

## Best Practices

- Use JSON serialization for simple deep copy
- Consider using structuredClone for complex objects
- Limit history size to prevent memory issues
- Document state capture semantics
- Use memento for transactional operations

## Interview Questions

1. What is the difference between Memento and Command?
2. How do you handle large object states?
3. Can memento be used across sessions?
4. When should you use Memento vs Command for undo?
5. How do you implement memento with serialization?

## References

- MDN: Memento Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Head First Design Patterns" by Freeman
