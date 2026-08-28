# Help Commands Internals

## Command Implementation

Java commands are implemented as native methods and Java classes:

### java command
- Launcher for Java applications
- Handles classpath, JVM options, and main class
- Delegates to JVM for execution

### javac command
- Java compiler implemented in Java
- Uses parser, type checker, and code generator
- Produces .class files from .java files

### jar command
- Built on java.util.jar package
- Handles ZIP format for jar files
- Supports manifests and signatures

### jshell
- Interactive Java REPL
- Uses compiler API for real-time compilation
- Supports code completion and documentation

## Diagnostic Tools

### jcmd
- Uses JVM's diagnostic command interface
- Accesses internal JVM state
- Requires running JVM process

### jmap
- Uses HotSpot-specific APIs
- Can dump heap to hprof format
- Requires attach API permissions

### jstack
- Uses HotSpot's thread dump capability
- Captures thread states and stack traces
- Can detect deadlocks

## Performance Impact

| Tool | Impact | Use Case |
|------|--------|----------|
| java -verbose:class | High | Debugging class loading |
| jcmd | Low | Monitoring |
| jmap | High | Memory analysis |
| jstack | Low | Thread analysis |
| jhat | High | Heap analysis |
