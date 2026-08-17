# Exercises: Thread Exceptions in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Global Handler

### Problem

Set a default `UncaughtExceptionHandler` that prints `"Global handler: <thread> - <message>"`. Create a thread that throws an exception and verify the handler catches it.

### Starter Code

```java
public class Exercise1 {
    public static void main(String[] args) throws InterruptedException {
        // TODO: Set default UncaughtExceptionHandler
        // TODO: Create thread that throws RuntimeException
        // TODO: Start thread and join
    }
}
```

### Expected Output

```
Global handler: worker - Thread error
```

### Hints

1. Use `Thread.setDefaultUncaughtExceptionHandler()`.
2. The handler receives `(thread, throwable)`.
3. Print the thread name and exception message.
4. Join the thread to wait for completion.

---

## Exercise 2: Future Exception Handling

### Problem

Submit a `Callable` that throws an exception. Catch the `ExecutionException` and print the original exception message.

### Starter Code

```java
import java.util.concurrent.*;

public class Exercise2 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            throw new RuntimeException("Task failed");
        });

        // TODO: Call future.get(), catch ExecutionException, print cause message

        executor.shutdown();
    }
}
```

### Expected Output

```
Task failed
```

### Hints

1. Call `future.get()` in a try block.
2. Catch `ExecutionException`.
3. Print `e.getCause().getMessage()`.
4. Also catch `InterruptedException` and restore the interrupt flag.

---

## Exercise 3: Thread Pool Error Handling

### Problem

Create a thread pool with 2 threads. Submit 4 tasks, each printing `"Task <n> completed"`. One task throws an exception. Ensure the pool continues processing remaining tasks.

### Starter Code

```java
import java.util.concurrent.*;

public class Exercise3 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 4; i++) {
            final int num = i;
            executor.submit(() -> {
                if (num == 3) throw new RuntimeException("Task 3 failed");
                System.out.println("Task " + num + " completed");
            });
        }

        // TODO: Shutdown executor and await termination
    }
}
```

### Expected Output

```
Task 1 completed
Task 2 completed
Task 4 completed
```

### Hints

1. Use `executor.shutdown()` and `executor.awaitTermination()`.
2. The failed task's exception is silently swallowed by `submit()`.
3. Other tasks continue to execute.
4. The pool processes all submitted tasks.
