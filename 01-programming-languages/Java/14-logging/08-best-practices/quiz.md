# Quiz: Logging Best Practices

## Multiple Choice

### Q1: What is the correct logger declaration?
- A) `private Logger logger = LoggerFactory.getLogger(getClass());`
- B) `public static Logger logger = LoggerFactory.getLogger(MyClass.class);`
- C) `private static final Logger logger = LoggerFactory.getLogger(MyClass.class);`
- D) `Logger logger = new Logger();`

### Q2: Why should exceptions be passed as the last argument?
- A) It looks cleaner
- B) It preserves the full stack trace
- C) It is faster
- D) Both B and C

### Q3: What should you NEVER log?
- A) User IDs
- B) Timestamps
- C) Passwords and tokens
- D) Error messages

### Q4: How should MDC be cleaned up?
- A) At the end of the method
- B) In a finally block
- C) It does not need cleanup
- D) Only when the application shuts down

### Q5: What is the recommended log level for normal business operations?
- A) DEBUG
- B) INFO
- C) WARN
- D) ERROR

### Q6: When should you guard a log statement with `isDebugEnabled()`?
- A) Before every log statement
- B) Only before expensive operations
- C) Never
- D) Only in production code

### Q7: What is the correct way to log an exception with context?
- A) `logger.error("Failed: " + e.getMessage());`
- B) `logger.error("Failed to process order {}", orderId, e);`
- C) `logger.error(e.toString());`
- D) `logger.error("Failed", e, orderId);`

## Answers

1. **C** - `private static final` with class reference is the standard
2. **D** - Preserves stack trace AND avoids string concatenation overhead
3. **C** - Passwords, tokens, PII should never appear in logs
4. **B** - Always use try/finally to ensure MDC is cleared
5. **B** - INFO is for normal business operations
6. **B** - Only guard expensive operations; simple messages do not need guards
7. **B** - Context first, exception as last argument for stack trace
