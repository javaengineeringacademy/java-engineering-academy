# Concurrency Quiz

## Questions

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
11. What is a livelock and how does it differ from a deadlock?
12. What is the purpose of `pthread_join`?
13. What is a critical section?
14. What is the difference between user-level and kernel-level threads?
15. What is a thread pool and why is it useful?

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
11. Livelock: threads actively change state in response to each other but make no progress; deadlock: threads are blocked waiting
12. Waits for a specific thread to finish execution; ensures the calling thread blocks until the target thread terminates
13. A section of code accessing shared resources that must not be executed by more than one thread at a time
14. User-level threads are managed by a library (fast switching, no kernel involvement); kernel-level threads are managed by the OS (true parallelism, heavier switching)
15. A group of pre-created threads that wait for tasks; avoids thread creation overhead and limits resource usage
