# Python Anti-Patterns

## 1. Mutable Default Arguments
**Description:** Using mutable objects (lists, dicts) as default function arguments.

**Why it's bad:** Default values are evaluated once at function definition, causing unexpected persistence between calls.

**Example (bad code):**
```python
def append_to(item, target=[]):
    target.append(item)
    return target
```

**Better approach:** Use None as default and create new object inside:
```python
def append_to(item, target=None):
    if target is None:
        target = []
    target.append(item)
    return target
```

**Impact:** Prevents unexpected state sharing between function calls.

---

## 2. Global State Abuse
**Description:** Relying heavily on global variables for state management.

**Why it's bad:** Makes code hard to test, debug, and reason about. Creates hidden dependencies.

**Example (bad code):**
```python
user_session = None
database_connection = None

def process_request():
    global user_session, database_connection
    # uses globals
```

**Better approach:** Pass dependencies explicitly or use dependency injection.

**Impact:** Improves testability, makes dependencies explicit, reduces side effects.

---

## 3. Wildcard Imports
**Description:** Using `from module import *`.

**Why it's bad:** Pollutes namespace, makes it unclear where names come from, can cause name collisions.

**Example (bad code):**
```python
from os import *
from sys import *
```

**Better approach:** Import specific names or use module prefix:
```python
import os
import sys
# or
from os import path, getcwd
```

**Impact:** Clearer code, no namespace pollution, explicit dependencies.

---

## 4. Excessive Nesting
**Description:** Deeply nested if/else, loops, or context managers.

**Why it's bad:** Reduces readability, makes code hard to follow, increases cognitive load.

**Example (bad code):**
```python
def process(data):
    if data:
        for item in data:
            if item.valid:
                if item.status == 'active':
                    # deeply nested logic
```

**Better approach:** Use early returns, guard clauses, or helper functions:
```python
def process(data):
    if not data:
        return
    for item in data:
        if not item.valid or item.status != 'active':
            continue
        # main logic
```

**Impact:** Improved readability, easier maintenance, reduced complexity.

---

## 5. Not Using Context Managers
**Description:** Manually opening/closing resources instead of using `with` statements.

**Why it's bad:** Can lead to resource leaks if exceptions occur before manual close.

**Example (bad code):**
```python
f = open('file.txt', 'r')
content = f.read()
f.close()
```

**Better approach:** Use context manager:
```python
with open('file.txt', 'r') as f:
    content = f.read()
```

**Impact:** Ensures proper resource cleanup, exception-safe code.

---

## 6. String Formatting Anti-Patterns
**Description:** Using old-style `%` formatting or concatenation instead of modern approaches.

**Why it's bad:** Less readable, error-prone, slower than modern alternatives.

**Example (bad code):**
```python
name = "Alice"
age = 30
msg = "Hello, %s. You are %d years old." % (name, age)
msg2 = "Hello, " + name + ". You are " + str(age) + " years old."
```

**Better approach:** Use f-strings or .format():
```python
msg = f"Hello, {name}. You are {age} years old."
msg2 = "Hello, {}. You are {} years old.".format(name, age)
```

**Impact:** Better readability, less error-prone, better performance.

---

## 7. Ignoring Exceptions
**Description:** Catching exceptions and doing nothing (empty except blocks).

**Why it's bad:** Hides errors, makes debugging impossible, can mask serious issues.

**Example (bad code):**
```python
try:
    risky_operation()
except Exception:
    pass
```

**Better approach:** Handle exceptions meaningfully or log them:
```python
try:
    risky_operation()
except SpecificException as e:
    logger.error(f"Operation failed: {e}")
    # handle or re-raise
```

**Impact:** Better error handling, easier debugging, more reliable applications.

---

## 8. Not Using Virtual Environments
**Description:** Installing packages globally instead of using virtual environments.

**Why it's bad:** Creates dependency conflicts between projects, makes reproducibility difficult.

**Example (bad code):**
```bash
pip install requests
pip install flask
# All installed globally
```

**Better approach:** Use virtual environments:
```bash
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

**Impact:** Isolated dependencies, reproducible environments, no version conflicts.

---

## 9. Overly Complex List Comprehensions
**Description:** Writing complex logic in single list comprehensions.

**Why it's bad:** Reduces readability, hard to debug, defeats the purpose of comprehensions.

**Example (bad code):**
```python
result = [transform(x) if x > 0 else default for x in data if x is not None and validate(x) and complex_check(x)]
```

**Better approach:** Break into multiple lines or use regular loops:
```python
result = []
for x in data:
    if x is None or not validate(x) or not complex_check(x):
        continue
    result.append(transform(x) if x > 0 else default)
```

**Impact:** Improved readability, easier debugging and maintenance.

---

## 10. Not Using Type Hints
**Description:** Writing Python code without type annotations.

**Why it's bad:** Reduces code clarity, makes IDE support weaker, harder to understand function contracts.

**Example (bad code):**
```python
def process(data, count):
    return data[:count]
```

**Better approach:** Add type hints:
```python
def process(data: list[str], count: int) -> list[str]:
    return data[:count]
```

**Impact:** Better documentation, improved IDE support, catching type errors early.

---

## 11. Mutable Class Attributes
**Description:** Defining mutable default values as class attributes.

**Why it's bad:** All instances share the same mutable object, causing unexpected behavior.

**Example (bad code):**
```python
class MyClass:
    items = []  # Shared across all instances
    
    def add(self, item):
        self.items.append(item)
```

**Better approach:** Initialize in __init__:
```python
class MyClass:
    def __init__(self):
        self.items = []
```

**Impact:** Each instance gets its own copy, preventing shared state issues.

---

## 12. Ignoring PEP 8
**Description:** Not following Python's style guide consistently.

**Why it's bad:** Reduces code readability, makes collaboration harder, creates inconsistent codebase.

**Example (bad code):**
```python
def my_function( x,y ):
    if(x>0):
        return True
    else:
        return False
```

**Better approach:** Follow PEP 8:
```python
def my_function(x, y):
    if x > 0:
        return True
    return False
```

**Impact:** Consistent style, improved readability, easier collaboration.