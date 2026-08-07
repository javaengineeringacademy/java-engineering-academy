# Performance Quiz

## Questions

1. What is profiling?
2. What is cache locality?
3. What is loop unrolling?
4. What does -O2 flag do?
5. What is a memory pool?
6. What is SIMD?
7. What is false sharing?
8. What is branch prediction?
9. What is the difference between optimization and micro-optimization?
10. When should you optimize?
11. What is the difference between `-O1`, `-O2`, and `-O3` optimization levels?
12. What is data-oriented design?
13. What is a hot path in profiling?
14. What is the difference between `inline` and compiler inlining?
15. What is a benchmark and how do you write a valid one?

## Answers

1. Analyzing program execution to find bottlenecks
2. Data stored close in memory for cache efficiency
3. Reducing loop overhead by executing multiple iterations
4. Enables level 2 compiler optimizations
5. Pre-allocated memory for fast allocation/deallocation
6. Single Instruction, Multiple Data - parallel processing
7. Threads on different cores sharing cache line
8. CPU predicting branch outcomes for pipelining
9. Optimization: overall improvement; Micro: tiny specific changes
10. After profiling shows bottlenecks (not prematurely)
11. `-O1`: basic optimizations; `-O2`: moderate, safe optimizations; `-O3`: aggressive, may increase binary size
12. Organizing data structures for cache efficiency (struct of arrays vs array of structs) rather than following OOP class hierarchies
13. The code section executed most frequently; targeting this for optimization gives the greatest overall speedup
14. `inline` is a hint to the compiler; the compiler may ignore it and inline based on heuristics regardless
15. A controlled measurement of code performance; must isolate the code, run multiple iterations, use steady-state timing, and avoid measuring noise
