# Python Knowledge Atoms — Core Concepts Every Python Developer Must Internalize

## Why These Concepts Matter

Every Python application, from a small automation script to a large production service, relies on foundational concepts that shape how the language works under the hood. These atomic ideas — duck typing, the GIL, reference counting, the data model — aren't just trivia. They're the difference between writing code that works and writing code that performs, scales, and maintains itself.

Without internalizing these atoms, you'd have to rediscover Python's quirks through production incidents and debugging sessions. That's why these concepts exist — they form the mental model that lets you read Python source code like a native speaker reads prose, and design systems that leverage the language's strengths rather than fighting them.

## What You'll Learn

By the end of this module, you'll be able to:

- Design around interfaces (duck typing) instead of inheritance hierarchies
- Choose between EAFP and LBYL patterns based on context
- Understand GIL limitations and when to use multiprocessing vs threading
- Manage memory effectively through reference counting and garbage collection
- Leverage the data model (dunder methods) to make custom objects integrate with Python syntax
- Use ABCs and Protocols to define and enforce contracts

---

## Duck Typing

> *"If it walks like a duck and quacks like a duck, it's a duck."*

Python doesn't care about types. It cares about **behavior**. If an object has the right methods and attributes, it works — regardless of its class hierarchy.

```python
class Duck:
    def quack(self):
        return "Quack!"

class Person:
    def quack(self):
        return "I'm pretending to be a duck!"

def make_it_quack(thing):
    print(thing.quack())  # Works for both — no type check needed

make_it_quack(Duck())     # Quack!
make_it_quack(Person())   # I'm pretending to be a duck!
```

**Why it matters:** Duck typing pushes you to design around *interfaces* (what something can do) rather than *inheritance* (what something is). This is the foundation of Python's flexibility.

**Practical rule:** If your code does `isinstance()` checks frequently, you're fighting Python's nature. Refactor toward protocol-based design.

---

## EAFP vs LBYL

Two philosophies for handling uncertainty:

| Approach | Stands For | Style |
|----------|-----------|-------|
| **LBYL** | Look Before You Leap | Check first, act second |
| **EAFP** | Easier to Ask Forgiveness than Permission | Act first, handle failure |

```python
# LBYL — C-like approach
if key in dictionary:
    value = dictionary[key]
else:
    value = default

# EAFP — Pythonic approach
try:
    value = dictionary[key]
except KeyError:
    value = default
```

**When to use which:**
- **EAFP** is preferred in Python — it's faster in the happy path and handles edge cases more cleanly
- **LBYL** is better when failure is expensive or when you need atomicity (race conditions where the state can change between check and action)

```python
# LBYL is better here — file could be deleted between open() and write()
import os
if os.path.exists(filename):
    os.remove(filename)  # File could vanish here!
```

---

## GIL (Global Interpreter Lock)

The GIL is a mutex that allows **only one thread to execute Python bytecode at a time** within a single process.

```python
import threading
import time

counter = 0

def count():
    global counter
    for _ in range(1_000_000):
        counter += 1  # NOT atomic — GIL doesn't save you here

t1 = threading.Thread(target=count)
t2 = threading.Thread(target=count)
t1.start(); t2.start()
t1.join(); t2.join()

print(counter)  # Often less than 2,000,000 — race condition!
```

**Key facts:**
- GIL exists because CPython's memory management is not thread-safe
- It affects **CPU-bound** tasks — threads can't parallelize computation
- It does **not** affect **I/O-bound** tasks — threads release the GIL during I/O waits
- `multiprocessing` bypasses the GIL (separate processes, separate interpreters)
- Python 3.13+ introduces experimental free-threaded mode (no GIL)

```python
# CPU-bound: use multiprocessing
from multiprocessing import Pool
with Pool(4) as p:
    results = p.map(cpu_intensive_function, data)

# I/O-bound: threading is fine
import urllib.request
with ThreadPoolExecutor(max_workers=10) as executor:
    results = executor.map(urllib.request.urlopen, urls)
```

---

## Reference Counting

Every object in Python has a reference count — the number of names or containers pointing to it.

```python
import sys

a = []          # refcount of the list = 1 (a points to it)
b = a           # refcount = 2 (a and b both point to it)
print(sys.getrefcount(b))  # 3 (a, b, and the getrefcount argument)

del b           # refcount = 2 (b is gone)
del a           # refcount = 0 → object is deallocated immediately
```

**How reference counting works:**
- Assignment increases refcount: `b = a` → +1
- `del` decreases refcount: `del a` → -1
- Passing as function argument: +1 (for duration of call)
- Adding to container: +1
- When refcount hits 0, memory is freed **immediately**

**The edge case:** Circular references keep refcounts above 0 forever. That's where garbage collection comes in.

---

## Garbage Collection

Python's garbage collector handles **cyclic references** that reference counting alone cannot clean up.

```python
class Node:
    def __init__(self):
        self.parent = None
        self.children = []

# Create a cycle: parent → child → parent
parent = Node()
child = Node()
parent.children.append(child)
child.parent = parent

del parent
del child  # refcount never hits 0 — circular reference!
# GC detects and breaks the cycle
```

**GC mechanics:**
- Python uses a **generational** garbage collector (3 generations)
- Generation 0: most frequently checked (new objects)
- Generation 1: survived one GC cycle
- Generation 2: survived two GC cycles (long-lived objects)
- You can tune it: `import gc; gc.set_threshold(700, 10, 10)`

```python
import gc

# Disable GC (useful for performance-critical sections)
gc.disable()

# Manually trigger collection
gc.collect()

# Find uncollectable objects
gc.garbage  # List of objects the GC couldn't free
```

**Practical advice:** Don't disable GC in production. Use `__del__` sparingly — it makes objects uncollectable in some edge cases. Prefer context managers and explicit cleanup.

---

## Name Mangling

Names starting with `__` (double underscore, no trailing underscore) are mangled to `_ClassName__name` to avoid collisions in subclasses.

```python
class Parent:
    def __init__(self):
        self.__secret = "hidden"

class Child(Parent):
    def __init__(self):
        super().__init__()
        self.__secret = "overridden"  # Different attribute!

p = Parent()
c = Child()

print(p._Parent__secret)   # "hidden" — mangled name
print(c._Child__secret)    # "overridden" — different mangled name
print(c.__secret)          # AttributeError — __secret doesn't exist
```

**When to use it:**
- Preventing accidental name collisions in large inheritance hierarchies
- Signaling "this is internal, don't touch" (convention, not enforcement)

**When NOT to use it:**
- Testing becomes harder (you need to know the mangled name)
- It's not true privacy — `_ClassName__name` is accessible
- Single underscore `_name` is usually sufficient for "private by convention"

---

## Descriptor Protocol

Any object implementing `__get__`, `__set__`, or `__delete__` is a descriptor. This is how properties, classmethods, staticmethods, and slots work internally.

```python
class Validated:
    """A descriptor that validates values on assignment."""
    def __init__(self, min_value=None, max_value=None):
        self.min_value = min_value
        self.max_value = max_value

    def __set_name__(self, owner, name):
        self.name = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)

    def __set__(self, obj, value):
        if self.min_value is not None and value < self.min_value:
            raise ValueError(f"{self.name} must be >= {self.min_value}")
        if self.max_value is not None and value > self.max_value:
            raise ValueError(f"{self.name} must be <= {self.max_value}")
        obj.__dict__[self.name] = value

class Account:
    balance = Validated(min_value=0)
    interest_rate = Validated(min_value=0, max_value=1.0)

    def __init__(self, balance, rate):
        self.balance = balance
        self.interest_rate = rate

# a = Account(-100, 0.05)  # ValueError: balance must be >= 0
# a = Account(100, 1.5)    # ValueError: interest_rate must be <= 1.0
a = Account(100, 0.05)     # Works
```

**Types of descriptors:**
- **Data descriptor:** Implements `__set__` or `__delete__` — takes precedence over instance `__dict__`
- **Non-data descriptor:** Only implements `__get__` — can be overridden by instance attributes

This is why `classmethod` and `staticmethod` work — they're non-data descriptors, so instance attributes can shadow them (though you shouldn't).

---

## Data Model

Python's data model is the set of **dunder methods** (double underscore / "magic" methods) that let you define how your objects interact with Python's syntax.

```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return f"Vector({self.x!r}, {self.y!r})"

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)

    def __abs__(self):
        return (self.x ** 2 + self.y ** 2) ** 0.5

    def __bool__(self):
        return bool(abs(self))

    def __hash__(self):
        return hash((self.x, self.y))

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

v1 = Vector(2, 3)
v2 = Vector(1, 1)
print(v1 + v2)       # Vector(3, 4) — __add__
print(abs(v1))        # 3.605... — __abs__
print(bool(Vector(0,0)))  # False — __bool__
```

**The key dunder categories:**

| Category | Methods | Purpose |
|----------|---------|---------|
| Construction | `__init__`, `__new__`, `__del__` | Object lifecycle |
| Representation | `__repr__`, `__str__`, `__format__` | String output |
| Comparison | `__eq__`, `__ne__`, `__lt__`, `__le__`, `__gt__`, `__ge__` | Ordering and equality |
| Arithmetic | `__add__`, `__sub__`, `__mul__`, etc. | Math operations |
| Container | `__getitem__`, `__setitem__`, `__len__`, `__contains__` | Collection behavior |
| Callable | `__call__` | Make instances callable |
| Context | `__enter__`, `__exit__` | `with` statement support |
| Attribute | `__getattr__`, `__setattr__`, `__delattr__` | Attribute access control |

**Mental model:** Dunder methods are how you teach Python what your objects *mean* when used with built-in syntax.

---

## Abstract Base Classes (ABCs)

ABCs define **interfaces** that concrete classes must implement. They're Python's way of enforcing contracts.

```python
from abc import ABC, abstractmethod

class PaymentProcessor(ABC):
    @abstractmethod
    def charge(self, amount: float) -> bool:
        """Charge the given amount. Return True if successful."""
        pass

    @abstractmethod
    def refund(self, transaction_id: str) -> bool:
        """Refund a transaction. Return True if successful."""
        pass

    def process_payment(self, amount: float) -> bool:
        """Template method — concrete, not abstract."""
        print(f"Processing ${amount:.2f}...")
        success = self.charge(amount)
        if success:
            print("Payment successful!")
        return success

# StripeProcessor(PaymentProcessor):  # Won't work if you forget charge() or refund()
#     pass  # TypeError: Can't instantiate abstract class

class StripeProcessor(PaymentProcessor):
    def charge(self, amount):
        return True  # Simplified
    def refund(self, transaction_id):
        return True  # Simplified

stripe = StripeProcessor()
stripe.process_payment(29.99)  # Works — template method + concrete implementations
```

**When to use ABCs:**
- You're defining a **plugin system** or **strategy pattern**
- You want **explicit enforcement** that subclasses implement required methods
- You're building a **library** and need to document the contract

**When NOT to use them:**
- You're duck typing — ABCs are the opposite of duck typing
- You're over-engineering — not every interface needs an ABC
- You're using them just for type hints — `Protocol` is lighter weight

```python
# Python 3.8+ Protocol — structural subtyping (duck typing with type checking)
from typing import Protocol

class Drawable(Protocol):
    def draw(self) -> None: ...

class Circle:
    def draw(self) -> None:  # Satisfies Drawable — no inheritance needed
        print("○")

def render(obj: Drawable) -> None:
    obj.draw()

render(Circle())  # Type checker approves — Circle has draw()
```

---

## Putting It All Together

These concepts aren't isolated. They interact:

1. **Duck typing + ABCs** → Define protocols, enforce where needed
2. **EAFP + exception handling** → Write resilient code
3. **GIL + reference counting + GC** → Understand memory and concurrency
4. **Descriptor protocol + data model** → Build powerful abstractions
5. **Name mangling + descriptors** → Control attribute access and visibility

Master these atoms, and you'll read Python source code like a native speaker reads prose.

---

## Interview Questions

### Q1: What is duck typing and why does Python use it?
**Answer:** Duck typing means "if it walks like a duck and quacks like a duck, it's a duck." Python checks object behavior rather than type. This enables flexibility and polymorphism without inheritance.

### Q2: Explain the GIL and its impact on multithreading.
**Answer:** The Global Interpreter Lock prevents multiple threads from executing Python bytecodes simultaneously. This means CPU-bound threads don't parallelize, but I/O-bound threads still benefit from concurrency.

### Q3: What is the difference between `is` and `==`?
**Answer:** `is` checks identity (same object in memory), `==` checks equality (same value). Use `is` for None checks, `==` for value comparison.

### Q4: How does Python's garbage collector work?
**Answer:** Python uses reference counting as primary mechanism and a cyclic garbage collector for reference cycles. The gc module manages generational collection.

### Q5: What is the data model and why is it important?
**Answer:** The data model defines how Python objects behave through special methods (__init__, __len__, __getitem__). It enables operator overloading, context managers, and iteration.

---

## Production Checklist

- [ ] Use duck typing for flexible interfaces; avoid unnecessary `isinstance()` checks
- [ ] Apply EAFP pattern for cleaner error handling; use LBYL only when atomicity matters
- [ ] Understand GIL limitations; use multiprocessing for CPU-bound tasks
- [ ] Monitor reference counts with `sys.getrefcount()` during debugging
- [ ] Tune garbage collection thresholds for long-running applications
- [ ] Use single underscore `_name` for private-by-convention; avoid `__name` unless needed
- [ ] Implement descriptor protocol for custom attribute access
- [ ] Define `__repr__` and `__eq__` for all custom classes
- [ ] Use ABCs or Protocols for plugin systems and formal interfaces
- [ ] Profile memory and concurrency bottlenecks before optimizing

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Understands basic duck typing, EAFP vs LBYL, and simple object creation |
| **Intermediate** | Grasps GIL implications, reference counting, garbage collection generations, and descriptor protocol |
| **Advanced** | Masters name mangling, data model dunders, ABCs vs Protocols, and custom descriptors |
| **Expert** | Designs systems leveraging these atoms: protocol-based APIs, non-data descriptors, generational GC tuning |

## Common Myths

1. **"GIL makes Python threads useless"** — GIL only limits CPU-bound parallelism; threads work fine for I/O-bound tasks
2. **"`__init__` creates objects"** — `__new__` creates; `__init__` initializes
3. **"Name mangling provides privacy"** — It's name collision avoidance, not access control
4. **"ABCs are always better than duck typing"** — Duck typing is Pythonic; ABCs are for enforced contracts
5. **"Garbage collection is automatic so I don't need to think about memory"** — Circular references can leak; use weakref and context managers
6. **"Descriptors are only for properties"** — They power classmethod, staticmethod, slots, and ORM field validation
7. **"Reference counting alone handles memory"** — Circular references require generational GC

## One-Minute Revision

- **Duck Typing**: Behavior over inheritance; design around protocols, not class hierarchies
- **EAFP vs LBYL**: Prefer try/except for Pythonic code; use LBYL for race-sensitive checks
- **GIL**: Mutex allowing one thread to execute Python bytecode; blocks CPU-bound parallelism, not I/O-bound
- **Reference Counting**: Primary memory management; refcount hits 0 → immediate deallocation
- **Garbage Collection**: Generational (3 gen); handles circular references; don't disable in production
- **Name Mangling**: `__name` → `_ClassName__name`; for collision avoidance, not privacy
- **Descriptor Protocol**: `__get__`, `__set__`, `__delete__`; powers properties, classmethod, slots
- **Data Model**: Dunder methods define how objects interact with Python syntax (`+`, `str`, `with`, etc.)
- **ABCs vs Protocols**: ABCs for enforced interfaces; Protocols for structural subtyping (duck typing with type checking)

---

*Next: Explore the `projects/` folder for hands-on practice with these concepts.*
