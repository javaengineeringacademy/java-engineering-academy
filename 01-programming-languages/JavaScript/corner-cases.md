# JavaScript Corner Cases

## Type Coercion

JavaScript performs implicit type coercion in many contexts. The `==` operator performs type coercion before comparison, while `===` does not. `"1" == 1` is true, but `"1" === 1` is false.

Arithmetic operators coerce strings to numbers. `"5" - 1` is `4`, but `"5" + 1` is `"51"` because `+` also does string concatenation.

```javascript
[] + []    // "" (empty string)
[] + {}    // "[object Object]"
{} + []    // 0 (in some contexts)
```

The `+` operator prefers string concatenation if either operand is a string. The unary `+` operator coerces to a number: `+""` is `0`, `+true` is `1`.

## Hoisting

`var` declarations are hoisted to the top of their scope and initialized to `undefined`. `let` and `const` are hoisted but not initialized, creating a temporal dead zone. Accessing them before declaration throws `ReferenceError`.

Function declarations are fully hoisted (both name and body). Function expressions and arrow functions are not hoisted by name.

```javascript
console.log(x); // undefined (var hoisted)
var x = 5;

console.log(y); // ReferenceError (temporal dead zone)
let y = 5;
```

## `this` Binding

`this` is determined by how a function is called, not where it is defined. In a regular function call, `this` is `undefined` in strict mode or the global object otherwise. In a method call, `this` is the receiver object.

Arrow functions do not have their own `this`. They inherit `this` from the enclosing lexical scope. They cannot be used as constructors.

```javascript
const obj = {
    name: "Alice",
    greet: function() {
        setTimeout(function() {
            console.log(this.name); // undefined (global this)
        }, 100);
    }
};
```

Use arrow functions in callbacks to preserve `this`, or use `.bind()`.

## Prototype Chain

Property lookup walks up the prototype chain. If a property is not found on the object, JavaScript checks the prototype, then the prototype's prototype, until `Object.prototype`.

Modifying `Object.prototype` affects all objects. This is considered a bad practice.

`hasOwnProperty` can be called on any object, but if the object has a `hasOwnProperty` property, it may fail. Use `Object.prototype.hasOwnProperty.call(obj, key)` for safety.

## Closure Scoping

Closures capture variables by reference, not by value. In loops, all closures share the same variable. By the time the closure executes, the variable holds its final value.

```javascript
for (var i = 0; i < 5; i++) {
    setTimeout(() => console.log(i), 100);
}
// prints 5, 5, 5, 5, 5
```

Use `let` instead of `var` to create a new binding per iteration, or use an IIFE to create a new scope.

## Array Quirks

`Array(5)` creates an array with length 5 but no elements. `Array.of(5)` creates `[5]`. The constructor behaves differently based on the number of arguments.

`[1, 2, 3].length` is `3`. Setting `.length` truncates the array. `[1, 2, 3].length = 1` results in `[1]`.

`splice` mutates the original array. `slice` returns a new array without mutation.

## NaN Behavior

`NaN` is not equal to itself: `NaN === NaN` is false. Use `Number.isNaN()` for checking. `isNaN()` coerces the argument to a number first, which can produce false positives: `isNaN("hello")` is true.

`NaN` is of type `number`. `typeof NaN` is `"number"`.

## Undefined vs Null

`undefined` means a variable has been declared but not assigned. `null` is an intentional absence of value. Both are falsy.

`null == undefined` is true, but `null === undefined` is false. Checking for either should use `== null`.

## Event Loop and Microtasks

Microtasks (Promises, `queueMicrotask`) run after the current task and before the next macrotask. This means microtasks can starve macrotasks if they keep adding more microtasks.

`setTimeout(fn, 0)` does not execute immediately. It queues the callback as a macrotask with a minimum delay (usually 4ms).

## JSON Serialization

`JSON.stringify` ignores functions, `undefined`, and `Symbol` values in objects. It converts `NaN`, `Infinity`, and `-Infinity` to `null`.

`JSON.parse` does not preserve `undefined` or functions. Round-tripping with `JSON.stringify` and `JSON.parse` loses information.

## Numeric Precision

`Number.MAX_SAFE_INTEGER` is `2^53 - 1`. Numbers beyond this cannot be represented exactly. `0.1 + 0.2` is not exactly `0.3` due to floating-point representation.

BigInt handles arbitrary precision integers but is not interchangeable with Number. Mixing BigInt and Number in arithmetic throws a `TypeError`.
