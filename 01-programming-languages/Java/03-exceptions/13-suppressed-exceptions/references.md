# References

## Official Documentation
- [Throwable — Oracle JDK](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html) — suppressed exceptions API
- [The try-with-resources Statement — Oracle Tutorials](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) — try-with-resources tutorial
- [AutoCloseable — Oracle JDK](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AutoCloseable.html) — closeable resource interface

## Official Source Code
- [OpenJDK — Throwable.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Throwable.java) — `addSuppressed` implementation
- [OpenJDK — TryStatement.java](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/comp/Attr.java) — compiler handling of suppressed exceptions

## Language Specification
- [JLS §14.20.3 — try-with-resources](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.3) — try-with-resources semantics
- [JLS §11.3 — Run-time Evaluation of Try Statements](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.3) — run-time exception handling

## JEP
- [JEP 274 — Enhanced Method Handles](https://openjdk.org/jeps/274) — related to `addSuppressed` mechanics
- [JEP 214 — Try-with-resources](https://openjdk.org/jeps/214) — JSR 334: `try-with-resources` language feature

## Version History
| Version | Change |
|---------|--------|
| Java 7 | `try-with-resources` introduced; `Throwable.addSuppressed` added |
| Java 9 | `addSuppressed` with `@SuppressWarnings` support |
| Java 21 | `Throwable` improvements; suppressed exception serialization |

## Recommended Reading
- **Effective Java (3rd Edition)**, Joshua Bloch — Item 9: Use try-with-resources
- **Java Concurrency in Practice**, Brian Goetz — suppressed exceptions in concurrent contexts
- [Baeldung: Java Suppressed Exceptions](https://www.baeldung.com/java-suppressed-exceptions)
- [Stack Overflow: How to use addSuppressed](https://stackoverflow.com/questions/15307557/how-to-use-throwable-addsuppressed)
