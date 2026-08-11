# Quiz: Suppressed Exceptions

## Questions

### Q1: What does a suppressed exception represent?
**Answer:** An additional exception from cleanup (e.g., from `close()` in try-with-resources), attached alongside the primary.

### Q2: In try-with-resources, when `close()` throws and the `try` block also threw, which exception becomes the primary?
**Answer:** The `try` block exception is the primary. The `close()` exception becomes suppressed.

### Q3: How do you retrieve suppressed exceptions from a `Throwable`?
**Answer:** `getSuppressed()` returns an array of suppressed `Throwable` objects.

### Q4: What happens if you call `addSuppressed()` with the same exception as the primary?
**Answer:** It throws `IllegalArgumentException`.

### Q5: Should suppressed exceptions be used for flow control?
**Answer:** No, they are only for cleanup failures. They should never be used for flow control.

### Q6: What happens when an exception is thrown in both the try block and the finally block?
**Answer:** The try-block exception becomes the primary, and the finally-block exception becomes a suppressed exception. This preserves both errors.

### Q7: Why does try-with-resources automatically call `addSuppressed()`?
**Answer:** To ensure that exceptions thrown during resource cleanup are not lost. The cleanup exception is attached to the primary exception rather than replacing it.

### Q8: Can you manually add suppressed exceptions outside of try-with-resources?
**Answer:** Yes, by calling `addSuppressed()` on any `Throwable`. However, this is mainly useful for custom resource management.

### Q9: What is the difference between `getCause()` and `getSuppressed()`?
**Answer:** `getCause()` returns the original exception that triggered the current one (exception chaining). `getSuppressed()` returns exceptions that occurred during cleanup of the current exception.

### Q10: What happens if `close()` throws and the `try` block completed normally?
**Answer:** The `close()` exception is the primary exception — there is no exception from the try block to make primary.

### Q11: Why are suppressed exceptions important for debugging?
**Answer:** They preserve the full context of what went wrong. Without them, cleanup errors could silently mask the original problem.

### Q12: What happens when you call `getSuppressed()` on an exception with no suppressed exceptions?
**Answer:** It returns an empty array, not null.

### Q13: Can a suppressed exception itself have a cause?
**Answer:** Yes, a suppressed exception is a full `Throwable` and can have its own cause chain.

### Q14: What happens when multiple resources are closed in try-with-resources and all throw?
**Answer:** The first exception becomes primary, and the rest are added as suppressed exceptions, in order.
