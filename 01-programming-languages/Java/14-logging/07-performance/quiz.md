# Quiz: Logging Performance

## Multiple Choice

### Q1: What is the most impactful logging performance optimization?
- A) Using async logging
- B) Using parameterized logging
- C) Reducing log levels
- D) Compressing log files

### Q2: What is the approximate cost of string concatenation vs parameterized logging?
- A) Concatenation is faster
- B) They are equal
- C) Parameterized is 5-10x faster
- D) Parameterized is 100x faster

### Q3: When should you use `if (logger.isDebugEnabled())`?
- A) Before every log statement
- B) Only before expensive operations
- C) Never, it is always unnecessary
- D) Only in production code

### Q4: What does AsyncAppender do?
- A) Makes log messages asynchronous
- B) Writes logs in background thread
- C) Compresses log files asynchronously
- D) Sends logs to remote server

### Q5: What is the risk of async logging with `discardingThreshold=0`?
- A) Logs may be lost
- B) Caller thread may block
- C) Memory usage increases
- D) Both B and C

### Q6: What is a sign that logging is causing performance issues?
- A) Application uses too much memory
- B) GC pauses correlate with log writes
- C) CPU usage is low
- D) Network traffic is high

### Q7: What is the recommended queue size for AsyncAppender?
- A) 16
- B) 64
- C) 256
- D) 1024

## Answers

1. **B** - Parameterized logging avoids string allocation entirely
2. **C** - Parameterized logging is 5-10x faster than concatenation
3. **B** - Only guard expensive operations; simple messages do not need guards
4. **B** - AsyncAppender writes logs in a background thread using a queue
5. **D** - With discardingThreshold=0, the caller blocks if the queue is full
6. **B** - GC pauses correlating with log writes indicate memory pressure from logging
7. **C** - 256 is a balanced default for most applications
