# JavaScript Strings

Working with strings, template literals, and string methods in JavaScript.

## Topics Covered

- String creation and template literals
- Searching and extracting substrings
- Transforming and formatting strings
- Splitting and joining

## Template Literals

```javascript
const name = "Alice";
const greeting = `Hello, ${name}!`;

// Multiline
const multi = `
Line 1
Line 2
`;

// Expressions
const result = `Total: $${price * quantity}`;
```

## Common Methods

| Method | Description |
|--------|-------------|
| `indexOf()` | Find first occurrence |
| `lastIndexOf()` | Find last occurrence |
| `includes()` | Check if contains |
| `startsWith()` | Check if starts with |
| `endsWith()` | Check if ends with |
| `slice()` | Extract substring |
| `substring()` | Extract substring |
| `replace()` | Replace first match |
| `replaceAll()` | Replace all matches |
| `split()` | Split into array |
| `trim()` | Remove whitespace |

## String Formatting

```javascript
"5".padStart(3, "0");   // "005"
"hello".toUpperCase();  // "HELLO"
"  hello  ".trim();     // "hello"
```

## Running the Example

```bash
node 01-fundamentals/07-strings/strings.js
```
