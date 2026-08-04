# JavaScript

A comprehensive guide to modern JavaScript (ES6+) covering core language features, asynchronous programming, DOM manipulation, and module systems.

---

## Table of Contents

1. [ES6+ Features](#es6-features)
2. [Arrow Functions](#arrow-functions)
3. [Template Literals](#template-literals)
4. [Destructuring](#destructuring)
5. [Spread and Rest Operators](#spread-and-rest-operators)
6. [Modules (ESM and CommonJS)](#modules)
7. [Promises](#promises)
8. [Async/Await](#asyncawait)
9. [Closures](#closures)
10. [Prototypes and Prototype Chain](#prototypes)
11. [Event Loop](#event-loop)
12. [DOM Manipulation](#dom-manipulation)
13. [Fetch API](#fetch-api)
14. [Error Handling](#error-handling)

---

## ES6+ Features

### let and const

`let` and `const` replace `var` with block-scoped declarations.

```javascript
// var is function-scoped, can be redeclared and reassigned
var name = 'Alice';
var name = 'Bob'; // no error

// let is block-scoped, can be reassigned but not redeclared
let age = 30;
age = 31; // OK
// let age = 32; // SyntaxError

// const is block-scoped, cannot be reassigned or redeclared
const PI = 3.14159;
// PI = 3.14; // TypeError

// const with objects - properties CAN be mutated
const user = { name: 'Alice' };
user.name = 'Bob'; // OK
// user = {}; // TypeError
```

### Block Scoping

```javascript
for (let i = 0; i < 5; i++) {
  setTimeout(() => console.log(i), 100);
}
// Output: 0, 1, 2, 3, 4

for (var j = 0; j < 5; j++) {
  setTimeout(() => console.log(j), 100);
}
// Output: 5, 5, 5, 5, 5
```

---

## Arrow Functions

Arrow functions provide shorter syntax and lexically bind `this`.

```javascript
// Traditional function
function add(a, b) {
  return a + b;
}

// Arrow function
const add = (a, b) => a + b;

// Single parameter - parentheses optional
const double = x => x * 2;

// No parameters - parentheses required
const greet = () => 'Hello!';

// Multi-line body - braces and return required
const calculate = (a, b) => {
  const sum = a + b;
  return sum * 2;
};

// Returning an object literal
const createUser = (name, age) => ({ name, age });
```

### this Binding

```javascript
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

// Traditional function would lose `this` context
// setInterval(function() {
//   this.seconds++; // `this` is undefined or global
// }, 1000);
```

---

## Template Literals

```javascript
const name = 'Alice';
const age = 30;

// String interpolation
const greeting = `Hello, ${name}! You are ${age} years old.`;

// Expressions inside interpolation
const price = 19.99;
const total = `Total: $${(price * 1.08).toFixed(2)}`;

// Multi-line strings
const html = `
  <div class="card">
    <h2>${name}</h2>
    <p>Age: ${age}</p>
  </div>
`;

// Tagged templates for custom processing
function highlight(strings, ...values) {
  return strings.reduce((result, str, i) => {
    const value = values[i] ? `<mark>${values[i]}</mark>` : '';
    return result + str + value;
  }, '');
}

const highlighted = highlight`Hello ${name}, you are ${age} years old`;
```

---

## Destructuring

### Array Destructuring

```javascript
const numbers = [1, 2, 3, 4, 5];

const [first, second, ...rest] = numbers;
// first = 1, second = 2, rest = [3, 4, 5]

// Skip elements
const [, , third] = numbers;
// third = 3

// Default values
const [a = 0, b = 0, c = 0] = [1, 2];
// a = 1, b = 2, c = 0

// Swapping variables
let x = 1, y = 2;
[x, y] = [y, x];
// x = 2, y = 1
```

### Object Destructuring

```javascript
const user = {
  name: 'Alice',
  age: 30,
  email: 'alice@example.com',
  address: {
    city: 'Seattle',
    state: 'WA'
  }
};

// Basic destructuring
const { name, age } = user;

// Rename variables
const { name: userName, age: userAge } = user;

// Default values
const { name, role = 'user' } = user;

// Nested destructuring
const { address: { city, state } } = user;

// Rest operator with objects
const { name: n, ...userInfo } = user;
// userInfo = { age: 30, email: 'alice@example.com', address: {...} }
```

### Function Parameter Destructuring

```javascript
function createUser({ name, age, role = 'user' }) {
  return { name, age, role };
}

createUser({ name: 'Alice', age: 30 });

function printUser({ name, address: { city } }) {
  console.log(`${name} from ${city}`);
}
```

---

## Spread and Rest Operators

### Spread Operator

```javascript
// Array spread
const arr1 = [1, 2, 3];
const arr2 = [4, 5, 6];
const combined = [...arr1, ...arr2]; // [1, 2, 3, 4, 5, 6]

// Copy array
const copy = [...arr1];

// Object spread
const defaults = { theme: 'light', lang: 'en' };
const userPrefs = { theme: 'dark' };
const config = { ...defaults, ...userPrefs };
// { theme: 'dark', lang: 'en' }

// Spread in function calls
const numbers = [3, 1, 2];
Math.max(...numbers); // 3
```

### Rest Operator

```javascript
// Rest parameters
function sum(...numbers) {
  return numbers.reduce((total, n) => total + n, 0);
}
sum(1, 2, 3, 4); // 10

// Rest with destructuring
const [first, second, ...remaining] = [1, 2, 3, 4, 5];
// remaining = [3, 4, 5]

const { id, ...details } = { id: 1, name: 'Alice', age: 30 };
// details = { name: 'Alice', age: 30 }
```

---

## Modules

### ES Modules (ESM)

```javascript
// math.js - named exports
export const add = (a, b) => a + b;
export const subtract = (a, b) => a - b;
export const PI = 3.14159;

// default export
export default class Calculator {
  add(a, b) { return a + b; }
}

// app.js - importing
import Calculator, { add, subtract, PI } from './math.js';
import * as math from './math.js';

// Dynamic import
const module = await import('./heavy-module.js');
```

### CommonJS (Node.js)

```javascript
// math.js
const add = (a, b) => a + b;
module.exports = { add };

// app.js
const { add } = require('./math.js');

// Dynamic require
const module = require(`./modules/${name}.js`);
```

### ESM vs CommonJS

| Feature | ESM | CommonJS |
|---------|-----|----------|
| Syntax | `import/export` | `require/module.exports` |
| Loading | Asynchronous | Synchronous |
| Static analysis | Yes | No |
| Tree shaking | Yes | No |
| Browser support | Native | Requires bundler |

---

## Promises

```javascript
// Creating a Promise
function fetchUser(id) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (id > 0) {
        resolve({ id, name: 'Alice' });
      } else {
        reject(new Error('Invalid ID'));
      }
    }, 1000);
  });
}

// Promise chaining
fetchUser(1)
  .then(user => fetchUserPosts(user.id))
  .then(posts => console.log(posts))
  .catch(error => console.error(error))
  .finally(() => console.log('Done'));

// Promise.all - all must resolve
const results = await Promise.all([
  fetch('/api/users'),
  fetch('/api/posts'),
  fetch('/api/comments')
]);

// Promise.allSettled - waits for all regardless of outcome
const outcomes = await Promise.allSettled([
  fetch('/api/slow'),
  fetch('/api/fast')
]);
outcomes.forEach(outcome => {
  if (outcome.status === 'fulfilled') {
    console.log('Success:', outcome.value);
  } else {
    console.log('Failed:', outcome.reason);
  }
});

// Promise.race - first to settle wins
const fastest = await Promise.race([
  fetch('/api/server1'),
  fetch('/api/server2')
]);

// Promise.any - first to resolve wins (ignores rejections)
const first = await Promise.any([
  fetch('/api/eu'),
  fetch('/api/us'),
  fetch('/api/apac')
]);
```

---

## Async/Await

```javascript
// Basic async/await
async function getUser(id) {
  try {
    const response = await fetch(`/api/users/${id}`);
    if (!response.ok) throw new Error('User not found');
    const user = await response.json();
    return user;
  } catch (error) {
    console.error('Failed to fetch user:', error);
    throw error;
  }
}

// Parallel execution
async function loadDashboard() {
  const [users, posts, comments] = await Promise.all([
    fetch('/api/users').then(r => r.json()),
    fetch('/api/posts').then(r => r.json()),
    fetch('/api/comments').then(r => r.json())
  ]);
  return { users, posts, comments };
}

// Sequential when order matters
async function processData() {
  const users = await fetchUsers();
  const posts = await fetchPostsForUsers(users);
  const comments = await fetchCommentsForPosts(posts);
  return { users, posts, comments };
}

// Async iteration
async function processItems(items) {
  for (const item of items) {
    await processItem(item);
  }
}

// Async generators
async function* paginatedFetch(url) {
  let page = 1;
  while (true) {
    const response = await fetch(`${url}?page=${page}`);
    const data = await response.json();
    if (data.length === 0) return;
    yield data;
    page++;
  }
}

for await (const page of paginatedFetch('/api/items')) {
  console.log(page);
}
```

---

## Closures

A closure is a function that retains access to its outer scope's variables even after the outer function has returned.

```javascript
// Counter factory
function createCounter(initial = 0) {
  let count = initial;
  return {
    increment: () => ++count,
    decrement: () => --count,
    getCount: () => count
  };
}

const counter = createCounter(10);
counter.increment(); // 11
counter.increment(); // 12
counter.getCount(); // 12

// Private variables
function createPerson(name) {
  let _name = name;
  return {
    getName: () => _name,
    setName: (newName) => { _name = newName; }
  };
}

// Event handler with closure
function setupButton(buttonId) {
  let clickCount = 0;
  const button = document.getElementById(buttonId);
  button.addEventListener('click', () => {
    clickCount++;
    console.log(`Button clicked ${clickCount} times`);
  });
}

// Closure in loop (common pitfall fix)
for (var i = 0; i < 5; i++) {
  (function(j) {
    setTimeout(() => console.log(j), 100);
  })(i);
}
// Output: 0, 1, 2, 3, 4
```

---

## Prototypes

```javascript
// Prototype chain
function Animal(name) {
  this.name = name;
}

Animal.prototype.speak = function() {
  return `${this.name} makes a sound.`;
};

function Dog(name, breed) {
  Animal.call(this, name);
  this.breed = breed;
}

Dog.prototype = Object.create(Animal.prototype);
Dog.prototype.constructor = Dog;

Dog.prototype.fetch = function(item) {
  return `${this.name} fetches the ${item}`;
};

const dog = new Dog('Rex', 'Labrador');
dog.speak(); // "Rex makes a sound."
dog.fetch('ball'); // "Rex fetches the ball"

// ES6 Classes (syntactic sugar over prototypes)
class Vehicle {
  #engine; // Private field

  constructor(brand, model) {
    this.brand = brand;
    this.model = model;
    this.#engine = 'V6';
  }

  get info() {
    return `${this.brand} ${this.model}`;
  }

  static create(brand, model) {
    return new Vehicle(brand, model);
  }

  #startEngine() {
    return `Starting ${this.#engine} engine`;
  }
}

// instanceof check
dog instanceof Dog; // true
dog instanceof Animal; // true
```

---

## Event Loop

```javascript
// Execution order demonstration
console.log('1: Start');

setTimeout(() => console.log('2: Timeout'), 0);

Promise.resolve().then(() => console.log('3: Promise'));

queueMicrotask(() => console.log('4: Microtask'));

console.log('5: End');

// Output: 1, 5, 3, 4, 2

// setTimeout vs setInterval
let count = 0;
const interval = setInterval(() => {
  console.log(`Count: ${count}`);
  count++;
  if (count >= 5) clearInterval(interval);
}, 1000);

// requestAnimationFrame
function animate() {
  // Update DOM
  element.style.transform = `translateX(${position}px)`;
  position += 2;
  if (position < 500) {
    requestAnimationFrame(animate);
  }
}
requestAnimationFrame(animate);

// Web Workers for CPU-intensive tasks
const worker = new Worker('worker.js');
worker.postMessage({ data: largeArray });
worker.onmessage = (event) => {
  console.log('Result:', event.data);
};
```

---

## DOM Manipulation

```javascript
// Selecting elements
const element = document.querySelector('.card');
const elements = document.querySelectorAll('.card');

// Creating elements
const div = document.createElement('div');
div.className = 'card';
div.innerHTML = '<h2>Title</h2><p>Content</p>';
div.setAttribute('data-id', '123');

// Appending
document.body.appendChild(div);
parent.insertBefore(newNode, referenceNode);

// Removing
element.remove();
parent.removeChild(child);

// Event handling
element.addEventListener('click', (event) => {
  event.preventDefault();
  event.stopPropagation();
  console.log('Clicked!', event.target);
});

// Delegation
document.addEventListener('click', (event) => {
  if (event.target.matches('.button')) {
    handleButtonClick(event);
  }
});

// Modern DOM APIs
element.classList.add('active');
element.classList.toggle('active');
element.classList.contains('active');

element.dataset.userId = '123';
element.dataset.userId; // "123"

// Smooth scrolling
element.scrollIntoView({ behavior: 'smooth' });

// Intersection Observer
const observer = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.classList.add('visible');
    }
  });
}, { threshold: 0.1 });

document.querySelectorAll('.lazy-section').forEach(section => {
  observer.observe(section);
});
```

---

## Fetch API

```javascript
// Basic GET request
const response = await fetch('https://api.example.com/users');
const users = await response.json();

// POST request
const response = await fetch('https://api.example.com/users', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify({ name: 'Alice', age: 30 })
});

// Error handling with fetch
async function fetchData(url) {
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    if (error.name === 'AbortError') {
      console.log('Request aborted');
    } else {
      console.error('Fetch error:', error);
    }
    throw error;
  }
}

// AbortController for timeout
const controller = new AbortController();
const timeoutId = setTimeout(() => controller.abort(), 5000);

try {
  const response = await fetch('/api/data', {
    signal: controller.signal
  });
  clearTimeout(timeoutId);
  const data = await response.json();
} catch (error) {
  if (error.name === 'AbortError') {
    console.log('Request timed out');
  }
}

// Streaming responses
const response = await fetch('/api/large-data');
const reader = response.body.getReader();
const decoder = new TextDecoder();

while (true) {
  const { done, value } = await reader.read();
  if (done) break;
  const chunk = decoder.decode(value);
  processChunk(chunk);
}
```

---

## Error Handling

```javascript
// Try-catch-finally
try {
  const data = JSON.parse(invalidJSON);
} catch (error) {
  if (error instanceof SyntaxError) {
    console.error('Invalid JSON:', error.message);
  } else {
    console.error('Unexpected error:', error);
  }
} finally {
  cleanup();
}

// Custom error classes
class ValidationError extends Error {
  constructor(message, field) {
    super(message);
    this.name = 'ValidationError';
    this.field = field;
  }
}

class NotFoundError extends Error {
  constructor(resource, id) {
    super(`${resource} with id ${id} not found`);
    this.name = 'NotFoundError';
    this.resource = resource;
    this.id = id;
  }
}

// Error boundary pattern
class ErrorHandler {
  static handle(error) {
    if (error instanceof ValidationError) {
      return { status: 400, message: error.message };
    }
    if (error instanceof NotFoundError) {
      return { status: 404, message: error.message };
    }
    console.error('Unhandled error:', error);
    return { status: 500, message: 'Internal server error' };
  }
}

// Async error handling
async function safeAsync(fn) {
  try {
    const result = await fn();
    return [result, null];
  } catch (error) {
    return [null, error];
  }
}

const [user, error] = await safeAsync(() => fetchUser(1));
if (error) {
  handleError(error);
}

// Unhandled promise rejection
window.addEventListener('unhandledrejection', (event) => {
  console.error('Unhandled promise rejection:', event.reason);
  event.preventDefault();
});
```

---

## Best Practices

- Use `const` by default, `let` when reassignment is needed, avoid `var`
- Prefer arrow functions for callbacks, traditional functions for methods
- Use destructuring to extract values from objects and arrays
- Leverage spread operator for immutable updates
- Use `async/await` over raw Promises for cleaner async code
- Handle errors at every async boundary
- Use closures to encapsulate private state
- Prefer `class` syntax for object-oriented patterns
- Use `queueMicrotask` for high-priority async work
- Prefer `fetch` over XMLHttpRequest for HTTP requests
- Use `AbortController` for request cancellation
- Write pure functions when possible for testability
