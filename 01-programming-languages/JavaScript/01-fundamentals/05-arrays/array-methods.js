// JavaScript Array Methods (Functional Programming)

const numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

// map - Transform each element
const doubled = numbers.map(n => n * 2);
const strings = numbers.map(n => `${n} items`);

// filter - Select elements
const evens = numbers.filter(n => n % 2 === 0);
const large = numbers.filter(n => n > 5);

// reduce - Accumulate to single value
const sum = numbers.reduce((acc, n) => acc + n, 0);
const product = numbers.reduce((acc, n) => acc * n, 1);

// find - Get first matching element
const firstEven = numbers.find(n => n % 2 === 0);
const firstLarge = numbers.find(n => n > 100); // undefined

// findIndex - Get index of first match
const firstEvenIndex = numbers.findIndex(n => n % 2 === 0);

// some - Check if any element matches
const hasNegative = numbers.some(n => n < 0); // false

// every - Check if all elements match
const allPositive = numbers.every(n => n > 0); // true

// flatMap - Map then flatten
const sentences = ["Hello World", "Goodbye Moon"];
const words = sentences.flatMap(s => s.split(" "));
// ["Hello", "World", "Goodbye", "Moon"]

// Chaining methods
const result = numbers
  .filter(n => n % 2 === 0)
  .map(n => n ** 2)
  .reduce((acc, n) => acc + n, 0);

// Practical examples

// Group by property
const people = [
  { name: "Alice", dept: "Engineering" },
  { name: "Bob", dept: "Marketing" },
  { name: "Charlie", dept: "Engineering" }
];

const grouped = people.reduce((acc, person) => {
  acc[person.dept] = acc[person.dept] || [];
  acc[person.dept].push(person);
  return acc;
}, {});

// Unique values
const duplicates = [1, 2, 2, 3, 3, 3];
const unique = [...new Set(duplicates)];

// Chunk array
function chunk(arr, size) {
  return arr.reduce((chunks, item, i) => {
    const chunkIndex = Math.floor(i / size);
    chunks[chunkIndex] = chunks[chunkIndex] || [];
    chunks[chunkIndex].push(item);
    return chunks;
  }, []);
}

// Flatten with reduce
function flatten(arr) {
  return arr.reduce((acc, item) => {
    return acc.concat(Array.isArray(item) ? flatten(item) : item);
  }, []);
}

// Pipe function (compose from left to right)
const pipe = (...fns) => (x) => fns.reduce((v, f) => f(v), x);

const processNumbers = pipe(
  nums => nums.filter(n => n > 0),
  nums => nums.map(n => n * 2),
  nums => nums.reduce((a, b) => a + b, 0)
);

// Method chaining with objects
const transactions = [
  { id: 1, amount: 100, type: "credit" },
  { id: 2, amount: 50, type: "debit" },
  { id: 3, amount: 200, type: "credit" }
];

const totalCredit = transactions
  .filter(t => t.type === "credit")
  .map(t => t.amount)
  .reduce((a, b) => a + b, 0);

// Performance note: forEach vs for
// forEach creates function scope for each iteration
// for loop is faster for simple operations
// Use forEach for readability when performance isn't critical
