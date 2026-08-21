# Decision: Introduction to Reflection

## When to Use Reflection

**Use Reflection when:**
- Building frameworks that work with user-defined types (Spring, Hibernate, JUnit)
- Creating plugin architectures that load classes dynamically
- Implementing dependency injection containers
- Writing serialization/deserialization libraries
- Building AOP (Aspect-Oriented Programming) proxies
- Creating test utilities that access private members

**Avoid Reflection when:**
- Business logic with known types at compile time
- Performance-critical code paths (10-50x slower than direct access)
- Simple method delegation on known types
- Compile-time verification is required
- Security-sensitive code that should not bypass access controls

## Decision Matrix

| Scenario | Use Reflection? | Alternative |
|----------|----------------|-------------|
| Framework development | Yes | N/A - reflection is the tool |
| Plugin loading | Yes | ServiceLoader (limited) |
| DI container | Yes | N/A |
| Business logic | No | Direct type access |
| Hot loop processing | No | MethodHandle or direct calls |
| Testing private members | Maybe | Consider making package-private |
| JSON serialization | Yes | Code generation (Lombok style) |
| Simple getter/setter | No | Direct access |

## Cost-Benefit Analysis

```
Do you know the type at compile time?
├── YES → Is this a framework/plugin use case?
│         ├── YES → Use reflection for discovery
│         └── NO  → Can you use interfaces/generics?
│                   ├── YES → Prefer compile-time safety
│                   └── NO  → Use reflection, document why
└── NO  → Use reflection. Cache Class/Method/Field objects.
          Document the performance tradeoff.
```

## Further Reading

- [Oracle Reflection Tutorial](https://docs.oracle.com/javase/tutorial/reflect/)
- [Baeldung Java Reflection](https://www.baeldung.com/java-reflection)
