# Proxy Pattern (TypeScript)

## Overview

The Proxy pattern provides a surrogate or placeholder for another object to control
access. TypeScript's type system enables generic proxy implementations with compile-time
safety.

## When to Use

- Lazy initialization
- Access control
- Logging and monitoring
- Caching
- Validation

## TypeScript Implementation

### Generic Proxy

```typescript
interface Handler {
  get(target: any, prop: string): any;
  set(target: any, prop: string, value: any): boolean;
}

function createProxy<T extends object>(target: T, handler: Handler): T {
  return new Proxy(target, handler);
}

const user = createProxy({ name: 'John', age: 30 }, {
  get: (target, prop) => {
    console.log(`Accessing ${prop}`);
    return Reflect.get(target, prop);
  },
  set: (target, prop, value) => {
    console.log(`Setting ${prop} to ${value}`);
    return Reflect.set(target, prop, value);
  }
});
```

### Validation Proxy

```typescript
interface Schema {
  [key: string]: (value: any) => boolean;
}

function createValidatedProxy<T extends object>(target: T, schema: Schema): T {
  return new Proxy(target, {
    set: (obj, prop, value) => {
      const key = prop as string;
      if (schema[key] && !schema[key](value)) {
        throw new Error(`Invalid value for ${key}`);
      }
      return Reflect.set(obj, prop, value);
    }
  });
}
```

### Caching Proxy

```typescript
function createCachingProxy<T extends (...args: any[]) => any>(fn: T): T {
  const cache = new Map();

  return ((...args: any[]) => {
    const key = JSON.stringify(args);
    if (cache.has(key)) {
      console.log('Cache hit');
      return cache.get(key);
    }
    const result = fn(...args);
    cache.set(key, result);
    return result;
  }) as T;
}
```

### Lazy Proxy

```typescript
function createLazyProxy<T>(factory: () => T): T {
  let instance: T | null = null;

  return new Proxy({} as T, {
    get: (target, prop) => {
      if (!instance) {
        instance = factory();
      }
      return (instance as any)[prop];
    }
  });
}
```

## Best Practices

- Use Reflect methods for default behavior
- Keep proxy handlers simple
- Consider performance implications
- Document proxy behavior clearly
- Use WeakMap for private data storage

## Interview Questions

1. What is the difference between Proxy and Decorator?
2. How does ES6 Proxy differ from polyfill implementations?
3. Can you proxy functions?
4. When should you use Proxy vs getter/setter?
5. How do you handle proxy for async operations?

## References

- MDN: Proxy
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "You Don't Know JS" by Kyle Simpson
