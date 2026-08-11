# References

## Official Documentation
- [Throwable — Oracle JDK](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html) — root of the exception hierarchy
- [Exception — Oracle JDK](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Exception.html) — checked exception base class
- [Error — Oracle JDK](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Error.html) — serious errors that programs should not catch
- [RuntimeException — Oracle JDK](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/RuntimeException.html) — unchecked exception base class
- [Checked Exceptions — Oracle Tutorials](https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrDeclare.html) — checked vs unchecked exceptions

## Official Source Code
- [OpenJDK — Throwable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java)
- [OpenJDK — Exception.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Exception.java)
- [OpenJDK — RuntimeException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/RuntimeException.java)
- [OpenJDK — Error.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Error.java)

## Language Specification
- [JLS §11.1 — Kinds of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1)
- [JLS §11.2 — Compile-Time Checking of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2)
- [JLS §11.3 — Run-time Evaluation of Try Statements](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.3)
- [JVM §2.10 — Exceptions](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.10) — JVM-level exception handling

## Version History
| Version | Change |
|---------|--------|
| Java 1.0 | Initial exception hierarchy: `Throwable`, `Exception`, `Error` |
| Java 1.2 | `RuntimeException` introduced as unchecked base |
| Java 7 | `AutoCloseable` and try-with-resources refined exception handling |
| Java 14 | `Throwable` enhancements: improved stack traces, `StackWalker` API |
| Java 21 | Sealed exceptions, enhanced `Throwable.getMessage` |

## Recommended Reading
- **Effective Java (3rd Edition)**, Joshua Bloch — Item 58: Use checked exceptions for recoverable conditions, unchecked for programming errors
- **Java Concurrency in Practice**, Brian Goetz — Exception handling in concurrent contexts
- **Clean Code**, Robert C. Martin — Chapter 7: Error Handling patterns
- [Baeldung: Java Exception Hierarchy](https://www.baeldung.com/java-exception-hierarchy)
