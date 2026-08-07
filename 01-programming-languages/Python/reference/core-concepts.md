# Python Core Concepts Reference

## What is Python's Core Concepts?

Python's core concepts form the foundation of how the language works internally. Understanding the data model, names, objects, types, namespaces, and scopes is essential for writing efficient and bug-free code.

## Why does Python's Core Concepts matter?

Understanding these concepts helps you:
- Avoid common bugs related to variable scope and mutable defaults
- Write more Pythonic code
- Debug issues related to object identity and mutability
- Optimize performance by understanding object creation

---

## 1. Python Data Model

Python treats everything as an object. Functions, classes, modules, and even integers are objects.

```python
# Everything is an object
x = 42
print(type(x))  # <class 'int'>
print(x.__class__.__name__)  # int

# Functions are objects
def greet():
    pass

print(type(greet))  # <class 'function'>
print(greet.__doc__)  # None

# Classes are objects
class MyClass:
    pass

print(type(MyClass))  # <class 'type'>
```

### Dunder Methods

Dunder (double underscore) methods define how objects behave.

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

v1 = Vector(2, 3)
v2 = Vector(4, 5)
print(v1 + v2)  # Vector(2, 3) + Vector(4, 5) = Vector(6, 8)
print(abs(v1))   # 3.605...
```

---

## 2. Names and Objects

### Variables are Names, Not Boxes

Python variables are references (names) to objects, not containers.

```python
# Names point to objects
a = [1, 2, 3]  # a points to a list object
b = a           # b points to the same list object
b.append(4)
print(a)        # [1, 2, 3, 4] - Both a and b refer to same object

# Identity vs Equality
x = [1, 2, 3]
y = [1, 2, 3]
print(x == y)   # True - Same value
print(x is y)   # False - Different objects

# Check identity
print(x is not y)  # True
```

### Object Identity and Mutability

```python
# Immutable objects
a = "hello"
b = "hello"
print(a is b)  # True - Python caches small integers and strings

# Mutable objects
a = [1, 2, 3]
b = [1, 2, 3]
print(a is b)  # False - Each list is a separate object

# Mutable default arguments
def append_to(item, target=[]):
    target.append(item)
    return target

print(append_to(1))  # [1]
print(append_to(2))  # [1, 2] - Bug! Same list is reused

# Fix: Use None as default
def append_to_fixed(item, target=None):
    if target is None:
        target = []
    target.append(item)
    return target
```

---

## 3. Types

### Type System Overview

Python is dynamically typed with strong type enforcement.

```python
# Dynamic typing
x = 42          # int
x = "hello"     # str - can change type
x = [1, 2, 3]  # list

# Strong typing - no implicit conversion
result = "42" + 42  # TypeError: can only concatenate str to str

# Type checking
print(type(42))           # <class 'int'>
print(isinstance(42, int)) # True
print(isinstance("hello", (int, str)))  # True - check multiple types

# Type hints (static typing optional)
def greet(name: str) -> str:
    return f"Hello, {name}"
```

### Type Hierarchy

```python
# Base classes
print(int.__bases__)       # (<class 'object'>,)
print(str.__bases__)       # (<class 'object'>,)
print(list.__bases__)      # (<class 'object'>,)

# Method Resolution Order (MRO)
print(int.__mro__)
# (<class 'int'>, <class 'object'>)

# Abstract Base Classes
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self):
        pass

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius
    
    def area(self):
        return 3.14159 * self.radius ** 2

# c = Shape()  # TypeError: Can't instantiate abstract class
c = Circle(5)
print(c.area())  # 78.539...
```

---

## 4. Namespaces

A namespace is a mapping from names to objects.

### Types of Namespaces

```python
# 1. Local namespace - inside functions
def my_func():
    local_var = 10  # Local namespace
    print(local_var)

# 2. Enclosing namespace - nested functions
def outer():
    outer_var = 20  # Enclosing namespace
    
    def inner():
        print(outer_var)  # Access enclosing variable
    
    inner()

# 3. Global namespace - module level
global_var = 30  # Global namespace

# 4. Built-in namespace - Python builtins
print(len([1, 2, 3]))  # Built-in function

# LEGB Rule: Local → Enclosing → Global → Built-in
x = "global"

def outer():
    x = "enclosing"
    
    def inner():
        x = "local"
        print(x)  # local
    
    inner()

outer()  # Prints: local
```

### Namespace Operations

```python
# View namespaces
print(globals())  # Global namespace dictionary
print(locals())   # Local namespace dictionary

# Inspect a module's namespace
import math
print(dir(math))  # List all names in math module

# __dict__ attribute
class MyClass:
    class_var = 10
    
    def __init__(self):
        self.instance_var = 20

obj = MyClass()
print(obj.__dict__)  # {'instance_var': 20}
print(MyClass.__dict__)  # {'class_var': 10, '__init__': <function>, ...}
```

---

## 5. Scopes

### Scope Rules

```python
# Global scope
global_var = 10

def func():
    # Local scope
    local_var = 20
    print(global_var)  # Can access global
    print(local_var)   # Can access local

# Nonlocal scope
def outer():
    x = 10
    
    def inner():
        nonlocal x   # Access enclosing variable
        x = 20
    
    inner()
    print(x)  # 20

# Global declaration
counter = 0

def increment():
    global counter
    counter += 1

increment()
print(counter)  # 1
```

### Closure Scope

```python
def make_multiplier(factor):
    def multiplier(x):
        return x * factor  # factor is from enclosing scope
    return multiplier

double = make_multiplier(2)
triple = make_multiplier(3)

print(double(5))  # 10
print(triple(5))  # 15

# Inspect closure variables
print(double.__closure__[0].cell_contents)  # 2
```

---

## 6. Memory Management

### Reference Counting

```python
import sys

a = [1, 2, 3]
print(sys.getrefcount(a))  # 2 (a + getrefcount parameter)

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

# del parent  # Doesn't free memory due to circular reference
# Use weakref for circular references
```

### Garbage Collection

```python
import gc

# Enable/disable garbage collector
gc.disable()
gc.enable()

# Force garbage collection
gc.collect()

# Get garbage collection stats
print(gc.get_stats())

# Find reference cycles
gc.set_debug(gc.DEBUG_LEAK)
gc.collect()

# Weak references
import weakref

class MyClass:
    def __del__(self):
        print("Object deleted")

obj = MyClass()
weak_ref = weakref.ref(obj)

print(weak_ref())  # <__main__.MyClass object>
del obj
print(weak_ref())  # None
```

---

## 7. Attribute Access

### Normal Attribute Access

```python
class MyClass:
    def __init__(self):
        self.x = 10
    
    def get_x(self):
        return self.x

obj = MyClass()
print(obj.x)         # 10
print(obj.get_x())   # 10
```

### Descriptor Protocol

```python
class Property:
    def __init__(self, fget, fset=None):
        self.fget = fget
        self.fset = fset
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return self.fget(obj)
    
    def __set__(self, obj, value):
        if self.fset is None:
            raise AttributeError("can't set attribute")
        self.fset(obj, value)

class Temperature:
    def __init__(self):
        self._celsius = 0
    
    @Property
    def celsius(self):
        return self._celsius
    
    @celsius.setter
    def celsius(self, value):
        if value < -273.15:
            raise ValueError("Temperature below absolute zero")
        self._celsius = value

temp = Temperature()
temp.celsius = 25
print(temp.celsius)  # 25
```

### Metaclass Attribute Access

```python
class Meta(type):
    def __new__(cls, name, bases, dict):
        print(f"Creating class {name}")
        return super().__new__(cls, name, bases, dict)
    
    def __getattr__(cls, name):
        print(f"Attribute {name} not found")
        raise AttributeError(name)

class MyClass(metaclass=Meta):
    pass

# MyClass.nonexistent  # Prints: Attribute nonexistent not found
```

---

## One-Minute Revision Table

| Concept | Description | Example |
|---------|-------------|---------|
| **Everything is object** | Functions, classes, modules are objects | `type(42)` → `<class 'int'>` |
| **Names are references** | Variables point to objects | `a = [1,2,3]; b = a` |
| **Identity vs Equality** | `is` checks identity, `==` checks value | `a is b` vs `a == b` |
| **Dynamic typing** | Variables can change type | `x = 42; x = "hello"` |
| **LEGB Rule** | Local → Enclosing → Global → Built-in | Variable lookup order |
| **Mutable defaults** | Default args are evaluated once | Use `None` as default |
| **Reference counting** | Objects freed when refcount = 0 | `sys.getrefcount(obj)` |
| **Descriptors** | Protocol for attribute access | `__get__`, `__set__`, `__delete__` |
| **Metaclasses** | Classes of classes | `class Meta(type):` |
| **Weak references** | References that don't increase refcount | `weakref.ref(obj)` |

---

## Common Mistakes

### 1. Mutable Default Arguments

```python
# WRONG
def append_to(item, target=[]):
    target.append(item)
    return target

# RIGHT
def append_to(item, target=None):
    if target is None:
        target = []
    target.append(item)
    return target
```

### 2. Late Binding Closures

```python
# WRONG
functions = []
for i in range(5):
    functions.append(lambda: i)  # All reference same i

# RIGHT
functions = []
for i in range(5):
    functions.append(lambda i=i: i)  # Capture i by value
```

### 3. Using `is` for Value Comparison

```python
# WRONG
x = "hello"
if x is "hello":  # Warning: literal comparison
    pass

# RIGHT
x = "hello"
if x == "hello":
    pass
```

### 4. Modifying Mutable Default

```python
# WRONG
def foo(x=[]):
    x.append(1)
    return x

print(foo())  # [1]
print(foo())  # [1, 1] - Bug!

# RIGHT
def foo(x=None):
    if x is None:
        x = []
    x.append(1)
    return x

print(foo())  # [1]
print(foo())  # [1] - Correct!
```

---

## Production Notes

1. **Use `isinstance()` for type checking** - More flexible than `type()` for inheritance
2. **Prefer `__slots__` for memory optimization** - Reduces memory usage for instances
3. **Use weak references for caches** - Prevents memory leaks
4. **Be careful with global state** - Makes testing harder
5. **Use type hints** - Improves code clarity and IDE support
6. **Understand LEGB rule** - Prevents scope-related bugs
7. **Use `locals()` and `globals()` sparingly** - Can make debugging difficult
8. **Profile before optimizing** - Use `cProfile` to find bottlenecks
9. **Document your API** - Use docstrings and type hints
10. **Test edge cases** - Empty inputs, None values, boundary conditions

---

## Further Reading

- Python Data Model documentation
- Fluent Python by Luciano Ramalho
- Python Cookbook by David Beazley
- Python reference manual
