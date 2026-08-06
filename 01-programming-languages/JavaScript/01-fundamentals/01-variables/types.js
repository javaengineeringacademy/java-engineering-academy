// JavaScript Data Types

// Primitive Types (7)
// 1. String
const string1 = "Hello"; // Double quotes
const string2 = 'World'; // Single quotes
const string3 = `Template`; // Backticks

// 2. Number
const int = 42;
const float = 3.14;
const negative = -100;
const infinity = Infinity;
const notANumber = NaN;

// 3. BigInt
const bigInt = 9007199254740991n;
const bigInt2 = BigInt("9007199254740991");

// 4. Boolean
const isTrue = true;
const isFalse = false;

// 5. undefined
let notAssigned; // undefined

// 6. null
const empty = null;

// 7. Symbol
const sym1 = Symbol("description");
const sym2 = Symbol("description");
console.log(sym1 === sym2); // false (unique)

// Reference Types (Objects)
// Object
const person = {
  name: "Alice",
  age: 30,
  greet() {
    return `Hi, I'm ${this.name}`;
  }
};

// Array (special object)
const fruits = ["apple", "banana", "cherry"];

// Function (special object)
function add(a, b) {
  return a + b;
}

// Date
const now = new Date();

// RegExp
const pattern = /hello/i;

// Type Checking
console.log(typeof "string"); // "string"
console.log(typeof 42); // "number"
console.log(typeof true); // "boolean"
console.log(typeof undefined); // "undefined"
console.log(typeof null); // "object" (bug)
console.log(typeof {}); // "object"
console.log(typeof []); // "object"
console.log(typeof function(){}); // "function"

// Better array check
console.log(Array.isArray([])); // true
console.log(Array.isArray({})); // false

// Type Coercion
console.log("5" + 3); // "53" (string concatenation)
console.log("5" - 3); // 2 (number subtraction)
console.log(true + 1); // 2
console.log(false + ""); // "false"

// Explicit Conversion
console.log(Number("42")); // 42
console.log(String(42)); // "42"
console.log(Boolean(0)); // false
console.log(Boolean("hello")); // true
console.log(parseInt("42px")); // 42
console.log(parseFloat("3.14abc")); // 3.14

// Truthy and Falsy Values
// Falsy: false, 0, "", null, undefined, NaN
// Truthy: Everything else

console.log(Boolean("")); // false
console.log(Boolean(0)); // false
console.log(Boolean("0")); // true
console.log(Boolean([])); // true
console.log(Boolean({})); // true
