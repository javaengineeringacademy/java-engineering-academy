# Module 00: Knowledge Atoms - Exercises

## Learning Objectives
- Understand duck typing and its role in Python
- Master fundamental Python concepts
- Build a strong foundation for advanced topics

## Exercises

### Exercise 1: Duck Typing Basics (⭐)
**File:** `duck_typing.py`

Implement classes that demonstrate duck typing. Create different bird classes that implement a `fly()` method and a function that accepts any object with a `fly()` method.

**Description:** Duck typing means "if it walks like a duck and quacks like a duck, it's a duck." Python doesn't check types explicitly - it checks for the presence of methods and attributes.

---

### Exercise 2: Iterable Objects (⭐⭐)
**File:** `duck_typing.py`

Create a custom class that implements the iterator protocol (`__iter__` and `__next__`) without inheriting from any built-in iterator class.

**Description:** Any object with `__iter__` and `__next__` methods can be used in a for loop, demonstrating duck typing in action.

---

### Exercise 3: Callable Objects (⭐⭐)
**File:** `duck_typing.py`

Implement a class that makes instances callable like functions using the `__call__` method. Create a multiplier class that when called returns the product.

**Description:** If an object has a `__call__` method, it can be invoked like a function - another duck typing example.

---

### Exercise 4: Context Manager Protocol (⭐⭐⭐)
**File:** `duck_typing.py`

Create a custom context manager class that implements `__enter__` and `__exit__` methods without using `contextmanager` decorator.

**Description:** Any object implementing the context manager protocol can be used with `with` statement.

---

### Exercise 5: Advanced Duck Typing (⭐⭐⭐)
**File:** `duck_typing.py`

Implement a `quack_all()` function that accepts any iterable of objects and calls `quack()` on each, regardless of their actual type. Handle objects that don't have `quack()` gracefully.

**Description:** This demonstrates runtime type checking and duck typing combined.

## Tips
- Remember: Python cares about behavior, not type
- Use `hasattr()` to check for method existence when needed
- Duck typing promotes flexibility and code reusability
- Test your code with different object types to verify duck typing works

## Test Cases
Run `python duck_typing.py` to verify your solutions.