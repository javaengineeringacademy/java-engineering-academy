# Python Fundamentals Quiz

## Question 1 (MCQ - Variable Assignment & Mutability)
What is the output of this code?

```python
a = [1, 2, 3]
b = a
b.append(4)
print(a)
```

A) `[1, 2, 3]`
B) `[1, 2, 3, 4]`
C) `[4, 1, 2, 3]`
D) Error

**Answer: B**
**Explanation:** Lists are mutable. `b = a` makes `b` reference the same list object as `a`, not a copy. Appending to `b` modifies the shared object, so `a` also reflects the change. To avoid this, use `b = a.copy()` or `b = a[:]`.

---

## Question 2 (Code Output - Type Conversion)
What is the output of this code?

```python
x = "10"
y = 3.7
result = int(x) + int(y)
print(result)
```

A) `13`
B) `13.7`
C) `14`
D) Error

**Answer: A**
**Explanation:** `int("10")` returns `10`. `int(3.7)` truncates to `3` (not rounded). The result is `10 + 3 = 13`. `int()` on a float truncates toward zero, it does not round.

---

## Question 3 (MCQ - List vs Tuple)
Which statement about lists and tuples is correct?

- A) Lists are immutable; tuples are mutable
- B) Tuples are hashable and can be used as dictionary keys; lists cannot
- C) Lists are faster to create than tuples
- D) Tuples use more memory than lists

**Answer: B**
**Explanation:** Tuples are immutable and therefore hashable (if their elements are hashable), making them valid dictionary keys. Lists are mutable and not hashable. Tuples are generally more memory-efficient than lists.

---

## Question 4 (Code Output - Dictionary Comprehension)
What is the output of this code?

```python
squares = {x: x**2 for x in range(5) if x % 2 == 0}
print(squares)
```

A) `{0: 0, 1: 1, 2: 4, 3: 9, 4: 16}`
B) `{0: 0, 2: 4, 4: 16}`
C) `{1: 1, 3: 9}`
D) `{0: 0, 4: 16}`

**Answer: B**
**Explanation:** The comprehension iterates `x` from 0 to 4. The `if x % 2 == 0` filter keeps only even values: 0, 2, 4. Each is mapped to its square: `{0: 0, 2: 4, 4: 16}`.

---

## Question 5 (MCQ - String Slicing)
What is the output of this code?

```python
text = "Python"
print(text[-4:-1])
```

A) `tho`
B) `yth`
C) `tho`
D) `ytho`

**Answer: A**
**Explanation:** Negative indices count from the end: `text[-4]` is `t`, `text[-3]` is `h`, `text[-2]` is `o`. Slice `[-4:-1]` gives characters at indices 2, 3, 4 → `"tho"`. The stop index `-1` is excluded.

---

## Question 6 (Scenario - Global vs Local Scope)
A developer writes the following code and gets an `UnboundLocalError`. What is the problem?

```python
x = 10
def modify():
    x = x + 5
    return x

print(modify())
```

- A) `x` is not defined in the function
- B) Python treats `x` as local because of the assignment, but it reads before assigning
- C) `x` cannot be modified inside a function
- D) The `+` operator does not work with integers in functions

**Answer: B**
**Explanation:** Python determines variable scope at compile time. Since `x` is assigned within `modify()`, it is treated as local. The `x + 5` expression tries to read the local `x` before it is assigned, causing `UnboundLocalError`. Use `global x` to fix this.

---

## Question 7 (MCQ - List Comprehension vs Generator Expression)
Which statement correctly describes the difference between list comprehensions and generator expressions?

- A) Generator expressions are always faster than list comprehensions
- B) List comprehensions consume more memory because they create the entire list in memory
- C) Generator expressions can only be iterated once
- D) Both B and C are correct

**Answer: D**
**Explanation:** List comprehensions build the entire list in memory, consuming more RAM. Generator expressions yield items lazily (one at a time) and can only be iterated once because they maintain internal state. Neither is always faster—it depends on the use case.

---

## Question 8 (Bug Finding - File Handling)
This code is supposed to write data to a file but fails silently on error. Find the bug:

```python
def write_data(filename, data):
    f = open(filename, 'w')
    f.write(data)
    f.close()
```

- A) The file is opened in read mode
- B) No error handling — if `write()` fails, the file handle is never closed
- C) `write()` cannot accept string data
- D) The function should return the file object

**Answer: B**
**Explanation:** If `f.write(data)` raises an exception (e.g., disk full), `f.close()` is never called, leaking the file handle. Use a `with` statement: `with open(filename, 'w') as f:` to ensure the file is always closed properly, even on exceptions.

---

## Question 9 (Architecture Decision - Exception Handling)
You are building a library that processes user-uploaded files. How should exceptions be handled?

- A) Catch all exceptions with a bare `except:` and log them
- B) Let exceptions propagate and document them in the API
- C) Catch specific exceptions, handle recoverable ones, and re-raise or wrap unrecoverable ones
- D) Convert all exceptions to return `None` to avoid crashing

**Answer: C**
**Explanation:** Catching specific exceptions (e.g., `FileNotFoundError`, `ValueError`) allows precise handling of expected errors. Bare `except:` swallows programming bugs. Returning `None` hides failures. Re-raising or wrapping exceptions preserves context for callers while handling what you can.

---

## Question 10 (Bug Finding - Default Mutable Arguments)
What is the output of this code?

```python
def append_to(element, target=[]):
    target.append(element)
    return target

print(append_to(1))
print(append_to(2))
```

A) `[1]` then `[2]`
B) `[1]` then `[1, 2]`
C) `[1]` then `[1, 2]` with a warning
D) `Error: mutable default argument`

**Answer: B**
**Explanation:** Default mutable arguments are evaluated once at function definition time, not at each call. The same list object persists across calls. `append_to(1)` returns `[1]`, then `append_to(2)` appends to that same list, returning `[1, 2]`. The fix is `target=None` with `if target is None: target = []` inside the function.
