# ES Modules

Modern JavaScript module system with import/export syntax.

## Topics Covered

- Named exports and imports
- Default exports and imports
- Re-exports
- Dynamic imports

## Named Exports

```javascript
// math.js
export const PI = 3.14159;
export function add(a, b) { return a + b; }

// main.js
import { PI, add } from "./math.js";
import { add as sum } from "./math.js";
import * as Math from "./math.js";
```

## Default Exports

```javascript
// calculator.js
export default class Calculator { }

// main.js
import Calculator from "./calculator.js";
```

## Import/Export Summary

| Type | Export Syntax | Import Syntax |
|------|---------------|---------------|
| Named | `export const x = 1` | `import { x } from "./mod.js"` |
| Default | `export default x` | `import x from "./mod.js"` |
| All | - | `import * as mod from "./mod.js"` |
| Dynamic | - | `const m = await import("./mod.js")` |

## Dynamic Imports

```javascript
const module = await import("./module.js");
module.default();
```

## Running the Example

```bash
# In Node.js, add "type": "module" to package.json
node --experimental-modules 04-modules/01-es-modules/import-export.js
```
