# References

## Official Documentation
- [JLS §14.20.2](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.2) — The try Statement (finally semantics)
- [JLS §14.21](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.21) — Unreachable Statements (finally block reachability)
- [Java Tutorials — The try Statement](https://docs.oracle.com/javase/tutorial/essential/exceptions/finally.html) — Official tutorial
- [Java Tutorials — Try-with-Resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) — Modern alternative

## Official Source Code
- [OpenJDK 21 — TryStatement.java (compiler)](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/comp/Attr.java) — finally block compilation
- [OpenJDK 21 — JavacParser.java](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/parser/JavacParser.java) — try-finally parsing

## Language Specification
- **JLS §14.20.2**: [try Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.2)
- **JLS §14.21**: [Unreachable Statements](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.21)
- **JVM Spec §3.12**: [try Statement](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-3.html#jvms-3.12) — JVM-level finally semantics

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | `finally` keyword introduced |
| JDK 5 | `try-with-resources` introduced as alternative (JSR 201) |
| JDK 7 | `AutoCloseable` refined; `finally` execution guaranteed |
| JDK 11 | `finally` with `return` semantics clarified |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 9: Use try-with-resources; Item 76: Prefer try-with-resources to try-finally
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
- **Java Concurrency in Practice** — Brian Goetz, Chapter 7: Cancellation and Shutdown

---

## Cross-Module References

- [I/O Module](../06-io/) — Exception handling in file and stream operations
- [NIO Module](../07-nio/) — Asynchronous exception handling with Channels
- [JDBC Module](../10-jdbc/) — SQLException handling in database operations
- [Concurrency Module](../12-concurrency/) — Thread exception handling and interruption
