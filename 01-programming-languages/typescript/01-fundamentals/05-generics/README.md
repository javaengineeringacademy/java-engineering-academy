# TypeScript Generics

## Overview
Generics allow you to write reusable, type-safe code.

## Generic Function
```typescript
function identity<T>(arg: T): T {
  return arg;
}

let num = identity<number>(42);
let str = identity<string>("hello");
```

## Generic Constraints
```typescript
interface HasLength {
  length: number;
}

function logLength<T extends HasLength>(arg: T): void {
  console.log(`Length: ${arg.length}`);
}
```

## Generic Interface
```typescript
interface Repository<T> {
  getById(id: number): T;
  getAll(): T[];
  save(item: T): void;
}
```

## Generic Class
```typescript
class InMemoryRepository<T> implements Repository<T> {
  private items: T[] = [];
  
  save(item: T): void {
    this.items.push(item);
  }
}
```

## keyof Constraint
```typescript
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}
```

## Default Type Parameters
```typescript
interface ApiResponse<T = unknown> {
  data: T;
  status: number;
}
```

## Key Takeaways
1. Use generics for type-safe reusable code
2. Apply constraints to limit generic types
3. Use keyof for property access safety
4. Provide defaults for flexible generic types