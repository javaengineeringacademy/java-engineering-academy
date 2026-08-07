# Performance Exercises

## Exercise 1: Profiling
Profile a program and identify bottlenecks.

```bash
# Compile with profiling
gcc -pg -o program program.c

# Run program
./program

# Analyze
gprof program gmon.out > analysis.txt
```

## Exercise 2: Cache Optimization
Rewrite matrix multiplication for better cache performance.

## Exercise 3: Loop Unrolling
Implement and benchmark loop unrolling.

## Exercise 4: Memory Pool
Implement a memory pool and compare with malloc.

## Exercise 5: SIMD Optimization
Use SIMD intrinsics to vectorize a computation.
