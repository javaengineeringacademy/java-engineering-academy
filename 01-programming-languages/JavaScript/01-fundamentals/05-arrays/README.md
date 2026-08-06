# JavaScript Arrays

Working with arrays and their methods in JavaScript.

## Topics Covered

- Creating arrays
- Adding and removing elements
- Iteration methods (map, filter, reduce, forEach)
- Searching (find, includes, indexOf)
- Sorting and slicing

## Creating Arrays

```javascript
const arr1 = [1, 2, 3];           // Array literal
const arr2 = new Array(5);        // Empty array of length 5
const arr3 = Array.from("Hello"); // From iterable
const arr4 = Array.of(1, 2, 3);  // From arguments
```

## Essential Methods

| Method | Description | Returns |
|--------|-------------|---------|
| `map()` | Transform each element | New array |
| `filter()` | Select matching elements | New array |
| `reduce()` | Accumulate to single value | Single value |
| `forEach()` | Execute function for each | undefined |
| `find()` | Find first matching element | Element or undefined |
| `some()` | Check if any element matches | Boolean |
| `every()` | Check if all elements match | Boolean |

## map/filter/reduce Pattern

```javascript
const numbers = [1, 2, 3, 4, 5];
const result = numbers
    .filter(n => n % 2 === 0)  // [2, 4]
    .map(n => n * 2)           // [4, 8]
    .reduce((sum, n) => sum + n, 0);  // 12
```

## Copying Arrays

```javascript
const original = [1, 2, 3];
const copy1 = [...original];        // Spread
const copy2 = original.slice();     // Slice
const copy3 = Array.from(original); // Array.from
```

## Running the Example

```bash
node 01-fundamentals/05-arrays/arrays.js
```
