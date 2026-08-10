# Iteration Patterns References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Iterator](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Iterator.html)
- **Oracle Java SE 21 API**: [java.lang.Iterable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Iterable.html)
- **Oracle Java SE 21 API**: [java.util.ListIterator](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ListIterator.html)
- **Oracle Java SE 21 API**: [java.util.Spliterator](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Spliterator.html)

## Official Source Code

- **OpenJDK 21**: [Iterator.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Iterator.java)
- **OpenJDK 21**: [Iterable.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/lang/Iterable.java)
- **OpenJDK 21**: [ListIterator.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/ListIterator.java)
- **OpenJDK 21**: [Spliterator.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Spliterator.java)

## Language Specification

- **JLS §14.14.2**: enhanced for loop (for-each) — syntactic sugar for Iterator
- **JLS §15.14.15**: enhanced for statement semantics

## Version History

- **Java 1.2**: Iterator, Iterable introduced as part of Collections Framework
- **Java 5**: Enhanced for loop (for-each) — syntactic sugar over Iterator
- **Java 8**: forEach() default method on Iterable, Spliterator for parallel streams
- **Java 9**: forEachRemaining() improvements
- **Java 16**: Record patterns (affects iteration over records)
- **Java 21**: Sequenced Collections — reversed(), getFirst(), getLast()

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 58: Use for-each loops
- **Effective Java (3rd Ed)** — Item 59: Know the contents of libraries and avoid reinventing
- **Introduction to Algorithms (CLRS)** — Chapter 10: Elementary Data Structures (Iterator patterns)

## JEPs (Java Enhancement Proposals)

- **JEP 321**: HTTP Client (uses Iterator patterns)
- **JEP 394**: Pattern Matching for instanceof (affects Iterator type checks)

## Additional References

- **Baeldung**: [Java Iterator Guide](https://www.baeldung.com/java-iterator)
- **Baeldung**: [Java for-each loop](https://www.baeldung.com/java-foreach)
- **Baeldung**: [Java Spliterator](https://www.baeldung.com/java-spliterator)
