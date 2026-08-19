# 02. ClassLoader - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | ClassLoader hierarchy mapping |
| `Solution2.java` | Custom classloader implementation |
| `Solution3.java` | ClassLoader leak detection |

## Running Solutions

```bash
javac Solution1.java && java Solution1
javac Solution2.java && java Solution2
javac Solution3.java && java Solution3
```

## Common Mistakes to Avoid

1. **Overriding loadClass() instead of findClass()**: This bypasses parent delegation
2. **Not calling findLoadedClass()**: Results in loading the same class multiple times
3. **Forgetting to close resources**: In custom classloaders, override close() to release resources
4. **Static references to classloader**: Prevents garbage collection of the classloader
