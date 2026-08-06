# JavaScript async/await

Modern asynchronous JavaScript with async functions and await.

## Topics Covered

- async functions
- await keyword
- Error handling with try/catch
- Parallel vs sequential operations

## Basic Syntax

```javascript
async function fetchData() {
    const response = await fetch("https://api.example.com/data");
    const data = await response.json();
    return data;
}
```

## Sequential vs Parallel

```javascript
// Sequential (slower)
async function sequential() {
    const a = await fetchUser(1);
    const b = await fetchUser(2);
    return [a, b];
}

// Parallel (faster)
async function parallel() {
    const [a, b] = await Promise.all([
        fetchUser(1),
        fetchUser(2)
    ]);
    return [a, b];
}
```

## Error Handling

```javascript
async function riskyOperation() {
    try {
        const result = await fetchData();
        return result;
    } catch (error) {
        console.error("Failed:", error.message);
        throw error;
    } finally {
        // Cleanup
    }
}
```

## Running the Example

```bash
node 02-asynchronous/03-async-await/async-await.js
```
