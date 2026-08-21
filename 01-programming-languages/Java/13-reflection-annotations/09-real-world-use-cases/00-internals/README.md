# Internals: Real-World Use Cases

## How Major Frameworks Use Reflection

### Spring Framework

Spring uses reflection for:
1. **Component scanning** — Finds @Component, @Service, @Repository classes
2. **Dependency injection** — Reads @Autowired fields/constructors
3. **AOP proxies** — Creates dynamic proxies for @Transactional
4. **Event handling** — Discovers @EventListener methods
5. **Configuration** — Processes @Configuration, @Bean classes

The reflection data is cached at startup to minimize runtime overhead.

### Hibernate/JPA

Hibernate uses reflection for:
1. **Entity mapping** — Reads @Entity, @Table, @Column annotations
2. **Field access** — Reads/writes field values for SQL generation
3. **Lazy loading** — Creates proxies for collection associations
4. **Lifecycle callbacks** — Invokes @PrePersist, @PostLoad methods

### Jackson

Jackson uses reflection for:
1. **Property discovery** — Finds getters, fields for serialization
2. **Annotation processing** — Reads @JsonProperty, @JsonIgnore
3. **Type handling** — Resolves generic types for collections
4. **Creator detection** — Finds @JsonCreator constructors/factories

### JUnit

JUnit uses reflection for:
1. **Test discovery** — Finds methods annotated with @Test
2. **Lifecycle** — Invokes @BeforeEach, @AfterEach methods
3. **Assertions** — Calls assertion methods via reflection
4. **Parameterized tests** — Resolves parameter sources

### Lombok

Lombok uses annotation processing (NOT runtime reflection):
1. **@Data** — Generates getters, setters, toString, equals, hashCode
2. **@Builder** — Generates builder pattern classes
3. **@Slf4j** — Generates logger field
4. **@Value** — Generates immutable classes

Zero runtime cost because everything is generated at compile time.
