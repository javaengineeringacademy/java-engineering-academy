# Python Type Hints Quiz

## Question 1 (MCQ - Basic Types)
What is the correct way to annotate a function that returns a list of strings?

- A) `def get_names() -> list:`
- B) `def get_names() -> list[str]:`
- C) `def get_names() -> List:`
- D) `def get_names() -> Array(str):`

**Answer: B**
**Explanation:** Since Python 3.9, you can use built-in types directly in type hints (`list[str]`). Before 3.9, you needed `from typing import List` and `List[str]`. Option A doesn't specify the element type. `Array` is not a standard type hint — that's from NumPy.

---

## Question 2 (MCQ - Optional vs Union)
What does `Optional[str]` mean?

- A) A string that might be `None`
- B) A string with default value `None`
- C) An optional parameter that defaults to `None`
- D) Equivalent to `str | None`

**Answer: D**
**Explanation:** `Optional[str]` is shorthand for `Union[str, None]`, meaning the value can be `str` or `None`. It does NOT mean the parameter is optional — that's controlled by default values in the function signature. `Optional[str]` and `str | None` are semantically identical.

---

## Question 3 (Code Output - TypeVar)
What is the output of this code?

```python
from typing import TypeVar, List

T = TypeVar('T')

def first(items: List[T]) -> T:
    return items[0]

result: int = first([1, 2, 3])
print(type(result))
result2: str = first(["a", "b"])
print(type(result2))
```

A) `<class 'int'>` then `<class 'str'>`
B) `<class 'Any'>` then `<class 'Any'>`
C) Error: TypeVar cannot be used with annotations
D) `<class 'T'>` then `<class 'T'>`

**Answer: A**
**Explanation:** `TypeVar('T')` makes the function generic. When called with `[1, 2, 3]`, `T` is inferred as `int`, so the return type is `int`. When called with `["a", "b"]`, `T` is inferred as `str`. The actual runtime type is preserved — type hints don't affect execution, but tools like mypy use them to verify correctness.

---

## Question 4 (MCQ - Protocol)
What does `typing.Protocol` enable?

- A) Runtime type enforcement
- B) Structural subtyping — a class satisfies a Protocol if it has the right attributes/methods, without explicit inheritance
- C) Automatic interface generation
- D) Performance optimization through protocol buffers

**Answer: B**
**Explanation:** `Protocol` enables structural typing (duck typing with type safety). A class doesn't need to inherit from a Protocol to satisfy it — it just needs to have the matching method signatures. This is different from nominal typing (ABCs) where explicit inheritance is required. Useful for libraries that want to accept "anything with a `read()` method" without requiring a specific base class.

---

## Question 5 (Code Output - Generic)
What is the output of this code?

```python
from typing import Generic, TypeVar

T = TypeVar('T')

class Box(Generic[T]):
    def __init__(self, value: T) -> None:
        self.value = value

    def get(self) -> T:
        return self.value

int_box: Box[int] = Box(42)
str_box: Box[str] = Box("hello")

print(int_box.get() + 8)
print(str_box.get() + " world")
```

A) `50` then `hello world`
B) Error: Generic[T] cannot be instantiated
C) `428` then `helloworld`
D) Error: T + int is undefined

**Answer: A**
**Explanation:** `Box[int]` means `T` is `int`, so `self.value` is 42 and `get()` returns `int`. `42 + 8 = 50`. `Box[str]` means `T` is `str`, so `get()` returns `"hello"`. `"hello" + " world" = "hello world"`. Generic classes preserve type information through parameterization.

---

## Question 6 (Bug Finding - Overloads)
This overloaded function has a bug. Find it:

```python
from typing import overload

@overload
def process(value: int) -> str: ...

@overload
def process(value: str) -> int: ...

def process(value):
    if isinstance(value, int):
        return str(value * 2)
    else:
        return len(value) * 2

result1: str = process(5)     # Line A
result2: int = process("hi")  # Line B
```

A) Line A fails: `process(5)` returns `"10"` (str), but declared as `str` — this is correct
B) Line B fails: `process("hi")` returns `4` (int), but the return type annotation is wrong
C) The `...` in overload stubs causes a syntax error
D) There's no bug — the overloads work correctly

**Answer: D**
**Explanation:** There is no bug. `process(5)` returns `str(value * 2)` = `"10"`, matching the `int -> str` overload. `process("hi")` returns `len("hi") * 2` = `4`, matching the `str -> int` overload. The overload stubs (with `...`) are just annotations — the actual implementation below handles both cases. Everything aligns correctly.

---

## Question 7 (Bug Finding - Mutable Default)
This type hint hides a subtle runtime bug. What is it?

```python
from typing import List

def add_item(item: str, items: List[str] = []) -> List[str]:
    items.append(item)
    return items

result1 = add_item("first")
result2 = add_item("second")
print(result1)
print(result2)
```

A) Type error: `List[str]` should be `list[str]`
B) `result1` and `result2` are the same list due to mutable default — both print `['first', 'second']`
C) The function should return `None`
D) No bug — the output is correct

**Answer: B**
**Explanation:** Default mutable arguments are shared across all calls. `items=[]` creates the list once, and every call that uses the default appends to the same list. `result1` points to that shared list (now `['first', 'second']`), and `result2` points to the same list. The fix is `items: List[str] | None = None` with `if items is None: items = []` inside. Type hints don't prevent this runtime behavior.

---

## Question 8 (Scenario - Generic Constraints)
You're building a data pipeline that processes different numeric types. Which approach is best?

```python
# Option A
T = TypeVar('T', int, float, complex)

# Option B
T = TypeVar('T', bound=Number)

# Option C
def process(value: Union[int, float, complex]) -> float: ...
```

- A) Option A — restricts T to exactly those three types
- B) Option B — allows any subclass of Number (Decimal, Fraction, etc.)
- C) Option C — no generics, just a union
- D) Option A and Option B are equivalent

**Answer: B**
**Explanation:** `TypeVar('T', bound=Number)` accepts any type that is a subclass of `Number`, including `Decimal`, `Fraction`, and custom numeric types. This is more extensible than Option A which only allows exactly `int`, `float`, `complex`. Option C loses the generic relationship between input and output types. Use `bound` when you want "anything that behaves like a number" — it's the most Pythonic and extensible approach.

---

## Question 9 (Architecture Decision - Protocol vs ABC)
You're designing an interface for plugins. Some plugins come from third-party code you can't modify. Which approach should you use?

- A) Abstract Base Class — forces third-party code to inherit from your class
- B) Protocol — third-party classes just need matching methods, no inheritance required
- C) Type aliases — simpler and works everywhere
- D) Duck typing without any type hints

**Answer: B**
**Explanation:** `Protocol` is the right choice when you can't control the implementation side. Third-party classes don't need to know about your Protocol — they just need to have the right methods. If you used an ABC, you'd require inheritance, which you can't enforce on external code. Protocols give you structural typing: "I don't care what class this is, I care that it has a `process()` method." This is the Liskov Substitution Principle at work.

---

## Question 10 (Architecture Decision - Gradual Typing)
Your team is adopting type hints on a large, existing codebase. What's the recommended strategy?

- A) Add types to everything at once — consistency matters most
- B) Start with public APIs and interfaces, then gradually add types inward as code is modified
- C) Only add types to new code, never touch old code
- D) Type hints aren't worth it on existing projects

**Answer: B**
**Explanation:** Gradual typing is the recommended approach. Start with public-facing APIs (they benefit most from documentation and validation), then add types to internal code as you touch it. This avoids a massive one-time effort, reduces merge conflicts, and lets the team build type-hinting muscle memory. Tools like `mypy --ignore-missing-imports` help you adopt incrementally. Type hints on existing code pay dividends in bug prevention, IDE support, and onboarding — it's always worth it.
