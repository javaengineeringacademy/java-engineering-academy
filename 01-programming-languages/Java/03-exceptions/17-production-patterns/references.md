# References

## Official Documentation
- [Spring Boot — Error Handling](https://spring.io/guides/gs/rest-service-errors) — Official Spring guide
- [Resilience4j Documentation](https://resilience4j.readme.io/) — Resilience library
- [SLF4J MDC Manual](https://www.slf4j.org/manual.html#mdc) — Diagnostic context for logging
- [Jakarta EE — Exception Handling](https://jakarta.ee/specifications/platform/10/apidocs/) — Jakarta exception APIs

## Official Source Code
- [Spring Framework — ResponseStatusException](https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/web/server/ResponseStatusException.java) — Spring exception source
- [Resilience4j — CircuitBreaker](https://github.com/resilience4j/resilience4j/blob/master/resilience4j-circuitbreaker/src/main/java/io/github/resilience4j/circuitbreaker/) — Circuit breaker source

## Standards
- **RFC 7807**: [Problem Details for HTTP APIs](https://www.rfc-editor.org/rfc/rfc7807) — Error response format
- **HTTP/1.1 Status Code Registry**: [IANA](https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml)

## Version History

| Version | Change |
|---------|--------|
| Spring 2.x | `@ControllerAdvice` introduced |
| Spring 5.x | `ResponseStatusException` added |
| Resilience4j 1.x | Circuit breaker pattern implemented |
| Java EE / Jakarta EE 9 | Namespace migration |

## Recommended Reading
- **Spring in Action** — Craig Walls, Chapter on REST and error handling
- **Release It!** — Michael T. Nygard, Chapter on Circuit Breaker pattern
- **Designing Data-Intensive Applications** — Martin Kleppmann, Chapter on fault tolerance
- **Baeldung** — [Exception Handling in Spring MVC](https://www.baeldung.com/exception-handling-for-rest-with-spring) — Practical guide

---

## Cross-Module References

- [I/O Module](../06-io/) — Exception handling in file and stream operations
- [JDBC Module](../10-jdbc/) — SQLException handling in database operations
- [Concurrency Module](../12-concurrency/) — Thread exception handling and interruption
- [Logging Module](../14-logging/) — Exception logging patterns and frameworks
