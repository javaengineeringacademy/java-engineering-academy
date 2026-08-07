# Python Type Hints Reference

## What are Type Hints?

Type hints are annotations that specify the expected types of variables, function arguments, and return values. They help improve code clarity, enable better IDE support, and facilitate static type checking.

## Why does Type Hints matter?

Understanding type hints helps you:
- Write more maintainable code
- Catch type-related bugs early
- Improve IDE autocomplete and documentation
- Enable static type checking with tools like mypy

---

## 1. Basic Type Hints

```python
# Variables
x: int = 42
name: str = "Alice"
is_active: bool = True

# Function arguments and return type
def greet(name: str) -> str:
    return f"Hello, {name}!"

# Multiple arguments
def add(a: int, b: int) -> int:
    return a + b
```

---

## 2. Complex Types

```python
from typing import List, Dict, Tuple, Set, Optional, Union

# List
numbers: List[int] = [1, 2, 3]
names: List[str] = ["Alice", "Bob"]

# Dict
scores: Dict[str, int] = {"Alice": 90, "Bob": 85}

# Tuple
point: Tuple[int, int] = (10, 20)

# Set
unique_numbers: Set[int] = {1, 2, 3}

# Optional (can be None)
def find_user(user_id: int) -> Optional[str]:
    if user_id == 1:
        return "Alice"
    return None

# Union (multiple types)
def process(value: Union[int, str]) -> str:
    return str(value)

# Python 3.10+ union syntax
def process(value: int | str) -> str:
    return str(value)
```

---

## 3. Callable

```python
from typing import Callable, Any

# Function type
def apply(func: Callable[[int, int], int], a: int, b: int) -> int:
    return func(a, b)

# Callable with any arguments
def execute(func: Callable[..., Any], *args: Any, **kwargs: Any) -> Any:
    return func(*args, **kwargs)
```

---

## 4. Generics

```python
from typing import TypeVar, Generic

T = TypeVar('T')

class Stack(Generic[T]):
    def __init__(self) -> None:
        self.items: List[T] = []
    
    def push(self, item: T) -> None:
        self.items.append(item)
    
    def pop(self) -> T:
        return self.items.pop()

# Usage
stack: Stack[int] = Stack()
stack.push(1)
stack.push(2)
```

---

## 5. Protocols

```python
from typing import Protocol, runtime_checkable

@runtime_checkable
class Drawable(Protocol):
    def draw(self) -> None: ...

class Circle:
    def draw(self) -> None:
        print("Drawing circle")

def draw_shape(shape: Drawable) -> None:
    shape.draw()

# Circle implements Drawable protocol
draw_shape(Circle())  # Works
```

---

## 6. TypeVar

```python
from typing import TypeVar

T = TypeVar('T')
T_co = TypeVar('T_co', covariant=True)
T_contra = TypeVar('T_contra', contravariant=True)

def first(items: List[T]) -> T:
    return items[0]

# Usage
numbers = [1, 2, 3]
result = numbers[0]  # int
```

---

## 7. Literal

```python
from typing import Literal

def set_direction(direction: Literal["north", "south", "east", "west"]) -> None:
    print(f"Direction: {direction}")

set_direction("north")  # Works
# set_direction("up")  # Type error
```

---

## 8. TypedDict

```python
from typing import TypedDict

class UserDict(TypedDict):
    name: str
    age: int
    email: str

def process_user(user: UserDict) -> None:
    print(user["name"])

# Usage
user: UserDict = {"name": "Alice", "age": 30, "email": "alice@example.com"}
process_user(user)
```

---

## 9. Final

```python
from typing import Final

MAX_SIZE: Final = 100
# MAX_SIZE = 200  # Type error
```

---

## One-Minute Revision Table

| Type | Description | Example |
|------|-------------|---------|
| **int** | Integer | `x: int = 42` |
| **str** | String | `name: str = "Alice"` |
| **bool** | Boolean | `active: bool = True` |
| **float** | Float | `pi: float = 3.14` |
| **List** | List | `numbers: List[int] = [1, 2]` |
| **Dict** | Dictionary | `scores: Dict[str, int] = {}` |
| **Tuple** | Tuple | `point: Tuple[int, int] = (1, 2)` |
| **Set** | Set | `unique: Set[int] = {1, 2}` |
| **Optional** | Can be None | `name: Optional[str] = None` |
| **Union** | Multiple types | `value: Union[int, str] = 42` |
| **Callable** | Function | `func: Callable[[int], int]` |
| **TypeVar** | Generic type | `T = TypeVar('T')` |
| **Protocol** | Structural typing | `class Drawable(Protocol):` |
| **Literal** | Literal types | `direction: Literal["north", "south"]` |
| **TypedDict** | Typed dictionary | `class User(TypedDict):` |
| **Final** | Constant | `MAX: Final = 100` |

---

## Common Mistakes

### 1. Forgetting to Import Types

```python
# WRONG
def greet(name: str) -> str:
    return f"Hello, {name}!"

# RIGHT (if using older Python)
from typing import str
def greet(name: str) -> str:
    return f"Hello, {name}!"
```

### 2. Using Type Hints at Runtime

```python
# WRONG
def greet(name: str) -> str:
    return f"Hello, {name}!"

# Type hints are not enforced at runtime
greet(42)  # No error

# RIGHT (use type checker)
# Run mypy to check types
```

### 3. Overusing Type Hints

```python
# WRONG (too verbose)
def process(data: List[Dict[str, Union[int, str, None]]]) -> None:
    pass

# RIGHT (use aliases)
from typing import TypeAlias
DataDict: TypeAlias = Dict[str, Union[int, str, None]]

def process(data: List[DataDict]) -> None:
    pass
```

---

## Production Notes

1. **Use type hints for public APIs** - Improve documentation
2. **Use mypy for static checking** - Catch type errors
3. **Use TypeVar for generics** - More flexible
4. **Use Protocol for structural typing** - More Pythonic
5. **Use TypedDict for dictionaries** - When keys are known
6. **Use Literal for constants** - When values are limited
7. **Use Final for constants** - Prevent modification
8. **Use Optional for nullable** - More explicit than None
9. **Use Union for multiple types** - When type can vary
10. **Use Callable for function types** - More flexible

---

## Further Reading

- Python documentation on typing module
- PEP 484 - Type Hints
- mypy documentation
