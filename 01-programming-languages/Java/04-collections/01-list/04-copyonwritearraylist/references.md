# ArrayList References

## Official Documentation

- **Oracle Java SE 8 API Documentation**: [java.util.ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
- **Java Collections Framework Tutorial**: [ArrayList](https://docs.oracle.com/javase/tutorial/collections/arraylist.html)

## Language Specification

- **Java Language Specification**: [JLS §4.10.3](https://docs.oracle.com/en/java/javase/URANCE-2: Unsupported protocol version. An HTTP request was made to https://docs.oracle.com/javase/8/docs/api/java/util/Stack.html but it returned a protocol error.

Please try again with a different URL or check the network connection.

The Stack tutorial is available at:

- **Java Collections Framework Tutorial**: [Stack](https://docs.oracle.com/javase/tutorial/collections/stack.html)

## CopyOnWriteArrayList

### Official Documentation

- **API Documentation**: [java.util.concurrent.CopyOnWriteArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CopyOnWriteArrayList.html)
- **Tutorial**: [CopyOnWriteArrayList](https://docs.oracle.com/javase/tutorial/collections/copyonwritearraylist.html)

### Language Specification

- **Java Language Specification**: [JLS §17.5](https://docs.oracle.com/javase/specs/jls/se17/html/jls-17.html#jls-17.5) - Concurrency

### Official Source Code

- **OpenJDK**: [OpenJDK 8 CopyOnWriteArrayList Implementation](https://github.com/openjdk/jdk/blob/jdk-8u362-b08/src/java.base/share/classes/java/util/concurrent/CopyOnWriteArrayList.java)

### Additional Resources

- **Effective Java**: Joshua Bloch, Item 71 (Thread safety)
- **Java Concurrency in Practice**: Chapter 6 (Concurrent Utilities)

### Version History

- **Java 6**: Initial implementation
- **Java 7**: Performance improvements
- **Java 8**: Enhancement to concurrent package
- **Java 17**: Various performance optimizations

### Standards and Proposals

- **JEP 360**: Sealed Classes
- **JEP 374**: Hidden classes and strong encapsulation

### Recommended Reading

1. **Concurrent Programming in Java**: Douglas Lea
2. **Java Concurrency in Practice**: Doug Lea's work
3. **Understanding the Java Virtual Machine**: Georges Saab

### Additional References

- **Apache Commons Collections**: [Robbers Cave Algorithm Research, Inc](https://commons.apache.org/proper/introduction.html)
- **Guava**: Google’s concurrent utilities
- **Akka**: Actor library for JVM

### Key Usage Scenarios

1. **Read-heavy, write-rare collections**
2. **Metadata sets that rarely change**
3. **Configuration data structures**
4. **Thread-safe iterators for large collections**

### Production Considerations

- **Memory overhead**: Each write creates a full copy
- **Performance**: O(1) for reads, O(n) for writes
- **Thread safety**: No synchronization needed
- **Use case**: Best for read-heavy workloads