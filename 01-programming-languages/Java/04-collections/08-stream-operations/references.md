# Stream Operations References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.stream](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html)
- **Java Streams Tutorial**: [Oracle Streams Tutorial](https://docs.oracle.com/javase/tutorial/collections/streams/)

## Official Source Code

- **OpenJDK 21**: [Stream.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/stream/Stream.java)
- **OpenJDK 21**: [Collectors.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/stream/Collectors.java)

## Language Specification

- **JLS §15.27.4**: Lambda Expressions (affects stream operations)
- **JLS §15.12**: Method Invocation Expressions (affects method references)

## Version History

- **Java 8**: Streams API introduced (JEP 107)
- **Java 9**: ofNullable(), takeWhile(), dropWhile() added
- **Java 10**: copyOf() for immutable collections
- **Java 11**: toArray(IntFunction) improvement
- **Java 16**: toList() returns immutable list
- **Java 16**: Stream.toList() added
- **Java 19**: Virtual Threads (Project Loom — affects parallel streams)

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 45: Use streams judiciously
- **Effective Java (3rd Ed)** — Item 46: Prefer side-effect-free functions in streams
- **Effective Java (3rd Ed)** — Item 47: Return collection of zero length, not null
- **Java Concurrency in Practice** — Chapter 7: Cancellation and Shutdown

## JEPs (Java Enhancement Proposals)

- **JEP 107**: Bulk Data Operations for Collections (Java 8)
- **JEP 269**: Convenience Factory Methods for Collections (Java 9)
- **JEP 323**: Local Variable Syntax for Lambda Parameters (Java 11)

## Additional References

- **Baeldung**: [Java Streams Guide](https://www.baeldung.com/java-streams)
- **Baeldung**: [Java Stream Operations](https://www.baeldung.com/java-stream-operations)
- **Baeldung**: [Java Stream Collectors](https://www.baeldung.com/java-stream-collectors)
- **Baeldung**: [Java Parallel Streams](https://www.baeldung.com/java-parallel-streams)
