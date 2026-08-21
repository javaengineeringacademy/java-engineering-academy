# Decision: Reflection & Annotations

## When to Use Reflection

**Use Reflection when:**
- Building frameworks (Spring, Hibernate)
- Need to access private fields/methods
- Implementing dynamic proxies
- Processing annotations at runtime
- Creating serialization/deserialization libraries

**Avoid Reflection when:**
- Simple, straightforward code works
- Performance is critical
- Compile-time safety is required
- Testing is difficult

## Annotation Use Cases

| Use Case | Example |
|----------|---------|
| Override checking | `@Override` |
| Deprecation | `@Deprecated` |
| Suppression warnings | `@SuppressWarnings` |
| Testing | `@Test`, `@BeforeEach` |
| Dependency injection | `@Autowired`, `@Inject` |
| Configuration | `@Value`, `@Configuration` |
| Validation | `@NotNull`, `@Size` |
| Mapping | `@Column`, `@Table` |

## Frameworks Using Reflection

| Framework | Usage |
|-----------|-------|
| Spring | Dependency injection, AOP |
| Hibernate | ORM mapping |
| Jackson | JSON serialization |
| JUnit | Test discovery |
| Mockito | Mock creation |
| Lombok | Code generation |

## Further Reading

- [Oracle Reflection Tutorial](https://docs.oracle.com/javase/tutorial/reflect/)
- [Baeldung Reflection](https://www.baeldung.com/java-reflection)
