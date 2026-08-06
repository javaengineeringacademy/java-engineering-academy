# TypeScript Design Patterns

## Overview
Common design patterns implemented in TypeScript.

## 1. Singleton Pattern
```typescript
class Database {
  private static instance: Database;
  
  private constructor() {}
  
  static getInstance(): Database {
    if (!Database.instance) {
      Database.instance = new Database();
    }
    return Database.instance;
  }
  
  query(sql: string): any {
    console.log(`Executing: ${sql}`);
  }
}
```

## 2. Factory Pattern
```typescript
interface Shape {
  area(): number;
  perimeter(): number;
}

class Circle implements Shape {
  constructor(private radius: number) {}
  area() { return Math.PI * this.radius ** 2; }
  perimeter() { return 2 * Math.PI * this.radius; }
}

class Rectangle implements Shape {
  constructor(private width: number, private height: number) {}
  area() { return this.width * this.height; }
  perimeter() { return 2 * (this.width + this.height); }
}

class ShapeFactory {
  static create(type: string, ...args: number[]): Shape {
    switch (type) {
      case "circle": return new Circle(args[0]);
      case "rectangle": return new Rectangle(args[0], args[1]);
      default: throw new Error("Unknown shape");
    }
  }
}
```

## 3. Observer Pattern
```typescript
interface Observer<T> {
  update(data: T): void;
}

class Subject<T> {
  private observers: Observer<T>[] = [];
  
  subscribe(observer: Observer<T>): void {
    this.observers.push(observer);
  }
  
  unsubscribe(observer: Observer<T>): void {
    this.observers = this.observers.filter(o => o !== observer);
  }
  
  notify(data: T): void {
    this.observers.forEach(observer => observer.update(data));
  }
}
```

## 4. Strategy Pattern
```typescript
interface SortStrategy<T> {
  sort(data: T[]): T[];
}

class BubbleSort<T> implements SortStrategy<T> {
  sort(data: T[]): T[] {
    // Bubble sort implementation
    return [...data];
  }
}

class QuickSort<T> implements SortStrategy<T> {
  sort(data: T[]): T[] {
    // Quick sort implementation
    return [...data];
  }
}

class Sorter<T> {
  constructor(private strategy: SortStrategy<T>) {}
  
  sort(data: T[]): T[] {
    return this.strategy.sort(data);
  }
}
```

## 5. Decorator Pattern
```typescript
function Log(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
  const originalMethod = descriptor.value;
  
  descriptor.value = function (...args: any[]) {
    console.log(`Calling ${propertyKey} with args: ${args}`);
    const result = originalMethod.apply(this, args);
    console.log(`${propertyKey} returned: ${result}`);
    return result;
  };
}

class Calculator {
  @Log
  add(a: number, b: number): number {
    return a + b;
  }
}
```

## 6. Repository Pattern
```typescript
interface Repository<T> {
  findById(id: number): Promise<T | null>;
  findAll(): Promise<T[]>;
  save(entity: T): Promise<T>;
  delete(id: number): Promise<boolean>;
}

class UserRepository implements Repository<User> {
  async findById(id: number): Promise<User | null> {
    // Database query
    return null;
  }
  
  async findAll(): Promise<User[]> {
    return [];
  }
  
  async save(user: User): Promise<User> {
    return user;
  }
  
  async delete(id: number): Promise<boolean> {
    return true;
  }
}
```

## Key Takeaways
1. Use Singleton for global state
2. Use Factory for object creation
3. Use Observer for event handling
4. Use Strategy for algorithm selection
5. Use Decorator for cross-cutting concerns