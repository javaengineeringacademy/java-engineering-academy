# Abstract Factory Pattern (TypeScript)

## Overview

The Abstract Factory pattern provides an interface for creating families of related
objects without specifying their concrete classes. TypeScript's interfaces ensure
consistency across factory implementations.

## When to Use

- Creating families of related objects
- System needs to be independent of object creation
- Need to enforce constraints between related objects
- Platform-independent UI development

## TypeScript Implementation

### Typed Abstract Factory

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

class DarkButton implements Button {
  render(): string {
    return '<button class="dark">Dark Button</button>';
  }
}

class DarkInput implements Input {
  render(): string {
    return '<input class="dark" />';
  }
}

class DarkThemeFactory implements UIFactory {
  createButton(): Button {
    return new DarkButton();
  }
  createInput(): Input {
    return new DarkInput();
  }
}
```

### Generic Abstract Factory

```typescript
interface Factory<T> {
  create(): T;
}

class AbstractFactory<T> {
  private factories: Map<string, Factory<T>> = new Map();

  register(name: string, factory: Factory<T>): void {
    this.factories.set(name, factory);
  }

  create(name: string): T {
    const factory = this.factories.get(name);
    if (!factory) {
      throw new Error(`Factory ${name} not found`);
    }
    return factory.create();
  }
}
```

### Theme System

```typescript
interface Theme {
  primary: string;
  secondary: string;
}

interface ThemeFactory {
  createButton(): string;
  createInput(): string;
}

const lightTheme: Theme = { primary: '#fff', secondary: '#000' };
const darkTheme: Theme = { primary: '#333', secondary: '#fff' };

class LightThemeFactory implements ThemeFactory {
  createButton(): string {
    return `<button style="background:${lightTheme.primary}">Light</button>`;
  }
  createInput(): string {
    return `<input style="border-color:${lightTheme.secondary}" />`;
  }
}
```

## Best Practices

- Keep factory families consistent
- Use interfaces for product types
- Document relationships between products
- Consider using dependency injection
- Use TypeScript generics for type safety

## Interview Questions

1. How does Abstract Factory differ from Factory Method?
2. When should you use Abstract Factory in TypeScript?
3. Can Abstract Factory return different types?
4. How do you extend Abstract Factory with new products?
5. When should you choose Abstract Factory over simple Factory?

## References

- TypeScript Handbook: Interfaces
- "TypeScript Design Patterns" by Vaskaran Sarcar
- Gang of Four, "Design Patterns"
