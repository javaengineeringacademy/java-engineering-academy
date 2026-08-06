# JavaScript Objects

Working with objects, destructuring, and spread operator in JavaScript.

## Topics Covered

- Object creation methods
- Property access (dot and bracket notation)
- Destructuring assignment
- Spread and rest operators
- Object methods (keys, values, entries)

## Creating Objects

```javascript
// Object literal
const obj = { key: "value" };

// Constructor
function Person(name) { this.name = name; }
const p = new Person("Alice");

// Object.create
const proto = { greet() { return "Hi"; } };
const obj2 = Object.create(proto);
```

## Destructuring

```javascript
const { name, age, email = "N/A" } = person;

// Nested destructuring
const { address: { city, zip } } = user;

// Renaming
const { name: personName } = person;
```

## Spread Operator

```javascript
// Copy
const copy = { ...original };

// Merge
const merged = { ...defaults, ...overrides };

// Update
const updated = { ...person, age: 31 };
```

## Object Methods

| Method | Description |
|--------|-------------|
| `Object.keys(obj)` | Array of keys |
| `Object.values(obj)` | Array of values |
| `Object.entries(obj)` | Array of [key, value] pairs |
| `Object.assign(target, ...sources)` | Merge objects |
| `Object.freeze(obj)` | Make immutable |
| `Object.fromEntries(entries)` | Create from entries |

## Running the Example

```bash
node 01-fundamentals/06-objects/objects.js
```
