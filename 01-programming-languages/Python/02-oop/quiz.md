# Python OOP Quiz

## Question 1 (MCQ - Class vs Instance Variables)
What is the output of this code?

```python
class Dog:
    species = "Canine"
    def __init__(self, name):
        self.name = name

d1 = Dog("Rex")
d2 = Dog("Buddy")
d1.species = "Retriever"
print(d1.species, d2.species, Dog.species)
```

A) `Retriever Retriever Retriever`
B) `Retriever Canine Canine`
C) `Canine Canine Canine`
D) `Retriever Canine Retriever`

**Answer: B**
**Explanation:** `species` is a class variable. `d1.species = "Retriever"` creates an instance variable on `d1`, shadowing the class variable for that instance only. `d2` and `Dog` still see the original class variable `"Canine"`.

---

## Question 2 (MCQ - Method Resolution Order)
Given this code, what is the MRO for class `D`?

```python
class A: pass
class B(A): pass
class C(A): pass
class D(B, C): pass

print([c.__name__ for c in D.__mro__])
```

A) `['D', 'B', 'C', 'A', 'object']`
B) `['D', 'B', 'A', 'C', 'object']`
C) `['D', 'B', 'C', 'A']`
D) `['D', 'A', 'B', 'C', 'object']`

**Answer: A**
**Explanation:** Python uses the C3 linearization algorithm for MRO. The order is: `D` → `B` → `C` → `A` → `object`. C3 ensures that a class appears before its parents and respects the order in the inheritance declaration.

---

## Question 3 (Code Output - Multiple Inheritance)
What is the output of this code?

```python
class Base:
    def greet(self):
        return "Hello from Base"

class Child1(Base):
    def greet(self):
        return "Hello from Child1"

class Child2(Base):
    def greet(self):
        return "Hello from Child2"

class Grand(Child1, Child2):
    pass

g = Grand()
print(g.greet())
```

A) `Hello from Base`
B) `Hello from Child1`
C) `Hello from Child2`
D) Error: ambiguous method

**Answer: B**
**Explanation:** MRO for `Grand` is `Grand → Child1 → Child2 → Base → object`. Python looks up `greet` in order: `Grand` (not defined) → `Child1` (found, uses this). This demonstrates how MRO resolves ambiguity in multiple inheritance.

---

## Question 4 (Code Output - Name Mangling)
What is the output of this code?

```python
class Secret:
    def __init__(self):
        self.__hidden = 42

    def reveal(self):
        return self.__hidden

s = Secret()
print(s.reveal())
print(s._Secret__hidden)
```

A) `42` then `42`
B) `42` then AttributeError
C) Error: `__hidden` is not accessible
D) `None` then `42`

**Answer: A**
**Explanation:** Python name mangling renames `__hidden` to `_Secret__hidden` internally. `s.reveal()` accesses it via the method and returns `42`. `s._Secret__hidden` also works because name mangling uses the class name prefix — it's name mangling, not true access restriction.

---

## Question 5 (MCQ - Abstract Classes)
Which statement about abstract classes in Python is correct?

- A) Abstract classes can be instantiated directly
- B) A class must inherit from `ABC` and use `@abstractmethod` decorators to be abstract
- C) Abstract methods must have a body
- D) Python enforces abstract class rules at runtime only

**Answer: B**
**Explanation:** Abstract classes inherit from `abc.ABC` (or use `ABCMeta`) and define methods with `@abstractmethod`. They cannot be instantiated. Abstract methods should have a body (often just `pass`) for documentation. Rules are enforced at instantiation time, not import time.

---

## Question 6 (Scenario - Magic Methods)
A developer wants `obj1 + obj2` to return a combined result for their custom class. Which magic method should they implement?

- A) `__add__`
- B) `__plus__`
- C) `__sum__`
- D) `__combine__`

**Answer: A**
**Explanation:** The `+` operator calls `__add__(self, other)`. If you define `__add__`, you can customize what `obj1 + obj2` returns. Python also supports `__radd__` for reflected addition (when the left operand doesn't support the operation).

---

## Question 7 (Bug Finding - Property Decorators)
This code raises an error when setting the name. Find the bug:

```python
class Person:
    def __init__(self, name):
        self._name = name

    @property
    def name(self):
        return self._name

p = Person("Alice")
p.name = "Bob"
```

A) `@property` is used incorrectly
B) Missing `@name.setter` decorator — you cannot set a property without a setter
C) `_name` should be `name` without underscore
D) The `__init__` method is wrong

**Answer: B**
**Explanation:** When you define a property with only a getter, assignment (`p.name = "Bob"`) raises `AttributeError: can't set attribute`. You need a `@name.setter` method to allow setting the property value.

---

## Question 8 (MCQ - `__slots__`)
What is the primary benefit of using `__slots__`?

- A) Faster attribute access and lower memory usage
- B) Allows adding new attributes dynamically
- C) Prevents inheritance
- D) Makes the class thread-safe

**Answer: A**
**Explanation:** `__slots__` tells Python to allocate a fixed set of attributes per instance, avoiding the `__dict__` dictionary. This reduces memory usage (especially for many instances) and slightly improves attribute access speed. It also prevents adding undeclared attributes.

---

## Question 9 (Architecture Decision - Dataclasses)
When should you use `@dataclass` instead of a regular class?

- A) When you need only a data container with automatic `__init__`, `__repr__`, `__eq__`, etc.
- B) When you need complex business logic in the class
- C) When you need full control over `__init__` with side effects
- D) `@dataclass` is always preferred over regular classes

**Answer: A**
**Explanation:** `@dataclass` automatically generates `__init__`, `__repr__`, `__eq__`, and more for data-holding classes. It's ideal for DTOs, configs, and value objects. For classes with complex behavior or side effects in `__init__`, a regular class gives more control.

---

## Question 10 (Scenario - Composition vs Inheritance)
You are modeling a `Car` that has an `Engine`. Which approach is better?

- A) Inheritance: `class Car(Engine)`
- B) Composition: `class Car` with `self.engine = Engine()`
- C) Both are equally appropriate
- D) Neither — use global functions

**Answer: B**
**Explanation:** A car *has an* engine (composition), it *is not an* engine (inheritance). Composition is preferred when the relationship is "has-a" — it provides better encapsulation, flexibility, and avoids tight coupling. Inheritance is appropriate for "is-a" relationships (e.g., `ElectricCar(Car)`).
