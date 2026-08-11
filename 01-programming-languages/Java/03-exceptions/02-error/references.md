# References

## Official Documentation
- [JDK 21 — Error](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Error.html) — Javadoc for the Error class
- [JDK 21 — Throwable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html) — Parent class of Error
- [JDK 21 — OutOfMemoryError](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/OutOfMemoryError.html) — Out of memory error
- [JDK 21 — StackOverflowError](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StackOverflowError.html) — Stack overflow error

## Official Source Code
- [OpenJDK 21 — Error.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Error.java) — Error class source
- [OpenJDK 21 — OutOfMemoryError.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/OutOfMemoryError.java) — OOM error source
- [OpenJDK 21 — StackOverflowError.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/StackOverflowError.java) — SOE error source
- [OpenJDK 21 — VirtualMachineError.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/VirtualMachineError.java) — Base for JVM errors

## Language Specification
- **JLS §11.1.1**: [Errors](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.1)
- **JVM Spec §2.10**: [Exceptions](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.10)
- **JVM Spec §6.5**: [Exception Table](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5)

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | Error class introduced as a Throwable subclass |
| JDK 1.4 | Chained exceptions support added |
| JDK 7 | NoClassDefFoundError behavior clarified |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 68: Use standard exceptions where applicable
- **Java Performance: The Definitive Guide** — Scott Oaks, Chapter on JVM Error Handling
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
