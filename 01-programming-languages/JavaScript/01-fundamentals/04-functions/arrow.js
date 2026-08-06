// JavaScript Arrow Functions

// Basic syntax
const add = (a, b) => a + b;

// Single parameter (parentheses optional)
const square = x => x * x;

// No parameters (require parentheses)
const sayHello = () => "Hello!";

// Multi-line body (require braces and return)
const calculate = (a, b) => {
  const sum = a + b;
  const product = a * b;
  return { sum, product };
};

// Returning objects (require parentheses around object)
const createUser = (name, age) => ({ name, age });

// Array methods with arrow functions
const numbers = [1, 2, 3, 4, 5];

const doubled = numbers.map(n => n * 2);
const evens = numbers.filter(n => n % 2 === 0);
const sum = numbers.reduce((acc, n) => acc + n, 0);

// Chaining
const result = numbers
  .filter(n => n > 2)
  .map(n => n * 10)
  .reduce((acc, n) => acc + n, 0);

// Arrow functions and `this`
class Timer {
  constructor() {
    this.seconds = 0;
  }

  start() {
    // Arrow function inherits `this` from enclosing scope
    setInterval(() => {
      this.seconds++;
      console.log(this.seconds);
    }, 1000);
  }
}

// Regular function `this` issue
class TimerBroken {
  constructor() {
    this.seconds = 0;
  }

  start() {
    // Regular function has its own `this`
    setInterval(function() {
      this.seconds++; // this is undefined or global
      console.log(this.seconds);
    }, 1000);
  }
}

// Arrow functions in callbacks
const people = [
  { name: "Alice", age: 30 },
  { name: "Bob", age: 25 },
  { name: "Charlie", age: 35 }
];

// Good: arrow function preserves context
const adults = people.filter(p => p.age >= 18);

// Arrow functions cannot be used as constructors
const Person = (name) => ({ name });
// const alice = new Person("Alice"); // TypeError!

// Arrow functions don't have arguments object
const logArgs = () => {
  // console.log(arguments); // ReferenceError
};

// Rest parameters instead
const logArgsFixed = (...args) => {
  console.log(args);
};

// Method shorthand vs arrow
const obj = {
  // Method shorthand (has own `this`)
  greet() {
    return `Hello, ${this.name}`;
  },
  // Arrow (inherits `this` from outer scope)
  greetArrow: () => {
    return `Hello, ${this.name}`; // this is NOT obj
  }
};

// Currying with arrows
const addCurry = (a) => (b) => a + b;
const add5 = addCurry(5);
console.log(add5(3)); // 8

// Partial application
const log = (level) => (message) => console.log(`[${level}] ${message}`);
const error = log("ERROR");
error("Something went wrong");

// When NOT to use arrow functions
// 1. Object methods
// 2. Constructor functions
// 3. Prototype methods
// 4. Event handlers (when you need `this`)
// 5. Generator functions
