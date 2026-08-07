# Python Concurrency Quiz

## Question 1 (MCQ - GIL Impact on Threading)
What does the GIL (Global Interpreter Lock) prevent in CPython?

- A) Multiple threads from executing Python bytecode simultaneously in a single process
- B) Multiple processes from running on different CPU cores
- C) Multiple threads from being created
- D) Multiple Python applications from running on the same machine

**Answer: A**
**Explanation:** The GIL is a mutex that allows only one thread to execute Python bytecode at a time per process. This means CPU-bound Python threads cannot run in parallel on multiple cores. However, I/O operations release the GIL, allowing other threads to run during waits.

---

## Question 2 (Scenario - Multiprocessing vs Threading)
You need to process 10,000 images (resize, apply filters). Which approach is better?

- A) `threading.Thread` — I/O-bound operations benefit from threads
- B) `multiprocessing.Process` — CPU-bound work benefits from separate processes
- C) `asyncio` — asynchronous is always best
- D) Single-threaded with optimization

**Answer: B**
**Explanation:** Image processing is CPU-bound — it uses the CPU heavily. Due to the GIL, threads won't provide true parallelism for CPU work. `multiprocessing` spawns separate processes, each with its own GIL, enabling actual parallel execution on multiple cores.

---

## Question 3 (MCQ - Asyncio Event Loop)
In `asyncio`, what happens when you call `await`?

- A) The entire program blocks until the coroutine completes
- B) The coroutine yields control to the event loop, allowing other tasks to run
- C) A new thread is created to run the coroutine
- D) The coroutine is removed from memory

**Answer: B**
**Explanation:** `await` yields control back to the event loop, not blocking the thread. The event loop can then schedule other coroutines. When the awaited operation completes (e.g., I/O), the event loop resumes the coroutine. This is cooperative multitasking within a single thread.

---

## Question 4 (Bug Finding - Thread Safety)
This code has a race condition. What is the issue?

```python
import threading

counter = 0

def increment():
    global counter
    for _ in range(100000):
        counter += 1

threads = [threading.Thread(target=increment) for _ in range(4)]
for t in threads:
    t.start()
for t in threads:
    t.join()

print(counter)
```

A) `counter` is not defined properly
B) `counter += 1` is not atomic — multiple threads can read the same value before writing
C) Threads cannot share global variables
D) `join()` causes deadlock

**Answer: B**
**Explanation:** `counter += 1` is actually three operations: read, add, write. Two threads can read the same value, both increment it, and write back — losing one increment. Use `threading.Lock` or `multiprocessing.Value` for thread-safe counting.

---

## Question 5 (Architecture Decision - Process Communication)
Two `multiprocessing.Process` instances need to share a large dataset. What is the most efficient approach?

- A) Pass data through command-line arguments
- B) Use `multiprocessing.Queue` to send data between processes
- C) Use `multiprocessing.shared_memory` or `multiprocessing.Array` for shared memory
- D) Write data to a file and have both processes read it

**Answer: C**
**Explanation:** `shared_memory` and `Array` allow processes to access the same memory region without copying data. `Queue` serializes/deserializes (slow for large data). Files involve disk I/O. Shared memory is fastest for large datasets since it avoids copying and serialization overhead.

---

## Question 6 (MCQ - Asyncio Tasks vs Threads)
When should you prefer `asyncio` over threading?

- A) When tasks are CPU-bound and need true parallelism
- B) When tasks are I/O-bound and you want efficient concurrency without thread overhead
- C) When tasks need to share memory easily
- D) `asyncio` is always preferred over threading

**Answer: B**
**Explanation:** `asyncio` is ideal for high-concurrency I/O-bound work (network requests, file I/O, database queries) because it runs in a single thread with low overhead. For CPU-bound work, use `multiprocessing`. Threading is a middle ground but has more overhead than `asyncio` for I/O-bound tasks.

---

## Question 7 (Bug Finding - Race Condition)
This code sometimes produces incorrect results. Find the race condition:

```python
import threading
import time

account_balance = 1000

def withdraw(amount):
    global account_balance
    if account_balance >= amount:
        time.sleep(0.01)  # Simulate processing
        account_balance -= amount
        print(f"Withdrew {amount}, balance: {account_balance}")

threads = [threading.Thread(target=withdraw, args=(800,)) for _ in range(3)]
for t in threads:
    t.start()
```

A) `time.sleep` causes the race condition
B) The check-then-act pattern: multiple threads can read `balance >= 800` before any thread deducts
C) `print` is not thread-safe
D) Global variables cannot be modified in threads

**Answer: B**
**Explanation:** All three threads can read `account_balance = 1000` (>= 800), then each deducts 800. After all three, the balance could be -1400. This is a classic TOCTOU (time-of-check-time-of-use) bug. Fix: use a `threading.Lock` around the entire check-and-deduct operation.

---

## Question 8 (MCQ - ThreadPoolExecutor)
What is the advantage of `ThreadPoolExecutor` over manually creating `Thread` objects?

- A) `ThreadPoolExecutor` uses multiprocessing internally
- B) It manages a pool of reusable threads, reducing creation overhead and simplifying task submission
- C) It provides no advantage over manual threads
- D) It automatically parallelizes CPU-bound tasks

**Answer: B**
**Explanation:** `ThreadPoolExecutor` maintains a fixed pool of threads. You submit callables and get `Future` objects. It handles thread lifecycle, reduces creation overhead, provides a clean API (`submit`, `map`, `as_completed`), and integrates well with `asyncio` via `loop.run_in_executor`.

---

## Question 9 (Code Output - Async Context Managers)
What is the output of this code?

```python
import asyncio

class AsyncDB:
    async def __aenter__(self):
        print("Connected")
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        print("Disconnected")
        return False

async def main():
    async with AsyncDB() as db:
        print("Querying")

asyncio.run(main())
```

A) `Connected` then `Querying` then `Disconnected`
B) `Querying` then `Connected` then `Disconnected`
C) `Connected` then `Disconnected` then `Querying`
D) Error: async context manager not supported

**Answer: A**
**Explanation:** `async with` calls `__aenter__` before the block and `__aexit__` after, just like synchronous context managers but for async code. The order is: "Connected" → "Querying" → "Disconnected".

---

## Question 10 (Architecture Decision - CPU-bound vs IO-bound)
A web scraper downloads pages and extracts data. Which architecture is best?

- A) Single-threaded synchronous processing
- B) `multiprocessing.Pool` for all tasks
- C) `asyncio` for downloading (I/O-bound), `multiprocessing` for extraction (CPU-bound)
- D) `threading` for everything

**Answer: C**
**Explanation:** The scraper has two phases: downloading (I/O-bound, waiting for network) and extracting (CPU-bound, parsing HTML). `asyncio` handles thousands of concurrent downloads efficiently in one thread. `multiprocessing` parallelizes CPU-heavy extraction across cores. Combining both gives optimal performance.
