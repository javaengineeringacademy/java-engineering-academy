# TypeScript Core Concepts

## Basic Types

```typescript
let name: string = "Alice";
let age: number = 30;
let active: boolean = true;
let items: Array<string> = ["a", "b"];
let tuple: [string, number] = ["hello", 42];
let anything: any = "flexible";
let unknown_val: unknown = "safer than any";
let nothing: void = undefined;
let n: null = null;
let u: undefined = undefined;
```

## Interfaces and Type Aliases

```typescript
// Interface - extendable, declaration merging
interface User {
  id: number;
  name: string;
  email?: string;
}

interface Admin extends User {
  role: "admin" | "superadmin";
}

// Type alias - flexible, no merging
type ID = string | number;
type Point = { x: number; y: number };
type Result<T> = { data: T; error: null } | { data: null; error: Error };
```

## Generics

```typescript
// Generic function
function identity<T>(arg: T): T {
  return arg;
}

// Generic interface
interface Repository<T> {
  getById(id: string): Promise<T>;
  getAll(): Promise<T[]>;
  save(entity: T): Promise<void>;
}

// Generic constraints
function merge<T extends object, U extends object>(a: T, b: U): T & U {
  return { ...a, ...b };
}
```

## Enums

```typescript
// Numeric enum
enum Direction {
  Up = 1,
  Down,
  Left,
  Right,
}

// String enum
enum Color {
  Red = "RED",
  Green = "GREEN",
  Blue = "BLUE",
}

// Const enum (inlined at compile time)
const enum Status {
  Active = "ACTIVE",
  Inactive = "INACTIVE",
}
```

## Decorators

```typescript
function sealed(constructor: Function) {
  Object.seal(constructor);
  Object.seal(constructor.prototype);
}

function Log(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
  const original = descriptor.value;
  descriptor.value = function (...args: any[]) {
    console.log(`Calling ${propertyKey} with ${args}`);
    return original.apply(this, args);
  };
}

@sealed
class Example {
  @Log
  method(arg: string) { return arg; }
}
```

## Modules

```typescript
// Named exports
export interface User { id: number; name: string; }
export function createUser(): User { return { id: 1, name: "Alice" }; }

// Default export
export default class UserService {
  getUser(): User { return { id: 1, name: "Alice" }; }
}

// Re-exports
export { User } from './models';
export type { UserDTO } from './dtos';
```

## Utility Types

```typescript
Partial<User>           // All properties optional
Required<User>          // All properties required
Pick<User, 'id'>        // Only selected properties
Omit<User, 'email'>     // All except selected
Record<string, User>    // Map of string to User
Readonly<User>          // All properties readonly
ReturnType<typeof fn>   // Return type of function
Parameters<typeof fn>   // Parameter types of function
```

## Type Assertions

```typescript
// Angle bracket (not in .tsx)
const len = (input as string).length;

// Always prefer type guards
function isString(val: unknown): val is string {
  return typeof val === "string";
}

if (isString(value)) {
  console.log(value.toUpperCase());
}
```
