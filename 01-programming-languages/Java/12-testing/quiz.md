# Debugging Quiz

## Question 1
What is the purpose of a stack trace in exception handling?
- A) To fix the exception automatically
- B) To show the sequence of method calls that led to the exception
- C) To log all variable values
- D) To restart the application

**Answer: B**
**Explanation:** A stack trace shows the call hierarchy from the point of exception origin back to the main method, helping developers trace the execution path and identify the root cause.

## Question 2
What is a memory leak in Java?
- A) When the JVM runs out of physical RAM
- B) When objects are no longer needed but are still referenced, preventing garbage collection
- C) When a thread is stuck in an infinite loop
- D) When a file handle is not closed

**Answer: B**
**Explanation:** A memory leak occurs when objects are unintentionally kept referenced (e.g., in static collections, unclosed resources), so the garbage collector cannot reclaim their memory even though they're no longer needed.

## Question 3
What is a deadlock in multithreaded programming?
- A) When a thread throws an exception
- B) When two or more threads are each waiting for the other to release a lock, so none can proceed
- C) When a thread runs too slowly
- D) When a thread is interrupted

**Answer: B**
**Explanation:** A deadlock occurs when Thread A holds Lock1 and waits for Lock2, while Thread B holds Lock2 and waits for Lock1. Both threads are stuck waiting forever, creating a circular dependency.

## Question 4
Which JVM flag enables remote debugging on port 5005?
- A) `-XX:+RemoteDebug=5005`
- B) `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005`
- C) `-Ddebug.port=5005`
- D) `-Xdebug:remote:5005`

**Answer: B**
**Explanation:** The `-agentlib:jdwp` flag loads the Java Debug Wire Protocol agent, which allows an external debugger to connect to the specified port for remote debugging.

## Question 5
What is the difference between `System.out.println` debugging and using a proper logging framework?
- A) `println` is faster
- B) Logging frameworks provide configurable log levels, structured output, and can be disabled without code changes
- C) `println` provides more information
- D) There is no difference

**Answer: B**
**Explanation:** Logging frameworks like SLF4J/Logback offer log levels (DEBUG, INFO, WARN, ERROR), structured formats, file rotation, and can be configured externally. `println` is hardcoded and lacks these features.