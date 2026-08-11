# References: The throws Declaration

## Official Documentation
- [JLS §11.1.1](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.1) — Exception Checking (throws requirements)
- [JLS §8.4.6](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.6) — throws in Method Declarations
- [JLS §11.2](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2) — Compile-Time Checking of Exceptions
- [Java Tutorials — Declarations](https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrDeclare.html) — Catch or Declare

## Official Source Code
- [OpenJDK 21 — CheckExceptions.java](https://github.com/openjdk/jdk/blob/master/src/jdk.compiler/share/classes/com/sun/tools/javac/comp/Check.java) — Compiler enforcement of throws

## Language Specification
- **JLS §8.4.6**: [throws Clause](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.6)
- **JLS §11.2**: [Compile-Time Checking of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2)
- **JLS §15.12.2.6**: [Method Invocation Type](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.12.2.6) — How throws affects method resolution
- **JVM Spec §4.3.3**: [descriptor](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.3.3) — Exception descriptors in bytecode

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | `throws` keyword introduced with checked exceptions |
| JDK 5 | Varargs and `@SafeVarargs` affected throws with generic arrays |
| JDK 7 | Multi-catch improved throws handling |
| JDK 7 | Precise rethrow refined throws semantics |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 61: Throw exceptions appropriate to the abstraction; Item 62: Favor the use of standard exceptions
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
- **Clean Code** — Robert C. Martin, Chapter 7: Boundaries (exception translation)
- **Release It!** — Michael T. Nygard, Chapter 4: Stability Patterns (exception propagation)

---

## Cross-Module References

- [JDBC Module](../10-jdbc/) — SQLException handling in database operations
- [Concurrency Module](../12-concurrency/) — Thread exception handling and interruption
