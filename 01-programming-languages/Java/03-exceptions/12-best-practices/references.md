# Exception Best Practices — References

## Official Documentation
- [Oracle: Exceptions — The Java Tutorials](https://docs.oracle.com/javase/tutorial/essential/exceptions/) — official Java exception handling guide
- [Oracle: Catching and Handling Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/catch.html) — catch block best practices
- [JLS §11.2 — Compile-Time Checking of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2)

## Official Source Code
- [OpenJDK — Throwable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java)
- [OpenJDK — Exception.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Exception.java)

## Style & Guidelines
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) — exception handling conventions
- [Google Guava](https://github.com/google/guava) — Preconditions and exception utilities

## Production Patterns
- [Spring Boot Exception Handling](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-web-applications.spring-mvc.error-handling) — `@ControllerAdvice`, `@ExceptionHandler`
- [Resilience4j](https://resilience4j.readme.io/) — circuit breaker, retry, fallback patterns

## Version History
| Version | Change |
|---------|--------|
| Java 1.0 | Checked exceptions enforced at compile time |
| Java 5 | Generics improved exception type safety |
| Java 7 | Try-with-resources reduced boilerplate |
| Java 9 | `addSuppressed` enhancements for suppressed exceptions |

## Recommended Reading
- **Effective Java (3rd Edition)**, Joshua Bloch — Items 69–77: exception best practices
- **Clean Code**, Robert C. Martin — Chapter 7: Error Handling
- "Error Handling Patterns in Java" — Martin Fowler
- "A Brief History of Exception Handling" — Neal Gafter
- "Checked Exceptions are a Mistake" — Bruce Eckel
