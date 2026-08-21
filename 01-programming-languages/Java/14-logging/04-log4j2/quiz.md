# Quiz: Log4j 2

## Multiple Choice

### Q1: What makes Log4j 2 faster than Logback for async logging?
- A) Better JVM optimization
- B) LMAX Disruptor instead of ArrayBlockingQueue
- C) More efficient string formatting
- D) Compiled log statements

### Q2: What is "garbage-free logging" in Log4j 2?
- A) Logs to /dev/null
- B) No string concatenation in log messages
- C) Minimizes object allocation during logging
- D) Uses compressed log files

### Q3: How do you create a Logger in Log4j 2?
- A) `LoggerFactory.getLogger(MyClass.class)`
- B) `LogManager.getLogger(MyClass.class)`
- C) `new Logger(MyClass.class)`
- D) `Logger.getInstance(MyClass.class)`

### Q4: What is the default behavior of JNDI in Log4j 2 (post-2.17.0)?
- A) Enabled for all lookups
- B) Disabled by default
- C) Only allows localhost
- D) Requires authentication

### Q5: Which async configuration provides the best performance?
- A) `<Async>` appender
- B) `<AsyncLogger>` at logger level
- C) Both perform identically
- D) Neither, synchronous is faster

### Q6: What does `status="WARN"` in Configuration do?
- A) Sets root logger to WARN
- B) Configures Log4j internal status logging level
- C) Enables warning filters
- D) Sets appender threshold to WARN

### Q7: Which layout is best for machine-parsed logs?
- A) PatternLayout
- B) JsonLayout
- C) HTMLLayout
- D) SimpleLayout

## Answers

1. **B** - Disruptor is a lock-free ring buffer, much faster than blocking queues
2. **C** - Garbage-free means minimizing or eliminating object allocation during logging
3. **B** - `LogManager.getLogger()` is the Log4j 2 way (similar to SLF4J's `LoggerFactory`)
4. **B** - JNDI is disabled by default since Log4Shell vulnerability
5. **B** - AsyncLogger at logger level avoids the extra thread hop of AsyncAppender
6. **B** - `status` controls Log4j 2's own internal logging (not application logs)
7. **B** - JsonLayout produces structured, machine-parseable JSON output
