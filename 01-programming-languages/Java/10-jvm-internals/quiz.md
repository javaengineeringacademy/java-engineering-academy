# JVM Internals Quiz

## Question 1
What is the purpose of the JVM's Method Area?
- A) Stores object instances
- B) Stores class metadata, static variables, and constant pool
- C) Stores local method variables
- D) Stores native method implementations

**Answer: B**
**Explanation:** The Method Area stores class metadata, static variables, constant pool, and method bytecode. It is shared among all threads.

## Question 2
Which GC algorithm became the default in Java 9 and is designed for large heaps with predictable pause times?
- A) Serial GC
- B) Parallel GC
- C) G1 GC
- D) ZGC

**Answer: C**
**Explanation:** G1 (Garbage-First) GC became the default in Java 9. It divides the heap into regions and prioritizes collecting regions with the most garbage.

## Question 3
What happens during the "Linking" phase of class loading?
- A) Reading the .class file from disk
- B) Executing static initializers
- C) Verifying bytecode, preparing memory, and resolving references
- D) Allocating heap memory for objects

**Answer: C**
**Explanation:** Linking consists of three sub-phases: Verification (checking bytecode validity), Preparation (allocating memory for static fields), and Resolution (replacing symbolic references with direct references).

## Question 4
Which of the following is a GC Root?
- A) An object referenced by another unreferenced object
- B) A local variable in an active method
- C) An object in the old generation
- D) A weak reference

**Answer: B**
**Explanation:** GC Roots include local variables in active frames, static fields, JNI references, active threads, and monitors. Objects reachable from GC Roots are not eligible for garbage collection.

## Question 5
What does the JVM flag `-XX:+HeapDumpOnOutOfMemoryError` do?
- A) Increases heap size automatically
- B) Enables concurrent garbage collection
- C) Generates a heap dump file when OutOfMemoryError occurs
- D) Restarts the application on memory errors

**Answer: C**
**Explanation:** This flag instructs the JVM to write a heap dump (hprof file) when an OutOfMemoryError is thrown, which helps in analyzing memory leaks and diagnosing memory issues.