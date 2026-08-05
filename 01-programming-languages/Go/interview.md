# Go Interview Questions

## Fundamentals

**1. What is the difference between a slice and an array?**
Arrays are fixed-size value types. Slices are dynamic, reference types backed by arrays. Slices have length and capacity.

**2. Explain goroutines and how they differ from OS threads.**
Goroutines are lightweight threads managed by Go runtime. They start with 2KB stack vs 1MB for threads. M:N scheduling maps many goroutines to fewer OS threads.

**3. What is the GMP model?**
G = Goroutine, M = Machine (OS thread), P = Processor (logical processor). P's have local run queues and work stealing balances load.

**4. How does garbage collection work in Go?**
Concurrent tri-color mark-sweep. Uses write barriers for concurrent marking. Sub-millisecond STW pauses. Triggered by GOGC percentage.

**5. What are channels and how do they work?**
Channels are typed conduits for goroutine communication. Unbuffered channels synchronize sender/receiver. Buffered channels decouple up to capacity.

## Concurrency

**6. What is a data race and how do you detect it?**
Concurrent unsynchronized access to shared memory. Detected with `-race` flag. Fixed with mutexes, channels, or atomic operations.

**7. Explain select statement behavior.**
Multiplexes channel operations. Blocks until one case is ready. Random selection if multiple cases ready. Default case prevents blocking.

**8. What is the difference between sync.Mutex and sync.RWMutex?**
Mutex provides exclusive locking. RWMutex allows multiple readers or one writer. Use RWMutex when reads far exceed writes.

**9. How do you handle context cancellation?**
Use `context.WithCancel`, `context.WithTimeout`, or `context.WithDeadline`. Pass context as first parameter. Check `ctx.Done()` channel.

**10. What is work stealing?**
When a P's local queue is empty, it steals from other Ps' queues. Balances load across processors without central bottleneck.

## Types and Interfaces

**11. Explain the difference between nil interface and typed nil.**
Interface stores type and value. `var err *MyError = nil` in interface is not nil because type is set. Check with `errors.As`.

**12. What is the empty interface?**
`interface{}` or `any` accepts any type. Useful for generics-like patterns. Type assert or type switch to use underlying value.

**13. How do you implement an interface?**
Implicitly - any type implementing all methods satisfies the interface. No explicit declaration needed.

**14. What is the difference between pointer and value receivers?**
Value receiver operates on copy. Pointer receiver operates on original. Use pointer for large structs or mutation.

## Memory and Performance

**15. What causes memory leaks in Go?**
Goroutine leaks, unclosed channels, forgotten timers, circular references in maps. Use pprof to diagnose.

**16. Explain escape analysis.**
Compiler determines if variable escapes to heap. Returning pointer to local variable, interface assignment, and closure capture cause escapes.

**17. How do you optimize memory usage?**
Pre-allocate slices, use sync.Pool, avoid unnecessary allocations, use value types for small structs, reuse buffers.

## Error Handling

**18. What is the error pattern in Go?**
Errors are values. Functions return (result, error). Check error before using result. Wrap with context.

**19. How do you handle panics?**
Use `defer` and `recover()`. Recover returns nil if no panic. Re-panic if not handled. Don't use for normal flow.

## System Design

**20. Design a concurrent web scraper.**
Use goroutines for workers, channel for URL queue, semaphore for rate limiting, context for cancellation, WaitGroup for coordination, mutex for shared state.
