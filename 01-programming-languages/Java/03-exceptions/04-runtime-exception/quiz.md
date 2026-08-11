# Quiz: RuntimeException

## Questions

### Q1: What class does `RuntimeException` extend?
**Answer:** `Exception`.

### Q2: Is `RuntimeException` checked or unchecked?
**Answer:** Unchecked.

### Q3: Does the compiler require you to catch `RuntimeException`?
**Answer:** No.

### Q4: What does a `RuntimeException` typically represent?
**Answer:** A programming bug.

### Q5: Name the exception thrown when you call a method on a `null` reference.
**Answer:** `NullPointerException`.

### Q6: Name the exception thrown when you access an array with an index that is too large.
**Answer:** `ArrayIndexOutOfBoundsException`.

### Q7: What exception should you throw when a method receives a null argument and null is not allowed?
**Answer:** `IllegalArgumentException`.

### Q8: What exception should you throw when a method is called on an object that is not in the correct state?
**Answer:** `IllegalStateException`.

### Q9: What is the difference between `IllegalArgumentException` and `IllegalStateException`?
**Answer:** `IllegalArgumentException` is for bad arguments; `IllegalStateException` is for bad object state.

### Q10: Name the exception thrown when you try to cast an object to an incompatible type.
**Answer:** `ClassCastException`.

### Q11: What exception is thrown when a `for` loop modifies a list while iterating over it?
**Answer:** `ConcurrentModificationException`.

### Q12: Why should you not use `RuntimeException` for control flow?
**Answer:** It hides bugs, uses exceptions for logic, and is slower than explicit checks.

### Q13: What is a better alternative to catching `RuntimeException` broadly?
**Answer:** Catch specific subtypes, or let the exception propagate to a global handler.

### Q14: Is `StackOverflowError` a `RuntimeException`?
**Answer:** No, it is an `Error`, which extends `Throwable` directly (not `Exception`).

### Q15: What is the recommended way to handle `RuntimeException` in a production application?
**Answer:** Register a global `UncaughtExceptionHandler` to log and report the exception.