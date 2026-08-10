# LinkedHashMap References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.LinkedHashMap](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/LinkedHashMap.html)
- **Java Collections Framework Tutorial**: [LinkedHashMap](https://docs.oracle.com/javase/tutorial/collections/impl.html)

## Official Source Code

- **OpenJDK 21**: [LinkedHashMap.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/LinkedHashMap.java)
- **JDK Source Viewer**: Search `accessOrder` for access-order vs insertion-order behavior

## Language Specification

- **JLS §4.10.3**: [Map Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.4**: LinkedHashMap introduced (maintains insertion/access order)
- **Java 5**: Generics added
- **Java 7**: Constructor for access-order ordering
- **Java 8**: forEach(), replaceAll() methods
- **Java 9**: Map.of() factory methods

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 20: Prefer static factory methods to constructors
- **Introduction to Algorithms (CLRS)** — Chapter 13: Red-Black Trees (underlying structure)
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Linked Maps

## Additional References

- **Baeldung**: [Java LinkedHashMap Guide](https://www.baeldung.com/java-linked-hashmap)
- **Baeldung**: [LRU Cache with LinkedHashMap](https://www.baeldung.com/lru-cache-java)
