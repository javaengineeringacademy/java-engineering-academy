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

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | ORM frameworks, API design, class validation, registration | `__init_subclass__` for simpler cases |
| When NOT to use | Don't use for simple singletons; prefer decorators | Use module-level state or `@dataclass` |
| Alternatives | `__init_subclass__`, class decorators, `type()` | Simple inheritance |
| Production Examples | Django models, SQLAlchemy, framework internals | Simple scripts, prototypes |
| Common Mistakes | Over-engineering, not testing metaclass behavior | Try `__init_subclass__` first |

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

## Production Incidents

### Incident 1: Metaclass Conflict in Inheritance

**Problem:** Application crashed with TypeError during class definition
**Cause:** Two parent classes used different metaclasses
**Impact:** Service failed to start; required code refactor
**Detection:** ImportError during module loading
**Solution:**
```python
# BAD: Conflicting metaclasses
class MetaA(type):
    pass
class MetaB(type):
    pass

class A(metaclass=MetaA):
    pass
class B(metaclass=MetaB):
    pass

# class C(A, B):  # TypeError: metaclass conflict!
#     pass

# GOOD: Use compatible metaclasses or __init_subclass__
class Plugin:
    _registry = {}
    def __init_subclass__(cls, **kwargs):
        super().__init_subclass__(**kwargs)
        Plugin._registry[cls.__name__] = cls
```
**Prevention:** Use `__init_subclass__` instead of metaclasses; check MRO for conflicts; test inheritance hierarchies

### Incident 2: Metaclass __new__ Running Multiple Times

**Problem:** Class validation logic executed 10 times during import
**Cause:** Metaclass `__new__` called for each class in hierarchy
**Impact:** Import time increased from 100ms to 2 seconds
**Detection:** Application startup time degradation
**Solution:**
```python
# BAD: Validation runs for every class in hierarchy
class ValidatedMeta(type):
    def __new__(cls, name, bases, namespace):
        # Runs for Base, Child, Grandchild...
        validate_class(namespace)
        return super().__new__(cls, name, bases, namespace)

# GOOD: Only validate concrete classes
class ValidatedMeta(type):
    def __new__(cls, name, bases, namespace):
        # Skip abstract base classes
        if bases and name != 'Abstract':
            validate_class(namespace)
        return super().__new__(cls, name, bases, namespace)
```
**Prevention:** Check if class is abstract before validation; cache validation results; profile metaclass overhead

### Incident 3: Metaclass Breaking Pickle Serialization

**Problem:** Objects couldn't be pickled for caching
**Cause:** Metaclass didn't implement `__reduce__` or `__getstate__`
**Impact:** Redis caching failed; performance degradation
**Detection:** PickleError in production logs
**Solution:**
```python
# BAD: Metaclass without pickle support
class CustomMeta(type):
    pass

class MyClass(metaclass=CustomMeta):
    pass

# Can't pickle MyClass instances!

# GOOD: Add pickle support
class CustomMeta(type):
    def __reduce__(cls):
        return (cls, ())

# Or use dataclasses with metaclass
from dataclasses import dataclass

@dataclass
class MyClass:
    x: int
    y: int
```
**Prevention:** Test pickling with metaclass; implement `__reduce__` or `__getstate__`; use dataclasses for simple cases

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

## Related Topics

- [02-oop](../02-oop/) - OOP fundamentals
- [10-internals](../10-internals/) - Class creation internals
- [11-design-patterns](../11-design-patterns/) - Design patterns using metaclasses

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

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Metaclass conflict in inheritance | Check MRO for conflicting metaclasses | Use `__init_subclass__` instead of metaclasses |
| Metaclass `__new__` running multiple times | Profile import time with `cProfile` | Skip validation for abstract base classes; cache results |
| Metaclass breaking pickle serialization | Test with `pickle.dumps()` | Implement `__reduce__` or use `@dataclass` with metaclass |
| `type()` dynamic creation failing | Check namespace dict for conflicts | Ensure namespace has no name collisions; use `exec()` carefully |
| `__init_subclass__` not receiving kwargs | Pass `**kwargs` through `super().__init_subclass__` | Ensure `__init_subclass__` accepts and forwards kwargs |

## Code Review Checklist

- [ ] `__init_subclass__` tried before resorting to metaclasses
- [ ] Class decorators used for simple one-time transformations
- [ ] Metaclass logic kept simple; complex logic moved to helper functions
- [ ] `super().__new__` called in metaclass `__new__` to preserve inheritance
- [ ] Metaclass behavior documented in class docstrings
- [ ] All subclasses tested for metaclass behavior
- [ ] `dataclass_transform` (3.11+) used for custom dataclass-like behavior

## Architecture Considerations

Metaclasses are the ultimate customization point for class creation. They enable framework-level abstractions like ORMs and API design. However, they add complexity and should be avoided when simpler alternatives (`__init_subclass__`, decorators) suffice. The decision depends on whether you need to control class creation itself or just post-creation modifications.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| `__init_subclass__` | Subclass hooks and registration | Simpler than metaclasses but limited to subclass creation |
| Class decorator | One-time class transformations | Easy to stack but can't intercept `__new__` |
| Metaclass `__new__` | Class creation control | Most powerful but most complex |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Metaclass executing arbitrary code | Code injection via `exec()` | Validate namespace; avoid `exec()` in metaclasses |
| Dynamic class creation from untrusted input | Object injection | Restrict `type()` calls to known base classes |
| Metaclass bypassing security decorators | Security check circumvention | Test metaclass behavior with security decorators |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.6+ | `__init_subclass__` | Replace metaclass subclass hooks with `__init_subclass__` |
| Python 3.11+ | `dataclass_transform` | Use for custom dataclass-like behavior without metaclasses |
| Python 3.12+ | `type` parameter syntax | Cleaner generic class creation |

## Version Validation

| Feature | Python Version | Status |
|---------|---------------|--------|
| `__init_subclass__` | 3.6+ | Stable, preferred over metaclasses |
| `dataclass_transform` | 3.11+ | Stable, custom dataclass behavior |
| `type()` dynamic creation | 2.2+ | Stable, class creation at runtime |
| Class decorators | 2.6+ | Stable, simple class transformations |
