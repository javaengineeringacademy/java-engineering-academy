# Variables & Data Types

## Why This Feature Exists

Every Java application, from a simple console program to a large enterprise system, needs to store data. Variables are the fundamental building blocks that allow you to capture and manipulate information in memory.

**Problem Statement:** Without a way to name and store values, a program can't remember state between operations. It can't calculate, iterate, or track user input. Variables solve this core need.

**Why Java Chose This Approach:** Java's strict type system and memory management make variables predictable and safe. Every variable must have a defined type at compile time, preventing many common programming errors.

## What You'll Learn

By the end of this module, you'll be able to:

- Declare and use all eight primitive types correctly
- Choose the right type for specific use cases
- Understand Java's type casting rules and when to apply them
- Write clean, idiomatic Java code following naming conventions
- Use constants effectively to make your code more maintainable
- Debug common type-related issues

## When to Use

Use primitive variables in every Java program:

- **Variables & Data Types** — everywhere in Java, from simple calculations to complex business logic
- **File processing** — reading and writing data
- **User input** — capturing values from console or GUI
- **Calculations** — mathematical operations, financial calculations
- **Flag settings** — boolean conditions for control flow

## Internal Working

Java variables are managed at the JVM level with a well-defined lifecycle:

**Memory Layout:**
- Variables exist in the JVM's heap (objects) or stack (primitives and references)
- Primitive values are stored directly in memory
- References point to objects on the heap
- JVM performs bounds checking for array access

**Type System:**
- **Primitive Types:** Stored directly as values (int, byte, char, etc.)
- **Reference Types:** Stored as pointers to objects on the heap
- **Type Erasure:** Generics use type erasure at runtime
- **Autoboxing:** Automatic conversion between primitives and wrappers

**Storage Allocation:**
- `int`, `float`, `boolean`: Stack allocation (fast)
- `long`, `double`, `char`: Stack allocation
- `String`: Heap allocation (special handling for literals)
- Arrays: Single allocation for elements + length metadata

**JVM Perspective:**
The JVM's type system ensures type safety:

1. **Compile-Time Checks:** The Java compiler validates all type operations
2. **Runtime Verification:** The JVM validates array bounds and type casts
3. **Garbage Collection:** Unused object references are collected automatically
4. **Exception Safety:** Type mismatches throw `VerifyError` at runtime if they bypass compiler checks

**Memory Layout Example:**
```java
public class VariableExample {
    public static void main(String[] args) {
        // Stack: method call frame
        // Heap: String objects
        int age = 25;           // Primitive stored on stack
        String name = "Alice";  // Reference to heap object
        String[] names = {"A", "B"}; // Array reference with heap elements
    }
}
```

## Memory Implications

**Space Requirements:**
- `byte`: 1 byte (-128 to 127)
- `short`: 2 bytes (-32,768 to 32,767)
- `int`: 4 bytes (-2.1B to 2.1B)
- `long`: 8 bytes (-9.2 quintillion to 9.2 quintillion)
- `float`: 4 bytes (single precision)
- `double`: 8 bytes (double precision)
- `char`: 2 bytes (UTF-16 character)
- `boolean`: Implementation-dependent (typically 1 byte)

**Allocation Patterns:**
- **Small variables** are often stored in CPU registers for fast access
- **Large arrays** may cause garbage collection pauses
- **String pooling** reduces memory for duplicate string literals
- **Autoboxing** creates temporary objects that can cause GC pressure

**Performance Impact:**
- Primitive access: ~1 nanosecond
- Object access: ~10-100 nanoseconds (reference indirection)
- Boxing/unboxing: Additional 50-100 nanoseconds per operation

## Best Practices

1. **Choose the right type:** Use `int` for most whole numbers, `double` for decimals
2. **Avoid raw types:** Always use generics to prevent runtime type errors
3. **Be mindful of boxing:** Use `int` instead of `Integer` for performance in tight loops
4. **Constants:** Use `final` for values that should never change
5. **Null safety:** Initialize variables when declared to avoid null pointer exceptions

## Common Mistakes

1. **Integer division pitfalls:**
   ```java
   int result = 7 / 2;      // Result: 3 (NOT 3.5)
   double result = 7.0 / 2;  // Result: 3.5
   ```

2. **Confusing assignment vs comparison:**
   ```java
   // WRONG: Assignment in if statement
   if (x = 5) { }  // Doesn't compile: can't assign in expression
   
   // CORRECT: Comparison
   if (x == 5) { }  // Checks if x equals 5
   ```

3. **Narrowing casting issues:**
   ```java
   int large = 300;
   byte small = (byte) large;  // Result: -52 (overflow!)
   ```

4. **Uninitialized variable usage:**
   ```java
   int value;
   System.out.println(value);  // Compilation error: variable might not have been initialized
   ```

5. **Autoboxing performance trap:**
   ```java
   // In tight loops, avoid boxing
   List<Integer> numbers = new ArrayList<>();
   for (int i = 0; i < 10000; i++) {
       numbers.add(i);  // Creates Integer object each time
   }
   // Better: Use int[] for primitive storage
   ```

## Interview Questions

1. **Type Selection:** What would you use for each of these scenarios?
   - A person's age? (`int`)
   - A price in dollars? (`double` or `BigDecimal`)
   - A student ID? (`int` or `String`)

2. **Casting:** What happens when you cast `double` to `int`? Can you give an example?

3. **Constants:** Why should we use `final` variables? Give an example.

4. **Memory Layout:** How does Java store primitive types vs. reference types in memory?

5. **Best Practices:** What are the naming conventions for Java variables and methods?

## Production Considerations

1. **Thread Safety:** Primitive variables are immutable in value; sharing across threads requires synchronization
2. **Serialization:** Some primitive types have special serialization handling
3. **Performance:** Use primitive types for performance-critical code
4. **Memory Management:** Be mindful of autoboxing creating temporary objects
5. **Debugging:** Variables are easier to debug in IDEs with proper naming

## References

- [Oracle Java Language Specification](https://docs.oracle.com/javase/specs/jls/se17/html/)
- [Effective Java (Joshua Bloch)](https://www.amazon.com/Effective-Java-Effortless-Programming-Standard-2nd/dp/0321356681)
- [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se17/html/)

## Last Verified

- **Java Version:** 17, 21, 24, 26
- **Last Updated:** 2026-03-17
- **Verification:** All examples tested with Java 21 LTS and Java 26 EA

---

**Next:** [02-Operators](./02-operators/)
