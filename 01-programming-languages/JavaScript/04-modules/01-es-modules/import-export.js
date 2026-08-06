/*
 * ES Modules
 * import, export, default, named
 */

// ============================================
// Named Exports
// ============================================

// math.js - Named exports
export const PI = 3.14159;
export const E = 2.71828;

export function add(a, b) {
    return a + b;
}

export function subtract(a, b) {
    return a - b;
}

// Export at end (alternative syntax)
const multiply = (a, b) => a * b;
const divide = (a, b) => a / b;

export { multiply, divide };

// ============================================
// Default Exports
// ============================================

// calculator.js - Default export
export default class Calculator {
    constructor() {
        this.result = 0;
    }

    add(value) {
        this.result += value;
        return this;
    }

    subtract(value) {
        this.result -= value;
        return this;
    }

    getResult() {
        return this.result;
    }
}

// Only one default export per module

// ============================================
// Named Imports
// ============================================

// main.js - Importing named exports
// import { PI, E, add, subtract } from "./math.js";
// console.log(PI);
// console.log(add(2, 3));

// Rename imports
// import { add as sum, subtract as diff } from "./math.js";
// console.log(sum(2, 3));

// Import all named exports as namespace
// import * as MathUtils from "./math.js";
// console.log(MathUtils.PI);
// console.log(MathUtils.add(2, 3));

// ============================================
// Default Imports
// ============================================

// Import default export (name can be anything)
// import Calculator from "./calculator.js";
// const calc = new Calculator();
// calc.add(10).subtract(5);
// console.log(calc.getResult());

// ============================================
// Mixed Imports
// ============================================

// import Calculator, { PI, add } from "./module.js";

// ============================================
// Re-exports
// ============================================

// Re-export named exports from another module
// export { add, subtract } from "./math.js";

// Re-export default as named
// export { default as Calculator } from "./calculator.js";

// Re-export all
// export * from "./math.js";

// ============================================
// Dynamic Imports
// ============================================

// Load module on demand
async function loadModule() {
    const module = await import("./math.js");
    console.log(module.add(2, 3));
}

// Conditional import
async function getCalculator(useAdvanced) {
    if (useAdvanced) {
        const { default: Calc } = await import("./advanced-calculator.js");
        return new Calc();
    } else {
        const { default: Calc } = await import("./calculator.js");
        return new Calc();
    }
}
