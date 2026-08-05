# Proxy Pattern (JavaScript)

## Overview

The Proxy pattern provides a surrogate or placeholder for another object to control
access. JavaScript's ES6 Proxy provides built-in support for creating proxies.

## When to Use

- Lazy initialization
- Access control
- Logging and monitoring
- Caching
- Validation

## JavaScript Implementation

### ES6 Proxy

```javascript
const handler = {
  get(target, prop) {
    console.log(`Accessing ${prop}`);
    return Reflect.get(target, prop);
  },
  set(target, prop, value) {
    console.log(`Setting ${prop} to ${value}`);
    return Reflect.set(target, prop, value);
  }
};

const proxy = new Proxy({ name: 'John', age: 30 }, handler);
```

### Validation Proxy

```javascript
function createValidatedProxy(target, schema) {
  return new Proxy(target, {
    set(obj, prop, value) {
      if (schema[prop] && !schema[prop](value)) {
        throw new Error(`Invalid value for ${prop}`);
      }
      return Reflect.set(obj, prop, value);
    }
  });
}

const user = createValidatedProxy({}, {
  age: (v) => typeof v === 'number' && v >= 0,
  email: (v) => typeof v === 'string' && v.includes('@')
});
```

### Caching Proxy

```javascript
function createCachingProxy(target) {
  const cache = new Map();

  return new Proxy(target, {
    apply(fn, thisArg, args) {
      const key = JSON.stringify(args);
      if (cache.has(key)) {
        console.log('Cache hit');
        return cache.get(key);
      }
      const result = fn.apply(thisArg, args);
      cache.set(key, result);
      return result;
    }
  });
}
```

### Lazy Proxy

```javascript
function createLazyProxy(factory) {
  let instance = null;

  return new Proxy({}, {
    get(target, prop) {
      if (!instance) {
        instance = factory();
      }
      return instance[prop];
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
- "Learning JavaScript Design Patterns" by Addy Osmani
- "You Don't Know JS" by Kyle Simpson
