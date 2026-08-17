# Garbage Collection Exercises

## Exercise 1: Memory Monitor
Create a class that monitors memory usage during object creation. Track how many objects are created and how much memory each uses.

**Requirements:**
- Create a `MemoryMonitor` class
- Implement a method to create objects and track memory
- Print memory usage before and after allocation
- Identify when GC occurs

## Exercise 2: Weak Reference Cache
Implement a simple cache using `WeakReference` that automatically evicts entries when memory is low.

**Requirements:**
- Use `WeakHashMap` or `WeakReference`
- Implement `put`, `get`, and `size` methods
- Demonstrate that entries are removed after GC
- Add a `cleanup` method to check for stale entries

## Exercise 3: GC Algorithm Comparison
Write a program that benchmarks different GC algorithms by measuring pause times and throughput.

**Requirements:**
- Accept GC algorithm as command-line argument
- Run a fixed workload (create and discard objects)
- Measure total time and report approximate GC overhead
- Compare results for at least two algorithms

## Guidelines

1. Use `Runtime.getRuntime()` for memory statistics
2. Use `System.gc()` to request garbage collection
3. Use `ManagementFactory.getGarbageCollectorMXBeans()` for GC stats
4. Test with different heap sizes using `-Xms` and `-Xmx`

## Expected Output Format

```
=== Exercise Name ===
Memory before: X MB
Memory after:  Y MB
Objects created: N
...
```
