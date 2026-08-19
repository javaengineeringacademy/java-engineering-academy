# 03. Class Loading - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | Class loading phases observation |
| `Solution2.java` | Static initialization order |
| `Solution3.java` | Lazy loading implementation |

## Running Solutions

```bash
javac Solution1.java && java Solution1
javac Solution2.java && java Solution2
javac Solution3.java && java Solution3
```

## Common Mistakes to Avoid

1. **Confusing loadClass() with forName()**: loadClass() does NOT initialize; forName() DOES
2. **Assuming static blocks run at load time**: They run at initialization time
3. **Ignoring circular initialization**: Can cause null values from static fields
4. **Not handling ExceptionInInitializerError**: Static block failures are fatal
