# References: Raw Types

## Official Documentation

- [Oracle: Generics — Raw Types](https://docs.oracle.com/javase/tutorial/java/generics/rawtypes.html)
- [JLS §4.8: Raw Types](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.8)
- [JLS §4.10.2: Subtyping among Class and Interface Types](https://docs.oracle.com/javase/specs/jls/se22/html/jls-4.html#jls-4.10.2)

## Language Specification

- Raw types definition: JLS §4.8
- Subtyping with raw types: JLS §4.10.2
- Unchecked warnings: JLS §5.1.9

## JDK Source References

- `java.util.List` — raw type vs `List<E>`
- `java.util.ArrayList` — raw type vs `ArrayList<E>`
- Pre-generics code in older JDK versions

## Version History

| Version | Feature |
|---|---|
| JDK 1.2 | Collections framework without generics |
| JDK 5 | Generics introduced; raw types retained for backward compatibility |
| JDK 7 | Diamond operator improved inference |
| JDK 9 | Module system — no change to raw types |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 26: "Don't use raw types"
- *Effective Java* (Joshua Bloch), Item 27: "Eliminate unchecked warnings"
- [Angelika Langer: Java Generics FAQ — Raw Types](https://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingFAQ.html)
