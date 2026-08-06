# JavaScript Promises

Understanding Promises and asynchronous operations in JavaScript.

## Topics Covered

- Creating Promises
- Promise chaining
- Error handling
- Promise.all, Promise.race, Promise.any

## Creating a Promise

```javascript
const promise = new Promise((resolve, reject) => {
    const success = true;
    if (success) {
        resolve("Done");
    } else {
        reject(new Error("Failed"));
    }
});
```

## Chaining Promises

```javascript
fetchUser(id)
    .then(user => fetchOrders(user))
    .then(orders => fetchDetails(orders[0]))
    .then(details => console.log(details))
    .catch(error => console.error(error));
```

## Promise Methods

| Method | Description |
|--------|-------------|
| `Promise.all()` | All must fulfill; rejects if any rejects |
| `Promise.allSettled()` | Waits for all; never rejects |
| `Promise.race()` | First to settle wins |
| `Promise.any()` | First to fulfill wins |

## Running the Example

```bash
node 02-asynchronous/02-promises/promises.js
```
