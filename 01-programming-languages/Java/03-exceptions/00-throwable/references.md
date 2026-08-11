# References

## Official Documentation

- [Throwable (Java SE 21 & JDK 21)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html) — Official Javadoc
- [StackTraceElement (Java SE 21)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StackTraceElement.html) — Stack trace element class
- [The Java Language Specification, §11.1.2](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.2) — Compile-Time Step 2: Determine Logic Error Types

## OpenJDK Source

- [OpenJDK Throwable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java) — OpenJDK source
- [OpenJDK StackTraceElement.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/StackTraceElement.java) — Stack trace element source
- [OpenJDK Exceptions.java (native)](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java) — `fillInStackTrace()` native method

## Key JDK Versions

| Version | Change |
|---|---|
| JDK 1.0 | Throwable introduced as root of exception hierarchy |
| JDK 1.2 | `initCause()` added for cause chaining |
| JDK 1.4 | `fillInStackTrace()` made public; `getStackTrace()` / `setStackTrace()` added |
| JDK 7 | `addSuppressed()` / `getSuppressed()` added for try-with-resources |

## Further Reading

- Effective Java, Item 75: Prefer exceptions to error codes
- Effective Java, Item 79: Avoid unnecessary use of checked exceptions
- JVM Specification, §2.10: Exceptions — how the JVM handles throwables
