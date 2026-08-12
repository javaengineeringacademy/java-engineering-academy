# References: Wildcards

## Official Documentation

- [Oracle: Generics — Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html)
- [Oracle: Generics — Upper Bounded Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/upperBounded.html)
- [Oracle: Generics — Lower Bounded Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/lowerBounded.html)
- [JLS §4.5.1: Type Arguments of Parameterized Types](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.5.1)
- [JLS §5.1.10: Capture Conversion](https://docs.oracle.com/javase/specs/jls/se22/html/jls-5.html#jls-5.1.10)

## Language Specification

- Wildcard types: JLS §4.5.1
- Capture conversion: JLS §5.1.10
- Subtyping with wildcards: JLS §4.10.2

## JDK Source References

- `java.util.Collections.copy(List<? super T>, List<? extends T>)` — canonical PECS example
- `java.util.Comparator.comparing(Function<? super T, ? extends R>)` — dual wildcard use
- `java.util.stream.Stream.flatMap(Function<? super T, ? extends Stream<? extends R>>)` — stacked wildcards in practice

## Version History

| Version | Feature |
|---|---|
| JDK 5 | Wildcards introduced with generics |
| JDK 7 | Diamond operator improved inference |
| JDK 8 | PECS became standard best practice |
| JDK 21 | Pattern matching with wildcards |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 31: "Use bounded wildcards to increase API flexibility"
- *Effective Java* (Joshua Bloch), Item 32: "Combine generics and varargs judiciously"
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler), Chapter 5
