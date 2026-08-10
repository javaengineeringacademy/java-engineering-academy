# Queue Interface References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.Queue](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Queue.html)
- **Java Collections Framework Tutorial**: [Queue Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces.html)

## Official Source Code

- **OpenJDK 21**: [Queue.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/Queue.java)
- **OpenJDK 21**: [AbstractQueue.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/AbstractQueue.java)

## Language Specification

- **JLS §4.10.3**: [Queue Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)

## Version History

- **Java 1.5**: Queue and Deque interfaces introduced (subinterfaces of Collection)
- **Java 5**: PriorityBlockingQueue, ArrayBlockingQueue, LinkedBlockingQueue added
- **Java 7**: TransferQueue added
- **Java 8**: Default methods, stream() support
- **Java 9**: List.of(), Set.of() factory methods
- **Java 19**: Virtual Threads (Project Loom — affects queue-based producer-consumer patterns)

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 80: Prefer executors to threads
- **Java Concurrency in Practice** — Chapter 5: Blocking Queues
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Queues

## JEPs (Java Enhancement Proposals)

- **JEP 193**: Variable Handles (affects internal CAS operations)
- **JEP 391**: Foreign Function & Memory API (future memory-mapped queues)

## Additional References

- **Baeldung**: [Java Queue Tutorial](https://www.baeldung.com/java-queue)
- **Baeldung**: [Queue Implementations Comparison](https://www.baeldung.com/java-queue-implementations)
