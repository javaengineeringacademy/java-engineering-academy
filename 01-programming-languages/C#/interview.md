# C# Interview Questions

## 1. Value Types vs Reference Types

**Q: What is the difference between value types and reference types?**

A: Value types (struct, enum, int, bool) store data directly on the stack. Reference types (class, string, array) store a reference on the stack pointing to data on the heap. Value types copy by value; reference types copy the reference.

## 2. Boxing and Unboxing

**Q: What are boxing and unboxing?**

A: Boxing is converting a value type to object (heap allocation). Unboxing is extracting the value type from the object. Both have performance costs. Use generics to avoid.

## 3. Generics

**Q: Why use generics over non-generic collections?**

A: Generics provide type safety, avoid boxing/unboxing, and enable code reuse without runtime type casting. They are resolved at compile time.

## 4. LINQ

**Q: What is the difference between LINQ method syntax and query syntax?**

A: Both produce equivalent IL. Method syntax uses extension methods (Where, Select). Query syntax uses SQL-like operators. Query syntax is better for joins; method syntax is more common.

## 5. Async/Await

**Q: What does async/await do under the hood?**

A: The compiler transforms async methods into state machines. Tasks represent asynchronous operations. The await keyword suspends execution until the task completes, freeing the thread.

## 6. Delegates vs Events

**Q: What is the difference between delegates and events?**

A: Delegates are type-safe function pointers. Events are delegates with restricted access - only the declaring class can invoke them. Events use the publish-subscribe pattern.

## 7. Garbage Collection

**Q: How does the .NET garbage collector work?**

A: The GC uses generational collection (Gen 0, 1, 2). Gen 0 collects short-lived objects frequently. Gen 2 collects long-lived objects less often. The Large Object Heap handles objects >= 85KB.

## 8. Dependency Injection

**Q: What are the DI service lifetimes?**

A: Transient (new instance each request), Scoped (one per HTTP request), Singleton (one for app lifetime). Choose based on state management and resource usage.

## 9. String vs StringBuilder

**Q: When should you use StringBuilder?**

A: Use StringBuilder for multiple string modifications in loops. Strings are immutable; each concatenation creates a new string. StringBuilder modifies in place.

## 10. Extension Methods

**Q: What are extension methods?**

A: Static methods that add functionality to existing types without modifying them. Defined in static classes with `this` parameter on the first argument.

## 11. Nullable Types

**Q: What is the difference between int? and Nullable<int>?**

A: They are identical. `int?` is syntax sugar for `Nullable<int>`. Nullable types can hold null values and have a HasValue property.

## 12. Interface vs Abstract Class

**Q: When to use interface vs abstract class?**

A: Use interfaces for contracts (can implement multiple). Use abstract classes for shared implementation (single inheritance). Abstract classes can have constructors and fields.

## 13. Task vs ValueTask

**Q: When to use ValueTask over Task?**

A: Use ValueTask for synchronous or cached async results. It avoids allocation when the result is available synchronously. Use Task for most async operations.

## 14. Pattern Matching

**Q: What pattern matching features exist in C#?**

A: is/switch expressions, type patterns, property patterns, relational patterns, tuple patterns. Pattern matching improves readability and reduces boilerplate.

## 15. Garbage Collection Tuning

**Q: How do you reduce GC pressure?**

A: Use object pooling, Span<T>, structs for small data, avoid allocations in hot paths, use ArrayPool<T>, and minimize string concatenation.
