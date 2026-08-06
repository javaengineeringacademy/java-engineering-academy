# TypeScript Best Practices

## Overview
Guidelines for writing clean, maintainable TypeScript code.

## 1. Enable Strict Mode
```json
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true
  }
}
```

## 2. Use Type Inference
```typescript
// Prefer
let name = "Alice";
let count = 42;

// Over
let name: string = "Alice";
let count: number = 42;
```

## 3. Prefer Interfaces for Object Shapes
```typescript
// Good
interface User {
  name: string;
  age: number;
}

// Use type for unions and intersections
type ID = string | number;
```

## 4. Use Discriminated Unions
```typescript
type Result =
  | { success: true; data: any }
  | { success: false; error: string };

function handleResult(result: Result) {
  if (result.success) {
    console.log(result.data);
  } else {
    console.error(result.error);
  }
}
```

## 5. Use Utility Types
```typescript
// Partial for updates
function updateUser(id: number, updates: Partial<User>): User {
  return { ...existingUser, ...updates };
}

// Pick for data shaping
type UserBasic = Pick<User, "id" | "name">;
```

## 6. Avoid Type Assertions
```typescript
// Bad
let value = someValue as string;

// Good
function isString(value: unknown): value is string {
  return typeof value === "string";
}
```

## 7. Use Const Assertions
```typescript
const ROUTES = {
  home: "/",
  about: "/about",
  contact: "/contact"
} as const;

// Type: { readonly home: "/"; readonly about: "/about"; ... }
```

## 8. Export Types Explicitly
```typescript
// Good - explicit type export
export interface User {
  name: string;
  age: number;
}

export type UserRole = "admin" | "user" | "guest";
```

## 9. Use Read-Only Data
```typescript
function processData(data: readonly number[]): number[] {
  // Can't modify input
  return data.map(x => x * 2);
}
```

## 10. Document Complex Types
```typescript
/**
 * Represents a user in the system
 * @property id - Unique identifier
 * @property name - Display name
 * @property email - Contact email
 */
interface User {
  id: number;
  name: string;
  email: string;
}
```

## Key Takeaways
1. Always enable strict mode
2. Leverage type inference
3. Use utility types for common patterns
4. Prefer type guards over assertions
5. Document complex types