# Python Migration

## Python 2 to 3

### Key Changes

**Print Statement to Function**
```python
# Python 2
print "Hello"

# Python 3
print("Hello")
```

**Integer Division**
```python
# Python 2
5 / 2 = 2

# Python 3
5 / 2 = 2.5
5 // 2 = 2  # Floor division
```

**Unicode Strings**
```python
# Python 2
unicode_string = u"Hello"
byte_string = b"Hello"

# Python 3
text_string = "Hello"  # Always Unicode
byte_string = b"Hello"
```

**Range**
```python
# Python 2
range(10)  # Returns list
xrange(10)  # Returns iterator

# Python 3
range(10)  # Returns iterator
list(range(10))  # If you need list
```

**Exception Syntax**
```python
# Python 2
try:
    pass
except ValueError, e:
    pass

# Python 3
try:
    pass
except ValueError as e:
    pass
```

### Automated Migration

#### 2to3 Tool
```bash
# Convert Python 2 code to Python 3
2to3 -w script.py

# Convert entire directory
2to3 -w mypackage/
```

#### Modernize
```bash
pip install modernize

# Convert code
python-modernize -w script.py
```

### Manual Changes

**Update Shebang**
```python
#!/usr/bin/env python3
```

**Update setup.py**
```python
from setuptools import setup

setup(
    name="myproject",
    version="0.1.0",
    python_requires=">=3.7",
)
```

### Testing
```bash
# Run tests with both versions
tox -e py27,py38

# Use pytest for consistent test runner
pytest tests/
```

## Dependency Migration

### requirements.txt
```txt
# Pin versions for reproducibility
requests==2.28.0
click>=8.0,<9.0
flask~=2.3
```

### Poetry
```bash
# Initialize
poetry init

# Add dependency
poetry add requests

# Export to requirements.txt
poetry export -f requirements.txt
```

### pipenv
```bash
# Initialize
pipenv install

# Add dependency
pipenv install requests

# Generate requirements.txt
pipenv requirements > requirements.txt
```

## Code Modernization

### f-strings (Python 3.6+)
```python
# Old
name = "World"
print("Hello, %s!" % name)
print("Hello, {}!".format(name))

# New
print(f"Hello, {name}!")
```

### Type Hints (Python 3.5+)
```python
def greet(name: str) -> str:
    return f"Hello, {name}"
```

### Dataclasses (Python 3.7+)
```python
from dataclasses import dataclass

@dataclass
class User:
    name: str
    email: str
```

### Walrus Operator (Python 3.8+)
```python
# Old
n = len(data)
if n > 10:
    print(n)

# New
if (n := len(data)) > 10:
    print(n)
```

## Framework Migration

### Django
```bash
# Upgrade Django
pip install "Django>=4.0"

# Run migrations
python manage.py migrate

# Check for deprecated features
python manage.py check --deploy
```

### Flask
```python
# Old Flask
from flask import Flask

app = Flask(__name__)

# New Flask with type hints
from flask import Flask
from typing import Flask

app: Flask = Flask(__name__)
```

## Version Strategy

### Use python-requires
```python
# setup.py
setup(
    python_requires=">=3.8",
)

# pyproject.toml
[project]
requires-python = ">=3.8"
```

### Test Matrix
```yaml
# .github/workflows/test.yml
strategy:
  matrix:
    python-version: [3.8, 3.9, 3.10, 3.11]
```

## Best Practices

1. Use `2to3` for initial conversion
2. Run tests frequently during migration
3. Update dependencies to Python 3 compatible versions
4. Use virtual environments for testing
5. Pin Python version in project configuration
6. Update CI/CD pipelines
7. Document breaking changes
8. Provide migration guide for users
