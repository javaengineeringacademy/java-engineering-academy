# WeakHashMap References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.WeakHashMap](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/WeakHashMap.html)
- **Java Collections Framework Tutorial**: [WeakHashMap](https://docs.oracle.com/javase/tutorial/collections/impl.html)

## Official Source Code

- **OpenJDK 21**: [WeakHashMap.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/WeakHashMap.java)
- **JDK Source Viewer**: Search `WeakReference` for WeakKey and GC behavior

## Language Specification

- **JLS §4.10.3**: [Map Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)
- **JLS §12.4**: Class Initialization (affects GC-triggered cleanup)

## Version History

- **Java 1.2**: WeakHashMap introduced as part of Collections Framework
- **Java 5**: Generics added
- **Java 8**: forEach() default method

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 7: Eliminate obsolete object references
- **Java Concurrency in Practice** — Chapter 3: Building Blocks (WeakHashMap concurrency caveats)
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Weak References

## Additional References

- **Baeldung**: [Java WeakHashMap Guide](https://www.baeldung.com/java-weak-hashmap)
- **Oracle Documentation**: [Weak, Soft, and Phantom References](https://docs.oracle.com/javase/8/docs/api/java/lang/ref/package-summary.html)
