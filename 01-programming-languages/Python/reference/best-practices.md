# Python Best Practices

Writing clean, Pythonic, and maintainable code.

## Code Style

- Follow PEP 8
- Use snake_case for variables and functions
- Use CamelCase for classes
- Use UPPER_SNAKE_CASE for constants
- Keep lines under 79 characters (or 99 with agreement)

## Pythonic Patterns

```python
# Swap variables
a, b = b, a

# Enumerate instead of range(len())
for i, item in enumerate(lst):
    pass

# Unpacking
first, *rest = lst
*head, last = lst

# Walrus operator
if (n := len(lst)) > 10:
    print(f"List too long: {n}")
```

## Type Hints

```python
def greet(name: str) -> str:
    return f"Hello, {name}"

def process(items: list[int]) -> dict[str, int]:
    return {str(i): i for i in items}
```

## Error Handling

```python
# Specific exceptions
try:
    result = int(user_input)
except ValueError:
    print("Invalid number")

# Use custom exceptions
class AppError(Exception):
    pass

class ValidationError(AppError):
    pass
```

## Documentation

```python
def calculate_discount(price: float, discount: float) -> float:
    """
    Calculate discounted price.

    Args:
        price: Original price (must be positive).
        discount: Discount percentage (0-100).

    Returns:
        Price after discount.

    Raises:
        ValueError: If discount is not in range 0-100.
    """
    if not 0 <= discount <= 100:
        raise ValueError("Discount must be 0-100")
    return price * (1 - discount / 100)
```

## Performance Tips

- Use `collections.defaultdict` instead of checking keys
- Use `set` for membership testing over `list`
- Use generator expressions for large datasets
- Use `f-strings` over `.format()` or `%`
- Profile before optimizing: `cProfile`, `timeit`

## Testing

- Write tests for all public APIs
- Use descriptive test names
- Test edge cases and error paths
- Keep tests independent
- Use fixtures for setup/teardown

## Project Structure

```
project/
├── src/
│   └── package/
│       ├── __init__.py
│       ├── module.py
│       └── utils.py
├── tests/
│   ├── __init__.py
│   └── test_module.py
├── pyproject.toml
└── README.md
```
