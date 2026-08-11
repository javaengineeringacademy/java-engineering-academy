# Quiz

## Questions

1. What does a suppressed exception represent?
   - A) The original cause of a failure
   - B) An additional exception from cleanup
   - C) A caught and rethrown exception
   - D) An exception from a different thread

2. In try-with-resources, when `close()` throws and the `try` block also
   threw, which exception becomes the primary?
   - A) The `close()` exception
   - B) The `try` block exception
   - C) Both are primary
   - D) A new exception is created

3. How do you retrieve suppressed exceptions from a `Throwable`?
   - A) `getCause()`
   - B) `getSuppressed()`
   - C) `getSuppressedExceptions()`
   - D) `suppressedExceptions()`

4. What happens if you call `addSuppressed()` with the same exception
   as the primary?
   - A) It is silently ignored
   - B) It throws `IllegalArgumentException`
   - C) It replaces the primary
   - D) It adds it as a cause

5. Should suppressed exceptions be used for flow control?
   - A) Yes, when you need to propagate multiple values
   - B) No, they are only for cleanup failures
   - C) Yes, as an alternative to returning multiple values
   - D) Only in performance-critical code

## Answers

1. B — A suppressed exception is an additional exception that occurred
   during cleanup, attached alongside the primary.
2. B — The `try` block exception is the primary. The `close()` exception
   becomes suppressed.
3. B — `getSuppressed()` returns an array of suppressed `Throwable` objects.
4. B — Calling `addSuppressed()` with the primary throws
   `IllegalArgumentException`.
5. B — Suppressed exceptions are strictly for preserving cleanup failures.
   They should never be used for flow control.
