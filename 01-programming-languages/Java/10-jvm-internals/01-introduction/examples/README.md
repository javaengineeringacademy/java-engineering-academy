# 01. JVM Introduction - Examples

## Example Files

| File | Description |
|------|-------------|
| `JvmOverview.java` | JVM architecture demonstration covering class loading, memory areas, and runtime info |
| `JvmStartup.java` | JVM startup sequence, lifecycle states, shutdown hooks, and class initialization order |

## Running Examples

```bash
# Compile
javac JvmOverview.java
javac JvmStartup.java

# Run
java JvmOverview
java JvmStartup
```

## Key Concepts Demonstrated

- Class loader hierarchy (Bootstrap, Platform, Application)
- Heap memory usage (max, total, free)
- JVM runtime information (version, vendor, OS)
- Shutdown hook registration
- Class initialization order (static → instance → constructor)
