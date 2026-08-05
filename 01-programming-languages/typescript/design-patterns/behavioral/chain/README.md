# Chain of Responsibility Pattern (TypeScript)

## Overview

The Chain of Responsibility pattern avoids coupling the sender of a request to its
receiver by giving more than one object a chance to handle the request. TypeScript's
generics enable type-safe middleware implementations.

## When to Use

- Multiple objects may handle a request
- Handler should be determined at runtime
- Request should be handled by one of multiple handlers
- Set of handlers should be specified dynamically

## TypeScript Implementation

### Generic Handler

```typescript
interface Handler<T, R> {
  handle(request: T): R | null;
  setNext(handler: Handler<T, R>): Handler<T, R>;
}

class AbstractHandler<T, R> implements Handler<T, R> {
  private next: Handler<T, R> | null = null;

  setNext(handler: Handler<T, R>): Handler<T, R> {
    this.next = handler;
    return handler;
  }

  handle(request: T): R | null {
    if (this.next) {
      return this.next.handle(request);
    }
    return null;
  }
}
```

### Middleware Pattern

```typescript
type Middleware<T> = (context: T, next: () => Promise<void>) => Promise<void>;

function createMiddleware<T>() {
  const middlewares: Middleware<T>[] = [];

  return {
    use(middleware: Middleware<T>): void {
      middlewares.push(middleware);
    },
    async execute(context: T): Promise<void> {
      let index = 0;

      const next = async (): Promise<void> => {
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

### Typed Chain

```typescript
class TypedHandler<T> {
  private next: TypedHandler<T> | null = null;
  private canHandle: (request: T) => boolean;
  private handleFn: (request: T) => any;

  constructor(
    canHandle: (request: T) => boolean,
    handleFn: (request: T) => any
  ) {
    this.canHandle = canHandle;
    this.handleFn = handleFn;
  }

  setNext(handler: TypedHandler<T>): TypedHandler<T> {
    this.next = handler;
    return handler;
  }

  handle(request: T): any {
    if (this.canHandle(request)) {
      return this.handleFn(request);
    } else if (this.next) {
      return this.next.handle(request);
    }
    return null;
  }
}
```

## Best Practices

- Use generics for type safety
- Keep handlers focused and small
- Define default behavior for unhandled requests
- Document handler ordering
- Consider using async/await for async chains

## Interview Questions

1. What is the difference between Chain of Responsibility and Middleware?
2. Can multiple handlers process same request?
3. How do you handle unhandled requests?
4. When should you use Chain vs Decorator?
5. How do you implement async chain of responsibility?

## References

- TypeScript Handbook: Generics
- "TypeScript Design Patterns" by Vaskaran Sarcar
- Express.js middleware documentation
