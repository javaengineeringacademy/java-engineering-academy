# Senior Python Engineer Quiz

## Question 1 (Multiple Choice)
Your Python service receives 10,000 requests per second. Profiling shows the GIL is not the bottleneck — the real issue is synchronous database calls blocking the event loop. What is the most effective architectural change?

- A) Switch to multiprocessing to bypass the GIL
- B) Replace synchronous DB calls with async drivers (asyncpg, SQLAlchemy async) and use connection pooling
- C) Use `threading` to parallelize DB calls
- D) Rewrite the service in Go

**Answer: B**
**Explanation:** The GIL only affects CPU-bound work. For I/O-bound work like database calls, the solution is async I/O, not more processes/threads. `asyncpg` is 4-5x faster than psycopg2 because it's natively async and avoids ORM overhead. Connection pooling (asyncpg's built-in pool or `asyncpg.Pool`) prevents connection exhaustion. Multiprocessing adds IPC overhead and memory duplication. Threading works for I/O but has context-switching overhead. Async + connection pooling is the standard for high-concurrency Python services.

---

## Question 2 (Multiple Choice)
You're designing a microservices architecture. Service A calls Service B synchronously, which calls Service C. If Service C is slow (5s response time), Service A's response time degrades to 5s+. Which pattern best addresses this?

- A) Add more instances of Service A
- B) Circuit breaker pattern — stop calling Service C after failures, return fallback responses, and periodically test recovery
- C) Cache the slow responses
- D) Increase timeout on Service A

**Answer: B**
**Explanation:** The circuit breaker prevents cascading failures. When Service C exceeds a failure threshold, the circuit opens and immediately returns a fallback (cached data, default response, or error). A half-open state periodically tests if C has recovered. This protects Service A and B from being dragged down by C's latency. Caching helps but doesn't solve the timeout issue. Adding instances doesn't help if they all block on C. Increasing timeouts just delays the failure. Libraries: `pybreaker`, `tenacity`.

---

## Question 3 (Multiple Choice)
In a production Python service, you observe that memory usage grows from 200MB to 2GB over 24 hours, then stabilizes. After a restart, it happens again. What is the most likely cause?

- A) Python's natural memory fragmentation — it's normal
- B) A memory leak — objects are being created but never garbage collected, likely due to reference cycles, caches without eviction, or closures capturing objects
- C) The OS is allocating extra memory for caching
- D) NumPy arrays are larger than expected

**Answer: B**
**Explanation:** Steady growth that stabilizes and repeats after restarts is a textbook memory leak. Common Python causes: (1) reference cycles preventing GC (especially with `__del__`), (2) module-level caches (`functools.lru_cache` with no maxsize), (3) closures capturing large objects, (4) `logging` handlers accumulating messages, (5) threads not being joined. Use `tracemalloc` snapshots to identify the source. The stabilization at 2GB suggests the leak fills available memory or hits a GC threshold.

---

## Question 4 (Multiple Choice)
You need to deploy a Python ML model that must respond to predictions in under 50ms. The model is 2GB. Which deployment strategy is most appropriate?

- A) Load the model on every request — most accurate
- B) Load the model once at startup into memory, use async workers to handle requests, and serve with FastAPI + uvicorn on multiple workers
- C) Use Django with synchronous views — it's simpler
- D) Package the model as a Lambda function — it's serverless

**Answer: B**
**Explanation:** Loading a 2GB model takes 10-30 seconds — doing this per request is unacceptable. Load once at startup (module-level or in a startup event), keep in memory, serve predictions via async endpoints. FastAPI + uvicorn with multiple workers (gunicorn with uvicorn workers) distributes requests. Lambda has cold start issues with 2GB models (10-30s init time). Django's sync views would block the worker during inference. The pattern: load model in `startup` event, serve via async endpoint, use `run_in_executor` if inference is CPU-bound.

---

## Question 5 (Code Output)
What is the output of this code?

```python
import asyncio

async def fetch(name, delay):
    print(f"Start {name}")
    await asyncio.sleep(delay)
    print(f"End {name}")
    return f"{name}: {delay}s"

async def main():
    results = await asyncio.gather(
        fetch("A", 2),
        fetch("B", 1),
        fetch("C", 3),
    )
    print(results)

asyncio.run(main())
```

**Answer:**
```
Start A
Start B
Start C
End B
End A
End C
['A: 2s', 'B: 1s', 'C: 3s']
```
**Explanation:** `asyncio.gather` runs all coroutines concurrently. All three start immediately (printing Start A, B, C). B finishes first (1s delay), then A (2s), then C (3s). `gather` returns results in the order they were passed, not completion order. Total wall time is ~3s (max delay), not 6s (sum). This demonstrates cooperative concurrency — all tasks share the same thread, yielding control at `await` points.

---

## Question 6 (Code Output)
What is the output of this code?

```python
from functools import lru_cache
import sys

@lru_cache(maxsize=None)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)

result = fibonacci(30)
print(f"fib(30) = {result}")
print(f"Cache size: {fibonacci.cache_info().currsize}")
print(f"Memory per cache entry: ~{sys.getsizeof(fibonacci)} bytes")
```

**Answer:**
```
fib(30) = 832040
Cache size: 31
Memory per cache entry: ~112 bytes (approximate)
```
**Explanation:** `fibonacci(30)` computes all values from 0 to 30, storing each in the cache. Cache size is 31 (fib(0) through fib(30)). The `sys.getsizeof` shows the function object size, not the cache size — the actual cache uses a dict internally. Each cached entry is a dict bucket + key + value, roughly 100-200 bytes. For `maxsize=None`, the cache grows unbounded — for large n, this can consume significant memory. Use `lru_cache(maxsize=128)` for production.

---

## Question 7 (Bug Finding)
Find the bug in this circuit breaker implementation:

```python
import time
from enum import Enum

class CircuitState(Enum):
    CLOSED = "closed"
    OPEN = "open"

class CircuitBreaker:
    def __init__(self, failure_threshold=5, recovery_timeout=60):
        self.failure_threshold = failure_threshold
        self.recovery_timeout = recovery_timeout
        self.failure_count = 0
        self.state = CircuitState.CLOSED
        self.last_failure_time = 0

    def call(self, func, *args, **kwargs):
        if self.state == CircuitState.OPEN:
            if time.time() - self.last_failure_time > self.recovery_timeout:
                self.state = CircuitState.CLOSED
                self.failure_count = 0
            else:
                raise Exception("Circuit is OPEN")
        
        try:
            result = func(*args, **kwargs)
            self.failure_count = 0
            return result
        except Exception as e:
            self.failure_count += 1
            self.last_failure_time = time.time()
            if self.failure_count >= self.failure_threshold:
                self.state = CircuitState.OPEN
            raise
```

**Bug:** The circuit breaker has no HALF_OPEN state. After the recovery timeout, it transitions directly from OPEN to CLOSED. If the service is still failing, it immediately hits the failure threshold again and reopens. A proper circuit breaker needs a HALF_OPEN state where only one trial request is allowed — if it succeeds, close the circuit; if it fails, reopen it. Also, there's no thread safety — concurrent calls can race on `failure_count` and `state`.
**Fix:** Add HALF_OPEN state and use `threading.Lock` for thread safety.

---

## Question 8 (Bug Finding)
Find the bug in this rate limiter:

```python
import time
from collections import deque

class SlidingWindowRateLimiter:
    def __init__(self, max_requests, window_seconds):
        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.timestamps = deque()

    def allow(self):
        now = time.time()
        while self.timestamps and self.timestamps[0] < now - self.window_seconds:
            self.timestamps.popleft()
        
        if len(self.timestamps) < self.max_requests:
            self.timestamps.append(now)
            return True
        return False

limiter = SlidingWindowRateLimiter(3, 60)
print(limiter.allow())  # True
print(limiter.allow())  # True
print(limiter.allow())  # True
print(limiter.allow())  # False
```

**Bug:** This works correctly for a single-threaded scenario but has a race condition in multi-threaded environments. Two threads can both call `allow()` simultaneously, both see `len(self.timestamps) < self.max_requests`, both append, and exceed the limit. The `deque` operations are not atomic. Also, there's no synchronization — `popleft()` and `append()` can interleave between threads.
**Fix:** Add `threading.Lock`:
```python
import threading

class SlidingWindowRateLimiter:
    def __init__(self, max_requests, window_seconds):
        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.timestamps = deque()
        self.lock = threading.Lock()

    def allow(self):
        with self.lock:
            now = time.time()
            while self.timestamps and self.timestamps[0] < now - self.window_seconds:
                self.timestamps.popleft()
            if len(self.timestamps) < self.max_requests:
                self.timestamps.append(now)
                return True
            return False
```

---

## Question 9 (Scenario)
You're leading a team migrating a monolithic Python 2.7 Django application to Python 3.12 with microservices. The codebase has 500K LOC, 200 database tables, and custom middleware. The business cannot afford more than 4 hours of downtime. How should you approach this?

- A) Big bang rewrite — rewrite everything in Python 3 at once and deploy
- B) Strangler fig pattern — incrementally replace modules with Python 3 microservices, using an API gateway to route traffic between old and new, with a dual-write strategy for data migration
- C) Fork the codebase and migrate in a branch
- D) Run Python 2 and Python 3 side by side with separate databases

**Answer: B**
**Explanation:** The strangler fig pattern is the gold standard for large migrations. Start with a low-risk module (e.g., notification service), build it as a Python 3 microservice, deploy it behind an API gateway, and route traffic to it. The gateway proxies unchanged modules to the monolith. Data migration uses dual-write (write to both old and new DB) or CDC (Change Data Capture) with Debezium. This allows incremental migration, rollback at any step, and zero downtime. Big bang rewrites have a >70% failure rate for codebases this size.

---

## Question 10 (Architecture Decision)
You're designing a distributed system that processes financial transactions. Requirements: exactly-once processing, audit trail for every operation, ability to replay events from any point in time, and sub-second latency. How should you architect this?

- A) PostgreSQL with serializable transactions — handles consistency but not replay
- B) Event sourcing with Kafka (partitioned, ordered logs) + CQRS + idempotent consumers + event store for audit/replay
- C) Redis for everything — it's fast enough
- D) REST API with database polling for new events

**Answer: B**
**Explanation:** Event sourcing stores every state change as an immutable event in Kafka. This provides: (1) exactly-once via idempotent consumers and Kafka transactions, (2) complete audit trail (events ARE the audit), (3) time-travel replay by reading from any offset, (4) sub-second latency via Kafka's batched commits. CQRS separates read/write models for performance. The event store (Kafka + compacted topics or a dedicated store like EventStoreDB) is the source of truth. REST + polling can't provide replay or exactly-once. Redis loses data on restart and doesn't provide ordered event history. This is how banks, exchanges, and financial systems handle transaction processing.

---
