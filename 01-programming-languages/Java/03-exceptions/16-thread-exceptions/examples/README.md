# Examples: Thread Exceptions in Java

Each example includes the source code, expected output, and an explanation of the mechanism being demonstrated.

---

## Example 1: UncaughtExceptionHandler

```java
public class UncaughtHandler {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("Uncaught in " + thread.getName() + ": " + throwable.getMessage());
        });

        Thread t = new Thread(() -> {
            throw new RuntimeException("Thread failure");
        }, "worker-1");

        t.start();
        try { t.join(); } catch (InterruptedException e) { /* ignored */ }
    }
}
```

**Output:**
```
Uncaught in worker-1: Thread failure
```

**Explanation:** `UncaughtExceptionHandler` catches exceptions that are not handled in a thread's `run()` method. `setDefaultUncaughtExceptionHandler` sets a global handler. The handler receives both the thread and the exception.

---

## Example 2: Per-Thread Handler

```java
public class PerThreadHandler {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            throw new RuntimeException("Task 1 failed");
        }, "task-1");

        Thread t2 = new Thread(() -> {
            throw new RuntimeException("Task 2 failed");
        }, "task-2");

        t1.setUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("Handler A: " + thread.getName() + " - " + throwable.getMessage());
        });

        t2.setUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("Handler B: " + thread.getName() + " - " + throwable.getMessage());
        });

        t1.start();
        t2.start();
        try { t1.join(); t2.join(); } catch (InterruptedException e) { /* ignored */ }
    }
}
```

**Output:**
```
Handler A: task-1 - Task 1 failed
Handler B: task-2 - Task 2 failed
```

**Explanation:** Each thread can have its own `UncaughtExceptionHandler`. Per-thread handlers override the default handler. This allows different threads to handle failures differently.

---

## Example 3: ExecutorService Exception Handling

```java
import java.util.concurrent.*;

public class ExecutorExceptionHandling {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<String> future = executor.submit(() -> {
            throw new RuntimeException("Task failed");
        });

        try {
            future.get();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException: " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
    }
}
```

**Output:**
```
ExecutionException: Task failed
```

**Explanation:** When a task submitted via `Future` throws an exception, it is wrapped in `ExecutionException`. The original exception is accessible via `getCause()`. `InterruptedException` indicates the thread was interrupted while waiting.

---

## Example 4: Callable Exception Handling

```java
import java.util.concurrent.*;

public class CallableExceptionHandling {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> task = () -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Random failure");
            }
            return 42;
        };

        Future<Integer> future = executor.submit(task);
        try {
            Integer result = future.get(5, TimeUnit.SECONDS);
            System.out.println("Result: " + result);
        } catch (ExecutionException e) {
            System.out.println("Task failed: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            System.out.println("Task timed out");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
    }
}
```

**Output:**
```
Result: 42
```
or
```
Task failed: Random failure
```

**Explanation:** `Callable` tasks can throw checked exceptions. The `Future.get()` method wraps any thrown exception in `ExecutionException`. The `TimeoutException` is thrown if the task doesn't complete within the specified timeout.
