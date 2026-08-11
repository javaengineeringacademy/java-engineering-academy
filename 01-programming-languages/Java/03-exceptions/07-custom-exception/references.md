# References

## Official Documentation
- [JDK 21 — Exception](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Exception.html) — Javadoc for the Exception class
- [JDK 21 — RuntimeException](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/RuntimeException.html) — Javadoc for RuntimeException
- [JDK 21 — Serializable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/Serializable.html) — Serializable interface for custom exceptions

## Official Source Code
- [OpenJDK 21 — Exception.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Exception.java) — Exception class source
- [OpenJDK 21 — RuntimeException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/RuntimeException.java) — RuntimeException source
- [OpenJDK 21 — IOException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/io/IOException.java) — Example of a well-designed custom exception

## Language Specification
- **JLS §11.1**: [The Kinds of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1)
- **JLS §11.5**: [The Exception Hierarchy](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.5)

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | Exception hierarchy established for custom exceptions |
| JDK 1.4 | `initCause()` added for chaining custom exceptions |
| JDK 7 | `@SafeVarargs` annotation available for generic exception factories |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 72: Favor the use of standard exceptions
- **Effective Java (3rd Ed)** — Item 73: Throw exceptions appropriate to abstractions
- **Effective Java (3rd Ed)** — Item 74: Document all exceptions thrown by each method
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
- **Baeldung** — [Java Custom Exceptions](https://www.baeldung.com/java-custom-exceptions) — Tutorial on designing custom exceptions
