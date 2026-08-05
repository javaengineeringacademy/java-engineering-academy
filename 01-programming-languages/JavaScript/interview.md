# JavaScript Interview Questions

## Fundamentals

**1. What is the difference between `==` and `===`?**
`==` performs type coercion before comparison. `===` compares both value and type without coercion. Always prefer `===` for predictable behavior.

**2. Explain closures and give a practical example.**
A closure is a function that retains access to its lexical scope even after the outer function has returned. Used for data privacy, function factories, and maintaining state.

**3. What is the event loop and how does it work?**
The event loop is a mechanism that allows asynchronous operations. It processes the call stack, then microtasks (Promises), then one macrotask (setTimeout, I/O). This enables non-blocking execution.

**4. What is hoisting?**
Hoisting moves variable and function declarations to the top of their scope during compilation. `var` declarations are hoisted and initialized with `undefined`. `let`/`const` are hoisted but not initialized (temporal dead zone).

**5. Explain the difference between `let`, `const`, and `var`.**
`var` is function-scoped and hoisted. `let` is block-scoped and not hoisted. `const` is block-scoped, not hoisted, and cannot be reassigned (but objects can be mutated).

## Functions and Scope

**6. What is the difference between function declarations and expressions?**
Declarations are hoisted and can be called before definition. Expressions are not hoisted and must be defined before use. Arrow functions are expressions with lexical `this`.

**7. Explain `this` keyword behavior.**
`this` depends on how a function is called:
- Global: `window` or `undefined` (strict mode)
- Object method: the object
- Constructor: new instance
- Arrow function: enclosing lexical scope
- Explicit: `call`, `apply`, `bind`

**8. What are Promises and how do they differ from callbacks?**
Promises represent eventual completion or failure. They avoid callback hell, support chaining, and have built-in error handling. `Promise.all`, `Promise.race`, `Promise.allSettled` handle multiple promises.

**9. Explain async/await and its advantages.**
`async/await` is syntactic sugar over Promises. Makes asynchronous code look synchronous, easier to read and debug. `await` pauses execution until Promise resolves. Must be used inside `async` function.

**10. What is the difference between `apply`, `call`, and `bind`?**
`call` invokes function with given `this` and individual arguments. `apply` takes arguments as array. `bind` returns new function with bound `this`, doesn't invoke immediately.

## Objects and Prototypes

**11. What is prototypal inheritance?**
Objects inherit from other objects through the prototype chain. Each object has a `[[Prototype]]` link. When property lookup fails on an object, JavaScript checks its prototype. `Object.create()` sets prototype explicitly.

**12. Explain the difference between `for...in` and `for...of`.**
`for...in` iterates over object enumerable property keys. `for...of` iterates over iterable values (arrays, strings, Maps, Sets). Use `Object.keys()` with `for...in` to avoid prototype properties.

**13. What are Symbols and when would you use them?**
Symbols are unique, immutable primitives used as object keys. Useful for avoiding property name collisions, implementing private properties, or defining well-known symbols like `Symbol.iterator`.

**14. What is destructuring and what are its use cases?**
Destructuring extracts values from arrays/objects into variables. Use cases: extracting API response fields, function parameters, swapping variables, default values, nested extraction.

## Advanced Concepts

**15. Explain the difference between deep and shallow copy.**
Shallow copy copies only top-level properties (arrays, objects are references). Deep copy recursively copies all nested values. Use `structuredClone()` or manual recursion for deep copies.

**16. What are WeakMap and WeakSet?**
WeakMap keys must be objects and are weakly held (garbage collected if no other references). WeakSet holds objects weakly. Both are not enumerable. Used for caching, private data, and avoiding memory leaks.

**17. What is the difference between `map`, `filter`, and `reduce`?**
`map` transforms each element. `filter` selects elements matching condition. `reduce` accumulates elements into single value. All return new arrays (reduce returns accumulator). All accept callback functions.

**18. Explain event delegation and when to use it.**
Event delegation attaches single listener to parent, uses event bubbling to handle events on children. Useful for dynamic content, reduces memory usage, simplifies maintenance. Check `event.target` for actual element.

## Performance and Security

**19. How do you optimize JavaScript performance?**
- Minimize DOM manipulation
- Use event delegation
- Debounce/throttle event handlers
- Lazy load images and components
- Use Web Workers for CPU-intensive tasks
- Minimize reflows and repaints
- Use requestAnimationFrame for animations

**20. What are common JavaScript security vulnerabilities?**
- XSS: Inject malicious scripts, prevent with sanitization and CSP
- CSRF: Cross-site request forgery, prevent with tokens
- Injection: Code injection through eval, prevent with input validation
- Insecure dependencies: Use npm audit, keep dependencies updated
