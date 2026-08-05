# Chain of Responsibility Pattern (JavaScript)

## Overview

The Chain of Responsibility pattern avoids coupling the sender of a request to its
receiver by giving more than one object a chance to handle the request. JavaScript's
middleware pipeline is a prime example.

## When to Use

- Multiple objects may handle a request
- Handler should be determined at runtime
- Request should be handled by one of multiple handlers
- Set of handlers should be specified dynamically

## JavaScript Implementation

### Basic Chain

```javascript
class Handler {
  constructor() {
    this.next = null;
  }

  setNext(handler) {
    this.next = handler;
    return handler;
  }

  handle(request) {
    if (this.next) {
      return this.next.handle(request);
    }
    return null;
  }
}
```

### Middleware Pattern

```javascript
function createMiddleware() {
  const middlewares = [];

  return {
    use(fn) {
      middlewares.push(fn);
    },
    async execute(context) {
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

### Express-Style Middleware

```javascript
class App {
  constructor() {
    this.middlewares = [];
  }

  use(middleware) {
    this.middlewares.push(middleware);
    return this;
  }

  async handle(req, res) {
    let index = 0;

    const next = async () => {
      if (index < this.middlewares.length) {
        const middleware = this.middlewares[index++];
        await middleware(req, res, next);
      }
    };

    await next();
  }
}
```

### Functional Chain

```javascript
function createHandler(canHandle, handle) {
  let nextHandler = null;

  const handler = {
    handle(request) {
      if (canHandle(request)) {
        return handle(request);
      } else if (nextHandler) {
        return nextHandler.handle(request);
      }
      return null;
    },
    setNext(next) {
      nextHandler = next;
      return next;
    }
  };

  return handler;
}
```

## Best Practices

- Keep handlers focused and small
- Define default behavior for unhandled requests
- Handle circular chains
- Document handler ordering
- Consider using async/await for async chains

## Interview Questions

1. What is the difference between Chain of Responsibility and Middleware?
2. Can multiple handlers process same request?
3. How do you handle unhandled requests?
4. When should you use Chain vs Decorator?
5. How do you implement async chain of responsibility?

## References

- MDN: Chain of Responsibility
- Express.js middleware documentation
- "Learning JavaScript Design Patterns" by Addy Osmani
