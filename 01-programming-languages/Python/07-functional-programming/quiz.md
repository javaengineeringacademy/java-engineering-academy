# Python Functional Programming Quiz

## Question 1 (MCQ - map vs list comprehension)
What is the difference between `map()` and a list comprehension?

- A) `map()` is always faster than list comprehensions
- B) `map()` returns a lazy iterator; list comprehensions return a list immediately
- C) List comprehensions cannot use lambda functions
- D) `map()` only works with built-in functions

**Answer: B**
**Explanation:** `map(func, iterable)` returns a lazy iterator — it applies `func` on demand as you iterate. A list comprehension `[func(x) for x in iterable]` evaluates all results immediately and stores them in a list. Use `map()` when you need lazy evaluation or are passing the result to another iterator. Use list comprehensions for readability and when you need a list right away.

---

## Question 2 (Code Output - reduce)
What is the output of this code?

```python
from functools import reduce

nums = [1, 2, 3, 4, 5]
result = reduce(lambda acc, x: acc + x, nums, 0)
print(result)
```

A) `15`
B) `[1, 3, 6, 10, 15]`
C) `5`
D) Error: reduce requires two arguments

**Answer: A**
**Explanation:** `reduce` applies the lambda cumulatively: `0+1=1`, `1+2=3`, `3+3=6`, `6+4=10`, `10+5=15`. The third argument `0` is the initial accumulator value. Without it, `reduce` uses the first element as the initial value. This is essentially `sum(nums)` — use `sum()` for addition, but `reduce` is powerful for any accumulation pattern.

---

## Question 3 (MCQ - Lambda Limitations)
What is a limitation of Python's `lambda`?

- A) `lambda` cannot accept multiple arguments
- B) `lambda` can only contain a single expression (no statements like `if/else` blocks, `for` loops, or assignments)
- C) `lambda` is slower than regular functions
- D) `lambda` cannot be passed as an argument

**Answer: B**
**Explanation:** `lambda` is restricted to a single expression. You can't write multi-line logic inside it. Use a regular `def` function for anything complex. Note that ternary expressions (`x if cond else y`) are expressions and work inside lambdas, but `if` statements, `for` loops, `try/except`, and assignments are statements and cannot be used.

---

## Question 4 (Code Output - closures)
What is the output of this code?

```python
def make_counters():
    counters = []
    for i in range(3):
        def counter():
            return i
        counters.append(counter)
    return counters

c1, c2, c3 = make_counters()
print(c1(), c2(), c3())
```

A) `0 1 2`
B) `2 2 2`
C) `0 0 0`
D) Error: cannot access loop variable

**Answer: B**
**Explanation:** This is the classic closure-over-loop-variable gotcha. The inner `counter` function captures `i` by reference, not by value. By the time any `counter()` is called, the loop has finished and `i` is `2`. All three closures share the same `i`. To fix: use `lambda i=i: i` or create a new scope with a factory function. This is one of the most common Python pitfalls.

---

## Question 5 (Code Output - partial)
What is the output of this code?

```python
from functools import partial

def power(base, exponent):
    return base ** exponent

square = partial(power, exponent=2)
cube = partial(power, exponent=3)

print(square(5), cube(5))
```

A) `25 125`
B) `32 243`
C) `5 5`
D) Error: partial requires positional arguments

**Answer: A**
**Explanation:** `partial(power, exponent=2)` creates a new callable with `exponent` pre-filled as `2`. So `square(5)` is equivalent to `power(5, exponent=2)` = `5**2` = `25`. Similarly, `cube(5)` = `power(5, exponent=3)` = `125`. `partial` is useful for creating specialized versions of general functions without writing wrapper functions.

---

## Question 6 (Bug Finding - filter)
This filter function isn't working as expected. Find the bug:

```python
data = ["hello", "", "world", "", "python"]
result = list(filter(None, data))
print(result)

# Expected: ["hello", "world", "python"]
# But they also try:
result2 = list(filter(lambda x: x != "", data))
print(result2)
```

A) The first filter is buggy — it removes empty strings incorrectly
B) Both approaches work correctly and produce the same output
C) `filter(None, data)` removes all falsy values, including `"0"` if it existed
D) The lambda version is buggy

**Answer: C**
**Explanation:** Both approaches produce `["hello", "world", "python"]` for this data. However, `filter(None, data)` removes ALL falsy values — empty strings, `0`, `False`, `None`, empty lists. If the data contained `"0"` (a truthy string) it would stay, but `0` (the integer) would be removed. The lambda `x: x != ""` only removes empty strings specifically. Choose based on whether you want "remove falsy" or "remove empty strings."

---

## Question 7 (Bug Finding - higher-order function)
This higher-order function has a bug. What is it?

```python
def apply_funcs(funcs, value):
    result = value
    for func in funcs:
        result = func(result)
    return result

add_one = lambda x: x + 1
double = lambda x: x * 2

pipeline = [add_one, double, add_one]
print(apply_funcs(pipeline, 3))
# Expected: 3 → 4 → 8 → 9
```

A) The function doesn't work — it can't handle lambdas
B) The function works correctly — output is `9`
C) The bug is that functions are applied in reverse order
D) The function modifies the original `funcs` list

**Answer: B**
**Explanation:** There is no bug. The pipeline applies functions left to right: `3 + 1 = 4`, `4 * 2 = 8`, `8 + 1 = 9`. This is correct function composition — each function's output becomes the next function's input. This pattern is the foundation of data pipelines and middleware chains in functional programming.

---

## Question 8 (Scenario - pure functions)
You have a function that modifies a dictionary in place AND returns it. How should you refactor it for functional programming?

```python
def add_user(users, name, age):
    users[name] = age
    return users
```

- A) Keep it — in-place modification is efficient
- B) Return a new dictionary instead of modifying the input: `{**users, name: age}`
- C) Remove the return value — just modify in place
- D) Use a global variable instead

**Answer: B**
**Explanation:** Pure functions avoid side effects. Instead of mutating the input, create and return a new dictionary. This makes the function predictable, testable, and safe for concurrent use. The cost of copying is usually negligible compared to the benefits of immutability. Use `{**users, name: age}` or `users | {name: age}` (Python 3.9+) for a clean functional approach.

---

## Question 9 (Architecture Decision - functional style)
When is a functional programming approach MORE appropriate than OOP?

- A) When you need to model complex state with many interconnected objects
- B) When you have data transformations, pipelines, or operations that should be composable and side-effect-free
- C) When you need inheritance hierarchies
- D) When performance is the top priority

**Answer: B**
**Explanation:** Functional programming excels at data transformation pipelines, parallel processing (no shared state), and composing small pure functions. OOP is better for modeling complex stateful domains with relationships. In practice, Python supports both paradigms — use functional style for data processing (map/filter/reduce chains), and OOP for domain modeling. Many Python projects mix both effectively.

---

## Question 10 (Architecture Decision - functools)
You're building a configuration system where functions need access to shared settings. Which approach is best?

- A) Global variables — simple and accessible everywhere
- B) `functools.partial` to bind configuration values to functions at creation time
- C) Class with instance variables — stores config as `self.settings`
- D) Pass the config dict as the first argument to every function

**Answer: B**
**Explanation:** `partial` lets you pre-bind configuration to functions, creating specialized versions without global state. For example: `validate = partial(validate, min_length=8, max_length=32)`. This is composable, testable (you can override defaults), and doesn't rely on global state. Option D works but is verbose. Option C is fine for complex state. Option A is the worst choice — globals make testing and reasoning difficult. `partial` is the functional programmer's tool for dependency injection.
