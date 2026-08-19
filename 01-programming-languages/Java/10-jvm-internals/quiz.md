# JVM Internals Quiz

## ClassLoader

1. What are the three types of class loaders in the JVM?
   - **Answer:** Bootstrap ClassLoader, Platform (Extension) ClassLoader, Application ClassLoader

2. What is the delegation model in class loading?
   - **Answer:** A class loader delegates loading to its parent first; only if the parent cannot load does the child attempt it.

3. What is the difference between `loadClass()` and `findClass()`?
   - **Answer:** `loadClass()` implements the delegation model; `findClass()` performs the actual class loading.

## Memory Model

4. What is the happens-before relationship?
   - **Answer:** A guarantee that memory writes by one operation are visible to subsequent operations.

5. What is the difference between `volatile` and `synchronized`?
   - **Answer:** `volatile` guarantees visibility; `synchronized` guarantees both visibility and atomicity.

6. What is a memory barrier?
   - **Answer:** A CPU instruction that enforces ordering constraints between operations.

## Garbage Collection

7. What are the two main phases of garbage collection?
   - **Answer:** Mark (identify reachable objects) and Sweep (reclaim unreachable memory).

8. What is generational garbage collection?
   - **Answer:** Dividing heap into young and old generations based on object lifecycle.

9. What is the difference between Minor GC and Major GC?
   - **Answer:** Minor GC collects young generation; Major GC collects old generation (and usually young too).

10. What is a Stop-The-World pause?
    - **Answer:** A pause where all application threads are stopped during garbage collection.

## JIT Compilation

11. What is the difference between interpreter and JIT compiler?
    - **Answer:** Interpreter executes bytecode line by line; JIT compiles hot bytecode to native code.

12. What is method inlining?
    - **Answer:** Replacing a method call with the method body to reduce overhead.

## Profiling

13. What is the difference between sampling and instrumentation profiling?
    - **Answer:** Sampling takes periodic snapshots; instrumentation inserts measurement code.

14. What metrics should you monitor in production?
    - **Answer:** GC pause time, heap usage, thread count, CPU usage, cache hit ratio.

## Module System

15. What is the purpose of the module system in Java 9+?
    - **Answer:** Strong encapsulation, reliable configuration, and modular JDK.
