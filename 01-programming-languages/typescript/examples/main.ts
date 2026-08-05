// TypeScript Fundamentals Example

// Basic types
const name: string = "TypeScript";
const version: number = 5.3;
const isActive: boolean = true;
console.log(`Language: ${name}, Version: ${version}`);

// Interfaces
interface Person {
  name: string;
  age: number;
  email?: string; // Optional
}

const p: Person = {
  name: "Alice",
  age: 30,
};
console.log("Person:", p);

// Type aliases
type ID = string | number;
type Result<T> = {
  data: T;
  success: boolean;
};

const result: Result<Person> = {
  data: p,
  success: true,
};
console.log("Result:", result);

// Generics
function identity<T>(arg: T): T {
  return arg;
}
console.log("Identity:", identity<string>("hello"));

// Enums
enum Status {
  Active = "ACTIVE",
  Inactive = "INACTIVE",
  Pending = "PENDING",
}

const userStatus: Status = Status.Active;
console.log("Status:", userStatus);

// Utility types
type Partial<T> = {
  [P in keyof T]?: T[P];
};

type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};

// Classes with access modifiers
class Calculator {
  private result: number = 0;

  add(n: number): Calculator {
    this.result += n;
    return this;
  }

  getResult(): number {
    return this.result;
  }
}

const calc = new Calculator();
calc.add(5).add(3).add(2);
console.log("Calculator result:", calc.getResult());
