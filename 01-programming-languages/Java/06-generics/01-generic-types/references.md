# References

## Official Documentation
- [JDK 21 — Generics Tutorial](https://docs.oracle.com/en/java/javase/21/language/generics.html) — Official Oracle guide
- [JDK 21 — Bounded Type Parameters](https://docs.oracle.com/en/java/javase/21/language/bounded-type-parameters.html) — Official bounded types guide
- [Java Language Specification — Generic Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5) — Formal specification
- [Java Tutorials — Generics](https://docs.oracle.com/javase/tutorial/java/generics/index.html) — Updated Java 8 tutorial

## Official Source Code
- [OpenJDK 21 — java.util.ArrayList](https://github.com/openjdk/jdk/blob/master/src/java.base/java/util/ArrayList.java) — Generic class example
- [OpenJDK 21 — java.util.HashMap](https://github.com/openjdk/jdk/blob/master/src/java.base/java/util/HashMap.java) — Multiple type parameters
- [OpenJDK 21 — java.util.Comparator](https://github.com/openjdk/jdk/blob/master/src/java.base/java/util/Comparator.java) — Functional generic interface

## Language Specification
- **JLS §4.5**: [Types, Classes, and Interfaces](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5) — Formal generic type rules
- **JLS §4.10.2**: [Subtyping among Class and Interface Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.10.2) — Generic subtyping

## Version History

| Version | Change |
|---------|--------|
| Java 1.0 | No generics; raw types only |
| Java 5 | Generics introduced; type parameters, bounded types, wildcards |
| Java 7 | Diamond operator (`<>`) for type inference |
| Java 8 | Improved type inference in lambdas and method references |
| Java 10 | Local variable type inference (`var`) |
| Java 17 | Pattern matching for `instanceof` (indirectly related) |

## Recommended Reading
- **Effective Java (3rd Ed)** — Item 26: Use raw types only in code pre-dating generics
- **Effective Java (3rd Ed)** — Item 29: Consider typesafe heterogeneous containers
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Generics
- **Java Generics and Collections** — Maurice Naftalini & Philip Wadler (O'Reilly)
