// TypeScript Functions - Typed params, return types, optional params

// Function with typed parameters and return type
function add(a: number, b: number): number {
  return a + b;
}

// Arrow function with types
const multiply = (a: number, b: number): number => a * b;

// Optional parameters
function greet(name: string, greeting?: string): string {
  return `${greeting || "Hello"}, ${name}!`;
}

// Default parameters
function createUser(name: string, age: number = 25, active: boolean = true) {
  return { name, age, active };
}

// Rest parameters
function sum(...numbers: number[]): number {
  return numbers.reduce((total, n) => total + n, 0);
}

// Function overload declarations
function format(value: string): string;
function format(value: number): string;
function format(value: Date): string;
function format(value: string | number | Date): string {
  if (typeof value === "string") {
    return value.toUpperCase();
  } else if (typeof value === "number") {
    return value.toFixed(2);
  } else {
    return value.toISOString();
  }
}

// Void return type
function logMessage(message: string): void {
  console.log(message);
}

// Never return type
function throwError(message: string): never {
  throw new Error(message);
}

// Function types
type MathOperation = (a: number, b: number) => number;

const subtract: MathOperation = (a, b) => a - b;
const divide: MathOperation = (a, b) => {
  if (b === 0) throw new Error("Division by zero");
  return a / b;
};

// Higher-order functions
function createMultiplier(factor: number): (value: number) => number {
  return (value) => value * factor;
}

const double = createMultiplier(2);
const triple = createMultiplier(3);

// Callback functions
function processData(data: number[], callback: (item: number) => boolean): number[] {
  return data.filter(callback);
}

// Usage examples
console.log(add(5, 3));
console.log(greet("Alice"));
console.log(format(3.14159));
console.log(sum(1, 2, 3, 4, 5));
console.log(double(5));
