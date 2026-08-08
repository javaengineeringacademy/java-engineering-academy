# Module 11: Design Patterns

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 30 min | **Practice:** 60 min | **Total:** 90 min

## Overview
Developers repeatedly encounter the same design problems: creating objects efficiently, adapting incompatible interfaces, notifying multiple objects of state changes. Design patterns are proven, reusable solutions to these recurring problems, giving teams a shared vocabulary and battle-tested approaches. This module covers all 23 Gang of Four (GoF) patterns with real-world examples.

## Learning Objectives
- Identify which pattern category (creational, structural, behavioral) solves a given design problem
- Implement Singleton, Factory, Builder, and other creational patterns with thread safety in mind
- Apply Adapter, Decorator, Facade, and Proxy patterns to structure classes and manage complexity
- Use Strategy, Observer, Command, and other behavioral patterns to decouple components
- Recognize anti-patterns and avoid over-engineering when simpler code suffices

## Prerequisites
- OOP concepts
- Java fundamentals
- Problem-solving skills

---

## Pattern Categories

### Creational Patterns (5)
| Pattern | Purpose | Use Case |
|---------|---------|----------|
| Singleton | Single instance | Configuration, Logger |
| Factory Method | Create objects without specifying class | Shape creation, Payment processing |
| Abstract Factory | Create families of related objects | Cross-platform UI, Database drivers |
| Builder | Construct complex objects step by step | HTTP requests, SQL queries |
| Prototype | Clone existing objects | Copying complex objects |

### Structural Patterns (7)
| Pattern | Purpose | Use Case |
|---------|---------|----------|
| Adapter | Convert one interface to another | Legacy integration, Third-party APIs |
| Bridge | Separate abstraction from implementation | Cross-platform rendering, Database drivers |
| Composite | Treat individual objects uniformly | File systems, UI components |
| Decorator | Add behavior dynamically | I/O streams, Coffee shop |
| Facade | Simplify complex subsystems | Computer startup, Library API |
| Flyweight | Share common state | Text editors, Game objects |
| Proxy | Control access to object | Caching, Logging, Security |

### Behavioral Patterns (11)
| Pattern | Purpose | Use Case |
|---------|---------|----------|
| Chain of Responsibility | Pass request along chain | Support ticket handling, Middleware |
| Command | Encapsulate requests as objects | Undo/Redo, Task queues |
| Iterator | Access elements sequentially | Collections, Database results |
| Mediator | Centralize complex communications | Chat rooms, Air traffic control |
| Memento | Capture and restore state | Undo/Redo, Checkpoints |
| Observer | Notify dependents of changes | Event systems, Pub/Sub |
| State | Change behavior based on state | Order processing, Game states |
| Strategy | Define family of algorithms | Sorting, Payment methods |
| Template Method | Define algorithm skeleton | Data processing, Testing frameworks |
| Visitor | Add operations to objects | AST traversal, Report generation |

---

## Architecture Diagram

```mermaid
graph TD
    A[Design Patterns] --> B[Creational - 5]
    A --> C[Structural - 7]
    A --> D[Behavioral - 11]
    
    B --> B1[Singleton]
    B --> B2[Factory Method]
    B --> B3[Abstract Factory]
    B --> B4[Builder]
    B --> B5[Prototype]
    
    C --> C1[Adapter]
    C --> C2[Bridge]
    C --> C3[Composite]
    C --> C4[Decorator]
    C --> C5[Facade]
    C --> C6[Flyweight]
    C --> C7[Proxy]
    
    D --> D1[Chain of Responsibility]
    D --> D2[Command]
    D --> D3[Iterator]
    D --> D4[Mediator]
    D --> D5[Memento]
    D --> D6[Observer]
    D --> D7[State]
    D --> D8[Strategy]
    D --> D9[Template Method]
    D --> D10[Visitor]
```

---

## Creational Patterns

### 1. Singleton Pattern
Ensures only one instance of a class exists.

**Flavors:**
- Eager initialization
- Lazy initialization
- Thread-safe (double-checked locking)
- Enum singleton
- Bill Pugh singleton

**When to use:**
- Configuration management
- Logging
- Connection pooling
- Cache

### 2. Factory Method Pattern
Defines interface for creating objects, lets subclasses decide which class to instantiate.

**Flavors:**
- Simple Factory
- Factory Method
- Parameterized Factory

**When to use:**
- When you don't know exact types at compile time
- Want to provide flexibility for extension
- Reduce coupling

### 3. Abstract Factory Pattern
Creates families of related objects without specifying concrete classes.

**Flavors:**
- Simple Abstract Factory
- Factory Registry

**When to use:**
- Cross-platform UI components
- Database driver families
- Theme systems

### 4. Builder Pattern
Separates construction of complex object from its representation.

**Flavors:**
- Step-by-step Builder
- Fluent Builder (Method chaining)
- Builder with validation
- Lombok @Builder

**When to use:**
- Complex object creation
- Many constructor parameters
- Immutable objects

### 5. Prototype Pattern
Creates new objects by cloning existing instances.

**Flavors:**
- Shallow clone
- Deep clone
- Clone with registry

**When to use:**
- Expensive object creation
- Avoiding subclasses
- Copying complex objects

---

## Structural Patterns

### 6. Adapter Pattern
Converts one interface to another expected by clients.

**Flavors:**
- Class adapter (inheritance)
- Object adapter (composition)
- Two-way adapter

**When to use:**
- Legacy system integration
- Third-party library adaptation
- Data format conversion

### 7. Bridge Pattern
Separates abstraction from implementation so both can vary independently.

**Flavors:**
- Simple Bridge
- Bridge with factory

**When to use:**
- Cross-platform rendering
- Multiple database drivers
- Platform-independent APIs

### 8. Composite Pattern
Composes objects into tree structures and treats individual objects uniformly.

**Flavors:**
- Safe composite (has add/remove methods)
- Unsafe composite (all components have add/remove)

**When to use:**
- File systems
- UI component trees
- Organization hierarchies

### 9. Decorator Pattern
Adds behavior to objects dynamically without modifying their structure.

**Flavors:**
- Simple Decorator
- Stackable Decorators
- Transparent Decorator

**When to use:**
- I/O streams
- Logging
- Coffee shop ordering
- Feature toggles

### 10. Facade Pattern
Provides simplified interface to complex subsystem.

**Flavors:**
- Simple Facade
- Facade with caching
- Abstract Facade

**When to use:**
- Simplifying complex libraries
- Legacy system wrapper
- Service layer

### 11. Flyweight Pattern
Minimizes memory usage by sharing common state.

**Flavors:**
- Simple Flyweight
- Flyweight with factory
- Composite Flyweight

**When to use:**
- Text editors (character formatting)
- Game objects (shared textures)
- String pool

### 12. Proxy Pattern
Provides surrogate or placeholder for another object to control access.

**Flavors:**
- Virtual proxy (lazy loading)
- Remote proxy (network access)
- Protection proxy (access control)
- Caching proxy
- Logging proxy

**When to use:**
- Lazy initialization
- Access control
- Logging and auditing
- Caching

---

## Behavioral Patterns

### 13. Chain of Responsibility
Passes request along chain until handled.

**Flavors:**
- Simple chain
- Graph-based chain
- Pipeline

**When to use:**
- Support ticket escalation
- Middleware processing
- Event bubbling

### 14. Command Pattern
Encapsulates requests as objects, enabling undo/redo.

**Flavors:**
- Simple Command
- Composite command
- Macro command
- Undoable command

**When to use:**
- Undo/Redo functionality
- Task queues
- Transaction processing

### 15. Iterator Pattern
Provides way to access elements sequentially without exposing representation.

**Flavors:**
- Simple iterator
- Filtering iterator
- Reverse iterator
- Tree iterator

**When to use:**
- Collection traversal
- Database result sets
- Custom data structures

### 16. Mediator Pattern
Defines simplified communication between objects.

**Flavors:**
- Mediator with registration
- Event-based mediator

**When to use:**
- Chat rooms
- Air traffic control
- UI form validation

### 17. Memento Pattern
Captures and externalizes object state for later restoration.

**Flavors:**
- Simple memento
- Multi-state memento
- Checkpoint memento

**When to use:**
- Undo/Redo
- Game saves
- Transaction rollbacks

### 18. Observer Pattern
Defines one-to-many dependency between objects.

**Flavors:**
- Pull observer
- Push observer
- Event bus

**When to use:**
- Event systems
- Pub/Sub messaging
- UI data binding

### 19. State Pattern
Allows object to change behavior when internal state changes.

**Flavors:**
- Simple state machine
- State with transitions
- State table

**When to use:**
- Order processing
- Game character states
- TCP connections

### 20. Strategy Pattern
Defines family of algorithms and makes them interchangeable.

**Flavors:**
- Simple strategy
- Strategy with factory
- Strategy with lambda

**When to use:**
- Sorting algorithms
- Payment methods
- Routing algorithms

### 21. Template Method Pattern
Defines algorithm skeleton with steps deferred to subclasses.

**Flavors:**
- Simple template
- Hook methods
- Method delegation

**When to use:**
- Data processing pipelines
- Testing frameworks
- Build processes

### 22. Visitor Pattern
Defines new operations on object structure without modifying classes.

**Flavors:**
- Simple visitor
- Acyclic visitor
- Reflective visitor

**When to use:**
- AST traversal
- Report generation
- Object structure operations

---

## Pattern Selection Guide

```mermaid
flowchart TD
    Start[Design Problem] --> Q1{Creating Objects?}
    
    Q1 -->|"Yes"| Creational
    Q1 -->|"No"| Q2{Structuring Classes/Objects?}
    Q2 -->|"Yes"| Structural
    Q2 -->|"No"| Q3{Communicating Between Objects?}
    Q3 -->|"Yes"| Behavioral
    
    subgraph Creational["Creational Patterns"]
        direction TB
        Q1A{Single Instance?}
        Q1A -->|"Yes"| Singleton["Singleton"]
        Q1A -->|"No"| Q1B{Create Without Specifying Class?}
        Q1B -->|"Yes"| Factory["Factory Method"]
        Q1B -->|"No"| Q1C{Create Families of Objects?}
        Q1C -->|"Yes"| AbstractFactory["Abstract Factory"]
        Q1C -->|"No"| Q1D{Complex Object Construction?}
        Q1D -->|"Yes"| Builder["Builder"]
        Q1D -->|"No"| Prototype["Prototype"]
    end
    
    subgraph Structural["Structural Patterns"]
        direction TB
        Q2A{Adapt Interface?}
        Q2A -->|"Yes"| Adapter["Adapter"]
        Q2A -->|"No"| Q2B{Simplify Complex System?}
        Q2B -->|"Yes"| Facade["Facade"]
        Q2B -->|"No"| Q2C{Add Behavior Dynamically?}
        Q2C -->|"Yes"| Decorator["Decorator"]
        Q2C -->|"No"| Q2D{Control Access?}
        Q2D -->|"Yes"| Proxy["Proxy"]
        Q2D -->|"No"| Q2E{Share Common State?}
        Q2E -->|"Yes"| Flyweight["Flyweight"]
        Q2E -->|"No"| Q2F{Tree Structure?}
        Q2F -->|"Yes"| Composite["Composite"]
        Q2F -->|"No"| Bridge["Bridge"]
    end
    
    subgraph Behavioral["Behavioral Patterns"]
        direction TB
        Q3A{Pass Request Along Chain?}
        Q3A -->|"Yes"| ChainOfResp["Chain of Responsibility"]
        Q3A -->|"No"| Q3B{Encapsulate Request?}
        Q3B -->|"Yes"| Command["Command"]
        Q3B -->|"No"| Q3C{Notify Multiple Objects?}
        Q3C -->|"Yes"| Observer["Observer"]
        Q3C -->|"No"| Q3D{Change Behavior by State?}
        Q3D -->|"Yes"| State["State"]
        Q3D -->|"No"| Q3E{Define Algorithm Variants?}
        Q3E -->|"Yes"| Strategy["Strategy"]
        Q3E -->|"No"| Q3F{Define Algorithm Skeleton?}
        Q3F -->|"Yes"| TemplateMethod["Template Method"]
        Q3F -->|"No"| Q3G{Centralize Communication?}
        Q3G -->|"Yes"| Mediator["Mediator"]
        Q3G -->|"No"| Q3H{Traverse Structure?}
        Q3H -->|"Yes"| Visitor["Visitor"]
        Q3H -->|"No"| Q3I{Save/Restore State?}
        Q3I -->|"Yes"| Memento["Memento"]
        Q3I -->|"No"| Iterator["Iterator"]
    end
    
    style Singleton fill:#ffcdd2
    style Factory fill:#c8e6c9
    style Builder fill:#bbdefb
    style Adapter fill:#fff9c4
    style Facade fill:#e1bee7
    style Decorator fill:#b2dfdb
    style Observer fill:#ffe0b2
    style Strategy fill:#d1c4e9
    style Command fill:#f0f4c3
```

---

**Continue to Part 2**: README-part2.md

## Interview Questions

1. **What is the Singleton pattern and when would you use it?**
   Singleton ensures only one instance exists. Use for configuration, logging, connection pools. Be careful with testing and thread safety. Prefer dependency injection over Singleton in modern applications.

2. **When would you use Factory Method vs Abstract Factory?**
   Factory Method creates one product via subclassing. Abstract Factory creates families of related objects. Use Factory Method when you need one product; Abstract Factory when you need a cohesive set (e.g., cross-platform UI components).

3. **What are the alternatives to the Builder pattern?**
   Lombok `@Builder`, telescoping constructors, records (for immutable objects), factory methods, and the step builder pattern. Records reduce Builder need for simple DTOs.

4. **What are common mistakes with design patterns?**
   Over-engineering (using patterns when simple code suffices), forcing wrong pattern, not following SOLID principles, creating God classes, and tight coupling through pattern misuse.

5. **How does the Strategy pattern differ from polymorphism?**
   Strategy uses composition and delegates behavior to interchangeable objects. Polymorphism uses inheritance. Strategy is more flexible — you can change behavior at runtime and combine behaviors.

6. **When should you NOT use the Observer pattern?**
   When you need synchronous guaranteed delivery, when order matters strictly, or when overused causing cascading updates that are hard to debug. Consider event sourcing or reactive streams instead.

7. **What is the difference between Adapter and Facade?**
   Adapter converts one interface to another (works with existing interfaces). Facade provides a simplified interface to a complex subsystem (defines its own simplified interface).

8. **How do you decide which pattern to use for a design problem?**
   Identify the problem category (creational, structural, behavioral), consider the specific constraints (thread safety, performance, extensibility), evaluate trade-offs, and start with the simplest solution that works.

9. **What are anti-patterns in the context of design patterns?**
   Golden Hammer (overusing one pattern), Abstract Leak (abstraction exposes details), Singleton abuse (global state), God Object (one class doing too much), and Analysis Paralysis (over-designing before coding).

10. **How do design patterns relate to SOLID principles?**
    Each pattern embodies specific SOLID principles: Strategy follows OCP (open/closed), Facade follows ISP (interface segregation), Factory follows SRP (single responsibility), Decorator follows OCP.

## Pitfalls

- **Over-engineering**: Using design patterns for simple problems adds unnecessary complexity. A simple if-else is sometimes better than a Strategy pattern.
- **Wrong pattern choice**: Using Singleton when you need testability, or Observer when you need guaranteed delivery.
- **God classes**: Combining too many patterns in one class, violating SRP.
- **Tight coupling through Singleton**: Global state makes unit testing difficult. Prefer dependency injection.
- **Ignoring thread safety**: Many patterns (Singleton, Observer) need explicit thread safety handling.
- **Premature abstraction**: Abstracting too early leads to wrong abstractions that are costly to change.
- **Pattern soup**: Applying every known pattern to a simple problem, making code harder to understand.
- **Not considering evolution**: Patterns chosen for today's requirements may not fit tomorrow's needs.

## Performance

| Pattern | Performance Impact | Notes |
|---------|-------------------|-------|
| Singleton | O(1) access | Thread safety adds synchronization cost |
| Factory Method | O(1) | Minimal overhead, creates objects |
| Builder | O(n) fields | Slight overhead for immutability |
| Proxy | O(1) per call | Adds indirection; caching proxy improves performance |
| Flyweight | Memory savings | Significant for large numbers of shared objects |
| Observer | O(n) notification | Linear in number of observers; can cause cascading updates |
| Strategy | O(1) | Composition overhead is negligible |
| Decorator | O(1) per layer | Stack depth adds indirection |
| Composite | O(n) traversal | Depends on tree depth and branching factor |

**Key insights:**
- Most patterns add minimal runtime overhead
- Flyweight and Object Pool patterns can significantly reduce memory/GC pressure
- Observer pattern can cause performance issues with many subscribers
- Proxy pattern can improve performance when combined with caching
- Benchmark before optimizing — pattern overhead is usually negligible

## Examples

```java
// Strategy Pattern — interchangeable algorithms
interface SortStrategy {
    void sort(int[] array);
}
class BubbleSort implements SortStrategy {
    public void sort(int[] array) { /* O(n^2) */ }
}
class QuickSort implements SortStrategy {
    public void sort(int[] array) { /* O(n log n) avg */ }
}
class Sorter {
    private SortStrategy strategy;
    public Sorter(SortStrategy strategy) { this.strategy = strategy; }
    public void sort(int[] array) { strategy.sort(array); }
}

// Observer Pattern — event notification
interface EventListener {
    void onEvent(String event);
}
class EventBus {
    private final List<EventListener> listeners = new ArrayList<>();
    public void subscribe(EventListener l) { listeners.add(l); }
    public void publish(String event) {
        listeners.forEach(l -> l.onEvent(event));
    }
}

// Decorator Pattern — add behavior dynamically
interface DataSource {
    void writeData(String data);
    String readData();
}
class FileDataSource implements DataSource {
    private String filename;
    public FileDataSource(String f) { this.filename = f; }
    public void writeData(String data) { /* write to file */ }
    public String readData() { return ""; /* read from file */ }
}
class EncryptionDecorator implements DataSource {
    private DataSource wrapped;
    public EncryptionDecorator(DataSource source) { this.wrapped = source; }
    public void writeData(String data) {
        String encrypted = encrypt(data);
        wrapped.writeData(encrypted);
    }
    public String readData() {
        String data = wrapped.readData();
        return decrypt(data);
    }
    private String encrypt(String s) { return s; /* encrypt */ }
    private String decrypt(String s) { return s; /* decrypt */ }
}
```

## Internal Working

**How patterns work under the hood:**

- **Singleton**: Uses class loading guarantees and volatile + double-checked locking (or enum) to ensure single instance. JVM class loader ensures one instance per classloader.
- **Factory Method**: Relies on dynamic dispatch — the JVM calls the appropriate subclass implementation at runtime via virtual method invocation.
- **Strategy**: Uses composition and interface polymorphism. The context holds a reference to a strategy interface; actual implementation is resolved at runtime.
- **Observer**: Maintains a list of registered listeners. When state changes, iterates through listeners. The subject holds references to observer interfaces.
- **Decorator**: Wraps objects implementing the same interface. Each decorator delegates to the wrapped object, adding behavior before/after.
- **Proxy**: Creates a surrogate class (often via java.lang.reflect.Proxy or cglib) that implements the same interface, intercepting method calls.
- **Composite**: Defines a tree structure where leaf and composite nodes implement the same interface. Operations are applied recursively.

## Why This Concept Exists

Design patterns exist because software developers repeatedly encounter the same design problems across different projects. Instead of reinventing solutions, patterns provide:

1. **Proven solutions** — Battle-tested approaches that handle edge cases and known pitfalls
2. **Common vocabulary** — A shared language for developers to communicate design decisions efficiently
3. **Best practices** — Encapsulated wisdom about handling extensibility, flexibility, and maintainability
4. **Reduced complexity** — Frameworks and libraries use patterns internally; understanding them helps understand the ecosystem
5. **Design principles** — Patterns embody SOLID, DRY, and other principles in practical, applicable forms

Without patterns, every team would solve the same problems differently, making codebases harder to maintain and onboard new developers.

## References

- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF)](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [Head First Design Patterns](https://www.amazon.com/First-Design-Patterns-Brain-Friendly/dp/0596007124)
- [Refactoring.Guru — Design Patterns](https://refactoring.guru/design-patterns)
- [SourceMaking — Design Patterns](https://sourcemaking.com/design_patterns)
- [Java Design Patterns — GitHub](https://github.com/iluwatar/java-design-patterns)

## Cross-References

- **Previous Module:** [10 - JVM Internals](../10-jvm-internals/)

## Prerequisites

- [OOP](../02-oop/README.md)

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Singleton causing test pollution | Dependency injection refactor | Replace singleton with DI-managed scoped beans; reset state in test setup |
| Observer memory leak | Heap dump + reference tracing | Identify listeners not being unregistered; check for strong references |
| Factory creating excessive objects | Allocation profiling (JFR) | Track object creation rate; identify factory hotspots |
| Over-engineered pattern usage | Code review + complexity metrics | Evaluate if simpler code would work; measure cyclomatic complexity |
| Strategy pattern not swapping correctly | Step-through debugger | Verify strategy assignment; check if context holds correct reference |

## Code Review Checklist

- [ ] Patterns solve real problems, not hypothetical ones
- [ ] Dependency injection used instead of singleton for testability
- [ ] Observer listeners properly unregistered on component destroy
- [ ] Factory methods have appropriate object pooling or caching
- [ ] Strategy implementations are thread-safe if shared
- [ ] Decorator stack depth is reasonable (not excessive indirection)
- [ ] No God classes combining multiple pattern responsibilities

## Architecture Considerations

Design patterns are architectural building blocks. At scale, pattern selection affects system modifiability, testability, and performance. For microservices, Factory and Abstract Factory patterns enable service implementation flexibility. Strategy patterns enable runtime behavior changes without redeployment. Observer patterns power event-driven architectures and pub/sub messaging.

In enterprise systems, patterns compose to form architectural styles — MVC (Model-View-Controller) combines Observer, Strategy, and Composite. Clean Architecture uses Factory, Strategy, and Adapter patterns at different layers. Understanding pattern trade-offs at scale prevents over-engineering while ensuring extensibility.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Strategy | Algorithm selection at runtime | Pros: Flexible, testable; Cons: More classes, indirection |
| Observer | Event notification | Pros: Decoupled, extensible; Cons: Debugging complexity, memory leaks |
| Factory | Object creation abstraction | Pros: Encapsulates creation; Cons: Adds abstraction layer |
| Decorator | Dynamic behavior addition | Pros: Stackable, composable; Cons: Many small classes, indirection |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Singleton global state in security contexts | Unauthorized access, state tampering | Use DI with request-scoped beans; avoid mutable singletons |
| Observer pattern leaking sensitive data | Information disclosure | Use weak references; implement proper listener cleanup |
| Proxy pattern bypassing security | Security check bypass | Ensure proxy applies security checks before delegation |
| Factory creating unauthorized instances | Privilege escalation | Validate inputs in factory methods; implement access controls |
| Strategy pattern with untrusted implementations | Code injection | Validate strategy implementations; use whitelisting |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0–1.4 | Manual pattern implementation | Adopt framework-supported patterns (Spring, etc.) |
| Java 5 | Enums for Singleton pattern | Replace complex singleton implementations with enum singleton |
| Java 8 | Lambda-based Strategy pattern | Replace anonymous Strategy classes with lambdas |
| Java 9 | Module system | Define module boundaries for pattern implementations |
| Java 14 | Records for immutable DTOs | Replace Builder pattern for simple data carriers with records |
| Java 17 | Sealed classes for State pattern | Use sealed classes to restrict state implementations |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Enum-based Singleton | Java 5 | Stable |
| Lambda-based Strategy | Java 8 | Stable |
| Records for DTOs | Java 16 | Stable |
| Sealed classes for State | Java 17 | Stable |
| Pattern matching for Visitor | Java 21 | Preview |
| Virtual threads with patterns | Java 21 | Stable |

## Production Incidents

### Incident 1: Singleton Causing Testing Difficulties

**Problem:** A unit test suite failed intermittently because `DatabaseConnection` singleton retained state between tests.
**Cause:** Singleton held database connection; tests didn't reset state; previous test's data affected next test.
**Impact:** 30% of tests failed non-deterministically; CI pipeline unreliable; development velocity decreased.
**Detection:** Flaky tests in CI; investigation revealed singleton state leakage.
**Solution:** Refactored to use dependency injection; singleton replaced with scoped instances per test.
**Prevention:** Avoid singleton for stateful objects; use dependency injection; reset state in test setup.

### Incident 2: Observer Pattern Memory Leak

**Problem:** A notification system leaked memory because event listeners were never unregistered.
**Cause:** `EventBus` held strong references to listeners; listeners were never removed after component destruction.
**Impact:** Memory usage grew 10% per hour; application crashed after 10 hours; required restart.
**Detection:** Heap dumps showed thousands of listener instances; memory profiler revealed leak.
**Solution:** Used `WeakReference` for listeners; added explicit `unregister()` method; implemented cleanup on component destroy.
**Prevention:** Use weak references for callbacks; implement `Closeable` for cleanup; document listener lifecycle.

### Incident 3: Factory Pattern Creating Too Many Objects

**Problem:** A factory pattern created thousands of objects per request, causing GC pressure and slow response times.
**Cause:** Factory created new objects for each request instead of reusing; no object pooling implemented.
**Impact:** Response times increased 5x; GC overhead 40%; SLA violations.
**Detection:** Performance profiling showed object creation overhead; GC logs showed frequent collections.
**Solution:** Implemented object pool pattern; cached frequently used objects; reduced object creation.
**Prevention:** Profile object creation in hot paths; use object pooling for expensive objects; implement caching.

## Production Checklist

- [ ] Use dependency injection instead of singleton for testability
- [ ] Document thread-safety guarantees for each pattern
- [ ] Avoid over-engineering — use simple code when patterns aren't needed
- [ ] Implement proper cleanup for patterns with callbacks/listeners
- [ ] Test patterns with multiple implementations
- [ ] Monitor performance impact of pattern overhead
- [ ] Use appropriate pattern for the problem — don't force patterns
- [ ] Document pattern usage and rationale in code
- [ ] Consider evolution — patterns should accommodate changing requirements
- [ ] Review patterns in code reviews for anti-patterns

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Knows basic patterns; uses Singleton everywhere; doesn't understand trade-offs |
| Intermediate | Applies appropriate patterns; understands when NOT to use patterns; uses composition |
| Advanced | Combines patterns effectively; designs flexible architectures; teaches patterns |
| Expert | Creates new patterns; contributes to pattern literature; architects complex systems |

## Common Myths

1. **Myth**: Design patterns are always necessary
   **Truth**: Over-engineering adds unnecessary complexity. Use patterns only when they solve a real problem.

2. **Myth**: Singleton is the best pattern for shared resources
   **Truth**: Singleton causes testing difficulties and tight coupling. Prefer dependency injection for shared resources.

3. **Myth**: More patterns mean better design
   **Truth**: Pattern soup makes code harder to understand. Simple code is better when appropriate.

4. **Myth**: Patterns are language-agnostic
   **Truth**: Some patterns are less relevant in Java due to language features (e.g., Strategy with lambdas).

5. **Myth**: Once applied, patterns should never change
   **Truth**: Design evolves; patterns should be refactored as requirements change.

## Related Topics

- [Senior](../15-senior/README.md)

## Next

- [Testing](../12-testing/README.md)
- [Senior](../15-senior/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Reusable solutions to common problems |
| Complexity | Varies |
| Thread Safe | Varies |
| Ordered | N/A |
| Allows Null | Varies |
| Best Alternative | Simple code (for simple cases) |
| When to Use | Common problems |
| When to Avoid | Over-engineering |
