/*
 * JavaScript Asynchronous: async/await
 * async functions, await, try/catch
 */

// ============================================
// Basic async Function
// ============================================

async function greet() {
    return "Hello, World!";
}

// Returns a Promise
greet().then(message => console.log(message));

// ============================================
// await Keyword
// ============================================

function fetchUser(id) {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({ id, name: "Alice", email: "alice@example.com" });
        }, 100);
    });
}

async function getUser() {
    const user = await fetchUser(1);
    console.log("User:", user.name);
    return user;
}

getUser();

// ============================================
// Sequential Async Operations
// ============================================

function fetchOrders(userId) {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve([
                { id: 1, item: "Book", price: 29.99 },
                { id: 2, item: "Pen", price: 4.99 }
            ]);
        }, 100);
    });
}

function fetchOrderDetails(orderId) {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({ id: orderId, status: "shipped", tracking: "123456" });
        }, 100);
    });
}

async function processOrder(userId) {
    console.log("Fetching user...");
    const user = await fetchUser(userId);

    console.log("Fetching orders...");
    const orders = await fetchOrders(user.id);

    console.log("Fetching details...");
    const details = await fetchOrderDetails(orders[0].id);

    return { user, orders, details };
}

processOrder(1).then(result => {
    console.log("Processed:", result.user.name, "has", result.orders.length, "orders");
});

// ============================================
// Parallel Operations
// ============================================

async function getMultipleUsers(ids) {
    // Fetch all users in parallel
    const promises = ids.map(id => fetchUser(id));
    const users = await Promise.all(promises);
    return users;
}

getMultipleUsers([1, 2, 3]).then(users => {
    console.log("Users:", users.map(u => u.name));
});

// ============================================
// Error Handling with try/catch
// ============================================

async function riskyOperation() {
    try {
        const user = await fetchUser(1);
        if (!user.email) {
            throw new Error("No email found");
        }
        console.log("Email:", user.email);
        return user;
    } catch (error) {
        console.error("Error in riskyOperation:", error.message);
        throw error; // Re-throw if needed
    } finally {
        console.log("Cleanup code here");
    }
}

riskyOperation().catch(err => console.log("Caught:", err.message));

// ============================================
// Common Patterns
// ============================================

// Sequential vs Parallel
async function sequential() {
    const a = await fetchUser(1);  // Wait for first
    const b = await fetchUser(2);  // Then fetch second
    return [a, b];
}

async function parallel() {
    const [a, b] = await Promise.all([
        fetchUser(1),  // Fetch both simultaneously
        fetchUser(2)
    ]);
    return [a, b];
}

// Timeout pattern
function withTimeout(promise, ms) {
    const timeout = new Promise((_, reject) => {
        setTimeout(() => reject(new Error("Timeout")), ms);
    });
    return Promise.race([promise, timeout]);
}

withTimeout(fetchUser(1), 500)
    .then(user => console.log("User:", user.name))
    .catch(err => console.error(err.message));
