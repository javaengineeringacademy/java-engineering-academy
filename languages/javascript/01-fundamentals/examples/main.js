// JavaScript Fundamentals Example

// Variables
const name = "JavaScript";
let version = "ES2024";
console.log(`Language: ${name}, Version: ${version}`);

// Arrays
const numbers = [1, 2, 3, 4, 5];
numbers.push(6);
console.log("Numbers:", numbers);

// Objects
const languages = {
  javascript: "JavaScript",
  typescript: "TypeScript",
  python: "Python",
};
console.log("Languages:", languages);

// Classes
class Person {
  constructor(name, age) {
    this.name = name;
    this.age = age;
  }

  greet() {
    return `Hello, I'm ${this.name}!`;
  }
}

const p = new Person("Alice", 30);
console.log(p.greet());

// Arrow functions
const add = (a, b) => a + b;
console.log("Add:", add(5, 3));

// Destructuring
const [first, ...rest] = numbers;
console.log("First:", first, "Rest:", rest);

// Async/Await
async function fetchData() {
  return new Promise((resolve) => {
    setTimeout(() => resolve("Data loaded!"), 1000);
  });
}

fetchData().then(console.log);

// Spread operator
const arr1 = [1, 2, 3];
const arr2 = [...arr1, 4, 5];
console.log("Spread:", arr2);
