# Solutions: Thread Exceptions in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Global Handler

```java
public class Exercise1 {
    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("Global handler: " + thread.getName() + " - " + throwable.getMessage());
        });

        Thread t = new Thread(() -> {
            throw new RuntimeException("Thread error");
        }, "worker");

        t.start();
        t.join();
    }
}
```

**Output:**
```
Global handler: worker - Thread error
```

**Key points:**
- `setDefaultUncaughtExceptionHandler` sets a global fallback.
- The handler receives the thread and the exception.
- `join()` waits for the thread to complete.

---

## Solution 2: Future Exception Handling

```java
import java.util.concurrent.*;

public class Exercise2 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            throw new RuntimeException("Task failed");
        });

        try {
            future.get();
        } catch (ExecutionException e) {
            System.out.println(e.getCause().getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
    }
}
```

**Output:**
```
Task failed
```

**Key points:**
- `ExecutionException` wraps the task's exception.
- `getCause()` retrieves the original exception.
- `InterruptedException` restores the interrupt flag.

---

## Solution 3: Thread Pool Error Handling

```java
import java.util.concurrent.*;

public class Exercise3 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 4; i++) {
            final int num = i;
            executor.submit(() -> {
                if (num == 3) throw new RuntimeException("Task 3 failed");
                System.out.println("Task " + num + " completed");
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
```

**Output:**
```
Task 1 completed
Task 2 completed
Task 4 completed
```

**Key points:**
- `submit()` swallows exceptions (unlike `execute()`).
- Other tasks continue despite one failure.
- `awaitTermination` waits for all tasks to complete.
- The pool remains healthy after a task failure.
