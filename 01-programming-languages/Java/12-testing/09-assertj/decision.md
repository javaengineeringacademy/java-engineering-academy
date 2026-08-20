# Decision: AssertJ

## When to Use AssertJ

**Use AssertJ when:**
- Tests need readable, fluent assertions
- Complex object/collection assertions are needed
- Better error messages are desired
- String assertions are frequent

**Use JUnit assertions when:**
- Simple tests with basic assertions
- Minimizing dependencies
- Team prefers static assertions

## Migration Strategy

| From JUnit | To AssertJ |
|------------|-----------|
| assertEquals | assertThat().isEqualTo() |
| assertTrue | assertThat().isTrue() |
| assertThrows | assertThatThrownBy() |
| assertNull | assertThat().isNull() |

## Best Practices

1. Use assertThat() for all assertions
2. Chain related assertions
3. Use describedAs() for custom messages
4. Prefer isInstanceOf() over class checks
5. Use extracting() for complex objects
