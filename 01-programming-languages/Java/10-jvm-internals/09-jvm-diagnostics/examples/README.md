# 09. JVM Diagnostics - Examples

## Example Files

| File | Description |
|------|-------------|
| *(See practices/ and solutions/ for runnable examples)* | Thread dump, heap dump, and diagnostics exercises |

## Running Examples

```bash
# Capture thread dump
jstack <pid>

# Capture heap dump
jmap -dump:live,format=b,file=heap.hprof <pid>

# Monitor GC statistics
jstat -gc <pid> 1000

# Use jcmd
jcmd <pid> VM.flags
jcmd <pid> Thread.print
jcmd <pid> GC.heap_info
```

## Key Concepts Demonstrated

- Thread dump capture and analysis
- Heap dump generation and analysis
- GC statistics monitoring
- Diagnostic tool usage
