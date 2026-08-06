// JavaScript Object Destructuring

// Basic destructuring
const person = { name: "Alice", age: 30, city: "NYC" };
const { name, age } = person;
// name="Alice", age=30

// Renaming
const { name: userName, age: userAge } = person;
// userName="Alice", userAge=30

// Default values
const { name: n, role = "user" } = person;
// role="user" (not in object)

// Nested destructuring
const user = {
  id: 1,
  address: {
    street: "123 Main St",
    city: "Boston",
    state: "MA"
  }
};

const { address: { city, state } } = user;
// city="Boston", state="MA"

// Array destructuring
const [first, second, third] = [1, 2, 3];
const [a, , c] = [1, 2, 3]; // Skip: a=1, c=3

// Rest in arrays
const [head, ...tail] = [1, 2, 3, 4];
// head=1, tail=[2, 3, 4]

// Swap variables
let x = 1, y = 2;
[x, y] = [y, x]; // x=2, y=1

// Function parameter destructuring
function greet({ name, age = 0 } = {}) {
  return `Hello ${name}, age ${age}`;
}

// Nested function params
function processUser({ name, address: { city } }) {
  return `${name} from ${city}`;
}

// Destructuring in loops
const people = [
  { name: "Alice", age: 30 },
  { name: "Bob", age: 25 }
];

for (const { name, age } of people) {
  console.log(`${name}: ${age}`);
}

// Destructuring with computed properties
const prop = "name";
const { [prop]: value } = { name: "Charlie" };
// value="Charlie"

// Destructuring and default together
const settings = { theme: "dark" };
const { theme, lang = "en", fontSize = 16 } = settings;

// Extracting from nested arrays
const [[id, name2], [x2, y2]] = [[1, "Alice"], [10, 20]];

// Destructuring with regex
const match = "2024-01-15".match(/(\d{4})-(\d{2})-(\d{2})/);
if (match) {
  const [, year, month, day] = match;
}

// Object destructuring for imports
const module = {
  default: "main",
  helper1: () => {},
  helper2: () => {}
};

const { default: mainExport, helper1, helper2 } = module;

// Conditional destructuring
const data = { user: { name: "Alice" } };
const name3 = data?.user?.name ?? "Unknown";

// Destructuring with type checking
function printInfo({ name, age }) {
  if (typeof name !== "string") throw new Error("Invalid name");
  if (typeof age !== "number") throw new Error("Invalid age");
  return `${name}: ${age}`;
}
