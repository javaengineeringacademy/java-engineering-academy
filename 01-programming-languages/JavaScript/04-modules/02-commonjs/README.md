# CommonJS Modules

Node.js module system with require and module.exports.

## Topics Covered

- module.exports and exports
- require syntax
- Module caching
- Built-in modules
- __dirname and __filename

## Exporting

```javascript
// math.js
const add = (a, b) => a + b;

// Export single value
module.exports = add;

// Export multiple values
module.exports = {
    add,
    subtract: (a, b) => a - b
};

// Alternative: exports object
exports.add = add;
exports.subtract = (a, b) => a - b;
```

## Importing

```javascript
// main.js
const add = require("./math.js");
const { add, subtract } = require("./math.js");
```

## Module Caching

```javascript
// Same object returned on multiple requires
const mod1 = require("./module.js");
const mod2 = require("./module.js");
// mod1 === mod2
```

## Running the Example

```bash
node 04-modules/02-commonjs/require-module.js
```
