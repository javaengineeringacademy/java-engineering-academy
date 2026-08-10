# BlockingQueue References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.concurrent.BlockingQueue](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/BlockingQueue.html)
- **Java Concurrency Tutorial**: [Blocking Queues](https://docs.oracle.com/javase/tutorial/essential/concurrency/blockingques.html)

## Official Source Code

- **OpenJDK 21**: [BlockingQueue.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/concurrent/BlockingQueue.java)
- **OpenJDK 21**: [ArrayBlockingQueue.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/concurrent/ArrayBlockingQueue.java)
- **OpenJDK 21**: [LinkedBlockingQueue.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/concurrent/LinkedBlockingQueue.java)

## Language Specification

- **JLS §4.10.3**: [Queue Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)
- **JLS §17.4**: Happens-before Order (affects blocking visibility)

## Version History

- **Java 5**: BlockingQueue introduced as part of java.util.concurrent
- **Java 5**: ArrayBlockingQueue (bounded), LinkedBlockingQueue (optionally bounded), PriorityBlockingQueue
- **Java 7**: SynchronousQueue, LinkedTransferQueue added
- **Java 8**: forEach() default method
- **Java 19**: Virtual Threads (Project Loom — affects producer-consumer patterns)

## Recommended Reading

- **Java Concurrency in Practice** — Chapter 5: Blocking Queues (core reference)
- **Effective Java (3rd Ed)** — Item 80: Prefer executors to threads
- **Core Java, Vol. II** — Cay S. Horstmann, Chapter on Concurrent Collections

## JEPs (Java Enhancement Proposals)

- **JEP 193**: Variable Handles (affects internal CAS operations)
- **JEP 391**: Foreign Function & Memory API (future memory-mapped queues)

## Additional References

- **Baeldung**: [Java BlockingQueue Guide](https://www.baeldung.com/java-blocking-queue)
- **Baeldung**: [Producer Consumer with BlockingQueue](https://www.baeldung.com/producer-consumer-queue)
