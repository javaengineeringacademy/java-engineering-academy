# JavaScript Control Flow

Understanding conditionals and loops in JavaScript.

## Topics Covered

- if/else if/else statements
- Switch statements
- Ternary operator
- for, while, do...while loops
- for...of and for...in loops
- break and continue

## Conditionals

### if/else

```javascript
if (condition) {
    // code if true
} else if (otherCondition) {
    // code if other condition true
} else {
    // code if all false
}
```

### Switch

```javascript
switch (expression) {
    case value1:
        // code
        break;
    case value2:
        // code
        break;
    default:
        // default code
}
```

### Ternary

```javascript
const result = condition ? valueIfTrue : valueIfFalse;
```

## Loops

| Loop | Use Case |
|------|----------|
| `for` | Known number of iterations |
| `while` | Unknown iterations, pre-check |
| `do...while` | Unknown iterations, post-check |
| `for...of` | Iterating over values (arrays, strings) |
| `for...in` | Iterating over keys (object properties) |

## Array Iteration Methods

```javascript
const arr = [1, 2, 3, 4, 5];
arr.forEach(item => console.log(item));
const doubled = arr.map(item => item * 2);
const evens = arr.filter(item => item % 2 === 0);
const sum = arr.reduce((acc, item) => acc + item, 0);
```

## Running the Examples

```bash
node 01-fundamentals/03-control-flow/conditionals.js
node 01-fundamentals/03-control-flow/loops.js
```
