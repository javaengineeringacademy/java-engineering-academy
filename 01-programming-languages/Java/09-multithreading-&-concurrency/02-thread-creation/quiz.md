# Thread Creation — Quiz

## Question 1

What is the difference between Runnable and Callable?

- A) They are the same
- B) Callable returns a value and can throw checked exceptions
- C) Runnable returns a value
- D) Callable cannot throw exceptions

**Answer: B**
Callable has a `call()` method that returns a value and can throw checked exceptions. Runnable's `run()` returns void.

## Question 2

What does `executor.submit(callable)` return?

- A) The result directly
- B) A Future object
- C) Nothing
- D) An ExecutorService

**Answer: B**
`submit()` returns a Future that can be used to retrieve the result or check for exceptions.

## Question 3

Which of the following is NOT a valid way to create a thread in Java?

- A) Extending `Thread`
- B) Implementing `Runnable`
- C) Implementing `Callable`
- D) Using `CompletableFuture`

**Answer: D**
`CompletableFuture` is not a thread creation mechanism. `Callable` can be submitted to an ExecutorService which creates threads internally. Extending `Thread` and implementing `Runnable` are direct creation methods.

## Question 4

What is the advantage of implementing `Runnable` over extending `Thread`?

- A) Runnable is faster
- B) Java allows extending only one class — using Runnable preserves inheritance
- C) Runnable automatically starts a new thread
- D) Runnable can throw exceptions

**Answer: B**
Java has single inheritance. Implementing `Runnable` (or `Callable`) allows the class to extend another class while still being executable as a thread task.

## Question 5

What is the output?

```java
Runnable r = () -> System.out.print("Lambda ");
Thread t = new Thread(r);
t.start();
t.join();
System.out.print("Done");
```

**Answer:** `Lambda Done`
The lambda runs in the new thread. `join()` ensures the main thread waits for completion.

## Question 6

True or False: A lambda expression can implement a functional interface with multiple abstract methods.

**Answer: False**
A lambda can only implement a functional interface — one with exactly one abstract method. Interfaces with multiple methods must use anonymous classes.

## Question 7

What does `ExecutorService.submit(Runnable)` return?

- A) The result of the task
- B) A `Future<?>` that completes when the task finishes
- C) `null`
- D) An `ExecutionException`

**Answer: B**
It returns a `Future<Void>` (effectively `Future<?>`) whose `get()` method returns `null` once the task completes, or throws if the task failed.

## Question 8

What happens if you create a `Callable` that throws a checked exception?

- A) The exception is swallowed
- B) The `Future.get()` wraps it in an `ExecutionException`
- C) The compiler rejects it
- D) The exception becomes a `RuntimeException`

**Answer: B**
Checked exceptions thrown by `Callable.call()` are wrapped in `ExecutionException` when retrieved via `Future.get()`. The compiler allows it because `Callable.call()` declares `throws Exception`.

## Question 9

Which pattern is preferred for creating short-lived tasks?

- A) Extending `Thread`
- B) Lambda expressions implementing `Runnable` or `Callable`
- C) Anonymous inner classes
- D) `ForkJoinTask`

**Answer: B**
Lambdas are concise and idiomatic for short tasks. They are compiled to private methods in the enclosing class and have no overhead compared to anonymous classes.

## Question 10

What does `Thread.ofVirtual().start(runnable)` create?

- A) A daemon thread
- B) A virtual thread with a new task
- C) A thread with maximum priority
- D) A platform thread

**Answer: B**
`Thread.ofVirtual().start()` creates a virtual thread (Java 21+). Virtual threads are lightweight and managed by the JVM, not the OS.
