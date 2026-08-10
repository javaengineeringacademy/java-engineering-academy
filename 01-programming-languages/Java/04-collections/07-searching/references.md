# Searching References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Collections#binarySearch](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html#binarySearch-java.util.List-java.lang.Object-)
- **Oracle Java SE 21 API**: [java.util.Arrays#binarySearch](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#binarySearch-int:A-int-)
- **Oracle Java SE 21 API**: [java.util.List#indexOf](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#indexOf-java.lang.Object-)

## Official Source Code

- **OpenJDK 21**: [Collections.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Collections.java) — binarySearch
- **OpenJDK 21**: [Arrays.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Arrays.java) — binarySearch
- **OpenJDK 21**: [ArrayList.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/ArrayList.java) — indexOf

## Language Specification

- **JLS §15.21.2**: Reference Equality Operators == and != (affects search equality)

## Version History

- **Java 1.2**: Collections.binarySearch(), indexOf(), lastIndexOf() introduced
- **Java 5**: Generics added — type-safe searching
- **Java 8**: Stream API — findFirst(), findAny(), anyMatch(), allMatch()
- **Java 9**: List.of() factory methods
- **Java 21**: Sequenced Collections — getFirst(), getLast()

## Recommended Reading

- **Introduction to Algorithms (CLRS)** — Chapter 2: Insertion Sort (sequential search)
- **Introduction to Algorithms (CLRS)** — Chapter 12: Binary Search Trees
- **Introduction to Algorithms (CLRS)** — Chapter 15: Dynamic Programming (optimal search)
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Searching Algorithms

## Additional References

- **Baeldung**: [Java Collections binarySearch](https://www.baeldung.com/java-collections-binary-search)
- **Baeldung**: [Java indexOf Methods](https://www.baeldung.com/java-indexof)
- **GeeksforGeeks**: [Binary Search in Java](https://www.geeksforgeeks.org/binary-search-in-java/)
