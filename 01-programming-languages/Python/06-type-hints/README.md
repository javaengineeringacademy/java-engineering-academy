# Module 06: Type Hints

> "Type hints are not about making Python typed. They're about making Python code *communicate* better."

---

## What Are Type Hints and Why They Exist

Type hints (introduced in Python 3.5 via PEP 484) are optional annotations that describe the expected types of variables, function parameters, and return values.

**Why they exist:**

- **Documentation that never lies** — Code is the source of truth, not a separate doc
- **IDE support** — Autocomplete, error detection, refactoring tools
- **Catch bugs before runtime** — Static type checkers (mypy, pyright) find errors at development time
- **Team communication** — Senior engineers signal intent to junior engineers
- **API contracts** — External consumers know exactly what to expect

```python
# Without type hints — what does this return?
def process(data):
    return data["value"] * 2

# With type hints — crystal clear
def process(data: dict[str, int]) -> int:
    return data["value"] * 2
```

**Key insight:** Type hints are *hints*, not enforced constraints at runtime. Python remains dynamically typed. The value is in the tooling ecosystem, not runtime enforcement.

---

## Basic Types

### Primitive Types

```python
from typing import Any

name: str = "Alice"
age: int = 30
price: float = 29.99
is_active: bool = True
data: bytes = b"binary"

# Python 3.10+ union syntax
def get_value(key: str) -> str | None:
    return cache.get(key)
```

### None Handling

```python
# None is a special case
def find_user(user_id: int) -> str | None:  # Python 3.10+
    """Returns username or None if not found."""
    return user_registry.get(user_id)

# Before Python 3.10
from typing import Optional
def find_user(user_id: int) -> Optional[str]:
    return user_registry.get(user_id)
```

### Any, NoReturn, Never

```python
from typing import Any, NoReturn

def accept_anything(value: Any) -> None:
    # You're on your own here — no type safety
    pass

def crash() -> NoReturn:
    raise SystemExit("fatal")

def infinite_loop() -> Never:  # Python 3.11+
    while True:
        pass
```

---

## Complex Types

### Collections

```python
from typing import List, Dict, Tuple, Set, Sequence

# Python 3.9+ — use built-in types directly
names: list[str] = ["Alice", "Bob"]
scores: dict[str, float] = {"Alice": 95.0}
coords: tuple[float, float] = (40.7128, -74.0060)
unique: set[int] = {1, 2, 3}

# Before Python 3.9 — use typing module
from typing import List, Dict, Tuple, Set
names: List[str] = ["Alice", "Bob"]
scores: Dict[str, float] = {"Alice": 95.0}

# Sequence for read-only lists
def get_names() -> Sequence[str]:
    return ["Alice", "Bob"]  # Caller can't modify
```

### Optional and Union

```python
from typing import Optional, Union

# Optional[X] is just Union[X, None]
def greet(name: Optional[str]) -> str:
    if name is None:
        return "Hello, stranger!"
    return f"Hello, {name}!"

# Union — value can be multiple types
def parse(input_val: str | int) -> str:
    if isinstance(input_val, int):
        return f"Number: {input_val}"
    return f"String: {input_val}"
```

### Callable

```python
from typing import Callable

def apply_twice(func: Callable[[int], int], value: int) -> int:
    return func(func(value))

# With keyword arguments
def create_transformer(
    func: Callable[[int], int],
    default: int = 0
) -> Callable[[int], int]:
    def transformer(x: int) -> int:
        return func(x) or default
    return transformer
```

### TypedDict and Literal

```python
from typing import TypedDict, Literal

class UserDict(TypedDict):
    name: str
    age: int
    email: str

def get_user(user_id: int) -> UserDict:
    return {"name": "Alice", "age": 30, "email": "alice@example.com"}

# Literal — only specific values allowed
def set_mode(mode: Literal["read", "write", "append"]) -> None:
    pass
```

---

## Protocol (Structural Subtyping)

Protocols enable duck typing with type safety — no inheritance required.

```python
from typing import Protocol, runtime_checkable

@runtime_checkable
class Drawable(Protocol):
    def draw(self) -> str: ...

class Circle:
    def draw(self) -> str:
        return "●"

class Square:
    def draw(self) -> str:
        return "■"

# Works with ANY class that has a draw() method
def render(shape: Drawable) -> None:
    print(shape.draw())

render(Circle())  # OK
render(Square())  # OK
```

**When to use Protocol:**
- You want duck typing with type safety
- You don't own the classes you're typing
- You want to avoid inheritance coupling
- Designing plugin systems

**When to use ABC:**
- You own the base class and want to enforce implementation
- You need class-level behavior (abstract class methods)
- You want a clear "is-a" relationship

---

## TypeVar and Generics

### Basic Generics

```python
from typing import TypeVar, Generic

T = TypeVar("T")

class Stack(Generic[T]):
    def __init__(self) -> None:
        self._items: list[T] = []

    def push(self, item: T) -> None:
        self._items.append(item)

    def pop(self) -> T:
        return self._items.pop()

# Type-safe usage
int_stack: Stack[int] = Stack()
int_stack.push(1)      # OK
int_stack.push("no")   # Error: expected int
```

### Constrained TypeVars

```python
from typing import TypeVar

# Constrained to specific types
Number = TypeVar("Number", int, float)

def add(a: Number, b: Number) -> Number:
    return a + b

# Bounded by a type hierarchy
from typing import SupportsFloat
Comparable = TypeVar("Comparable", bound=SupportsFloat)
```

### Generic Functions

```python
from typing import TypeVar, Sequence

T = TypeVar("T")

def first(items: Sequence[T]) -> T | None:
    return items[0] if items else None

# Inferred correctly
result = first([1, 2, 3])    # result: int | None
result = first(["a", "b"])   # result: str | None
```

---

## When to Use Type Hints

**Strongly recommended for:**
- Public APIs and library code
- Complex data transformations
- Functions with multiple optional parameters
- Return types that aren't obvious
- Team projects with multiple developers
- Code that will be maintained for years

**Example: Production-ready signature**

```python
def process_user_events(
    events: Sequence[dict[str, Any]],
    *,
    batch_size: int = 100,
    callback: Callable[[dict[str, Any]], None] | None = None,
) -> tuple[list[dict[str, Any]], list[Exception]]:
    """
    Process events in batches, returning (processed, errors).

    Args:
        events: Raw event dictionaries from the queue
        batch_size: Number of events per batch
        callback: Optional function called after each batch

    Returns:
        Tuple of processed events and any errors encountered
    """
    ...
```

---

## When NOT to Use Type Hints

**Skip or simplify when:**

1. **Quick scripts and prototypes** — Type hints add overhead when exploring
2. **Simple helper functions** — `def add(a, b): return a + b` is fine
3. **Heavy duck typing with dynamic dispatch** — `Any` defeats the purpose
4. **Overly complex generics** — If the type is harder to read than the code
5. **When `Any` is the honest answer** — Don't fake precision you don't have

```python
# Overkill
def square(x: int) -> int:
    return x * x

# This is fine — the name says everything
def sq(x):
    return x * x

# Don't do this — Any everywhere is worse than no hints
def mystery(data: Any) -> Any:
    return do_something(data)
```

---

## Production Checklist

- [ ] **Enable mypy in CI** — Run `mypy --strict` or gradually increase strictness
- [ ] **Type all public APIs** — Every function in `__all__` should have full annotations
- [ ] **Use `py.typed` marker** — Signal to downstream tools that your package is typed
- [ ] **Avoid `# type: ignore`** — If needed, add a comment explaining why
- [ ] **Type your exceptions** — Exception classes should have typed attributes
- [ ] **Document `Any` usage** — If you use `Any`, explain why in a comment
- [ ] **Type external stubs** — For third-party libraries without types
- [ ] **Review type complexity** — If a type annotation is harder to read than the code, simplify
- [ ] **Use `TypeAlias` for complex types** — Name complex types for clarity
- [ ] **Test with `--strict`** — Catch gradual type drift

```bash
# CI configuration
mypy --strict --disallow-untyped-defs --disallow-incomplete-defs src/
```

---

## Maturity Levels

| Level | What It Looks Like | Indicators |
|-------|-------------------|------------|
| **Beginner** | No type hints at all | Relies on docstrings only |
| **Basic** | Return types only | `def greet() -> str:` |
| **Intermediate** | Full annotations | Parameters + return types everywhere |
| **Advanced** | Generics, Protocols, TypeVar | Custom generic classes, structural typing |
| **Expert** | mypy --strict in CI, 0 errors | Overloaded functions, complex type guards |

### Progression Path

1. **Start:** Add return types to existing functions (zero-risk)
2. **Then:** Add parameter types to public APIs
3. **Then:** Enable mypy with basic settings
4. **Then:** Gradually tighten strictness
5. **Finally:** Use generics and protocols for complex abstractions

---

## Common Myths

**Myth: "Type hints slow down Python"**
> Reality: Zero runtime cost. Annotations are stored in `__annotations__` but never interpreted by the runtime. Python ignores them completely.

**Myth: "Type hints replace tests"**
> Reality: Type hints catch *type* errors, not *logic* errors. A function can be perfectly typed and still return wrong values.

**Myth: "Duck typing and type hints are contradictory"**
> Reality: Protocols *are* duck typing with type safety. You get the flexibility of Python with the safety of static analysis.

**Myth: "We need to type everything from day one"**
> Reality: Incremental adoption is the standard approach. Start with public APIs, work inward.

**Myth: "Type hints make Python verbose"**
> Reality: Well-typed code is *more concise* because you don't need docstrings explaining types, and IDEs provide better autocomplete.

---

## One-Minute Revision

- Type hints are **optional annotations** for documentation, IDE support, and static checking
- **Basic types:** `int`, `str`, `float`, `bool`, `None`
- **Collections:** `list[str]`, `dict[str, int]`, `tuple[int, ...]`, `set[int]`
- **Optional:** `str | None` (or `Optional[str]` before 3.10)
- **Callable:** `Callable[[int, str], bool]`
- **Protocol:** Structural subtyping — type safety without inheritance
- **TypeVar/Generic:** Create type-safe generic classes and functions
- Use for **public APIs, complex code, team projects**
- Skip for **quick scripts, simple helpers, exploratory code**
- **No runtime cost** — pure static analysis tool
- Start with mypy in CI, gradually increase strictness
- Type hints **complement, not replace** tests
