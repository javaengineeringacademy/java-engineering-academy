/*
 * JavaScript Objects
 * Object creation, destructuring, spread
 */

// ============================================
// Object Creation
// ============================================

// Object literal
const person = {
    name: "Alice",
    age: 30,
    greet() {
        return `Hello, I'm ${this.name}`;
    }
};

// Constructor function
function Car(make, model) {
    this.make = make;
    this.model = model;
}
const myCar = new Car("Toyota", "Camry");

// Object.create
const proto = { greet() { return `Hi, I'm ${this.name}`; } };
const bob = Object.create(proto);
bob.name = "Bob";

// ============================================
// Accessing Properties
// ============================================

console.log(person.name);     // Dot notation
console.log(person["age"]);   // Bracket notation
console.log(person.greet());  // Method call

const key = "name";
console.log(person[key]);     // Dynamic key access

// ============================================
// Destructuring
// ============================================

const { name, age } = person;
console.log(`${name} is ${age} years old`);

// Rename variables
const { name: personName, age: personAge } = person;
console.log(`${personName} is ${personAge}`);

// Default values
const { email = "N/A" } = person;
console.log(`Email: ${email}`);

// Nested destructuring
const user = {
    id: 1,
    address: {
        street: "123 Main St",
        city: "NYC"
    }
};
const { address: { city } } = user;
console.log(`City: ${city}`);

// ============================================
// Spread Operator
// ============================================

// Copy object
const personCopy = { ...person };
console.log("Copy:", personCopy);

// Merge objects
const defaults = { theme: "dark", lang: "en" };
const settings = { theme: "light" };
const config = { ...defaults, ...settings };
console.log("Config:", config); // theme is "light" (overrides default)

// Add properties
const updated = { ...person, email: "alice@example.com", age: 31 };
console.log("Updated:", updated);

// ============================================
// Object Methods
// ============================================

const obj = { a: 1, b: 2, c: 3 };

// Object.keys - array of keys
console.log("Keys:", Object.keys(obj));

// Object.values - array of values
console.log("Values:", Object.values(obj));

// Object.entries - array of [key, value] pairs
console.log("Entries:", Object.entries(obj));

// Object.assign - merge into target
const target = {};
Object.assign(target, obj, { d: 4 });
console.log("Assigned:", target);

// Object.freeze - make immutable
const frozen = Object.freeze({ x: 1, y: 2 });
// frozen.x = 10; // Silently fails (or throws in strict mode)

// Object.fromEntries - create object from entries
const entries = [["name", "Alice"], ["age", 30]];
const fromEntries = Object.fromEntries(entries);
console.log("From entries:", fromEntries);

// ============================================
// Computed Property Keys
// ============================================

const prefix = "user";
const dynamicKey = `${prefix}Name`;
const obj2 = {
    [dynamicKey]: "Alice",
    [`get${dynamicKey.charAt(0).toUpperCase() + dynamicKey.slice(1)}`]() {
        return this[dynamicKey];
    }
};
console.log("Dynamic:", obj2[dynamicKey]);

// ============================================
// Optional Chaining
// ============================================

const data = {
    user: {
        profile: {
            name: "Alice"
        }
    }
};

console.log(data?.user?.profile?.name); // "Alice"
console.log(data?.user?.address?.city); // undefined (no error)
