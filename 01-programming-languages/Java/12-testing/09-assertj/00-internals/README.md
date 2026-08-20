# AssertJ - Internals

## Fluent API Architecture

```
assertThat(actual) → AbstractAssert
    ↓
isEqualTo(expected) → throws AssertionError or returns this
    ↓
isNotNull() → throws AssertionError or returns this
    ↓
Chaining continues...
```

## Assertion Chain

Each method:
1. Evaluates condition
2. If fails: throws AssertionError with message
3. If passes: returns this for chaining

## Error Message Builder

```
assertThat(actual).isEqualTo(expected)
    ↓
if (!actual.equals(expected)) {
    throw new AssertionError(
        String.format("Expected: <%s> but was: <%s>", expected, actual)
    );
}
```

## Type Safety

Generic assertions ensure compile-time type checking:

```java
assertThat("hello")  // AbstractStringAssert
    .contains("ell") // String-specific method

assertThat(list)     // AbstractListAssert
    .hasSize(3)      // Collection-specific method
```
