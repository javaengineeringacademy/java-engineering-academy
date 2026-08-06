# TypeScript Functions

## Overview
TypeScript provides type safety for function parameters and return values.

## Typed Parameters
```typescript
function add(a: number, b: number): number {
  return a + b;
}
```

## Optional Parameters
```typescript
function greet(name: string, greeting?: string): string {
  return `${greeting || "Hello"}, ${name}!`;
}
```

## Default Parameters
```typescript
function createUser(name: string, age: number = 25) {
  return { name, age };
}
```

## Rest Parameters
```typescript
function sum(...numbers: number[]): number {
  return numbers.reduce((total, n) => total + n, 0);
}
```

## Function Overloads
```typescript
function format(value: string): string;
function format(value: number): string;
function format(value: string | number): string {
  // Implementation
}
```

## Function Types
```typescript
type MathOperation = (a: number, b: number) => number;
const add: MathOperation = (a, b) => a + b;
```

## Higher-Order Functions
```typescript
function createMultiplier(factor: number): (value: number) => number {
  return (value) => value * factor;
}
```

## Key Takeaways
1. Always specify return types for clarity
2. Use optional parameters for flexibility
3. Leverage function types for callbacks
4. Use overloads for multiple signatures