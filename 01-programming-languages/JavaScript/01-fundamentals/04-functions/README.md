# JavaScript Functions

Understanding different function types and patterns in JavaScript.

## Topics Covered

- Function declarations and expressions
- Arrow functions
- Default and rest parameters
- IIFE (Immediately Invoked Function Expression)
- Closures and higher-order functions

## Function Types

| Type | Syntax | Hoisted | `this` Binding |
|------|--------|---------|----------------|
| Declaration | `function foo() {}` | Yes | Dynamic |
| Expression | `const foo = function() {}` | No | Dynamic |
| Arrow | `const foo = () => {}` | No | Lexical |

## Arrow Functions

```javascript
// Concise body (implicit return)
const add = (a, b) => a + b;

// Block body (explicit return)
const multiply = (a, b) => {
    return a * b;
};
```

## Closures

A closure is a function that retains access to its outer scope:

```javascript
function createCounter() {
    let count = 0;
    return {
        increment: () => ++count,
        getCount: () => count
    };
}
```

## Running the Example

```bash
node 01-fundamentals/04-functions/functions.js
```
