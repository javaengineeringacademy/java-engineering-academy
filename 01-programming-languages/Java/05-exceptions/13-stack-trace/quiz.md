# Stack Trace Quiz

## Questions

### Q1: Array Order

What is the order of elements in `Throwable.getStackTrace()`?

- A) Oldest frame first (main at index 0)
- B) Newest frame first (most-recently-called at index 0)
- C) Arbitrary order
- D) Reverse chronological by timestamp

---

### Q2: Line Numbers

What does a line number of `-2` indicate in a `StackTraceElement`?

- A) The line was compiled with `-g:none`
- B) The method is a native (JNI) method
- C) The stack trace was suppressed
- D) The source file is missing

---

### Q3: Performance

Which approach is fastest for an exception with no diagnostic value?

- A) `new Exception()` — let the JVM fill the trace
- B) `new Exception()` followed by `setStackTrace(new StackTraceElement[0])`
- C) Override `fillInStackTrace()` to return `this`
- D) Catch `Exception` and call `printStackTrace()`

---

### Q4: Thread Stack

What does `Thread.currentThread().getStackTrace()` return?

- A) The stack trace of the last thrown exception
- B) The current call stack of the executing thread
- C) A copy of the JVM's native stack
- D) An empty array if no exception was thrown

---

### Q5: Filtering

You want to hide all `java.lang.reflect.*` frames in a log. Which is the most
reliable approach?

- A) String-replace in the log message
- B) Stream-filter `e.getStackTrace()` by package prefix before logging
- C) Set a JVM flag to exclude reflection frames
- D) Use `e.getMessage()` instead

---

### Q6: Lazy Stack Trace

A class overrides `fillInStackTrace()` to return `this`. What is the consequence?

- A) The exception always has zero frames
- B) The exception always has the full stack
- C) The exception is not throwable
- D) The JVM catches it automatically

---

### Q7: setStackTrace

After calling `exception.setStackTrace(new StackTraceElement[0])`, what does
`exception.getStackTrace()` return?

- A) The original trace
- B) An empty array
- C) `null`
- D) Throws IllegalStateException

---

### Q8: Causal Chains

Which method reveals the original exception when one is wrapped in another?

- A) `toString()`
- B) `getMessage()`
- C) `getCause()`
- D) `getStackTrace()`

---

### Q9: Native Method Frame

In a stack trace, a frame for a native method shows which line number?

- A) `-1`
- B) `-2`
- C) `0`
- D) `-10`

---

### Q10: Production Logging

For a high-throughput service throwing 10,000 exceptions per second, what is the
recommended approach?

- A) Log full stack trace for every exception
- B) Log only the exception message
- C) Sample — log full trace every Nth exception, summary for the rest
- D) Disable all exception logging

---

## Answers

| Q   | Answer | Explanation                                         |
|-----|--------|-----------------------------------------------------|
| 1   | B      | Newest frame first (top of stack at index 0)        |
| 2   | B      | `-2` = native method                                |
| 3   | C      | Override fillInStackTrace — no stack walk at all     |
| 4   | B      | Current call stack of the executing thread           |
| 5   | B      | Filter by package prefix before logging              |
| 6   | A      | Always returns the instance with no frames           |
| 7   | B      | setStackTrace replaces the trace; get returns new    |
| 8   | C      | getCause() returns the wrapped exception              |
| 9   | B      | `-2` for native methods                              |
| 10  | C      | Sample to balance diagnostics and performance        |
