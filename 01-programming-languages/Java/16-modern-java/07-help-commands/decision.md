# Help Commands - Decision Guide

## Use Help Commands When

### Debugging Issues
```bash
jstack <pid>         # Thread dumps
jmap -heap <pid>     # Memory analysis
jinfo <pid>          # Configuration info
```

### Monitoring Applications
```bash
jcmd <pid> GC.run    # Force garbage collection
jcmd <pid> VM.flags  # Show JVM configuration
jcmd <pid> JFR.start # Start flight recorder
```

### Learning Java
```bash
jshell               # Interactive Java REPL
java --help          # Command-line options
javac --help         # Compiler options
```

### Packaging Applications
```bash
jar tf app.jar       # Inspect jar contents
jar xf app.jar       # Extract jar contents
jar cf app.jar .     # Create jar file
```

## Common Scenarios

### Application Won't Start
```bash
java -verbose:class -jar app.jar  # Show class loading
java -XshowSettings:all -jar app.jar  # Show all settings
```

### Memory Issues
```bash
jmap -heap <pid>     # Heap summary
jmap -dump:format=b,file=heap.hprof <pid>  # Heap dump
jhat heap.hprof      # Analyze heap dump
```

### Performance Issues
```bash
jstack <pid>         # Thread dumps
jcmd <pid> PerfCounter.print  # Performance counters
jcmd <pid> JFR.start duration=60s  # Flight recording
```

## Best Practices

1. **Start with --help** - Most commands have detailed help
2. **Use jshell for experimentation** - Quick code testing
3. **Check version first** - Ensure compatible features
4. **Use verbose flags sparingly** - They can be overwhelming
