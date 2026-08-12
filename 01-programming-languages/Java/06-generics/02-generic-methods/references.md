# References: Generic Methods

## Official Documentation

- [Oracle: Generics — Generic Methods](https://docs.oracle.com/javase/tutorial/java/generics/methods.html)
- [Oracle: Generics — Bounded Type Parameters](https://docs.oracle.com/javase/tutorial/java/generics/bounded.html)
- [JLS §8.4.4: Generic Methods](https://docs.oracle.com/javase/specs/jls/se22/html/jls-8.html#jls-8.4.4)
- [JLS §15.12.2.7: Inferring Type Arguments Based on Actual Arguments](https://docs.oracle.com/javase/specs/jls/se22/html/jls-15.html#jls-15.12.2.7)
- [Java API: java.lang.reflect.Method](https://docs.oracle.com/en/java/javase/22/docs/api/java.base/java/lang/reflect/Method.html)

## Language Specification

- Type inference for generic methods: JLS §18.5.2
- Method invocation type inference: JLS §15.12.2.7
- Type witnesses: JLS §15.12.2.6

## JDK Source References

- `java.util.Collections.emptyList()` — canonical generic factory method
- `java.util.Arrays.asList(T...)` — varargs generic method
- `java.util.stream.Stream.of(T...)` — generic varargs with inference

## Version History

| Version | Feature |
|---|---|
| JDK 5 | Generic methods introduced |
| JDK 7 | Diamond operator improved inference |
| JDK 8 | Target-type inference in lambda contexts |
| JDK 10 | `var` with generic method inference |
| JDK 21 | Pattern matching interacts with generic inference |

## Recommended Reading

- *Effective Java* (Joshua Bloch), Item 30: "Favor generic methods"
- *Effective Java* (Joshua Bloch), Item 31: "Use bounded wildcards to increase API flexibility"
- *Java Generics and Collections* (Maurizio Gftigori, Philip Wadler)
