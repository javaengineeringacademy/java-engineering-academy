# Python Advanced Quiz

## Question 1 (MCQ - Decorator Execution Order)
What is the output of this code?

```python
def bold(func):
    def wrapper():
        return "<b>" + func() + "</b>"
    return wrapper

def italic(func):
    def wrapper():
        return "<i>" + func() + "</i>"
    return wrapper

@bold
@italic
def hello():
    return "Hello"

print(hello())
```

A) `<i><b>Hello</b></i>`
B) `<b><i>Hello</i></b>`
C) `<b>Hello</b><i>Hello</i>`
D) Error

**Answer: B**
**Explanation:** Decorators apply bottom-up: `@italic` wraps `hello` first, then `@bold` wraps the result. Execution is top-down: `bold`'s wrapper runs first, calling `italic`'s wrapper, which calls `hello`. Result: `<b><i>Hello</i></b>`.

---

## Question 2 (MCQ - Generator vs List Comprehension Memory)
You need to process 10 million numbers. Which approach uses less memory?

- A) `[x**2 for x in range(10_000_000)]`
- B) `(x**2 for x in range(10_000_000))`
- C) Both use the same memory
- D) `list(range(10_000_000))` uses less than both

**Answer: B**
**Explanation:** The list comprehension (A) builds the entire list in memory (~80MB for integers). The generator expression (B) yields one value at a time, using constant memory (~100 bytes regardless of size). For large datasets, generators are far more memory-efficient.

---

## Question 3 (Code Output - Context Manager Protocol)
What is the output of this code?

```python
class Timer:
    def __enter__(self):
        print("Starting")
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        print("Stopping")
        return False

with Timer() as t:
    print("Running")
```

A) `Starting` then `Running` then `Stopping`
B) `Running` then `Starting` then `Stopping`
C) `Starting` then `Running`
D) `Running` then `Stopping`

**Answer: A**
**Explanation:** `__enter__` runs before the `with` block body, `__exit__` runs after. The order is: `__enter__` prints "Starting", body prints "Running", `__exit__` prints "Stopping". Returning `False` from `__exit__` does not suppress exceptions.

---

## Question 4 (Code Output - Walrus Operator)
What is the output of this code?

```python
data = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
filtered = [y for x in data if (y := x * 2) > 10]
print(filtered)
```

A) `[12, 14, 16, 18, 20]`
B) `[11, 12, 13, 14, 15]`
C) `[6, 7, 8, 9, 10]`
D) Error: walrus operator not allowed in comprehensions

**Answer: A**
**Explanation:** The walrus operator `:=` assigns `x * 2` to `y` and the expression returns that value. The `if` clause checks `y > 10`, so only values where `2x > 10` (x > 5) pass. The result is `[12, 14, 16, 18, 20]`.

---

## Question 5 (Bug Finding - Closure Variables)
What is the output of this code?

```python
funcs = []
for i in range(3):
    funcs.append(lambda: i)

print([f() for f in funcs])
```

A) `[0, 1, 2]`
B) `[2, 2, 2]`
C) `[0, 1, 2]` with a warning
D) Error

**Answer: B**
**Explanation:** Lambdas capture variables by reference, not by value. All three lambdas reference the same variable `i`, which ends up as `2` after the loop. This is a classic closure pitfall. Fix: use `lambda i=i: i` or `functools.partial`.

---

## Question 6 (MCQ - Lambda Limitations)
Which statement about lambda functions is correct?

- A) Lambdas can contain multiple statements
- B) Lambdas can only contain a single expression
- C) Lambdas are always anonymous and cannot be named
- D) Lambdas support type annotations

**Answer: B**
**Explanation:** Lambda functions in Python are restricted to a single expression. They cannot contain statements, multiple expressions, or type annotations. However, they can be assigned to variables (e.g., `f = lambda x: x + 1`), so they are not always anonymous.

---

## Question 7 (Architecture Decision - Async/Await Execution)
In an `asyncio` application, when does the code after an `await` resume?

- A) When the awaited coroutine is scheduled
- B) When the awaited coroutine completes or yields control back
- C) Immediately, without waiting
- D) When the event loop is terminated

**Answer: B**
**Explanation:** `await` pauses the coroutine's execution and yields control to the event loop. The event loop runs other tasks. When the awaited coroutine completes (or yields), the event loop resumes the code after `await`. This is cooperative multitasking — coroutines voluntarily yield.

---

## Question 8 (Code Output - Comprehension Scope)
What is the output of this code?

```python
x = "outer"
result = [x for x in range(3)]
print(x)
```

A) `2`
B) `"outer"`
C) `0`
D) Error

**Answer: B**
**Explanation:** In Python 3, list comprehensions have their own scope. The variable `x` inside the comprehension does not leak into the enclosing scope. After the comprehension, `x` retains its original value `"outer"`. (Python 2 had different behavior.)

---

## Question 9 (Bug Finding - Decorator with Arguments)
This decorator with arguments fails. Find the bug:

```python
def repeat(n):
    def decorator(func):
        def wrapper(*args, **kwargs):
            for _ in range(n):
                func(*args, **kwargs)
        return wrapper
    return decorator

@repeat(3)
def greet(name):
    print(f"Hello {name}")

greet("Alice")
```

A) Decorator with arguments is not allowed in Python
B) Missing `return` in `wrapper` — `greet` returns `None` instead of calling the original
C) `n` is not accessible inside `wrapper`
D) `*args` and `**kwargs` cannot be used together

**Answer: B**
**Explanation:** The `wrapper` calls `func(*args, **kwargs)` but doesn't return its result. If `greet` returns a value, it would be lost. The fix is `return func(*args, **kwargs)` (or loop and return the last result, depending on intent). Also, `functools.wraps` should be added to preserve metadata.

---

## Question 10 (Code Output - Generator Sending Values)
What is the output of this code?

```python
def accumulator():
    total = 0
    while True:
        value = yield total
        if value is None:
            break
        total += value

gen = accumulator()
print(next(gen))
print(gen.send(5))
print(gen.send(10))
print(gen.send(3))
```

A) `0` then `5` then `15` then `18`
B) `0` then `5` then `10` then `3`
C) `None` then `5` then `15` then `18`
D) Error

**Answer: A**
**Explanation:** `next(gen)` starts the generator, which runs to `yield total` and returns `0`. `gen.send(5)` sends `5` into the generator (replacing `None` from `yield`), adds to total (5), loops back, and yields `5`. Then `send(10)` → total=15, yields 15. `send(3)` → total=18, yields 18.
