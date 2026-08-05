# TypeScript Common Pitfalls

## The `any` Trap

```typescript
// Dangerous - bypasses all type checking
let data: any = fetchData();
data.nonExistentMethod();  // No error at compile time

// Safe alternatives
let data: unknown = fetchData();
if (typeof data === 'object' && data !== null) {
  // narrow type before using
}
```

## Type Assertions Over Type Guards

```typescript
// Bad - can be wrong at runtime
const user = response as User;

// Good - validates at runtime
function isUser(obj: unknown): obj is User {
  return typeof obj === 'object' && obj !== null && 'id' in obj;
}

if (isUser(response)) {
  // safe to use as User
}
```

## Null and Undefined

```typescript
// Without strictNullChecks
const arr: string[] = [];
const item = arr[0];        // string (lie!)
item.toUpperCase();          // Runtime error!

// With strictNullChecks
const item = arr[0];         // string | undefined
if (item) item.toUpperCase(); // Safe
```

## Enum Surprises

```typescript
// Numeric enum has reverse mapping
enum Direction { Up, Down, Left, Right }
Direction[0];  // "Up" (unexpected!)

// String enums are safer
enum Direction { Up = "UP", Down = "DOWN" }
Direction.Up;  // "UP"
```

## Closure in Loops

```typescript
// Problem - all callbacks see final value
for (var i = 0; i < 5; i++) {
  setTimeout(() => console.log(i), 100);  // 5, 5, 5, 5, 5
}

// Fix - use let for block scope
for (let i = 0; i < 5; i++) {
  setTimeout(() => console.log(i), 100);  // 0, 1, 2, 3, 4
}
```

## Object.freeze is Shallow

```typescript
const obj = { nested: { value: 42 } };
Object.freeze(obj);
obj.nested.value = 100;  // Works! Not frozen deeply

// Deep freeze
function deepFreeze(obj: any) {
  Object.freeze(obj);
  Object.values(obj).forEach(deepFreeze);
}
```

## Array Mutation

```typescript
const arr = [1, 2, 3];
arr.push(4);  // Mutates original

// Immutable approaches
const newArr = [...arr, 4];
const filtered = arr.filter(x => x !== 2);
```

## Async/Await Errors

```typescript
// Missing await
async function getData() {
  fetch('/api/data');  // Fire and forget!
}

// Unhandled rejection
async function risky() {
  const result = await mightFail();  // No try/catch
}
```

## Type Widening

```typescript
// const - literal type
const x = 'hello';  // type: 'hello'

// let - widened type
let y = 'hello';    // type: string

// Explicit literal type
const x: 'hello' = 'hello';
```

## Template Literal Types

```typescript
//意外的类型
type Event = 'click' | 'scroll';
type Handler = `on${Capitalize<Event>}`;
// Handler = 'onClick' | 'onScroll'
```

## Structural Typing Surprises

```typescript
interface Point { x: number; y: number; }
interface LabeledPoint { x: number; y: number; label: string; }

const p: Point = { x: 1, y: 2 };
const lp: LabeledPoint = p;  // Error - missing label

// But this works
const lp2: LabeledPoint = { x: 1, y: 2, label: 'A' };
const p2: Point = lp2;  // OK - extra properties ignored
```

## keyof and Index Access

```typescript
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

const user = { name: 'Alice', age: 30 };
getProperty(user, 'name');  // string
getProperty(user, 'age');   // number
```
