# Python Knowledge Atoms Quiz

## Question 1 (MCQ)
What is "duck typing" in Python?

- A) A type system where variables must be explicitly declared
- B) A concept where an object's suitability is determined by the presence of certain methods and properties, not its type
- C) A way to type-annotate duck-shaped objects
- D) A debugging technique for tracing object references

**Answer: B**
**Explanation:** Duck typing follows the principle "If it walks like a duck and quacks like a duck, then it must be a duck." Python doesn't check the type of an object; it checks whether the object has the required methods/attributes at runtime. This enables polymorphism without inheritance.

---

## Question 2 (MCQ)
What does the EAFP (Easier to Ask Forgiveness than Permission) principle mean in Python?

- A) Always use try/except for every operation
- B) Assume an operation will succeed and handle exceptions if it fails, rather than checking conditions beforehand
- C) Never use if/else statements
- D) Catch all exceptions silently

**Answer: B**
**Explanation:** EAFP is a coding style where you write code that assumes something will work and handle exceptions if it doesn't. For example, `try: value = d[key]` is preferred over `if key in d: value = d[key]` because it avoids race conditions and is more Pythonic (LBYL = Look Before You Leap).

---

## Question 3 (Code Output)
What is the output of this code?

```python
x = [1, 2, 3]
y = x
y.append(4)
print(len(x))
```

- A) 3
- B) 4
- C) NameError
- D) [1, 2, 3, 4]

**Answer: B**
**Explanation:** In Python, variables are references. `y = x` makes `y` point to the same list object as `x`. When `y.append(4)` modifies the list, both `x` and `y` reflect the change. The output is 4 because `x` and `y` refer to the same list in memory.

---

## Question 4 (MCQ)
What is the Global Interpreter Lock (GIL) in CPython?

- A) A lock that prevents multiple processes from accessing Python
- B) A mutex that allows only one thread to execute Python bytecode at a time within a single process
- C) A garbage collection mechanism
- D) A file locking system for concurrent writes

**Answer: B**
**Explanation:** The GIL is a mutex in CPython that protects access to Python objects, preventing multiple threads from executing Python bytecode simultaneously. This simplifies memory management (reference counting) but limits CPU-bound parallelism. I/O-bound threads can still run concurrently.

---

## Question 5 (Code Output)
What is the output of this code?

```python
class MyClass:
    _secret = "hidden"

obj = MyClass()
print(obj.__dict__)
print(obj._MyClass__secret)
```

- A) `{'_secret': 'hidden'} hidden`
- B) `{} hidden`
- C) `{'_secret': 'hidden'} _secret`
- D) AttributeError

**Answer: B**
**Explanation:** Name mangling transforms `_MyClass__secret` to `_secret` when accessed from outside the class. The instance `obj.__dict__` is empty because `_secret` is a class variable, not an instance variable. Accessing `obj._MyClass__secret` works because name mangling allows direct access to the mangled name.

---

## Question 6 (Bug Finding)
A developer wrote this code expecting both methods to work identically. Find the bug:

```python
class Animal:
    def speak(self):
        raise NotImplementedError

class Dog(Animal):
    def speak(self):
        return "Woof"

def make_sound(animal):
    if type(animal) == Animal:
        return animal.speak()
    return "Unknown"

dog = Dog()
print(make_sound(dog))
```

- A) The code runs correctly
- B) `type(animal) == Animal` returns False for Dog instances, so it prints "Unknown" instead of "Woof"
- C) `speak()` is not defined in Animal
- D) The Dog class doesn't properly inherit from Animal

**Answer: B**
**Explanation:** `type(animal) == Animal` performs an exact type check, not an inheritance-aware check. Since `dog` is a `Dog` instance, not an `Animal` instance, the check fails. The fix is to use `isinstance(animal, Animal)` which respects inheritance hierarchies.

---

## Question 7 (Bug Finding)
What is wrong with this code?

```python
def append_to_list(item, lst=[]):
    lst.append(item)
    return lst

print(append_to_list(1))
print(append_to_list(2))
```

- A) It outputs `[1]` then `[2]`
- B) It outputs `[1]` then `[1, 2]` due to mutable default argument bug
- C) It raises a TypeError
- D) It outputs `[1, 2]` twice

**Answer: B**
**Explanation:** Default arguments in Python are evaluated once at function definition time, not at each call. The list `[]` is created once and shared across calls. The fix is to use `lst=None` and create a new list inside: `lst = lst or []`.

---

## Question 8 (MCQ)
What is the data model in Python?

- A) A way to define database schemas
- B) The set of special/magic methods (`__init__`, `__len__`, `__getitem__`, etc.) that define how objects behave with built-in operations
- C) A type hinting system
- D) A data serialization format

**Answer: B**
**Explanation:** Python's data model defines how instances of your classes interact with built-in language constructs. By implementing special methods like `__len__`, `__getitem__`, `__add__`, you can make your objects work with `len()`, indexing, and `+` operators respectively.

---

## Question 9 (Scenario)
You're designing a library where users can define custom collections. You want to ensure all collections implement `add()`, `remove()`, and `__len__()` methods. Which Python feature should you use?

- A) Abstract Base Classes (ABCs) with `@abstractmethod`
- B) Duck typing alone
- C) Type hints only
- D) Multiple inheritance

**Answer: A**
**Explanation:** ABCs (from `abc` module) allow you to define interfaces that subclasses must implement. Using `@abstractmethod` ensures subclasses implement required methods, raising `TypeError` at instantiation if they don't. Duck typing works but doesn't enforce contracts at definition time.

---

## Question 10 (Architecture)
You need to implement a descriptor that validates attribute values before setting them. Which method(s) should the descriptor class implement?

- A) Only `__get__`
- B) `__get__`, `__set__`, and optionally `__delete__`
- C) Only `__set__`
- D) `__init__` and `__new__`

**Answer: B**
**Explanation:** Descriptors implement `__get__` (for attribute access), `__set__` (for attribute assignment), and optionally `__delete__` (for deletion). A data descriptor (implements `__set__` or `__delete__`) takes precedence over instance dictionaries. This is how `@property`, `@classmethod`, and `@staticmethod` work internally.
