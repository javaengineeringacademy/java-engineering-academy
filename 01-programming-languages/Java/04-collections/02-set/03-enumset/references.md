# EnumSet References

## Official Documentation

- **Oracle Java SE 21 API**: [java.util.EnumSet](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/EnumSet.html)
- **Java Collections Framework Tutorial**: [EnumSet](https://docs.oracle.com/javase/tutorial/collections/enumsets.html)

## Official Source Code

- **OpenJDK 21**: [EnumSet.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/EnumSet.java)
- **OpenJDK 21**: [RegularEnumSet.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/RegularEnumSet.java) — bit-vector implementation
- **OpenJDK 21**: [JumboEnumSet.java](https://github.com/openjdk/jdk/blob/jdk-21.0.1/src/java.base/share/classes/java/util/JumboEnumSet.java) — for > 64 enum constants

## Language Specification

- **JLS §4.10.3**: [Set Hierarchy](https://docs.oracle.com/en/java/javase/17/docs/specs/jls/4.html#4.10.3)
- **JLS §8.9**: Enums (affects EnumSet internals)

## Version History

- **Java 5**: EnumSet introduced alongside Generics and Enums
- **Java 9**: Set.of() factory methods

## Recommended Reading

- **Effective Java (3rd Ed)** — Item 34: Use enums instead of int constants
- **Effective Java (3rd Ed)** — Item 36: Use EnumSet instead of bit fields
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Enums

## Additional References

- **Baeldung**: [Java EnumSet Guide](https://www.baeldung.com/java-enum-set)
- **OpenJDK Source**: Search `universe` for internal Enum universe array
