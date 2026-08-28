# Help Commands Memory

## Memory Characteristics

### java command
- Launches JVM with specified memory settings
- Default heap: 256MB initial, 1GB max
- Can be configured with `-Xms` and `-Xmx`

### javac command
- Compiler uses internal memory for parsing
- Memory usage depends on source code size
- Temporary objects for AST and type checking

### Diagnostic Tools
- jstack: Minimal memory impact
- jmap: Can cause GC pauses during heap dump
- jcmd: Low memory overhead

## Common Memory Settings

```bash
# Set initial heap to 512MB
java -Xms512m -jar app.jar

# Set max heap to 2GB
java -Xmx2g -jar app.jar

# Set both
java -Xms512m -Xmx2g -jar app.jar

# Enable GC logging
java -Xlog:gc* -jar app.jar
```

## Memory Analysis

```bash
# Heap dump
jmap -dump:format=b,file=heap.hprof <pid>

# Heap summary
jmap -heap <pid>

# Finalization info
jmap -finalinfo <pid>

# Histogram
jmap -histo <pid>
```

## Best Practices

1. **Monitor memory usage** - Use jstat or JConsole
2. **Set appropriate heap size** - Based on application needs
3. **Use GC logging** - To identify memory issues
4. **Analyze heap dumps** - When memory issues occur
