# Hashtable References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Hashtable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Hashtable.html)
- **Legacy Collections Tutorial**: [Hashtable Class](https://docs.oracle.com/javase/tutorial/collections/legacy.html)

## Official Source Code

- **OpenJDK 21**: [Hashtable.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Hashtable.java)
- **JDK Source Viewer**: Search `synchronized` methods for thread-safety pattern

## Language Specification

- **JLS §4.10.3**: [Map Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.0**: Hashtable introduced (pre-Collections Framework — synchronized)
- **Java 1.2**: Collections Framework — Hashtable retrofitted to implement Map
- **Java 5**: Generics added
- **Java 9**: Deprecated (due to synchronized overhead) — use ConcurrentHashMap

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 81: Prefer concurrency utilities to wait/notify
- **Java Concurrency in Practice** — Chapter 5: Building Custom Synchronization
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Legacy Collections

## Deprecation Notices

- **Java 9+**: Hashtable is not deprecated but strongly discouraged — use ConcurrentHashMap or HashMap

## Additional References

- **Baeldung**: [Java Hashtable vs ConcurrentHashMap](https://www.baeldung.com/java-hashtable-vs-concurrenthashmap)
- **OpenJDK Bug Database**: Search Hashtable for known thread-safety edge cases
