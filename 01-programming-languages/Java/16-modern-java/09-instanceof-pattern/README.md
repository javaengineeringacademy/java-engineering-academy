# instanceof Pattern Matching (Java 16)

instanceof Pattern Matching allows you to test and cast an object in a single expression, introducing a pattern variable that can be used directly.

## Key Features

- **Test and cast in one step** - No explicit cast needed
- **Pattern variable** - Introduced variable is final
- **Guarded patterns** - Add conditions with `&&`
- **Scope rules** - Variable is in scope where it's definitely assigned

## Syntax

```java
// Before
if (obj instanceof String) {
    String s = (String) obj;
    // use s
}

// After
if (obj instanceof String s) {
    // use s directly
}

// With guard
if (obj instanceof String s && s.length() > 5) {
    // s is a String with length > 5
}
```

## Pattern Variable Scope

```java
// Valid - s is in scope
if (obj instanceof String s) {
    System.out.println(s.length());
}

// Invalid - s may not be assigned
if (!(obj instanceof String s)) {
    System.out.println(s.length()); // Compilation error
}

// Valid - s is definitely assigned in else branch
if (obj instanceof String s) {
    System.out.println(s.length());
} else {
    // s is not in scope here
}
```

## Rules

1. Pattern variables are effectively final
2. Scope follows definite assignment rules
3. Can use `&&` but not `||` with patterns
4. Pattern variable type must be a reference type

## When to Use

- Type checking followed by casting
- Replacing verbose instanceof + cast
- Complex type conditions with guards
