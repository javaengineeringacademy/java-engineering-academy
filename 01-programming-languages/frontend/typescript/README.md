# TypeScript

TypeScript is a strongly-typed superset of JavaScript that compiles to plain JavaScript. It adds static type checking, interfaces, enums, and advanced type system features that catch errors at compile time rather than runtime.

## Table of Contents

- [Basic Types](#basic-types)
- [Union & Intersection Types](#union--intersection-types)
- [Literal Types](#literal-types)
- [Generics](#generics)
- [Interfaces vs Types](#interfaces-vs-types)
- [Classes](#classes)
- [Enums](#enums)
- [Utility Types](#utility-types)
- [Type Guards](#type-guards)
- [Discriminated Unions](#discriminated-unions)
- [Declaration Files](#declaration-files)
- [tsconfig.json](#tsconfigjson)
- [Strict Mode](#strict-mode)

---

## Basic Types

TypeScript provides several built-in types for type safety.

```typescript
// Primitive types
let name: string = "Alice";
let age: number = 30;
let isActive: boolean = true;
let nothing: null = null;
let notDefined: undefined = undefined;

// Arrays
let numbers: number[] = [1, 2, 3];
let names: Array<string> = ["Alice", "Bob"];

// Tuples
let person: [string, number] = ["Alice", 30];

// Enums
enum Direction {
  Up,
  Down,
  Left,
  Right,
}

// Unknown and any
let data: unknown = "Hello";
let anything: any = "Loosely typed";

// Void and never
function logMessage(msg: string): void {
  console.log(msg);
}

function throwError(msg: string): never {
  throw new Error(msg);
}

// Object types
let user: object = { name: "Alice", age: 30 };
```

### Type Inference

TypeScript can automatically infer types from assignments:

```typescript
let x = 10; // inferred as number
let arr = [1, 2, 3]; // inferred as number[]
let obj = { name: "Alice" }; // inferred as { name: string }
```

---

## Union & Intersection Types

### Union Types

Union types allow a variable to hold values of multiple types:

```typescript
type StringOrNumber = string | number;

let value: StringOrNumber = "Hello";
value = 42; // Also valid

// Use type narrowing for safe access
function processValue(val: StringOrNumber): string {
  if (typeof val === "string") {
    return val.toUpperCase();
  }
  return val.toFixed(2);
}
```

### Intersection Types

Intersection types combine multiple types into one:

```typescript
type HasName = { name: string };
type HasAge = { age: number };
type Person = HasName & HasAge;

const person: Person = {
  name: "Alice",
  age: 30,
};
```

---

## Literal Types

Literal types restrict values to specific constants:

```typescript
type Direction = "up" | "down" | "left" | "right";
type HttpStatus = 200 | 201 | 400 | 404 | 500;
type Boolean = true | false;

function move(direction: Direction): void {
  console.log(`Moving ${direction}`);
}

move("up"); // Valid
// move("forward"); // Error

// Template literal types
type EventName = `on${Capitalize<"click" | "focus" | "blur">}`;
// Results in: "onClick" | "onFocus" | "onBlur"
```

---

## Generics

Generics allow you to write reusable, type-safe code:

```typescript
// Basic generic function
function identity<T>(arg: T): T {
  return arg;
}

const num = identity<number>(42);
const str = identity<string>("hello");

// Generic interfaces
interface ApiResponse<T> {
  data: T;
  status: number;
  message: string;
}

// Generic classes
class Stack<T> {
  private items: T[] = [];

  push(item: T): void {
    this.items.push(item);
  }

  pop(): T | undefined {
    return this.items.pop();
  }

  peek(): T | undefined {
    return this.items[this.items.length - 1];
  }
}

const numberStack = new Stack<number>();
numberStack.push(1);
numberStack.push(2);

// Generic constraints
interface HasLength {
  length: number;
}

function logLength<T extends HasLength>(arg: T): T {
  console.log(arg.length);
  return arg;
}

logLength("hello"); // Valid
logLength([1, 2, 3]); // Valid
// logLength(42); // Error - number has no length

// Default generic parameters
interface ApiResponse<T = unknown> {
  data: T;
  status: number;
}
```

---

## Interfaces vs Types

Both `interface` and `type` can define object shapes, but have key differences:

```typescript
// Interfaces - extendable, mergeable
interface User {
  name: string;
  email: string;
}

interface User {
  age: number; // Declaration merging
}

// Extends
interface Admin extends User {
  role: "admin";
}

// Type aliases - more flexible
type Point = {
  x: number;
  y: number;
};

// Union/intersection
type StringOrNumber = string | number;
type Named = { name: string } & { age: number };

// Mapped types
type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};

// Conditional types
type IsString<T> = T extends string ? true : false;
```

### Key Differences

| Feature | Interface | Type |
|---------|-----------|------|
| Declaration merging | Yes | No |
| Extends | Yes (`extends`) | Yes (`&`) |
| Unions | No | Yes |
| Tuples | No | Yes |
| Computed properties | Yes | Yes |

---

## Classes

TypeScript extends JavaScript classes with type annotations:

```typescript
abstract class Animal {
  constructor(public name: string, protected age: number) {}

  abstract makeSound(): string;

  getInfo(): string {
    return `${this.name} is ${this.age} years old`;
  }
}

class Dog extends Animal {
  constructor(name: string, age: number, private breed: string) {
    super(name, age);
  }

  makeSound(): string {
    return "Woof!";
  }

  getBreed(): string {
    return this.breed;
  }
}

// Access modifiers: public, private, protected
class Person {
  public name: string;
  private ssn: string;
  protected age: number;

  constructor(name: string, ssn: string, age: number) {
    this.name = name;
    this.ssn = ssn;
    this.age = age;
  }
}

// Parameter properties
class User {
  constructor(
    public readonly id: number,
    public name: string,
    private email: string
  ) {}
}

// Static members
class MathUtils {
  static PI = 3.14159;

  static circleArea(radius: number): number {
    return MathUtils.PI * radius * radius;
  }
}

// Getters and setters
class Temperature {
  private _celsius: number;

  constructor(celsius: number) {
    this._celsius = celsius;
  }

  get fahrenheit(): number {
    return this._celsius * 1.8 + 32;
  }

  set fahrenheit(value: number) {
    this._celsius = (value - 32) / 1.8;
  }
}
```

---

## Enums

Enums provide a way to define named constants:

```typescript
// Numeric enums
enum Status {
  Active, // 0
  Inactive, // 1
  Pending, // 2
}

// String enums
enum Color {
  Red = "RED",
  Green = "GREEN",
  Blue = "BLUE",
}

// Const enums (inlined at compile time)
const enum Direction {
  Up = "UP",
  Down = "DOWN",
  Left = "LEFT",
  Right = "RIGHT",
}

// Usage
let status: Status = Status.Active;
let color: Color = Color.Red;

// Enum as a type
function getStatusName(status: Status): string {
  return Status[status];
}

// Enums with computed members
enum FileAccess {
  None = 0,
  Read = 1 << 0,
  Write = 1 << 1,
  ReadWrite = Read | Write,
}
```

---

## Utility Types

TypeScript provides built-in utility types for common type transformations:

```typescript
interface User {
  id: number;
  name: string;
  email: string;
  age: number;
  role: "admin" | "user";
}

// Partial - all properties optional
type PartialUser = Partial<User>;
// { id?: number; name?: string; ... }

// Required - all properties required
type RequiredUser = Required<PartialUser>;

// Pick - select specific properties
type UserBasic = Pick<User, "id" | "name">;
// { id: number; name: string }

// Omit - exclude specific properties
type UserWithoutEmail = Omit<User, "email">;
// { id: number; name: string; age: number; role: "admin" | "user" }

// Record - construct object type
type UserMap = Record<string, User>;
// { [key: string]: User }

// Readonly
type ReadonlyUser = Readonly<User>;

// Exclude - exclude union members
type StringOrNumber = string | number | boolean;
type JustStringOrNumber = Exclude<StringOrNumber, boolean>;
// string | number

// Extract - extract union members
type OnlyString = Extract<StringOrNumber, string>;
// string

// NonNullable
type MaybeString = string | null | undefined;
type DefinitelyString = NonNullable<MaybeString>;
// string

// ReturnType
function getUser() {
  return { id: 1, name: "Alice" };
}
type GetUserReturn = ReturnType<typeof getUser>;
// { id: number; name: string }

// Parameters
function createUser(name: string, age: number) {}
type CreateUserParams = Parameters<typeof createUser>;
// [string, number]

// InstanceType
class MyClass {
  value = 42;
}
type MyInstance = InstanceType<typeof MyClass>;
// MyClass

// Awaited (for Promises)
type Result = Awaited<Promise<string>>; // string
```

---

## Type Guards

Type guards narrow types at runtime:

```typescript
// typeof guards
function process(value: string | number) {
  if (typeof value === "string") {
    return value.toUpperCase();
  }
  return value.toFixed(2);
}

// instanceof guards
class Cat {
  meow() { return "Meow!"; }
}

class Dog {
  bark() { return "Woof!"; }
}

function makeSound(animal: Cat | Dog) {
  if (animal instanceof Cat) {
    return animal.meow();
  }
  return animal.bark();
}

// in operator
interface Bird {
  fly(): void;
  layEggs(): void;
}

interface Fish {
  swim(): void;
  layEggs(): void;
}

function move(animal: Bird | Fish) {
  if ("fly" in animal) {
    animal.fly();
  } else {
    animal.swim();
  }
}

// Custom type guards (type predicates)
function isString(value: unknown): value is string {
  return typeof value === "string";
}

function processValue(value: string | number) {
  if (isString(value)) {
    return value.toUpperCase();
  }
  return value.toFixed(2);
}

// Assertion functions
function assertIsString(value: unknown): asserts value is string {
  if (typeof value !== "string") {
    throw new Error("Expected string");
  }
}
```

---

## Discriminated Unions

Discriminated unions use a common property to distinguish between types:

```typescript
type Shape =
  | { kind: "circle"; radius: number }
  | { kind: "rectangle"; width: number; height: number }
  | { kind: "triangle"; base: number; height: number };

function area(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2;
    case "rectangle":
      return shape.width * shape.height;
    case "triangle":
      return (shape.base * shape.height) / 2;
  }
}

// Exhaustive checking
function areaExhaustive(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2;
    case "rectangle":
      return shape.width * shape.height;
    case "triangle":
      return (shape.base * shape.height) / 2;
    default:
      const _exhaustive: never = shape;
      return _exhaustive;
  }
}
```

---

## Declaration Files

Declaration files (`.d.ts`) provide type definitions for JavaScript libraries:

```typescript
// types/custom-module.d.ts
declare module "my-library" {
  export interface Config {
    debug: boolean;
    apiUrl: string;
  }

  export function init(config: Config): void;
  export default function createApp(): App;
}

// Ambient declarations
declare global {
  interface Window {
    __APP_CONFIG__: {
      apiUrl: string;
      environment: string;
    };
  }
}

// Module augmentation
declare module "express" {
  interface Request {
    user?: {
      id: number;
      name: string;
    };
  }
}
```

---

## tsconfig.json

The TypeScript configuration file controls compilation:

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "module": "ESNext",
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "resolveJsonModule": true,
    "declaration": true,
    "declarationMap": true,
    "sourceMap": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noImplicitReturns": true,
    "noFallthroughCasesInSwitch": true,
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "noEmit": true,
    "jsx": "react-jsx",
    "baseUrl": ".",
    "paths": {
      "@/*": ["./src/*"]
    }
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "dist"]
}
```

---

## Strict Mode

Strict mode enables additional type-checking options:

```json
{
  "compilerOptions": {
    "strict": true
  }
}
```

### Strict Mode Flags

| Flag | Description |
|------|-------------|
| `strictNullChecks` | `null` and `undefined` are not assignable to other types |
| `strictFunctionTypes` | Function parameter types are checked strictly |
| `strictBindCallApply` | `bind`, `call`, and `apply` are type-checked |
| `noImplicitAny` | Error on implicit `any` types |
| `noImplicitThis` | Error on `this` with implicit `any` type |
| `alwaysStrict` | Emit `"use strict"` in output files |
| `useUnknownInCatchVariables` | Catch clause variable typed as `unknown` |
| `exactOptionalPropertyTypes` | Distinguishes between `undefined` and missing properties |

### StrictNullChecks Example

```typescript
// Without strictNullChecks (loose)
let name: string = null; // Allowed
let length = name.length; // Runtime error!

// With strictNullChecks (strict)
let name: string = null; // Error
let name: string | null = null; // Explicit nullable
let length = name.length; // Error - must narrow first

if (name !== null) {
  console.log(name.length); // OK
}
```
