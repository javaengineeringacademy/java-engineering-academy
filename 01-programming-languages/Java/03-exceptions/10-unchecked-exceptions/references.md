# References

## Official Documentation
- [JDK 21 — RuntimeException](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/RuntimeException.html) — Javadoc for RuntimeException
- [JDK 21 — Error](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Error.html) — Javadoc for Error
- [JDK 21 — Throwable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html) — Parent class
- [Java Tutorials — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/) — Official exception overview

## Official Source Code
- [OpenJDK 21 — RuntimeException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/RuntimeException.java) — RuntimeException source
- [OpenJDK 21 — NullPointerException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/NullPointerException.java) — NPE source
- [OpenJDK 21 — IllegalArgumentException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/IllegalArgumentException.java) — IAE source

## Language Specification
- **JLS §11.1.1**: [Compile-Time Step 2: Determine Logic Error Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.1)
- **JLS §11.2**: [Compile-Time Checking of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2)
- **JVM Spec §2.10**: [Exceptions](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.10)

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | RuntimeException and Error subclasses introduced |
| JDK 1.4 | Chained exceptions added |
| JDK 5 | Enums and autoboxing reduced some unchecked exceptions |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 69: Use exceptions only for exceptional conditions
- **Clean Code** — Robert C. Martin, Chapter 7: Error Handling
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
