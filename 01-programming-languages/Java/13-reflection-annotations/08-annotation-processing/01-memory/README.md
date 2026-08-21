# Memory: Annotation Processing

## Compile-Time Processing Memory

### During Processing

Each annotation processor maintains:
- Processing environment: ~100 bytes
- Element mirrors: ~50-100 bytes per element
- Generated file buffers: Variable

### Generated Code

Generated source files are:
- Written to disk by Filer
- Compiled by javac in subsequent rounds
- No persistent memory cost after compilation

## Runtime Annotation Memory

### Runtime Annotations

Each runtime annotation instance:
- Proxy object: ~40-60 bytes
- Element values: ~8-16 bytes per element

### Class Annotation Data

The Class object caches annotation data:
```java
// Internal: annotationData field in Class
// SoftReference allows GC under memory pressure
// Re-parsed from bytecode if reclaimed
```

## Performance Considerations

### Annotation Processing Time

| Phase | Time | Notes |
|-------|------|-------|
| Round 1 | Depends on annotations | Most work happens here |
| Round 2+ | Usually fast | Generated code has fewer annotations |
| Compilation | Standard javac | Generated code is normal Java |

### Runtime Annotation Reading

```java
// Fast: checks cached data
clazz.isAnnotationPresent(MyAnnotation.class)

// Slower: creates proxy object
MyAnnotation ann = clazz.getAnnotation(MyAnnotation.class)
```

## Best Practices

1. Use CLASS retention when runtime reading is not needed
2. Minimize annotation element sizes
3. Cache annotation reading results
4. Use SOURCE retention for compile-time-only processing
