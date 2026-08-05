# JavaScript Cheat Sheet

## Variables

```javascript
var x = 10;          // Function scoped, hoisted
let y = 20;          // Block scoped
const z = 30;        // Block scoped, immutable
```

## Data Types

```javascript
// Primitives
let str = "hello";
let num = 42;
let big = 9007199254740991n;
let bool = true;
let undef = undefined;
let empty = null;
let sym = Symbol('id');

// Reference
let obj = { key: 'value' };
let arr = [1, 2, 3];
let func = () => {};
```

## Functions

```javascript
function add(a, b) { return a + b; }
const subtract = (a, b) => a - b;
const greet = (name = 'World') => `Hello, ${name}!`;
const sum = (...nums) => nums.reduce((a, b) => a + b);
```

## Control Flow

```javascript
if (condition) {} else if (other) {} else {}

switch (value) {
    case 1: break;
    case 2: break;
    default: break;
}

for (let i = 0; i < 10; i++) {}
for (const item of array) {}
for (const key in object) {}
while (condition) {}
do {} while (condition);
```

## Arrays

```javascript
const arr = [1, 2, 3, 4, 5];

arr.map(x => x * 2);           // [2, 4, 6, 8, 10]
arr.filter(x => x > 2);        // [3, 4, 5]
arr.reduce((a, b) => a + b, 0); // 15
arr.find(x => x > 2);          // 3
arr.findIndex(x => x > 2);     // 2
arr.some(x => x > 3);          // true
arr.every(x => x > 0);         // true
arr.includes(3);                // true
arr.flat();                     // [1, 2, 3, 4, 5]
arr.flatMap(x => [x, x * 2]); // [1, 2, 2, 4, 3, 6, 4, 8, 5, 10]

arr.slice(1, 3);    // [2, 3]
arr.splice(1, 1);   // removes 1 element at index 1
arr.push(6);        // adds to end
arr.pop();          // removes from end
arr.unshift(0);     // adds to beginning
arr.shift();        // removes from beginning
arr.reverse();
arr.sort((a, b) => a - b);
```

## Objects

```javascript
const obj = { name: 'Alice', age: 30 };

const { name, age } = obj;           // Destructuring
const { name: n, age: a } = obj;     // Rename
const { city = 'NYC' } = obj;        // Default

const merged = { ...obj, city: 'NYC' }; // Spread
Object.keys(obj);     // ['name', 'age']
Object.values(obj);   // ['Alice', 30]
Object.entries(obj);  // [['name', 'Alice'], ['age', 30]]
Object.assign({}, obj, { city: 'NYC' });
Object.freeze(obj);
```

## Async

```javascript
// Promise
const p = new Promise((resolve, reject) => {
    resolve(value);
});

p.then(v => console.log(v))
 .catch(e => console.error(e))
 .finally(() => console.log('done'));

// Async/Await
async function getData() {
    try {
        const data = await fetch('/api');
        return await data.json();
    } catch (error) {
        console.error(error);
    }
}

// Parallel
const [a, b] = await Promise.all([fetchA(), fetchB()]);
```

## DOM

```javascript
document.querySelector('.class');
document.querySelectorAll('.class');
document.getElementById('id');
document.createElement('div');
element.textContent = 'text';
element.innerHTML = '<b>html</b>';
element.classList.add('active');
element.setAttribute('href', url);
element.addEventListener('click', handler);
element.removeEventListener('click', handler);
```

## ES6+ Features

```javascript
// Optional chaining
const street = user?.address?.street;

// Nullish coalescing
const value = input ?? 'default';

// Map and Set
const map = new Map([['key', 'value']]);
const set = new Set([1, 2, 3]);

// Destructuring
const { a: x, b: y } = { a: 1, b: 2 };
const [first, , third] = [1, 2, 3];

// Template literals
const msg = `Hello, ${name}!`;

// Shorthand properties
const name = 'Alice';
const person = { name };

// Computed property names
const prop = 'name';
const obj = { [prop]: 'Alice' };
```
