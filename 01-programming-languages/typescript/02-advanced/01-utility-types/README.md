# TypeScript Utility Types

## Overview
Utility types provide common type transformations.

## Partial<T>
Makes all properties optional:
```typescript
function updateUser(id: number, updates: Partial<User>): User {
  return { ...user, ...updates };
}
```

## Required<T>
Makes all properties required:
```typescript
type RequiredConfig = Required<Config>;
```

## Pick<T, K>
Selects specific properties:
```typescript
type UserBasic = Pick<User, "id" | "name">;
```

## Omit<T, K>
Removes specific properties:
```typescript
type UserWithoutEmail = Omit<User, "email">;
```

## Record<K, V>
Creates object type with specific keys:
```typescript
type Roles = "admin" | "user" | "guest";
type UserRoles = Record<Roles, string[]>;
```

## Readonly<T>
Makes all properties readonly:
```typescript
type ReadonlyUser = Readonly<User>;
```

## Other Useful Types
- `Exclude<T, U>` - Remove types from union
- `Extract<T, U>` - Extract types from union
- `NonNullable<T>` - Remove null and undefined
- `ReturnType<T>` - Get function return type
- `Parameters<T>` - Get function parameter types

## Key Takeaways
1. Use Partial for update operations
2. Use Pick/Omit for data shaping
3. Use Record for dictionary types
4. Use Readonly for immutable data