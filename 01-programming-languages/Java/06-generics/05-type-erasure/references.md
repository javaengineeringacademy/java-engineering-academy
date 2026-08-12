# References: Type Erasure

## Official Documentation

- [Oracle: Generics — Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)
- [Oracle: Generics — Restrictions — No Primitive Types, No instanceof, No Create Arrays](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html)
- [JLS §4.6: Type Erasure](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.6)
- [JLS §15.12.2.1: Identify Potentially Applicable Methods](https://docs.oracle.com/javase/specs/jls/se22/html/jls-15.html#jls-15.12.2.1)

## Language Specification

- Type erasure definition: JLS §4.6
- Erasure of types: JLS §4.6
- Bridge methods: JLS §15.12.2.5
- Overloading and erasure: JLS §8.4.8.1

## JDK Source References

- `java.lang.Class` — type token pattern
- `java.lang.reflect.ParameterizedType` — accessing generic type info via reflection
- `java.lang.reflect.TypeVariable` — runtime representation of type variables

## Version History

| Version | Impact |
|---|---|
| JDK 5 | Type erasure introduced as backward-compatible approach |
| JDK 7 | Diamond operator improved inference |
| JDK 8 | Repeated annotations with generics |
| JDK 12 | Switch expressions — no direct erasure impact |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 33: "Use `Class<T>` tokens for type safety"
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler), Chapter 15
- [Angelika Langer: Java Generics FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html)
