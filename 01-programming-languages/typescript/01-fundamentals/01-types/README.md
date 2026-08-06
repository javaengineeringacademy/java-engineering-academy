# TypeScript Types

## Overview
TypeScript provides a rich type system that helps catch errors at compile time.

## Primitive Types
- `boolean` - true/false
- `number` - integers and floats
- `string` - text values
- `null` and `undefined`
- `symbol` and `bigint`

## Array Types
```typescript
let numbers: number[] = [1, 2, 3];
let strings: Array<string> = ["a", "b"];
```

## Tuple Types
Fixed-length arrays with specific types:
```typescript
let person: [string, number] = ["Alice", 30];
```

## Enum Types
Named constants:
```typescript
enum Status {
  Active,
  Inactive,
  Pending
}
```

## Any vs Unknown
- `any` - Disables type checking (avoid)
- `unknown` - Type-safe alternative, requires type checks

## Union Types
```typescript
let id: string | number = 123;
id = "abc";
```

## Type Assertions
```typescript
let value: unknown = "hello";
let len = (value as string).length;
```

## Key Takeaways
1. Prefer `unknown` over `any`
2. Use type inference when possible
3. Leverage union types for flexibility
4. Always check types before using `unknown`