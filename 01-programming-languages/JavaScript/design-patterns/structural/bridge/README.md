# Bridge Pattern (JavaScript)

## Overview

The Bridge pattern decouples an abstraction from its implementation so that the two
can vary independently. JavaScript's dynamic typing and duck typing make bridges
particularly flexible.

## When to Use

- Avoiding permanent binding between abstraction and implementation
- Both abstraction and implementation should be extensible
- Changes in implementation should not affect clients
- Sharing implementation across multiple objects

## JavaScript Implementation

### Basic Bridge

```javascript
class Abstraction {
  constructor(implementation) {
    this.implementation = implementation;
  }

  operation() {
    return this.implementation.operationImpl();
  }
}

class RefinedAbstraction extends Abstraction {
  additionalOperation() {
    return `Refined: ${this.implementation.operationImpl()}`;
  }
}

class ImplementationA {
  operationImpl() {
    return 'ImplementationA';
  }
}

class ImplementationB {
  operationImpl() {
    return 'ImplementationB';
  }
}
```

### Functional Bridge

```javascript
function createAbstraction(implementation) {
  return {
    operation: () => implementation.operationImpl(),
    additional: () => `Refined: ${implementation.operationImpl()}`
  };
}

function createImplA() {
  return { operationImpl: () => 'A' };
}

function createImplB() {
  return { operationImpl: () => 'B' };
}
```

### Event Bridge

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
  }

  emit(event, data) {
    if (this.events[event]) {
      this.events[event].forEach(cb => cb(data));
    }
  }
}

class AbstractionBridge {
  constructor(emitter) {
    this.emitter = emitter;
  }

  send(event, data) {
    this.emitter.emit(event, data);
  }
}
```

## Best Practices

- Keep abstraction and implementation hierarchies separate
- Use interfaces or duck typing for implementations
- Document extension points clearly
- Use Bridge when inheritance hierarchy grows
- Consider using dependency injection

## Interview Questions

1. How does Bridge differ from Adapter?
2. What is the relationship between Bridge and Strategy?
3. When should you use Bridge over multiple inheritance?
4. How do you extend implementation without changing abstraction?
5. Can Bridge be combined with Abstract Factory?

## References

- MDN: Bridge Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Pattern-Oriented Software Architecture" by Buschmann
