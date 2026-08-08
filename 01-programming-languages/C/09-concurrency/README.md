# Concurrency — C Language

## The Problem

Single-threaded programs cannot utilize multiple CPU cores and cannot handle I/O while processing. A web server that processes requests sequentially can only handle one client at a time — useless for any real workload. Concurrency lets you:

- Utilize multiple CPU cores for parallel computation
- Overlap I/O with processing (read network while computing)
- Keep applications responsive (UI doesn't freeze during computation)
- Improve throughput (handle thousands of connections simultaneously)

But concurrency introduces new classes of bugs: race conditions, deadlocks, and data corruption that are impossible in single-threaded code.

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
