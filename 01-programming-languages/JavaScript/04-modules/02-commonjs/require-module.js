/*
 * CommonJS Modules
 * require, module.exports
 */

// ============================================
// Exporting with module.exports
// ============================================

// math.js - CommonJS export

const PI = 3.14159;
const E = 2.71828;

function add(a, b) {
    return a + b;
}

function subtract(a, b) {
    return a - b;
}

// Export single value/function
// module.exports = add;

// OR export object with multiple exports
module.exports = {
    PI,
    E,
    add,
    subtract,
    multiply: (a, b) => a * b,
    divide: (a, b) => a / b
};

// ============================================
// Exporting with exports object
// ============================================

// utils.js - Using exports object

exports.formatDate = (date) => {
    return date.toISOString().split("T")[0];
};

exports.formatCurrency = (amount) => {
    return `$${amount.toFixed(2)}`;
};

exports.capitalize = (str) => {
    return str.charAt(0).toUpperCase() + str.slice(1);
};

// ============================================
// Importing with require
// ============================================

// main.js - Importing modules

// Single export
// const add = require("./math.js");
// console.log(add(2, 3));

// Multiple exports (object destructuring)
// const { PI, E, subtract } = require("./math.js");
// console.log(PI);
// console.log(subtract(5, 3));

// Import built-in modules
const fs = require("fs");
const path = require("path");

// ============================================
// Module Caching
// ============================================

// Modules are cached after first require
// const math1 = require("./math.js");
// const math2 = require("./math.js");
// console.log(math1 === math2); // true (same object)

// ============================================
// __dirname and __filename
// ============================================

console.log("Directory:", __dirname);
console.log("File:", __filename);

// ============================================
// Common Patterns
// ============================================

// Factory pattern
module.exports = function createLogger(options) {
    return {
        log: (msg) => console.log(`[${options.level}] ${msg}`)
    };
};

// Singleton pattern
let instance = null;

module.exports = function getInstance() {
    if (!instance) {
        instance = { value: 0 };
    }
    return instance;
};
