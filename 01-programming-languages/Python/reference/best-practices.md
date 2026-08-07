# Python Best Practices

## Code Style

### PEP 8 Compliance
1. Use 4 spaces for indentation (no tabs)
2. Maximum line length of 88 characters (Black default)
3. Use blank lines to separate functions and classes
4. Use spaces around operators and after commas
5. Use snake_case for functions and variables
6. Use PascalCase for classes
7. Use UPPER_CASE for constants

### Naming Conventions
```python
# Functions and variables
def calculate_total_price():
    item_count = 0

# Classes
class UserManager:
    pass

# Constants
MAX_RETRY_COUNT = 3

# Private attributes
class MyClass:
    def __init__(self):
        self._internal = None
        self.__private = None
```

## Type Hints

```python
from typing import List, Dict, Optional, Tuple

def process_items(items: List[str]) -> Dict[str, int]:
    return {item: len(item) for item in items}

def find_user(user_id: int) -> Optional[Dict]:
    # Returns None if not found
    pass

def get_coordinates() -> Tuple[float, float]:
    return (40.7128, -74.0060)
```

## Error Handling

```python
# Specific exceptions
try:
    result = int(user_input)
except ValueError as e:
    logger.warning(f"Invalid input: {e}")
    raise

# Custom exceptions
class AppError(Exception):
    """Base application error."""
    pass

class ValidationError(AppError):
    """Validation failed."""
    pass
```

## Context Managers

```python
from contextlib import contextmanager

@contextmanager
def timer():
    import time
    start = time.time()
    try:
        yield
    finally:
        print(f"Elapsed: {time.time() - start:.2f}s")

with timer():
    # Code to time
    pass
```

## Generators

```python
# Memory efficient
def read_large_file(path):
    with open(path) as f:
        for line in f:
            yield line.strip()

# Generator expression
squares = (x**2 for x in range(1000000))
```

## Dataclasses

```python
from dataclasses import dataclass, field
from typing import List

@dataclass
class User:
    name: str
    email: str
    age: int
    roles: List[str] = field(default_factory=list)
```

## Virtual Environments

```bash
# Always use virtual environments
python -m venv venv
source venv/bin/activate

# Keep requirements up to date
pip freeze > requirements.txt

# Separate dev and prod dependencies
pip install -r requirements-dev.txt
```

## Testing

```python
# pytest style
def test_addition():
    assert 1 + 1 == 2

# Fixtures
import pytest

@pytest.fixture
def sample_data():
    return {"key": "value"}

def test_process(sample_data):
    assert process(sample_data) == expected
```

## Documentation

```python
def calculate_discount(price: float, discount: float) -> float:
    """Calculate discounted price.
    
    Args:
        price: Original price
        discount: Discount percentage (0-100)
    
    Returns:
        Price after discount
    
    Raises:
        ValueError: If discount is not between 0 and 100
    """
    if not 0 <= discount <= 100:
        raise ValueError("Discount must be between 0 and 100")
    return price * (1 - discount / 100)
```

## Performance

1. Use built-in functions and libraries
2. Profile before optimizing
3. Use appropriate data structures
4. Use list comprehensions
5. Consider generators for large datasets

## Security

1. Never hardcode secrets
2. Use environment variables
3. Validate all input
4. Use parameterized queries
5. Scan dependencies regularly

## Project Structure

1. Use src layout for new projects
2. Keep tests separate from source
3. Use pyproject.toml for configuration
4. Include type hints throughout
5. Write docstrings for all public APIs

## Tools

1. **Formatter**: Black
2. **Linter**: Ruff or Flake8
3. **Type Checker**: mypy
4. **Testing**: pytest
5. **Dependency Management**: Poetry or pip-tools
