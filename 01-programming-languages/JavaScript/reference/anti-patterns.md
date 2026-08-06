# JavaScript Anti-Patterns

Common mistakes and anti-patterns to avoid in JavaScript.

## 1. Global Variables

```javascript
// BAD: Global pollution
var count = 0;
function increment() {
    count++;
}

// GOOD: Use modules or closures
let count = 0;
function increment() {
    count++;
}
```

## 2. Using var Instead of let/const

```javascript
// BAD: var has function scoping
var x = 10;
if (true) {
    var x = 20; // Same variable!
}

// GOOD: Block scoping
let x = 10;
if (true) {
    let x = 20; // Different variable
}
```

## 3. == Instead of ===

```javascript
// BAD: Type coercion
0 == ""      // true
null == undefined // true

// GOOD: Strict equality
0 === ""      // false
null === undefined // false
```

## 4. Callback Hell

```javascript
// BAD: Deeply nested callbacks
getUser(id, (user) => {
    getOrders(user, (orders) => {
        getDetails(orders[0], (details) => {
            // Hard to read and maintain
        });
    });
});

// GOOD: Use async/await
const user = await getUser(id);
const orders = await getOrders(user);
const details = await getDetails(orders[0]);
```

## 5. Memory Leaks

```javascript
// BAD: Not cleaning up
function setup() {
    const element = document.querySelector(".btn");
    element.addEventListener("click", handleClick);
}

// GOOD: Clean up event listeners
function setup() {
    const element = document.querySelector(".btn");
    element.addEventListener("click", handleClick);
    return () => {
        element.removeEventListener("click", handleClick);
    };
}
```

## 6. Using innerHTML with User Input

```javascript
// BAD: XSS vulnerability
element.innerHTML = userInput;

// GOOD: Use textContent
element.textContent = userInput;
```

## Quick Reference

| Anti-Pattern | Better Alternative |
|--------------|-------------------|
| Global variables | Module pattern |
| var | let/const |
| == | === |
| Callback hell | async/await |
| Memory leaks | Cleanup functions |
| innerHTML | textContent |
| Deep nesting | Early returns |
