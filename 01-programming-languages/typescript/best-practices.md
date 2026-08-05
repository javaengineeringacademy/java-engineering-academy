# TypeScript Best Practices

## 1. Enable Strict Mode

```json
{
  "compilerOptions": {
    "strict": true,
    "noUncheckedIndexedAccess": true
  }
}
```

## 2. Avoid `any`

```typescript
// Bad
function process(data: any) { return data.value; }

// Good
function process(data: unknown) {
  if (typeof data === 'object' && data !== null && 'value' in data) {
    return (data as { value: string }).value;
  }
}
```

## 3. Use Type Inference

```typescript
// Bad - redundant type annotation
const items: string[] = ['a', 'b', 'c'];

// Good - let TypeScript infer
const items = ['a', 'b', 'c'];  // string[]
```

## 4. Prefer Interfaces for Public APIs

```typescript
// Good - interfaces for public contracts
export interface UserService {
  getUser(id: string): Promise<User>;
  createUser(data: CreateUserDto): Promise<User>;
}

// Type aliases for unions and primitives
type ID = string | number;
type Result<T> = { success: true; data: T } | { success: false; error: string };
```

## 5. Use Discriminated Unions

```typescript
type Result<T> =
  | { status: 'success'; data: T }
  | { status: 'error'; message: string };

function handle(result: Result<User>) {
  if (result.status === 'success') {
    console.log(result.data);  // TypeScript knows data exists
  } else {
    console.error(result.message);
  }
}
```

## 6. Use `readonly` by Default

```typescript
// Immutable data
interface User {
  readonly id: string;
  readonly name: string;
}

const user: User = { id: '1', name: 'Alice' };
user.name = 'Bob';  // Error
```

## 7. Prefer `unknown` Over `any`

```typescript
// unknown is type-safe
function parse(input: unknown): string {
  if (typeof input === 'string') return input;
  throw new Error('Invalid input');
}
```

## 8. Use Non-Null Assertion谨慎

```typescript
// Avoid when possible
const user = getUser()!;  // Bad

// Prefer null checks
const user = getUser();
if (!user) throw new Error('User not found');
```

## 9. Leverage Utility Types

```typescript
// Partial for updates
async function updateUser(id: string, updates: Partial<User>) { }

// Pick for projections
type UserPreview = Pick<User, 'id' | 'name'>;

// Omit for exclusions
type CreateUserDto = Omit<User, 'id' | 'createdAt'>;
```

## 10. Use Enums Wisely

```typescript
// Prefer const enums for smaller output
const enum Direction { Up, Down, Left, Right }

// Or use string unions
type Direction = 'up' | 'down' | 'left' | 'right';
```

## 11. Type Assertions Only When Necessary

```typescript
// Prefer type guards
function isString(val: unknown): val is string {
  return typeof val === 'string';
}

if (isString(value)) {
  value.toUpperCase();  // Safe
}
```

## 12. Export Types Explicitly

```typescript
// Explicit type exports
export type { User, CreateUserDto, UpdateUserDto };
export { UserService };
```

## 13. Use Async/Await Over Promises

```typescript
// Good - readable
async function getUser(id: string): Promise<User> {
  const response = await fetch(`/api/users/${id}`);
  return response.json();
}

// Avoid
function getUser(id: string): Promise<User> {
  return fetch(`/api/users/${id}`).then(r => r.json());
}
```

## 14. Configure Path Aliases

```json
{
  "compilerOptions": {
    "baseUrl": "./src",
    "paths": {
      "@/*": ["./*"],
      "@models/*": ["models/*"]
    }
  }
}
```

## 15. Use `satisfies` for Validation

```typescript
// Validates type without widening
const config = {
  port: 3000,
  debug: true,
} satisfies Record<string, string | number>;

config.port;  // number
config.debug; // boolean
```
