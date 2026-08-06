# JavaScript Best Practices

Guidelines for writing clean, maintainable JavaScript code.

## Code Style

### Use const and let

```javascript
// GOOD
const API_URL = "https://api.example.com";
let counter = 0;

// BAD
var globalVar = "avoid";
```

### Use Descriptive Names

```javascript
// GOOD
const userAge = 25;
const isLoggedIn = true;

// BAD
const x = 25;
const flag = true;
```

### Use Template Literals

```javascript
// GOOD
const message = `Hello, ${name}! You are ${age}.`;

// BAD
const message = "Hello, " + name + "! You are " + age + ".";
```

## Functions

### Keep Functions Small

```javascript
// GOOD: Single responsibility
function validateEmail(email) {
    return email.includes("@");
}

// BAD: Doing too much
function processUser(user) {
    // validation, database, email, logging...
}
```

### Use Arrow Functions for Callbacks

```javascript
// GOOD
const doubled = numbers.map(n => n * 2);
const evens = numbers.filter(n => n % 2 === 0);

// BAD
const doubled = numbers.map(function(n) {
    return n * 2;
});
```

## Error Handling

### Always Handle Errors

```javascript
// GOOD
async function fetchData() {
    try {
        const response = await fetch(url);
        return await response.json();
    } catch (error) {
        console.error("Failed to fetch:", error);
        throw error;
    }
}

// BAD: No error handling
async function fetchData() {
    const response = await fetch(url);
    return await response.json();
}
```

## Arrays

### Use Array Methods

```javascript
// GOOD
const evens = numbers.filter(n => n % 2 === 0);
const doubled = numbers.map(n => n * 2);
const sum = numbers.reduce((a, b) => a + b, 0);

// BAD: Manual loops
const evens = [];
for (let i = 0; i < numbers.length; i++) {
    if (numbers[i] % 2 === 0) {
        evens.push(numbers[i]);
    }
}
```

## Objects

### Use Destructuring

```javascript
// GOOD
const { name, age, email } = user;

// BAD
const name = user.name;
const age = user.age;
const email = user.email;
```

## Key Principles

1. **Keep it simple** - Write code that's easy to read
2. **DRY** - Don't Repeat Yourself
3. **Single Responsibility** - Each function does one thing
4. **Error Handling** - Always handle potential failures
5. **Immutability** - Avoid mutating data when possible
