# TypeScript Debugging

## Source Maps

Enable source maps for debugging TypeScript directly.

```json
// tsconfig.json
{
  "compilerOptions": {
    "sourceMap": true,
    "inlineSourceMap": false,
    "declarationMap": true
  }
}
```

## VS Code Debugging

### Launch Configuration

```json
// .vscode/launch.json
{
  "version": "0.2.0",
  "configurations": [
    {
      "name": "Debug Node.js",
      "type": "node",
      "request": "launch",
      "program": "${workspaceFolder}/src/index.ts",
      "preLaunchTask": "tsc: build",
      "outFiles": ["${workspaceFolder}/dist/**/*.js"],
      "sourceMaps": true,
      "console": "integratedTerminal",
      "env": {
        "NODE_ENV": "development"
      }
    },
    {
      "name": "Debug Jest",
      "type": "node",
      "request": "launch",
      "program": "${workspaceFolder}/node_modules/.bin/jest",
      "args": ["--runInBand"],
      "console": "integratedTerminal",
      "internalConsoleOptions": "neverOpen"
    }
  ]
}
```

### Breakpoints

- Click in the gutter to set breakpoints
- Use conditional breakpoints (right-click)
- Set logpoints for non-breaking logging
- Use `debugger` statement in code

## Node.js Debugging

```bash
# Enable inspector
node --inspect src/index.js

# Break on first line
node --inspect-brk src/index.js

# Debug with Chrome DevTools
node --inspect src/index.js
# Open chrome://inspect in Chrome
```

## Chrome DevTools

```typescript
// Trigger debugger from code
function complexFunction(data: User[]) {
  debugger;  // Pauses here
  return data.filter(u => u.active);
}
```

## Console Debugging

```typescript
// Basic logging
console.log('value:', variable);

// Table for arrays/objects
console.table([{ name: 'Alice', age: 30 }, { name: 'Bob', age: 25 }]);

// Grouping
console.group('Processing');
console.log('Step 1');
console.log('Step 2');
console.groupEnd();

// Timing
console.time('operation');
await heavyOperation();
console.timeEnd('operation');

// Stack trace
console.trace('Called from:');
```

## VS Code Extensions

| Extension | Purpose |
|-----------|---------|
| Debugger for Chrome | Chrome debugging |
| Node.js Debugger | Node.js debugging |
| Error Lens | Inline error display |
| TypeScript Importer | Auto imports |

## ts-node Debugging

```json
{
  "name": "Debug ts-node",
  "type": "node",
  "request": "launch",
  "runtimeExecutable": "${workspaceFolder}/node_modules/.bin/ts-node",
  "program": "${workspaceFolder}/src/index.ts",
  "args": ["${relativeFile}"],
  "console": "integratedTerminal"
}
```

## Vitest Debugging

```bash
# Debug single test
node --inspect node_modules/.bin/vitest run --reporter=verbose

# In VS Code
{
  "name": "Debug Vitest",
  "type": "node",
  "request": "launch",
  "program": "${workspaceFolder}/node_modules/vitest/dist/cli-wrapper.js",
  "args": ["run", "${relativeFile}"],
  "console": "integratedTerminal"
}
```

## Common Debug Patterns

```typescript
// Debug async code
async function fetchData() {
  try {
    const result = await apiCall();
    console.log('Result:', result);
    return result;
  } catch (error) {
    console.error('Error:', error);
    throw error;
  }
}

// Debug type issues
const value: unknown = getData();
console.log('Type:', typeof value);
console.log('Value:', JSON.stringify(value, null, 2));
```
