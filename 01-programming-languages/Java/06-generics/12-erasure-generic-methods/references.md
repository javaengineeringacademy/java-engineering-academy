# References: Erasure of Generic Methods

## Official Documentation

- [Oracle: Generics — Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)
- [JLS §4.6: Type Erasure](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.6)
- [JLS §15.12.2.5: Method Signature](https://docs.oracle.com/javase/specs/jls/se22/html/jls-15.html#jls-15.12.2.5)
- [JLS §15.12.2.7: Inferring Type Arguments](https://docs.oracle.com/javase/specs/jls/se22/html/jls-15.html#jls-15.12.2.7)

## Language Specification

- Bridge methods: JLS §15.12.2.5
- Method erasure: JLS §4.6
- Overloading and erasure: JLS §8.4.8.1

## JDK Source References

- `java.lang.reflect.Method.getGenericReturnType()` — accessing generic return type
- `java.lang.reflect.Method.getGenericParameterTypes()` — accessing generic parameter types
- Bridge method examples in `java.util.AbstractList`

## Version History

| Version | Impact |
|---|---|
| JDK 5 | Bridge methods introduced with generics |
| JDK 7 | No change to bridge method mechanics |
| JDK 8 | Default methods in interfaces — bridge methods still apply |
| JDK 16 | Sealed classes — no change to bridge method mechanics |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 30: "Favor generic methods"
- *Effective Java* (Joshua Bloch), Item 31: "Use bounded wildcards"
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler), Chapter 15
- [Angelika Langer: Java Generics FAQ — Bridge Methods](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html)
