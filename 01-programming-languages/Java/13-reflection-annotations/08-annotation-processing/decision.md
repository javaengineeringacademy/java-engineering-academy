# Decision: Annotation Processing

## When to Use Annotation Processing

**Use when:**
- Generating boilerplate code at compile time (Lombok-style)
- Creating type-safe builders and mappers
- Validating code at compile time
- Generating configuration files from annotations
- Building code generation frameworks

**Avoid when:**
- Runtime reflection processing is sufficient
- The logic is simple enough for a utility method
- Compile-time errors are not helpful

## Decision Matrix

| Processing Type | When to Use | Example |
|-----------------|-------------|---------|
| Compile-time (APT) | Generate source code, validate at compile time | Lombok, Dagger 2 |
| Runtime reflection | Read annotations to make runtime decisions | Spring, JUnit |
| Bytecode manipulation | Modify existing bytecode | AspectJ, Mockito |
