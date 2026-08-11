# References

## Official Documentation
- [JDK 21 — Exception](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Exception.html) — Javadoc for the Exception class
- [Java Tutorials — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/) — Official exception overview
- [Java Tutorials — Catch or Specify](https://docs.oracle.com/javase/tutorial/essential/exceptions/catchOrDeclare.html) — The Catch or Specify Requirement
- [Java Tutorials — Try-with-Resources](https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) — Resource management

## Official Source Code
- [OpenJDK 21 — Exception.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Exception.java) — Exception class source
- [OpenJDK 21 — IOException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/io/IOException.java) — Common checked exception source
- [OpenJDK 21 — SQLException.java](https://github.com/openjdk/jdk/blob/master/src/java.sql/java/sql/SQLException.java) — Database exception source

## Language Specification
- **JLS §11.1.1**: [Compile-Time Step 2: Determine Logic Error Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.1.1)
- **JLS §11.2**: [Compile-Time Checking of Exceptions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-11.html#jls-11.2)
- **JVM Spec §6.5**: [The athrow Instruction](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.athrow)

## Version History

| Version | Change |
|---------|--------|
| JDK 1.0 | Checked exceptions and the catch-or-specify requirement introduced |
| JDK 5 | Generics improved exception type safety in multi-catch |
| JDK 7 | Multi-catch and try-with-resources reduced boilerplate |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 69: Use exceptions only for exceptional conditions
- **Effective Java (3rd Ed)** — Item 71: Avoid unnecessary use of checked exceptions
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Exception Handling
