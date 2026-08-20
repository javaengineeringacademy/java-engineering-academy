# Decision: Code Coverage

## When to Measure Coverage

**Always measure coverage for:**
- New code additions
- Critical business logic
- CI/CD pipelines
- Release quality gates

**Consider skipping for:**
- Simple configuration classes
- Generated code
- Trivial POJOs

## Coverage Targets

| Code Type | Target |
|-----------|--------|
| Business logic | 80-90% |
| Service layer | 70-80% |
| Repository/DAO | 60-70% |
| Controllers | 50-60% |
| Overall | 70-80% |

## Coverage Tools Comparison

| Tool | Pros | Cons |
|------|------|------|
| JaCoCo | Standard, Maven/Gradle | No mutation testing |
| Cobertura | Legacy support | Slower |
| Istanbul | JavaScript focus | Not for Java |

## Exclusion Guidelines

Exclude from coverage:
- DTOs and POJOs
- Configuration classes
- Generated code
- toString/equals/hashCode
