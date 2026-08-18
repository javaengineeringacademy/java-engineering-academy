# CompletableFuture - Quiz

## Multiple Choice Questions

### 1. What does `thenApply(fn)` do?
A) Applies fn asynchronously and returns CompletableFuture<CompletableFuture<T>>
B) Applies fn synchronously to the result and returns CompletableFuture<U>
C) Composes two futures
D) Blocks until the result is available

**Answer: B** - thenApply is a synchronous transformation like Stream.map.

### 2. Which method is the async equivalent of thenApply?
A) thenApplyAsync
B) thenCompose
C) thenCombine
D) supplyAsync

**Answer: A** - thenApplyAsync runs the function in the ForkJoinPool or a provided Executor.

### 3. What does `allOf(c1, c2, c3)` return?
A) The result of the first completed future
B) A CompletableFuture<Void>
C) A List of results
D) A CompletableFuture<List<T>>

**Answer: B** - allOf returns CompletableFuture<Void>; use join() on individual futures.

### 4. Which method handles both success and failure?
A) exceptionally
B) handle
C) thenApply
D) thenAccept

**Answer: B** - handle receives BiFunction<T, Throwable, U>.

### 5. What is the difference between `thenCompose` and `thenApply`?
A) No difference
B) thenCompose returns a nested future, thenApply does not
C) thenCompose is for async chains, thenApply for sync transforms
D) thenCompose blocks, thenApply does not

**Answer: C** - thenCompose flattens nested futures (like flatMap); thenApply wraps results.

## True/False

### 6. CompletableFuture.supplyAsync() runs the supplier on the calling thread.
**Answer: False** - It runs on ForkJoinPool.commonPool() by default (async).

### 7. `join()` and `get()` both block for the result, but join() does not throw checked exceptions.
**Answer: True** - join() throws CompletionException (unchecked); get() throws ExecutionException and InterruptedException.

### 8. You can chain multiple thenApply calls in a pipeline without blocking.
**Answer: True** - Each thenApply returns a new CompletableFuture that completes when the chain finishes.

## Code Output

### 9. What does this print?
```java
CompletableFuture<String> cf = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World");
System.out.println(cf.join());
```
**Answer:** `Hello World` - supplyAsync produces "Hello"; thenApply appends " World".

### 10. What is the output?
```java
CompletableFuture<Integer> cf = CompletableFuture
    .supplyAsync(() -> 10)
    .thenApply(x -> x * 2)
    .exceptionally(ex -> 0);
System.out.println(cf.join());
```
**Answer:** `20` - 10 * 2 = 20; exceptionally is not triggered.
