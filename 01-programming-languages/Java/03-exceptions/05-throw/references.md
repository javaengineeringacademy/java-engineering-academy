# References: The throw Keyword

## Official Documentation
- [JLS §14.18](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.18) — The throw Statement
- [JLS §11.1.1](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.1) — The Kinds of Exceptions (throw semantics)
- [Java Tutorials — Throwing Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/throwing.html) — Official tutorial

## Official Source Code
- [OpenJDK 21 — JLS 14.18 ThrowStatement](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/comp/Attr.java) — Compiler handling of throw

## Language Specification
- **JLS §14.18**: [throw Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.18)
- **JLS §15.27**: [Throw Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.27) — throw in lambda expressions (Java 7+)
- **JLS §14.21**: [Unreachable Statements](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.21) — Rules for throw placement

## JVM Spec
- **JVM Spec §3.68**: [athrow](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-3.html#jvms-3.68) — The JVM instruction that implements throw
- **JVM Spec §2.10**: [Exceptions](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html#jvms-2.10) — Exception handling at the JVM level

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | `throw` keyword introduced |
| JDK 7 | Precise rethrow added (catch and rethrow without declaring caught type) |
| JDK 7 | Multi-catch (`catch (A | B e)`) improved throw handling |
| JDK 11 | Stack trace improvement for rethrown exceptions |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 65: Don't fail silently; Item 73: Throw exceptions appropriate to the abstraction
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
- **Java Concurrency in Practice** — Brian Goetz, Chapter 7: Cancellation and Shutdown (exception propagation)
- **Clean Code** — Robert C. Martin, Chapter on Error Handling
