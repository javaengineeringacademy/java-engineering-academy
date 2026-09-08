# Concurrency — C Language

## Why It Matters

When you're building web servers, databases, or any system handling multiple tasks simultaneously, single-threaded programs can't utilize multiple CPU cores or overlap I/O with processing. Concurrency lets you handle thousands of connections simultaneously, keep UIs responsive during computation, and improve throughput — but it introduces new bug classes: race conditions, deadlocks, and data corruption impossible in single-threaded code.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Multi-core utilization, overlapping I/O, responsive UIs | Single-threaded with async I/O for simple cases |
| When NOT to use | When synchronization overhead exceeds parallelism benefit | Event-driven (epoll/kqueue) for I/O-bound loads |
| Alternatives | Go goroutines, Rust async/tokio, Erlang processes | Higher-level abstractions, different trade-offs |
| Production Examples | Nginx (worker processes), Redis (single-threaded + I/O threads), PostgreSQL | Threading models vary by workload |
| Common Mistakes | `volatile` for thread safety, not checking `pthread_cond_wait` return | Use `stdatomic.h`, always use `while` loop for condvars |

## What It Is

C provides concurrency through POSIX threads (pthreads), C11 atomics, and platform-specific APIs:

| Mechanism | Purpose | Header |
|-----------|---------|--------|
| Threads | Independent execution paths | `<pthread.h>` |
| Mutex | Mutual exclusion (protect shared data) | `<pthread.h>` |
| Condition variables | Thread synchronization (wait/signal) | `<pthread.h>` |
| Semaphores | Counting synchronization | `<semaphore.h>` |
| Atomics | Thread-safe operations without locks | `<stdatomic.h>` |

## Why It Exists

Concurrency is not optional in modern systems:
- Every multi-core CPU can execute threads in parallel
- Every network server handles concurrent connections
- Every GUI must remain responsive during background work
- Every database processes concurrent transactions

C's concurrency model maps directly to OS primitives — no runtime overhead, no garbage collector interference.

### Architecture: Threading Models

```
Single-threaded
┌──────────────┐
│  Main Thread  │ → Sequential execution
└──────────────┘

Multi-threaded (shared memory)
┌──────────┐ ┌──────────┐ ┌──────────┐
│ Thread 1 │ │ Thread 2 │ │ Thread 3 │ → Share address space
└──────────┘ └──────────┘ └──────────┘
       ↕           ↕           ↕
   ┌─────────────────────────────────┐
   │         Shared Memory           │
   │   (requires synchronization)   │
   └─────────────────────────────────┘

Event-driven (single thread, async I/O)
┌──────────────────────────────────┐
│  Main Thread                      │
│  ├── poll/select/epoll            │
│  ├── Handle ready fd              │
│  └── Repeat                       │
└──────────────────────────────────┘
```

## Expanded Code Examples

### Thread Creation and Management

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct {
    int id;
    int iterations;
    int result;
} ThreadArg;

void *worker(void *arg) {
    ThreadArg *ta = (ThreadArg *)arg;
    ta->result = 0;
    for (int i = 0; i < ta->iterations; i++) {
        ta->result += i;
    }
    printf("Thread %d: result = %d\n", ta->id, ta->result);
    return NULL;
}

int main(void) {
    const int NUM_THREADS = 4;
    pthread_t threads[NUM_THREADS];
    ThreadArg args[NUM_THREADS];

    for (int i = 0; i < NUM_THREADS; i++) {
        args[i].id = i;
        args[i].iterations = 1000000;
        if (pthread_create(&threads[i], NULL, worker, &args[i]) != 0) {
            perror("pthread_create");
            return 1;
        }
    }

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    int total = 0;
    for (int i = 0; i < NUM_THREADS; i++) {
        total += args[i].result;
    }
    printf("Total: %d\n", total);

    return 0;
}
```

### Mutex — Protecting Shared Data

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define NUM_THREADS 4
#define ITERATIONS 1000000

int counter = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *increment(void *arg) {
    (void)arg;
    for (int i = 0; i < ITERATIONS; i++) {
        pthread_mutex_lock(&mutex);
        counter++;
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main(void) {
    pthread_t threads[NUM_THREADS];

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_create(&threads[i], NULL, increment, NULL);
    }

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("Counter: %d (expected: %d)\n", counter, NUM_THREADS * ITERATIONS);
    pthread_mutex_destroy(&mutex);
    return 0;
}
```

### Condition Variables — Producer-Consumer

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define BUFFER_SIZE 10

typedef struct {
    int buffer[BUFFER_SIZE];
    int count;
    int in;
    int out;
    pthread_mutex_t mutex;
    pthread_cond_t not_full;
    pthread_cond_t not_empty;
} BoundedBuffer;

void bb_init(BoundedBuffer *bb) {
    bb->count = bb->in = bb->out = 0;
    pthread_mutex_init(&bb->mutex, NULL);
    pthread_cond_init(&bb->not_full, NULL);
    pthread_cond_init(&bb->not_empty, NULL);
}

void bb_destroy(BoundedBuffer *bb) {
    pthread_mutex_destroy(&bb->mutex);
    pthread_cond_destroy(&bb->not_full);
    pthread_cond_destroy(&bb->not_empty);
}

void bb_put(BoundedBuffer *bb, int item) {
    pthread_mutex_lock(&bb->mutex);
    while (bb->count == BUFFER_SIZE) {
        pthread_cond_wait(&bb->not_full, &bb->mutex);
    }
    bb->buffer[bb->in] = item;
    bb->in = (bb->in + 1) % BUFFER_SIZE;
    bb->count++;
    pthread_cond_signal(&bb->not_empty);
    pthread_mutex_unlock(&bb->mutex);
}

int bb_get(BoundedBuffer *bb) {
    pthread_mutex_lock(&bb->mutex);
    while (bb->count == 0) {
        pthread_cond_wait(&bb->not_empty, &bb->mutex);
    }
    int item = bb->buffer[bb->out];
    bb->out = (bb->out + 1) % BUFFER_SIZE;
    bb->count--;
    pthread_cond_signal(&bb->not_full);
    pthread_mutex_unlock(&bb->mutex);
    return item;
}

// Producer thread
void *producer(void *arg) {
    BoundedBuffer *bb = (BoundedBuffer *)arg;
    for (int i = 0; i < 100; i++) {
        bb_put(bb, i);
    }
    return NULL;
}

// Consumer thread
void *consumer(void *arg) {
    BoundedBuffer *bb = (BoundedBuffer *)arg;
    for (int i = 0; i < 100; i++) {
        int item = bb_get(bb);
        printf("Consumed: %d\n", item);
    }
    return NULL;
}
```

### Atomic Operations — Lock-Free Counters

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <stdatomic.h>

#define NUM_THREADS 4
#define ITERATIONS 1000000

atomic_int atomic_counter = 0;
int non_atomic_counter = 0;

void *increment_both(void *arg) {
    (void)arg;
    for (int i = 0; i < ITERATIONS; i++) {
        atomic_fetch_add(&atomic_counter, 1);
        non_atomic_counter++;  // Data race!
    }
    return NULL;
}

int main(void) {
    pthread_t threads[NUM_THREADS];

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_create(&threads[i], NULL, increment_both, NULL);
    }
    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("Atomic:      %d (expected: %d)\n",
           atomic_load(&atomic_counter), NUM_THREADS * ITERATIONS);
    printf("Non-atomic:  %d (expected: %d, data race!)\n",
           non_atomic_counter, NUM_THREADS * ITERATIONS);

    return 0;
}
```

### Thread Pool Pattern

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define POOL_SIZE 4
#define TASK_QUEUE_SIZE 100

typedef void (*TaskFunc)(void *arg);

typedef struct {
    TaskFunc func;
    void *arg;
} Task;

typedef struct {
    Task queue[TASK_QUEUE_SIZE];
    int head, tail, count;
    pthread_mutex_t mutex;
    pthread_cond_t not_empty;
    pthread_cond_t not_full;
    int shutdown;
} ThreadPool;

typedef struct {
    ThreadPool *pool;
    int id;
} WorkerArg;

void *worker_thread(void *arg) {
    WorkerArg *wa = (WorkerArg *)arg;
    ThreadPool *pool = wa->pool;

    while (1) {
        pthread_mutex_lock(&pool->mutex);
        while (pool->count == 0 && !pool->shutdown) {
            pthread_cond_wait(&pool->not_empty, &pool->mutex);
        }
        if (pool->shutdown) {
            pthread_mutex_unlock(&pool->mutex);
            break;
        }

        Task task = pool->queue[pool->head];
        pool->head = (pool->head + 1) % TASK_QUEUE_SIZE;
        pool->count--;
        pthread_cond_signal(&pool->not_full);
        pthread_mutex_unlock(&pool->mutex);

        task.func(task.arg);
    }
    return NULL;
}

void pool_init(ThreadPool *pool) {
    pool->head = pool->tail = pool->count = 0;
    pool->shutdown = 0;
    pthread_mutex_init(&pool->mutex, NULL);
    pthread_cond_init(&pool->not_empty, NULL);
    pthread_cond_init(&pool->not_full, NULL);

    pthread_t threads[POOL_SIZE];
    WorkerArg args[POOL_SIZE];
    for (int i = 0; i < POOL_SIZE; i++) {
        args[i].pool = pool;
        args[i].id = i;
        pthread_create(&threads[i], NULL, worker_thread, &args[i]);
    }
}
```

## Production Incidents

### Incident 1: Race Condition in Counter

**Problem**: Multi-threaded counter shows inconsistent counts.

**Cause**: `counter++` is read-modify-write, not atomic:

```c
int counter = 0;
void *increment(void *arg) {
    for (int i = 0; i < 1000000; i++) counter++;
    return NULL;
}
```

**Solution**: Use atomic operations or mutex:

```c
#include <stdatomic.h>
atomic_int counter = 0;
void *increment(void *arg) {
    for (int i = 0; i < 1000000; i++) atomic_fetch_add(&counter, 1);
    return NULL;
}
```

### Incident 2: Deadlock from Lock Ordering

**Problem**: Two threads periodically deadlock, freezing the application.

**Cause**: Threads acquire locks in opposite orders:

```c
// Thread 1: lock(A), lock(B)
// Thread 2: lock(B), lock(A)
```

**Solution**: Establish global lock ordering:

```c
// Always: lock(A), then lock(B)
// Document and enforce this ordering
```

### Incident 3: Condition Variable Spurious Wakeup

**Problem**: A producer-consumer queue occasionally loses data due to spurious wakeups.

```c
void *consumer(void *arg) {
    Queue *q = arg;
    while (true) {
        pthread_mutex_lock(&q->mutex);
        pthread_cond_wait(&q->not_empty, &q->mutex);  // May wake spuriously
        Item *item = dequeue(q);
        pthread_mutex_unlock(&q->mutex);
        process(item);
    }
}
```

**Cause**: `pthread_cond_wait` can wake up without a signal (spurious wakeup). The queue may be empty when the consumer wakes.

**Impact**: NULL pointer dereference, crash, data loss.

**Solution**: Always check condition in a while loop:

```c
void *consumer(void *arg) {
    Queue *q = arg;
    while (true) {
        pthread_mutex_lock(&q->mutex);
        while (queue_empty(q)) {
            pthread_cond_wait(&q->not_empty, &q->mutex);
        }
        Item *item = dequeue(q);
        pthread_mutex_unlock(&q->mutex);
        process(item);
    }
}
```

**Prevention**: Always use `while` loop (not `if`) with `pthread_cond_wait`; never assume the condition is true after wakeup.

---

### Incident 4: Thread Pool Exhaustion Under Load

**Problem**: A web server's thread pool becomes exhausted under high load, causing request timeouts.

```c
#define MAX_THREADS 64

typedef struct {
    pthread_t threads[MAX_THREADS];
    int thread_count;
    // ...
} ThreadPool;

void handle_connection(int client_fd) {
    // Process request...
    sleep(10);  // Simulate slow processing
    close(client_fd);
}

// All 64 threads are blocked in handle_connection
// New connections cannot be processed
```

**Cause**: Thread pool size is fixed; slow requests block all threads.

**Impact**: Service unavailability, request timeouts, client failures.

**Solution**: Use work queue with bounded thread pool and reject policy:

```c
#define MAX_THREADS 64
#define MAX_QUEUE 1000

typedef struct {
    pthread_t threads[MAX_THREADS];
    TaskQueue queue;
    pthread_mutex_t mutex;
    bool shutdown;
} ThreadPool;

void *worker(void *arg) {
    ThreadPool *pool = arg;
    while (true) {
        pthread_mutex_lock(&pool->mutex);
        while (queue_empty(&pool->queue) && !pool->shutdown) {
            pthread_cond_wait(&pool->cond, &pool->mutex);
        }
        if (pool->shutdown) {
            pthread_mutex_unlock(&pool->mutex);
            break;
        }
        Task task = dequeue(&pool->queue);
        pthread_mutex_unlock(&pool->mutex);
        task.fn(task.arg);
    }
    return NULL;
}
```

**Prevention**: Size thread pool based on workload; use bounded queue with reject policy; monitor queue depth and thread utilization.

---

### Incident 5: ABA Problem in Lock-Free Stack

**Problem**: A lock-free stack using compare-and-swap (CAS) experiences ABA problem, causing data corruption.

```c
typedef struct Node {
    int data;
    struct Node *next;
} Node;

_Atomic(Node *) stack_top = NULL;

void push(int value) {
    Node *new_node = malloc(sizeof(Node));
    new_node->data = value;
    Node *old_top;
    do {
        old_top = atomic_load(&stack_top);
        new_node->next = old_top;
    } while (!atomic_compare_exchange_weak(&stack_top, &old_top, new_node));
}

int pop(void) {
    Node *old_top;
    int value;
    do {
        old_top = atomic_load(&stack_top);
        if (!old_top) return -1;
        value = old_top->data;
        // ABA: old_top may have been freed and reallocated at same address
    } while (!atomic_compare_exchange_weak(&stack_top, &old_top, old_top->next));
    free(old_top);  // May free a different node at the same address
    return value;
}
```

**Cause**: Thread reads old_top (A), gets preempted, another thread pops and pushes, reusing the same address (A). The CAS succeeds incorrectly.

**Impact**: Data corruption, use-after-free, crash.

**Solution**: Use hazard pointers or tagged pointers:

```c
// Option 1: Use hazard pointers for safe memory reclamation
// Option 2: Use generation counter (tagged pointer)
typedef struct {
    Node *ptr;
    uintptr_t tag;  // Monotonically increasing
} TaggedPointer;

_Atomic(TaggedPointer) stack_top = {0};
```

**Prevention**: Use hazard pointers, epoch-based reclamation, or tagged pointers for lock-free structures.

## Production Checklist

- [ ] Use proper synchronization for shared data
- [ ] Establish and document lock ordering to prevent deadlock
- [ ] Use atomic operations for simple counters and flags
- [ ] Test with ThreadSanitizer (`-fsanitize=thread`)
- [ ] Minimize critical section length
- [ ] Use `pthread_cond_wait` with `while` loop (spurious wakeups)
- [ ] Initialize mutexes and condition variables before use
- [ ] Destroy mutexes and condition variables when done
- [ ] Avoid holding locks during I/O operations
- [ ] Use thread pools for repeated task execution

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Creates and joins threads | Uses `pthread_create` and `pthread_join` |
| **Intermediate** | Uses mutexes and condition variables | Protects shared data, implements producer-consumer |
| **Advanced** | Masters lock-free programming | Uses atomics, implements lock-free structures |
| **Expert** | Designs concurrent architectures | Thread pools, work stealing, NUMA-aware design |

## Common Myths Debunked

1. **Myth**: Threads are always faster
   **Truth**: Threads add synchronization overhead. For simple tasks, single-threaded code may be faster. Use threads when there is actual parallelism benefit.

2. **Myth**: Atomic operations are always safe
   **Truth**: Atomics solve individual variable races, but complex operations may need multiple atomics or locks. Atomics don't help with compound invariants.

3. **Myth**: `volatile` makes variables thread-safe
   **Truth**: `volatile` prevents compiler optimization but does NOT prevent CPU reordering or provide atomicity. Use `stdatomic.h` for thread safety.

## One-Minute Revision

| Concept | Description | Key Detail |
|---------|-------------|------------|
| Thread | Independent execution path | Shares address space with other threads |
| Mutex | Mutual exclusion lock | Protects critical sections |
| Condition | Thread synchronization | Wait/signal pattern |
| Semaphore | Counting synchronization | Limits concurrent access |
| Atomic | Thread-safe operations | No lock needed for simple ops |
| Deadlock | Threads waiting forever | Prevent with lock ordering |
| Race condition | Non-deterministic behavior | Protect shared data |
| Volatile | NOT thread-safe | Only prevents compiler optimization |

## Related Topics

- [Performance](../12-performance/README.md) — Parallelism and optimization
- [Networking](../10-networking/README.md) — Concurrent network servers
- [Best Practices](../15-best-practices/README.md) — Coding standards for concurrent code

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Race conditions on shared data | ThreadSanitizer | Compile with `-fsanitize=thread`; detects data races with stack traces for both access locations |
| Deadlock from lock ordering violation | Lock hierarchy documentation + timeout | Document global lock ordering; use `pthread_mutex_timedlock` to detect potential deadlocks |
| Spurious wakeups in condition variables | `while` loop pattern check | Ensure `pthread_cond_wait` is always in a `while` loop checking the condition, not `if` |
| Thread creation/JOIN resource leak | Valgrind `--show-reachable=yes` | Check for unjoined threads and uninitialized mutexes/condvars in Valgrind output |
| `volatile` misuse for thread safety | Code review + TSan | Replace `volatile` with `atomic_load`/`atomic_store` from `<stdatomic.h>`; TSan catches volatile data races |

## Code Review Checklist

- [ ] All shared data protected by mutex (or atomic operations)
- [ ] Lock ordering documented and consistently followed (prevents deadlock)
- [ ] `pthread_cond_wait` used with `while` loop (not `if`) to handle spurious wakeups
- [ ] Mutexes and condvars initialized before use and destroyed when done
- [ ] No locks held during I/O operations (prevents priority inversion)
- [ ] Thread creation return values checked for errors
- [ ] Atomic operations used for simple counters and flags (not `volatile`)

## Architecture Considerations

Concurrency in C maps directly to OS primitives — pthreads, mutexes, condition variables, and atomics. The choice of threading model depends on workload: shared-memory threads for CPU-bound parallelism, event-driven I/O (epoll/kqueue) for connection-bound servers, or hybrid models (thread pool + event loop). Lock-free programming with `<stdatomic.h>` eliminates mutex overhead for simple operations but requires careful memory ordering reasoning.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Thread pool | Repeated task execution, server workloads | Amortizes thread creation cost; bounded by pool size |
| Event loop (epoll/kqueue) | High-concurrency I/O-bound servers | Single-threaded simplicity; scales to millions of connections |
| Lock-free with atomics | Simple counters, stacks, queues | No mutex overhead; complex to verify correctness |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Race conditions on authentication/state | Bypass security checks, data corruption | Protect all shared state with mutex; use atomics for flags |
| Priority inversion (low-priority holds lock) | High-priority thread blocked indefinitely | Use priority inheritance mutexes (`PTHREAD_PRIO_INHERIT`) |
| Thread-unsafe library functions | Data corruption in multi-threaded context | Use `_r` variants (`strtok_r`, `asctime_r`); avoid global state |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | No native threading support | Use POSIX threads (`<pthread.h>`) on Unix; Windows threads on Windows |
| C99 → C11 | Added `<threads.h>`, `<stdatomic.h>`, `_Thread_local` | Use `<threads.h>` for portable threading; use `<stdatomic.h>` for lock-free operations |
| C11 → C23 | Improved `constexpr` for compile-time constants | Use `constexpr` for thread configuration constants; continue using `<stdatomic.h>` |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| `<pthread.h>` (POSIX threads) | POSIX (not C standard) | Widely available on Unix; use `<threads.h>` for C11 portability |
| `<threads.h>` (C11 threads) | C11 | Standard but limited platform support; prefer pthreads on Unix |
| `<stdatomic.h>` (atomics) | C11 | Standard — use for lock-free operations |
| `_Thread_local` storage | C11 | Standard — thread-local storage qualifier |

## Interview Questions

1. **What is the difference between a mutex and an atomic operation?**: A mutex provides mutual exclusion for critical sections of arbitrary size. Atomics provide lock-free operations on individual variables (increment, compare-and-swap). Use atomics for simple counters/flags; use mutexes for complex state.
2. **Why must `pthread_cond_wait` be used in a `while` loop?**: Spurious wakeups can occur — the condition may not actually be true when the thread wakes up. A `while` loop rechecks the condition, ensuring the thread only proceeds when the condition is actually met.
3. **How do you prevent deadlock in a multi-lock scenario?**: Establish a global lock ordering (e.g., always lock A before B). Document and enforce this ordering. Alternatively, use `pthread_mutex_trylock` to attempt locks and back off on failure.
4. **What does `volatile` actually do and why is it not thread-safe?**: `volatile` prevents compiler optimization (reordering, caching in registers) but does NOT prevent CPU reordering or provide atomicity. It was designed for memory-mapped I/O, not thread safety. Use `<stdatomic.h>` for thread safety.
5. **When should you use a thread pool instead of creating threads on demand?**: Use a thread pool when you have many short-lived tasks (server requests, queued work). Thread creation has significant overhead (stack allocation, kernel calls). A pool amortizes this cost and bounds the number of concurrent threads.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [POSIX Threads Programming (LLNL)](https://hpc-tutorials.llc.us/posix/)
- [Secure Coding in C and CERT C Coding Standard](https://wiki.sei.cmu.edu/confluence/display/c/)

## Overview

The Concurrency module covers multi-threaded programming in C using POSIX threads (pthreads), C11 atomics, and synchronization primitives. Concurrency enables handling thousands of connections simultaneously, keeping UIs responsive, and improving throughput — but introduces race conditions, deadlocks, and data corruption.

## Learning Objectives

- Create and manage threads with pthreads
- Protect shared data with mutexes
- Synchronize threads with condition variables
- Use atomic operations for lock-free programming
- Implement thread pool patterns

## Prerequisites

- Completion of Module 08 (Memory Management)
- Understanding of pointers and functions
- Basic understanding of processes

## History

- **1972** — Processes and signals in original C
- **1978** — K&R C documented process creation
- **1989** — ANSI C standardized `setjmp`/`longjmp`
- **1995** — POSIX threads (pthreads) standardized
- **2011** — C11 added `<stdatomic.h>` and `<threads.h>`
- **2023** — C23 added improved atomic operations

## Production Notes

- **Where is it used?** Web servers, databases, operating systems, game engines
- **Why is it useful?** Multi-core utilization, overlapping I/O, responsive UIs
- **When should it be avoided?** When synchronization overhead exceeds parallelism benefit
- **Alternative?** Go goroutines, Rust async/tokio, Erlang processes

## Core Concepts

### Concurrency Mechanisms

| Mechanism | Purpose | Header |
|-----------|---------|--------|
| Threads | Independent execution paths | `<pthread.h>` |
| Mutex | Mutual exclusion (protect shared data) | `<pthread.h>` |
| Condition variables | Thread synchronization (wait/signal) | `<pthread.h>` |
| Semaphores | Counting synchronization | `<semaphore.h>` |
| Atomics | Thread-safe operations without locks | `<stdatomic.h>` |

### Threading Models

| Model | Description | Use Case |
|-------|-------------|----------|
| One thread per task | Simple but doesn't scale | Low-concurrency apps |
| Thread pool | Reuse threads for tasks | Web servers |
| Event-driven | Single thread + async I/O | High-concurrency I/O |
| Actor model | Message passing between actors | Distributed systems |

## Internal Working

### Thread Execution

```
Main Thread
    ↓ pthread_create()
New Thread
    ↓
Thread Body Function
    ↓
pthread_exit() or return
    ↓
Main Thread continues
```

### Mutex Lock/Unlock

```
Thread A: lock(mutex) → critical section → unlock(mutex)
Thread B: lock(mutex) → waits... → critical section → unlock(mutex)
```

## Syntax

```c
#include <pthread.h>
#include <stdatomic.h>

// Thread creation
void *thread_func(void *arg) {
    printf("Hello from thread\n");
    return NULL;
}

int main(void) {
    pthread_t thread;
    pthread_create(&thread, NULL, thread_func, NULL);
    pthread_join(thread, NULL);
    return 0;
}

// Mutex
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_lock(&mutex);
// critical section
pthread_mutex_unlock(&mutex);

// Condition variable
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
pthread_mutex_lock(&mutex);
while (!ready) {
    pthread_cond_wait(&cond, &mutex);
}
pthread_mutex_unlock(&mutex);

// Atomic operations
atomic_int counter = ATOMIC_VAR_INIT(0);
atomic_fetch_add(&counter, 1);
int val = atomic_load(&counter);
```

## Examples

### Easy Example: Thread Creation

```c
#include <stdio.h>
#include <pthread.h>

void *print_message(void *arg) {
    char *msg = (char *)arg;
    printf("%s\n", msg);
    return NULL;
}

int main(void) {
    pthread_t t1, t2;
    pthread_create(&t1, NULL, print_message, "Thread 1");
    pthread_create(&t2, NULL, print_message, "Thread 2");
    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    return 0;
}
```

### Medium Example: Mutex Counter

```c
#include <stdio.h>
#include <pthread.h>

int counter = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *increment(void *arg) {
    for (int i = 0; i < 100000; i++) {
        pthread_mutex_lock(&mutex);
        counter++;
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main(void) {
    pthread_t t1, t2;
    pthread_create(&t1, NULL, increment, NULL);
    pthread_create(&t2, NULL, increment, NULL);
    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    printf("Counter: %d\n", counter);  // 200000
    return 0;
}
```

### Hard Example: Producer-Consumer

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define BUFFER_SIZE 10
int buffer[BUFFER_SIZE];
int count = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t not_full = PTHREAD_COND_INITIALIZER;
pthread_cond_t not_empty = PTHREAD_COND_INITIALIZER;

void *producer(void *arg) {
    for (int i = 0; i < 20; i++) {
        pthread_mutex_lock(&mutex);
        while (count == BUFFER_SIZE) {
            pthread_cond_wait(&not_full, &mutex);
        }
        buffer[count++] = i;
        pthread_cond_signal(&not_empty);
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

void *consumer(void *arg) {
    for (int i = 0; i < 20; i++) {
        pthread_mutex_lock(&mutex);
        while (count == 0) {
            pthread_cond_wait(&not_empty, &mutex);
        }
        int item = buffer[--count];
        pthread_cond_signal(&not_full);
        pthread_mutex_unlock(&mutex);
        printf("Consumed: %d\n", item);
    }
    return NULL;
}
```

### Enterprise Example: Thread Pool

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct Task {
    void (*function)(void *);
    void *arg;
    struct Task *next;
} Task;

typedef struct {
    pthread_t *threads;
    Task *task_head;
    Task *task_tail;
    pthread_mutex_t mutex;
    pthread_cond_t not_empty;
    int thread_count;
    int running;
} ThreadPool;

void *worker(void *arg) {
    ThreadPool *pool = (ThreadPool *)arg;
    while (1) {
        pthread_mutex_lock(&pool->mutex);
        while (!pool->task_head && pool->running) {
            pthread_cond_wait(&pool->not_empty, &pool->mutex);
        }
        if (!pool->running && !pool->task_head) {
            pthread_mutex_unlock(&pool->mutex);
            break;
        }
        Task *task = pool->task_head;
        pool->task_head = task->next;
        pthread_mutex_unlock(&pool->mutex);
        task->function(task->arg);
        free(task);
    }
    return NULL;
}

ThreadPool *pool_create(int count) {
    ThreadPool *pool = malloc(sizeof(ThreadPool));
    pool->thread_count = count;
    pool->running = 1;
    pool->task_head = pool->task_tail = NULL;
    pthread_mutex_init(&pool->mutex, NULL);
    pthread_cond_init(&pool->not_empty, NULL);
    pool->threads = malloc(count * sizeof(pthread_t));
    for (int i = 0; i < count; i++) {
        pthread_create(&pool->threads[i], NULL, worker, pool);
    }
    return pool;
}
```

## Performance Considerations

| Aspect | Consideration | Optimization |
|--------|---------------|--------------|
| Lock contention | Serializes execution | Minimize critical section |
| Context switching | Expensive | Use thread pool |
| False sharing | Cache line conflicts | Pad shared data |
| Lock-free | No locks | Use atomics for simple operations |
| Thread creation | Expensive | Reuse threads |

## Best Practices

- Do:
  - Always check pthread return values
  - Use `while` loop for condition variables
  - Initialize mutexes before use
  - Destroy mutexes when done
  - Use atomics for simple counters
  
- Don't:
  - Use `volatile` for thread safety
  - Forget to unlock mutexes
  - Hold mutexes while calling `pthread_cond_wait`
  - Create too many threads
  - Ignore return values from pthread functions

## Common Mistakes

| Mistake | Consequence | Prevention |
|---------|-------------|------------|
| Race condition | Data corruption | Use mutex for shared data |
| Deadlock | Program hangs | Consistent lock ordering |
| Use-after-free in threads | Undefined behavior | Use `pthread_join` before free |
| Missing mutex unlock | Deadlock | Always unlock in same scope |
| Signal handler race | Undefined behavior | Use `sig_atomic_t` |

## Interview Questions

### Q1: What is the difference between a process and a thread?
**Answer:** Process: separate memory space, heavier. Thread: shared memory space, lighter. Threads within same process share memory.

### Q2: What is a race condition?
**Answer:** When two or more threads access shared data concurrently and the result depends on timing. Can cause data corruption.

### Q3: What is a deadlock?
**Answer:** When two or more threads are blocked forever, each waiting for the other to release a resource.

### Q4: What is the difference between mutex and semaphore?
**Answer:** Mutex: binary (locked/unlocked), owned by thread. Semaphore: counting, no ownership. Mutex protects critical section; semaphore signals events.

### Q5: What is the purpose of `pthread_join`?
**Answer:** Waits for a thread to finish. Ensures thread has completed before accessing its results.

### Q6: What is a condition variable?
**Answer:** A synchronization primitive that allows threads to wait until a condition becomes true. Used with mutex for producer-consumer patterns.

### Q7: What is the difference between `volatile` and atomics?
**Answer:** `volatile` prevents compiler optimization but doesn't guarantee atomicity. Atomics guarantee atomic operations and memory ordering.

### Q8: What is a thread pool?
**Answer:** A collection of pre-created threads that wait for tasks. Reduces thread creation overhead.

### Q9: What is false sharing?
**Answer:** When threads on different cores access different data on the same cache line, causing cache invalidation. Fix with padding.

### Q10: What is the difference between `pthread_mutex_lock` and `trylock`?
**Answer:** `lock` waits until mutex is available. `trylock` returns immediately if mutex is locked.

### Q11: What is a deadlock detection?
**Answer:** Detecting when threads are in a circular wait. Can be done with resource allocation graphs or timeouts.

### Q12: What is the difference between user-space and kernel-space threads?
**Answer:** User-space: managed by library, lightweight. Kernel-space: managed by OS, heavier. Pthreads are user-space.

### Q13: What is the purpose of `pthread_detach`?
**Answer:** Marks a thread as detached, so its resources are automatically released when it finishes. No need to join.

### Q14: What is the difference between `pthread_cond_broadcast` and `pthread_cond_signal`?
**Answer:** `broadcast` wakes all waiting threads. `signal` wakes one waiting thread. Use `broadcast` for state changes affecting all threads.

### Q15: What is the difference between `_Atomic` qualifier and `atomic_*` functions?
**Answer:** `_Atomic` is a type qualifier for atomic variables. `atomic_*` functions provide explicit atomic operations with memory ordering.

## Cross-References

- **Previous Module:** [08 - Memory Management](../08-memory-management/)
- **Next Module:** [10 - Networking](../10-networking/)
- **Related:** [07 - Algorithms](../07-algorithms/) — Parallel algorithms
- **Related:** [12 - Performance](../12-performance/) — Performance optimization
- **External:** [POSIX Threads Programming](https://hpc-tutorials.llc.us/posix/)
- **External:** [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
