# JavaScript Architecture

## V8 Engine

V8 is Google's high-performance JavaScript and WebAssembly engine.

Key components:

- **Parser**: Converts source code to Abstract Syntax Tree (AST)
- **Ignition**: Interpreter that generates and executes bytecode
- **Sparkplug**: Non-optimizing compiler for quick execution
- **Maglev**: Mid-tier optimizing compiler
- **TurboFan**: Optimizing compiler using TurboFan JIT
- **Orinoco**: Garbage collector with concurrent and incremental phases

V8 optimizations:

- Hidden class transitions for property access
- Inline caching for repeated lookups
- Monomorphic call sites
- Escape analysis for stack allocation
- Dead code elimination
- Function inlining

## Event Loop

JavaScript uses a single-threaded event loop with non-blocking I/O.

```
Call Stack -> Web APIs -> Task Queue -> Microtask Queue -> Call Stack
```

The event loop continuously checks:

1. Execute all synchronous code on call stack
2. Process all microtasks (Promises, queueMicrotask)
3. Process one macrotask (setTimeout, I/O, UI rendering)
4. Repeat

## Call Stack

The call stack tracks function execution:

- Last In, First Out (LIFO) data structure
- Each function call adds a frame
- Frame contains parameters, local variables, return address
- Stack overflow occurs with too many nested calls
- Maximum size varies by engine (typically 10-25K frames)

## Task Queue

Macrotasks represent discrete units of work:

- `setTimeout` and `setInterval` callbacks
- I/O operations (file, network)
- UI rendering events
- `requestAnimationFrame` callbacks
- Message events from Web Workers

Each task runs to completion before next task starts.

## Microtask Queue

Microtasks have higher priority than macrotasks:

- Promise `.then()`, `.catch()`, `.finally()` callbacks
- `queueMicrotask()` callbacks
- `MutationObserver` callbacks
- Process all microtasks before next macrotask
- Enables Promise chaining without race conditions

## Web Workers

Workers provide true parallelism:

- Separate thread with own event loop
- Communication via `postMessage` and `SharedArrayBuffer`
- Cannot access DOM directly
- Types: Dedicated workers, Shared workers, Service workers
- Use `importScripts()` for dependencies
- Terminate with `terminate()` method

## Module System

```javascript
// ES Modules
import { func } from './module.js';
export const value = 42;

// CommonJS (Node.js)
const module = require('./module.js');
module.exports = { value: 42 };
```

## Memory Management

V8 garbage collector strategies:

- **Scavenger**: Handles young generation (short-lived objects)
- **Mark-Sweep-Compact**: Handles old generation
- **Concurrent marking**: Marks objects without pausing
- **Incremental marking**: Breaks marking into small chunks
- **Idle-time collection**: GC during idle periods
