/*
 * Jest Testing
 * Jest basics, expect, describe, it
 */

// ============================================
// Functions to Test
// ============================================

function add(a, b) {
    return a + b;
}

function multiply(a, b) {
    return a * b;
}

function divide(a, b) {
    if (b === 0) {
        throw new Error("Cannot divide by zero");
    }
    return a / b;
}

function isEven(num) {
    return num % 2 === 0;
}

function getFullName(first, last) {
    return `${first} ${last}`;
}

async function fetchUser(id) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({ id, name: "Test User" });
        }, 100);
    });
}

// ============================================
// Jest Test Examples
// ============================================

// describe - groups related tests
describe("Math Operations", () => {
    // it (or test) - individual test case
    it("should add two numbers correctly", () => {
        expect(add(2, 3)).toBe(5);
        expect(add(-1, 1)).toBe(0);
        expect(add(0, 0)).toBe(0);
    });

    it("should multiply two numbers correctly", () => {
        expect(multiply(2, 3)).toBe(6);
        expect(multiply(-2, 3)).toBe(-6);
        expect(multiply(0, 5)).toBe(0);
    });

    it("should divide two numbers correctly", () => {
        expect(divide(10, 2)).toBe(5);
        expect(divide(9, 3)).toBe(3);
        expect(divide(7, 2)).toBe(3.5);
    });

    it("should throw error when dividing by zero", () => {
        expect(() => divide(10, 0)).toThrow("Cannot divide by zero");
    });
});

// ============================================
// Matcher Examples
// ============================================

describe("Jest Matchers", () => {
    // Equality matchers
    it("should test equality", () => {
        expect(add(2, 3)).toBe(5);         // Strict equality
        expect(add(2, 3)).toEqual(5);      // Deep equality
        expect(add(2, 3)).not.toBe(6);     // Negation
    });

    // Truthiness
    it("should test truthiness", () => {
        expect(true).toBeTruthy();
        expect(false).toBeFalsy();
        expect(null).toBeNull();
        expect(undefined).toBeUndefined();
        expect("hello").toBeDefined();
    });

    // Numbers
    it("should test numbers", () => {
        expect(add(2, 3)).toBeGreaterThan(4);
        expect(add(2, 3)).toBeGreaterThanOrEqual(5);
        expect(add(2, 3)).toBeLessThan(10);
    });

    // Strings
    it("should test strings", () => {
        expect(getFullName("John", "Doe")).toMatch("John");
        expect(getFullName("John", "Doe")).toContain("Doe");
    });

    // Arrays
    it("should test arrays", () => {
        const arr = [1, 2, 3, 4, 5];
        expect(arr).toHaveLength(5);
        expect(arr).toContain(3);
    });

    // Objects
    it("should test objects", () => {
        const user = { name: "Alice", age: 30 };
        expect(user).toHaveProperty("name");
        expect(user).toHaveProperty("name", "Alice");
        expect(user).toEqual({ name: "Alice", age: 30 });
    });
});

// ============================================
// Async Tests
// ============================================

describe("Async Operations", () => {
    it("should fetch user asynchronously", async () => {
        const user = await fetchUser(1);
        expect(user).toBeDefined();
        expect(user.id).toBe(1);
        expect(user.name).toBe("Test User");
    });

    it("should handle async with promise", () => {
        return fetchUser(1).then(user => {
            expect(user.id).toBe(1);
        });
    });
});

// ============================================
// beforeEach and afterEach
// ============================================

describe("Setup and Teardown", () => {
    let items = [];

    // Runs before each test
    beforeEach(() => {
        items = [1, 2, 3, 4, 5];
    });

    // Runs after each test
    afterEach(() => {
        items = [];
    });

    it("should have 5 items", () => {
        expect(items).toHaveLength(5);
    });

    it("should filter even numbers", () => {
        const evens = items.filter(n => n % 2 === 0);
        expect(evens).toEqual([2, 4]);
    });
});

// ============================================
// Running Tests
// ============================================

// Run all tests
// npx jest

// Run specific file
// npx jest test-example.js

// Run with coverage
// npx jest --coverage

// Run in watch mode
// npx jest --watch
