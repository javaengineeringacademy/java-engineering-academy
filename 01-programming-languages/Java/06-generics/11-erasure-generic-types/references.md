# References: Erasure of Generic Types

## Official Documentation

- [Oracle: Generics — Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)
- [JLS §4.6: Type Erasure](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.6)
- [JLS §15.12.2.5: Method Signature](https://docs.oracle.com/javase/specs/jls/se22/html/jls-15.html#jls-15.12.2.5)

## Language Specification

- Type erasure definition: JLS §4.6
- Erasure of types: JLS §4.6
- Bridge methods: JLS §15.12.2.5

## JDK Source References

- `javap -c` — bytecode inspection for erased types
- `java.lang.reflect.ParameterizedType` — accessing generic type info
- `java.lang.reflect.TypeVariable` — runtime type variable representation

## Version History

| Version | Impact |
|---|---|
| JDK 5 | Type erasure introduced |
| JDK 7 | Diamond operator — inference improvement only |
| JDK 8 | No change to erasure mechanics |
| JDK 12 | No change to erasure mechanics |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 33: "Use `Class<T>` tokens"
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler), Chapter 15
- [Angelika Langer: Java Generics FAQ — Type Erasure](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html)
