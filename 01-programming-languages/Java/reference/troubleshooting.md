# Java Troubleshooting

> OOM errors, deadlocks, classpath issues, and version conflicts.

## Common Issues Overview

| Issue | Symptom | Quick Fix |
|-------|---------|-----------|
| OutOfMemoryError | Heap/PermGen full | Increase heap or fix leak |
| StackOverflowError | Stack trace printed | Fix recursion or increase stack |
| ClassNotFoundException | NoClassDefFoundError | Fix classpath |
| Deadlock | Thread stuck | Fix lock ordering |
| ClassCastException | Type mismatch | Fix generics |

## OutOfMemoryError

### Java Heap Space

```bash
# Symptom
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space

# Solutions
# 1. Increase heap
-XX:MaxHeapSize=4g
-Xmx4g

# 2. Diagnose
jmap -histo:live <pid> | head -20
jmap -dump:live,format=b,file=heap.hprof <pid>

# 3. Enable heap dump on OOM
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heapdump.hprof
```

### Metaspace

```bash
# Symptom
java.lang.OutOfMemoryError: Metaspace

# Solution
-XX:MaxMetaspaceSize=256m
-XX:+UseCompressedClassPointers

# Diagnose
jcmd <pid> VM.metaspace
```

### GC Overhead

```bash
# Symptom
java.lang.OutOfMemoryError: GC overhead limit exceeded

# Solution: increase heap or fix allocation rate
-XX:MaxHeapSize=8g
-XX:GCTimeRatio=19
```

### Direct Memory

```bash
# Symptom
java.lang.OutOfMemoryError: Direct buffer memory

# Solution
-XX:MaxDirectMemorySize=1g

# Diagnose
jcmd <pid> VM.info
```

## StackOverflowError

```bash
# Symptom
java.lang.OutOfMemoryError: Java stack space

# Solution 1: increase stack size
-Xss2m

# Solution 2: fix recursion
# Bad: infinite recursion
void process() { process(); }

# Good: base case
void process(int n) {
    if (n <= 0) return;
    process(n - 1);
}
```

## Deadlock Detection

```bash
# 1. Thread dump
jstack <pid>

# 2. Find deadlock
"Thread-1" waiting for lock held by "Thread-2"
"Thread-2" waiting for lock held by "Thread-1"

# 3. jcmd
jcmd <pid> Thread.print

# 4. VisualVM detects deadlocks automatically
```

### Deadlock Prevention

```java
// Bad: inconsistent lock ordering
synchronized (lockA) {
    synchronized (lockB) { /* ... */ }
}

// Good: consistent lock ordering
synchronized (lockA) {
    synchronized (lockB) { /* ... */ }
}
// Always acquire locks in same order

// Good: tryLock with timeout
if (lockA.tryLock(1, TimeUnit.SECONDS)) {
    try {
        if (lockB.tryLock(1, TimeUnit.SECONDS)) {
            try {
                // safe access
            } finally {
                lockB.unlock();
            }
        }
    } finally {
        lockA.unlock();
    }
}
```

## ClassNotFoundException

```bash
# Symptom
java.lang.ClassNotFoundException: com.example.MyClass

# Solutions
# 1. Check classpath
echo $CLASSPATH
java -verbose:class -cp myapp.jar com.example.Main

# 2. Check JAR contents
jar tf myapp.jar | grep MyClass

# 3. Check module path (Java 9+)
--module-path /path/to/modules
```

### NoClassDefFoundError

```bash
# Symptom
java.lang.NoClassDefFoundError: com/example/Dependency

# Solutions
# 1. Ensure dependency in classpath
# 2. Check scope (test vs compile)
# 3. Check transitive dependencies
mvn dependency:tree
```

## Version Conflicts

### Dependency Conflict

```bash
# Find conflicts
mvn dependency:tree -Dverbose
gradle dependencies

# Exclude transitive dependency
<dependency>
    <groupId>com.example</groupId>
    <artifactId>lib</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### Java Version Mismatch

```bash
# Symptom
UnsupportedClassVersionError: Bad version number

# Solution: ensure consistent Java version
java -version
javac -version

# Compile for specific version
javac -source 11 -target 11 MyClass.java
```

## ClassLoader Issues

```java
// Symptom: same class loaded by different classloaders
// java.lang.ClassCastException: MyClass cannot be cast to MyClass

// Solution: ensure same classloader
// 1. Check classpath order
// 2. Use parent classloader delegation
// 3. Check for multiple JARs with same class
```

## Thread Issues

### Thread Leak

```bash
# Symptom: increasing thread count
jcmd <pid> Thread.print | grep -c "Thread-"

# Diagnose
jstack <pid> | grep "java.lang.Thread.State" | sort | uniq -c

# Solution: use thread pools
ExecutorService executor = Executors.newFixedThreadPool(10);
```

### High Thread Count

```bash
# Diagnose
jcmd <pid> Thread.print | grep "java.lang.Thread.State" | sort | uniq -c

# Common causes
# 1. Too many threads in pool
# 2. Thread leak
# 3. Excessive thread creation
```

## Logging Issues

```bash
# Symptom: no logs appearing

# Solutions
# 1. Check log level
-Dlogging.level.root=DEBUG

# 2. Check log config file
# logback.xml, log4j2.xml, logging.properties

# 3. Check classpath for log config
# Logback: logback.xml on classpath
# Log4j2: log4j2.xml on classpath
```

## Configuration Issues

```bash
# Symptom: configuration not loading

# Solutions
# 1. Check file location
# Spring: application.yml in classpath root

# 2. Check environment variables
echo $SPRING_PROFILES_ACTIVE

# 3. Check system properties
-Dspring.profiles.active=prod

# 4. Check property precedence
# Command line > environment > application.yml > defaults
```

## Quick Diagnostic Commands

```bash
# System info
jcmd <pid> VM.flags
jcmd <pid> VM.system_properties
jcmd <pid> GC.heap_info

# Thread info
jcmd <pid> Thread.print
jstack <pid>

# Memory info
jmap -heap <pid>
jmap -histo:live <pid>

# Class info
jcmd <pid> VM.classloader_stats
```

## References

- [Java Troubleshooting Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/)
- [JVM Diagnostic Commands](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/diag.html)

---
**Prerequisites:** [Java debugging](debugging.md)
**Related:** [Java pitfalls](pitfalls.md) | [Java production](production.md)
**Next:** [Java migration](migration.md)
