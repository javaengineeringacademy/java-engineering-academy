# 11.10 Hamcrest Matchers

## 1. Introduction

Hamcrest is a matcher framework that provides a library of reusable matchers for test assertions. It enables expressive, readable tests with self-documenting assertions.

## 2. Learning Objectives

- Use Hamcrest matchers for expressive assertions
- Chain matchers with logical combinators
- Create custom matchers
- Understand Hamcrest integration with JUnit
- Compare Hamcrest with AssertJ

## 3. Prerequisites

- JUnit 5 basics
- Understanding of assertions

## 4. Why This Concept Exists

Hamcrest provides:
- Self-documenting assertions
- Reusable matcher library
- Composable matchers
- Better error messages
- Integration with multiple frameworks

## 5. Problem Statement

How do we write assertions that read like natural language and provide clear failure messages?

## 6. Theory

### Core Matchers

| Matcher | Purpose |
|---------|---------|
| is() | Delegates to another matcher |
| equalTo() | Exact equality |
| hasItem() | Collection contains item |
| hasSize() | Collection size |
| containsString() | String contains substring |
| greaterThan() | Numeric comparison |
| nullValue() / notNullValue() | Null checks |
| instanceOf() | Type checking |
| allOf() | Logical AND |
| anyOf() | Logical OR |
| not() | Logical NOT |

### Matcher Interface

```java
public interface Matcher<T> extends SelfDescribing {
    boolean matches(Object item);
    void describeMismatch(Object item, Description mismatchDescription);
    void describeTo(Description description);
}
```

### Custom Matchers

```java
public static Matcher<String> isEmail() {
    return new TypeSafeMatcher<>() {
        @Override
        protected boolean matchesSafely(String item) {
            return item.contains("@");
        }
        @Override
        protected void describeMismatchSafely(String item, Description mismatch) {
            mismatch.appendText("was ").appendValue(item);
        }
        @Override
        public void describeTo(Description description) {
            description.appendText("an email address");
        }
    };
}
```

## 7. Internal Working

### Matcher Execution

1. assertThat() calls matcher.matches(actual)
2. If false: matcher.describeMismatch() called
3. AssertionError thrown with mismatch description
4. If true: assertion passes

### Composable Matchers

```
allOf(matcher1, matcher2)
    ↓
matcher1.matches(item) && matcher2.matches(item)
```

## 8. JVM Perspective

- Matchers run in test JVM
- No additional overhead
- Matchers are stateless
- Reusable across tests

## 9. Memory Representation

```
Hamcrest Memory Model:
┌─────────────────────────────────────┐
│           Heap Memory               │
│  - Matcher instances                │
│  - Description builder              │
│  - Actual value reference           │
├─────────────────────────────────────┤
│          Stack Memory               │
│  - assertThat() frame               │
│  - matcher.matches() frame          │
│  - describeTo() frame               │
└─────────────────────────────────────┘
```

## 10. Easy Example

```java
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class HamcrestBasicTest {

    @Test
    void shouldUseBasicMatchers() {
        assertThat(2 + 3, is(5));
        assertThat("hello", containsString("ell"));
        assertThat(true, is(true));
    }
}
```

## 11. Medium Example

```java
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;

class HamcrestCollectionTest {

    @Test
    void shouldAssertCollections() {
        List<String> list = List.of("a", "b", "c");

        assertThat(list, hasSize(3));
        assertThat(list, hasItem("a"));
        assertThat(list, not(hasItem("d")));
    }

    @Test
    void shouldAssertStrings() {
        assertThat("Hello World", startsWith("Hello"));
        assertThat("Hello World", endsWith("World"));
        assertThat("Hello World", containsString("llo"));
    }
}
```

## 12. Hard Example

```java
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.Map;

class HamcrestAdvancedTest {

    @Test
    void shouldAssertComplexConditions() {
        assertThat(5, allOf(greaterThan(3), lessThan(10)));
        assertThat("test@test.com", isEmail());
        assertThat(Map.of("a", 1), hasEntry("a", 1));
    }

    @Test
    void shouldUseCustomMatcher() {
        assertThat("user@example.com", isEmail());
        assertThat("invalid", not(isEmail()));
    }

    static Matcher<String> isEmail() {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(String item) {
                return item != null && item.contains("@") && item.contains(".");
            }
            @Override
            protected void describeMismatchSafely(String item, Description mismatch) {
                mismatch.appendText("was ").appendValue(item);
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("an email address");
            }
        };
    }
}
```

## Interview Questions

1. **What is Hamcrest?**
   Hamcrest is a matcher framework for writing expressive, self-documenting test assertions.

2. **Why use Hamcrest over JUnit assertions?**
   Better readability, composable matchers, and descriptive failure messages.

3. **How do you create a custom matcher?**
   Extend TypeSafeMatcher and implement matchesSafely(), describeMismatchSafely(), and describeTo().

4. **What is the difference between is() and equalTo()?**
   is() is a decorator that delegates to another matcher; equalTo() checks exact equality.

5. **How do you combine matchers?**
   Use allOf() for AND, anyOf() for OR, and not() for negation.
