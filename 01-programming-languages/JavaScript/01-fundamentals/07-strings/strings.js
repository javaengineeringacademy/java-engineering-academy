/*
 * JavaScript Strings
 * String methods, template literals
 */

// ============================================
// Creating Strings
// ============================================

const single = 'Hello';
const double = "World";
const backtick = `Hello World`;

// ============================================
// Template Literals
// ============================================

const name = "Alice";
const age = 30;

// String interpolation
const greeting = `Hello, ${name}! You are ${age} years old.`;
console.log(greeting);

// Expressions in template literals
const price = 10;
const quantity = 3;
console.log(`Total: $${price * quantity}`);

// Multiline strings
const multiline = `
This is a
multiline string
`;
console.log(multiline);

// Tagged templates
function highlight(strings, ...values) {
    return strings.reduce((result, str, i) => {
        const value = values[i] ? `<strong>${values[i]}</strong>` : "";
        return result + str + value;
    }, "");
}
console.log(highlight`Hello ${name}, you are ${age}`);

// ============================================
// String Methods - Searching
// ============================================

const str = "Hello, World! Hello, JavaScript!";

console.log(str.indexOf("Hello"));         // 0
console.log(str.lastIndexOf("Hello"));     // 14
console.log(str.includes("World"));        // true
console.log(str.startsWith("Hello"));      // true
console.log(str.endsWith("JavaScript!"));  // true
console.log(str.search(/world/i));         // 7 (case-insensitive)

// ============================================
// String Methods - Extracting
// ============================================

console.log(str.substring(0, 5));  // "Hello"
console.log(str.slice(7, 12));     // "World"
console.log(str.charAt(0));        // "H"
console.log(str.charCodeAt(0));    // 72

// ============================================
// String Methods - Transforming
// ============================================

console.log(str.toUpperCase());    // "HELLO, WORLD! HELLO, JAVASCRIPT!"
console.log(str.toLowerCase());    // "hello, world! hello, javascript!"
console.log(str.trim());           // Removes whitespace

// ============================================
// String Methods - Replacing
// ============================================

console.log(str.replace("Hello", "Hi"));         // First occurrence
console.log(str.replaceAll("Hello", "Hi"));      // All occurrences

// ============================================
// String Methods - Splitting and Joining
// ============================================

const csv = "apple,banana,cherry";
const fruits = csv.split(",");
console.log(fruits); // ["apple", "banana", "cherry"]

const joined = fruits.join(" - ");
console.log(joined); // "apple - banana - cherry"

// ============================================
// String Methods - Padding
// ============================================

console.log("5".padStart(3, "0"));    // "005"
console.log("5".padEnd(3, "0"));      // "500"
console.log("hello".padStart(10, "-")); // "-----hello"

// ============================================
// String Methods - Repeat
// ============================================

console.log("ha".repeat(3)); // "hahaha"
