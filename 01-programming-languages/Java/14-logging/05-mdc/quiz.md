# Quiz: MDC

## Multiple Choice

### Q1: What does MDC stand for?
- A) Multi-Data Context
- B) Mapped Diagnostic Context
- C) Managed Data Cache
- D) Message Delivery Context

### Q2: What is the primary purpose of MDC?
- A) To store global configuration
- B) To add thread-local context to log messages
- C) To cache database queries
- D) To compress log files

### Q3: How does MDC store data?
- A) In a database
- B) In thread-local storage
- C) In a file
- D) In heap memory shared across threads

### Q4: What happens to MDC values when a thread completes?
- A) They persist in the thread pool
- B) They are garbage collected with the thread
- C) They need manual cleanup
- D) They are automatically propagated to new threads

### Q5: How do you access MDC values in a log pattern?
- A) `%m{key}`
- B) `%X{key}`
- C) `%M{key}`
- D) `%D{key}`

### Q6: What is a common MDC key for request tracing?
- A) `logId`
- B) `requestId`
- C) `traceKey`
- D) `contextId`

### Q7: How do you propagate MDC to a new thread?
- A) Values are automatically copied
- B) Use `MDC.getCopyOfContextMap()` and `MDC.setContextMap()`
- C) Use `ThreadLocal继承`
- D) MDC cannot be propagated

## Answers

1. **B** - Mapped Diagnostic Context
2. **B** - MDC adds thread-local key-value pairs to log output
3. **B** - ThreadLocal storage, unique per thread
4. **C** - MDC must be manually cleaned up (finally blocks)
5. **B** - `%X{key}` accesses MDC values in log patterns
6. **B** - `requestId` is the most common MDC key for request tracing
7. **B** - Copy the map before thread creation, restore it in the new thread
