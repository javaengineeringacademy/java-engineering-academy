# Thread Creation - Internals

## Thread Creation Internals

### Extending Thread

When you extend `Thread`:

1. The subclass overrides `run()` to define the task
2. `new MyThread()` allocates the Thread object
3. `start()` triggers native thread creation
4. The OS thread calls `run()` on the new thread's stack

The `start0()` native method:
- Allocates native thread resources (stack, TCB)
- Registers the thread with the JVM
- Begins execution at `run()` method

### Implementing Runnable

When using `Runnable`:

1. `new Thread(runnable)` stores the Runnable reference
2. `Thread.run()` checks if `target != null` (the Runnable)
3. If target exists, calls `target.run()`
4. This is the composition pattern (favor over inheritance)

### Callable + Future Internals

When using `Callable`:

1. `ExecutorService.submit(callable)` wraps it in a `FutureTask`
2. `FutureTask` implements both `Runnable` and `Future`
3. The task is submitted to the thread pool
4. `call()` is executed, result stored in `FutureTask` state
5. `Future.get()` blocks until result is available

### Virtual Thread Internals (Java 21+)

Virtual threads use a fundamentally different model:

1. `Thread.ofVirtual().start(runnable)` creates a virtual thread
2. The JVM allocates a lightweight structure (no OS thread)
3. The virtual thread is mounted onto a carrier (platform) thread
4. When a virtual thread blocks (I/O), it is unmounted
5. The carrier thread is freed to run other virtual threads
6. When I/O completes, the virtual thread is remounted

This is called "structured concurrency" and allows millions of concurrent tasks.
