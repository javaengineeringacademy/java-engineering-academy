# References: Restrictions on Generics

## Official Documentation

- [Oracle: Generics — Restrictions](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html)
- [Oracle: Generics — No Primitive Types](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#noPrimitives)
- [Oracle: Generics — No instanceof or casts](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#instanceof)
- [Oracle: Generics — No Generic Arrays](https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html#noArrays)
- [JLS §4.4: Type Variables](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.4)

## Language Specification

- Type erasure and restrictions: JLS §4.6
- Static members and type parameters: JLS §8.3.1.2
- Array creation with generics: JLS §15.10.1

## JDK Source References

- `java.lang.reflect.Array` — `newInstance()` for dynamic array creation
- `com.google.gson.reflect.TypeToken` — Gson's type token pattern
- `org.springframework.core.ParameterizedTypeReference` — Spring's type reference pattern

## Version History

| Version | Impact |
|---|---|
| JDK 5 | Restrictions established with type erasure |
| JDK 7 | Diamond operator didn't change restrictions |
| JDK 9 | Module system — no change to restrictions |
| JDK 16 | Sealed classes — no change to restrictions |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 33: "Use `Class<T>` tokens for type safety"
- [Angelika Langer: Java Generics FAQ — Restrictions](https://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingFAQ.html)
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler), Chapter 15
