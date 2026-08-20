# Hamcrest - Internals

## Matcher Interface

```java
public interface Matcher<T> extends SelfDescribing {
    boolean matches(Object item);
    void describeMismatch(Object item, Description mismatchDescription);
    void describeTo(Description description);
}
```

## Matcher Execution Flow

1. assertThat(actual, matcher) called
2. matcher.matches(actual) evaluated
3. If false:
   - Description built via describeTo()
   - Mismatch built via describeMismatch()
   - AssertionError thrown with combined description
4. If true: assertion passes

## Composable Matcher Internals

```
allOf(m1, m2, m3)
    ↓
New AllOf matcher containing [m1, m2, m3]
    ↓
matches(item) → m1.matches(item) && m2.matches(item) && m3.matches(item)
```

## Type Safety

TypeSafeMatcher provides:
- Compile-time type checking via generics
- Automatic null handling
- Type mismatch detection
- Safe casting of actual value
