# Python Exception Handling Quiz

## Question 1 (MCQ)
In Python's exception hierarchy, which is the base class for all built-in exceptions?

- A) `Exception`
- B) `BaseException`
- C) `object`
- D) `StandardError`

**Answer: B**
**Explanation:** `BaseException` is the root of the exception hierarchy. `Exception` inherits from `BaseException` and is the base for most application-level exceptions. `SystemExit`, `KeyboardInterrupt`, and `GeneratorExit` inherit directly from `BaseException`, which is why catching `Exception` doesn't catch those.

---

## Question 2 (MCQ)
What is the correct order of execution in a try/except/else/finally block?

- A) try → except → else → finally
- B) try → finally → except → else
- C) try → else → except → finally
- D) try → except → finally → else

**Answer: A**
**Explanation:** The execution order is: `try` block runs first. If an exception occurs, `except` handles it. If no exception, `else` runs. `finally` always runs regardless of whether an exception occurred. The `else` block is useful for code that should only run when the `try` succeeds.

---

## Question 3 (Code Output)
What is the output of this code?

```python
try:
    result = 10 / 0
except ZeroDivisionError:
    print("Caught")
else:
    print("No error")
finally:
    print("Done")

```

- A) Caught, Done
- B) No error, Done
- C) Caught
- D) ZeroDivisionError

**Answer: A**
**Explanation:** The division by zero raises `ZeroDivisionError`, which is caught by the `except` block printing "Caught". The `else` block is skipped because an exception occurred. The `finally` block always executes, printing "Done". Output: "Caught" then "Done".

---

## Question 4 (Code Output)
What is the output of this code?

```python
def divide(a, b):
    try:
        return a / b
    except ZeroDivisionError:
        return "Error"
    finally:
        print("Finally")

result = divide(10, 0)
print(result)

```

- A) Finally, Error
- B) Error, Finally
- C) Finally, Error, Finally
- D) 0, Finally

**Answer: A**
**Explanation:** The `try` block raises `ZeroDivisionError`, caught by `except` which returns "Error". Before returning, `finally` executes and prints "Finally". The printed order is "Finally" first (from finally), then "Error" (the return value printed by `print(result)`).

---

## Question 5 (MCQ)
How do you create a custom exception class in Python?

- A) Inherit from `Exception` (or another built-in exception)
- B) Use the `exception` keyword
- C) Call `create_exception()`
- D) Inherit from `BaseException` only

**Answer: A**
**Explanation:** Custom exceptions should inherit from `Exception` (not `BaseException`). This ensures they're caught by bare `except Exception` clauses and don't interfere with system-exit exceptions like `KeyboardInterrupt`. Example: `class MyError(Exception): pass`.

---

## Question 6 (Bug Finding)
Find the bug in this exception handling code:

```python
try:
    value = int(input("Enter a number: "))
    result = 100 / value
except ValueError, ZeroDivisionError:
    print("Invalid input")

```

- A) The code is correct
- B) Multiple exceptions should be caught as a tuple: `except (ValueError, ZeroDivisionError)`
- C) `except` cannot catch multiple exceptions
- D) The `print` statement needs a newline

**Answer: B**
**Explanation:** To catch multiple exception types, you must pass them as a tuple: `except (ValueError, ZeroDivisionError)`. Using a comma (Python 2 syntax) or without tuple syntax causes a `SyntaxError` in Python 3. The correct syntax is `except (ValueError, ZeroDivisionError):`.

---

## Question 7 (Bug Finding)
What's wrong with this exception chaining code?

```python
try:
    open("nonexistent.txt")
except FileNotFoundError as e:
    raise ValueError("Failed to process") from e

try:
    open("nonexistent.txt")
except FileNotFoundError:
    raise ValueError("Failed to process")

```

- A) Both blocks work identically
- B) The first block chains exceptions properly; the second block loses the original traceback
- C) The second block is correct; the first is wrong
- D) Neither block works

**Answer: B**
**Explanation:** The first block uses `raise ... from e` to explicitly chain exceptions, preserving the original traceback. The second block implicitly chains (Python 3 does this automatically), but using explicit `from` is clearer and allows setting `__cause__` to `None` to suppress chaining when needed.

---

## Question 8 (MCQ)
What does `sys.exc_info()` return?

- A) A tuple of (exception type, exception value, traceback)
- B) The current exception object only
- C) A formatted exception string
- D) The exception's line number

**Answer: A**
**Explanation:** `sys.exc_info()` returns a 3-tuple: `(type, value, traceback)` for the current exception. This is useful when you need to access the exception programmatically within an `except` block. Outside an exception handler, it returns `(None, None, None)`.

---

## Question 9 (Scenario)
You're building an API endpoint that needs to catch validation errors, log them, re-raise a custom exception with context, and ensure cleanup happens. Which pattern should you use?

- A) Catch all exceptions with `except Exception`
- B) Catch specific exceptions, use `raise CustomError() from e` for chaining, and use `finally` for cleanup
- C) Use `try/except` without `finally` since `raise` handles it
- D) Use `atexit` handlers instead

**Answer: B**
**Explanation:** The best practice is: catch specific exceptions (not bare `except`), chain them with `from` to preserve the traceback, and use `finally` for cleanup (closing files, connections). This provides full context for debugging while ensuring resources are released.

---

## Question 10 (Architecture)
When designing a large application's exception hierarchy, what is the recommended approach?

- A) Create one exception class for the entire application
- B) Create a base application exception, then domain-specific subclasses (e.g., `ValidationError`, `AuthenticationError`, `DatabaseError`)
- C) Use only built-in exceptions
- D) Create an exception for every possible error

**Answer: B**
**Explanation:** A well-structured exception hierarchy has a base `AppError` that inherits from `Exception`, with domain-specific subclasses. This allows callers to catch broad categories or specific errors. Each module can have its own exception subtree. This pattern is used in frameworks like Django (`django.core.exceptions`) and Flask.
