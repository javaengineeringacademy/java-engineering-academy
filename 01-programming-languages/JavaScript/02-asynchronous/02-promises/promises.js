/*
 * JavaScript Asynchronous: Promises
 * Promise creation, chaining, error handling
 */

// ============================================
// Creating a Promise
// ============================================

const myPromise = new Promise((resolve, reject) => {
    const success = true;
    if (success) {
        resolve("Operation completed successfully");
    } else {
        reject(new Error("Operation failed"));
    }
});

myPromise
    .then(result => console.log(result))
    .catch(error => console.error(error));

// ============================================
// Promise Chaining
// ============================================

function fetchUser(id) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({ id, name: "Alice" });
        }, 100);
    });
}

function fetchOrders(user) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve([
                { id: 1, item: "Book" },
                { id: 2, item: "Pen" }
            ]);
        }, 100);
    });
}

function fetchDetails(order) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({ ...order, price: 29.99 });
        }, 100);
    });
}

// Clean promise chain
fetchUser(1)
    .then(user => {
        console.log("User:", user.name);
        return fetchOrders(user);
    })
    .then(orders => {
        console.log("Orders:", orders.length);
        return fetchDetails(orders[0]);
    })
    .then(details => {
        console.log("Details:", details);
    })
    .catch(error => {
        console.error("Error:", error.message);
    });

// ============================================
// Promise.all - Run in parallel
// ============================================

const promise1 = Promise.resolve(1);
const promise2 = Promise.resolve(2);
const promise3 = Promise.resolve(3);

Promise.all([promise1, promise2, promise3])
    .then(results => {
        console.log("All results:", results); // [1, 2, 3]
    })
    .catch(error => {
        console.error("One failed:", error);
    });

// ============================================
// Promise.allSettled - Wait for all (no rejection)
// ============================================

const p1 = Promise.resolve("success");
const p2 = Promise.reject("failed");
const p3 = Promise.resolve("done");

Promise.allSettled([p1, p2, p3])
    .then(results => {
        results.forEach(result => {
            if (result.status === "fulfilled") {
                console.log("Fulfilled:", result.value);
            } else {
                console.log("Rejected:", result.reason);
            }
        });
    });

// ============================================
// Promise.race - First to settle wins
// ============================================

const fast = new Promise(resolve => setTimeout(() => resolve("fast"), 100));
const slow = new Promise(resolve => setTimeout(() => resolve("slow"), 500));

Promise.race([fast, slow])
    .then(result => {
        console.log("Winner:", result); // "fast"
    });

// ============================================
// Promise.any - First to fulfill wins
// ============================================

const p11 = Promise.reject("error1");
const p22 = Promise.resolve("success");
const p33 = Promise.reject("error3");

Promise.any([p11, p22, p33])
    .then(result => {
        console.log("First success:", result); // "success"
    })
    .catch(errors => {
        console.error("All failed:", errors);
    });

// ============================================
// Error Handling
// ============================================

function riskyOperation() {
    return new Promise((resolve, reject) => {
        const random = Math.random();
        if (random > 0.5) {
            resolve("Success");
        } else {
            reject(new Error("Random failure"));
        }
    });
}

riskyOperation()
    .then(result => console.log(result))
    .catch(error => {
        console.error("Caught:", error.message);
        return "Recovered";
    })
    .then(result => console.log(result));
