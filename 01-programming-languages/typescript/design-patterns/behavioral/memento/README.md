# Memento Pattern (TypeScript)

## Overview

The Memento pattern provides the ability to restore an object to its previous state.
TypeScript's type system enables type-safe memento implementations.

## When to Use

- Need to save and restore object state
- Implementing undo/redo functionality
- Capturing snapshots without exposing internals
- Transaction rollback mechanisms

## TypeScript Implementation

### Generic Memento

```typescript
interface Memento<T> {
  getState(): T;
  getTimestamp(): number;
}

class ConcreteMemento<T> implements Memento<T> {
  private state: T;
  private timestamp: number;

  constructor(state: T) {
    this.state = JSON.parse(JSON.stringify(state));
    this.timestamp = Date.now();
  }

  getState(): T {
    return JSON.parse(JSON.stringify(this.state));
  }

  getTimestamp(): number {
    return this.timestamp;
  }
}
```

### Originator

```typescript
class Originator<T> {
  private state: T;

  constructor(initialState: T) {
    this.state = initialState;
  }

  setState(state: T): void {
    this.state = state;
  }

  getState(): T {
    return { ...this.state };
  }

  save(): Memento<T> {
    return new ConcreteMemento(this.state);
  }

  restore(memento: Memento<T>): void {
    this.state = memento.getState();
  }
}
```

### Caretaker

```typescript
class Caretaker<T> {
  private history: Memento<T>[] = [];
  private currentIndex: number = -1;

  constructor(private originator: Originator<T>) {}

  save(): void {
    this.history.push(this.originator.save());
    this.currentIndex++;
  }

  undo(): void {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.originator.restore(this.history[this.currentIndex]);
    }
  }

  redo(): void {
    if (this.currentIndex < this.history.length - 1) {
      this.currentIndex++;
      this.originator.restore(this.history[this.currentIndex]);
    }
  }
}
```

### Snapshot Pattern

```typescript
function createSnapshot<T>(initialState: T) {
  let state: T = { ...initialState };
  const history: T[] = [];

  return {
    getState: (): T => ({ ...state }),
    setState: (newState: T): void => {
      history.push({ ...state });
      state = { ...newState };
    },
    undo: (): void => {
      if (history.length > 0) {
        state = history.pop()!;
      }
    }
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

- TypeScript Handbook: Classes
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Head First Design Patterns" by Freeman
