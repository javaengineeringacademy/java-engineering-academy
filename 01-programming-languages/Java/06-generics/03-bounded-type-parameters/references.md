# References: Bounded Type Parameters

## Official Documentation

- [Oracle: Generics — Bounded Type Parameters](https://docs.oracle.com/javase/tutorial/java/generics/bounded.html)
- [Oracle: Generics — Subtyping](https://docs.oracle.com/javase/tutorial/java/generics/subtyping.html)
- [JLS §4.4: Type Variables](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.4)
- [JLS §8.4.4: Generic Methods](https://docs.oracle.com/javase/specs/jls/se22/html/jls-8.html#jls-8.4.4)

## Language Specification

- Bounded type variables: JLS §4.4
- Multiple bounds: JLS §4.4 — intersection types
- Type inference with bounds: JLS §18.1.3

## JDK Source References

- `java.util.Collections.sort()` — uses `<T extends Comparable<? super T>>`
- `java.util.Comparable` — target of most self-bounded patterns
- Builder pattern in `java.lang.ProcessBuilder`, `java.util.stream.Stream.Builder`

## Version History

| Version | Feature |
|---|---|
| JDK 5 | Bounded type parameters introduced |
| JDK 7 | Improved inference with diamond operator |
| JDK 8 | Type inference in lambdas improved |
| JDK 16 | Sealed classes interact with bounds |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 31: "Use bounded wildcards to increase API flexibility"
- *Effective Java* (Joshua Bloch), Item 32: "Combine generics and varargs judiciously"
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler), Chapter 7
