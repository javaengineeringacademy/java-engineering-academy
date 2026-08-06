/*
 * JavaScript Operators
 * Arithmetic, comparison, logical, nullish
 */

// ============================================
// Arithmetic Operators
// ============================================

const a = 10;
const b = 3;

console.log("Arithmetic Operators:");
console.log(`a + b = ${a + b}`);   // Addition: 13
console.log(`a - b = ${a - b}`);   // Subtraction: 7
console.log(`a * b = ${a * b}`);   // Multiplication: 30
console.log(`a / b = ${a / b}`);   // Division: 3.333...
console.log(`a % b = ${a % b}`);   // Modulo: 1
console.log(`a ** b = ${a ** b}`); // Exponentiation: 1000

// Increment and Decrement
let counter = 5;
counter++; // Post-increment: 6
++counter; // Pre-increment: 7
counter--; // Post-decrement: 6
--counter; // Pre-decrement: 5

// ============================================
// Comparison Operators
// ============================================

console.log("\nComparison Operators:");

// Loose equality (==) - performs type coercion
console.log(5 == "5");    // true
console.log(0 == false);  // true
console.log(null == undefined); // true

// Strict equality (===) - no type coercion
console.log(5 === "5");   // false
console.log(0 === false); // false
console.log(null === undefined); // false

// Inequality operators
console.log(5 != "5");    // false (loose)
console.log(5 !== "5");   // true (strict)
console.log(5 > 3);      // true
console.log(5 < 3);      // false
console.log(5 >= 5);     // true

// ============================================
// Logical Operators
// ============================================

console.log("\nLogical Operators:");

const x = true;
const y = false;

// AND (&&) - returns first falsy or last value
console.log(x && y);       // false
console.log(x && "hello"); // "hello"
console.log(y && "hello"); // false

// OR (||) - returns first truthy or last value
console.log(x || y);            // true
console.log(y || "hello");      // "hello"
console.log("" || "default");   // "default"

// NOT (!) - converts to boolean then negates
console.log(!true);  // false
console.log(!0);     // true
console.log(!"");    // true

// ============================================
// Nullish Coalescing (??)
// ============================================

// Returns right side only if left is null or undefined
console.log(null ?? "default");       // "default"
console.log(undefined ?? "default");  // "default"
console.log(0 ?? "default");         // 0 (0 is not nullish)
console.log("" ?? "default");        // "" (empty string is not nullish)

// ============================================
// Nullish Coalescing Assignment (??=)
// ============================================

let config = null;
config ??= { theme: "dark" }; // Assigns if null/undefined
console.log(config); // { theme: "dark" }

let existing = "keep";
existing ??= "new"; // Does not assign
console.log(existing); // "keep"

// ============================================
// Optional Chaining (?.)
// ============================================

const user = {
    name: "Alice",
    address: {
        city: "Wonderland"
    }
};

console.log(user?.name);          // "Alice"
console.log(user?.address?.city); // "Wonderland"
console.log(user?.phone);         // undefined (no error)

// ============================================
// Ternary Operator
// ============================================

const age = 20;
const status = age >= 18 ? "adult" : "minor";
console.log(`Status: ${status}`); // "adult"

// ============================================
// Operator Precedence
// ============================================

// Remember: PEMDAS/BODMAS applies
console.log(2 + 3 * 4);   // 14 (not 20)
console.log((2 + 3) * 4); // 20

// Best Practice: Use parentheses for clarity
const result = (a + b) * (a - b);
