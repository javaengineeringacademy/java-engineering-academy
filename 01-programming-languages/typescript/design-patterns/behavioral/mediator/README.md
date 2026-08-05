# Mediator Pattern (TypeScript)

## Overview

The Mediator pattern defines an object that encapsulates how a set of objects interact.
TypeScript's generics and interfaces enable type-safe mediator implementations.

## When to Use

- Set of objects communicate in complex ways
- Reuse object is difficult due to dependencies
- Custom behavior distributed across several classes
- Event-driven communication systems

## TypeScript Implementation

### Generic Mediator

```typescript
interface Mediator<T> {
  notify(sender: T, event: string): void;
}

class Colleague<T> {
  constructor(protected mediator: Mediator<T>) {}

  send(event: string): void {
    this.mediator.notify(this as unknown as T, event);
  }
}
```

### Typed Event Bus

```typescript
interface EventMap {
  [key: string]: any;
}

class TypedEventBus<T extends EventMap> {
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
```

### Chat Room

```typescript
class ChatRoom {
  private users: Map<string, ChatUser> = new Map();

  addUser(user: ChatUser): void {
    this.users.set(user.name, user);
  }

  send(message: string, from: ChatUser): void {
    this.users.forEach((user, name) => {
      if (name !== from.name) {
        user.receive(message, from.name);
      }
    });
  }
}

class ChatUser {
  constructor(public name: string, private room: ChatRoom) {}

  send(message: string): void {
    this.room.send(message, this);
  }

  receive(message: string, from: string): void {
    console.log(`${this.name} received from ${from}: ${message}`);
  }
}
```

## Best Practices

- Use generics for type safety
- Keep mediator focused on coordination
- Avoid putting business logic in mediator
- Document component communication patterns
- Consider using event bus for loose coupling

## Interview Questions

1. How does Mediator differ from Observer?
2. Can mediator handle asynchronous communication?
3. How do you test code with mediator?
4. When should you avoid using Mediator?
5. How do you handle mediator in microservices?

## References

- TypeScript Handbook: Generics
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Head First Design Patterns" by Freeman
