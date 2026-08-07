# Performance Exercises

## Exercise 1: Profiling
Profile a program to find bottlenecks.

**Requirements:**
- Create a program with intentional slow sections
- Use profiler (gprof, perf, or Valgrind)
- Identify bottlenecks
- Optimize and measure improvement

## Exercise 2: Cache Optimization
Optimize code for cache performance.

**Requirements:**
- Implement matrix multiplication
- Compare row-major vs column-major access
- Measure cache misses
- Optimize for cache efficiency

## Exercise 3: Memory Pool
Implement a memory pool for frequent allocations.

**Requirements:**
- Pre-allocate memory block
- Allocate objects from pool
- Compare with new/delete
- Measure performance improvement

## Exercise 4: Move Semantics Performance
Measure performance impact of move semantics.

**Requirements:**
- Create large object with copy constructor
- Measure copy vs move time
- Demonstrate performance gain
- Document findings