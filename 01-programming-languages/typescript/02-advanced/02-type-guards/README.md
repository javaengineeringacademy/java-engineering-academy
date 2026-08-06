# TypeScript Type Guards

## Overview
Type guards help narrow down types at runtime.

## typeof Guards
```typescript
function processValue(value: string | number): string {
  if (typeof value === "string") {
    return value.toUpperCase();
  }
  return value.toFixed(2);
}
```

## instanceof Guards
```typescript
function makeSound(animal: Dog | Cat): string {
  if (animal instanceof Dog) {
    return animal.bark();
  }
  return animal.meow();
}
```

## Custom Type Guards
```typescript
function isFish(pet: Fish | Bird): pet is Fish {
  return (pet as Fish).swim !== undefined;
}
```

## Discriminated Unions
```typescript
interface Circle {
  kind: "circle";
  radius: number;
}

interface Square {
  kind: "square";
  sideLength: number;
}

type Shape = Circle | Square;

function getArea(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2;
    case "square":
      return shape.sideLength ** 2;
  }
}
```

## Exhaustiveness Checking
```typescript
function getArea(shape: Shape): number {
  switch (shape.kind) {
    case "circle": return Math.PI * shape.radius ** 2;
    case "square": return shape.sideLength ** 2;
    default:
      const _exhaustive: never = shape;
      return _exhaustive;
  }
}
```

## Key Takeaways
1. Use typeof for primitive types
2. Use instanceof for class instances
3. Use custom guards for complex checks
4. Use discriminated unions for union types