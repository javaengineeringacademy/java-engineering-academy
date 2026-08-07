# Best Practices Quiz

## Question 1 (Multiple Choice)
According to PEP 8, what is the recommended maximum line length for Python code?

- A) 79 characters (traditional), 88-99 for modern projects with tools like Black
- B) 120 characters — longer lines are more readable
- C) No limit — let the developer decide
- D) 50 characters — shorter is always better

**Answer: A**
**Explanation:** PEP 8 recommends 79 characters for maximum line length. Modern tools like Black default to 88, and many teams use 99-120. The key principle is readability: lines that require horizontal scrolling hurt readability. Long lines should be wrapped using parentheses (implicit continuation) or backslashes (explicit continuation). The exact limit is a team decision — consistency matters more than the specific number.

---

## Question 2 (Multiple Choice)
Which naming convention does PEP 8 recommend for class names?

- A) `camelCase` — like Java
- B) `PascalCase` (CapWords) — each word capitalized
- C) `snake_case` — like functions
- D) `UPPER_SNAKE_CASE` — like constants

**Answer: B**
**Explanation:** PEP 8 specifies PascalCase for classes (`MyClass`, `HTTPServer`). Functions and methods use snake_case (`my_function`). Constants use UPPER_SNAKE_CASE (`MAX_RETRIES`). Variables use snake_case (`user_name`). Private attributes use leading underscore (`_internal`). This consistency makes code scannable — you can instantly tell if something is a class, function, or constant by its name.

---

## Question 3 (Multiple Choice)
What is the purpose of `pyproject.toml` in a Python project?

- A) It's a legacy file from Python 2 — use `setup.py` instead
- B) It's the modern standard project configuration file for build systems, dependencies, and tool settings (PEP 518, PEP 621)
- C) It only stores pytest configuration
- D) It replaces the `README.md` file

**Answer: B**
**Explanation:** `pyproject.toml` is the modern Python project configuration standard. It replaces `setup.py`, `setup.cfg`, `requirements.txt`, and tool-specific configs (`pytest.ini`, `.flake8`, `mypy.ini`) in one file. It defines build requirements (PEP 518), project metadata (PEP 621), and tool configurations. This reduces config file sprawl and is the recommended approach for all new Python projects.

---

## Question 4 (Multiple Choice)
In a code review, a developer submits a function with 200 lines, 8 parameters, and mixes I/O, parsing, and validation. What is the most critical issue?

- A) The function name is wrong
- B) It violates the Single Responsibility Principle — does too many things, making it hard to test, reuse, and maintain
- C) It should use more comments
- D) It should use global variables instead of parameters

**Answer: B**
**Explanation:** A function should do one thing and do it well. 200 lines with 8 parameters and mixed concerns is a maintenance nightmare. It can't be unit tested in isolation, reused in different contexts, or understood quickly. Refactor into smaller functions: `validate_input()`, `parse_data()`, `save_results()`. This follows SRP, improves testability, and makes the code self-documenting. Good code review catches design issues, not just syntax errors.

---

## Question 5 (Code Output)
What is the output of this code?

```python
def greet(name: str, greeting: str = "Hello") -> str:
    """Greet a user with a custom greeting."""
    return f"{greeting}, {name}!"

print(greet("Alice"))
print(greet("Bob", greeting="Hi"))
print(greet.__doc__)
```

**Answer:**
```
Hello, Alice!
Hi, Bob!
Greet a user with a custom greeting.
```
**Explanation:** This demonstrates PEP 8 best practices: type hints (`name: str`, `-> str`) for clarity, default parameter values (`greeting="Hello"`), keyword argument usage (`greeting="Hi"`), and a docstring accessible via `__doc__`. The function is self-documenting: its signature tells you what it expects and returns, the docstring explains why it exists, and it's callable with positional or keyword arguments.

---

## Question 6 (Code Output)
What is the output of this code?

```python
from dataclasses import dataclass, field

@dataclass
class User:
    name: str
    age: int
    email: str
    tags: list = field(default_factory=list)

user1 = User("Alice", 30, "alice@example.com")
user2 = User("Bob", 25, "bob@example.com", tags=["admin"])
user3 = User("Alice", 30, "alice@example.com")

print(user1 == user2)
print(user1 == user3)
print(user1)
```

**Answer:**
```
False
True
User(name='Alice', age=30, email='alice@example.com', tags=[])
```
**Explanation:** `@dataclass` automatically generates `__init__`, `__repr__`, and `__eq__` methods. `__eq__` compares all fields, so `user1 == user3` is `True` (same values). `user1 == user2` is `False` (different values). `field(default_factory=list)` creates a new list per instance (avoiding the mutable default argument trap). This is a PEP 8-aligned, DRY approach to data classes that eliminates boilerplate.

---

## Question 7 (Bug Finding)
Find the bug in this code:

```python
def process_items(items=[]):
    items.append("new")
    return items

print(process_items())
print(process_items())
print(process_items())
```

**Bug:** Mutable default argument trap. The default `[]` is created once when the function is defined, not on each call. All calls share the same list object. Each call appends to it, so the output is `['new']`, `['new', 'new']`, `['new', 'new', 'new']`. This is one of Python's most common gotchas — the default value is evaluated once at function definition time.
**Fix:** Use `None` as default and create a new list inside:
```python
def process_items(items=None):
    if items is None:
        items = []
    items.append("new")
    return items
```

---

## Question 8 (Bug Finding)
Find the bug in this project structure:

```
myproject/
    main.py
    utils.py
    models.py
    tests/
        test_main.py
        test_utils.py
```

There's no `__init__.py` in any directory, no `pyproject.toml`, and `main.py` imports `from utils import helper_function`.

**Bug:** Multiple issues: (1) No `pyproject.toml` or `setup.py` — the project can't be installed as a package. (2) No `__init__.py` — `tests/test_main.py` can't import from the project using standard package imports. (3) `from utils import helper_function` uses implicit relative imports, which are discouraged in Python 3. (4) No virtual environment configuration. (5) No tool configuration (linting, formatting, testing).
**Fix:** Modern Python project structure:
```
myproject/
    pyproject.toml
    src/
        myproject/
            __init__.py
            main.py
            utils.py
            models.py
    tests/
        __init__.py
        test_main.py
        test_utils.py
```
Use `src/` layout, absolute imports, and `pyproject.toml` for all configuration.

---

## Question 9 (Scenario)
A junior developer asks: "Why can't I just use `import *` to get all the functions I need? It saves time typing imports." How should you respond?

- A) They're right — `import *` is the most efficient way
- B) `import *` pollutes the namespace, makes dependencies unclear, can cause name collisions, and breaks static analysis tools — always use explicit imports
- C) It only works in Python 2, not Python 3
- D) `import *` is fine for scripts but not for libraries

**Answer: B**
**Explanation:** `from module import *` dumps everything into the current namespace, making it impossible to know where names come from. It can silently overwrite existing names (e.g., `from math import *` overwrites `sum`). Static analysis tools (mypy, IDE autocompletion) can't track dependencies. Explicit imports (`from math import sqrt, pi`) make dependencies visible, prevent name collisions, and are required for type checking. PEP 8 explicitly discourages `import *`.

---

## Question 10 (Architecture Decision)
You're starting a new Python project that will grow into a large application with multiple developers. How should you structure the project for long-term maintainability?

- A) Single `app.py` file — keep it simple
- B) `src/` layout with `pyproject.toml`, `src/package/` for source, `tests/` for tests, separate modules by domain, and pre-commit hooks for linting/formatting
- C) Flat directory with all files at the top level
- D) Copy-paste code between files to avoid complex imports

**Answer: B**
**Explanation:** The `src/` layout (recommended by Packaging Authority) prevents accidental imports from the project directory during testing. `pyproject.toml` centralizes all configuration. Domain-based modules (`users/`, `orders/`, `payments/`) organize by business logic, not technical function. Pre-commit hooks enforce consistent formatting (Black), linting (Ruff), and type checking (mypy) before code enters the repository. This structure scales from one developer to fifty without reorganization. It's the standard for professional Python projects.

---
