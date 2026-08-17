# Thread Lifecycle (Part 2)

[📖 Back to Part 1](README.md)

---

## Advanced Concepts

### wait/notify Pitfalls

The `wait()`/`notify()` mechanism has several common pitfalls:

```java
// PITFALL 1: Spurious Wakeups
// Always use while loop, never if
synchronized (lock) {
    while (!condition) {  // NOT: if (!condition)
        lock.wait();
    }
}

// PITFALL 2: Calling wait() without holding monitor
synchronized (lock) {
    lock.wait(); // CORRECT - monitor held
}
lock.wait(); // WRONG - IllegalMonitorStateException

// PITFALL 3: notify() instead of notifyAll()
// notify() only wakes ONE thread - use notifyAll() when
// multiple threads may be waiting for different conditions
```

### Thread State Monitoring

You can monitor thread states for debugging:

```java
// Get all thread states
Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
threads.forEach((thread, stack) -> {
    System.out.println(thread.getName() + ": " + thread.getState());
    for (StackTraceElement frame : stack) {
        System.out.println("\tat " + frame);
    }
});

// JMX monitoring
ThreadMXBean bean = ManagementFactory.getThreadMXBean();
long[] deadlockedThreads = bean.findDeadlockedThreads();
```

### ThreadGroup for Management

ThreadGroups allow managing groups of threads:

```java
ThreadGroup group = new ThreadGroup("WorkerGroup");

for (int i = 0; i < 5; i++) {
    new Thread(group, () -> {
        while (!Thread.currentThread().isInterrupted()) {
            // Do work
        }
    }, "Worker-" + i).start();
}

// Interrupt all threads in group
group.interrupt();

// List all threads in group
group.list();
```

### Custom Thread UncaughtExceptionHandler

Handle uncaught exceptions globally:

```java
Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
    System.err.println("Thread " + thread.getName() + " threw: " + throwable);
    // Log to file, alert monitoring, etc.
});

// Or per-thread
Thread t = new Thread(() -> {
    throw new RuntimeException("Oops!");
});
t.setUncaughtExceptionHandler((thread, throwable) -> {
    System.err.println("Custom handler: " + throwable);
});
t.start();
```

### Shutdown Hooks

Register cleanup code for JVM shutdown:

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    System.out.println("Shutting down...");
    // Close resources, flush buffers, etc.
}));
```

---

[📖 Back to Part 1](README.md)
