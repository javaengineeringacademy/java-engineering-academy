# Best Practices — JVM Internals

## How JVM Enforces (or Doesn't Enforce) Best Practices

The JVM doesn't enforce exception best practices. It's the compiler and bytecode verifier:

```
Best Practice                    JVM Enforcement
─────────────────────────────────────────────────
Don't catch Exception            ❌ None (compiler allows)
Don't swallow exceptions         ❌ None (empty catch is valid bytecode)
Use specific exceptions          ❌ None (catch Throwable is valid)
Don't throw in finally           ❌ None (finally always executes)
Log before rethrowing            ❌ None (no logging requirement)
```

## Bytecode Verification

The JVM verifies:
- Exception handler ranges are valid
- Catch blocks target valid instruction offsets
- No fall-through into catch blocks

It does NOT verify:
- Whether exceptions are meaningful
- Whether catch blocks do useful work
- Whether logging occurs

## JIT and Anti-Patterns

```
// JIT doesn't care about best practices
catch (Exception e) {
    // Empty catch block — JIT compiles this as a no-op
    // No warning, no error, just optimized away
}
```

## Reflection for Validation

```java
// You can enforce best practices via reflection
Method m = cls.getMethod("process");
Class<?>[] exceptions = m.getExceptionTypes();
// Check if method declares overly broad exceptions
```

## Key Insight

The JVM is agnostic about exception best practices. It provides the mechanism (try-catch-finally, throw, throws) but doesn't enforce how you use it. Best practices are conventions, not runtime rules.
