# References: Type Inference

## Official Documentation

- [Oracle: Generics — Type Inference](https://docs.oracle.com/javase/tutorial/java/generics/inference.html)
- [Oracle: The Diamond Operator](https://docs.oracle.com/javase/tutorial/java/generics/diamond.html)
- [JEP 286: Local Variable Type Inference (var)](https://openjdk.org/jeps/286)
- [JLS §18: Type Inference](https://docs.oracle.com/javase/specs/jls/se22/html/jls-18.html)
- [JLS §14.4.1: Local Variable Declarations](https://docs.oracle.com/javase/specs/jls/se22/html/jls-14.html#jls-14.4.1)

## Language Specification

- Type inference algorithm: JLS §18
- Target-type inference: JLS §15.12.2.7
- Diamond operator: JLS §15.9.1
- `var` inference: JLS §14.4.1

## JDK Source References

- `java.util.List` — target-type inference in factory methods
- `java.util.Map.Entry` — complex type inference in nested generics
- `java.util.stream.Stream` — lambda type inference

## Version History

| Version | Feature |
|---|---|
| JDK 5 | Basic type inference introduced |
| JDK 7 | Diamond operator `<>` for constructors |
| JDK 8 | Target-type inference in lambda contexts |
| JDK 10 | `var` local variable type inference (JEP 286) |
| JDK 11 | `var` in lambda parameters |
| JDK 16 | Records with `var` |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 14: "In generics, use bounded wildcards to increase API flexibility"
- [Angelika Langer: Java Generics FAQ — Type Inference](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html)
