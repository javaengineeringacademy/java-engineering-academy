# Module 07: Functional Programming - Exercises

## Learning Objectives
- Master lambda functions and anonymous functions
- Use map, filter, and reduce effectively
- Implement higher-order functions
- Understand closures and decorators
- Practice functional programming paradigms

## Exercises

### Exercise 1: Lambda Functions (⭐)
**File:** `functional.py`

Implement various lambda functions for common operations.

**Description:** Lambda functions are anonymous functions defined with the `lambda` keyword. They're useful for short, one-time operations.

---

### Exercise 2: Map and Filter (⭐⭐)
**File:** `functional.py`

Use map() and filter() to transform and filter data collections.

**Description:** `map()` applies a function to each element, `filter()` selects elements that pass a test.

---

### Exercise 3: Reduce and Accumulation (⭐⭐)
**File:** `functional.py`

Implement reduce operations to accumulate results from collections.

**Description:** `reduce()` applies a function cumulatively to reduce a collection to a single value.

---

### Exercise 4: Closures (⭐⭐⭐)
**File:** `functional.py`

Create functions that return other functions with captured state.

**Description:** A closure is a function that remembers the values from its enclosing scope even after the outer function has finished executing.

---

### Exercise 5: Decorators (⭐⭐⭐⭐)
**File:** `functional.py`

Implement various decorators for function enhancement.

**Description:** Decorators are a application of closures that modify or enhance functions.

## Tips
- Lambda functions are limited to single expressions
- Use `functools.reduce` for reduce operations
- Closures capture variables by reference, not value
- Decorators should preserve function metadata using `functools.wraps`
- Functional programming emphasizes immutability

## Test Cases
Run `python functional.py` to verify your solutions.