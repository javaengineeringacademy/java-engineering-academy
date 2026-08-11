# Quiz: Production Exception Patterns

## Questions

**1. What annotation creates a centralized exception handler in Spring Boot?**
- A) `@ExceptionHandler`
- B) `@RestControllerAdvice`
- C) `@ControllerException`
- D) `@GlobalHandler`

**2. Which HTTP status code indicates a resource was not found?**
- A) 400
- B) 409
- C) 404
- D) 500

**3. What is the primary purpose of a circuit breaker?**
- A) Log all exceptions
- B) Prevent cascading failures
- C) Retry failed requests
- D) Validate input data

**4. Which exception should NOT be retried?**
- A) `SocketTimeoutException`
- B) `ValidationException`
- C) `HttpServerErrorException`
- D) `DatabaseConnectionException`

**5. What field should every structured error response include?**
- A) stackTrace
- B) traceId
- C) userId
- D) password

**6. What is the correct logging approach for exceptions?**
- A) `log.error(ex.getMessage())`
- B) `System.out.println(ex)`
- C) `log.error("message", ex)`
- D) `log.error(ex.toString())`

**7. Which tool provides distributed tracing for exception monitoring?**
- A) Log4j
- B) DataDog
- C) JUnit
- D) Maven

**8. What does the circuit breaker HALF_OPEN state indicate?**
- A) Service is fully operational
- B) Service is testing recovery
- C) Service is completely down
- D) No requests are allowed

**9. What is graceful degradation?**
- A) Throwing more exceptions
- B) Crashing immediately on failure
- C) Providing reduced functionality when a dependency fails
- D) Retrying indefinitely

**10. Why are correlation IDs important?**
- A) They encrypt error messages
- B) They trace requests across multiple services
- C) They replace logging
- D) They prevent exceptions

---

## Answers

1. **B** - `@RestControllerAdvice` combines `@ControllerAdvice` and `@ResponseBody`
2. **C** - 404 Not Found is the standard HTTP status for missing resources
3. **B** - Circuit breakers stop requests to failing dependencies to prevent cascading failures
4. **B** - `ValidationException` indicates invalid input; retrying won't fix it
5. **B** - `traceId` enables correlating logs across distributed systems
6. **C** - Pass exception as last argument to preserve stack trace
7. **B** - DataDog provides distributed tracing and exception monitoring
8. **B** - HALF_OPEN means the circuit breaker is allowing test requests to check recovery
9. **C** - Graceful degradation means providing partial functionality instead of failing completely
10. **B** - Correlation IDs link related log entries across microservices for debugging
