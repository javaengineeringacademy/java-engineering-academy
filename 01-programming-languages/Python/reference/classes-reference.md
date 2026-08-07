# Python Classes Reference

## What are Python Classes?

Classes are blueprints for creating objects. They encapsulate data (attributes) and behavior (methods) into a single unit. Python's class system supports inheritance, polymorphism, and other OOP principles.

## Why does Python Classes matter?

Understanding classes helps you:
- Organize code into logical structures
- Implement inheritance and polymorphism
- Create reusable and maintainable code
- Understand Python's object model

---

## 1. Class Creation

```python
# Basic class
class MyClass:
    class_var = 10  # Class variable
    
    def __init__(self, value):
        self.value = value  # Instance variable
    
    def method(self):
        return self.value

# Create instance
obj = MyClass(42)
print(obj.method())  # 42
print(obj.class_var)  # 10
```

---

## 2. Special Methods

```python
class MyClass:
    def __init__(self, value):
        self.value = value
    
    def __repr__(self):
        return f"MyClass({self.value!r})"
    
    def __str__(self):
        return f"MyClass: {self.value}"
    
    def __eq__(self, other):
        return self.value == other.value
    
    def __lt__(self, other):
        return self.value < other.value
    
    def __hash__(self):
        return hash(self.value)
    
    def __bool__(self):
        return self.value != 0
    
    def __len__(self):
        return abs(self.value)
    
    def __contains__(self, item):
        return item == self.value
    
    def __getitem__(self, index):
        return self.value
    
    def __setitem__(self, index, value):
        self.value = value
    
    def __call__(self, *args, **kwargs):
        return f"Called with {args} and {kwargs}"
    
    def __enter__(self):
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        return False
```

---

## 3. Inheritance

```python
# Basic inheritance
class Animal:
    def __init__(self, name):
        self.name = name
    
    def speak(self):
        raise NotImplementedError

class Dog(Animal):
    def speak(self):
        return "Woof!"

class Cat(Animal):
    def speak(self):
        return "Meow!"

# Polymorphism
animals = [Dog("Rex"), Cat("Whiskers")]
for animal in animals:
    print(f"{animal.name}: {animal.speak()}")

# Multiple inheritance
class A:
    def method(self):
        return "A"

class B:
    def method(self):
        return "B"

class C(A, B):
    pass

print(C().method())  # A (MRO: C -> A -> B)

# Method Resolution Order
print(C.__mro__)  # (<class 'C'>, <class 'A'>, <class 'B'>, <class 'object'>)
```

---

## 4. Class Variables vs Instance Variables

```python
class MyClass:
    class_var = 10  # Shared by all instances
    
    def __init__(self, value):
        self.value = value  # Unique to each instance

obj1 = MyClass(1)
obj2 = MyClass(2)

# Class variable
MyClass.class_var = 20
print(obj1.class_var)  # 20
print(obj2.class_var)  # 20

# Instance variable
obj1.value = 100
print(obj1.value)  # 100
print(obj2.value)  # 2 (unchanged)
```

---

## 5. Properties

```python
class Circle:
    def __init__(self, radius):
        self._radius = radius
    
    @property
    def radius(self):
        return self._radius
    
    @radius.setter
    def radius(self, value):
        if value < 0:
            raise ValueError("Radius cannot be negative")
        self._radius = value
    
    @property
    def area(self):
        return 3.14159 * self._radius ** 2

c = Circle(5)
print(c.area)  # 78.539...
c.radius = 10
print(c.area)  # 314.159...
```

---

## 6. Metaclasses

```python
class Meta(type):
    def __new__(cls, name, bases, dict):
        print(f"Creating class {name}")
        return super().__new__(cls, name, bases, dict)
    
    def __init__(cls, name, bases, dict):
        super().__init__(name, bases, dict)
        print(f"Initializing class {name}")

class MyClass(metaclass=Meta):
    pass

# Creating class MyClass
# Initializing class MyClass
```

---

## 7. Descriptors

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

class MyClass:
    @Property
    def value(self):
        return self._value
    
    @value.setter
    def value(self, value):
        self._value = value
```

---

## 8. Slots

```python
class MyClass:
    __slots__ = ['x', 'y']
    
    def __init__(self, x, y):
        self.x = x
        self.y = y

obj = MyClass(1, 2)
print(obj.x, obj.y)

# AttributeError: 'MyClass' object has no attribute 'z'
# obj.z = 3
```

---

## 9. Abstract Base Classes

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self):
        pass
    
    @abstractmethod
    def perimeter(self):
        pass

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius
    
    def area(self):
        return 3.14159 * self.radius ** 2
    
    def perimeter(self):
        return 2 * 3.14159 * self.radius

# c = Shape()  # TypeError: Can't instantiate abstract class
c = Circle(5)
print(c.area())  # 78.539...
```

---

## 10. Mixins

```python
class JsonMixin:
    def to_json(self):
        import json
        return json.dumps(self.__dict__)
    
    @classmethod
    def from_json(cls, json_str):
        import json
        data = json.loads(json_str)
        return cls(**data)

class MyClass(JsonMixin):
    def __init__(self, x, y):
        self.x = x
        self.y = y

obj = MyClass(1, 2)
json_str = obj.to_json()
print(json_str)  # {"x": 1, "y": 2}

obj2 = MyClass.from_json(json_str)
print(obj2.x, obj2.y)  # 1 2
```

---

## One-Minute Revision Table

| Concept | Description | Example |
|---------|-------------|---------|
| **class** | Blueprint for objects | `class MyClass:` |
| **__init__** | Constructor | `def __init__(self):` |
| **self** | Instance reference | `self.value = value` |
| **inheritance** | Class from another class | `class Child(Parent):` |
| **property** | Computed attribute | `@property` |
| **metaclass** | Class of a class | `class Meta(type):` |
| **descriptor** | Protocol for attribute access | `__get__`, `__set__` |
| **slots** | Restrict instance attributes | `__slots__ = ['x']` |
| **ABC** | Abstract base class | `class Shape(ABC):` |
| **mixin** | Reusable behavior | `class JsonMixin:` |

---

## Common Mistakes

### 1. Mutable Default Arguments

```python
# WRONG
class MyClass:
    def __init__(self, items=[]):
        self.items = items

# RIGHT
class MyClass:
    def __init__(self, items=None):
        self.items = items if items is not None else []
```

### 2. Modifying Class Variables

```python
# WRONG
class MyClass:
    items = []

obj = MyClass()
obj.items.append(1)  # Modifies class variable

# RIGHT
class MyClass:
    def __init__(self):
        self.items = []
```

### 3. Using `is` for Type Checking

```python
# WRONG
if type(obj) is MyClass:
    pass

# RIGHT
if isinstance(obj, MyClass):
    pass
```

### 4. Not Using `super()`

```python
# WRONG
class Child(Parent):
    def __init__(self, value):
        Parent.__init__(self, value)

# RIGHT
class Child(Parent):
    def __init__(self, value):
        super().__init__(value)
```

---

## Production Notes

1. **Use `@property` for computed attributes** - More Pythonic than getters/setters
2. **Use `@classmethod` for factory methods** - When you need class instead of instance
3. **Use `@staticmethod` for utility methods** - When you don't need instance/class
4. **Use `__slots__` for memory optimization** - Reduces instance memory usage
5. **Use ABC for abstract classes** - Enforce interface implementation
6. **Use mixins for reusable behavior** - Keep classes focused
7. **Use `__repr__` for debugging** - More useful than `__str__`
8. **Use dataclasses for simple classes** - Less boilerplate
9. **Use `__post_init__` for validation** - In dataclasses
10. **Use `__slots__` with inheritance** - Be careful with multiple inheritance

---

## Further Reading

- Python documentation on classes
- Python documentation on descriptors
- Fluent Python by Luciano Ramalho
- Python Cookbook by David Beazley
