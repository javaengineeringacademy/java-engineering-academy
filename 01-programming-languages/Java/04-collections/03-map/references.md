# Map Interface References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Map](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Map.html)
- **Java Collections Framework Tutorial**: [Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces.html)

## Official Source Code

- **OpenJDK 21**: [Map.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Map.java)
- **OpenJDK 21**: [AbstractMap.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/AbstractMap.java)

## Language Specification

- **JLS §4.10.3**: [Map Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)
- Note: Map is NOT part of Collection hierarchy — it's a separate top-level interface

## Version History

- **Java 1.2**: Map interface introduced as part of Collections Framework
- **Java 5**: Generics added — type-safe Map
- **Java 8**: Default methods, forEach(), replaceAll(), compute(), merge()
- **Java 9**: Map.of(), Map.copyOf() factory methods
- **Java 10**: putIfAbsent returns value, compute improvements
- **Java 17**: Sealed Maps (future consideration)

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 20: Prefer static factory methods to constructors
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Maps
- **Introduction to Algorithms (CLRS)** — Chapter 11: Hash Tables (HashMap internals)

## JEPs (Java Enhancement Proposals)

- **JEP 269**: Convenience Factory Methods for Collections (Java 9)
- **JEP 394**: Pattern Matching for instanceof (improves Map type checks)

## Additional References

- **Baeldung**: [Java Map Tutorial](https://www.baeldung.com/java-map)
- **Baeldung**: [Map Implementations Comparison](https://www.baeldung.com/java-map-implementations)
- **GeeksforGeeks**: [Map Interface in Java](https://www.geeksforgeeks.org/map-interface-java-examples/)
