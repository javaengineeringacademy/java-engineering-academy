# Concurrency — C Language

## What it is
Concurrency allows multiple tasks to execute simultaneously or interleaved.

## Why it exists
To improve performance, responsiveness, and resource utilization.

## When to use it
When you need parallel processing, I/O overlap, or responsive applications.

## How it works

### Threads (POSIX)

```c
#include <pthread.h>

void *thread_func(void *arg) {
    printf("Thread running\n");
    return NULL;
}

int main(void) {
    pthread_t thread;
    pthread_create(&thread, NULL, thread_func, NULL);
    pthread_join(thread, NULL);
    return 0;
}
```

### Mutex

```c
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *thread_func(void *arg) {
    pthread_mutex_lock(&mutex);
    // critical section
    pthread_mutex_unlock(&mutex);
    return NULL;
}
```

### Condition Variables

```c
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

// Wait
pthread_mutex_lock(&mutex);
while (!condition)
    pthread_cond_wait(&cond, &mutex);
pthread_mutex_unlock(&mutex);

// Signal
pthread_mutex_lock(&mutex);
condition = 1;
pthread_cond_signal(&cond);
pthread_mutex_unlock(&mutex);
```

### Semaphores

```c
#include <semaphore.h>

sem_t sem;
sem_init(&sem, 0, 1);  // Initial value 1

sem_wait(&sem);    // Decrement (lock)
// critical section
sem_post(&sem);    // Increment (unlock)
```

### Atomic Operations

```c
#include <stdatomic.h>

atomic_int counter = 0;
atomic_fetch_add(&counter, 1);  // Thread-safe increment
```

## Production Incidents

### Incident 1: Race Condition in Shared State

**Problem:** A multi-threaded counter produces inconsistent counts, sometimes showing negative values.

**Cause:** Two threads increment a shared counter without synchronization:

```c
int counter = 0;

void *increment(void *arg) {
    for (int i = 0; i < 1000000; i++) {
        counter++;  // Read-modify-write is not atomic
    }
    return NULL;
}
```

**Impact:** Counter shows 1,200,000 instead of expected 2,000,000. Financial calculations produce wrong totals, triggers compliance alerts.

**Detection:** ThreadSanitizer reports data race on `counter`. Manual verification shows inconsistent counts across runs.

**Solution:** Use atomic operations or mutex:

```c
#include <stdatomic.h>
atomic_int counter = 0;

void *increment(void *arg) {
    for (int i = 0; i < 1000000; i++) {
        atomic_fetch_add(&counter, 1);
    }
    return NULL;
}
```

**Prevention:** Run with ThreadSanitizer (`-fsanitize=thread`), use atomics for simple operations, mutexes for complex ones, document shared state access patterns.

---

### Incident 2: Deadlock in Mutex Ordering

**Problem:** Two threads periodically deadlock, freezing the application entirely.

**Cause:** Threads acquire two locks in opposite orders:

```c
// Thread 1
pthread_mutex_lock(&lock_a);
pthread_mutex_lock(&lock_b);  // Waits for Thread 2

// Thread 2
pthread_mutex_lock(&lock_b);
pthread_mutex_lock(&lock_a);  // Waits for Thread 1
```

**Impact:** Application freezes, requires process kill. Occurs randomly under load, causing service outages lasting minutes.

**Detection:** `gdb` attach shows both threads waiting on each other's locks. strace shows `futex(FUTEX_WAIT)` on both threads.

**Solution:** Establish global lock ordering:

```c
// Always acquire locks in order: lock_a, then lock_b
// Thread 1 and Thread 2 both:
pthread_mutex_lock(&lock_a);
pthread_mutex_lock(&lock_b);
// ... critical section ...
pthread_mutex_unlock(&lock_b);
pthread_mutex_unlock(&lock_a);
```

**Prevention:** Document and enforce lock ordering, use `-fsanitize=thread` to detect potential deadlocks, minimize number of locks held simultaneously, use `trylock` with timeout and backoff.

## Production Checklist

- [ ] Use proper synchronization
- [ ] Avoid deadlocks
- [ ] Use atomic operations when possible
- [ ] Test with thread sanitizer
- [ ] Document threading model

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Creates and joins threads |
| Intermediate | Uses mutexes and condition variables |
| Advanced | Masters lock-free programming |

## Common Myths

1. **Myth**: Threads are always faster
   **Truth**: Threads add overhead; use only when beneficial

2. **Myth**: Atomic operations are always safe
   **Truth**: Complex operations may need multiple atomics or locks

## One-Minute Revision

| Concept | Description |
|---------|-------------|
| Thread | Independent execution path |
| Mutex | Mutual exclusion lock |
| Condition | Thread synchronization |
| Semaphore | Counting synchronization |
| Atomic | Thread-safe operations |
| Deadlock | Threads waiting forever |
| Race condition | Non-deterministic behavior |

## Related Topics

- [Performance](../12-performance/README.md)
- [Best Practices](../15-best-practices/README.md)
