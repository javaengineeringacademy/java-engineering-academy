# Stack References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Stack](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Stack.html)
- **Legacy Collections Tutorial**: [Stack Class](https://docs.oracle.com/javase/tutorial/collections/legacy.html)

## Official Source Code

- **OpenJDK 21**: [Stack.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Stack.java)
- **JDK Source Viewer**: Search `synchronized` methods for thread-safety pattern

## Language Specification

- **JLS §4.10.3**: [List Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.0**: Stack introduced (pre-Collections Framework — extends Vector)
- **Java 1.2**: Collections Framework — Stack retrofitted to implement List
- **Java 6**: Deque interface added — ArrayDeque preferred as stack replacement
- **Java 9+**: Deprecated for removal (due to synchronized overhead)

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 25: Prefer lists to arrays
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Stacks and Queues
- **Introduction to Algorithms (CLRS)** — Chapter 10: Elementary Data Structures

## Deprecation Notices

- **Java 9+**: Stack is deprecated for removal — use ArrayDeque as LIFO stack

## Additional References

- **Baeldung**: [Java Stack vs ArrayDeque](https://www.baeldung.com/java-stack-vs-arraydeque)
- **OpenJDK Bug Database**: Search Stack for known thread-safety edge cases
