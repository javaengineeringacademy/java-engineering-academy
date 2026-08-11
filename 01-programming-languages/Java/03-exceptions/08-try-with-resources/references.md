# References

## Official Documentation
- [JLS §14.20.3](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.3) — Try-with-Resources specification
- [JDK 21 — AutoCloseable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AutoCloseable.html) — AutoCloseable interface
- [JDK 21 — Closeable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/Closeable.html) — Closeable interface
- [Java Tutorials — Try-with-Resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) — Official tutorial

## Official Source Code
- [OpenJDK 21 — AutoCloseable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/AutoCloseable.java) — AutoCloseable interface source
- [OpenJDK 21 — Closeable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/io/Closeable.java) — Closeable interface source

## Language Specification
- **JLS §14.20.3**: [try-with-resources](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.3)
- **JVM Spec §3.12**: [try Statement](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-3.html#jvms-3.12)

## Version History

| Version | Change |
|---------|--------|
| JDK 5 | `AutoCloseable` and `Closeable` interfaces introduced |
| JDK 7 | Try-with-resources syntax introduced (JSR 334 / Project Coin) |
| JDK 9 | Effectively final variables allowed in try-with-resources |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 9: Use try-with-resources
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
- **Java Concurrency in Practice** — Brian Goetz, Chapter 7: Cancellation and Shutdown
- **Baeldung** — [Java Try With Resources](https://www.baeldung.com/java-try-with-resources) — Practical tutorial

## Related Topics
- [09-finally](../09-finally/references.md) — Finally block references
