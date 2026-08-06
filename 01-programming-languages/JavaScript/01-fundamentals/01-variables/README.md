# JavaScript Variables

Understanding variable declarations, scoping, and data types in JavaScript.

## Topics Covered

- `var`, `let`, and `const` declarations
- Block scoping vs function scoping
- Hoisting behavior
- Data types and `typeof` operator
- Type coercion

## Variable Declarations

| Keyword | Scope | Hoisted | Redeclarable | Reassignable |
|---------|-------|---------|--------------|--------------|
| `var` | Function | Yes | Yes | Yes |
| `let` | Block | No | No | Yes |
| `const` | Block | No | No | No |

## Scoping Rules

```javascript
// var is function-scoped
function example() {
    if (true) {
        var x = 10;
    }
    console.log(x); // 10 - accessible outside block
}

// let/const are block-scoped
function example2() {
    if (true) {
        let y = 10;
    }
    // console.log(y); // ReferenceError
}
```

## Hoisting

Variables declared with `var` are hoisted to the top of their function scope:

```javascript
console.log(a); // undefined (not ReferenceError)
var a = 5;

// let/const are in "Temporal Dead Zone" until declaration
// console.log(b); // ReferenceError
let b = 10;
```

## Data Types

JavaScript has 8 data types:

- **Primitive**: string, number, boolean, null, undefined, symbol, bigint
- **Non-primitive**: object (includes arrays, functions, dates)

## Running the Example

```bash
node 01-fundamentals/01-variables/variables.js
```
