# Internals: Annotation Processing

## How Compile-Time Annotation Processing Works

### The Processing Pipeline

```
Source Code → javac → APT finds processors → process() called per round
                                                  ↓
                                          Generate new source/class files
                                                  ↓
                                          New annotations found? → Another round
                                          No new annotations → Done
```

### Processing Rounds

1. **Round 1:** Process all annotations in the source tree
2. **Round 2+:** Process annotations generated in previous rounds
3. **Final round:** When no new annotations are generated

### Processor Registration

Processors are registered via `META-INF/services/javax.annotation.processing.Processor`:
```
com.example.MyProcessor
com.example.AnotherProcessor
```

### Filer API

The `Filer` creates new files:
- `createSourceFile()` — New .java file
- `createClassFile()` — New .class file
- `createResource()` — New resource file

### Element Types

Processed elements are typed:
- `TypeElement` — Class/interface/enum
- `ExecutableElement` — Method/constructor
- `VariableElement` — Field/parameter
- `PackageElement` — Package

### Error Reporting

Use `Messager` to report errors:
```java
messager.printMessage(Diagnostic.Kind.ERROR, "Invalid annotation", element);
```
