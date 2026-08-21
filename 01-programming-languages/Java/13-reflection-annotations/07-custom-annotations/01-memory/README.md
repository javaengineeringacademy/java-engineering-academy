# Memory: Custom Annotations

## Memory Cost of Annotations

### Runtime Annotations

Each runtime annotation instance is a proxy object:
- Object header: 16 bytes
- Annotation type reference: 8 bytes
- Element values: 8 bytes per element (references)

For a simple annotation with 2 elements: ~40-60 bytes

### CLASS Retention Annotations

Stored in bytecode but not loaded into runtime objects:
- Contributes to class file size
- Does not consume heap memory

### SOURCE Retention Annotations

Completely discarded after compilation:
- No runtime cost
- No bytecode cost

## Performance Impact

### Reading Annotations

```java
// Fast: checks cached annotation data
clazz.isAnnotationPresent(MyAnnotation.class)

// Slower: creates annotation proxy object
MyAnnotation ann = clazz.getAnnotation(MyAnnotation.class)
```

### Annotation Caching

The JVM caches annotation data in the Class object's `annotationData` field:
```java
// Internal: Class annotationData field
// Contains: AnnotationData with parsed annotations
// Cached until class is garbage collected
```

## Best Practices

1. Use SOURCE retention when possible — no runtime cost
2. Use CLASS retention for bytecode processing — no heap cost
3. Only use RUNTIME retention when reflection processing is needed
4. Avoid storing large data in annotation elements
5. Cache annotation results when reading repeatedly
