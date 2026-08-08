# Module 06: Type Hints

> "Type hints are not about making Python typed. They're about making Python code *communicate* better."

## Why Type Hints Exist

Every Python codebase eventually reaches a point where functions become hard to understand, bugs hide in unexpected type conversions, and new team members struggle to figure out what parameters a function expects. Type hints solve this by providing optional annotations that describe the expected types of variables, function parameters, and return values. Without them, you'd rely on documentation that quickly becomes outdated, or worse, reading implementation details to understand interfaces.

Without type hints, you'd spend hours debugging type-related errors that a static checker could catch in seconds, and IDE support would be limited to basic syntax highlighting. That's why type hints exist — they bridge the gap between Python's dynamic nature and the need for clear, maintainable, and toolable code in production teams.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Public APIs, complex code, team projects, long-term maintenance | Quick scripts, simple helpers |
| When NOT to use | Don't over-annotate simple code; don't use `Any` everywhere | Skip for trivial functions |
| Alternatives | Docstrings for documentation, runtime checks | Duck typing without annotations |
| Production Examples | Libraries, web APIs, data pipelines | Prototypes, throwaway scripts |
| Common Mistakes | Type hints that lie, over-using `Any`, ignoring mypy errors | Keep annotations accurate; run mypy in CI |

## What You'll Learn

By the end of this module, you'll be able to:

- Annotate functions and variables with proper type hints
- Use static type checkers (mypy, pyright) to catch bugs before runtime
- Use type hints for better IDE support and code documentation
- Understand the difference between gradual typing and strict typing
- Apply type hints effectively in team environments

---

## Interview Questions

### Q1: What is the difference between `List[int]` and `list[int]`?
**Answer:** `List[int]` is from typing module (Python 3.5+). `list[int]` is built-in generics (Python 3.9+). Both work, prefer built-in for Python 3.9+.

### Q2: What is type erasure in Python?
**Answer:** Type hints are erased at runtime. Python doesn't enforce types - they're for static analysis only. mypy and pyright check types at development time.

### Q3: What is the difference between `Optional[X]` and `X | None`?
**Answer:** Both mean the same thing. `Optional[X]` is older syntax (typing module). `X | None` is newer syntax (Python 3.10+). Prefer the newer syntax.

### Q4: What is a Protocol?
**Answer:** A Protocol defines a structural subtyping interface. Classes don't need to explicitly inherit from Protocol - they just need to implement the methods.

### Q5: What is the difference between `Any` and `object`?
**Answer:** `Any` disables type checking (accepts anything). `object` is the base type (accepts anything but requires casting). Use `Any` sparingly.

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

## Production Incidents

### Incident 1: Type Hint Lying About Return Value

**Problem:** API returned `None` but client code expected `User` object
**Cause:** Function signature said `-> User` but could return `None`
**Impact:** TypeError in production when accessing user attributes
**Detection:** Error monitoring caught NoneType exceptions
**Solution:**
```python
# BAD: Lying type hint
def get_user(user_id: int) -> User:
    return user_registry.get(user_id)  # Can return None!

# GOOD: Accurate type hint
def get_user(user_id: int) -> User | None:
    return user_registry.get(user_id)
```
**Prevention:** Use `Optional` or `X | None` for nullable returns; run `mypy --strict` in CI

### Incident 2: Generic Type Causing Runtime Error

**Problem:** `Stack[int]` accepted string at runtime despite type hint
**Cause:** Python doesn't enforce generic types at runtime; only static checkers catch this
**Impact:** Type mismatch caused TypeError in production
**Detection:** Runtime error in data processing pipeline
**Solution:**
```python
# Type hints don't enforce at runtime
stack: Stack[int] = Stack()
stack.push("oops")  # No error at runtime!

# Add runtime validation
class Stack(Generic[T]):
    def push(self, item: T) -> None:
        if not isinstance(item, self._item_type):
            raise TypeError(f"Expected {self._item_type}, got {type(item)}")
        self._items.append(item)
```
**Prevention:** Run mypy in CI; add runtime validation for critical paths; use `TypeGuard` for type narrowing

### Incident 3: Complex Type Annotation Slowing IDE

**Problem:** IDE autocompletion became extremely slow after adding complex types
**Cause:** Deeply nested `Union` types with `Callable` caused IDE type checker to hang
**Impact:** Developer productivity dropped; 10-second autocomplete delays
**Detection:** Developer complaints about IDE performance
**Solution:**
```python
# BAD: Complex nested type
def process(data: dict[str, list[tuple[int, str] | None]]) -> dict[str, Any]: ...

# GOOD: Named type alias
from typing import TypeAlias
DataRecord: TypeAlias = dict[str, list[tuple[int, str] | None]]

def process(data: DataRecord) -> dict[str, Any]: ...
```
**Prevention:** Use `TypeAlias` for complex types; keep annotations simple; test IDE performance

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

## References
- PEP 484: Type Hints
- PEP 604: Union Types (X | Y)
- PEP 585: Type Hinting Generics In Standard Collections
- PEP 695: Type Parameter Syntax
- mypy Documentation
- pyright Documentation
- Typing PEPs: https://peps.python.org/topic/typing/

## Related Topics

- [02-oop](../02-oop/) - Protocols and ABCs
- [16-best-practices](../16-best-practices/) - Type hints in best practices
- [18-senior](../18-senior/) - Type safety in production systems

## Version Validation
- Verified against: Python 3.12+ (PEP 695 syntax), Python 3.10+ (X | Y syntax)

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Type hint lying about return value | `mypy --strict` in CI | Use `X | None` for nullable returns; run `mypy` on every commit |
| Generic type accepted at runtime despite wrong type | `mypy` + runtime validation | Add runtime checks for critical paths; use `TypeGuard` for narrowing |
| Complex type annotation slowing IDE | Simplify with `TypeAlias` | Name complex types; keep annotations readable over precise |
| `Any` used everywhere defeating purpose | `mypy --disallow-any-expr` | Replace `Any` with specific types; document unavoidable `Any` usage |
| `TypeVar` not inferring correctly | Check bound/constraint usage | Use `bound=` for type hierarchy; use constraints for union of types |

## Code Review Checklist

- [ ] All public APIs have full type annotations (parameters + return)
- [ ] `Optional` or `X | None` used for nullable returns
- [ ] `Any` avoided; documented when unavoidable
- [ ] `TypeAlias` used for complex nested types
- [ ] `mypy --strict` passes in CI with zero errors
- [ ] `py.typed` marker included for library packages
- [ ] No `# type: ignore` without explanation comment

## Architecture Considerations

Type hints bridge Python's dynamic nature with static analysis tooling. They enable IDE autocompletion, catch bugs at development time, and serve as living documentation. Protocols enable structural subtyping without inheritance coupling, aligning with Python's duck typing philosophy while providing type safety.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Protocol for structural typing | Plugin systems, duck-typed interfaces | Flexible but no runtime enforcement |
| TypedDict for structured dicts | API responses, config data | Clear structure but verbose |
| `TypeVar` with bounds | Generic containers | Type-safe but adds complexity |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Type hint revealing internal structure | Information leakage in API | Use `TypeAlias` to control public type exposure |
| `TypeGuard` logic bypassing validation | Unsafe type narrowing | Verify `TypeGuard` implementations with tests |
| `cast()` hiding type mismatch | Runtime `TypeError` in production | Minimize `cast()` usage; prefer runtime checks |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.10+ | `X \| Y` union syntax | Replace `Union[X, Y]` with `X \| Y` |
| Python 3.12+ | PEP 695 type parameter syntax | Replace `TypeVar` boilerplate with inline syntax |
| Python 3.13+ | Type defaults for generics | Use `class Stack[T=int]` for default type parameters |


