# Introduction to Multithreading - Internals

## How Java Threads Work Internally

### Thread Object Creation

When you write `new Thread(runnable)`:

1. The JVM allocates a `Thread` object on the heap
2. The object stores:
   - Reference to the `Runnable` (or null if overriding `run()`)
   - Thread name (auto-generated or user-provided)
   - Priority (default: 5)
   - Daemon flag (default: false)
   - Thread group reference
   - Stack size configuration
3. No OS thread is created yet

### Thread.start() Internals

When `start()` is called:

1. Java checks if the thread hasn't been started (`threadStatus == 0`)
2. Throws `IllegalThreadStateException` if already started
3. Calls native `start0()` method (JNI call)
4. The native method creates an OS-level thread via:
   - Linux: `pthread_create()`
   - Windows: `CreateThread()`
   - macOS: `pthread_create()`
5. The new OS thread begins executing the `run()` method
6. The `start()` call returns immediately (doesn't wait for thread)

### Thread Stack Allocation

Each thread gets its own native stack:
- Default size: 512KB (varies by OS and JVM)
- Configurable via `new Thread(runnable, name, stackSize)`
- Larger stacks needed for deep recursion
- Smaller stacks save memory for simple tasks

### JVM Thread Management

The JVM maintains internal thread structures:
- **Thread Control Block (TCB)**: Native structure storing thread state
- **Thread Local Storage (TLS)**: Per-thread data for fast access
- **Safepoint mechanism**: Threads pause at safepoints for GC
- **Thread pools in JVM**: GC threads, Finalizer, Reference Handler

### Scheduling

- The JVM delegates scheduling to the OS
- Java priority is a hint, not a guarantee
- Most OS use preemptive scheduling with time slicing
- The `yield()` method is a hint to the scheduler (may be ignored)
