# Sorting References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Comparator](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Comparator.html)
- **Oracle Java SE 21 API**: [java.lang.Comparable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Comparable.html)
- **Oracle Java SE 21 API**: [java.util.Collections](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html#sort-java.util.List-)
- **Oracle Java SE 21 API**: [java.util.List](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#sort-java.util.Comparator-)

## Official Source Code

- **OpenJDK 21**: [Collections.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Collections.java)
- **OpenJDK 21**: [TimSort.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/TimSort.java)
- **OpenJDK 21**: [Arrays.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Arrays.java) — dual-pivot quicksort

## Language Specification

- **JLS §15.21.2**: Reference Equality Operators == and != (affects Comparator contract)
- **JLS §15.20.1**: Number Equality Operators (affects sorting primitives)

## Version History

- **Java 1.2**: Collections.sort(), Comparable, Comparator introduced
- **Java 5**: Generics added — type-safe Comparators
- **Java 8**: Comparator.comparing(), reversed(), thenComparing() — functional Comparators
- **Java 9**: List.sort() default method
- **Java 11**: Comparator nullsFirst(), nullsLast(), naturalOrder(), reverseOrder()
- **Java 17**: Sequenced Collections — default ordering for sorted collections

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 12: Consider implementing Comparable
- **Introduction to Algorithms (CLRS)** — Chapter 2: Getting Started (Sorting basics)
- **Introduction to Algorithms (CLRS)** — Chapter 6: Heapsort
- **Introduction to Algorithms (CLRS)** — Chapter 7: Quicksort
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Sorting

## JEPs (Java Enhancement Proposals)

- **JEP 269**: Convenience Factory Methods for Collections (Java 9)
- **JEP 301**: Enhanced Enums (affects enum-based Comparators)

## Additional References

- **Baeldung**: [Java Comparable vs Comparator](https://www.baeldung.com/java-comparable-comparator)
- **Baeldung**: [Java Sorting with Comparator](https://www.baeldung.com/java-sorting)
- **Baeldung**: [Java TimSort Algorithm](https://www.baeldung.com/java-timsort)
