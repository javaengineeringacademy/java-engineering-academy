# Concurrency

## What it is
The ability to execute multiple tasks simultaneously.

## Why it exists
To improve performance and responsiveness of applications.

## When to use it
When you need parallel processing, async operations, or responsive UIs.

## How it works

### Threads
```cpp
#include <thread>

void task() {
    std::cout << "Thread running" << std::endl;
}

int main() {
    std::thread t(task);
    t.join();
}
```

### Mutexes
```cpp
#include <mutex>

std::mutex mtx;
int counter = 0;

void increment() {
    std::lock_guard<std::mutex> lock(mtx);
    counter++;
}
```

### Condition Variables
```cpp
#include <condition_variable>

std::condition_variable cv;
std::mutex mtx;
bool ready = false;

void wait() {
    std::unique_lock<std::mutex> lock(mtx);
    cv.wait(lock, []{ return ready; });
}
```

### Async
```cpp
#include <future>

std::future<int> result = std::async([]{ return 42; });
int value = result.get();
```

## Production Incidents

### Incident 1: Data Race in Multithreaded Code
**Problem**: A trading engine produced incorrect P&L calculations when processing more than 1000 orders per second, causing a $2M reconciliation discrepancy.

**Cause**: A shared `OrderBook` struct had a `std::map<int, Order>` updated by the order-matching thread while a reporting thread iterated over it for P&L computation — no mutex protected the shared state. The map was modified mid-iteration, causing undefined behavior.

**Impact**: Incorrect financial reports sent to compliance. Manual reconciliation required 3 engineers working 12-hour shifts for 2 days. Regulatory filing delayed by 48 hours.

**Detection**: ThreadSanitizer flagged the race in a soak test with simulated production load. The race was intermittent — only reproduced after ~30 minutes of sustained 1000+ order/second throughput.

**Solution**: Split the `OrderBook` into a read-only snapshot and a mutable live copy. The reporting thread operates on an immutable snapshot taken at the start of each reporting window. The matching thread writes to the live copy and atomically swaps it at window boundaries.

**Prevention**: Enable TSan in all CI pipeline stages. Adopt a reader-writer pattern for shared state. Rule — any container accessed from multiple threads must be either immutable or protected by a mutex.

---

### Incident 2: Deadlock in Lock Ordering
**Problem**: A payment processing service deadlocked under load, freezing all transactions for 15 minutes until an operator manually restarted the process.

**Cause**: Thread A locked `mutex_account` then `mutex_ledger`. Thread B locked `mutex_ledger` then `mutex_account`. Both threads blocked waiting for the other to release — classic ABBA deadlock.

**Impact**: All inbound payments froze. 3,400 transactions failed. Customer-facing dashboard showed "processing" indefinitely. Two enterprise clients triggered SLA breach penalties.

**Detection**: `gdb` attach to the hung process showed two threads waiting on mutexes in a circular chain. `perf record` and FlameGraph analysis confirmed the deadlock pattern in the production binary.

**Solution**: Established a global lock ordering: always acquire `mutex_account` before `mutex_ledger`. Used `std::scoped_lock` (C++17) which locks multiple mutexes atomically, eliminating the possibility of interleaved acquisition.

**Prevention**: Rule — always use `std::scoped_lock` when acquiring multiple mutexes. Document lock ordering in code comments. Add a runtime lock-order validator in debug builds (`std::lock_guard` with `std::adopt_lock` pattern).

---

## Production Checklist
- [ ] Use `std::lock_guard` for automatic locking
- [ ] Avoid data races with proper synchronization
- [ ] Use `std::atomic` for simple shared data
- [ ] Prefer `std::async` over manual thread management
- [ ] Use thread pools for frequent task creation
- [ ] Test for deadlocks and race conditions

## Maturity Levels
- **Beginner**: Basic threads, join, detach
- **Intermediate**: Mutexes, condition variables
- **Advanced**: Lock-free programming, thread pools

## Common Myths
- ❌ "More threads always mean better performance"
- ❌ "Mutexes are always slow"
- ❌ "Concurrency is only for multi-core systems"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Thread | Independent execution path |
| Mutex | Mutual exclusion lock |
| Condition Variable | Thread synchronization |
| Atomic | Lock-free operations |
| Future/Promise | Async result delivery |

## Related Topics
- [Smart Pointers](../06-smart-pointers/)
- [Modern C++](../08-modern-cpp/)
- [Performance](../11-performance/)