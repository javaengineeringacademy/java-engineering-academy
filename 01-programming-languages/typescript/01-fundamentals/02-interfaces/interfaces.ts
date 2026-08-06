// TypeScript Interfaces - Basic, Extending, Optional

// Basic interface
interface User {
  name: string;
  age: number;
  email: string;
}

let user1: User = {
  name: "Alice",
  age: 30,
  email: "alice@example.com"
};

// Interface with optional properties
interface Product {
  id: number;
  name: string;
  price: number;
  description?: string; // Optional
  category?: string; // Optional
}

let product1: Product = {
  id: 1,
  name: "Laptop",
  price: 999.99
};

// Interface extending another interface
interface Employee extends User {
  employeeId: number;
  department: string;
  startDate: Date;
}

let emp1: Employee = {
  name: "Bob",
  age: 25,
  email: "bob@company.com",
  employeeId: 12345,
  department: "Engineering",
  startDate: new Date()
};

// Multiple interface inheritance
interface Contact {
  phone: string;
  address: string;
}

interface Person extends User, Contact {
  occupation: string;
}

let person1: Person = {
  name: "Charlie",
  age: 35,
  email: "charlie@email.com",
  phone: "555-1234",
  address: "123 Main St",
  occupation: "Developer"
};

// Interface for function types
interface SearchFunc {
  (source: string, subString: string): boolean;
}

let mySearch: SearchFunc = (source, subString) => {
  return source.search(subString) > -1;
};

// Indexable interfaces
interface StringArray {
  [index: number]: string;
}

let fruits: StringArray = ["apple", "banana", "cherry"];

// Readonly properties
interface Config {
  readonly apiUrl: string;
  readonly timeout: number;
}

let config: Config = {
  apiUrl: "https://api.example.com",
  timeout: 5000
};
// config.apiUrl = "new url"; // Error: readonly property

console.log("Interfaces example running");
