# JavaScript Core Concepts

## Variables

```javascript
var x = 10;          // Function scoped, hoisted
let y = 20;          // Block scoped, not hoisted
const z = 30;        // Block scoped, immutable binding
```

- `var` is function-scoped and hoisted
- `let` is block-scoped and not hoisted
- `const` is block-scoped and cannot be reassigned
- Use `const` by default, `let` when needed, avoid `var`

## Data Types

- **Primitive**: string, number, bigint, boolean, undefined, symbol, null
- **Reference**: object, array, function
- Primitives are immutable and compared by value
- References are mutable and compared by reference

## Functions

```javascript
// Function declaration
function add(a, b) {
    return a + b;
}

// Function expression
const subtract = function(a, b) {
    return a - b;
};

// Arrow function
const multiply = (a, b) => a * b;

// Default parameters
function greet(name = 'World') {
    return `Hello, ${name}!`;
}

// Rest parameters
function sum(...numbers) {
    return numbers.reduce((a, b) => a + b, 0);
}
```

## Closures

A closure is a function that remembers its lexical scope:

```javascript
function counter() {
    let count = 0;
    return {
        increment() { return ++count; },
        decrement() { return --count; },
        getCount() { return count; }
    };
}

const c = counter();
c.increment(); // 1
c.increment(); // 2
c.getCount();  // 2
```

## Prototypes

Every object has a `[[Prototype]]`:

```javascript
function Person(name) {
    this.name = name;
}

Person.prototype.greet = function() {
    return `Hello, ${this.name}`;
};

// ES6 classes
class Animal {
    constructor(name) {
        this.name = name;
    }

    speak() {
        return `${this.name} makes a noise`;
    }
}
```

## Promises

```javascript
const promise = new Promise((resolve, reject) => {
    if (success) {
        resolve(value);
    } else {
        reject(error);
    }
});

promise
    .then(value => console.log(value))
    .catch(error => console.error(error))
    .finally(() => console.log('done'));

Promise.all([p1, p2, p3]);
Promise.allSettled([p1, p2, p3]);
Promise.race([p1, p2, p3]);
Promise.any([p1, p2, p3]);
```

## Async/Await

```javascript
async function fetchData() {
    try {
        const response = await fetch('/api/data');
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error);
    } finally {
        console.log('Complete');
    }
}
```

## Destructuring

```javascript
// Object destructuring
const { name, age, city = 'Unknown' } = person;

// Array destructuring
const [first, second, ...rest] = array;

// Nested destructuring
const { address: { street, city } } = person;
```

## Spread and Rest

```javascript
// Spread operator
const newArray = [...oldArray, 4, 5];
const newObject = { ...oldObject, key: 'value' };

// Rest parameters
function fn(first, ...rest) {
    // rest is an array of remaining arguments
}
```

## Modules

```javascript
// math.js
export const add = (a, b) => a + b;
export default class Calculator {}

// app.js
import Calculator, { add } from './math.js';
import * as math from './math.js';
```
