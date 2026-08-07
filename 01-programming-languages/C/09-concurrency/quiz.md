# Concurrency Quiz

1. What is a thread?
2. What is a mutex?
3. What is a race condition?
4. What is a deadlock?
5. What is a condition variable?
6. What is a semaphore?
7. What is atomic operations?
8. How do you avoid deadlocks?
9. What is thread-safe code?
10. What is the difference between pthread_mutex and semaphore?

## Answers

1. An independent execution path within a process
2. A lock providing mutual exclusion
3. Non-deterministic behavior due to unsynchronized access
4. Circular waiting where threads never proceed
5. A synchronization mechanism for waiting on conditions
6. A counting synchronization primitive
7. Operations that complete atomically (all or nothing)
8. Lock ordering, timeouts, avoiding nested locks
9. Code that behaves correctly with multiple threads
10. Mutex: binary lock; Semaphore: counting lock
