# Python Best Practices

A detailed guide to Python coding standards, project structure, and development practices.

## Table of Contents

- [PEP 8 Style Guide](#pep-8-style-guide)
- [Type Hints](#type-hints)
- [Testing](#testing)
- [Virtual Environments](#virtual-environments)
- [Package Management](#package-management)
- [Code Quality](#code-quality)
- [Documentation](#documentation)
- [Project Structure](#project-structure)
- [Error Handling](#error-handling)
- [Performance](#performance)

---

## PEP 8 Style Guide

### Naming Conventions

```python
# Variables and functions: snake_case
user_name = "Alice"
def get_user_name():
    pass

# Classes: PascalCase
class UserProfile:
    pass

# Constants: UPPER_SNAKE_CASE
MAX_RETRY_COUNT = 3
DATABASE_URL = "postgresql://localhost/mydb"

# Private: single underscore prefix
_internal_data = {}

# Name mangling: double underscore prefix
class MyClass:
    __private_var = "secret"  # Becomes _MyClass__private_var

# Module names: short, lowercase, underscores if needed
my_module.py

# Package names: short, lowercase, no underscores
mypackage/

# Avoid:
# - Single character names (except counters)
# - Capital letters (except classes)
# - Conflicting with built-ins (list, dict, etc.)
```

### Indentation and Whitespace

```python
# 4 spaces per indentation level
def function():
    if True:
        for i in range(10):
            print(i)

# Maximum line length: 79 characters
# For expressions: 72 characters

# Bad
result = some_function(argument_one, argument_two, argument_three, argument_four)

# Good
result = some_function(
    argument_one,
    argument_two,
    argument_three,
    argument_four
)

# Blank lines
# 2 blank lines before top-level definitions
# 1 blank line before method definitions

class MyClass:
    def method_one(self):
        pass

    def method_two(self):
        pass

def top_level_function():
    pass
```

### Imports

```python
# Imports should be on separate lines
# Bad
import os, sys

# Good
import os
import sys

# Import order (separated by blank lines)
# 1. Standard library imports
# 2. Related third party imports
# 3. Local application/library specific imports

import os
import sys
from pathlib import Path

import requests
from flask import Flask

from myapp import models
from myapp.utils import helper

# Use absolute imports
from mypackage.mymodule import my_class

# Use explicit relative imports (when needed)
from . import sibling_module
from ..parent_package import some_module
```

### Strings and Quotes

```python
# Use single quotes for short strings
name = 'Alice'

# Use double quotes for strings containing single quotes
greeting = "Hello, it's me"

# Use triple quotes for docstrings and multi-line strings
"""This is a docstring."""

'''This is also a docstring.'''

"""
This is a multi-line string.
"""

# f-strings preferred
name = "Alice"
print(f"Hello, {name}")
```

---

## Type Hints

### Basic Type Hints

```python
# Variables
name: str = "Alice"
age: int = 30
height: float = 5.9
is_active: bool = True

# Functions
def greet(name: str) -> str:
    return f"Hello, {name}!"

def process(data: list[int]) -> dict[str, int]:
    return {item: len(item) for item in data}

# Optional and Union
from typing import Optional, Union

def find_user(user_id: int) -> Optional[str]:
    if user_id == 1:
        return "Alice"
    return None

def merge(a: Union[str, int], b: Union[str, int]) -> str:
    return f"{a}{b}"
```

### Advanced Type Hints

```python
from typing import List, Dict, Tuple, Set, Callable, Type, Any
from typing import TypeVar, Generic, Protocol
from dataclasses import dataclass
from collections.abc import Sequence, Mapping

# Collections
def process(items: List[str]) -> Dict[str, int]:
    return {item: len(item) for item in items}

# Callables
def apply(func: Callable[[int, int], int], a: int, b: int) -> int:
    return func(a, b)

# Generics
T = TypeVar('T')

class Stack(Generic[T]):
    def __init__(self) -> None:
        self._items: List[T] = []

    def push(self, item: T) -> None:
        self._items.append(item)

    def pop(self) -> T:
        return self._items.pop()

# Protocols (structural subtyping)
class Drawable(Protocol):
    def draw(self) -> str: ...

def draw_shape(shape: Drawable) -> None:
    print(shape.draw())

# TypedDict
from typing import TypedDict

class UserDict(TypedDict):
    name: str
    age: int
    email: str

# Dataclass
@dataclass
class Point:
    x: float
    y: float
```

### Type Checking

```python
# Using mypy
# pip install mypy
# Run: mypy script.py

# Type ignore
x = some_untyped_function()  # type: ignore

# Casting
from typing import cast

value = cast(int, some_value)

# Type guards
from typing import TypeGuard

def is_string(val: Any) -> TypeGuard[str]:
    return isinstance(val, str)

def process(val: str | int) -> None:
    if is_string(val):
        print(val.upper())  # val is str here
    else:
        print(val * 2)
```

---

## Testing

### pytest

```python
# test_example.py
import pytest
from mymodule import add, subtract

# Basic test
def test_add():
    assert add(2, 3) == 5

def test_subtract():
    assert subtract(5, 3) == 2

# Test with fixtures
@pytest.fixture
def sample_data():
    return {"name": "Alice", "age": 30}

def test_process_data(sample_data):
    assert sample_data["name"] == "Alice"

# Parametrized tests
@pytest.mark.parametrize("a, b, expected", [
    (1, 2, 3),
    (5, 5, 10),
    (-1, 1, 0),
])
def test_add_parametrized(a, b, expected):
    assert add(a, b) == expected

# Exception testing
def test_division_by_zero():
    with pytest.raises(ZeroDivisionError):
        1 / 0

# Markers
@pytest.mark.slow
def test_heavy_computation():
    assert sum(range(1000000)) == 499999500000

# Running tests
# pytest
# pytest -v
# pytest -m "not slow"
# pytest --cov=mymodule
```

### unittest

```python
import unittest
from mymodule import Calculator

class TestCalculator(unittest.TestCase):
    def setUp(self):
        self.calc = Calculator()

    def tearDown(self):
        pass

    def test_add(self):
        self.assertEqual(self.calc.add(2, 3), 5)

    def test_subtract(self):
        self.assertEqual(self.calc.subtract(5, 3), 2)

    def test_divide(self):
        self.assertEqual(self.calc.divide(10, 2), 5)

    def test_divide_by_zero(self):
        with self.assertRaises(ZeroDivisionError):
            self.calc.divide(1, 0)

if __name__ == '__main__':
    unittest.main()
```

### Test Organization

```
project/
├── src/
│   └── mypackage/
│       ├── __init__.py
│       ├── module1.py
│       └── module2.py
├── tests/
│   ├── __init__.py
│   ├── conftest.py
│   ├── test_module1.py
│   └── test_module2.py
├── pytest.ini
└── setup.py
```

---

## Virtual Environments

### venv

```bash
# Create virtual environment
python -m venv venv

# Activate
# Linux/Mac
source venv/bin/activate

# Windows
venv\Scripts\activate

# Deactivate
deactivate

# Install packages
pip install requests
pip install -r requirements.txt

# Save dependencies
pip freeze > requirements.txt

# Install from requirements
pip install -r requirements.txt
```

### Poetry

```bash
# Install poetry
curl -sSL https://install.python-poetry.org | python3 -

# Create new project
poetry new myproject

# Add dependencies
poetry add requests
poetry add --group dev pytest

# Install dependencies
poetry install

# Run commands
poetry run python script.py
poetry run pytest

# Export requirements
poetry export -f requirements.txt --output requirements.txt
```

### Pipenv

```bash
# Install pipenv
pip install pipenv

# Create virtual environment
pipenv --python 3.11

# Install packages
pipenv install requests
pipenv install --dev pytest

# Run commands
pipenv run python script.py
pipenv run pytest

# Generate requirements.txt
pipenv requirements > requirements.txt
```

---

## Package Management

### pyproject.toml

```toml
[build-system]
requires = ["setuptools>=61.0"]
build-backend = "setuptools.build_meta"

[project]
name = "myproject"
version = "0.1.0"
description = "My awesome project"
readme = "README.md"
license = {text = "MIT"}
requires-python = ">=3.8"
authors = [
    {name = "Alice", email = "alice@example.com"},
]
dependencies = [
    "requests>=2.28.0",
    "flask>=2.0.0",
]

[project.optional-dependencies]
dev = [
    "pytest>=7.0.0",
    "pytest-cov>=4.0.0",
    "mypy>=1.0.0",
    "black>=23.0.0",
    "ruff>=0.1.0",
]

[tool.pytest.ini_options]
testpaths = ["tests"]
addopts = "-v --cov"

[tool.mypy]
python_version = "3.8"
strict = true

[tool.black]
line-length = 88

[tool.ruff]
line-length = 88
select = ["E", "F", "W", "I", "N", "UP", "B"]
```

### setup.py (Legacy)

```python
from setuptools import setup, find_packages

setup(
    name="myproject",
    version="0.1.0",
    packages=find_packages(where="src"),
    package_dir={"": "src"},
    install_requires=[
        "requests>=2.28.0",
    ],
    extras_require={
        "dev": [
            "pytest>=7.0.0",
            "mypy>=1.0.0",
        ],
    },
    python_requires=">=3.8",
)
```

---

## Code Quality

### Linting Tools

```bash
# flake8 - Style checking
pip install flake8
flake8 myproject/

# pylint - Comprehensive analysis
pip install pylint
pylint myproject/

# ruff - Fast linter (Rust-based)
pip install ruff
ruff check myproject/
ruff format myproject/
```

### Formatting Tools

```bash
# black - Code formatting
pip install black
black myproject/

# isort - Import sorting
pip install isort
isort myproject/

# autopep8 - PEP 8 formatting
pip install autopep8
autopep8 --in-place myproject/**/*.py
```

### Pre-commit Hooks

```yaml
# .pre-commit-config.yaml
repos:
  - repo: https://github.com/pre-commit/pre-commit-hooks
    rev: v4.4.0
    hooks:
      - id: trailing-whitespace
      - id: end-of-file-fixer
      - id: check-yaml
      - id: check-added-large-files

  - repo: https://github.com/psf/black
    rev: 23.3.0
    hooks:
      - id: black

  - repo: https://github.com/pycqa/isort
    rev: 5.12.0
    hooks:
      - id: isort

  - repo: https://github.com/pycqa/flake8
    rev: 6.0.0
    hooks:
      - id: flake8

  - repo: https://github.com/pre-commit/mirrors-mypy
    rev: v1.4.0
    hooks:
      - id: mypy
```

```bash
# Install pre-commit
pip install pre-commit

# Install hooks
pre-commit install

# Run on all files
pre-commit run --all-files
```

---

## Documentation

### Docstrings

```python
def calculate_area(width: float, height: float) -> float:
    """Calculate the area of a rectangle.

    Args:
        width: The width of the rectangle.
        height: The height of the rectangle.

    Returns:
        The area of the rectangle.

    Raises:
        ValueError: If width or height is negative.

    Example:
        >>> calculate_area(5, 3)
        15.0
    """
    if width < 0 or height < 0:
        raise ValueError("Dimensions must be non-negative")
    return width * height

class Student:
    """A class representing a student.

    Attributes:
        name: The student's name.
        grade: The student's grade (0-100).
    """

    def __init__(self, name: str, grade: int):
        """Initialize a Student instance.

        Args:
            name: The student's name.
            grade: The student's grade (0-100).
        """
        self.name = name
        self.grade = grade
```

### Sphinx Documentation

```python
# conf.py
project = 'My Project'
copyright = '2024, Alice'
author = 'Alice'
release = '0.1.0'

extensions = [
    'sphinx.ext.autodoc',
    'sphinx.ext.napoleon',
    'sphinx.ext.viewcode',
]

napoleon_google_docstring = True
napoleon_numpy_docstring = True
```

```bash
# Build documentation
pip install sphinx
sphinx-quickstart docs
cd docs
make html
```

---

## Project Structure

### Standard Layout

```
myproject/
├── src/
│   └── myproject/
│       ├── __init__.py
│       ├── core.py
│       ├── models.py
│       ├── utils.py
│       └── exceptions.py
├── tests/
│   ├── __init__.py
│   ├── conftest.py
│   ├── test_core.py
│   └── test_models.py
├── docs/
│   ├── conf.py
│   └── index.rst
├── scripts/
│   └── setup.py
├── pyproject.toml
├── setup.py
├── requirements.txt
├── README.md
├── LICENSE
└── .gitignore
```

### src Layout

```
myproject/
├── src/
│   └── myproject/
│       ├── __init__.py
│       └── ...
├── tests/
│   └── ...
└── ...
```

Benefits:
- Prevents accidental imports from working directory
- Cleaner packaging
- Better test isolation

---

## Error Handling

### Exception Hierarchy

```python
# Built-in exception hierarchy
BaseException
 ├── SystemExit
 ├── KeyboardInterrupt
 ├── GeneratorExit
 ├── Exception
      ├── StopIteration
      ├── ArithmeticError
      │    ├── FloatingPointError
      │    ├── OverflowError
      │    └── ZeroDivisionError
      ├── AssertionError
      ├── AttributeError
      ├── EOFError
      ├── ImportError
      │    └── ModuleNotFoundError
      ├── LookupError
      │    ├── IndexError
      │    └── KeyError
      ├── NameError
      │    └── UnboundLocalError
      ├── OSError
      │    ├── FileNotFoundError
      │    ├── IsADirectoryError
      │    ├── PermissionError
      │    └── TimeoutError
      ├── RuntimeError
      │    ├── NotImplementedError
      │    └── RecursionError
      ├── SyntaxError
      │    └── IndentationError
      ├── TypeError
      └── ValueError
           └── UnicodeError
```

### Custom Exceptions

```python
class AppError(Exception):
    """Base exception for application."""
    pass

class ValidationError(AppError):
    """Raised when validation fails."""
    def __init__(self, field: str, message: str):
        self.field = field
        self.message = message
        super().__init__(f"{field}: {message}")

class NotFoundError(AppError):
    """Raised when resource is not found."""
    def __init__(self, resource: str, id: Any):
        self.resource = resource
        self.id = id
        super().__init__(f"{resource} with id {id} not found")

class DatabaseError(AppError):
    """Raised when database operation fails."""
    pass

# Usage
def get_user(user_id: int) -> dict:
    user = db.find_user(user_id)
    if not user:
        raise NotFoundError("User", user_id)
    return user
```

### Exception Best Practices

```python
# 1. Be specific
# Bad
try:
    do_something()
except Exception:
    handle_error()

# Good
try:
    do_something()
except ValueError as e:
    handle_value_error(e)
except ConnectionError as e:
    handle_connection_error(e)

# 2. Don't catch exceptions you can't handle
# Bad
try:
    user = get_user(user_id)
except Exception:
    return None

# Good
try:
    user = get_user(user_id)
except NotFoundError:
    return None

# 3. Use custom exceptions for business logic
# Bad
if not user:
    raise Exception("User not found")

# Good
if not user:
    raise NotFoundError("User", user_id)

# 4. Include context in exceptions
# Bad
raise ValueError("Invalid input")

# Good
raise ValueError(f"Invalid email format: {email}")

# 5. Use finally for cleanup
def process_file(filename):
    f = None
    try:
        f = open(filename)
        return f.read()
    except FileNotFoundError:
        return None
    finally:
        if f:
            f.close()

# Or use context manager
def process_file(filename):
    with open(filename) as f:
        return f.read()
```

---

## Performance

### Optimization Tips

```python
# 1. Use built-in functions
# Bad
def sum_list(lst):
    total = 0
    for x in lst:
        total += x
    return total

# Good
def sum_list(lst):
    return sum(lst)

# 2. Use list comprehensions
# Bad
result = []
for x in range(1000):
    result.append(x ** 2)

# Good
result = [x ** 2 for x in range(1000)]

# 3. Use generators for large datasets
# Bad
def process_large():
    return [process(x) for x in range(1000000)]

# Good
def process_large():
    return (process(x) for x in range(1000000))

# 4. Use local variables
# Bad
def process():
    import math
    return [math.sqrt(x) for x in range(1000)]

# Good
def process():
    from math import sqrt
    return [sqrt(x) for x in range(1000)]

# 5. Use appropriate data structures
# Bad (O(n) lookup)
if item in my_list:
    process(item)

# Good (O(1) lookup)
if item in my_set:
    process(item)
```

### Profiling

```python
import cProfile
import timeit

# Profile your code
cProfile.run('your_function()')

# Time specific operations
time = timeit.timeit('sum(range(1000))', number=10000)
print(f"Time: {time:.4f}s")
```

---

## Summary

Python best practices:

1. **Follow PEP 8** - Consistent style
2. **Use type hints** - Better documentation and tooling
3. **Write tests** - pytest or unittest
4. **Use virtual environments** - Isolate dependencies
5. **Manage packages** - pyproject.toml or setup.py
6. **Use linting tools** - flake8, ruff, mypy
7. **Format code** - black, isort
8. **Document code** - Docstrings and comments
9. **Handle exceptions** - Specific and informative
10. **Profile and optimize** - Measure before optimizing
