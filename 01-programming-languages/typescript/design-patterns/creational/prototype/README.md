# Prototype Pattern (TypeScript)

## Overview

The Prototype pattern creates new objects by cloning existing instances. TypeScript's
interface and class system enables type-safe prototype implementations.

## When to Use

- Creating objects expensive to construct
- When object creation is complex
- Need many similar objects
- Avoiding subclassing for object creation

## TypeScript Implementation

### Cloneable Interface

```typescript
interface Cloneable<T> {
  clone(): T;
}

class Employee implements Cloneable<Employee> {
  constructor(
    public name: string,
    public salary: number
  ) {}

  clone(): Employee {
    return new Employee(this.name, this.salary);
  }
}
```

### Deep Clone

```typescript
class DeepCloneable<T> {
  static deepClone<T>(obj: T): T {
    return JSON.parse(JSON.stringify(obj));
  }
}

interface Address {
  city: string;
  country: string;
}

class Person implements Cloneable<Person> {
  constructor(
    public name: string,
    public address: Address
  ) {}

  clone(): Person {
    return new Person(
      this.name,
      JSON.parse(JSON.stringify(this.address))
    );
  }
}
```

### Prototype Registry

```typescript
class PrototypeRegistry<T extends Cloneable<T>> {
  private prototypes: Map<string, T> = new Map();

  add(key: string, prototype: T): void {
    this.prototypes.set(key, prototype);
  }

  get(key: string): T | undefined {
    const prototype = this.prototypes.get(key);
    return prototype?.clone();
  }
}
```

### Generic Prototype

```typescript
abstract class TypedPrototype<T> {
  abstract clone(): T;
}

class ConcretePrototype extends TypedPrototype<ConcretePrototype> {
  constructor(public value: number) {
    super();
  }

  clone(): ConcretePrototype {
    return new ConcretePrototype(this.value);
  }
}
```

## Best Practices

- Implement Cloneable interface for type safety
- Use structuredClone for modern browsers
- Document clone semantics (shallow vs deep)
- Consider using records for immutable prototypes
- Validate cloned objects

## Interview Questions

1. What is the difference between shallow and deep clone?
2. How does TypeScript enforce clone method?
3. Can you implement prototype without Cloneable?
4. When should you use Prototype vs Factory?
5. How do you handle circular references in cloning?

## References

- TypeScript Handbook: Classes
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "You Don't Know JS" by Kyle Simpson
