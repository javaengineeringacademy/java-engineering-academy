# HashMap References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.HashMap](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/HashMap.html)
- **Java Collections Framework Tutorial**: [HashMap](https://docs.oracle.com/javase/tutorial/collections/impl.html)

## Official Source Code

- **OpenJDK 21**: [HashMap.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/HashMap.java)
- **JDK Source Viewer**: Search `Node<K,V>` and `TreeNode<K,V>` for bucket and tree node structure

## Language Specification

- **JLS §4.10.3**: [Map Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.2**: HashMap introduced as part of Collections Framework
- **Java 5**: Generics added
- **Java 8**: Tree bins — when bucket exceeds 8 elements, converts to Red-Black tree (O(log n) worst case)
- **Java 9**: Map.of() factory methods
- **Java 11**: compute() improvements, help() for concurrent operations

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 11: Always override hashCode when you override equals
- **Introduction to Algorithms (CLRS)** — Chapter 11: Hash Tables
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on HashMap Internals

## JEPs (Java Enhancement Proposals)

- **JEP 269**: Convenience Factory Methods for Collections (Java 9)
- **JEP 280**: Indify String Concatenation (affects internal String hashing)

## Additional References

- **Baeldung**: [Java HashMap Guide](https://www.baeldung.com/java-hashmap)
- **Baeldung**: [HashMap Internals](https://www.baeldung.com/java-hashmap-internal)
- **OpenJDK Bug Database**: Search HashMap for known edge cases
