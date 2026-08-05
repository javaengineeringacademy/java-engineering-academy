# Factory Pattern (TypeScript)

## Overview

The Factory pattern provides an interface for creating objects without specifying their
concrete classes. TypeScript's generics and type inference enable type-safe factory
implementations.

## When to Use

- Object creation logic is complex
- Need to create different types based on input
- Avoiding code duplication in object creation
- Creating objects from configuration

## TypeScript Implementation

### Generic Factory

```typescript
interface Product {
  name: string;
  price: number;
}

class ConcreteProductA implements Product {
  name = 'Product A';
  price = 10;
}

class ConcreteProductB implements Product {
  name = 'Product B';
  price = 20;
}

class Factory {
  static create(type: 'A' | 'B'): Product {
    const products = {
      A: new ConcreteProductA(),
      B: new ConcreteProductB()
    };
    return products[type];
  }
}
```

### Abstract Factory

```typescript
interface Button {
  render(): string;
}

interface Input {
  render(): string;
}

interface UIFactory {
  createButton(): Button;
  createInput(): Input;
}

class DarkThemeFactory implements UIFactory {
  createButton(): Button {
    return { render: () => 'Dark Button' };
  }
  createInput(): Input {
    return { render: () => 'Dark Input' };
  }
}
```

### Factory with Generics

```typescript
class TypedFactory<T> {
  private creators: Map<string, () => T> = new Map();

  register(type: string, creator: () => T): void {
    this.creators.set(type, creator);
  }

  create(type: string): T {
    const creator = this.creators.get(type);
    if (!creator) {
      throw new Error(`Unknown type: ${type}`);
    }
    return creator();
  }
}
```

### Factory Method Pattern

```typescript
abstract class Creator {
  abstract createProduct(): Product;

  someOperation(): string {
    const product = this.createProduct();
    return `Created: ${product.name}`;
  }
}

class ConcreteCreatorA extends Creator {
  createProduct(): Product {
    return new ConcreteProductA();
  }
}
```

## Best Practices

- Use generics for type safety
- Keep factory functions small and focused
- Use descriptive names for factory methods
- Document return type expectations
- Consider using dependency injection

## Interview Questions

1. What is the difference between Factory and Abstract Factory?
2. When should you use factory function vs constructor?
3. Can factory pattern work with TypeScript generics?
4. How do you handle factory errors?
5. When is factory better than direct object creation?

## References

- TypeScript Handbook: Generics
- "Learning JavaScript Design Patterns" by Addy Osmani
- "TypeScript Design Patterns" by Vaskaran Sarcar
