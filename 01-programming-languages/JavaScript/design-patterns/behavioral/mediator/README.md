# Mediator Pattern (JavaScript)

## Overview

The Mediator pattern defines an object that encapsulates how a set of objects interact.
JavaScript's event-driven nature makes mediator implementations natural and effective.

## When to Use

- Set of objects communicate in complex ways
- Reuse object is difficult due to dependencies
- Custom behavior distributed across several classes
- Event-driven communication systems

## JavaScript Implementation

### Basic Mediator

```javascript
class Mediator {
  constructor() {
    this.users = new Set();
  }

  register(user) {
    this.users.add(user);
  }

  send(message, sender) {
    this.users.forEach(user => {
      if (user !== sender) {
        user.receive(message);
      }
    });
  }
}

class User {
  constructor(name, mediator) {
    this.name = name;
    this.mediator = mediator;
  }

  send(message) {
    console.log(`${this.name} sending: ${message}`);
    this.mediator.send(message, this);
  }

  receive(message) {
    console.log(`${this.name} received: ${message}`);
  }
}
```

### Event-Based Mediator

```javascript
class EventBus {
  constructor() {
    this.events = {};
  }

  on(event, callback) {
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event].push(callback);
  }

  emit(event, data) {
    if (this.events[event]) {
      this.events[event].forEach(cb => cb(data));
    }
  }

  off(event, callback) {
    if (this.events[event]) {
      this.events[event] = this.events[event].filter(cb => cb !== callback);
    }
  }
}
```

### Chat Room

```javascript
class ChatRoom {
  constructor() {
    this.users = new Map();
  }

  addUser(user) {
    this.users.set(user.name, user);
  }

  send(message, from) {
    this.users.forEach((user, name) => {
      if (name !== from.name) {
        user.receive(message, from.name);
      }
    });
  }
}
```

## Best Practices

- Keep mediator focused on coordination
- Avoid putting business logic in mediator
- Use interfaces for mediator abstraction
- Document component communication patterns
- Consider using event bus for loose coupling

## Interview Questions

1. How does Mediator differ from Observer?
2. Can mediator handle asynchronous communication?
3. How do you test code with mediator?
4. When should you avoid using Mediator?
5. How do you handle mediator in microservices?

## References

- MDN: Mediator Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Head First Design Patterns" by Freeman
