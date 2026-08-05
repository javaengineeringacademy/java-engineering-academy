# Flyweight Pattern (JavaScript)

## Overview

The Flyweight pattern minimizes memory usage by sharing as much data as possible with
similar objects. JavaScript's closures and object sharing make flyweight implementations
efficient.

## When to Use

- Application uses large number of objects
- Object state can be made extrinsic
- Memory costs are high
- Many objects can be replaced with fewer shared ones

## JavaScript Implementation

### Basic Flyweight

```javascript
class Flyweight {
  constructor(sharedState) {
    this.sharedState = sharedState;
  }

  operation(extrinsicState) {
    console.log(`Shared: ${JSON.stringify(this.sharedState)}, Extrinsic: ${extrinsicState}`);
  }
}

class FlyweightFactory {
  constructor() {
    this.flyweights = {};
  }

  getFlyweight(key) {
    if (!this.flyweights[key]) {
      this.flyweights[key] = new Flyweight(key);
      console.log(`Creating flyweight for ${key}`);
    }
    return this.flyweights[key];
  }

  getCount() {
    return Object.keys(this.flyweights).length;
  }
}
```

### Functional Flyweight

```javascript
function createFlyweightFactory() {
  const flyweights = {};

  return {
    get(key) {
      if (!flyweights[key]) {
        flyweights[key] = { key, data: {} };
      }
      return flyweights[key];
    },
    getCount() {
      return Object.keys(flyweights).length;
    }
  };
}
```

### Object Pool with Flyweight

```javascript
class ObjectPool {
  constructor(factory, maxSize = 100) {
    this.factory = factory;
    this.pool = [];
    this.maxSize = maxSize;
  }

  acquire() {
    if (this.pool.length > 0) {
      return this.pool.pop();
    }
    return this.factory();
  }

  release(obj) {
    if (this.pool.length < this.maxSize) {
      this.pool.push(obj);
    }
  }
}
```

### WeakMap Flyweight

```javascript
function createWeakFlyweight() {
  const cache = new WeakMap();

  return {
    get(key) {
      if (!cache.has(key)) {
        cache.set(key, {});
      }
      return cache.get(key);
    }
  };
}
```

## Best Practices

- Separate intrinsic from extrinsic state
- Use WeakMap for automatic cleanup
- Consider object pooling for reuse
- Document flyweight lifecycle
- Use flyweight for memory optimization

## Interview Questions

1. What is the difference between Flyweight and Singleton?
2. How do you handle thread safety in Flyweight?
3. What is intrinsic vs extrinsic state?
4. When should you use Flyweight over caching?
5. How do you manage flyweight lifecycle?

## References

- MDN: Flyweight Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Object-Oriented Software Construction" by Meyer
