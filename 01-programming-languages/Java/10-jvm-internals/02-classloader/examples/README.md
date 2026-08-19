# 02. ClassLoader - Examples

## Example Files

| File | Description |
|------|-------------|
| `ClassLoadingDeepDive.java` | Demonstrates parent delegation, class identity, and classloader hierarchy |

## Running Examples

```bash
# Compile
javac ClassLoadingDeepDive.java

# Run
java ClassLoadingDeepDive

# Run with verbose class loading
java -verbose:class ClassLoadingDeepDive
```

## Key Concepts Demonstrated

- Parent delegation model in action
- Class identity across different classloaders
- Bootstrap vs Application ClassLoader detection
- ClassLoader hierarchy traversal
- Thread Context ClassLoader usage
- Class caching behavior (findLoadedClass)
