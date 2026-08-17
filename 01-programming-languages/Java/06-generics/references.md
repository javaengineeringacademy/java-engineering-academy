# Generics - References

## Official Documentation
- [Java Tutorials: Generics](https://docs.oracle.com/javase/tutorial/java/generics/index.html)
- [Java Language Specification: Generics](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html)
- [Java API: java.lang.reflect.Type](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Type.html)
- [PFE (Java Generics and Collections FAQ)](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeArguments.html)

## Books
- *Java Generics and Collections* (Naftalin & Wadler) - Comprehensive reference
- *Effective Java* (Joshua Bloch) - Items on generics, wildcards, and type safety

## Generic Type Parameters

| Parameter | Convention | Usage |
|-----------|-----------|-------|
| T | Type | General type parameter |
| E | Element | Collection element type |
| K | Key | Map key type |
| V | Value | Map value type |
| N | Number | Numeric types |
| R | Return | Method return type |

## Type Erasure Examples

| Source Code | After Erasure |
|-------------|---------------|
| `List<String>` | `List` |
| `Box<T>` | `Box` |
| `T extends Number` | `Number` |
| `? extends Comparable` | `Comparable` |

## Wildcard Bounds

| Wildcard | Can Read | Can Write | Use Case |
|----------|----------|-----------|----------|
| `?` | Yes | No | Read-only access |
| `? extends T` | Yes (as T) | No | Producer of T |
| `? super T` | Yes (as Object) | Yes (T) | Consumer of T |

## Common Patterns

### Generic Factory
```java
public interface Factory<T> { T create(); }
```

### Generic Builder
```java
class Builder<T> { T build(); }
```

### Type Token
```java
public class TypeReference<T> { Type getType(); }
```

## Related Topics
- [Type Annotations (JEP 308)](https://openjdk.org/jeps/308)
- [Reified Generics Discussion](https://openjdk.org/projects/valhalla/)
- [Pattern Matching and Generics](https://openjdk.org/jeps/441)
