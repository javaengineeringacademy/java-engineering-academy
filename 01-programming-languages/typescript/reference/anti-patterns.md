# TypeScript Anti-Patterns

## Overview
Common mistakes and anti-patterns to avoid in TypeScript.

## 1. Using `any` Type
```typescript
// Bad
let data: any = fetchData();
data.foo.bar.baz; // No error, but runtime crash

// Good
let data: unknown = fetchData();
if (typeof data === "object" && data !== null) {
  // Type-safe access
}
```

## 2. Ignoring Null Checks
```typescript
// Bad
let name = user.name; // Potential null reference

// Good
let name = user?.name ?? "Unknown";
```

## 3. Overusing Type Assertions
```typescript
// Bad
let value = someValue as string;

// Good
function isString(value: unknown): value is string {
  return typeof value === "string";
}
if (isString(someValue)) {
  // Type-safe usage
}
```

## 4. Mutating Function Parameters
```typescript
// Bad
function processUser(user: User) {
  user.name = "Modified"; // Side effect
}

// Good
function processUser(user: Readonly<User>): User {
  return { ...user, name: "Modified" };
}
```

## 5. Creating Unnecessary Interfaces
```typescript
// Bad
interface UserInterface {
  name: string;
}
type UserType = UserInterface;

// Good - pick one
type User = { name: string };
```

## 6. Not Using Type Guards
```typescript
// Bad
function process(value: string | number) {
  return value.toUpperCase(); // Error
}

// Good
function process(value: string | number) {
  if (typeof value === "string") {
    return value.toUpperCase();
  }
  return value.toFixed(2);
}
```

## 7. Excessive Nesting
```typescript
// Bad
interface A {
  b: {
    c: {
      d: string;
    };
  };
}

// Good - flatten structure
interface A {
  bC?: string;
}
```

## Best Practices
1. Prefer `unknown` over `any`
2. Use strict null checks
3. Leverage type inference
4. Use discriminated unions
5. Keep types simple and composable