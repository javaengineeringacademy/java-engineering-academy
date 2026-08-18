# Scheduled Executor - Quiz

## Multiple Choice Questions

### 1. Which method executes a task once after a delay?
A) scheduleAtFixedRate
B) scheduleWithFixedDelay
C) schedule
D) execute

**Answer: C** - schedule() runs a task once after the specified delay.

### 2. In `scheduleAtFixedRate`, what is the "period" measured from?
A) End of previous execution
B) Start of previous execution
C) When the executor was created
D) When the method was called

**Answer: B** - Period is measured from the start of each execution.

### 3. What happens if a scheduled task throws an exception?
A) The executor shuts down
B) The task is rescheduled automatically
C) The task is cancelled and not rescheduled
D) Another thread takes over

**Answer: C** - An unhandled exception cancels the periodic task permanently.

### 4. How do you create a ScheduledExecutorService with 2 threads?
A) Executors.newScheduledThreadPool(2)
B) Executors.newFixedThreadPool(2)
C) new ScheduledThreadPoolExecutor(2)
D) Both A and C

**Answer: D** - Both create a ScheduledThreadPoolExecutor with 2 core threads.

### 5. Which is better for a "run every 5 seconds after completion" pattern?
A) scheduleAtFixedRate(task, 0, 5, SECONDS)
B) scheduleWithFixedDelay(task, 0, 5, SECONDS)
C) Either works identically
D) Neither; use Thread.sleep()

**Answer: B** - scheduleWithFixedDelay measures delay from end to start.

## True/False

### 6. `scheduleAtFixedRate` can cause overlapping executions if a task takes longer than the period.
**Answer: True** - The next execution starts even if the previous one hasn't finished.

### 7. ScheduledExecutorService uses System.currentTimeMillis() for scheduling.
**Answer: False** - It uses System.nanoTime() for more accurate timing.

### 8. You can cancel a scheduled task using the returned ScheduledFuture.
**Answer: True** - scheduledFuture.cancel(false/true) stops future executions.

## Code Output

### 9. What does this print?
```java
ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
s.schedule(() -> System.out.print("Done"), 1, TimeUnit.SECONDS);
s.shutdown();
s.awaitTermination(3, TimeUnit.SECONDS);
```
**Answer:** `Done` (after ~1 second delay)

### 10. What is the output?
```java
ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
int[] count = {0};
ScheduledFuture<?> f = s.scheduleAtFixedRate(
    () -> { count[0]++; System.out.print(count[0]); },
    0, 100, TimeUnit.MILLISECONDS);
Thread.sleep(350);
f.cancel(false);
s.shutdown();
s.awaitTermination(1, TimeUnit.SECONDS);
```
**Answer:** `123` (or `1234` depending on timing) - Counter increments approximately every 100ms.
