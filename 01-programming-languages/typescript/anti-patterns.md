# TypeScript Anti-Patterns

## 1. Overusing `any` Type
**Description:** Using `any` type to bypass TypeScript's type system.

**Why it's bad:** Defeats the purpose of TypeScript, loses type safety, hides errors.

**Example (bad code):**
```typescript
function process(data: any): any {
    return data.foo.bar;
}
```

**Better approach:** Use proper types or unknown:
```typescript
interface DataType {
    foo: {
        bar: string;
    };
}

function process(data: DataType): string {
    return data.foo.bar;
}
```

**Impact:** Full type safety, better IDE support, compile-time error checking.

---

## 2. Type Assertions Over Type Guards
**Description:** Using `as` keyword instead of proper type guards.

**Why it's bad:** Can cause runtime errors if assertion is wrong, bypasses type checking.

**Example (bad code):**
```typescript
function process(input: unknown) {
    const data = input as { name: string };
    console.log(data.name); // might crash
}
```

**Better approach:** Use type guards:
```typescript
function process(input: unknown) {
    if (typeof input === 'object' && input !== null && 'name' in input) {
        const data = input as { name: string };
        console.log(data.name);
    }
}
```

**Impact:** Runtime safety, prevents type assertion errors.

---

## 3. Null and Undefined Confusion
**Description:** Not properly handling null/undefined in type system.

**Why it's bad:** Runtime errors, inconsistent null handling.

**Example (bad code):**
```typescript
function getLength(value: string | null): number {
    return value.length; // error if null
}
```

**Better approach:** Use strict null checks and proper handling:
```typescript
function getLength(value: string | null): number {
    return value?.length ?? 0;
}
```

**Impact:** Explicit null handling, fewer runtime errors.

---

## 4. Ignoring Strict Property Initialization
**Description:** Not initializing class properties or using definite assignment assertion.

**Why it's bad:** Can cause undefined property access errors.

**Example (bad code):**
```typescript
class UserService {
    name!: string; // definite assignment assertion
    
    greet() {
        console.log(`Hello, ${this.name}`); // might be undefined
    }
}
```

**Better approach:** Initialize properties properly:
```typescript
class UserService {
    name: string;
    
    constructor(name: string) {
        this.name = name;
    }
    
    greet() {
        console.log(`Hello, ${this.name}`);
    }
}
```

**Impact:** Guaranteed initialization, fewer undefined errors.

---

## 5. Using Function Types Incorrectly
**Description:** Overcomplicating function types or using wrong function signatures.

**Why it's bad:** Hard to read, incorrect type checking.

**Example (bad code):**
```typescript
const process: (x: string, y: number) => void = (x, y) => {
    // ...
};
```

**Better approach:** Use named interfaces for complex types:
```typescript
interface ProcessFunction {
    (x: string, y: number): void;
}

const process: ProcessFunction = (x, y) => {
    // ...
};
```

**Impact:** Better readability, reusable type definitions.

---

## 6. Not Using Discriminated Unions
**Description:** Using optional properties instead of discriminated unions for state.

**Why it's bad:** Hard to determine state, requires checking multiple optional properties.

**Example (bad code):**
```typescript
interface Request {
    loading?: boolean;
    data?: any;
    error?: string;
}
```

**Better approach:** Use discriminated unions:
```typescript
type Request = 
    | { status: 'loading' }
    | { status: 'success'; data: unknown }
    | { status: 'error'; error: string };
```

**Impact:** Clear state representation, exhaustive checking possible.

---

## 7. Not Using `readonly` Properly
**Description:** Not marking immutable data as readonly.

**Why it's bad:** Accidental mutation of data, unexpected side effects.

**Example (bad code):**
```typescript
interface Config {
    apiUrl: string;
    timeout: number;
}

function processConfig(config: Config) {
    config.apiUrl = 'new url'; // mutates original
}
```

**Better approach:** Use readonly:
```typescript
interface Config {
    readonly apiUrl: string;
    readonly timeout: number;
}

function processConfig(config: Readonly<Config>) {
    // config.apiUrl = 'new url'; // error
}
```

**Impact:** Prevents accidental mutation, clearer intent.

---

## 8. Barrel File Abuse
**Description:** Creating large index.ts files that export everything.

**Why it's bad:** Increases bundle size, slows compilation, makes dependencies unclear.

**Example (bad code):**
```typescript
// index.ts
export * from './module1';
export * from './module2';
export * from './module3';
// ... hundreds more
```

**Better approach:** Export only what's needed:
```typescript
export { Module1 } from './module1';
export { Module2 } from './module2';
```

**Impact:** Smaller bundles, clearer dependencies, faster compilation.

---

## 9. Not Using `satisfies` Operator
**Description:** Using type annotations where satisfies would be better.

**Why it's bad:** Can lose literal types, less precise type checking.

**Example (bad code):**
```typescript
const config: Record<string, string> = {
    apiUrl: 'https://api.example.com',
};
// config.apiUrl type is string, not the literal
```

**Better approach:** Use satisfies:
```typescript
const config = {
    apiUrl: 'https://api.example.com',
} satisfies Record<string, string>;
// config.apiUrl type is 'https://api.example.com'
```

**Impact:** Preserves literal types, more precise type information.

---

## 10. Ignoring `unknown` Over `any`
**Description:** Using `any` when `unknown` would be safer.

**Why it's bad:** `any` disables type checking entirely, `unknown` forces type narrowing.

**Example (bad code):**
```typescript
function parse(input: any) {
    return JSON.parse(input); // no type safety
}
```

**Better approach:** Use unknown:
```typescript
function parse(input: unknown): unknown {
    return JSON.parse(input as string);
}
```

**Impact:** Safer type handling, forces explicit type checking.

---

## 11. Not Using Template Literal Types
**Description:** Not leveraging template literal types for string patterns.

**Why it's bad:** Missed opportunities for compile-time string validation.

**Example (bad code):**
```typescript
type EventName = string;
function on(event: EventName, handler: () => void) {}
```

**Better approach:** Use template literal types:
```typescript
type EventName = 'click' | 'hover' | 'focus';
function on(event: EventName, handler: () => void) {}
```

**Impact:** Compile-time validation of string values.

---

## 12. Not Using Mapped Types
**Description:** Manually creating types that could be derived.

**Why it's bad:** Error-prone, hard to maintain, violates DRY principle.

**Example (bad code):**
```typescript
interface User {
    name: string;
    age: number;
}

interface ReadonlyUser {
    readonly name: string;
    readonly age: number;
}
```

**Better approach:** Use mapped types:
```typescript
interface User {
    name: string;
    age: number;
}

type ReadonlyUser = Readonly<User>;
```

**Impact:** DRY code, easier maintenance, less duplication.