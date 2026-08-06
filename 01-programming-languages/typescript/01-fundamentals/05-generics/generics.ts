// TypeScript Generics - Generic functions, classes, constraints

// Generic function
function identity<T>(arg: T): T {
  return arg;
}

let num = identity<number>(42);
let str = identity<string>("hello");

// Generic arrow function
const getLength = <T extends { length: number }>(arg: T): number => {
  return arg.length;
};

console.log(getLength("hello")); // 5
console.log(getLength([1, 2, 3])); // 3

// Multiple type parameters
function merge<T, U>(obj1: T, obj2: U): T & U {
  return { ...obj1, ...obj2 };
}

let merged = merge({ name: "Alice" }, { age: 30 });

// Generic constraints
interface HasLength {
  length: number;
}

function logLength<T extends HasLength>(arg: T): void {
  console.log(`Length: ${arg.length}`);
}

logLength("hello"); // Works
logLength([1, 2, 3]); // Works
// logLength(123); // Error: number doesn't have length

// Generic interface
interface Repository<T> {
  getById(id: number): T;
  getAll(): T[];
  save(item: T): void;
  delete(id: number): boolean;
}

// Generic class
class InMemoryRepository<T> implements Repository<T> {
  private items: T[] = [];
  
  getById(id: number): T {
    return this.items[id];
  }
  
  getAll(): T[] {
    return this.items;
  }
  
  save(item: T): void {
    this.items.push(item);
  }
  
  delete(id: number): boolean {
    if (this.items[id]) {
      this.items.splice(id, 1);
      return true;
    }
    return false;
  }
}

// Using generic class
interface User {
  id: number;
  name: string;
}

let userRepo = new InMemoryRepository<User>();
userRepo.save({ id: 1, name: "Alice" });

// Generic with keyof constraint
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

let user = { name: "Alice", age: 30 };
let name = getProperty(user, "name"); // string

// Generic default type
interface ApiResponse<T = unknown> {
  data: T;
  status: number;
  message: string;
}

let response: ApiResponse<User> = {
  data: { id: 1, name: "Alice" },
  status: 200,
  message: "Success"
};

console.log("Generics example running");
