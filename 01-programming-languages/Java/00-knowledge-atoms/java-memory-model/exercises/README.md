# Java Memory Model Exercises

## Exercise 1: Visibility Fix
Given a broken visibility example, fix it using the appropriate synchronization mechanism.

**Requirements:**
- Fix the visibility issue in the provided code
- Use the most appropriate mechanism (volatile, synchronized, or AtomicReference)
- Explain why your solution works

## Exercise 2: Thread-Safe Counter
Implement a thread-safe counter using three different approaches.

**Requirements:**
- Implement using `synchronized`
- Implement using `volatile` (if possible, explain limitations)
- Implement using `AtomicInteger`
- Compare the approaches

## Exercise 3: Happens-Before Chain
Create a scenario that demonstrates a chain of happens-before relationships.

**Requirements:**
- Use at least 3 different happens-before rules
- Print clear output showing each rule in action
- Verify correctness with assertions

## Guidelines

1. Use `Thread.sleep()` carefully (handle InterruptedException)
2. Use `Thread.join()` to wait for thread completion
3. Print thread name with `Thread.currentThread().getName()` for clarity
4. Use `CountDownLatch` or `CyclicBarrier` for thread coordination

## Expected Output Format

```
=== Exercise Name ===
Thread-1: action
Thread-2: action
Result: value
Expected: value
```
