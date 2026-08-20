# Decision: Hamcrest

## When to Use Hamcrest

**Use Hamcrest when:**
- Self-documenting assertions are needed
- Complex conditions with multiple matchers
- Custom matchers for domain-specific assertions
- Legacy test code uses Hamcrest

**Use AssertJ when:**
- Fluent API is preferred
- Rich collection/string assertions
- Modern Java projects
- Team prefers chaining style

## Matcher Selection

| Need | Matcher |
|------|---------|
| Equality | equalTo() |
| Collection size | hasSize() |
| String contains | containsString() |
| Type check | instanceOf() |
| Null check | nullValue() |
| Numeric comparison | greaterThan(), lessThan() |

## Custom Matcher Guidelines

1. Extend TypeSafeMatcher for type safety
2. Implement matchesSafely() for logic
3. Implement describeMismatchSafely() for errors
4. Implement describeTo() for expected description
