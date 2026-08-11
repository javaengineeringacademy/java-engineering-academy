# References

## Official Documentation
- [JDK 21 — Throwable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html) — Root class of the exception hierarchy
- [JDK 21 — StackTraceElement](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StackTraceElement.html) — Represents an element in a stack trace
- [Java Tutorials — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/) — Official introduction to exception handling
- [Java Tutorials — The try Statement](https://docs.oracle.com/javase/tutorial/essential/exceptions/finally.html) — try-finally and try-catch semantics

## Official Source Code
- [OpenJDK 21 — Throwable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java) — Full source of the Throwable class
- [OpenJDK 21 — StackTraceElement.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/StackTraceElement.java) — Stack trace element source
- [OpenJDK 21 — Error.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Error.java) — Error subclass source

## Language Specification
- **JLS §11.1.2**: [Compile-Time Step 2: Determine Logic Error Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.2)
- **JLS §11.5**: [The Exception Hierarchy](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.5)
- **JVM Spec §2.10**: [Exceptions](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.10) — How the JVM handles throwables

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | Throwable introduced as root of exception hierarchy |
| JDK 1.2 | `initCause()` added for cause chaining |
| JDK 1.4 | `fillInStackTrace()` made public; `getStackTrace()` / `setStackTrace()` added |
| JDK 7 | `addSuppressed()` / `getSuppressed()` added for try-with-resources |
| JDK 21 | Current state — no changes to Throwable |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 75: Prefer exceptions to error codes
- **Effective Java (3rd Ed)** — Item 79: Avoid unnecessary use of checked exceptions
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
