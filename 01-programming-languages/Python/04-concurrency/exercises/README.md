# Python Concurrency Exercises

## Learning Objectives
- Understand threading vs asyncio concepts
- Implement thread-safe code
- Use locks, semaphores, and queues
- Write async/await coroutines
- Handle concurrent tasks efficiently

## Exercises

### threading.py
1. **Thread-Safe Counter** (Easy) - Implement thread-safe operations
2. **Producer-Consumer** (Medium) - Implement producer-consumer pattern
3. **Thread Pool** (Medium) - Implement simple thread pool
4. **Reader-Writer Lock** (Hard) - Implement reader-writer synchronization
5. **Parallel Map** (Medium) - Parallel execution of function

### asyncio.py
1. **Async Hello World** (Easy) - Basic async/await
2. **Async HTTP Fetcher** (Medium) - Concurrent URL fetching
3. **Async Task Scheduler** (Medium) - Schedule async tasks
4. **Async Producer-Consumer** (Hard) - Async queue implementation
5. **Async Rate Limiter** (Hard) - Async rate limiting

## Tips
- Use threading for I/O-bound tasks
- Use asyncio for high-concurrency I/O-bound tasks
- Always use locks for shared resources
- Avoid deadlocks by acquiring locks in consistent order
- Use asyncio.gather for concurrent async tasks
