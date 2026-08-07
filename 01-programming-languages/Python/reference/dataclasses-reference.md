# Python Dataclasses Reference

## What are Dataclasses?

Dataclasses are a decorator and function for automatically generating special methods like `__init__`, `__repr__`, `__eq__`, etc., based on class annotations. They reduce boilerplate code for simple data containers.

## Why does Dataclasses matter?

Understanding dataclasses helps you:
- Write cleaner data classes
- Reduce boilerplate code
- Create immutable objects
- Generate hashes for dictionary keys

---

## 1. Basic Dataclass

```python
from dataclasses import dataclass

@dataclass
class Point:
    x: int
    y: int

# Usage
p = Point(1, 2)
print(p)  # Point(x=1, y=2)
print(p.x, p.y)  # 1 2
```

---

## 2. Generated Methods

```python
from dataclasses import dataclass

@dataclass
class Person:
    name: str
    age: int

# __init__
p = Person("Alice", 30)

# __repr__
print(p)  # Person(name='Alice', age=30)

# __eq__
p1 = Person("Alice", 30)
p2 = Person("Alice", 30)
print(p1 == p2)  # True

# __lt__, __le__, __gt__, __ge__ (with order=True)
@dataclass(order=True)
class SortedPoint:
    x: int
    y: int

p1 = SortedPoint(1, 2)
p2 = SortedPoint(3, 4)
print(p1 < p2)  # True
```

---

## 3. Field Options

```python
from dataclasses import dataclass, field

@dataclass
class Student:
    name: str
    age: int
    grades: list = field(default_factory=list)
    id: int = field(init=False, default=0)
    _private: str = field(repr=False, default="hidden")

s = Student("Alice", 30)
print(s)  # Student(name='Alice', age=30, grades=[], id=0)
```

---

## 4. Frozen Dataclasses

```python
from dataclasses import dataclass

@dataclass(frozen=True)
class ImmutablePoint:
    x: int
    y: int

p = ImmutablePoint(1, 2)
# p.x = 3  # AttributeError: cannot assign to field 'x'

# Can be used as dictionary key
d = {p: "point"}
```

---

## 5. Dataclass with Slots

```python
from dataclasses import dataclass

@dataclass(slots=True)
class Point:
    x: int
    y: int

p = Point(1, 2)
print(p.x, p.y)
```

---

## 6. Inheritance

```python
from dataclasses import dataclass

@dataclass
class Animal:
    name: str

@dataclass
class Dog(Animal):
    breed: str

d = Dog("Rex", "Labrador")
print(d)  # Dog(name='Rex', breed='Labrador')
```

---

## 7. Post-init

```python
from dataclasses import dataclass

@dataclass
class Rectangle:
    width: float
    height: float
    
    def __post_init__(self):
        self.area = self.width * self.height

r = Rectangle(5, 10)
print(r.area)  # 50
```

---

## 8. Convert to Dict

```python
from dataclasses import dataclass, asdict, astuple

@dataclass
class Point:
    x: int
    y: int

p = Point(1, 2)
print(asdict(p))  # {'x': 1, 'y': 2}
print(astuple(p))  # (1, 2)
```

---

## One-Minute Revision Table

| Feature | Description | Example |
|---------|-------------|---------|
| **@dataclass** | Create dataclass | `@dataclass class Point:` |
| **field** | Field options | `field(default_factory=list)` |
| **frozen** | Immutable | `@dataclass(frozen=True)` |
| **order** | Comparison methods | `@dataclass(order=True)` |
| **slots** | Use __slots__ | `@dataclass(slots=True)` |
| **init** | Generate __init__ | Default True |
| **repr** | Generate __repr__ | Default True |
| **eq** | Generate __eq__ | Default True |
| **asdict** | Convert to dict | `asdict(instance)` |
| **astuple** | Convert to tuple | `astuple(instance)` |

---

## Common Mistakes

### 1. Mutable Default Values

```python
# WRONG
@dataclass
class Point:
    x: int
    y: int
    tags: list = []  # Shared between instances

# RIGHT
@dataclass
class Point:
    x: int
    y: int
    tags: list = field(default_factory=list)
```

### 2. Forgetting frozen=True

```python
# WRONG (if you want immutable)
@dataclass
class Point:
    x: int
    y: int

p = Point(1, 2)
p.x = 3  # Works but shouldn't

# RIGHT
@dataclass(frozen=True)
class Point:
    x: int
    y: int

p = Point(1, 2)
# p.x = 3  # AttributeError
```

### 3. Not Using field() for Complex Defaults

```python
# WRONG
@dataclass
class Config:
    items: dict = {}  # Shared

# RIGHT
@dataclass
class Config:
    items: dict = field(default_factory=dict)
```

---

## Production Notes

1. **Use dataclasses for simple data containers** - Reduce boilerplate
2. **Use `frozen=True` for immutable objects** - Prevent accidental modification
3. **Use `order=True` for comparison** - When objects need sorting
4. **Use `slots=True` for memory efficiency** - Reduces instance size
5. **Use `field(default_factory=...)` for mutable defaults** - Prevent shared state
6. **Use `__post_init__` for validation** - After initialization
7. **Use `asdict` for serialization** - Convert to dictionary
8. **Use inheritance carefully** - Field order matters
9. **Use `init=False` for computed fields** - Not part of constructor
10. **Use `repr=False` for sensitive data** - Hide from repr

---

## Further Reading

- Python documentation on dataclasses module
- PEP 557 - Data Classes
- dataclasses documentation
