/*
 * JavaScript Functions
 * Declaration, expression, arrow, IIFE
 */

// ============================================
// Function Declaration
// ============================================

function greet(name) {
    return `Hello, ${name}!`;
}
console.log(greet("Alice"));

// Hoisted - can be called before declaration
console.log(add(2, 3)); // Works!
function add(a, b) {
    return a + b;
}

// ============================================
// Function Expression
// ============================================

const multiply = function (a, b) {
    return a * b;
};
console.log(multiply(4, 5));

// Not hoisted - must be declared before use
const divide = function (a, b) {
    return a / b;
};

// ============================================
// Arrow Functions
// ============================================

// Concise syntax
const subtract = (a, b) => a - b;
console.log(subtract(10, 3));

// Single parameter - parentheses optional
const double = x => x * 2;
console.log(double(5));

// Multiple statements - need braces and return
const calculate = (a, b) => {
    const sum = a + b;
    const product = a * b;
    return { sum, product };
};
console.log(calculate(3, 4));

// ============================================
// Default Parameters
// ============================================

function createUser(name, role = "user") {
    return { name, role };
}
console.log(createUser("Alice"));        // { name: "Alice", role: "user" }
console.log(createUser("Bob", "admin")); // { name: "Bob", role: "admin" }

// ============================================
// Rest Parameters
// ============================================

function sum(...numbers) {
    return numbers.reduce((total, num) => total + num, 0);
}
console.log(sum(1, 2, 3, 4, 5)); // 15

// ============================================
// IIFE (Immediately Invoked Function Expression)
// ============================================

(function () {
    const private = "I'm private";
    console.log("IIFE executed:", private);
})();

// Arrow IIFE
(() => {
    console.log("Arrow IIFE");
})();

// ============================================
// Higher-Order Functions
// ============================================

// Function that takes a function
function repeat(n, action) {
    for (let i = 0; i < n; i++) {
        action(i);
    }
}

repeat(3, i => console.log(`Iteration ${i}`));

// Function that returns a function
function createMultiplier(multiplier) {
    return function (number) {
        return number * multiplier;
    };
}

const triple = createMultiplier(3);
console.log(triple(5)); // 15

// ============================================
// Closure
// ============================================

function createCounter() {
    let count = 0;
    return {
        increment: () => ++count,
        decrement: () => --count,
        getCount: () => count
    };
}

const counter = createCounter();
console.log(counter.increment()); // 1
console.log(counter.increment()); // 2
console.log(counter.getCount());  // 2
