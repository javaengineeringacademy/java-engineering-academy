# TreeSet References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.TreeSet](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/TreeSet.html)
- **Java Collections Framework Tutorial**: [TreeSet](https://docs.oracle.com/javase/tutorial/collections/impl.html)

## Official Source Code

- **OpenJDK 21**: [TreeSet.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/TreeSet.java)
- **JDK Source Viewer**: Search `NavigableMap<E,Object>` for backing TreeMap structure

## Language Specification

- **JLS §4.10.3**: [Set Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.2**: TreeSet introduced as part of Collections Framework
- **Java 5**: Generics added
- **Java 6**: NavigableSet interface added — TreeSet implements it
- **Java 9**: Set.of() factory methods

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 21: Use interface references to refer to objects
- **Introduction to Algorithms (CLRS)** — Chapter 13: Red-Black Trees (TreeSet internals)
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Sorted Sets

## JEPs (Java Enhancement Proposals)

- **JEP 269**: Convenience Factory Methods for Collections (Java 9)
- **JEP 181**: Nest-Based Access Control (affects internal comparator access)

## Additional References

- **Baeldung**: [Java TreeSet Guide](https://www.baeldung.com/java-tree-set)
- **GeeksforGeeks**: [TreeSet in Java](https://www.geeksforgeeks.org/treeset-in-java/)
