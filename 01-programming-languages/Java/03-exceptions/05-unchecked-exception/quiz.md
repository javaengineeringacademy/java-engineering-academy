# Quiz: Unchecked Exceptions

## Questions

### Q1: Which of the following is an unchecked exception?
**Answer:** `NullPointerException` (extends `RuntimeException`, which is unchecked).

### Q2: Unchecked exceptions extend which class (or its subclasses)?
**Answer:** `RuntimeException` or `Error`.

### Q3: True or False: The Java compiler requires you to catch or declare unchecked exceptions in a `throws` clause.
**Answer:** False. The compiler does not enforce handling of unchecked exceptions.

### Q4: Which of these is a reason to use an unchecked exception?
**Answer:** A method received a null argument that should never be null. This is a programming error.

### Q5: What is the recommended approach when an unchecked exception occurs that indicates a programming bug?
**Answer:** Log the exception, let it propagate, and fix the underlying bug.

### Q6: Which exception is thrown when a `null` reference is dereferenced?
**Answer:** `NullPointerException`.

### Q7: True or False: `Error` subtypes are checked exceptions.
**Answer:** False. `Error` subtypes are unchecked.

### Q8: Which of the following is an anti-pattern?
**Answer:** Using exceptions for control flow instead of if/else checks. This is slow, hides bugs, and is considered an anti-pattern.

### Q9: What is the superclass of `IllegalArgumentException`?
**Answer:** `RuntimeException`.

### Q10: When should you catch an unchecked exception?
**Answer:** Only catch when you have a meaningful recovery strategy; otherwise let it propagate and fix the bug.

### Q11: What is the difference between `RuntimeException` and `Error`?
**Answer:** `RuntimeException` represents application programming bugs; `Error` represents JVM-level failures that are usually unrecoverable.

### Q12: Why is it considered an anti-pattern to catch `RuntimeException` broadly?
**Answer:** It can mask programming bugs, makes debugging harder, and prevents the bug from being fixed at its source.

### Q13: What is the performance cost of creating an unchecked exception?
**Answer:** Filling in the stack trace is expensive; in tight loops, this can cause significant performance degradation.

### Q14: Name a common scenario where you might throw an `IllegalStateException`.
**Answer:** When a method is called on an object that is not in the correct state, such as trying to use a connection that has been closed.

### Q15: How should you handle `NullPointerException` in production code?
**Answer:** Validate inputs at method boundaries, use `Objects.requireNonNull()`, and let the exception propagate to a global handler if it occurs unexpectedly.