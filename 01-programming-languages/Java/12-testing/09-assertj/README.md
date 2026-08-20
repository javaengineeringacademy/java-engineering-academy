# 11.9 AssertJ Fluent Assertions

## 1. Introduction

AssertJ provides a rich set of fluent assertions for Java, making test assertions more readable and expressive. It integrates seamlessly with JUnit 5 and provides better error messages than standard assertions.

## 2. Learning Objectives

- Use AssertJ's fluent API for readable assertions
- Assert collections, strings, exceptions, and objects
- Chain assertions for complex validations
- Customize assertion messages
- Compare AssertJ with JUnit assertions

## 3. Prerequisites

- JUnit 5 basics
- Understanding of assertions

## 4. Why This Concept Exists

AssertJ solves problems with standard assertions:
- Verbose and hard to read
- Poor error messages
- Limited collection support
- No fluent chaining
- Manual null checks

## 5. Problem Statement

How do we write clear, expressive assertions with informative failure messages?

## 6. Theory

### AssertJ vs JUnit Assertions

| Feature | AssertJ | JUnit Assertions |
|---------|---------|------------------|
| Readability | Fluent chain | Static method calls |
| Error messages | Detailed | Basic |
| Collection support | Extensive | Limited |
| String assertions | Rich | Basic |
| Exception assertions | Fluent | assertThrows only |

### Assertion Categories

1. **Object assertions**: assertThat(obj).isEqualTo(), isNotNull()
2. **String assertions**: assertThat(str).contains(), startsWith()
3. **Collection assertions**: assertThat(list).hasSize(), contains()
4. **Map assertions**: assertThat(map).containsKey(), hasEntry()
5. **Exception assertions**: assertThatThrownBy().hasMessage()
6. **Number assertions**: assertThat(num).isGreaterThan(), isBetween()

## 7. Internal Working

### Fluent API Design

```
assertThat(actual)
    .describedAs("description")
    .isEqualTo(expected)
    .isNotNull()
    .isInstanceOf(Type.class)
```

Each method returns the assertion object for chaining.

### Error Message Generation

AssertJ generates descriptive error messages:

```
Expected: <5> but was: <3>
```

vs JUnit's:
```
expected: <5> but was: <3>
```

## 8. JVM Perspective

- AssertJ runs in same JVM as tests
- No additional memory overhead
- Assertions are inline methods
- No proxy or reflection usage

## 9. Memory Representation

```
AssertJ Memory Model:
┌─────────────────────────────────────┐
│           Heap Memory               │
│  - AssertJ assertion objects        │
│  - Actual value reference           │
│  - Error message builder            │
├─────────────────────────────────────┤
│          Stack Memory               │
│  - Assertion method frames          │
│  - Chaining method calls            │
└─────────────────────────────────────┘
```

## 10. Easy Example

```java
import static org.assertj.core.api.Assertions.*;

class AssertJBasicTest {

    @Test
    void shouldUseBasicAssertions() {
        assertThat(2 + 3).isEqualTo(5);
        assertThat("hello").isNotEmpty();
        assertThat(true).isTrue();
    }
}
```

## 11. Medium Example

```java
import static org.assertj.core.api.Assertions.*;
import java.util.List;

class AssertJCollectionTest {

    @Test
    void shouldAssertCollections() {
        List<String> list = List.of("a", "b", "c");

        assertThat(list)
            .hasSize(3)
            .contains("a", "b")
            .doesNotContain("d")
            .startsWith("a");
    }

    @Test
    void shouldAssertStrings() {
        assertThat("Hello World")
            .contains("World")
            .startsWith("Hello")
            .endsWith("World")
            .hasSize(11);
    }
}
```

## 12. Hard Example

```java
import static org.assertj.core.api.Assertions.*;
import java.util.Map;

class AssertJAdvancedTest {

    @Test
    void shouldAssertComplexObjects() {
        User user = new User("Alice", 25, "alice@example.com");

        assertThat(user)
            .extracting("name", "age", "email")
            .containsExactly("Alice", 25, "alice@example.com");
    }

    @Test
    void shouldAssertExceptions() {
        assertThatThrownBy(() -> {
            throw new IllegalArgumentException("Invalid input");
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid input");
    }

    @Test
    void shouldAssertMaps() {
        Map<String, Integer> map = Map.of("a", 1, "b", 2);

        assertThat(map)
            .hasSize(2)
            .containsKey("a")
            .containsEntry("b", 2);
    }
}
```

## Interview Questions

1. **What is AssertJ?**
   AssertJ is a Java assertion library providing fluent, readable assertions with better error messages.

2. **Why use AssertJ over JUnit assertions?**
   Better readability, richer API, better error messages, and extensive collection/string support.

3. **How do you assert collections with AssertJ?**
   Use assertThat(list).hasSize(), contains(), doesNotContain(), etc.

4. **How do you assert exceptions?**
   Use assertThatThrownBy(() -> { ... }).isInstanceOf().hasMessage().

5. **Is AssertJ thread-safe?**
   Yes, assertions are stateless and thread-safe.
