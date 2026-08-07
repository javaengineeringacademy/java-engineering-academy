# Design Patterns Quiz

## Question 1 (Multiple Choice)
Which pattern is best suited when you need to ensure exactly one instance of a class exists throughout the application lifecycle, and that instance is lazily initialized with thread safety?

- A) Factory Method — creates objects but doesn't control instance count
- B) Singleton using module-level variable (Python's most Pythonic approach)
- C) Prototype — clones existing instances rather than controlling creation
- D) Observer — manages subscriptions, not instance creation

**Answer: B**
**Explanation:** Python's module-level singleton is the most idiomatic approach — importing a module initializes it exactly once. No locks needed because Python's import system handles it. The classic `__new__`-based Singleton with locks is over-engineered for most cases.

---

## Question 2 (Multiple Choice)
Your notification system needs to send alerts via email, SMS, and push notifications. The notification type is determined at runtime. Which pattern should you use?

- A) Strategy pattern — define a common interface and swap implementations at runtime
- B) Singleton — ensure one notification manager
- C) Observer — let receivers subscribe to notifications
- D) Facade — simplify the notification API

**Answer: A**
**Explanation:** Strategy defines a family of algorithms (notification channels) and makes them interchangeable. The client code selects the strategy at runtime based on configuration or user preference. This follows the Open/Closed Principle — adding a new channel like Slack means adding a new strategy, not modifying existing code.

---

## Question 3 (Multiple Choice)
In the Decorator pattern, what is the key difference from Python's `@decorator` syntax for functions?

- A) They are the same thing — both wrap behavior around objects
- B) The Decorator pattern wraps objects at the class level; `@decorator` syntax wraps functions/methods at definition time
- C) `@decorator` syntax is only for classes
- D) The Decorator pattern cannot add responsibilities dynamically

**Answer: B**
**Explanation:** The Decorator design pattern (GoF) wraps objects to add responsibilities at runtime — it operates on instances. Python's `@decorator` syntax is syntactic sugar for function wrapping at definition time. They share the concept but differ in scope and mechanism. Python's built-in `functools.wraps` makes function decorators easy; the class-level pattern requires more scaffolding.

---

## Question 4 (Multiple Choice)
You're building a chess game engine. A `Piece` class has many instances (all pawns, rooks, etc.) that share most of their state but differ in position. Which pattern minimizes memory usage?

- A) Singleton — one piece instance per type
- B) Flyweight — share intrinsic state (type, appearance) and pass extrinsic state (position) externally
- C) Builder — construct pieces step by step
- D) Command — encapsulate piece moves as objects

**Answer: B**
**Explanation:** Flyweight shares intrinsic state (piece type, color, image) among many instances while storing extrinsic state (position on board) externally. With 32 pieces, you only need a handful of type objects rather than 32 separate objects with duplicated data. This dramatically reduces memory footprint when you have thousands of similar objects.

---

## Question 5 (Code Output)
What is the output of this code?

```python
class Observer:
    def __init__(self):
        self._observers = []

    def subscribe(self, callback):
        self._observers.append(callback)

    def notify(self, event):
        for obs in self._observers:
            obs(event)

class EventEmitter(Observer):
    def emit(self, event):
        self.notify(event)

emitter = EventEmitter()
emitter.subscribe(lambda e: print(f"A:{e}", end=" "))
emitter.subscribe(lambda e: print(f"B:{e}", end=" "))
emitter.subscribe(lambda e: print(f"C:{e}", end=" "))
emitter.emit("Event1")
```

**Answer:** `A:Event1 B:Event1 C:Event1`
**Explanation:** The Observer pattern notifies all subscribers in subscription order. The base class `Observer` maintains a list of callbacks. When `emit("Event1")` is called, it iterates through the list and calls each callback in sequence. The lambda callbacks print in order: A, B, C. The order depends on when `subscribe` was called.

---

## Question 6 (Code Output)
What is the output of this code?

```python
class Singleton:
    _instance = None
    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance.value = "first"
        return cls._instance

a = Singleton()
a.value = "modified"
b = Singleton()
print(a is b, b.value)
```

**Answer:** `True modified`
**Explanation:** `__new__` controls instance creation. The first call creates the instance and sets `value = "first"`. The assignment `a.value = "modified"` mutates that single instance. The second call returns the same instance (since `_instance` is not None). So `a is b` is `True` and `b.value` is `"modified"` because both variables point to the same object. This demonstrates how Singleton enforces a single shared state.

---

## Question 7 (Bug Finding)
Find the bug in this Factory implementation:

```python
class Dog:
    def speak(self): return "Woof"

class Cat:
    def speak(self): return "Meow"

class AnimalFactory:
    def create_animal(self, animal_type):
        if animal_type == "dog":
            return Dog()
        elif animal_type == "cat":
            return Cat()

factory = AnimalFactory()
animals = [factory.create_animal("dog"), factory.create_animal("bird")]
for animal in animals:
    print(animal.speak())
```

**Bug:** The factory doesn't handle unknown animal types. `create_animal("bird")` returns `None` (no explicit return for "bird"), so calling `.speak()` on `None` raises `AttributeError: 'NoneType' object has no attribute 'speak'`. The factory silently returns `None` instead of raising a clear error.
**Fix:** Add a default case that raises `ValueError` for unknown types:
```python
def create_animal(self, animal_type):
    if animal_type == "dog": return Dog()
    elif animal_type == "cat": return Cat()
    else: raise ValueError(f"Unknown animal type: {animal_type}")
```

---

## Question 8 (Bug Finding)
Find the bug in this Strategy pattern implementation:

```python
class SortStrategy:
    def sort(self, data): pass

class BubbleSort(SortStrategy):
    def sort(self, data):
        return sorted(data)

class QuickSort(SortStrategy):
    def sort(self, data):
        return sorted(data)

class Sorter:
    def __init__(self, strategy: SortStrategy):
        self.strategy = strategy

    def set_strategy(self, strategy):
        self.strategy = strategy

    def do_sort(self, data):
        return self.strategy.sort(data)

sorter = Sorter(BubbleSort())
result1 = sorter.do_sort([3, 1, 2])
sorter.set_strategy(QuickSort())
result2 = sorter.do_sort([3, 1, 2])
print(result1 == result2)
```

**Bug:** Both `BubbleSort` and `QuickSort` use Python's built-in `sorted()`, so the output is identical — the "strategy" doesn't actually differ. In a real implementation, each strategy should have its own algorithm. The pattern is structurally correct but the strategies are functionally equivalent, making the pattern pointless here.
**Fix:** Implement genuinely different algorithms:
```python
class BubbleSort(SortStrategy):
    def sort(self, data):
        arr = data[:]
        n = len(arr)
        for i in range(n):
            for j in range(0, n-i-1):
                if arr[j] > arr[j+1]:
                    arr[j], arr[j+1] = arr[j+1], arr[j]
        return arr
```

---

## Question 9 (Scenario)
You're building a web scraper that downloads pages, extracts data, and saves results. Each step needs to be interchangeable and independently testable. The pipeline configuration changes per job. How should you architect this?

- A) Put all logic in one monolithic function
- B) Use the Chain of Responsibility pattern — each step is a handler that processes data and passes it to the next
- C) Use the Singleton pattern for a shared scraper instance
- D) Use the Observer pattern to log each step

**Answer: B**
**Explanation:** Chain of Responsibility lets you compose processing pipelines dynamically. Each handler (`DownloadHandler`, `ParseHandler`, `SaveHandler`) does one thing and passes the result to the next. You can reorder, add, or remove handlers per job without modifying existing ones. It also makes testing trivial — test each handler in isolation with mock inputs. This is the same pattern middleware frameworks like Django and Flask use.

---

## Question 10 (Architecture Decision)
You're designing an e-commerce checkout system. Orders go through: validation → inventory check → payment → shipping. Each step can fail and needs compensating actions (e.g., release inventory if payment fails). How should you architect this?

- A) Sequential function calls with try/except blocks
- B) Saga pattern — each step is a command with an execute and compensating action, orchestrated by a coordinator
- C) Chain of Responsibility — pass order through handlers
- D) Observer pattern — notify services when order progresses

**Answer: B**
**Explanation:** The Saga pattern is designed for distributed transactions with compensating actions. Each step (`ValidateSaga`, `InventorySaga`, `PaymentSaga`) has an `execute()` and `compensate()` method. If payment fails, the orchestrator runs compensate() on previous steps in reverse order. Chain of Responsibility doesn't handle rollback. Observer only notifies — it doesn't execute or compensate. Sagas are how Netflix, Uber, and e-commerce systems handle multi-step workflows with failure recovery.

---
