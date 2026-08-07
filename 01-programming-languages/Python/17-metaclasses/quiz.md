# Metaclasses Quiz

## Question 1 (Multiple Choice)
In Python, what is a metaclass?

- A) A class that inherits from another class
- B) The class of a class — it defines how classes themselves are created and behave
- C) A private class that can't be accessed
- D) A class decorated with `@staticmethod`

**Answer: B**
**Explanation:** Everything in Python is an object, including classes. If `MyClass` is an instance of `type`, then `type` is the metaclass. When you write `class Foo:`, Python calls `type('Foo', bases, namespace)` to create the class object. Metaclasses let you intercept and customize class creation — adding methods, validating attributes, registering classes, or enforcing constraints. They're powerful but should be used sparingly.

---

## Question 2 (Multiple Choice)
What is the difference between `type(name, bases, dict)` and `type.__new__(cls, name, bases, dict)`?

- A) They are identical — both create a new class
- B) `type()` is the constructor call; `__new__` is the low-level hook that controls class creation — metaclasses override `__new__` to customize the class before it's fully initialized
- C) `type()` creates instances; `__new__` creates classes
- D) `__new__` is deprecated in favor of `__init_subclass__`

**Answer: B**
**Explanation:** When you write `class Foo(metaclass=MyMeta)`, Python calls `MyMeta.__new__(MyMeta, 'Foo', (Base,), namespace)` to create the class object. `__new__` is the static method that actually creates the class. `__init__` then initializes it. Metaclasses override `__new__` to modify the class (add methods, wrap attributes, validate structure) before it's returned. This is the primary customization point.

---

## Question 3 (Multiple Choice)
When should you use a metaclass versus a class decorator?

- A) Always prefer metaclasses — they're more powerful
- B) Use metaclasses when you need to control class creation itself (influencing subclasses, modifying namespaces before class creation); use decorators for simpler post-creation modifications
- C) They are interchangeable — use whichever you prefer
- D) Never use metaclasses — they're always over-engineering

**Answer: B**
**Explanation:** Metaclasses are appropriate when: you need to affect subclasses automatically (ORM field registration), you need to modify the class namespace before the class is created, or you're building a framework that requires class-level control. Decorators are simpler for: adding methods after creation, wrapping methods, or modifying attributes. Metaclasses compose better with inheritance but are harder to debug. When in doubt, try a decorator first.

---

## Question 4 (Multiple Choice)
What does `ABCMeta` from `abc` module provide that regular `type` does not?

- A) Faster class creation
- B) Abstract method enforcement — classes with `@abstractmethod` decorators cannot be instantiated unless all abstract methods are implemented
- C) Automatic serialization
- D) Memory optimization

**Answer: B**
**Explanation:** `ABCMeta` is a metaclass that powers Python's Abstract Base Class system. When a class uses `metaclass=ABCMeta`, any method decorated with `@abstractmethod` must be overridden in subclasses. Attempting to instantiate a class with unimplemented abstract methods raises `TypeError`. This enforces interfaces at class creation time, not at runtime call time. Regular `type` has no concept of abstract methods.

---

## Question 5 (Code Output)
What is the output of this code?

```python
class Meta(type):
    def __new__(cls, name, bases, namespace):
        print(f"Creating class: {name}")
        namespace['class_id'] = name.lower()
        return super().__new__(cls, name, bases, namespace)

class MyClass(metaclass=Meta):
    pass

class AnotherClass(metaclass=Meta):
    pass

print(MyClass.class_id)
print(AnotherClass.class_id)
```

**Answer:**
```
Creating class: MyClass
Creating class: AnotherClass
myclass
anotherclass
```
**Explanation:** `Meta.__new__` is called every time a class with `metaclass=Meta` is created. It prints the class name (intercepting creation) and injects `class_id` into the namespace. `super().__new__()` delegates to `type.__new__()` to actually create the class. The `class_id` attribute is available on both classes because it was injected during creation. This demonstrates how metaclasses intercept and modify class creation.

---

## Question 6 (Code Output)
What is the output of this code?

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self): pass
    
    @abstractmethod
    def perimeter(self): pass

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius
    
    def area(self):
        return 3.14159 * self.radius ** 2
    
    # perimeter is NOT implemented!

try:
    c = Circle(5)
except TypeError as e:
    print(f"Error: {e}")
```

**Answer:** `Error: Can't instantiate abstract class Circle with abstract method perimeter`
**Explanation:** `ABCMeta` checks at instantiation time whether all `@abstractmethod`s are implemented. `Circle` implements `area()` but not `perimeter()`, so Python raises `TypeError` when you try to create an instance. This catches missing implementations early — at object creation, not when the method is called. It's Python's way of enforcing interface contracts.

---

## Question 7 (Bug Finding)
Find the bug in this metaclass:

```python
class ValidatedMeta(type):
    def __new__(cls, name, bases, namespace):
        for key, value in namespace.items():
            if not key.startswith('_') and callable(value):
                if not hasattr(value, '__doc__') or not value.__doc__:
                    raise ValueError(f"Public method {name}.{key} must have a docstring")
        return super().__new__(cls, name, bases, namespace)

class UserService(metaclass=ValidatedMeta):
    def create_user(self, name):
        # No docstring!
        return {"name": name}
```

**Bug:** The metaclass checks `hasattr(value, '__doc__')` but all functions have `__doc__` (it's `None` by default). The check should be `if not value.__doc__` (which catches `None` and empty strings). However, the real issue is that this metaclass applies to *all* subclasses — including those that inherit from `UserService`. If a subclass adds a method without a docstring, the metaclass enforces it. But the error message doesn't account for inherited methods. The bug is the check logic: `hasattr(value, '__doc__')` always returns `True`.
**Fix:** Change to:
```python
if not value.__doc__:
    raise ValueError(f"Public method {name}.{key} must have a docstring")
```

---

## Question 8 (Bug Finding)
Find the bug in this descriptor implementation:

```python
class Validated:
    def __init__(self, validator):
        self.validator = validator
    
    def __set_name__(self, owner, name):
        self.name = name
    
    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)
    
    def __set__(self, obj, value):
        if not self.validator(value):
            raise ValueError(f"Invalid value for {self.name}")
        obj.__dict__[self.name] = value

class User:
    name = Validated(lambda v: isinstance(v, str) and len(v) > 0)
    age = Validated(lambda v: isinstance(v, int) and v > 0)

u = User()
u.name = "Alice"
u.age = 30
print(u.name, u.age)
u.name = ""  # What happens?
```

**Bug:** The `__set__` method validates correctly — empty string fails `len(v) > 0`, raising `ValueError`. But there's no bug in the descriptor itself; it works as designed. The potential issue is that `__get__` uses `obj.__dict__.get(self.name)` — if the attribute hasn't been set yet, it returns `None` instead of raising `AttributeError` (which would be the expected behavior for an unset attribute). This could mask initialization bugs.
**Fix:** Raise `AttributeError` for unset attributes:
```python
def __get__(self, obj, objtype=None):
    if obj is None:
        return self
    try:
        return obj.__dict__[self.name]
    except KeyError:
        raise AttributeError(f"{self.name} not set")
```

---

## Question 9 (Scenario)
You're building an ORM (like Django's models) where users define classes like `class User(Model): name = CharField(max_length=100)`. The framework needs to: (1) automatically create database table mappings, (2) validate field types, (3) generate SQL schemas, (4) register models in a registry. How should you implement this?

- A) Use class decorators — they're simpler
- B) Use a metaclass that intercepts class creation, validates fields, registers the class in a model registry, and generates the schema before the class is fully created
- C) Use `__init_subclass__` — it's the modern replacement
- D) Both B and C work; C is simpler for this use case

**Answer: D**
**Explanation:** `__init_subclass__` (Python 3.6+) is the modern way to hook into subclass creation. It runs when a class inherits from the parent, receiving the child class as an argument. For ORM registration, `__init_subclass__` can register models and validate fields without the complexity of a full metaclass. A metaclass is still needed if you want to modify the class namespace before creation or control `__new__`. For most ORM-like patterns, `__init_subclass__` is sufficient and more maintainable.

---

## Question 10 (Architecture Decision)
You're building a plugin system where third-party developers create classes that inherit from a `Plugin` base class. The system needs to: (1) automatically register all plugins, (2) validate that plugins implement required methods, (3) load plugins dynamically from a directory. How should you architect this?

- A) Use a metaclass that auto-registers plugins and validates interface compliance at class creation time, combined with `importlib` for dynamic loading
- B) Use `__init_subclass__` for registration and validation, with `importlib` for dynamic loading — simpler than metaclasses for this pattern
- C) Use a decorator on each plugin class — requires developers to remember to add it
- D) Use `abc.ABC` with `@abstractmethod` for validation, manual registration in a registry dict

**Answer: B**
**Explanation:** `__init_subclass__` is the modern, Pythonic approach. The `Plugin` base class overrides `__init_subclass__` to auto-register subclasses and validate required methods. No metaclass complexity needed. Dynamic loading uses `importlib.import_module()` or `importlib.util.spec_from_file_location()`. This is how modern frameworks (FastAPI, Pydantic v2) implement plugin systems — it's simpler, more debuggable, and achieves the same result as a metaclass. The metaclass approach works but adds unnecessary complexity for this pattern.

---
