# Singleton Pattern (TypeScript)

## Overview

The Singleton pattern ensures a class has only one instance. TypeScript's access
modifiers and generics enable type-safe singleton implementations with compile-time
guarantees.

## When to Use

- Managing global state
- Database connections
- Configuration objects
- Logging services
- Cache implementations

## TypeScript Implementation

### Class Singleton

```typescript
class Singleton {
  private static instance: Singleton;
  private data: number;

  private constructor() {
    this.data = Math.random();
  }

  static getInstance(): Singleton {
    if (!Singleton.instance) {
      Singleton.instance = new Singleton();
    }
    return Singleton.instance;
  }

  getData(): number {
    return this.data;
  }
}
```

### Generic Singleton

```typescript
class GenericSingleton<T> {
  private static instances: Map<string, any> = new Map();

  static getInstance<T>(key: string, factory: () => T): T {
    if (!GenericSingleton.instances.has(key)) {
      GenericSingleton.instances.set(key, factory());
    }
    return GenericSingleton.instances.get(key) as T;
  }
}
```

### Module Singleton

```typescript
// config.ts
class Config {
  private settings: Map<string, any> = new Map();

  get<T>(key: string): T | undefined {
    return this.settings.get(key) as T;
  }

  set<T>(key: string, value: T): void {
    this.settings.set(key, value);
  }
}

export const config = new Config();
```

### Lazy Singleton

```typescript
class LazySingleton<T> {
  private instance: T | null = null;
  private factory: () => T;

  constructor(factory: () => T) {
    this.factory = factory;
  }

  getInstance(): T {
    if (!this.instance) {
      this.instance = this.factory();
    }
    return this.instance;
  }
}
```

## Best Practices

- Use private constructor to prevent instantiation
- Consider lazy initialization for expensive objects
- Use dependency injection as alternative
- Document singleton usage clearly
- Consider thread safety in multi-threaded environments

## Interview Questions

1. Why is singleton considered anti-pattern in some cases?
2. How does TypeScript enforce singleton behavior?
3. Can you make singleton thread-safe in TypeScript?
4. When should you use singleton vs dependency injection?
5. How do you test code using singletons?

## References

- TypeScript Handbook: Classes
- "Learning JavaScript Design Patterns" by Addy Osmani
- "TypeScript Design Patterns" by Vaskaran Sarcar
