/*
 * JavaScript Control Flow: Loops
 * for, while, for...of, for...in
 */

// ============================================
// for Loop
// ============================================

console.log("for loop:");
for (let i = 0; i < 5; i++) {
    console.log(`Iteration ${i}`);
}

// ============================================
// while Loop
// ============================================

console.log("\nwhile loop:");
let count = 0;
while (count < 5) {
    console.log(`Count: ${count}`);
    count++;
}

// ============================================
// do...while Loop
// ============================================

console.log("\ndo...while loop:");
let num = 0;
do {
    console.log(`Number: ${num}`);
    num++;
} while (num < 5);

// ============================================
// for...of Loop (iterates over values)
// ============================================

console.log("\nfor...of loop:");
const fruits = ["apple", "banana", "cherry"];

for (const fruit of fruits) {
    console.log(`Fruit: ${fruit}`);
}

// Works with strings
const word = "Hello";
for (const char of word) {
    console.log(`Character: ${char}`);
}

// Works with Maps
const map = new Map([["a", 1], ["b", 2]]);
for (const [key, value] of map) {
    console.log(`${key}: ${value}`);
}

// ============================================
// for...in Loop (iterates over keys/indices)
// ============================================

console.log("\nfor...in loop:");
const person = { name: "Alice", age: 30, city: "NYC" };

for (const key in person) {
    console.log(`${key}: ${person[key]}`);
}

// with arrays (indices)
const colors = ["red", "green", "blue"];
for (const index in colors) {
    console.log(`Index ${index}: ${colors[index]}`);
}

// ============================================
// break and continue
// ============================================

console.log("\nbreak example:");
for (let i = 0; i < 10; i++) {
    if (i === 5) break; // Exit loop when i is 5
    console.log(i);
}

console.log("\ncontinue example:");
for (let i = 0; i < 10; i++) {
    if (i % 2 === 0) continue; // Skip even numbers
    console.log(i);
}

// ============================================
// Labeled Statements
// ============================================

console.log("\nlabeled loop:");
outer: for (let i = 0; i < 3; i++) {
    for (let j = 0; j < 3; j++) {
        if (i === 1 && j === 1) break outer;
        console.log(`${i}, ${j}`);
    }
}

// ============================================
// Array Iteration Methods (Functional Approach)
// ============================================

const numbers = [1, 2, 3, 4, 5];

// forEach - no break/continue possible
numbers.forEach((num, index) => {
    console.log(`Index ${index}: ${num}`);
});

// map - creates new array
const doubled = numbers.map(num => num * 2);
console.log("Doubled:", doubled);

// filter - creates new array with matching elements
const evens = numbers.filter(num => num % 2 === 0);
console.log("Evens:", evens);
