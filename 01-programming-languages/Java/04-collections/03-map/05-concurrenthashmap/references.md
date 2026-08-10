# ConcurrentHashMap References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.concurrent.ConcurrentHashMap](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)
- **Java Concurrency Tutorial**: [Concurrent Collections](https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html)

## Official Source Code

- **OpenJDK 21**: [ConcurrentHashMap.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/concurrent/ConcurrentHashMap.java)
- **JDK Source Viewer**: Search `Node<K,V>`, `TreeBin`, `CounterCell` for segment structure

## Language Specification

- **JLS §4.10.3**: [Map Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)
- **JLS §17.4**: Happens-before Order (affects concurrent visibility)

## Version History

- **Java 5**: ConcurrentHashMap introduced (lock striping)
- **Java 8**: CAS-based implementation — full concurrent reads/writes without locks
- **Java 9**: compute(), forEach() improvements
- **Java 11**: putIfAbsent() improvements
- **Java 19**: Virtual Threads (Project Loom — affects concurrent Map usage)

## Recommended Reading

- **Java Concurrency in Practice** — Chapter 5: Building Custom Synchronization
- **Effective Java (3rd Ed)** — Item 81: Prefer concurrency utilities to wait/notify
- **Introduction to Algorithms (CLRS)** — Chapter 11: Hash Tables

## JEPs (Java Enhancement Proposals)

- **JEP 193**: Variable Handles (affects internal CAS operations)
- **JEP 391**: Foreign Function & Memory API (future concurrent data structures)

## Additional References

- **Baeldung**: [Java ConcurrentHashMap Guide](https://www.baeldung.com/java-concurrent-hashmap)
- **Baeldung**: [ConcurrentHashMap Internals](https://www.baeldung.com/java-concurrent-map)
