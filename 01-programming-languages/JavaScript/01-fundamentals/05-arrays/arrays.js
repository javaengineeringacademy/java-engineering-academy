/*
 * JavaScript Arrays
 * Array methods: map, filter, reduce, forEach
 */

// ============================================
// Creating Arrays
// ============================================

const fruits = ["apple", "banana", "cherry"];
const numbers = [1, 2, 3, 4, 5];
const mixed = [1, "two", true, null, { key: "value" }];
const empty = [];

// Array constructor
const arr1 = new Array(5); // Creates array with 5 empty slots
const arr2 = Array.from("Hello"); // ["H", "e", "l", "l", "o"]
const arr3 = Array.of(1, 2, 3); // [1, 2, 3]

// ============================================
// Accessing and Modifying
// ============================================

console.log(fruits[0]);      // "apple"
console.log(fruits.length);  // 3
fruits[1] = "blueberry";     // Modify element
fruits.push("date");         // Add to end
fruits.unshift("avocado");   // Add to start
const last = fruits.pop();   // Remove from end
const first = fruits.shift(); // Remove from start

// ============================================
// map() - Transform each element
// ============================================

const doubled = numbers.map(num => num * 2);
console.log("Doubled:", doubled); // [2, 4, 6, 8, 10]

const names = fruits.map(fruit => fruit.toUpperCase());
console.log("Uppercase:", names);

// ============================================
// filter() - Select elements matching condition
// ============================================

const evens = numbers.filter(num => num % 2 === 0);
console.log("Evens:", evens); // [2, 4]

const longFruits = fruits.filter(fruit => fruit.length > 5);
console.log("Long fruits:", longFruits);

// ============================================
// reduce() - Accumulate into single value
// ============================================

const sum = numbers.reduce((accumulator, current) => {
    return accumulator + current;
}, 0);
console.log("Sum:", sum); // 15

const wordLengths = fruits.reduce((acc, fruit) => {
    acc[fruit] = fruit.length;
    return acc;
}, {});
console.log("Word lengths:", wordLengths);

// ============================================
// forEach() - Execute function for each element
// ============================================

numbers.forEach((num, index) => {
    console.log(`Index ${index}: ${num}`);
});

// ============================================
// find() and findIndex()
// ============================================

const found = numbers.find(num => num > 3);
console.log("Found:", found); // 4

const foundIndex = numbers.findIndex(num => num > 3);
console.log("Found index:", foundIndex); // 3

// ============================================
// some() and every()
// ============================================

const hasNegative = numbers.some(num => num < 0);
console.log("Has negative:", hasNegative); // false

const allPositive = numbers.every(num => num > 0);
console.log("All positive:", allPositive); // true

// ============================================
// flat() and flatMap()
// ============================================

const nested = [1, [2, 3], [4, [5, 6]]];
const flat = nested.flat(Infinity);
console.log("Flat:", flat); // [1, 2, 3, 4, 5, 6]

const sentences = ["Hello World", "Goodbye Moon"];
const words = sentences.flatMap(sentence => sentence.split(" "));
console.log("Words:", words);

// ============================================
// includes(), indexOf(), slice(), splice()
// ============================================

console.log("Includes 3:", numbers.includes(3)); // true
console.log("Index of 3:", numbers.indexOf(3)); // 2

const sliced = numbers.slice(1, 4); // [2, 3, 4]
console.log("Sliced:", sliced);

const removed = numbers.splice(2, 1); // Remove 1 element at index 2
console.log("Removed:", removed); // [3]
console.log("After splice:", numbers);

// ============================================
// sort()
// ============================================

const unsorted = [3, 1, 4, 1, 5, 9, 2, 6];
const sorted = [...unsorted].sort((a, b) => a - b); // Ascending
console.log("Sorted:", sorted);

const sortedDesc = [...unsorted].sort((a, b) => b - a); // Descending
console.log("Sorted desc:", sortedDesc);
