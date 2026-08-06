/*
 * JavaScript Variables
 * var, let, const, types, typeof
 */

// ============================================
// var - Function scoped, hoisted, can redeclare
// ============================================

var globalVar = "I am function scoped";
var redeclare = 1;
var redeclare = 2; // Allowed with var

function varExample() {
    var functionScoped = "Only in this function";
    if (true) {
        var insideIf = "Still accessible outside if block";
    }
    console.log(insideIf); // Works! var ignores block scope
}

// ============================================
// let - Block scoped, cannot redeclare
// ============================================

let count = 0;
// let count = 1; // SyntaxError: Identifier 'count' has already been declared

if (true) {
    let blockScoped = "Only in this block";
    count = 10; // Can reassign
}
// console.log(blockScoped); // ReferenceError: blockScoped is not defined

// ============================================
// const - Block scoped, cannot reassign
// ============================================

const PI = 3.14159;
// PI = 3; // TypeError: Assignment to constant variable

const user = { name: "Alice" };
user.name = "Bob"; // Allowed - modifying property, not reassigning
// user = {}; // TypeError: Assignment to constant variable

// ============================================
// Data Types
// ============================================

const stringType = "Hello";
const numberType = 42;
const floatType = 3.14;
const booleanType = true;
const nullType = null;
let undefinedType; // undefined
const symbolType = Symbol("id");
const bigintType = 9007199254740991n;
const objectType = { key: "value" };
const arrayType = [1, 2, 3];
const functionType = () => {};

// ============================================
// typeof Operator
// ============================================

console.log(typeof stringType);    // "string"
console.log(typeof numberType);    // "number"
console.log(typeof booleanType);   // "boolean"
console.log(typeof nullType);      // "object" (historical bug in JS)
console.log(typeof undefinedType); // "undefined"
console.log(typeof symbolType);    // "symbol"
console.log(typeof bigintType);    // "bigint"
console.log(typeof objectType);    // "object"
console.log(typeof arrayType);     // "object" (arrays are objects)
console.log(typeof functionType);  // "function"

// ============================================
// Type Coercion Examples
// ============================================

console.log("5" + 3);    // "53" (string concatenation)
console.log("5" - 3);    // 2 (numeric subtraction)
console.log(true + 1);   // 2 (true becomes 1)
console.log(false + ""); // "false" (boolean to string)

// Loose vs Strict Equality
console.log(null == undefined);  // true
console.log(null === undefined); // false
console.log("0" == false);      // true
console.log("0" === false);     // false

// ============================================
// Best Practices
// ============================================

// 1. Use const by default, let when reassignment is needed
// 2. Avoid var due to function scoping issues
// 3. Use descriptive variable names
// 4. Declare variables at the top of their scope
// 5. Use camelCase for variables and functions
// 6. Use UPPER_SNAKE_CASE for true constants
