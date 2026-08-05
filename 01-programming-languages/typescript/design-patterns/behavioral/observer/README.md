# Observer Pattern (TypeScript)

## Overview

The Observer pattern defines a one-to-many dependency between objects so that when one
object changes state, all its dependents are notified. TypeScript's generics enable
type-safe observer implementations.

## When to Use

- Changes to one object require changing others
- Don't know how many objects need to be changed
- Objects should notify observers without coupling
- Event-driven systems

## TypeScript Implementation

### Generic Observer

```typescript
interface Observer<T> {
  update(data: T): void;
}

class Subject<T> {
  private observers: Observer<T>[] = [];

  attach(observer: Observer<T>): void {
    this.observers.push(observer);
  }

  detach(observer: Observer<T>): void {
    this.observers = this.observers.filter(o => o !== observer);
  }

  notify(data: T): void {
    this.observers.forEach(observer => observer.update(data));
  }
}
```

### Typed EventEmitter

```typescript
class TypedEmitter<T extends Record<string, any>> {
  private events: { [K in keyof T]?: Array<(data: T[K]) => void> } = {};

  on<K extends keyof T>(event: K, callback: (data: T[K]) => void): () => void {
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event]!.push(callback);

    return () => {
      this.events[event] = this.events[event]!.filter(cb => cb !== callback);
    };
  }

  emit<K extends keyof T>(event: K, data: T[K]): void {
    if (this.events[event]) {
      this.events[event]!.forEach(callback => callback(data));
    }
  }
}

interface Events {
  message: { text: string; sender: string };
  error: { code: number; message: string };
}

const emitter = new TypedEmitter<Events>();
```

### PubSub

```typescript
class TypedPubSub<T extends Record<string, any>> {
  private subscribers: { [K in keyof T]?: Array<(data: T[K]) => void> } = {};

  subscribe<K extends keyof T>(topic: K, callback: (data: T[K]) => void): () => void {
    if (!this.subscribers[topic]) {
      this.subscribers[topic] = [];
    }
    this.subscribers[topic]!.push(callback);

    return () => {
      this.subscribers[topic] = this.subscribers[topic]!.filter(cb => cb !== callback);
    };
  }

  publish<K extends keyof T>(topic: K, data: T[K]): void {
    if (this.subscribers[topic]) {
      this.subscribers[topic]!.forEach(callback => callback(data));
    }
  }
}
```

## Best Practices

- Use generics for type safety
- Implement unsubscribe functionality
- Keep observer interface minimal
- Handle errors in observers
- Document notification order

## Interview Questions

1. What is the difference between Observer and PubSub?
2. How do you prevent memory leaks in Observer?
3. Can observers be notified asynchronously?
4. When should you use events vs custom observer?
5. How do you handle observer errors?

## References

- TypeScript Handbook: Generics
- "TypeScript Design Patterns" by Vaskaran Sarcar
- Node.js EventEmitter documentation
