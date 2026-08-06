# TypeScript Classes

## Overview
Classes provide a blueprint for creating objects with properties and methods.

## Basic Class
```typescript
class Animal {
  name: string;
  
  constructor(name: string) {
    this.name = name;
  }
  
  speak(): string {
    return `${this.name} makes a sound`;
  }
}
```

## Access Modifiers
- `public` - Accessible anywhere
- `private` - Accessible only within class
- `protected` - Accessible within class and subclasses

```typescript
class BankAccount {
  public owner: string;
  private balance: number;
  
  constructor(owner: string, balance: number) {
    this.owner = owner;
    this.balance = balance;
  }
}
```

## Parameter Properties
```typescript
class Person {
  constructor(
    public name: string,
    public age: number,
    private ssn: string
  ) {}
}
```

## Abstract Classes
```typescript
abstract class Shape {
  abstract area(): number;
  
  describe(): string {
    return `Area: ${this.area()}`;
  }
}
```

## Static Members
```typescript
class MathUtils {
  static PI = 3.14159;
  
  static add(a: number, b: number): number {
    return a + b;
  }
}
```

## Getters and Setters
```typescript
class Temperature {
  private _celsius: number;
  
  get fahrenheit(): number {
    return this._celsius * 9/5 + 32;
  }
}
```

## Key Takeaways
1. Use access modifiers for encapsulation
2. Leverage parameter properties for cleaner constructors
3. Use abstract classes for base class contracts
4. Use static members for class-level utilities