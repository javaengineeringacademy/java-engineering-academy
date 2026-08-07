# Module 06: Type Hints - Exercises

## Learning Objectives
- Master Python type hints and annotations
- Use type hints for better code documentation
- Implement type checking with mypy
- Understand advanced type hint patterns

## Exercises

### Exercise 1: Basic Type Hints (⭐)
**File:** `type_hints.py`

Add type hints to functions that perform basic operations. Include hints for parameters and return types.

**Description:** Type hints improve code readability and help catch errors before runtime.

---

### Exercise 2: Complex Types (⭐⭐)
**File:** `type_hints.py`

Implement functions using complex type hints including List, Dict, Tuple, Optional, and Union.

**Description:** Python's typing module provides types for complex data structures.

---

### Exercise 3: Generic Types (⭐⭐⭐)
**File:** `type_hints.py`

Create generic classes using TypeVar and Generic to implement a type-safe container.

**Description:** Generics allow you to write reusable code that works with different types while maintaining type safety.

---

### Exercise 4: Protocol and Structural Subtyping (⭐⭐⭐)
**File:** `type_hints.py`

Implement Protocol classes to define structural subtyping without inheritance.

**Description:** Protocols define the expected structure of objects without requiring explicit inheritance.

---

### Exercise 5: Callable and Function Types (⭐⭐⭐⭐)
**File:** `type_hints.py`

Write functions that accept and return other functions with proper type hints.

**Description:** Callable types allow you to annotate function parameters and return types.

## Tips
- Start with simple type hints and gradually add complexity
- Use `Optional[X]` instead of `Union[X, None]`
- Type hints are not enforced at runtime - they're for documentation and static analysis
- Use mypy to check your type hints
- Don't over-complicate hints - readability matters

## Test Cases
Run `python type_hints.py` to verify your solutions.
Run `mypy type_hints.py` for static type checking.