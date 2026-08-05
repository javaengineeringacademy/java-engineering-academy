# Python Corner Cases

## Mutable Default Arguments

Default argument values are evaluated once at function definition time, not at each call. Mutable defaults like lists or dicts are shared across all calls. This causes unexpected state retention between calls.

```python
def append_to(item, target=[]):
    target.append(item)
    return target

append_to(1)  # [1]
append_to(2)  # [1, 2]  -- surprise!
```

Use `None` as the default and create the mutable object inside the function body.

## GIL and True Parallelism

The Global Interpreter Lock prevents multiple native threads from executing Python bytecodes simultaneously. CPU-bound threads do not achieve parallelism. Use `multiprocessing` for CPU-bound work or `threading` for I/O-bound work.

Asyncio and async/await provide concurrency for I/O-bound tasks but not parallelism. They run on a single thread.

## Late Binding Closures

Closures in Python capture variables by reference, not by value. In loops, the closure captures the loop variable, not its value at the time of creation. By the time the closure executes, the variable holds its final value.

```python
funcs = []
for i in range(5):
    funcs.append(lambda: i)

[f() for f in funcs]  # [4, 4, 4, 4, 4]
```

Fix by using a default argument: `lambda i=i: i`.

## List Comprehension Variable Leakage

In Python 2, list comprehension variables leak into the enclosing scope. In Python 3, they do not. This is a source of bugs when porting code between versions.

Generator expressions do not leak variables in any version.

## Dictionary Key Mutation

Dictionaries require keys to be hashable. Mutable objects like lists cannot be used as keys. If you modify an object after using it as a key, the hash changes and the dictionary cannot find the entry.

Tuples containing mutable objects are technically hashable but can break if the contained object is mutated. This leads to silent corruption.

## Float as Dictionary Key

Floating-point values can cause subtle issues as dictionary keys. Due to representation imprecision, `0.1 + 0.2` and `0.3` are different keys. This is not a bug in Python but a consequence of IEEE 754.

## String Interning and Identity

Small strings and identifiers are interned by Python, making `is` comparisons work in some cases. But relying on `is` for string equality is incorrect. Always use `==` for value comparison.

`is` checks identity (same object), `==` checks equality (same value). For large or dynamically created strings, `is` may fail even when values are equal.

## Unpacking and Extended Unpacking

Extended unpacking with `*` (e.g., `a, *b, c = [1, 2, 3, 4]`) works in Python 3 but not in Python 2. The starred variable becomes a list, even if it captures a single element.

Unpacking a generator with `*` consumes it entirely. This can be unexpected if the generator has side effects.

## Exception Handling and Variable Scope

Variables defined in `try` blocks are accessible in `except` and `finally` blocks, but only if the assignment succeeded. If the assignment itself raises an exception, the variable is not defined.

```python
try:
    x = 1 / 0
except ZeroDivisionError:
    print(x)  # NameError: name 'x' is not defined
```

## Boolean Evaluation Short-circuit

Python uses short-circuit evaluation for `and` and `or`. The second operand is not evaluated if the result is determined by the first. This is used for default values (`x or default`) and guard clauses.

`not` has higher precedence than `and` and `or`. `not a == b` is parsed as `not (a == b)`, not `(not a) == b`.

## Import Side Effects

Importing a module executes its top-level code. This can cause unexpected side effects, especially in test environments or when modules have global state. Circular imports may work in some cases but are fragile.

`from module import *` pollutes the namespace and makes it unclear where names come from. It is discouraged in production code.

## Decorator Order

Multiple decorators are applied bottom-up. `@decorator_a @decorator_b def f()` means `decorator_a(decorator_b(f))`. Reversing the order changes the behavior, often subtly.

Decorators that return a different callable can break `functools.wraps` if not applied correctly.

## Pickling Limitations

Pickling cannot serialize lambda functions, generators, or dynamic classes. `pickle` and `deepcopy` have different capabilities. `pickle` is faster but less safe; `deepcopy` is slower but handles more cases.

Objects with `__slots__` may not pickle correctly if `__getstate__` and `__setstate__` are not defined.

## Type Hints Are Not Enforced

Python type hints are static annotations checked by tools like mypy. The runtime does not enforce them. Passing the wrong type does not raise an error unless the code explicitly checks.

This can mask bugs that would be caught at compile time in statically typed languages.
