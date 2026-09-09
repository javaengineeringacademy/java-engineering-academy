# Concurrency — C++

## Overview

C++ concurrency enables programs to perform multiple tasks simultaneously, leveraging multi-core processors for improved throughput and responsiveness. The C++ Standard Library provides `std::thread`, `std::mutex`, `std::condition_variable`, `std::atomic`, and `std::async`/`std::future` as building blocks for concurrent programming. Since C++11, concurrency is part of the language standard, with continued improvements in C++17 (`std::scoped_lock`, `std::shared_mutex`) and C++20 (`std::jthread`, `std::counting_semaphore`). Concurrency is essential for high-performance servers, real-time systems, parallel data processing, and responsive user interfaces.

## Learning Objectives

- Create, join, and manage threads using `std::thread` and `std::jthread`
- Protect shared data with `std::mutex`, `std::lock_guard`, `std::unique_lock`, and `std::scoped_lock`
- Synchronize thread execution using `std::condition_variable` and its predicate-based wait
- Use `std::atomic` for lock-free operations on simple shared data
- Launch asynchronous tasks with `std::async` and retrieve results via `std::future`/`std::promise`
- Identify and prevent data races, deadlocks, false sharing, and starvation
- Understand the C++ memory model and memory ordering guarantees
- Apply concurrency patterns: thread pools, producer-consumer, read-write locks

## Prerequisites

- Solid understanding of C++ fundamentals (pointers, references, smart pointers)
- Familiarity with `std::function`, lambdas, and move semantics
- Basic knowledge of operating system concepts (processes, threads, context switching)
- Understanding of cache coherence and CPU architecture basics
- Completion of the [Smart Pointers](../06-smart-pointers/) module (thread safety of `shared_ptr`)

## History

| Year | Milestone | Significance |
|------|-----------|--------------|
| 1979 | Bjarne Stroustrup adds classes to C | C++ born; no threading support |
| 1995 | C++98 standard | No concurrency primitives; POSIX threads (pthreads) used via platform APIs |
| 2011 | C++11 standard | `std::thread`, `std::mutex`, `std::atomic`, `std::future`, `std::condition_variable` introduced — first-class concurrency |
| 2014 | C++14 | Minor improvements; no new concurrency features |
| 2017 | C++17 | `std::scoped_lock`, `std::shared_mutex`, `std::shared_lock` for simpler multi-lock and read-write patterns |
| 2020 | C++20 | `std::jthread` (auto-joining), `std::stop_token`, `std::counting_semaphore`, `std::latch`, `std::barrier` |
| 2023 | C++23 | `std::execution` (senders/receivers) proposal continues; structured concurrency experiments |

## Why It Matters

Modern CPUs have multiple cores, and sequential code leaves most of them idle. When you need to process thousands of requests simultaneously, update UI while computing, or parallelize expensive algorithms across cores, concurrency transforms a server from handling one request at a time to handling thousands. But concurrency is powerful but dangerous — data races can cause millions in incorrect calculations.

## What It Is

C++ provides threads, mutexes, condition variables, atomics, and async/futures for concurrency, letting you execute code simultaneously while coordinating access to shared data.

## Production Notes

- Always compile with threading support enabled: `-pthread` on GCC/Clang, no special flag on MSVC
- Enable ThreadSanitizer in CI: `-fsanitize=thread` to catch data races at test time
- Avoid `std::thread::detach()` in production — use `std::jthread` (C++20) or explicit `join()` to prevent resource leaks
- Use `std::lock_guard` or `std::scoped_lock` instead of manual `lock()`/`unlock()` to guarantee exception safety
- Set thread stack sizes explicitly for threads with deep call stacks (default is 1–8 MB depending on platform)
- Profile lock contention in production — high contention on a single mutex is a scaling bottleneck
- Prefer `std::shared_mutex` over `std::mutex` for read-heavy workloads (10:1 read:write ratio or higher)

## Architecture: How Concurrency Fits Together

```
┌─────────────────────────────────────────────────────────────┐
│                  C++ Concurrency                             │
├───────────────┬───────────────┬─────────────────────────────┤
│   std::thread │   std::mutex  │   std::atomic               │
│ (Execution)   │  (Exclusion)  │   (Lock-free data)          │
├───────────────┴───────────────┴─────────────────────────────┤
│        std::condition_variable (Synchronization)             │
├─────────────────────────────────────────────────────────────┤
│     std::async / std::future (Async results)                 │
├─────────────────────────────────────────────────────────────┤
│              Thread Pools & Lock-Free Structures              │
└─────────────────────────────────────────────────────────────┘
```

## Core Concepts

| Concept | Description | C++ Type |
|---------|-------------|----------|
| Thread | An independent execution path within a process | `std::thread`, `std::jthread` |
| Mutex | Mutual exclusion lock that protects shared data from concurrent access | `std::mutex`, `std::recursive_mutex`, `std::shared_mutex` |
| Lock Guard | RAII wrapper that acquires a mutex on construction and releases on destruction | `std::lock_guard`, `std::unique_lock` |
| Scoped Lock | RAII wrapper that atomically locks multiple mutexes (C++17) | `std::scoped_lock` |
| Condition Variable | Blocks threads until a condition is met, used for signaling between threads | `std::condition_variable`, `std::condition_variable_any` |
| Atomic | Lock-free operations on a single variable with memory ordering guarantees | `std::atomic<T>` |
| Future/Promise | Asynchronous result delivery mechanism | `std::future`, `std::promise` |
| Async | Launches a task that returns a future | `std::async` |
| Memory Order | Specifies the ordering constraints for atomic operations | `memory_order_relaxed`, `acquire`, `release`, `acq_rel`, `seq_cst` |
| Data Race | Undefined behavior from unsynchronized concurrent access to the same memory location | — (bug, not a type) |
| Deadlock | Two or more threads waiting indefinitely for each other to release locks | — (bug, not a type) |
| False Sharing | Performance degradation from threads invalidating each other's cache lines | — (hardware effect) |

## Threads

```cpp
#include <thread>
#include <iostream>

void task(int id) {
    std::cout << "Thread " << id << " running\n";
}

int main() {
    std::thread t1(task, 1);
    std::thread t2(task, 2);

    t1.join();  // Wait for t1 to finish
    t2.join();  // Wait for t2 to finish
}
```

## Mutexes

```cpp
#include <mutex>
#include <thread>
#include <vector>

std::mutex mtx;
int counter = 0;

void increment() {
    for (int i = 0; i < 1000; ++i) {
        std::lock_guard<std::mutex> lock(mtx);
        counter++;
    }
}

int main() {
    std::vector<std::thread> threads;
    for (int i = 0; i < 10; ++i) {
        threads.emplace_back(increment);
    }
    for (auto& t : threads) t.join();
    std::cout << "Counter: " << counter << "\n";  // 10000
}
```

## Condition Variables

```cpp
#include <condition_variable>
#include <mutex>
#include <queue>
#include <thread>

std::queue<int> tasks;
std::mutex q_mutex;
std::condition_variable cv;
bool done = false;

void producer() {
    for (int i = 0; i < 10; ++i) {
        {
            std::lock_guard<std::mutex> lock(q_mutex);
            tasks.push(i);
        }
        cv.notify_one();
    }
    {
        std::lock_guard<std::mutex> lock(q_mutex);
        done = true;
    }
    cv.notify_all();
}

void consumer() {
    while (true) {
        std::unique_lock<std::mutex> lock(q_mutex);
        cv.wait(lock, [] { return !tasks.empty() || done; });
        while (!tasks.empty()) {
            int task = tasks.front();
            tasks.pop();
            lock.unlock();
            std::cout << "Processing: " << task << "\n";
            lock.lock();
        }
        if (done) break;
    }
}
```

## Atomics

```cpp
#include <atomic>
#include <thread>

std::atomic<int> counter{0};

void increment() {
    for (int i = 0; i < 1000; ++i) {
        counter++;  // Atomic — no lock needed
    }
}

// atomic operations: load, store, exchange, compare_exchange_strong/weak
// Memory orders: relaxed, acquire, release, acq_rel, seq_cst
```

## Async and Futures

```cpp
#include <future>
#include <iostream>

int compute(int x) {
    return x * x;
}

int main() {
    // Launch async task
    auto future = std::async(std::launch::async, compute, 42);
    int result = future.get();  // Blocks until result is ready
    std::cout << "Result: " << result << "\n";  // 1764

    // Promise/future
    std::promise<int> promise;
    auto future2 = promise.get_future();
    std::thread([](std::promise<int> p) {
        p.set_value(100);
    }, std::move(promise)).detach();
    std::cout << "Promise: " << future2.get() << "\n";
}
```

## std::scoped_lock (C++17)

```cpp
// Locks multiple mutexes atomically — prevents deadlock
std::mutex m1, m2;
std::scoped_lock lock(m1, m2);  // Both locked atomically
```

## Internal Working

### OS Thread Implementation

`std::thread` maps to OS-level threading primitives: `pthread_create` on POSIX systems and `CreateThread` on Windows. When you construct a `std::thread`, the C++ runtime allocates a stack (default 1–8 MB depending on OS), creates a native thread, and begins execution of the provided callable. `join()` calls a platform join (e.g., `pthread_join`) that blocks until the thread completes. `detach()` severs the C++ thread object from the OS thread, which continues running independently — detached threads are cleaned up on process exit.

### Futex (Fast Userspace Mutex)

Most `std::mutex` implementations on Linux use a **futex** (fast userspace mutex) under the hood:

1. **Uncontended case (fast path)**: A single atomic compare-and-swap in userspace acquires the lock — no syscall, ~5–10 ns.
2. **Contended case (slow path)**: If the lock is held, the thread calls `futex(FUTEX_WAIT)`, which puts the thread to sleep via the kernel scheduler. The lock holder calls `futex(FUTEX_WAKE)` to wake waiters.
3. This two-tier design makes uncontended mutexes extremely fast while still correctly handling contention.

### C++ Memory Model

The C++ memory model defines how memory operations from different threads interact:

- **Sequentially Consistent (`seq_cst`)**: All threads see operations in the same global order. Strongest guarantee, but slowest.
- **Acquire-Release (`acquire`/`release`**: A release store synchronizes-with an acquire load — everything the releasing thread wrote is visible to the acquiring thread. Used for mutex unlock/lock and producer-consumer.
- **Relaxed (`relaxed`)**: No ordering guarantees between threads — only atomicity is guaranteed. Used for counters where order doesn't matter.
- **Happens-before**: The fundamental relation — if operation A happens-before operation B, A's effects are visible to B. Thread creation, mutex unlock, and atomic release-establish this relation.

### Cache Coherence and False Sharing

CPU caches operate on **cache lines** (typically 64 bytes). When two threads write to different variables on the same cache line, the line bounces between cores via the cache coherence protocol (MESI/MOESI), causing severe performance degradation. Solution: pad shared variables with `alignas(std::hardware_destructive_interference_size)` (typically 64 bytes) to ensure they occupy separate cache lines.

## Syntax

```cpp
// Thread creation and joining
#include <thread>
std::thread t(function_ptr, arg1, arg2);  // Create thread
t.join();                                  // Wait for completion
t.detach();                                // Release thread

// Mutex locking (RAII)
#include <mutex>
std::mutex mtx;
std::lock_guard<std::mutex> guard(mtx);        // Lock on construct, unlock on destroy
std::unique_lock<std::mutex> ulock(mtx);        // Flexible: can lock/unlock/relock
std::scoped_lock lock(mtx1, mtx2);             // Lock multiple mutexes atomically (C++17)

// Condition variable
#include <condition_variable>
std::condition_variable cv;
std::unique_lock<std::mutex> ulock(mtx);
cv.wait(ulock, []{ return ready; });            // Wait with predicate (spurious-wakeup safe)
cv.notify_one();                                 // Wake one waiting thread
cv.notify_all();                                 // Wake all waiting threads

// Atomic operations
#include <atomic>
std::atomic<int> counter{0};
counter.store(10, std::memory_order_relaxed);
int val = counter.load(std::memory_order_acquire);
counter.compare_exchange_strong(expected, desired, std::memory_order_acq_rel);

// Async and futures
#include <future>
auto fut = std::async(std::launch::async, func, args...);
auto result = fut.get();                        // Blocks until result is ready

std::promise<int> prom;
std::future<int> fut2 = prom.get_future();
prom.set_value(42);                             // Set the result
```

## Examples

### Producer-Consumer with Condition Variable

```cpp
#include <queue>
#include <mutex>
#include <condition_variable>
#include <thread>
#include <iostream>

std::queue<int> buffer;
std::mutex mtx;
std::condition_variable cv;
constexpr int MAX_SIZE = 10;

void producer(int count) {
    for (int i = 0; i < count; ++i) {
        std::unique_lock<std::mutex> lock(mtx);
        cv.wait(lock, []{ return buffer.size() < MAX_SIZE; });
        buffer.push(i);
        std::cout << "Produced: " << i << "\n";
        lock.unlock();
        cv.notify_one();
    }
}

void consumer(int count) {
    for (int i = 0; i < count; ++i) {
        std::unique_lock<std::mutex> lock(mtx);
        cv.wait(lock, []{ return !buffer.empty(); });
        int val = buffer.front();
        buffer.pop();
        std::cout << "Consumed: " << val << "\n";
        lock.unlock();
        cv.notify_one();
    }
}

int main() {
    std::thread prod(producer, 20);
    std::thread cons(consumer, 20);
    prod.join();
    cons.join();
}
```

### Thread Pool

```cpp
#include <vector>
#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <functional>
#include <future>

class ThreadPool {
    std::vector<std::thread> workers;
    std::queue<std::function<void()>> tasks;
    std::mutex mtx;
    std::condition_variable cv;
    bool stop = false;
public:
    ThreadPool(size_t n) {
        for (size_t i = 0; i < n; ++i)
            workers.emplace_back([this] {
                while (true) {
                    std::function<void()> task;
                    {
                        std::unique_lock<std::mutex> lock(mtx);
                        cv.wait(lock, [this]{ return stop || !tasks.empty(); });
                        if (stop && tasks.empty()) return;
                        task = std::move(tasks.front());
                        tasks.pop();
                    }
                    task();
                }
            });
    }
    template<class F>
    auto enqueue(F&& f) -> std::future<decltype(f())> {
        auto task = std::make_shared<std::packaged_task<decltype(f())()>>(std::forward<F>(f));
        auto fut = task->get_future();
        {
            std::lock_guard<std::mutex> lock(mtx);
            tasks.emplace([task](){ (*task)(); });
        }
        cv.notify_one();
        return fut;
    }
    ~ThreadPool() {
        { std::lock_guard<std::mutex> lock(mtx); stop = true; }
        cv.notify_all();
        for (auto& w : workers) w.join();
    }
};
```

### Atomic Lock-Free Counter with Memory Ordering

```cpp
#include <atomic>
#include <thread>
#include <iostream>

std::atomic<int> counter{0};

void increment_relaxed() {
    for (int i = 0; i < 1000; ++i)
        counter.fetch_add(1, std::memory_order_relaxed);
}

void increment_release() {
    for (int i = 0; i < 1000; ++i)
        counter.fetch_add(1, std::memory_order_release);
}

int main() {
    std::thread t1(increment_relaxed);
    std::thread t2(increment_release);
    t1.join();
    t2.join();
    std::cout << "Counter: " << counter.load(std::memory_order_acquire) << "\n";
}
```

## Performance Considerations

| Scenario | Recommended Approach | Expected Performance |
|----------|---------------------|---------------------|
| Simple counter/flag | `std::atomic` with `relaxed` ordering | ~5–10 ns per operation |
| Complex critical section | `std::mutex` + `std::lock_guard` | ~20 ns uncontended, 100+ ns contended |
| Read-heavy workload | `std::shared_mutex` + `std::shared_lock` | Many concurrent readers, exclusive writers |
| Multiple mutexes | `std::scoped_lock` | Avoids deadlock, same overhead as manual ordering |
| Async task with result | `std::async` + `std::future` | May use thread pool internally (implementation-defined) |
| High-frequency short tasks | Custom thread pool | Avoids thread creation/destruction overhead |
| Per-thread data | `thread_local` | Zero synchronization overhead |

- **Thread count**: Optimal is `std::thread::hardware_concurrency()` for CPU-bound work. I/O-bound work may benefit from more threads.
- **False sharing**: Pad shared atomics with `alignas(64)` to avoid cache line bouncing.
- **Lock granularity**: Fine-grained locking (many small locks) improves throughput but increases complexity.
- **Avoid lock contention**: Profile with `perf lock` or ThreadSanitizer; high contention means redesign needed.

## Best Practices

1. **Always use RAII for mutex locking** — `std::lock_guard` or `std::scoped_lock` ensure exception-safe lock release
2. **Never call `join()` twice** — check `joinable()` before joining
3. **Use `std::jthread` (C++20)** — auto-joins on destruction, preventing forgotten joins
4. **Prefer `std::scoped_lock` for multiple mutexes** — eliminates manual lock ordering
5. **Use condition variables with predicates** — `cv.wait(lock, pred)` prevents spurious wakeup bugs
6. **Minimize critical sections** — lock only the code that accesses shared data
7. **Prefer immutable data** — no synchronization needed for read-only data
8. **Use `std::atomic` for simple shared counters/flags** — avoids mutex overhead
9. **Document lock ordering** — critical for preventing deadlocks in complex systems
10. **Enable ThreadSanitizer in CI** — catches data races before they reach production
11. **Avoid `detach()` in production** — prefer `join()` or `std::jthread` for deterministic cleanup
12. **Set thread stack sizes** for deep recursion or large local variables

## Common Mistakes

| Mistake | Why It's Wrong | Fix |
|---------|---------------|-----|
| Accessing shared data without synchronization | Data race → undefined behavior | Use `std::mutex`, `std::atomic`, or immutable data |
| Calling `std::thread::join()` on a detached thread | Throws `std::system_error` | Check `joinable()` before joining |
| Forgetting to join a thread before destruction | Calls `std::terminate()` | Always join or detach; prefer `std::jthread` |
| Using `cv.wait()` without a predicate | Spurious wakeups cause incorrect behavior | Always use `cv.wait(lock, []{ return condition; })` |
| Locking mutexes in inconsistent order | Deadlock (ABBA pattern) | Establish global lock order; use `std::scoped_lock` |
| Calling `future::get()` twice | Undefined behavior / exception | Call `get()` once; store the result |
| Using `std::atomic<bool>` for complex state | Atomic types only guarantee atomicity, not complex transitions | Use `std::mutex` for complex state machines |
| Holding a lock across a blocking I/O call | Other threads starved waiting for the lock | Release lock before I/O; redesign synchronization |
| Creating too many threads | Context switching overhead degrades performance | Use a thread pool with `hardware_concurrency()` workers |
| Assuming `std::async` runs on a new thread | Implementation may defer or run on current thread | Use `std::launch::async` policy explicitly |

## Cross-References

| Topic | Module | Relevance |
|-------|--------|-----------|
| Smart Pointers | [Smart Pointers](../06-smart-pointers/) | `std::shared_ptr` reference counting is thread-safe; `std::unique_ptr` is not |
| Modern C++ | [Modern C++](../08-modern-cpp/) | `std::jthread`, `std::stop_token`, `std::latch`, `std::barrier` (C++20) |
| Performance | [Performance](../11-performance/) | Parallelism optimization, false sharing, cache effects |
| Templates | [Templates](../05-templates/) | Type traits for thread-safe type dispatch |
| Move Semantics | [Move Semantics](../04-move-semantics/) | `std::thread` is move-only; `std::mutex` is non-copyable |
| Memory Model | [Low-Level Memory](../10-low-level-memory/) | Cache coherence, memory ordering, atomic operations |

## Engineering Decision Framework

### When to Use Concurrency
- Parallel processing of independent data
- Responsive UI (background computation)
- I/O-bound operations (network, disk)
- Real-time processing

### When NOT to Use Concurrency
- Simple sequential tasks
- When synchronization overhead exceeds parallelism benefit
- When shared state makes coordination complex

### Common Pitfalls
| Issue | Description | Solution |
|-------|-------------|----------|
| Data Race | Unsynchronized access to shared data | Use mutex, atomic, or immutable data |
| Deadlock | Circular lock waiting | Use `std::scoped_lock` or lock ordering |
| False Sharing | Threads invalidating each other's cache lines | Pad shared atomics with `alignas(64)` |
| Starvation | One thread never gets the lock | Use fair mutexes or work distribution |

### Real-World Production Examples
1. **Game Engines**: Main thread for game logic, render thread for GPU, audio thread for sound
2. **Web Servers**: Thread-per-request or async I/O (Boost.Asio)
3. **Databases**: Lock-free data structures for high-throughput transaction processing

## Production Incidents

### Incident 1: Data Race in Trading Engine
**Problem**: Incorrect P&L calculations at >1000 orders/second.

**Cause**: Reporting thread iterated over `OrderBook` while matching thread modified it — no synchronization.

**Solution**: Immutable snapshots for reporting, mutable live copy for matching.

---

### Incident 2: Deadlock in Payment Service
**Problem**: All transactions froze for 15 minutes.

**Cause**: Thread A locked account then ledger; Thread B locked ledger then account (ABBA deadlock).

**Solution**: Established global lock ordering. Used `std::scoped_lock`.

---

### Incident 3: Race Condition in User Session Cache
**Problem**: Users intermittently saw other users' session data after login — a security incident affecting 50,000 accounts.

**Cause**: A shared `std::unordered_map<SessionId, UserData>` was accessed by the HTTP handler threads without synchronization. Thread A reading a session could see a partially written entry from Thread B performing a session refresh.

**Solution**: Replaced the raw map with a `std::shared_mutex`-protected structure using `std::shared_lock` for reads and `std::unique_lock` for writes. Session data was also made immutable after creation — refreshes created new entries and atomically swapped pointers.

---

### Incident 4: Deadlock in Database Connection Pool
**Problem**: Database connections exhausted under moderate load; application became unresponsive after 200 concurrent requests.

**Cause**: Connection checkout locked the pool mutex, then called `connection->ping()` which could block on network I/O while holding the lock. Other threads waiting to check out connections were starved. Additionally, the return path locked a different mutex before releasing the pool lock — creating a 3-lock cycle.

**Solution**: Removed network I/O from the critical section — connections are checked out first, then validated outside the lock. Replaced the two-mutex design with a single `std::mutex` plus `std::condition_variable` for the pool. Added connection timeout (`wait_for`) to prevent indefinite blocking.

---

### Incident 5: Thread Pool Exhaustion in Web Server
**Problem**: Web server stopped accepting new connections after spawning 500 threads, each blocked on external API calls.

**Cause**: Thread pool was configured with `hardware_concurrency()` (8) workers, but each task called an external API with a 30-second timeout. The 8 workers were all blocked, and new tasks queued indefinitely. The task queue grew unbounded, consuming memory until OOM killed the process.

**Solution**: Increased pool size to 64 for I/O-bound workload. Added a bounded task queue with a maximum size — `enqueue()` now returns `false` when the queue is full, allowing the server to return HTTP 503 (Service Unavailable) instead of silently queuing. Added per-task timeouts and circuit breaker pattern to fail fast when external services are slow.

---

## Production Checklist
- [ ] Use `std::lock_guard` for automatic locking
- [ ] Use `std::scoped_lock` for multiple mutexes
- [ ] Avoid data races with proper synchronization
- [ ] Use `std::atomic` for simple shared counters
- [ ] Enable ThreadSanitizer (`-fsanitize=thread`) in CI
- [ ] Document lock ordering in code comments
- [ ] Prefer immutable data over synchronization
- [ ] Use thread pools for frequent task creation
- [ ] Test for deadlocks with stress tests

## Maturity Levels

### Beginner
- Create and join threads
- Use `std::lock_guard` for mutex locking
- Understand basic race conditions

### Intermediate
- Use condition variables for producer-consumer
- Use atomics for lock-free counters
- Understand memory ordering

### Advanced
- Implement thread pools
- Design lock-free data structures
- Optimize for false sharing and cache effects

## Common Myths Debunked

### Myth 1: "More threads always mean better performance"
**Reality**: Beyond the number of hardware threads, context switching overhead degrades performance. A thread pool with N workers (N = hardware concurrency) is optimal.

### Myth 2: "Mutexes are always slow"
**Reality**: Uncontended mutexes are very fast (~20ns). Only under contention do they become expensive. Use atomics for simple operations to avoid mutex overhead entirely.

## One-Minute Revision

| Concept | What It Is | Why It Matters | Key Rule |
|---------|-----------|----------------|----------|
| Thread | Independent execution path | Parallel work | Always join or detach |
| Mutex | Mutual exclusion lock | Protect shared data | Use lock_guard or scoped_lock |
| Condition Variable | Thread synchronization | Wait for events | Always use with predicate |
| Atomic | Lock-free operations | Simple shared data | Use for counters, flags |
| Future/Promise | Async result delivery | Decouple producer/consumer | get() blocks until ready |

## Related Topics
- [Smart Pointers](../06-smart-pointers/) — Thread safety of shared_ptr
- [Modern C++](../08-modern-cpp/) — std::jthread (C++20)
- [Performance](../11-performance/) — Parallelism optimization

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Data race causing intermittent crash or wrong result | ThreadSanitizer (`-fsanitize=thread`) | Compile with `-fsanitize=thread`; TSan reports every data race with full stack traces |
| Deadlock from ABBA lock ordering | Lock ordering documentation + `std::scoped_lock` | Establish global lock order; use `std::scoped_lock(m1, m2)` to lock atomically |
| False sharing degrading multi-threaded performance | `perf c2c` (cache-to-cache analysis) | Run `perf c2c record ./program`; identify cache lines with high "Cycles Lost" counts |
| `std::future` hanging because `get()` called twice | Code review + future state tracking | Call `get()` only once per future; store result immediately |
| Thread not joining causing process hang on exit | ASan + thread leak detection | Enable `-fsanitize=thread`; ensure every `std::thread` is joined or detached |

## Code Review Checklist

- [ ] `std::lock_guard` or `std::scoped_lock` used for all mutex locking
- [ ] No data races — all shared mutable state properly synchronized
- [ ] Global lock ordering documented and enforced (prevents deadlock)
- [ ] `std::atomic` used for simple shared counters and flags
- [ ] Every `std::thread` joined or detached before destruction
- [ ] Condition variables always used with a predicate (prevents spurious wakeup)
- [ ] ThreadSanitizer enabled in CI (`-fsanitize=thread`)

## Architecture Considerations

Concurrency transforms systems from sequential to parallel, enabling responsive UIs, high-throughput servers, and efficient CPU utilization. However, concurrency introduces complexity: data races, deadlocks, and false sharing can cause millions in incorrect calculations. Architecture must define clear ownership boundaries, lock ordering, and synchronization strategies. Immutable data eliminates synchronization entirely — prefer immutable snapshots for read-heavy workloads.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Thread pool for task execution | High-frequency short tasks | Avoids thread creation overhead vs. fixed pool size limits parallelism |
| Immutable snapshots for read-heavy data | Reporting, analytics on live data | No synchronization needed vs. memory overhead from copying |
| Lock-free atomics for counters/flags | High-performance metrics, signals | No lock overhead vs. complex reasoning about memory ordering |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Race condition in authentication/authorization | Bypassing security checks, privilege escalation | Use mutex-protected critical sections; verify lock coverage in security audit |
| Deadlock causing denial of service | Service unavailability | Use `std::scoped_lock` for multiple locks; implement lock timeouts |
| Thread-local storage leaking sensitive data | Information disclosure across threads | Clear thread-local data on thread exit; use `thread_local` with care |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `std::thread`, `std::mutex`, `std::atomic`, `std::future` | Replace POSIX threads with C++ standard threading primitives |
| C++17 | `std::scoped_lock`, `std::shared_mutex` | Replace manual lock ordering with `std::scoped_lock`; use `shared_mutex` for read-heavy workloads |
| C++20 | `std::jthread`, `std::counting_semaphore`, `std::latch` | Replace `std::thread` with `std::jthread` for automatic joining; use latches/barriers for synchronization |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::thread`, `std::mutex`, `std::atomic` | C++11 | Widely supported |
| `std::scoped_lock` | C++17 | Widely supported |
| `std::shared_mutex` | C++17 | Widely supported |
| `std::jthread` / `std::stop_token` | C++20 | Supported in GCC 10+, Clang 14+, MSVC 19.28+ |

## Interview Questions

1. **What is a data race and how do you prevent it?**: A data race occurs when two threads access the same memory location concurrently, at least one writes, and no synchronization exists. It is undefined behavior in C++. Prevent with `std::mutex`, `std::atomic`, or immutable data.

2. **Explain the difference between `std::mutex` and `std::shared_mutex`**: `std::mutex` provides exclusive locking — one thread at a time. `std::shared_mutex` allows multiple concurrent readers (`lock_shared`) but exclusive writers (`lock`). Use `shared_mutex` for read-heavy workloads (10:1+ read:write ratio).

3. **What causes deadlock and how do you prevent it?**: Deadlock occurs when threads wait on each other in a circular lock dependency (ABBA pattern). Prevent with: global lock ordering, `std::scoped_lock` for atomic multi-lock acquisition, or lock timeouts.

4. **When should you use `std::atomic` vs `std::mutex`?**: Use `std::atomic` for simple types (counters, flags, pointers) where lock-free operations are sufficient. Use `std::mutex` for complex critical sections involving multiple variables or non-trivial operations.

5. **What is false sharing and how do you fix it?**: False sharing occurs when threads write to adjacent memory on the same cache line, causing the line to ping-pong between cores. Fix with `alignas(std::hardware_destructive_interference_size)` padding on shared variables.

6. **Explain the C++ memory model and memory orderings**: The memory model defines how atomic operations from different threads interact. `seq_cst` provides total ordering (strongest, slowest). `acquire`/`release` establishes a happens-before relationship between threads. `relaxed` provides only atomicity with no ordering — used for independent counters.

7. **What is the difference between `std::lock_guard` and `std::unique_lock`?**: `std::lock_guard` is a simple RAII wrapper — locks on construction, unlocks on destruction. `std::unique_lock` is more flexible: it supports deferred locking, timed locking, manual lock/unlock, and is required by `std::condition_variable::wait()`. Use `lock_guard` by default; use `unique_lock` when you need flexibility.

8. **How does `std::condition_variable` work and why use a predicate?**: `cv.wait()` atomically releases the mutex and puts the thread to sleep. When notified, it re-acquires the mutex and returns. Without a predicate, spurious wakeups (OS can wake threads without notification) cause incorrect behavior. The predicate version `cv.wait(lock, pred)` loops internally, checking the predicate after each wakeup.

9. **What is `std::scoped_lock` and why is it better than manual lock ordering?**: `std::scoped_lock` (C++17) acquires multiple mutexes atomically in a single operation, eliminating the possibility of deadlock from interrupted lock ordering. Manual ordering requires discipline across all code paths and is fragile. `std::scoped_lock` uses a deadlock-avoidance algorithm (typically try-and-back-off).

10. **Explain the ABA problem in lock-free programming**: The ABA problem occurs when a thread reads value A, another thread changes it to B then back to A, and the first thread's compare-and-swap succeeds incorrectly — the data may have changed semantically even though the pointer/value is the same. Fix with hazard pointers, epoch-based reclamation, or tagged pointers.

11. **How do you implement a thread pool and what are its benefits?**: A thread pool pre-creates N worker threads that pull tasks from a shared queue. Benefits: avoids thread creation/destruction overhead (~1μs per thread), limits concurrency to prevent resource exhaustion, and provides natural backpressure via bounded queues. Key design: mutex + condition_variable for the queue, graceful shutdown with a stop flag.

12. **What is `std::jthread` and why was it introduced?**: `std::jthread` (C++20) is an auto-joining thread — it calls `join()` in its destructor if the thread is joinable. It also provides cooperative cancellation via `std::stop_token`. It was introduced because forgotten `join()` calls are a common source of `std::terminate()` crashes and resource leaks.

13. **How does `std::async` differ from manually creating threads?**: `std::async` launches a task and returns a `std::future` for the result. It may reuse threads from an internal pool or create new threads (implementation-defined). Benefits over manual threads: automatic result delivery via futures, no need to manage thread lifetime. Use `std::launch::async` policy to guarantee a new thread.

14. **What are the trade-offs of lock-free vs lock-based data structures?**: Lock-free structures avoid mutex overhead and priority inversion but are complex to implement correctly, harder to debug, and may use more CPU cycles due to CAS retry loops. Lock-based structures are simpler, easier to reason about, and perform well under low contention. Choose lock-free for high-throughput, low-latency scenarios where lock contention is measured and significant.

15. **How do you test for concurrency bugs?**: Use ThreadSanitizer (`-fsanitize=thread`) to detect data races at runtime. Write stress tests that run concurrent operations with randomized timing. Use tools like `Helgrind` (Valgrind) for lock-order checking. Employ code review focused on shared state. Test with varying thread counts to expose timing-dependent bugs. Use deterministic concurrency testing (model checking) for critical sections.

## References

- [C++ Concurrency in Action — Anthony Williams](https://www.amazon.com/C-Concurrency-Action-Anthony-Williams/dp/1617294691)
- [CppReference — Thread Support Library](https://en.cppreference.com/w/cpp/thread)
- [C++ Core Guidelines — Concurrency](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-concurrency)
- [CppCon Talk: C++ Concurrency in Action](https://youtube.com/cppcon)
