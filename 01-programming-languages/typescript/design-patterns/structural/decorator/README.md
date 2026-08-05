# Decorator Pattern (TypeScript)

## Overview

The Decorator pattern attaches additional responsibilities to objects dynamically.
TypeScript's type system and higher-order functions enable type-safe decorator
implementations.

## When to Use

- Adding responsibilities dynamically
- Extending functionality without subclassing
- Creating layered behaviors
- Implementing cross-cutting concerns

## TypeScript Implementation

### Function Decorator

```typescript
function withLogging<T extends (...args: any[]) => any>(fn: T): T {
  return ((...args: any[]) => {
    console.log(`Calling ${fn.name} with`, args);
    const result = fn(...args);
    console.log(`${fn.name} returned`, result);
    return result;
  }) as T;
}

function add(a: number, b: number): number {
  return a + b;
}

const loggedAdd = withLogging(add);
```

### Class Decorator

```typescript
function withRetry(maxRetries: number = 3) {
  return function <T extends new (...args: any[]) => any>(target: T) {
    return class extends target {
      async execute(...args: any[]) {
        for (let i = 0; i < maxRetries; i++) {
          try {
            return await super.execute(...args);
          } catch (error) {
            if (i === maxRetries - 1) throw error;
            console.log(`Retry ${i + 1}`);
          }
        }
      }
    };
  };
}
```

### Object Decorator

```typescript
interface Car {
  drive(): string;
}

function withGPS(car: Car): Car {
  const original = car.drive;
  car.drive = () => {
    return original.call(car) + ' with GPS';
  };
  return car;
}
```

### Middleware Pattern

```typescript
type Middleware<T> = (context: T, next: () => Promise<void>) => Promise<void>;

function createMiddleware<T>() {
  const middlewares: Middleware<T>[] = [];

  return {
    use(middleware: Middleware<T>) {
      middlewares.push(middleware);
    },
    async execute(context: T) {
      let index = 0;
      const next = async () => {
        if (index < middlewares.length) {
          const middleware = middlewares[index++];
          await middleware(context, next);
        }
      };
      await next();
    }
  };
}
```

## Best Practices

- Keep decorators focused and small
- Use composition over inheritance
- Document decorator behavior clearly
- Consider using middleware for complex scenarios
- Make decorators composable

## Interview Questions

1. How does Decorator differ from inheritance?
2. What are TypeScript decorators?
3. Can you stack multiple decorators?
4. When should you use Decorator vs Proxy?
5. How do you handle decorator ordering?

## References

- TypeScript Handbook: Decorators
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Functional Programming in TypeScript"
