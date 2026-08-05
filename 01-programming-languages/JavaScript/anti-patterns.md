# JavaScript Anti-Patterns

## 1. Using var Instead of let/const
**Description:** Using `var` for variable declarations.

**Why it's bad:** Function-scoped instead of block-scoped, can lead to unexpected hoisting behavior.

**Example (bad code):**
```javascript
var x = 10;
if (true) {
    var x = 20; // same variable!
}
console.log(x); // 20
```

**Better approach:** Use let for reassignable, const for constants:
```javascript
const x = 10;
if (true) {
    let y = 20; // block-scoped
}
```

**Impact:** Predictable scoping, fewer bugs from hoisting.

---

## 2. Using == Instead of ===
**Description:** Using loose equality operator that performs type coercion.

**Why it's bad:** Unexpected type conversions, confusing results.

**Example (bad code):**
```javascript
0 == ''      // true
null == undefined  // true
'0' == 0     // true
```

**Better approach:** Always use strict equality:
```javascript
0 === ''      // false
null === undefined  // false
'0' === 0     // false
```

**Impact:** Predictable comparisons, fewer type-related bugs.

---

## 3. Callback Hell
**Description:** Deeply nested callbacks making code hard to read and maintain.

**Why it's bad:** Creates pyramid of doom, error handling becomes complex, hard to reason about.

**Example (bad code):**
```javascript
getData(function(a) {
    getMoreData(a, function(b) {
        getEvenMoreData(b, function(c) {
            console.log(c);
        });
    });
});
```

**Better approach:** Use Promises or async/await:
```javascript
async function fetchData() {
    const a = await getData();
    const b = await getMoreData(a);
    const c = await getEvenMoreData(b);
    console.log(c);
}
```

**Impact:** Flat code structure, easier error handling, better readability.

---

## 4. Memory Leaks from Event Listeners
**Description:** Not removing event listeners when elements are removed.

**Why it's bad:** Causes memory leaks, especially in single-page applications.

**Example (bad code):**
```javascript
function setup() {
    const button = document.getElementById('btn');
    button.addEventListener('click', function() {
        // handler
    });
    // element removed later, listener still attached
}
```

**Better approach:** Remove listeners when done:
```javascript
function setup() {
    const button = document.getElementById('btn');
    const handler = () => { /* ... */ };
    button.addEventListener('click', handler);
    return () => button.removeEventListener('click', handler);
}
```

**Impact:** Prevents memory leaks, proper cleanup.

---

## 5. Not Using Strict Mode
**Description:** Not enabling `'use strict'` or ES6 modules.

**Why it's bad:** Allows silent errors, makes debugging harder, permits unsafe actions.

**Example (bad code):**
```javascript
function test() {
    x = 10; // global variable created silently
}
```

**Better approach:** Use strict mode or modules:
```javascript
'use strict';
function test() {
    x = 10; // ReferenceError
}
```

**Impact:** Catches errors early, prevents unsafe actions.

---

## 6. Polluting Global Scope
**Description:** Defining variables and functions in global scope.

**Why it's bad:** Name collisions, hard to track dependencies, conflicts with libraries.

**Example (bad code):**
```javascript
function helper() { /* ... */ }
var config = {};
```

**Better approach:** Use modules or IIFE:
```javascript
// module.js
export function helper() { /* ... */ }
export const config = {};
```

**Impact:** Clean namespace, explicit dependencies, no conflicts.

---

## 7. Using for...in on Arrays
**Description:** Using for...in loop to iterate over arrays.

**Why it's bad:** Iterates over enumerable properties, not just array indices.

**Example (bad code):**
```javascript
Array.prototype.extra = 'bad';
const arr = [1, 2, 3];
for (const key in arr) {
    console.log(key); // '0', '1', '2', 'extra'
}
```

**Better approach:** Use for...of or array methods:
```javascript
for (const item of arr) {
    console.log(item);
}
```

**Impact:** Correct iteration, no prototype pollution issues.

---

## 8. Not Handling Promise Rejections
**Description:** Creating Promises without .catch() or async functions without try/catch.

**Why it's bad:** Unhandled rejections can crash Node.js applications or cause silent failures.

**Example (bad code):**
```javascript
fetchData().then(data => {
    // no .catch()
});
```

**Better approach:** Always handle rejections:
```javascript
fetchData()
    .then(data => { /* ... */ })
    .catch(error => console.error(error));
// or
try {
    const data = await fetchData();
} catch (error) {
    console.error(error);
}
```

**Impact:** Prevents unhandled rejections, better error handling.

---

## 9. Synchronous XHR
**Description:** Using XMLHttpRequest synchronously.

**Why it's bad:** Blocks the main thread, freezes the UI, poor user experience.

**Example (bad code):**
```javascript
const xhr = new XMLHttpRequest();
xhr.open('GET', '/api/data', false); // synchronous
xhr.send();
```

**Better approach:** Use async requests:
```javascript
const xhr = new XMLHttpRequest();
xhr.open('GET', '/api/data', true); // asynchronous
xhr.onload = () => console.log(xhr.responseText);
xhr.send();
```

**Impact:** Non-blocking UI, better user experience.

---

## 10. String Concatenation for HTML
**Description:** Building HTML strings with concatenation.

**Why it's bad:** XSS vulnerabilities, hard to maintain, error-prone.

**Example (bad code):**
```javascript
const html = '<div>' + userInput + '</div>';
element.innerHTML = html;
```

**Better approach:** Use template literals and textContent, or a framework:
```javascript
element.textContent = userInput; // safe
// or
const div = document.createElement('div');
div.textContent = userInput;
```

**Impact:** Prevents XSS, safer DOM manipulation.

---

## 11. Not Using Array Methods
**Description:** Using manual loops instead of map, filter, reduce.

**Why it's bad:** More verbose, less readable, more error-prone.

**Example (bad code):**
```javascript
const doubled = [];
for (let i = 0; i < numbers.length; i++) {
    doubled.push(numbers[i] * 2);
}
```

**Better approach:** Use array methods:
```javascript
const doubled = numbers.map(n => n * 2);
```

**Impact:** More concise, declarative, less error-prone.

---

## 12. Blocking the Event Loop
**Description:** Performing CPU-intensive operations on the main thread.

**Why it's bad:** Freezes the UI, makes application unresponsive.

**Example (bad code):**
```javascript
function heavyComputation() {
    for (let i = 0; i < 1e9; i++) {
        // blocks main thread
    }
}
```

**Better approach:** Use Web Workers or break into chunks:
```javascript
// Web Worker
self.onmessage = (e) => {
    const result = heavyComputation(e.data);
    postMessage(result);
};
```

**Impact:** Responsive UI, better user experience.