# References

## Official Documentation
- [JDK 21 — Exception](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Exception.html) — Javadoc for the Exception class
- [JDK 21 — Throwable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html) — Parent class of Exception
- [Java Tutorials — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/) — Official introduction to exception handling
- [Java Tutorials — Try-with-Resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) — Resource management with exceptions

## Official Source Code
- [OpenJDK 21 — Exception.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Exception.java) — Exception class source
- [OpenJDK 21 — Throwable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java) — Throwable parent class source

## Language Specification
- **JLS §11.1**: [The Kinds of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1)
- **JLS §11.2**: [Compile-Time Checking of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2)
- **JLS §11.3**: [Run-Time Handling of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.3)
- **JLS §11.5**: [The Exception Hierarchy](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.5)
- **JVM Spec §6.5**: [The athrow Instruction](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.athrow)

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | Exception class introduced as a Throwable subclass |
| JDK 1.4 | Chained exceptions support added via `initCause()` |
| JDK 7 | `getSuppressed()` / `addSuppressed()` added |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 58: Use checked exceptions for recoverable conditions
- **Effective Java (3rd Ed)** — Item 69: Use exceptions only for exceptional conditions
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
