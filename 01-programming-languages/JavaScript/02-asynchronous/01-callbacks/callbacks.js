/*
 * JavaScript Asynchronous: Callbacks
 * Callback pattern, callback hell
 */

// ============================================
// What is a Callback?
// ============================================

// A callback is a function passed as an argument to another function

function greet(name, callback) {
    const greeting = `Hello, ${name}!`;
    callback(greeting);
}

greet("Alice", (message) => {
    console.log(message); // "Hello, Alice!"
});

// ============================================
// Synchronous Callbacks
// ============================================

const numbers = [1, 2, 3, 4, 5];

// forEach uses a synchronous callback
numbers.forEach((num, index) => {
    console.log(`Index ${index}: ${num}`);
});

// ============================================
// Asynchronous Callbacks
// ============================================

// setTimeout with callback
console.log("Start");
setTimeout(() => {
    console.log("Inside setTimeout (after 1 second)");
}, 1000);
console.log("End");
// Output: Start, End, Inside setTimeout

// ============================================
// Callback Hell (Pyramid of Doom)
// ============================================

function getUser(userId, callback) {
    setTimeout(() => {
        callback({ id: userId, name: "Alice" });
    }, 100);
}

function getOrders(user, callback) {
    setTimeout(() => {
        callback([
            { id: 1, item: "Book" },
            { id: 2, item: "Pen" }
        ]);
    }, 100);
}

function getOrderDetails(order, callback) {
    setTimeout(() => {
        callback({ ...order, price: 29.99 });
    }, 100);
}

// Callback hell example
getUser(1, (user) => {
    console.log("User:", user.name);
    getOrders(user, (orders) => {
        console.log("Orders:", orders.length);
        getOrderDetails(orders[0], (details) => {
            console.log("Details:", details);
            // Deeply nested callbacks become hard to read and maintain
        });
    });
});

// ============================================
// Error-First Callbacks (Node.js Convention)
// ============================================

function fetchData(callback) {
    setTimeout(() => {
        const success = Math.random() > 0.3;
        if (success) {
            callback(null, { data: "success" });
        } else {
            callback(new Error("Network error"));
        }
    }, 100);
}

fetchData((err, result) => {
    if (err) {
        console.error("Error:", err.message);
        return;
    }
    console.log("Result:", result);
});
