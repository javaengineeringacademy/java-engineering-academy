# Design Patterns Exercises

Practice design patterns in Java through hands-on exercises.

## Exercise 1: Singleton Pattern (3 Approaches)

**Problem Statement:**
Implement the Singleton pattern using three different approaches: eager initialization, lazy initialization with double-checked locking, andBill Pugh's static inner class approach. Compare their thread safety, laziness, and performance characteristics.

**Expected Behavior:**
- Eager singleton is created at class load time and is always thread-safe.
- Double-checked locking uses `volatile` and synchronized block for lazy init.
- Static inner class approach is lazy and thread-safe without synchronization overhead.
- All three approaches return the same instance across multiple calls.
- Reflection and deserialization attacks are defended against.

**Hints:**
- For double-checked locking, use `volatile` on the instance field.
- For the inner class approach, the holder class is only loaded when accessed.
- Override `readResolve()` to prevent duplicate instances via deserialization.
- Throw an exception in the constructor if a second instance is attempted.

---

## Exercise 2: Factory Pattern for Payment Processing

**Problem Statement:**
Create a payment processing system using the Factory pattern. Define a `PaymentProcessor` interface with implementations for Credit Card, PayPal, and Bank Transfer. A `PaymentProcessorFactory` creates the correct processor based on payment type.

**Expected Behavior:**
- `PaymentProcessorFactory.create("CREDIT_CARD")` returns a `CreditCardProcessor`.
- `PaymentProcessorFactory.create("PAYPAL")` returns a `PayPalProcessor`.
- `PaymentProcessorFactory.create("BANK_TRANSFER")` returns a `BankTransferProcessor`.
- Each processor implements `processPayment(double amount)` with type-specific logic.
- An unknown payment type throws `IllegalArgumentException`.

**Hints:**
- Use a `switch` statement or `Map<String, Supplier<PaymentProcessor>>` in the factory.
- Each processor should validate its own payment details.
- Add a `validate()` method to the interface for pre-processing validation.
- Consider using an enum for payment types for type safety.

---

## Exercise 3: Observer Pattern for Event System

**Problem Statement:**
Implement an event system using the Observer pattern. Create a `StockMarket` (subject) that notifies multiple `Investor` (observers) when stock prices change. Support adding, removing, and notifying observers.

**Expected Behavior:**
- When a stock price changes, all registered investors are notified.
- Each investor receives the stock symbol, old price, and new price.
- Investors can be added and removed at runtime.
- Notification order is the order of registration.
- An investor that throws an exception does not prevent others from being notified.

**Hints:**
- Use a `List<Investor>` to store observers in the subject.
- Call ` investor.update(symbol, oldPrice, newPrice)` for each observer.
- Catch exceptions per-observer to isolate failures.
- Use `Observable` class or implement your own subject with `addObserver`/`removeObserver`.

---

## Exercise 4: Strategy Pattern for Sorting Algorithms

**Problem Statement:**
Implement the Strategy pattern to support multiple sorting algorithms. Create a `Sorter` class that accepts different sorting strategies (Bubble Sort, Merge Sort, Quick Sort) at runtime. Benchmark each strategy on the same dataset.

**Expected Behavior:**
- `Sorter` accepts any `SortingStrategy` via its constructor or setter.
- Each strategy implements `sort(int[] array)` and sorts in-place.
- All strategies produce the same sorted output.
- Benchmark shows Merge Sort and Quick Sort are faster than Bubble Sort for large arrays.
- Strategies can be swapped at runtime without changing the Sorter class.

**Hints:**
- Define a `SortingStrategy` functional interface with `void sort(int[] array)`.
- Implement each algorithm as a separate class or lambda expression.
- Use `System.nanoTime()` to measure sort time for each strategy.
- Test with arrays of size 100, 1000, and 10000.

---

## Exercise 5: Decorator Pattern for Logging

**Problem Statement:**
Build a logging framework using the Decorator pattern. Start with a `ConsoleLogger` base implementation, then add decorators for timestamping, uppercasing messages, and filtering by log level.

**Expected Behavior:**
- `ConsoleLogger.log("hello")` prints `hello` to console.
- `TimestampLogger` decorator prepends a timestamp to each message.
- `UppercaseLogger` decorator converts messages to uppercase.
- `LevelFilterLogger` decorator only passes messages above a certain level.
- Decorators can be stacked: `new TimestampLogger(new UppercaseLogger(new ConsoleLogger()))`.

**Hints:**
- Define a `Logger` interface with `void log(String message)`.
- Each decorator wraps a `Logger` instance and delegates after adding behavior.
- Pass the log level as a parameter to the filter decorator.
- Use constructor injection for the wrapped logger in each decorator.

---

## Exercise 6: Adapter Pattern for Legacy Integration

**Problem Statement:**
You have a modern `DataAnalyzer` interface that expects `DataStream` objects. Create an adapter to integrate a legacy `LegacyDataReader` that uses a different API (reading from a `String[]` array with index-based access).

**Expected Behavior:**
- `LegacyDataReaderAdapter` implements `DataStream` and wraps `LegacyDataReader`.
- The adapter translates `readNext()` calls to legacy `getNextRecord(index)` calls.
- The adapter handles index management internally.
- `DataAnalyzer` processes data from the adapter without knowing about the legacy API.
- The adapter is transparent to the `DataAnalyzer` consumer.

**Hints:**
- Implement `DataStream` interface and hold a `LegacyDataReader` reference.
- Maintain an internal index counter that increments on each `readNext()`.
- Translate data formats if the legacy reader returns a different structure.
- Add `hasNext()` support by catching end-of-data exceptions from the legacy reader.

---

## Exercise 7: Proxy Pattern for Access Control

**Problem Statement:**
Implement a protection proxy for a `DocumentService` that controls access based on user roles. The proxy should check permissions before delegating to the real service.

**Expected Behavior:**
- `DocumentServiceProxy` wraps the real `DocumentService`.
- Read operations require VIEWER role or higher.
- Write operations require EDITOR role or higher.
- Delete operations require ADMIN role only.
- Unauthorized access throws `AccessDeniedException` with the user and operation details.
- The proxy logs all access attempts for auditing.

**Hints:**
- Create a `User` class with a `Role` enum (VIEWER, EDITOR, ADMIN).
- The proxy checks `user.getRole().compareTo(requiredRole) >= 0`.
- Use `java.lang.reflect.Proxy` for a dynamic proxy or a static proxy class.
- Log each access attempt with timestamp, user, operation, and result.

---

## Exercise 8: Command Pattern for Undo/Redo

**Problem Statement:**
Implement a text editor with undo/redo functionality using the Command pattern. Create commands for InsertText, DeleteText, and ReplaceText, each supporting execute and undo operations.

**Expected Behavior:**
- `InsertText.execute()` adds text at a position; `undo()` removes it.
- `DeleteText.execute()` removes text; `undo()` restores it.
- `ReplaceText.execute()` replaces text; `undo()` restores the original.
- A `CommandHistory` stack supports undo (pop from undo stack, push to redo stack).
- Redo re-executes the last undone command.
- The editor state is correct after any sequence of operations.

**Hints:**
- Each command stores the document state it needs to reverse the operation.
- Use two stacks: `undoStack` and `redoStack` in the `CommandHistory`.
- Clear the redo stack when a new command is executed.
- Store the deleted text in `DeleteText` to enable undo restoration.

---

## Exercise 9: State Pattern for Order Workflow

**Problem Statement:**
Implement an order processing system using the State pattern. An `Order` can be in states: Pending, Processing, Shipped, Delivered, and Cancelled. Each state defines the allowed transitions and behavior.

**Expected Behavior:**
- Pending orders can be processed or cancelled.
- Processing orders can be shipped or cancelled.
- Shipped orders can be delivered (no cancellation allowed).
- Delivered orders are final (no transitions allowed).
- Cancelled orders are final (no transitions allowed).
- Attempting an invalid transition throws `IllegalStateException`.

**Hints:**
- Define an `OrderState` interface with methods for each possible transition.
- Each state class implements only the transitions it allows.
- The `Order` class holds a reference to its current state and delegates calls.
- Invalid transitions throw exceptions with descriptive messages.

---

## Exercise 10: Composite Pattern for File System

**Problem Statement:**
Implement a file system representation using the Composite pattern. Create `File` and `Directory` components that share a common `FileSystemEntry` interface. Support operations like `getSize()`, `display(int indent)`, and `findByName(String name)`.

**Expected Behavior:**
- `File` returns its size in bytes from `getSize()`.
- `Directory` returns the sum of all contained entries' sizes.
- `display()` prints the tree structure with proper indentation.
- `findByName()` recursively searches directories and returns matching entries.
- A directory can contain files and other directories (nested composites).
- Empty directories return size 0 from `getSize()`.

**Hints:**
- Define `FileSystemEntry` with `getName()`, `getSize()`, `display(int indent)`.
- `Directory` maintains a `List<FileSystemEntry>` for children.
- `display()` uses `indent * 2` spaces for each level of nesting.
- `findByName()` recurses into directories and collects matching entries.

## Interview Questions

1. **What is the Singleton pattern and why is it considered an anti-pattern by some?**
   Singleton ensures a class has only one instance and provides a global point of access. It's criticized for introducing global state, making testing difficult, and violating the Single Responsibility Principle. Use it sparingly—for configuration, connection pools, or logging—where a single instance truly makes sense.

2. **What is the difference between Factory and Abstract Factory patterns?**
   Factory Method creates objects of a single type via a method (e.g., `createPaymentProcessor("CREDIT_CARD")`). Abstract Factory creates families of related objects without specifying concrete classes (e.g., `PaymentFactory` produces `Processor`, `Validator`, and `Notifier` for a given payment method). Abstract Factory is used when the system must remain independent of how its products are created.

3. **When should you use the Strategy pattern instead of conditionals?**
   Use Strategy when you have multiple algorithms for the same operation and want to switch them at runtime without modifying the context class. If you have a simple if-else with 2-3 cases that rarely change, a conditional is simpler. Strategy excels when algorithms may be added later or when the context shouldn't know about algorithm details (Open/Closed Principle).

4. **What is the difference between Decorator and Proxy patterns?**
   Decorator adds behavior dynamically while maintaining the same interface (e.g., wrapping a logger with timestamp functionality). Proxy controls access to the real object (e.g., lazy loading, access control, caching). The key difference: Decorator focuses on adding behavior, Proxy focuses on controlling access. Both wrap an object but with different intentions.

5. **What are common mistakes when implementing design patterns?**
   - Overusing Singleton for global state
   - Creating "God Object" classes that implement too many patterns
   - Using patterns when simple code suffices (premature abstraction)
   - Not considering thread safety in Singleton and Observer patterns
   - Hardcoding concrete classes in Factory instead of using configuration

6. **How does the Observer pattern handle thread safety?**
   The standard Observer pattern is not inherently thread-safe. When adding/removing observers from multiple threads, you must synchronize access to the observer list. Use `CopyOnWriteArrayList` for frequent reads with occasional writes, or `Collections.synchronizedList()` with explicit synchronization during iteration. Never modify the list while iterating.

7. **What is the role of the Command pattern in implementing undo/redo?**
   The Command pattern encapsulates an action as an object with `execute()` and `undo()` methods. For undo/redo, each command stores the state needed to reverse its operation. A history stack tracks executed commands: undo pops from the undo stack and pushes to the redo stack. New commands clear the redo stack. This cleanly separates invoker, command, and receiver.

8. **When would you use the Adapter pattern over the Facade pattern?**
   Adapter converts one interface to another the client expects, working with a single class. Facade provides a simplified interface to a complex subsystem with many classes. Use Adapter when integrating incompatible interfaces. Use Facade when you want to hide subsystem complexity from the client.

9. **How does the State pattern differ from using an enum with switch statements?**
   The State pattern distributes behavior across state classes, each implementing transitions it allows. Using enums with switch concentrates all transitions in one place. State pattern follows Open/Closed Principle—adding a new state doesn't modify existing code. Enums with switch require modifying the switch in multiple places. State pattern is better for complex state machines with many transitions.

10. **What is the Composite pattern and when should you use it?**
    Composite lets you treat individual objects and compositions uniformly. Use it when you have a tree structure (file systems, UI components, organizational hierarchies) and want to perform operations on the entire tree without distinguishing between leaves and branches. It simplifies client code by treating all nodes identically through a common interface.

## Pitfalls

1. **Singleton Abuse** — Using Singleton for everything creates hidden dependencies and makes unit testing nearly impossible. Instead, use dependency injection and scope beans appropriately.

2. **Observer Memory Leaks** — Forgetting to remove observers when they go out of scope. In long-lived applications, this causes memory leaks. Always use `WeakReference` observers or ensure cleanup in lifecycle methods.

3. **Factory Without Extensibility** — Hardcoding `switch` statements in factories makes them closed for extension. Use `Map<String, Supplier<T>>` or reflection to make factories open for new types without modification.

4. **Decorator Stack Overflow** — Deeply nested decorators (10+ layers) can cause stack overflow on recursive calls and make debugging difficult. Limit decorator depth and consider composition over deep nesting.

5. **Proxy Performance Overhead** — Every method call goes through the proxy's `InvocationHandler`, adding overhead. For performance-critical code, use static proxies instead of dynamic proxies via `java.lang.reflect.Proxy`.

6. **Strategy Pattern Anti-Pattern: God Strategy** — Creating a single strategy class that handles all algorithm variants internally with flags defeats the purpose. Each strategy should encapsulate one specific algorithm.

7. **State Pattern Explosion** — When every state can transition to every other state, you end up with N² state classes. Consider combining State with Flyweight to share state objects, or use a state table approach.

8. **Composite Without Uniform Interface** — Not defining a common interface for leaf and composite nodes forces clients to check types, breaking the pattern's purpose. Always define `FileSystemEntry` (or equivalent) that both `File` and `Directory` implement.

## Performance

1. **Singleton Initialization** — Eager initialization has zero runtime cost but uses memory at class load. Bill Pugh's static inner class approach is lazy with no synchronization overhead—preferred for most cases.

2. **Observer Notification** — Iterating a `CopyOnWriteArrayList` is O(n) per notification but lock-free for reads. For high-frequency events with many observers, consider `ConcurrentLinkedQueue` with snapshot iteration.

3. **Strategy Object Creation** — Creating new strategy objects per operation adds GC pressure. Cache strategy instances or reuse them as stateless singletons when possible.

4. **Decorator Call Chain** — Each decorator adds one method invocation to the call stack. For I/O-intensive decorators, the overhead is negligible compared to I/O. For CPU-bound operations, flatten the decorator chain.

5. **Factory Lookup** — `Map<String, Supplier<T>>` lookups are O(1) amortized. Switch statements compile to jump tables which are also O(1) but require recompilation when adding new types.

6. **Proxy Invocation** — Dynamic proxies add ~100ns per call vs. direct invocation. For methods called millions of times, consider bytecode generation (CGLIB/ByteBuddy) which is ~2x faster than reflection-based proxies.

7. **Composite Tree Traversal** — Recursive traversal is O(n) where n is total nodes. For deep trees, iterative traversal with an explicit stack prevents stack overflow. Cache `getSize()` results for read-heavy trees.

8. **Command History Memory** — Each command object stores state for undo. For large documents, store deltas instead of full state. Use the Memento pattern alongside Command for efficient state snapshots.

## Examples

```java
// Strategy Pattern - Sorting
SortingStrategy bubbleSort = arr -> { /* bubble sort */ };
SortingStrategy mergeSort = arr -> { /* merge sort */ };

Sorter sorter = new Sorter(mergeSort);
sorter.sort(new int[]{5, 3, 1, 4, 2});

// Decorator Pattern - Logging
Logger base = new ConsoleLogger();
Logger timed = new TimestampLogger(base);
Logger upper = new UppercaseLogger(timed);
upper.log("hello world"); // [2024-01-15 10:30:00] HELLO WORLD

// Observer Pattern - Event System
StockMarket market = new StockMarket();
Investor alice = (symbol, old, nw) -> 
    System.out.println("Alice: " + symbol + " " + old + " -> " + nw);
market.addObserver(alice);
market.priceChanged("AAPL", 150.0, 155.0);
```

## Internal Working

Design patterns work by establishing relationships between classes and objects. The JVM executes pattern code through standard OOP mechanisms: interfaces enable polymorphism, inheritance provides reuse, and composition builds complex behavior from simple parts. Patterns are not language features—they are recurring solutions encoded as conventions. The JVM doesn't "know" about patterns; it simply executes the bytecodes that implement these structural relationships.

## Why This Concept Exists

Design patterns were documented by the Gang of Four (1994) after studying successful object-oriented systems. They solve recurring design problems: how to create objects without tight coupling (Factory), how to add behavior without subclassing (Decorator), how to notify dependents of state changes (Observer), and how to manage complex state transitions (State). Patterns promote code reuse, maintainability, and shared vocabulary among developers.

## Overview

Design patterns are reusable solutions to common software design problems. They fall into three categories: **Creational** (Singleton, Factory, Builder) control object creation. **Structural** (Adapter, Decorator, Proxy, Composite) define how objects are composed. **Behavioral** (Observer, Strategy, Command, State) manage communication between objects. Mastering patterns helps you write flexible, maintainable code and communicate design decisions effectively with other developers.

## References

- [Design Patterns: Elements of Reusable Object-Oriented Software (GoF Book)](https://www.amazon.com/Design-Patterns-Elements-Reusable-Object-Oriented/dp/0201633612)
- [Refactoring.Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [Java Design Patterns](https://java-design-patterns.com/)
- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/9781492076933/)
- [Martin Fowler - Patterns of Enterprise Application Architecture](https://martinfowler.com/eaaCatalog/)
- [Related: SOLID Principles](https://www.digitalocean.com/community/conceptual-articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Related: Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
