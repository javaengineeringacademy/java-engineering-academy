# Quiz: Stack Trace

## Questions

### Q1: What is the order of elements in `Throwable.getStackTrace()`?
**Answer:** Newest frame first — the most-recently-called frame is at index 0.

### Q2: What does a line number of `-2` indicate in a `StackTraceElement`?
**Answer:** It indicates a native (JNI) method.

### Q3: Which approach is fastest for an exception with no diagnostic value?
**Answer:** Override `fillInStackTrace()` to return `this` — it avoids the stack walk entirely.

### Q4: What does `Thread.currentThread().getStackTrace()` return?
**Answer:** The current call stack of the executing thread.

### Q5: You want to hide all `java.lang.reflect.*` frames in a log. Which is the most reliable approach?
**Answer:** Stream-filter `e.getStackTrace()` by package prefix before logging.

### Q6: A class overrides `fillInStackTrace()` to return `this`. What is the consequence?
**Answer:** The exception always has zero frames — no stack trace is captured.

### Q7: After calling `exception.setStackTrace(new StackTraceElement[0])`, what does `exception.getStackTrace()` return?
**Answer:** An empty array. `setStackTrace` replaces the trace; `get` returns the new one.

### Q8: Which method reveals the original exception when one is wrapped in another?
**Answer:** `getCause()` returns the wrapped exception.

### Q9: In a stack trace, a frame for a native method shows which line number?
**Answer:** `-2` for native methods.

### Q10: For a high-throughput service throwing 10,000 exceptions per second, what is the recommended approach?
**Answer:** Sample — log full trace every Nth exception, summary for the rest. This balances diagnostics and performance.

### Q11: Why is `Throwable.getStackTrace()` ordered newest-first?
**Answer:** Because the most recent call frame is most relevant for debugging. It matches the typical top-down reading of a stack trace.

### Q12: What happens when you call `fillInStackTrace()` on an already-captured exception?
**Answer:** It re-walks the current stack, replacing the original trace. This is expensive and rarely needed.

### Q13: Why is `getCause()` more useful than `getMessage()` for wrapped exceptions?
**Answer:** `getCause()` returns the full exception object, preserving its stack trace and type. `getMessage()` only returns a string.

### Q14: Why should you avoid logging full stack traces in production for high-volume exceptions?
**Answer:** Stack trace capture is expensive and logging thousands of full traces degrades performance. Use sampling or summary logging instead.
