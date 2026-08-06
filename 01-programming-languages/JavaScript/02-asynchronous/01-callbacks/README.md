# JavaScript Callbacks

Understanding callback functions and asynchronous patterns in JavaScript.

## Topics Covered

- What callbacks are
- Synchronous vs asynchronous callbacks
- Callback hell (pyramid of doom)
- Error-first callback pattern

## What is a Callback?

A callback is a function passed as an argument to another function:

```javascript
function fetchData(callback) {
    const data = { name: "Alice" };
    callback(data);
}

fetchData((result) => {
    console.log(result);
});
```

## Synchronous vs Asynchronous

```javascript
// Synchronous - executes immediately
[1, 2, 3].forEach(num => console.log(num));

// Asynchronous - executes later
setTimeout(() => {
    console.log("After 1 second");
}, 1000);
```

## Error-First Pattern (Node.js)

```javascript
function readFile(path, callback) {
    callback(null, "content");
}

readFile("file.txt", (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    console.log(data);
});
```

## Running the Example

```bash
node 02-asynchronous/01-callbacks/callbacks.js
```
