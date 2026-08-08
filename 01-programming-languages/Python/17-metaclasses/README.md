# Python Metaclasses

## Why Metaclasses Matter

Every Python developer eventually encounters situations where they need to control how classes are created, validate class definitions at definition time, or implement framework-level abstractions. Metaclasses provide the mechanism for intercepting and customizing class creation — they're classes that create classes. Without understanding metaclasses, you'd miss powerful tools for framework development and library design.

Without metaclasses, you'd resort to workarounds and hacks to achieve behaviors that metaclasses handle elegantly. That's why metaclasses exist — they provide the ultimate customization point for class behavior, enabling patterns like automatic registration, validation, and declarative APIs that make frameworks intuitive and powerful.

## What You'll Learn

By the end of this module, you'll be able to:

- Understand how metaclasses intercept and customize class creation
- Create classes dynamically using `type()` and custom metaclasses
- Implement common metaclass patterns for validation and registration
- Know when to use metaclasses and when simpler alternatives suffice
- Debug metaclass-related issues in complex inheritance hierarchies

## Table of Contents

- [What Are Metaclasses](#what-are-metaclasses)
- [Creating Classes Dynamically](#creating-classes-dynamically)
- [Common Metaclass Patterns](#common-metaclass-patterns)
- [When to Use Metaclasses](#when-to-use-metaclasses)
- [Alternatives to Metaclasses](#alternatives-to-metaclasses)

---

## What Are Metaclasses

Metaclasses are classes that create classes. In Python, `type` is the default metaclass — every class is an instance of `type`.

```python
# type is the metaclass of all classes
print(type(int))        # <class 'type'>
print(type(type))       # <class 'type'>

# Creating a class dynamically
MyClass = type('MyClass', (object,), {'attr': 42})
obj = MyClass()
print(obj.attr)  # 42
```

### The Metaclass Hierarchy

```python
# Everything is an object
print(type(42))        # <class 'int'>
print(type(int))       # <class 'type'>
print(type(type))      # <class 'type'>

# Custom metaclass
class Meta(type):
    pass

class MyClass(metaclass=Meta):
    pass

print(type(MyClass))       # <class 'Meta'>
print(isinstance(MyClass, Meta))  # True
```

---

## Creating Classes Dynamically

### Using type()

```python
# type(name, bases, namespace)
MyClass = type('MyClass', (object,), {
    'class_attr': 'hello',
    'method': lambda self: self.class_attr
})

obj = MyClass()
print(obj.method())  # hello
```

### Using exec()

```python
# exec() for complex class creation
class_name = 'DynamicClass'
base_class = object

class_body = """
def __init__(self, value):
    self.value = value

def __repr__(self):
    return f'{self.__class__.__name__}({self.value!r})'
"""

namespace = {}
exec(class_body, namespace)
DynamicClass = type(class_name, (base_class,), namespace)

obj = DynamicClass(42)
print(obj)  # DynamicClass(42)
```

---

## Common Metaclass Patterns

### Singleton Metaclass

```python
class SingletonMeta(type):
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            cls._instances[cls] = super().__call__(*args, **kwargs)
        return cls._instances[cls]

class Database(metaclass=SingletonMeta):
    def __init__(self):
        self.connection = "connected"

db1 = Database()
db2 = Database()
print(db1 is db2)  # True
```

### Validation Metaclass

```python
class ValidatedMeta(type):
    def __new__(cls, name, bases, namespace):
        # Validate all class attributes
        for key, value in namespace.items():
            if not key.startswith('_') and not callable(value):
                if not isinstance(value, (int, float, str)):
                    raise TypeError(f"{key} must be int, float, or str")
        return super().__new__(cls, name, bases, namespace)

class Config(metaclass=ValidatedMeta):
    name = "default"
    timeout = 30
    # invalid = []  # TypeError: invalid must be int, float, or str
```

### Registry Metaclass

```python
class RegistryMeta(type):
    _registry = {}

    def __init__(cls, name, bases, namespace):
        super().__init__(name, bases, namespace)
        if name != 'Base':
            RegistryMeta._registry[name] = cls

    @classmethod
    def get_registered(mcs):
        return dict(mcs._registry)

class Base(metaclass=RegistryMeta):
    pass

class PluginA(Base):
    pass

class PluginB(Base):
    pass

print(RegistryMeta.get_registered())
# {'PluginA': <class 'PluginA'>, 'PluginB': <class 'PluginB'>}
```

### Abstract Base with Metaclass

```python
class AbstractMeta(type):
    def __new__(cls, name, bases, namespace):
        if bases:  # Skip abstract class itself
            for key, value in namespace.items():
                if not key.startswith('_') and callable(value):
                    if not hasattr(value, '_is_implemented'):
                        raise NotImplementedError(
                            f"{name} must implement {key}"
                        )
        return super().__new__(cls, name, bases, namespace)

def abstract(func):
    func._is_implemented = False
    return func

class Interface(metaclass=AbstractMeta):
    @abstract
    def process(self):
        pass

# class Bad(Interface):
#     pass  # NotImplementedError: Bad must implement process

class Good(Interface):
    def process(self):
        return "processed"
```

---

## When to Use Metaclasses

### Appropriate Use Cases

1. **Class validation** — Enforce constraints at class creation time
2. **Registration** — Auto-register classes in a registry
3. **ORM frameworks** — Map database tables to Python classes (Django, SQLAlchemy)
4. **API design** — Enforce interface contracts (like ABCs but with more control)
5. **Framework internals** — Django models, Python dataclasses

### When NOT to Use Metaclasses

1. **Simple singleton** — Use module-level state instead
2. **Class decoration** — `@decorator` is simpler for most transformations
3. **`__init_subclass__`** — Modern Python alternative for subclass hooks
4. **`type.__init_subclass__`** — Built-in subclass notification without metaclasses

```python
# __init_subclass__ is often simpler than metaclasses
class Plugin:
    _registry = {}

    def __init_subclass__(cls, **kwargs):
        super().__init_subclass__(**kwargs)
        Plugin._registry[cls.__name__] = cls

class MyPlugin(Plugin):
    pass

print(Plugin._registry)  # {'MyPlugin': <class 'MyPlugin'>}
```

---

## Alternatives to Metaclasses

| Pattern | Use Case | Complexity |
|---------|----------|------------|
| `@classmethod` | Simple class-level operations | Low |
| `__init_subclass__` | Subclass hooks and registration | Low |
| `__set_name__` | Descriptor attribute naming | Low |
| `dataclass_transform` | Custom dataclass-like behavior | Medium |
| `__class_getitem__` | Generic type support | Low |
| `type()` dynamic creation | Runtime class generation | Medium |
| Class decorators | One-time class transformations | Low |

### Class Decorator vs Metaclass

```python
# Class decorator (simpler)
def add_repr(cls):
    def __repr__(self):
        return f'{cls.__name__}({self.__dict__})'
    cls.__repr__ = __repr__
    return cls

@add_repr
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

# Equivalent metaclass (more powerful but complex)
class ReprMeta(type):
    def __new__(cls, name, bases, namespace):
        def __repr__(self):
            return f'{name}({self.__dict__})'
        namespace['__repr__'] = __repr__
        return super().__new__(cls, name, bases, namespace)

class Point(metaclass=ReprMeta):
    def __init__(self, x, y):
        self.x = x
        self.y = y
```

---

## Production Checklist

- [ ] Prefer `__init_subclass__` over metaclasses for subclass hooks
- [ ] Use class decorators for simple one-time transformations
- [ ] Keep metaclass logic simple; move complex logic to helper functions
- [ ] Document metaclass behavior in class docstrings
- [ ] Test metaclass behavior explicitly; it affects all subclasses
- [ ] Use `super().__new__` in metaclass `__new__` to avoid breaking inheritance
- [ ] Avoid metaclasses for singletons; use module-level state or `functools.lru_cache`
- [ ] Consider `dataclass_transform` (Python 3.11+) for custom dataclass-like behavior
- [ ] Use ABCs (`abc.ABC`) for interface enforcement unless metaclasses add value
- [ ] Profile metaclass overhead; `__new__` runs at class creation, not instantiation

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Understands that `type` creates classes; knows metaclasses are "classes of classes" |
| **Intermediate** | Can read metaclass `__new__` and `__init__`; uses `type()` for dynamic class creation |
| **Advanced** | Implements custom metaclasses for validation, registration, and ORM patterns |
| **Expert** | Designs framework-level metaclasses; knows when to avoid metaclasses; uses `__init_subclass__` as alternative |

## Common Myths

1. **"Metaclasses are always necessary for class creation control"** — `__init_subclass__` and decorators handle most cases
2. **"Metaclasses are Pythonic"** — They're powerful but often over-engineered; Python prefers simplicity
3. **"Metaclasses run at instantiation time"** — They run at class creation time; `__call__` handles instantiation
4. **"You can't stack metaclasses"** — You can, but it's complex; prefer single metaclass or composition
5. **"Metaclasses are the only way to enforce interfaces"** — ABCs and Protocols are simpler alternatives
6. **"Metaclasses are always slow"** — One-time cost at class creation; minimal runtime overhead

## One-Minute Revision

- **Metaclass**: A class that creates classes; `type` is the default metaclass
- **`type(name, bases, namespace)`**: Create classes dynamically; equivalent to `class` statement
- **`__new__`**: Called when class is created; validate/modify namespace before class exists
- **`__init__`**: Called after class is created; post-creation hooks and registration
- **`__call__`**: Called when instance is created; controls instantiation behavior
- **Singleton metaclass**: Override `__call__` to return cached instance
- **Registry metaclass**: Auto-register classes in `__init__` at class creation time
- **`__init_subclass__`**: Modern alternative; runs when subclass is created; no metaclass needed
- **Class decorator**: Simpler alternative for one-time transformations; stacks easily
- **Best practice**: Try `__init_subclass__` first; use metaclasses only when you need `__new__` control

## Interview Questions

### Q1: What is the difference between `type()` and `type`?
**Answer:** type(object) returns the type of object. type is the base metaclass. type('Name', (bases,), dict) creates a class dynamically.

### Q2: What is the difference between `__new__` and `__init__`?
**Answer:** __new__ creates the instance (class method). __init__ initializes it (instance method). __new__ runs first, __init__ second.

### Q3: What is the descriptor protocol?
**Answer:** Objects implementing __get__, __set__, __delete__. Data descriptors (with __set__) take precedence over instance attributes.

### Q4: When should you use metaclasses?
**Answer:** Almost never. Use class decorators instead. Metaclasses for: ORM frameworks, API design, automatic registration.

### Q5: What is the difference between metaclass and class decorator?
**Answer:** Both modify class creation. Class decorator is simpler (applied to result). Metaclass controls creation process. Prefer class decorators.
