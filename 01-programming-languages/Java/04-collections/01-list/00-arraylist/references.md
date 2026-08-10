# ArrayList References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.ArrayList](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayList.html)
- **Java Collections Framework Tutorial**: [ArrayList](https://docs.oracle.com/javase/tutorial/collections/impl.html)

## Official Source Code

- **OpenJDK 21**: [ArrayList.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/ArrayList.java)
- **JDK Source Viewer**: Search `Object[] elementData` in ArrayList for backing array structure

## Language Specification

- **JLS §4.10.3**: [List Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.2**: ArrayList introduced as part of Collections Framework (replaced Vector)
- **Java 5**: Generics added — type-safe ArrayList
- **Java 8**: Default methods, stream() support
- **Java 9**: List.of() factory methods
- **Java 10**: copyOf() immutable copies
- **Java 16**:toArray(IntFunction) improvement

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 13: Prefer interfaces to abstract classes
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Collections
- **Data Structures and Algorithms in Java** — Robert Lafore, Chapter 6 (Arrays)

## JEPs (Java Enhancement Proposals)

- **JEP 269**: Convenience Factory Methods for Collections (Java 9)
- **JEP 218**: Generics Over Primitive Types (Project Valhalla — future)

## Additional References

- **Baeldung**: [Java ArrayList Guide](https://www.baeldung.com/java-arraylist)
- **OpenJDK Bug Database**: Search ArrayList for known edge cases
