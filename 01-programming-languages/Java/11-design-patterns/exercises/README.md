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
