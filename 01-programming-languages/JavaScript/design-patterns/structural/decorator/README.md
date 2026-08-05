# Decorator Pattern (JavaScript)

## Overview

The Decorator pattern attaches additional responsibilities to objects dynamically.
JavaScript's higher-order functions and closures make decorators particularly elegant.

## When to Use

- Adding responsibilities dynamically
- Extending functionality without subclassing
- Creating layered behaviors
- Implementing cross-cutting concerns

## JavaScript Implementation

### Higher-Order Function Decorator

```javascript
function withLogging(fn) {
  return function(...args) {
    console.log(`Calling ${fn.name} with`, args);
    const result = fn.apply(this, args);
    console.log(`${fn.name} returned`, result);
    return result;
  };
}

function add(a, b) {
  return a + b;
}

const loggedAdd = withLogging(add);
loggedAdd(1, 2);
```

### Class Decorator

```javascript
function withRetry(maxRetries = 3) {
  return function(target) {
    return class extends target {
      async execute(...args) {
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

```javascript
class Car {
  drive() {
    return 'Driving';
  }
}

function withGPS(car) {
  const original = car.drive;
  car.drive = function() {
    return original.call(car) + ' with GPS';
  };
  return car;
}

const myCar = new Car();
withGPS(myCar);
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

## Best Practices

- Keep decorators focused and small
- Use composition over inheritance
- Document decorator behavior clearly
- Consider using middleware for complex scenarios
- Make decorators composable

## Interview Questions

1. How does Decorator differ from inheritance?
2. What are higher-order functions?
3. Can you stack multiple decorators?
4. When should you use Decorator vs Proxy?
5. How do you handle decorator ordering?

## References

- MDN: Higher-Order Functions
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Functional Programming in JavaScript" by Luis Atencio
