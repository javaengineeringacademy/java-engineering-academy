# Quiz: Logging Basics

## Multiple Choice

### Q1: What is the correct order of log levels from least to most severe?
- A) DEBUG, INFO, WARN, ERROR, FATAL, TRACE
- B) TRACE, DEBUG, INFO, WARN, ERROR, FATAL
- C) INFO, DEBUG, WARN, ERROR, FATAL, TRACE
- D) TRACE, INFO, DEBUG, WARN, ERROR, FATAL

### Q2: What is the primary purpose of SLF4J?
- A) To write log messages to files
- B) To provide a logging API facade that delegates to implementations
- C) To replace java.util.logging entirely
- D) To compress log files

### Q3: Which of the following is the correct way to log with SLF4J?
- A) `logger.debug("User " + userId + " logged in");`
- B) `logger.debug("User {} logged in", userId);`
- C) `logger.debugf("User %s logged in", userId);`
- D) `logger.log(Level.DEBUG, "User " + userId);`

### Q4: Where should you declare a Logger instance?
- A) As a local variable in each method
- B) As a `public static` field
- C) As a `private static final` field
- D) As an instance field created in the constructor

### Q5: When should you use WARN level logging?
- A) For normal application startup
- B) When an operation fails and needs immediate attention
- C) When something unexpected happens but the application can continue
- D) For detailed debugging information

### Q6: Why is parameterized logging preferred over string concatenation?
- A) It produces shorter log messages
- B) It avoids creating the concatenated string if the log level is disabled
- C) It automatically includes timestamps
- D) It encrypts sensitive data

### Q7: What is a logging bridge?
- A) A physical network device
- B) Software that routes logging API calls from one framework to another
- C) A configuration file format
- D) A log aggregation service

## Answers

1. **B** - TRACE (most verbose) < DEBUG < INFO < WARN < ERROR < FATAL (most severe)
2. **B** - SLF4J is a facade/API; the actual implementation is provided by Logback, Log4j2, etc.
3. **B** - Parameterized logging with `{}` placeholders is the SLF4J standard
4. **C** - `private static final Logger logger = LoggerFactory.getLogger(ClassName.class);`
5. **C** - WARN indicates unexpected but non-critical situations
6. **B** - String concatenation happens before the level check; parameterized avoids this
7. **B** - Bridges like jcl-over-slf4j redirect API calls between different logging frameworks
