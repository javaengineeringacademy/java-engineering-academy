# References

## Official Documentation
- [JLS §14.20.2 — The try Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.2) — try-finally semantics
- [JLS §14.21 — Unreachable Statements](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.21) — finally block reachability rules
- [Oracle: The try Statement](https://docs.oracle.com/javase/tutorial/essential/exceptions/finally.html) — official tutorial
- [Oracle: try-with-resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) — modern alternative

## Official Source Code
- [OpenJDK — FinallyBlock.java (compiler)](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/comp/Attr.java)
- [OpenJDK — TryStatement.java (parser)](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/parser/JavacParser.java)

## Language Specification
- [JLS §14.20.2 — try Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20.2)
- [JLS §14.21 — Unreachable Statements](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.21)
- [JVM §3.12 — try Statement](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-3.html#jvms-3.12) — JVM-level finally semantics

## Version History
| Version | Change |
|---------|--------|
| Java 1.0 | `finally` keyword introduced |
| Java 5 | `try-with-resources` introduced (JSR 201) |
| Java 7 | `AutoCloseable` refined; `finally` execution guaranteed |
| Java 11 | `finally` with `return` semantics clarified |

## Recommended Reading
- **Effective Java (3rd Edition)**, Joshua Bloch — Item 9: Use try-with-resources; Item 76: Prefer try-with-resources to try-finally
- [Baeldung: Java Finally Block](https://www.baeldung.com/java-finally)
- [Oracle: Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/) — official tutorials
