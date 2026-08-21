# Quiz: Logback

## Multiple Choice

### Q1: What are the three Logback modules?
- A) logback-core, logback-classic, logback-web
- B) logback-api, logback-impl, logback-common
- C) logback-core, logback-classic, logback-access
- D) logback-base, logback-slf4j, logback-file

### Q2: What is the default Logback configuration if no logback.xml is found?
- A) Logs nothing
- B) Logs to file
- C) Logs INFO and above to console
- D) Logs DEBUG and above to console

### Q3: Which rolling policy supports both time and size-based rotation?
- A) `TimeBasedRollingPolicy`
- B) `SizeBasedRollingPolicy`
- C) `SizeAndTimeBasedRollingPolicy`
- D) `FixedWindowRollingPolicy`

### Q4: What is the purpose of AsyncAppender?
- A) To make logging asynchronous (non-blocking)
- B) To append logs to async files
- C) To compress log files
- D) To encrypt log content

### Q5: Which pattern represents the log level?
- A) `%d`
- B) `%thread`
- C) `%level`
- D) `%msg`

### Q6: What does `%d{HH:mm:ss.SSS}` produce?
- A) Date only
- B) Time with milliseconds
- C) Full date and time
- D) ISO 8601 format

### Q7: In Spring Boot, where do you configure Logback levels?
- A) logback.xml
- B) application.yml under logging.level
- C) bootstrap.yml
- D) Both A and B

## Answers

1. **C** - logback-core (foundation), logback-classic (SLF4J impl), logback-access (HTTP)
2. **C** - Default configuration logs INFO level and above to System.out
3. **C** - `SizeAndTimeBasedRollingPolicy` combines both strategies
4. **A** - AsyncAppender wraps another appender with a blocking queue for non-blocking logging
5. **C** - `%level` represents the log level (or `%-5level` for padded version)
6. **B** - Time pattern with hours:minutes:seconds.milliseconds
7. **D** - Both logback.xml and Spring Boot's application.yml can configure logging
