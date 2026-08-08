# Design Patterns Quiz

## Questions

### 1. What is the Singleton pattern's primary purpose?
A) To create multiple instances efficiently
B) To ensure a class has exactly one instance and provide a global point of access to it
C) To prevent any instance from being created
D) To destroy all instances when the program ends

**Answer**: **B** — Singleton guarantees exactly one instance exists. It's used for shared resources like configuration, logging, or connection pools. Meyer's Singleton (local static) is thread-safe in C++11+.

---

### 2. When should you use the Factory pattern?
A) When creating objects is trivial and doesn't vary
B) When object creation logic is complex, varies by type, or should be centralized for flexibility
C) When you always want to create the same type of object
D) When you need to destroy objects

**Answer**: **B** — Factory decouples object creation from usage. Use it when creation logic is complex (multiple parameters, conditional logic), when the type to create is determined at runtime, or when you want a single point for creating related objects.

---

### 3. What problem does the Observer pattern solve?
A) Object creation
B) One-to-many dependency where state changes in one object need to notify multiple dependents without tight coupling
C) Algorithm selection
D) Interface compatibility

**Answer**: **B** — Observer defines a one-to-many relationship. When the subject changes, all registered observers are notified. This decouples the subject from its observers — the subject doesn't know what observers do with the information.

---

### 4. What is the key difference between Strategy and State patterns?
A) They are identical patterns
B) Strategy selects an algorithm at runtime; State allows an object to change its behavior when its internal state changes
C) Strategy is for classes, State is for functions
D) Strategy uses inheritance, State uses composition

**Answer**: **B** — Strategy externalizes algorithm selection (you swap the algorithm). State internalizes behavior changes (the object itself transitions between states). Both use composition, but the intent differs.

---

### 5. What is the Decorator pattern used for?
A) Creating comments in code
B) Adding responsibilities to objects dynamically by wrapping them in decorator objects
C) Making code more decorative
D) Creating abstract classes

**Answer**: **B** — Decorator wraps an object to add behavior without modifying its class. Unlike inheritance, decorators can be stacked (multiple decorators) and composed at runtime. Each decorator implements the same interface as the wrapped object.

---

### 6. What is the Adapter pattern?
A) A pattern for adapting code for different compilers
B) A pattern that converts one interface into another that clients expect
C) A pattern for adapting algorithms
D) A pattern for adapting hardware

**Answer**: **B** — Adapter wraps an existing interface to make it compatible with a different interface. It's essential when integrating third-party libraries, legacy code, or systems with incompatible APIs.

---

### 7. What is the main advantage of the Factory Method over the Simple Factory?
A) It's simpler
B) It follows the Open/Closed Principle — new products can be added by creating new subclasses without modifying existing factory code
C) It uses less memory
D) It's faster

**Answer**: **B** — Simple Factory uses conditionals (`if/else` or `switch`) to create objects. Adding a new product means modifying the factory. Factory Method delegates creation to subclasses — adding a product means adding a new subclass, not modifying existing code.

---

### 8. When is the Singleton pattern an anti-pattern?
A) When you need exactly one instance
B) When it introduces hidden dependencies, makes unit testing difficult, or when multiple instances would actually be fine
C) When the class has no state
D) When the class is small

**Answer**: **B** — Singleton becomes problematic when: (1) it hides dependencies, (2) it makes testing hard (can't mock it easily), (3) it introduces global state, (4) it creates tight coupling. Consider dependency injection instead.

---

### 9. What is the Facade pattern?
A) A pattern for creating user interfaces
B) A pattern that provides a simplified interface to a complex subsystem
C) A pattern for face recognition
D) A pattern for building facades in buildings

**Answer**: **B** — Facade hides the complexity of a subsystem behind a simple interface. Clients interact with the facade instead of directly with subsystem components. This reduces coupling and makes the subsystem easier to use.

---

### 10. What is an anti-pattern in software design?
A) A pattern used in reverse engineering
B) A common but ineffective or counterproductive solution to a recurring problem that appears beneficial but has more negative consequences
C) A deprecated design pattern
D) A pattern used only for testing

**Answer**: **B** — Anti-patterns are solutions that seem good but cause problems. Examples: God Object (one class does everything), Spaghetti Code (no structure), Golden Hammer (one tool for everything), Premature Optimization.

---

## Detailed Answer Explanations

| # | Correct | Key Takeaway |
|---|---------|-------------|
| 1 | B | Singleton ensures one instance with global access |
| 2 | B | Factory centralizes complex or conditional object creation |
| 3 | B | Observer decouples subject from dependents for notifications |
| 4 | B | Strategy = algorithm selection; State = behavior per internal state |
| 5 | B | Decorator adds behavior dynamically via wrapping |
| 6 | B | Adapter converts one interface to another |
| 7 | B | Factory Method follows Open/Closed; Simple Factory violates it |
| 8 | B | Singleton hinders testing and introduces hidden dependencies |
| 9 | B | Facade simplifies complex subsystems behind one interface |
| 10 | B | Anti-patterns appear useful but cause more problems than they solve |
