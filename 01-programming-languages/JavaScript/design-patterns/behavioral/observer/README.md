# Observer Pattern (JavaScript)

## Overview

The Observer pattern defines a one-to-many dependency between objects so that when one
object changes state, all its dependents are notified. JavaScript provides EventEmitter
class for built-in observer implementations.

## When to Use

- Changes to one object require changing others
- Don't know how many objects need to be changed
- Objects should notify observers without coupling
- Event-driven systems

## JavaScript Implementation

### EventEmitter

```javascript
class EventEmitter {
  constructor() {
    this.events = {};
  }

  on(event, callback) {
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event].push(callback);
    return () => this.off(event, callback);
  }

  off(event, callback) {
    if (this.events[event]) {
      this.events[event] = this.events[event].filter(cb => cb !== callback);
    }
  }

  emit(event, ...args) {
    if (this.events[event]) {
      this.events[event].forEach(callback => callback(...args));
    }
  }
}
```

### PubSub Pattern

```javascript
class PubSub {
  constructor() {
    this.subscribers = {};
  }

  subscribe(topic, callback) {
    if (!this.subscribers[topic]) {
      this.subscribers[topic] = [];
    }
    this.subscribers[topic].push(callback);

    return {
      unsubscribe: () => {
        this.subscribers[topic] = this.subscribers[topic].filter(cb => cb !== callback);
      }
    };
  }

  publish(topic, data) {
    if (this.subscribers[topic]) {
      this.subscribers[topic].forEach(callback => callback(data));
    }
  }
}
```

### Observable

```javascript
class Observable {
  constructor() {
    this.observers = new Set();
  }

  subscribe(observer) {
    this.observers.add(observer);
    return () => this.observers.delete(observer);
  }

  notify(data) {
    this.observers.forEach(observer => observer(data));
  }
}
```

## Best Practices

- Use weak references to prevent memory leaks
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

- MDN: EventEmitter
- "Learning JavaScript Design Patterns" by Addy Osmani
- Node.js EventEmitter documentation
