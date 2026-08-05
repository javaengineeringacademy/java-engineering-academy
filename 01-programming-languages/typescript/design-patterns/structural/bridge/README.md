# Bridge Pattern (TypeScript)

## Overview

The Bridge pattern decouples an abstraction from its implementation so that the two
can vary independently. TypeScript's interfaces enable compile-time separation of
abstraction and implementation.

## When to Use

- Avoiding permanent binding between abstraction and implementation
- Both abstraction and implementation should be extensible
- Changes in implementation should not affect clients
- Sharing implementation across multiple objects

## TypeScript Implementation

### Typed Bridge

```typescript
interface Implementation {
  operationImpl(): string;
}

class Abstraction {
  constructor(protected implementation: Implementation) {}

  operation(): string {
    return this.implementation.operationImpl();
  }
}

class RefinedAbstraction extends Abstraction {
  additionalOperation(): string {
    return `Refined: ${this.implementation.operationImpl()}`;
  }
}
```

### Generic Bridge

```typescript
class GenericAbstraction<T> {
  constructor(protected implementation: { operationImpl(): T }) {}

  operation(): T {
    return this.implementation.operationImpl();
  }
}
```

### Event Bridge

```typescript
interface Emitter {
  emit(event: string, data: any): void;
  on(event: string, callback: (data: any) => void): void;
}

class BridgeAbstraction {
  constructor(private emitter: Emitter) {}

  send(event: string, data: any): void {
    this.emitter.emit(event, data);
  }
}
```

## Best Practices

- Keep abstraction and implementation hierarchies separate
- Use interfaces for implementation contracts
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

- TypeScript Handbook: Interfaces
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Pattern-Oriented Software Architecture" by Buschmann
