# JavaScript Operators

Understanding arithmetic, comparison, logical, and nullish operators in JavaScript.

## Topics Covered

- Arithmetic operators (+, -, *, /, %, **)
- Comparison operators (==, ===, !=, !==, >, <)
- Logical operators (&&, ||, !)
- Nullish operators (??, ??=, ?.)
- Operator precedence

## Arithmetic Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `+` | Addition | `5 + 3 = 8` |
| `-` | Subtraction | `5 - 3 = 2` |
| `*` | Multiplication | `5 * 3 = 15` |
| `/` | Division | `6 / 3 = 2` |
| `%` | Modulo (remainder) | `5 % 3 = 2` |
| `**` | Exponentiation | `2 ** 3 = 8` |

## Equality Comparison

```javascript
// Loose equality (==) - type coercion
5 == "5"      // true
0 == false    // true

// Strict equality (===) - no coercion
5 === "5"     // false
0 === false   // false
```

**Best Practice**: Always use `===` and `!==` to avoid unexpected type coercion.

## Nullish Coalescing

```javascript
null ?? "default"      // "default"
undefined ?? "default" // "default"
0 ?? "default"         // 0 (not nullish)
"" ?? "default"        // "" (not nullish)
```

## Optional Chaining

```javascript
const user = { name: "Alice", address: { city: "NYC" } };
user?.address?.city  // "NYC"
user?.phone          // undefined (no error thrown)
```

## Running the Example

```bash
node 01-fundamentals/02-operators/operators.js
```
