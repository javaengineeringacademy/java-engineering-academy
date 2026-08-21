# Decision: Custom Annotations

## When to Use Custom Annotations

**Use when:**
- Marking code for framework processing (like Spring @Service)
- Creating domain-specific metadata (like @JsonProperty)
- Implementing compile-time code generation (like Lombok @Data)
- Building configuration systems (like @ConfigValue)
- Creating validation frameworks (like @NotNull)

**Avoid when:**
- Simple comments or javadoc would suffice
- The annotation adds no processing value
- You cannot write a processor for it

## Decision Matrix

| Retention | Use When | Example |
|-----------|----------|---------|
| SOURCE | Compile-time only, discarded after | @Override, @SuppressWarnings |
| CLASS | Byte-time processing, not at runtime | Lombok, code generation |
| RUNTIME | Framework reflection processing | @Autowired, @Test |
