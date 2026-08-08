# Concurrency — C++

## Why It Matters

Modern CPUs have multiple cores, and sequential code leaves most of them idle. When you need to process thousands of requests simultaneously, update UI while computing, or parallelize expensive algorithms across cores, concurrency transforms a server from handling one request at a time to handling thousands. But concurrency is powerful but dangerous — data races can cause millions in incorrect calculations.

## What It Is

C++ provides threads, mutexes, condition variables, atomics, and async/futures for concurrency, letting you execute code simultaneously while coordinating access to shared data.

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

1. **What is a data race and how do you prevent it?**: A data race occurs when two threads access the same memory location concurrently, at least one writes, and no synchronization exists. Prevent with `std::mutex`, `std::atomic`, or immutable data.
2. **Explain the difference between `std::mutex` and `std::shared_mutex`**: `std::mutex` provides exclusive locking — one thread at a time. `std::shared_mutex` allows multiple concurrent readers (`lock_shared`) but exclusive writers (`lock`). Use it for read-heavy workloads.
3. **What causes deadlock and how do you prevent it?**: Deadlock occurs when threads wait on each other in a circular lock dependency (ABBA pattern). Prevent with: global lock ordering, `std::scoped_lock` for atomic multi-lock acquisition, or lock timeouts.
4. **When should you use `std::atomic` vs `std::mutex`?**: Use `std::atomic` for simple types (counters, flags, pointers) where lock-free operations are sufficient. Use `std::mutex` for complex critical sections involving multiple variables or non-trivial operations.
5. **What is false sharing and how do you fix it?**: False sharing occurs when threads write to adjacent memory on the same cache line, causing the line to ping-pong between cores. Fix with `alignas(std::hardware_destructive_interference_size)` padding on shared variables.

## References

- [C++ Concurrency in Action — Anthony Williams](https://www.amazon.com/C-Concurrency-Action-Anthony-Williams/dp/1617294691)
- [CppReference — Thread Support Library](https://en.cppreference.com/w/cpp/thread)
- [C++ Core Guidelines — Concurrency](https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines#S-concurrency)
- [CppCon Talk: C++ Concurrency in Action](https://youtube.com/cppcon)
