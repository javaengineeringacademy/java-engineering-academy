# Concurrency Exercises

## Exercise 1: Thread Safety
Implement a thread-safe counter.

**Requirements:**
- Create a counter class
- Use mutex for synchronization
- Test with multiple threads
- Measure performance impact

## Exercise 2: Producer-Consumer
Implement producer-consumer pattern.

**Requirements:**
- Use threads, mutex, and condition variable
- Producer creates items
- Consumer processes items
- Handle shutdown gracefully

## Exercise 3: Async Tasks
Use `std::async` for parallel computation.

**Requirements:**
- Implement parallel sum of large array
- Compare with sequential sum
- Measure speedup

## Exercise 4: Thread Pool
Implement a simple thread pool.

**Requirements:**
- Fixed number of worker threads
- Task queue with mutex
- Condition variable for waiting
- Graceful shutdown