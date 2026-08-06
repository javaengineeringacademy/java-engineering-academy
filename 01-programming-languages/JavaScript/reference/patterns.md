# JavaScript Patterns

Common design patterns and patterns in JavaScript.

## Module Pattern

Encapsulate code and expose public API.

```javascript
const Calculator = (function() {
    let result = 0;

    function add(x) { result += x; }
    function subtract(x) { result -= x; }
    function getResult() { return result; }

    return { add, subtract, getResult };
})();

Calculator.add(5);
Calculator.subtract(2);
console.log(Calculator.getResult()); // 3
```

## Singleton Pattern

Ensure only one instance exists.

```javascript
class Database {
    static instance = null;

    static getInstance() {
        if (!Database.instance) {
            Database.instance = new Database();
        }
        return Database.instance;
    }

    query(sql) {
        return `Executing: ${sql}`;
    }
}

const db1 = Database.getInstance();
const db2 = Database.getInstance();
console.log(db1 === db2); // true
```

## Factory Pattern

Create objects without specifying exact class.

```javascript
function createUser(type) {
    const users = {
        admin: { role: "admin", permissions: ["read", "write", "delete"] },
        user: { role: "user", permissions: ["read"] },
        guest: { role: "guest", permissions: [] }
    };

    return {
        ...users[type],
        hasPermission(perm) {
            return this.permissions.includes(perm);
        }
    };
}

const admin = createUser("admin");
console.log(admin.hasPermission("delete")); // true
```

## Observer Pattern

Notify subscribers when state changes.

```javascript
class EventEmitter {
    constructor() {
        this.listeners = {};
    }

    on(event, callback) {
        if (!this.listeners[event]) {
            this.listeners[event] = [];
        }
        this.listeners[event].push(callback);
    }

    emit(event, data) {
        if (this.listeners[event]) {
            this.listeners[event].forEach(cb => cb(data));
        }
    }
}

const emitter = new EventEmitter();
emitter.on("data", (msg) => console.log("Received:", msg));
emitter.emit("data", "Hello World");
```

## Strategy Pattern

Define family of algorithms, make them interchangeable.

```javascript
const strategies = {
    add: (a, b) => a + b,
    subtract: (a, b) => a - b,
    multiply: (a, b) => a * b
};

function calculate(strategy, a, b) {
    return strategies[strategy](a, b);
}

console.log(calculate("add", 5, 3));      // 8
console.log(calculate("multiply", 5, 3)); // 15
```

## Quick Reference

| Pattern | Use Case |
|---------|----------|
| Module | Code organization, encapsulation |
| Singleton | Single instance (DB, config) |
| Factory | Object creation |
| Observer | Event handling, pub/sub |
| Strategy | Algorithm selection |
