# TypeScript Mapped Types

## Overview
Mapped types allow you to create new types by transforming existing ones.

## Basic Mapped Type
```typescript
type Nullable<T> = {
  [P in keyof T]: T[P] | null;
};

type NullableUser = Nullable<User>;
```

## keyof Operator
```typescript
type UserKeys = keyof User; // "id" | "name" | "email"

function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}
```

## Adding/Removing Modifiers
```typescript
type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};

type Required<T> = {
  [P in keyof T]-?: T[P];
};
```

## Filtering Properties
```typescript
type StringOnly<T> = {
  [P in keyof T]: T[P] extends string ? T[P] : never;
};

type UserStrings = StringOnly<User>;
// { id: never; name: string; email: string }
```

## Remapping Keys
```typescript
type PickByType<T, U> = {
  [K in keyof T as T[K] extends U ? K : never]: T[K];
};

type UserStringsOnly = PickByType<User, string>;
// { name: string; email: string }
```

## Recursive Mapped Types
```typescript
type DeepReadonly<T> = {
  readonly [P in keyof T]: T[P] extends object ? DeepReadonly<T[P]> : T[P];
};
```

## Key Takeaways
1. Use keyof for type-safe property access
2. Use mapped types for type transformations
3. Combine with conditional types for filtering
4. Use recursive mapped types for deep transformations