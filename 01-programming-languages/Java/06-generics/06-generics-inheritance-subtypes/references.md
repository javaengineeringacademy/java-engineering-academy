# References: Generics and Inheritance / Subtyping

## Official Documentation

- [Oracle: Generics — Subtyping](https://docs.oracle.com/javase/tutorial/java/generics/subtyping.html)
- [Oracle: Generics — Type Inference](https://docs.oracle.com/javase/tutorial/java/generics/inference.html)
- [JLS §4.10.2: Subtyping among Class and Interface Types](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.10.2)
- [JLS §4.5.1: Type Arguments of Parameterized Types](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.5.1)

## Language Specification

- Subtyping with parameterized types: JLS §4.10.2
- Type hierarchy: JLS §4.10
- Covariant return types: JLS §8.4.5

## JDK Source References

- `java.util.Collection<E>` — subtype hierarchy (`List<E>`, `Set<E>`, `Queue<E>`)
- `java.util.AbstractList<E>` — partial implementation in hierarchy
- `java.util.ImmutableCollections` — covariant unmodifiable wrappers

## Version History

| Version | Feature |
|---|---|
| JDK 5 | Generics and invariance introduced |
| JDK 7 | Diamond operator inference |
| JDK 12 | `var` with inferred types |
| JDK 16 | Sealed classes and subtyping |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 31: "Use bounded wildcards for flexibility"
- *Effective Java* (Joshua Bloch), Item 29: "Consider typesafe heterogeneous containers"
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler), Chapter 4
