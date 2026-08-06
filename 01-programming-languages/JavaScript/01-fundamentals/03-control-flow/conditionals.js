/*
 * JavaScript Control Flow: Conditionals
 * if/else, switch, ternary
 */

// ============================================
// if/else if/else Statement
// ============================================

const temperature = 25;

if (temperature > 35) {
    console.log("It's very hot!");
} else if (temperature > 25) {
    console.log("It's warm outside.");
} else if (temperature > 15) {
    console.log("It's mild.");
} else {
    console.log("It's cold!");
}

// ============================================
// Nested if Statements
// ============================================

const age = 20;
const hasLicense = true;

if (age >= 18) {
    if (hasLicense) {
        console.log("You can drive.");
    } else {
        console.log("You need a license.");
    }
} else {
    console.log("You're too young to drive.");
}

// ============================================
// Truthy and Falsy Values
// ============================================

// Falsy values: false, 0, "", null, undefined, NaN
// Truthy values: everything else

const values = [0, 1, "", "hello", null, undefined, NaN, false, true, [], {}];

values.forEach(val => {
    if (val) {
        console.log(`${JSON.stringify(val)} is truthy`);
    } else {
        console.log(`${JSON.stringify(val)} is falsy`);
    }
});

// ============================================
// Switch Statement
// ============================================

const dayOfWeek = "Monday";

switch (dayOfWeek) {
    case "Monday":
        console.log("Start of work week");
        break;
    case "Tuesday":
    case "Wednesday":
    case "Thursday":
        console.log("Mid-week");
        break;
    case "Friday":
        console.log("Almost weekend!");
        break;
    case "Saturday":
    case "Sunday":
        console.log("Weekend!");
        break;
    default:
        console.log("Invalid day");
}

// Switch with strict comparison
const value = "5";

switch (value) {
    case 5:
        console.log("Number 5");
        break;
    case "5":
        console.log("String 5");
        break;
    default:
        console.log("Something else");
}

// ============================================
// Ternary Operator (Conditional Expression)
// ============================================

const hour = 14;
const greeting = hour < 12 ? "Good Morning" : "Good Afternoon";
console.log(greeting);

// Nested ternary (use sparingly for readability)
const score = 85;
const grade = score >= 90 ? "A"
    : score >= 80 ? "B"
        : score >= 70 ? "C"
            : "F";
console.log(`Grade: ${grade}`);

// ============================================
// Logical Operators in Conditions
// ============================================

const isLoggedIn = true;
const isAdmin = false;

// AND - both conditions must be true
if (isLoggedIn && isAdmin) {
    console.log("Welcome, Admin!");
} else if (isLoggedIn) {
    console.log("Welcome, User!");
}

// OR - at least one condition must be true
if (isLoggedIn || isAdmin) {
    console.log("Access granted");
}

// NOT - inverts the boolean
if (!isAdmin) {
    console.log("Admin privileges not available");
}

// ============================================
// Short-circuit Evaluation
// ============================================

// AND (&&) returns first falsy or last value
const username = "Alice" && "User"; // "User"
const empty = "" && "User";          // ""

// OR (||) returns first truthy or last value
const name1 = "" || "Anonymous";     // "Anonymous"
const name2 = "Bob" || "Anonymous";  // "Bob"

console.log(`Username: ${username}, Name: ${name2}`);
