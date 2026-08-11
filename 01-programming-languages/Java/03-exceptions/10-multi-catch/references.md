# References

## Official Documentation
- [JLS §14.20](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20) — The try Statement (multi-catch)
- [JEP 169](https://openjdk.org/jeps/169) — Improve the Control of Checked Exceptions
- [Java Tutorials — Catching Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/catchable.html) — Multi-catch tutorial
- [JDK 21 — Multi-catch syntax](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Exception.html) — API overview

## Official Source Code
- [OpenJDK 21 — JavacParser.java](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/parser/JavacParser.java) — Multi-catch parsing
- [OpenJDK 21 — CatchClause.java (compiler)](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/tree/JCTree.java) — Catch clause AST

## Language Specification
- **JLS §14.20**: [The try Statement](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.20)
- **JVM Spec §3.12**: [try Statement](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-3.html#jvms-3.12) — JVM-level multi-catch semantics

## Version History

| Version | Change |
|---------|--------|
| JDK 7 | Multi-catch syntax introduced (JEP 169) |
| JDK 9 | Effectively final variables allowed in multi-catch |
| JDK 21 | Current state — no changes to multi-catch |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 65: Favor the use of standard exceptions
- **Modern Java in Action** — Urma, Fusco, Mycroft, Chapter 14: Streams and exception handling
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
- **Baeldung** — [Java Multi-Catch](https://www.baeldung.com/java-multi-catch) — Practical tutorial

## Related Topics
- [05-checked-exception](../05-checked-exception/references.md) — Checked exception references
- [06-unchecked-exception](../06-unchecked-exception/references.md) — Unchecked exception references
