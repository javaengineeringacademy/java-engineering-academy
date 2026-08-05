# Strategy Pattern (TypeScript)

## Overview

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes
them interchangeable. TypeScript's interfaces and generics enable type-safe strategy
implementations.

## When to Use

- Multiple algorithms for specific task
- Need to switch algorithms at runtime
- Avoiding conditional statements
- Isolating algorithm implementation

## TypeScript Implementation

### Generic Strategy

```typescript
interface Strategy<T, R> {
  execute(input: T): R;
}

class Context<T, R> {
  private strategy: Strategy<T, R>;

  constructor(strategy: Strategy<T, R>) {
    this.strategy = strategy;
  }

  setStrategy(strategy: Strategy<T, R>): void {
    this.strategy = strategy;
  }

  execute(input: T): R {
    return this.strategy.execute(input);
  }
}
```

### Functional Strategy

```typescript
type StrategyFn<T, R> = (input: T) => R;

class FunctionalContext<T, R> {
  private strategy: StrategyFn<T, R>;

  constructor(strategy: StrategyFn<T, R>) {
    this.strategy = strategy;
  }

  setStrategy(strategy: StrategyFn<T, R>): void {
    this.strategy = strategy;
  }

  execute(input: T): R {
    return this.strategy(input);
  }
}
```

### Strategy Registry

```typescript
class StrategyRegistry<T, R> {
  private strategies: Map<string, Strategy<T, R>> = new Map();

  register(name: string, strategy: Strategy<T, R>): void {
    this.strategies.set(name, strategy);
  }

  get(name: string): Strategy<T, R> | undefined {
    return this.strategies.get(name);
  }

  execute(name: string, input: T): R {
    const strategy = this.strategies.get(name);
    if (!strategy) {
      throw new Error(`Strategy ${name} not found`);
    }
    return strategy.execute(input);
  }
}
```

### Validation Strategy

```typescript
interface ValidationStrategy<T> {
  validate(value: T): boolean;
}

class ValidationContext<T> {
  private strategy: ValidationStrategy<T>;

  constructor(strategy: ValidationStrategy<T>) {
    this.strategy = strategy;
  }

  validate(value: T): boolean {
    return this.strategy.validate(value);
  }
}
```

## Best Practices

- Use generics for type safety
- Keep strategy interface small
- Consider using functions for simple strategies
- Document strategy selection criteria
- Make strategies stateless when possible

## Interview Questions

1. How does Strategy differ from State?
2. When should you use functions over classes?
3. Can strategies have state?
4. How do you handle strategy selection?
5. When is Strategy better than inheritance?

## References

- TypeScript Handbook: Generics
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Functional Programming in TypeScript"
