# References

## Official Documentation

- **JDK 21 — `StackTraceElement`**: <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StackTraceElement.html>
- **JDK 21 — `Throwable.getStackTrace()`**: <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html#getStackTrace()>
- **JDK 21 — `Throwable.fillInStackTrace()`**: <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html#fillInStackTrace()>
- **Java Tutorials — Stack Trace**: <https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrDeclare.html>

## Official Source Code

- **OpenJDK 21 — `StackTraceElement.java`**: <https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/lang/StackTraceElement.java>
- **OpenJDK 21 — `Throwable.java`** (fillInStackTrace): <https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/lang/Throwable.java>

## Language Specification

- **JLS §11.1.2**: [Compile-Time Step 2: Determine Logic Error Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.2)
- **JVM Spec §2.10**: [Exceptions](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.10)

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | `Throwable.getStackTrace()` not yet public |
| JDK 1.4 | `getStackTrace()`, `setStackTrace()`, `fillInStackTrace()` made public |
| JDK 7 | Suppressed exceptions added to stack trace output |
| JDK 14 | Helpful NullPointerExceptions (JEP 305) — enhanced NPE messages |
| JDK 21 | Virtual Threads — stack trace walking changes |

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 75: Prefer exceptions to error codes
- **Effective Java (3rd Ed)** — Item 79: Avoid unnecessary use of checked exceptions
- **Java Performance** — Scott Oaks, Chapter on Exception Performance
- **Core Java, Vol. I** — Cay S. Horstmann, Exception Handling chapter

---

## Cross-Module References

- [Logging Module](../14-logging/) — Exception logging patterns and frameworks
- [Concurrency Module](../12-concurrency/) — Thread exception handling and interruption
