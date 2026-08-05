# Flyweight Pattern (TypeScript)

## Overview

The Flyweight pattern minimizes memory usage by sharing as much data as possible with
similar objects. TypeScript's generics enable type-safe flyweight implementations.

## When to Use

- Application uses large number of objects
- Object state can be made extrinsic
- Memory costs are high
- Many objects can be replaced with fewer shared ones

## TypeScript Implementation

### Generic Flyweight

```typescript
class Flyweight<T> {
  constructor(private sharedState: T) {}

  operation(extrinsicState: any): void {
    console.log(`Shared: ${JSON.stringify(this.sharedState)}, Extrinsic: ${extrinsicState}`);
  }
}

class FlyweightFactory<T> {
  private flyweights: Map<string, Flyweight<T>> = new Map();

  getFlyweight(key: string): Flyweight<T> {
    if (!this.flyweights.has(key)) {
      this.flyweights.set(key, new Flyweight(key as any));
      console.log(`Creating flyweight for ${key}`);
    }
    return this.flyweights.get(key)!;
  }

  getCount(): number {
    return this.flyweights.size;
  }
}
```

### Functional Flyweight

```typescript
function createFlyweightFactory<T>() {
  const flyweights = new Map<string, T>();

  return {
    get(key: string): T {
      if (!flyweights.has(key)) {
        flyweights.set(key, {} as T);
      }
      return flyweights.get(key)!;
    },
    getCount(): number {
      return flyweights.size;
    }
  };
}
```

### Object Pool

```typescript
class ObjectPool<T> {
  private pool: T[] = [];

  constructor(
    private factory: () => T,
    private maxSize: number = 100
  ) {}

  acquire(): T {
    if (this.pool.length > 0) {
      return this.pool.pop()!;
    }
    return this.factory();
  }

  release(obj: T): void {
    if (this.pool.length < this.maxSize) {
      this.pool.push(obj);
    }
  }
}
```

## Best Practices

- Separate intrinsic from extrinsic state
- Use generics for type safety
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

- TypeScript Handbook: Generics
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Object-Oriented Software Construction" by Meyer
