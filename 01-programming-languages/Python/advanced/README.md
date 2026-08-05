# Python Advanced Concepts

A comprehensive guide to advanced Python programming concepts.

## Table of Contents

- [Metaclasses](#metaclasses)
- [Descriptors](#descriptors)
- [Generators and Iterators](#generators-and-iterators)
- [Async/Await](#asyncawait)
- [Advanced Typing](#advanced-typing)
- [Dataclasses](#dataclasses)
- [Slots](#slots)
- [Garbage Collection](#garbage-collection)
- [Memory Management](#memory-management)
- [Advanced Patterns](#advanced-patterns)

---

## Metaclasses

### What is a Metaclass?

A metaclass is the class of a class. Just as a class defines how instances behave, a metaclass defines how classes behave.

```python
# Everything in Python is an object
class MyClass:
    pass

obj = MyClass()

print(type(obj))       # <class '__main__.MyClass'>
print(type(MyClass))   # <class 'type'>
print(type(type))      # <class 'type'>
```

### Basic Metaclass

```python
class MyMeta(type):
    def __new__(cls, name, bases, dict):
        # Called when class is created
        print(f"Creating class: {name}")
        cls = super().__new__(cls, name, bases, dict)
        return cls

    def __init__(cls, name, bases, dict):
        # Called after class is created
        super().__init__(name, bases, dict)
        print(f"Initializing class: {name}")

    def __call__(cls, *args, **kwargs):
        # Called when instance is created
        print(f"Creating instance of {cls.__name__}")
        instance = super().__call__(*args, **kwargs)
        return instance

class MyClass(metaclass=MyMeta):
    pass

# Output:
# Creating class: MyClass
# Initializing class: MyClass

obj = MyClass()
# Output: Creating instance of MyClass
```

### Practical Metaclass Examples

```python
# Singleton pattern
class SingletonMeta(type):
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            cls._instances[cls] = super().__call__(*args, **kwargs)
        return cls._instances[cls]

class Database(metaclass=SingletonMeta):
    def __init__(self):
        self.connection = "Connected"

db1 = Database()
db2 = Database()
print(db1 is db2)  # True

# Auto-register classes
class RegistryMeta(type):
    _registry = {}

    def __init_subclass__(cls, **kwargs):
        super().__init_subclass__(**kwargs)
        cls._registry[cls.__name__] = cls

    @classmethod
    def get_registry(mcs):
        return dict(mcs._registry)

class Plugin(metaclass=RegistryMeta):
    pass

class PluginA(Plugin):
    pass

class PluginB(Plugin):
    pass

print(RegistryMeta.get_registry())  # {'PluginA': <class 'PluginA'>, ...}
```

### Class Creation Process

```python
# The class creation process
# 1. metaclass.__new__() - creates the class
# 2. metaclass.__init__() - initializes the class
# 3. class.__init_subclass__() - called for subclasses

class Base(metaclass=type):
    def __init_subclass__(cls, **kwargs):
        super().__init_subclass__(**kwargs)
        print(f"Subclass created: {cls.__name__}")

class Child(Base):
    pass
# Output: Subclass created: Child
```

---

## Descriptors

### What is a Descriptor?

A descriptor is an object that defines any of `__get__`, `__set__`, or `__delete__` methods.

```python
class Property:
    """A Python property-like descriptor."""

    def __init__(self, fget=None, fset=None, fdel=None, doc=None):
        self.fget = fget
        self.fset = fset
        self.fdel = fdel
        self.__doc__ = doc or (fget.__doc__ if fget else None)

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        if self.fget is None:
            raise AttributeError("unreadable attribute")
        return self.fget(obj)

    def __set__(self, obj, value):
        if self.fset is None:
            raise AttributeError("can't set attribute")
        self.fset(obj, value)

    def __delete__(self, obj):
        if self.fdel is None:
            raise AttributeError("can't delete attribute")
        self.fdel(obj)

    def getter(self, fget):
        return type(self)(fget, self.fset, self.fdel, self.__doc__)

    def setter(self, fset):
        return type(self)(self.fget, fset, self.fdel, self.__doc__)

    def deleter(self, fdel):
        return type(self)(self.fget, self.fset, fdel, self.__doc__)
```

### Data Descriptors vs Non-Data Descriptors

```python
# Data descriptor (defines __set__ or __delete__)
class DataDescriptor:
    def __get__(self, obj, objtype=None):
        print("DataDescriptor.__get__")
        return 42

    def __set__(self, obj, value):
        print("DataDescriptor.__set__")

# Non-data descriptor (only defines __get__)
class NonDataDescriptor:
    def __get__(self, obj, objtype=None):
        print("NonDataDescriptor.__get__")
        return 42

class MyClass:
    data_desc = DataDescriptor()
    non_data_desc = NonDataDescriptor()

obj = MyClass()

# Data descriptor takes precedence over instance attributes
obj.data_desc      # DataDescriptor.__get__
obj.data_desc = 10 # DataDescriptor.__set__
print(obj.__dict__)  # {} - nothing stored

# Non-data descriptor can be overridden by instance attributes
obj.non_data_desc      # NonDataDescriptor.__get__
obj.non_data_desc = 10 # Stored in instance __dict__
obj.non_data_desc      # 10 (instance attribute wins)
```

### Practical Descriptor Examples

```python
# Type-checked attribute
class TypeChecked:
    def __init__(self, name, expected_type):
        self.name = name
        self.expected_type = expected_type

    def __set_name__(self, owner, name):
        self.name = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)

    def __set__(self, obj, value):
        if not isinstance(value, self.expected_type):
            raise TypeError(
                f"{self.name} must be {self.expected_type.__name__}"
            )
        obj.__dict__[self.name] = value

class Person:
    name = TypeChecked("name", str)
    age = TypeChecked("age", int)

    def __init__(self, name, age):
        self.name = name
        self.age = age

p = Person("Alice", 30)
# p.name = 123  # TypeError: name must be str

# Lazy evaluation
class LazyProperty:
    def __init__(self, func):
        self.func = func

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        value = self.func(obj)
        setattr(obj, self.func.__name__, value)
        return value

class DataProcessor:
    def __init__(self, data):
        self.data = data

    @LazyProperty
    def processed(self):
        print("Processing...")
        return [x * 2 for x in self.data]

processor = DataProcessor([1, 2, 3])
print(processor.processed)  # Processing... [2, 4, 6]
print(processor.processed)  # [2, 4, 6] (cached, no Processing...)
```

---

## Generators and Iterators

### Iterator Protocol

```python
class CountDown:
    def __init__(self, start):
        self.start = start

    def __iter__(self):
        return self

    def __next__(self):
        if self.start <= 0:
            raise StopIteration
        self.start -= 1
        return self.start + 1

for num in CountDown(5):
    print(num)  # 5, 4, 3, 2, 1
```

### Generator Functions

```python
# Basic generator
def countdown(n):
    while n > 0:
        yield n
        n -= 1

for num in countdown(5):
    print(num)  # 5, 4, 3, 2, 1

# Generator with send()
def accumulator():
    total = 0
    while True:
        value = yield total
        if value is None:
            break
        total += value

acc = accumulator()
next(acc)          # Initialize (prime the generator)
acc.send(10)       # 10
acc.send(20)       # 30
acc.send(30)       # 60

# Generator with return value
def gen_with_return():
    yield 1
    yield 2
    return "Done"

gen = gen_with_return()
print(next(gen))  # 1
print(next(gen))  # 2
try:
    next(gen)
except StopIteration as e:
    print(e.value)  # "Done"
```

### Generator Expressions

```python
# Generator expression
squares = (x**2 for x in range(1000000))
print(next(squares))  # 0
print(next(squares))  # 1

# Memory efficient sum
total = sum(x**2 for x in range(1000000))

# Chaining generators
def flatten(nested):
    for item in nested:
        if isinstance(item, (list, tuple)):
            yield from flatten(item)
        else:
            yield item

data = [1, [2, 3], [4, [5, 6]], 7]
print(list(flatten(data)))  # [1, 2, 3, 4, 5, 6, 7]
```

### itertools Module

```python
import itertools

# Infinite iterators
count = itertools.count(start=1, step=2)
cycle = itertools.cycle([1, 2, 3])
repeat = itertools.repeat("hello", times=3)

# Terminating iterators
chain = itertools.chain([1, 2], [3, 4], [5, 6])
compact = itertools.filterfalse(None, [0, 1, "", "hello", None, 42])
grouped = itertools.groupby("AAABBBCC")
starmap = itertools.starmap(pow, [(2, 3), (3, 2)])
takewhile = itertools.takewhile(lambda x: x < 5, [1, 3, 5, 2, 1])
dropwhile = itertools.dropwhile(lambda x: x < 5, [1, 3, 5, 2, 1])
compress = itertools.compress("ABCDEF", [1, 0, 1, 0, 1, 0])

# Combinatoric iterators
permutations = itertools.permutations("ABC", 2)
combinations = itertools.combinations("ABC", 2)
combinations_with_replacement = itertools.combinations_with_replacement("ABC", 2)
product = itertools.product("AB", repeat=2)
```

---

## Async/Await

### Basic Async/Await

```python
import asyncio

async def fetch_data():
    print("Starting fetch...")
    await asyncio.sleep(2)  # Simulate I/O operation
    print("Fetch complete!")
    return {"data": "some data"}

# Running async code
async def main():
    result = await fetch_data()
    print(result)

asyncio.run(main())
```

### Concurrent Execution

```python
import asyncio
import time

async def task(name, duration):
    print(f"Task {name} started")
    await asyncio.sleep(duration)
    print(f"Task {name} completed")
    return f"Result from {name}"

async def main():
    # Sequential execution
    start = time.time()
    result1 = await task("A", 2)
    result2 = await task("B", 2)
    print(f"Sequential: {time.time() - start:.2f}s")

    # Concurrent execution
    start = time.time()
    result1, result2 = await asyncio.gather(
        task("C", 2),
        task("D", 2)
    )
    print(f"Concurrent: {time.time() - start:.2f}s")

    # Using tasks
    tasks = [task(f"Task-{i}", i) for i in range(5)]
    results = await asyncio.gather(*tasks)
    print(results)

asyncio.run(main())
```

### Async Context Managers and Iterators

```python
import asyncio

# Async context manager
class AsyncDatabase:
    async def __aenter__(self):
        print("Connecting to database...")
        await asyncio.sleep(1)
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        print("Disconnecting from database...")
        await asyncio.sleep(1)
        return False

    async def query(self, sql):
        await asyncio.sleep(0.5)
        return f"Results for: {sql}"

async def main():
    async with AsyncDatabase() as db:
        result = await db.query("SELECT * FROM users")
        print(result)

# Async iterator
class AsyncCounter:
    def __init__(self, stop):
        self.current = 0
        self.stop = stop

    def __aiter__(self):
        return self

    async def __anext__(self):
        if self.current >= self.stop:
            raise StopAsyncIteration
        await asyncio.sleep(0.1)
        self.current += 1
        return self.current

async def main():
    async for num in AsyncCounter(5):
        print(num)

asyncio.run(main())
```

### Async Generators

```python
import asyncio

# Async generator
async def async_range(start, stop, delay=0.1):
    current = start
    while current < stop:
        await asyncio.sleep(delay)
        yield current
        current += 1

async def main():
    async for num in async_range(0, 5):
        print(num)

# Async generator expression
async def process_items():
    return [x async for x in async_range(0, 10) if x % 2 == 0]

asyncio.run(main())
```

### Async Queues

```python
import asyncio

async def producer(queue, n):
    for i in range(n):
        print(f"Producing item {i}")
        await asyncio.sleep(0.5)
        await queue.put(f"Item {i}")
    await queue.put(None)  # Signal completion

async def consumer(queue):
    while True:
        item = await queue.get()
        if item is None:
            break
        print(f"Consuming {item}")
        await asyncio.sleep(1)
        queue.task_done()

async def main():
    queue = asyncio.Queue(maxsize=5)
    producer_task = asyncio.create_task(producer(queue, 10))
    consumer_task = asyncio.create_task(consumer(queue))
    await asyncio.gather(producer_task, consumer_task)

asyncio.run(main())
```

---

## Advanced Typing

### Generics

```python
from typing import TypeVar, Generic, List, Optional

T = TypeVar('T')
K = TypeVar('K')
V = TypeVar('V')

class Stack(Generic[T]):
    def __init__(self) -> None:
        self._items: List[T] = []

    def push(self, item: T) -> None:
        self._items.append(item)

    def pop(self) -> T:
        if not self._items:
            raise IndexError("Stack is empty")
        return self._items.pop()

    def peek(self) -> Optional[T]:
        if not self._items:
            return None
        return self._items[-1]

    def __len__(self) -> int:
        return len(self._items)

# Usage
int_stack: Stack[int] = Stack()
int_stack.push(1)
int_stack.push(2)
print(int_stack.pop())  # 2

# Generic function
def first(items: List[T]) -> Optional[T]:
    return items[0] if items else None

result = first([1, 2, 3])  # Optional[int]
```

### Protocols (Structural Subtyping)

```python
from typing import Protocol, runtime_checkable

@runtime_checkable
class Drawable(Protocol):
    def draw(self) -> str: ...

@runtime_checkable
class Resizable(Protocol):
    def resize(self, factor: float) -> None: ...

class Circle:
    def draw(self) -> str:
        return "Drawing circle"

    def resize(self, factor: float) -> None:
        pass

class Square:
    def draw(self) -> str:
        return "Drawing square"

def draw_shape(shape: Drawable) -> None:
    print(shape.draw())

# Works with any object that has draw() method
draw_shape(Circle())  # Works
draw_shape(Square())  # Works
draw_shape("hello")   # TypeError at runtime if @runtime_checkable
```

### TypeVar Constraints and Bounds

```python
from typing import TypeVar, Union

# Constrained TypeVar
AnyStr = TypeVar('AnyStr', str, bytes)

def concat(x: AnyStr, y: AnyStr) -> AnyStr:
    return x + y

concat("hello", " world")   # str
concat(b"hello", b" world") # bytes
# concat("hello", b" world")  # Error

# Bounded TypeVar
from typing import List

def max_item(items: List[T]) -> T:
    return max(items)

# TypeVar with bound
Number = TypeVar('Number', bound='int')

def double(x: Number) -> Number:
    return x * 2

# NewType
from typing import NewType

UserId = NewType('UserId', int)
OrderId = NewType('OrderId', int)

def get_user(user_id: UserId) -> str:
    return f"User {user_id}"

user_id = UserId(123)
get_user(user_id)      # OK
# get_user(OrderId(123))  # Error - different type
```

### Advanced TypedDict

```python
from typing import TypedDict, Required, NotRequired, Literal

class MovieDict(TypedDict):
    name: str
    year: int
    director: Required[str]
    rating: NotRequired[float]

# Total=False
class MovieOptional(TypedDict, total=False):
    name: str
    year: int
    director: str

# Inheritance
class BookDict(MovieDict):
    author: str
    isbn: str

# Literal types
def set_mode(mode: Literal['read', 'write', 'append']) -> None:
    pass

set_mode('read')   # OK
# set_mode('delete')  # Error

# Final
from typing import Final

MAX_SIZE: Final = 100
# MAX_SIZE = 200  # Error - cannot reassign Final
```

---

## Dataclasses

### Basic Dataclass

```python
from dataclasses import dataclass, field
from typing import List

@dataclass
class Point:
    x: float
    y: float

# Automatically generates __init__, __repr__, __eq__, etc.
p1 = Point(1.0, 2.0)
p2 = Point(1.0, 2.0)
print(p1 == p2)  # True

# With defaults
@dataclass
class User:
    name: str
    age: int
    email: str = ""
    is_active: bool = True

user = User("Alice", 30)
print(user)  # User(name='Alice', age=30, email='', is_active=True)

# With field options
@dataclass
class Config:
    host: str = "localhost"
    port: int = 8080
    debug: bool = False
    tags: List[str] = field(default_factory=list)
    _private: str = field(init=False, default="hidden")

config = Config()
print(config.tags)  # []
```

### Advanced Dataclass Features

```python
@dataclass(frozen=True)  # Immutable
class Point:
    x: float
    y: float

p = Point(1.0, 2.0)
# p.x = 5.0  # FrozenInstanceError

@dataclass(order=True)  # Supports comparison
class Student:
    grade: int
    name: str = field(compare=False)

s1 = Student(90, "Alice")
s2 = Student(85, "Bob")
print(s1 > s2)  # True (compares grade)

# Custom post-init
@dataclass
class Temperature:
    celsius: float

    def __post_init__(self):
        if self.celsius < -273.15:
            raise ValueError("Temperature below absolute zero")

    @property
    def fahrenheit(self) -> float:
        return self.celsius * 9/5 + 32

# Inheritance
@dataclass
class Base:
    x: int = 0

@dataclass
class Child(Base):
    y: int = 0
    z: int = 0

c = Child(x=1, y=2, z=3)
```

### Dataclass Converters

```python
from dataclasses import dataclass, asdict, astuple, fields
import json

@dataclass
class User:
    name: str
    age: int
    email: str

user = User("Alice", 30, "alice@example.com")

# Convert to dictionary
user_dict = asdict(user)
print(user_dict)  # {'name': 'Alice', 'age': 30, 'email': 'alice@example.com'}

# Convert to tuple
user_tuple = astuple(user)
print(user_tuple)  # ('Alice', 30, 'alice@example.com')

# Get fields
for f in fields(user):
    print(f.name, f.type)

# Custom serialization
def to_json(obj):
    return json.dumps(asdict(obj))

print(to_json(user))
```

---

## Slots

### What are __slots__?

`__slots__` is a class variable that, when defined, prevents the creation of `__dict__` and `__weakref__` for each instance.

```python
# Without __slots__
class PointRegular:
    def __init__(self, x, y):
        self.x = x
        self.y = y

p = PointRegular(1, 2)
print(p.__dict__)  # {'x': 1, 'y': 2}
p.z = 3            # Can add arbitrary attributes

# With __slots__
class PointSlotted:
    __slots__ = ('x', 'y')

    def __init__(self, x, y):
        self.x = x
        self.y = y

p = PointSlotted(1, 2)
# p.__dict__  # AttributeError
# p.z = 3    # AttributeError
```

### Slots Benefits

```python
import sys

class Regular:
    def __init__(self, x, y):
        self.x = x
        self.y = y

class Slotted:
    __slots__ = ('x', 'y')

    def __init__(self, x, y):
        self.x = x
        self.y = y

regular = Regular(1, 2)
slotted = Slotted(1, 2)

print(sys.getsizeof(regular))         # 48 (varies)
print(sys.getsizeof(regular.__dict__)) # 64 (varies)
print(sys.getsizeof(slotted))         # 40 (varies)

# Slotted uses less memory
import tracemalloc

tracemalloc.start()

# Create many instances
regular_instances = [Regular(i, i) for i in range(100000)]
current, peak = tracemalloc.get_traced_memory()
print(f"Regular: {current / 1024 / 1024:.2f} MB")
tracemalloc.stop()

tracemalloc.start()
slotted_instances = [Slotted(i, i) for i in range(100000)]
current, peak = tracemalloc.get_traced_memory()
print(f"Slotted: {current / 1024 / 1024:.2f} MB")
tracemalloc.stop()
```

### Slots with Inheritance

```python
class Base:
    __slots__ = ('x',)

class Child(Base):
    __slots__ = ('y',)

c = Child()
c.x = 1
c.y = 2
print(c.x, c.y)  # 1 2

# Multiple inheritance with slots
class A:
    __slots__ = ('a',)

class B:
    __slots__ = ('b',)

# class C(A, B):
#     __slots__ = ()  # Error if A and B have slots

# Works if no slots conflict
class C(A, B):
    pass
```

---

## Garbage Collection

### Reference Counting

```python
import sys

# Reference counting
a = []
print(sys.getrefcount(a))  # 2 (a + getrefcount argument)

b = a
print(sys.getrefcount(a))  # 3

del b
print(sys.getrefcount(a))  # 2

# Circular references
class Node:
    def __init__(self):
        self.parent = None
        self.children = []

parent = Node()
child = Node()
parent.children.append(child)
child.parent = parent  # Circular reference

del parent
del child
# Objects still exist due to circular reference
```

### Generational Garbage Collector

```python
import gc

# Enable/disable GC
gc.disable()
gc.enable()

# Set thresholds
gc.set_threshold(700, 10, 10)

# GC generations
# Generation 0: New objects (most frequently collected)
# Generation 1: Survived one collection
# Generation 2: Survived two collections (long-lived)

# Force garbage collection
gc.collect()

# Get GC stats
print(gc.get_count())      # Objects in each generation
print(gc.get_threshold())  # Collection thresholds

# Debug GC
gc.set_debug(gc.DEBUG_LEAK)
gc.collect()

# Inspect unreachable objects
unreachable = gc.garbage
print(f"Unreachable objects: {len(unreachable)}")
```

### Weak References

```python
import weakref

class ExpensiveObject:
    def __init__(self, name):
        self.name = name

    def __repr__(self):
        return f"ExpensiveObject({self.name})"

    def __del__(self):
        print(f"Deleting {self.name}")

# Create weak reference
obj = ExpensiveObject("data")
weak_ref = weakref.ref(obj)()

print(weak_ref)  # ExpensiveObject(data)
print(weak_ref())  # ExpensiveObject(data)

# Weak reference callbacks
def callback(ref):
    print("Object was deleted")

obj = ExpensiveObject("data")
weak_ref = weakref.ref(obj, callback)

del obj  # Output: Object was deleted, Deleting data

# WeakValueDictionary
class Data:
    def __init__(self, value):
        self.value = value

cache = weakref.WeakValueDictionary()
obj = Data(42)
cache['key'] = obj
print(cache['key'].value)  # 42

del obj
print('key' in cache)  # False
```

---

## Memory Management

### Memory Profiling

```python
import tracemalloc
import sys

# Track memory allocations
tracemalloc.start()

# Your code here
data = [i ** 2 for i in range(100000)]

# Get current and peak memory usage
current, peak = tracemalloc.get_traced_memory()
print(f"Current: {current / 1024:.2f} KB")
print(f"Peak: {peak / 1024:.2f} KB")

# Take snapshot
snapshot = tracemalloc.take_snapshot()
top_stats = snapshot.statistics('lineno')

print("[ Top 10 ]")
for stat in top_stats[:10]:
    print(stat)

# Compare snapshots
tracemalloc.start()
snapshot1 = tracemalloc.take_snapshot()

# Do more work
data2 = [i ** 3 for i in range(100000)]

snapshot2 = tracemalloc.take_snapshot()
top_stats = snapshot2.compare_to(snapshot1, 'lineno')

print("[ Top 10 differences ]")
for stat in top_stats[:10]:
    print(stat)

tracemalloc.stop()
```

### Memory-Saving Techniques

```python
# Use __slots__
class PointSlotted:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y

# Use generators instead of lists
def generate_squares(n):
    for i in range(n):
        yield i ** 2

# Use array module for homogeneous data
from array import array
arr = array('i', range(1000000))  # 4 bytes per int vs 28 bytes for list

# Use numpy for large numerical data
import numpy as np
arr = np.arange(1000000)  # 8 bytes per int

# Use __sizeof__ for custom objects
class Data:
    __slots__ = ('values',)

    def __init__(self):
        self.values = []

    def __sizeof__(self):
        return object.__sizeof__(self) + sum(
            sys.getsizeof(v) for v in self.values
        )
```

---

## Advanced Patterns

### Abstract Base Classes

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self) -> float:
        pass

    @abstractmethod
    def perimeter(self) -> float:
        pass

    def describe(self) -> str:
        return f"Shape with area {self.area():.2f}"

class Circle(Shape):
    def __init__(self, radius: float):
        self.radius = radius

    def area(self) -> float:
        return 3.14159 * self.radius ** 2

    def perimeter(self) -> float:
        return 2 * 3.14159 * self.radius

# shape = Shape()  # TypeError: Can't instantiate abstract class
circle = Circle(5)
print(circle.describe())  # Shape with area 78.54
```

### Mixins

```python
import json

class JsonMixin:
    def to_json(self) -> str:
        return json.dumps(self.__dict__)

    @classmethod
    def from_json(cls, json_str: str):
        data = json.loads(json_str)
        return cls(**data)

class LogMixin:
    def log(self, message: str) -> None:
        print(f"[{self.__class__.__name__}] {message}")

class User(JsonMixin, LogMixin):
    def __init__(self, name: str, age: int):
        self.name = name
        self.age = age

user = User("Alice", 30)
print(user.to_json())  # {"name": "Alice", "age": 30}
user.log("User created")  # [User] User created
```

### Builder Pattern

```python
class QueryBuilder:
    def __init__(self):
        self._table = None
        self._conditions = []
        self._order_by = None
        self._limit = None

    def table(self, table: str) -> 'QueryBuilder':
        self._table = table
        return self

    def where(self, condition: str) -> 'QueryBuilder':
        self._conditions.append(condition)
        return self

    def order_by(self, column: str, desc: bool = False) -> 'QueryBuilder':
        self._order_by = f"{column} {'DESC' if desc else 'ASC'}"
        return self

    def limit(self, n: int) -> 'QueryBuilder':
        self._limit = n
        return self

    def build(self) -> str:
        query = f"SELECT * FROM {self._table}"
        if self._conditions:
            query += " WHERE " + " AND ".join(self._conditions)
        if self._order_by:
            query += f" ORDER BY {self._order_by}"
        if self._limit:
            query += f" LIMIT {self._limit}"
        return query

query = (QueryBuilder()
    .table("users")
    .where("age > 18")
    .where("active = true")
    .order_by("name")
    .limit(10)
    .build())
print(query)
```

### Observer Pattern

```python
from typing import Callable, List

class Event:
    def __init__(self):
        self._handlers: List[Callable] = []

    def __iadd__(self, handler: Callable) -> 'Event':
        self._handlers.append(handler)
        return self

    def __isub__(self, handler: Callable) -> 'Event':
        self._handlers.remove(handler)
        return self

    def emit(self, *args, **kwargs) -> None:
        for handler in self._handlers:
            handler(*args, **kwargs)

class EventEmitter:
    def __init__(self):
        self.on_data = Event()
        self.on_error = Event()

    def process(self, data):
        try:
            self.on_data.emit(data)
        except Exception as e:
            self.on_error.emit(e)

# Usage
emitter = EventEmitter()
emitter.on_data += lambda data: print(f"Data: {data}")
emitter.on_error += lambda e: print(f"Error: {e}")

emitter.process("hello")  # Data: hello
```

### Strategy Pattern

```python
from typing import Protocol

class SortStrategy(Protocol):
    def sort(self, data: list) -> list: ...

class BubbleSort:
    def sort(self, data: list) -> list:
        arr = data.copy()
        n = len(arr)
        for i in range(n):
            for j in range(0, n-i-1):
                if arr[j] > arr[j+1]:
                    arr[j], arr[j+1] = arr[j+1], arr[j]
        return arr

class QuickSort:
    def sort(self, data: list) -> list:
        if len(data) <= 1:
            return data
        pivot = data[len(data) // 2]
        left = [x for x in data if x < pivot]
        middle = [x for x in data if x == pivot]
        right = [x for x in data if x > pivot]
        return self.sort(left) + middle + self.sort(right)

class Sorter:
    def __init__(self, strategy: SortStrategy):
        self._strategy = strategy

    def set_strategy(self, strategy: SortStrategy) -> None:
        self._strategy = strategy

    def sort(self, data: list) -> list:
        return self._strategy.sort(data)

# Usage
sorter = Sorter(BubbleSort())
print(sorter.sort([3, 1, 4, 1, 5, 9]))

sorter.set_strategy(QuickSort())
print(sorter.sort([3, 1, 4, 1, 5, 9]))
```

---

## Summary

Advanced Python concepts provide powerful tools for building sophisticated applications:

- **Metaclasses** control class creation and behavior
- **Descriptors** enable property-like functionality
- **Generators** provide memory-efficient iteration
- **Async/await** enables concurrent programming
- **Advanced typing** improves code documentation and tooling
- **Dataclasses** reduce boilerplate code
- **Slots** optimize memory usage
- **Garbage collection** manages memory automatically
- **Design patterns** provide proven solutions to common problems
