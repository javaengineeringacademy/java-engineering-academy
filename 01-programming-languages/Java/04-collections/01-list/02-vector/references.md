# Vector References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Vector](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Vector.html)
- **Legacy Collections Tutorial**: [Vector Class](https://docs.oracle.com/javase/tutorial/collections/legacy.html)

## Official Source Code

- **OpenJDK 21**: [Vector.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Vector.java)
- **JDK Source Viewer**: Search `synchronized` methods for thread-safety pattern

## Language Specification

- **JLS §4.10.3**: [List Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.0**: Vector introduced (pre-Collections Framework — synchronized)
- **Java 1.2**: Collections Framework — Vector retrofitted to implement List
- **Java 5**: Generics added, synchronized overhead documented
- **Java 9**: Deprecated for removal (due to synchronized overhead)

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 81: Prefer concurrency utilities to wait/notify
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Legacy Collections
- **Java Concurrency in Practice** — Chapter 5: Building Custom Synchronization

## Deprecation Notices

- **Java 9+**: Vector is not deprecated but strongly discouraged — use ArrayList + Collections.synchronizedList or CopyOnWriteArrayList

## Additional References

- **Baeldung**: [Java Vector vs ArrayList](https://www.baeldung.com/java-vector-vs-arraylist)
- **StackOverflow**: [Why is Vector deprecated in Java?](https://stackoverflow.com/questions/13421849/why-is-vector-class-deprecated-in-java)
