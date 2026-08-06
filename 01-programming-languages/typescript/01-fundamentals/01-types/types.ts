// TypeScript Types - Primitive, Array, Tuple, Enum, Any, Unknown

// Primitive types
let isDone: boolean = false;
let decimal: number = 6;
let color: string = "blue";
let notMuch: null = null;
let undefined2: undefined = undefined;

// Symbol and BigInt
let sym: symbol = Symbol("key");
let big: bigint = 100n;

// Array types
let list: number[] = [1, 2, 3];
let genericList: Array<number> = [1, 2, 3];
let strings: string[] = ["a", "b", "c"];

// Tuple types
let x: [string, number] = ["hello", 10];
let y: [number, string, boolean] = [42, "world", true];

// Enum types
enum Direction {
  Up,
  Down,
  Left,
  Right
}
let dir: Direction = Direction.Up;

enum Color {
  Red = "RED",
  Green = "GREEN",
  Blue = "BLUE"
}
let c: Color = Color.Red;

// Any type - avoid when possible
let notSure: any = 4;
notSure = "maybe a string";
notSure = false;

// Unknown type - type-safe alternative to any
let inputValue: unknown = 4;
// Must check type before using
if (typeof inputValue === "string") {
  console.log(inputValue.toUpperCase());
}

// Union types
let unionType: string | number = "hello";
unionType = 42;

// Literal types
let literal: "hello" = "hello";
let numericLiteral: 42 = 42;

// Type assertions
let someValue: unknown = "this is a string";
let strLength: number = (someValue as string).length;

// Void type
function logMessage(message: string): void {
  console.log(message);
}

// Never type
function throwError(message: string): never {
  throw new Error(message);
}

// Type inference
let inferredString = "hello"; // TypeScript infers string
let inferredNumber = 42; // TypeScript infers number

console.log("Types example running");
