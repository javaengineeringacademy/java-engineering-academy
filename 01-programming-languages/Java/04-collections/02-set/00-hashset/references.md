# HashSet References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.HashSet](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/HashSet.html)
- **Java Collections Framework Tutorial**: [HashSet](https://docs.oracle.com/javase/tutorial/collections/impl.html)

## Official Source Code

- **OpenJDK 21**: [HashSet.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/HashSet.java)
- **JDK Source Viewer**: Search `HashMap<E,PRESENT>` for backing HashMap structure

## Language Specification

- **JLS §4.10.3**: [Set Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.2**: HashSet introduced as part of Collections Framework
- **Java 5**: Generics added
- **Java 8**: Tree bins for hash table (O(log n) worst case instead of O(n))
- **Java 9**: Set.of() factory methods

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 13: Prefer interfaces to abstract classes
- **Introduction to Algorithms (CLRS)** — Chapter 11: Hash Tables
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Hash Sets

## JEPs (Java Enhancement Proposals)

- **JEP 269**: Convenience Factory Methods for Collections (Java 9)
- **JEP 280**: Indify String Concatenation (affects internal String hashing)

## Additional References

- **Baeldung**: [Java HashSet Guide](https://www.baeldung.com/java-hashset)
- **GeeksforGeeks**: [How HashSet Works internally in Java](https://www.geeksforgeeks.org/internal-working-of-hashset-in-java/)
