# javac Annotation Processing

Annotation processing (APT) runs before type-checking and can generate new source files, resource files, or report errors/warnings. It is used by frameworks like Lombok, MapStruct, Dagger, and AutoValue.

## How Annotation Processing Works

### Compilation Phases with APT

```
1. Parse source → AST
2. Read annotations from source
3. Discover annotation processors
4. Run processors (may generate new source)
5. If new source generated → repeat steps 2-4
6. Type-check and compile
```

The key insight: annotation processing is an **iterative loop**. Processors can generate new files, which are then processed again until no new files appear.

### When Processors Run

```java
@SupportedAnnotationTypes("com.example.MyAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        // Only process if there are annotations to handle
        if (annotations.isEmpty()) return false;

        // Process each annotated element
        for (Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
            // Generate code, report errors, etc.
        }

        return true; // Claim these annotations
    }
}
```

### Round Lifecycle

```
Round 1:
  Input: User source files
  Processors: Run with user annotations
  Output: Generated files

Round 2 (if files were generated):
  Input: User source + Round 1 generated files
  Processors: Run with all annotations (including generated)
  Output: More generated files (or none)

Round 3 (final, even if no new files):
  Input: All source files
  Processors: Last chance to process
  Output: Final state
```

## Processor Discovery

### ServiceLoader (META-INF/services)

Processors are registered via `META-INF/services/javax.annotation.processing.Processor`:

```
META-INF/
  services/
    javax.annotation.processing.Processor
      → com.example.MyProcessor
```

The file lists fully qualified processor class names, one per line.

### Compile-Time Discovery

When javac finds `@SupportedAnnotationTypes`, it can automatically discover processors on the processor path:

```bash
javac -processorpath lib/processors.jar src/*.java
```

### Programmatic Registration

```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
JavaCompiler.CompilationTask task = compiler.getTask(
    null, null, diagnostics, options, null, files
);

// Register processors directly
task.setProcessors(List.of(new MyProcessor()));
```

## Processing Elements

Processors operate on the javax.lang.model API:

### Element Types

| Element | Description |
|---------|-------------|
| `TypeElement` | Class, interface, enum, record |
| `ExecutableElement` | Method, constructor |
| `VariableElement` | Field, parameter, local variable |
| `PackageElement` | Package |
| `ModuleElement` | Module |

### Annotated Elements

```java
// Get all elements annotated with @Override
for (Element e : roundEnv.getElementsAnnotatedWith(Override.class)) {
    switch (e.getKind()) {
        case METHOD -> processMethod((ExecutableElement) e);
        case FIELD  -> processField((VariableElement) e);
        // ...
    }
}
```

### Type Mirror

Processors can inspect types without needing full compilation:

```java
TypeMirror type = element.asType();
if (type.getKind() == TypeKind.DECLARED) {
    DeclaredType declared = (DeclaredType) type;
    TypeElement typeElement = (TypeElement) declared.asElement();
    // Inspect class hierarchy, interfaces, type parameters
}
```

## Code Generation

Processors generate source files using `javax.annotation.processing.Filer`:

### Generate a New Class

```java
@Override
public boolean process(Set<? extends TypeElement> annotations,
                       RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
        String className = element.getSimpleName() + "Generated";

        JavaFileObject file = processingEnv.getFiler()
            .createSourceFile("com.example." + className);

        try (PrintWriter out = new PrintWriter(file.openWriter())) {
            out.println("package com.example;");
            out.println("public class " + className + " {");
            out.println("    public void run() {");
            out.println("        System.out.println(\"Generated!\");");
            out.println("    }");
            out.println("}");
        }
    }
    return true;
}
```

### Generate a Resource File

```java
FileObject resource = processingEnv.getFiler()
    .createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/my-config.properties");

try (PrintWriter out = new PrintWriter(resource.openWriter())) {
    out.println("generated=true");
    out.println("source=" + element.getSimpleName());
}
```

### Generate a Class File (Binary)

```java
JavaFileObject classFile = processingEnv.getFiler()
    .createClassFile("com.example.GeneratedClass");

try (DataOutputStream out = new DataOutputStream(classFile.openOutputStream())) {
    // Write bytecode directly
}
```

## Error and Warning Reporting

```java
// Report an error
processingEnv.getMessager().printMessage(
    Diagnostic.Kind.ERROR,
    "Missing required field: " + fieldName,
    element
);

// Report a warning
processingEnv.getMessager().printMessage(
    Diagnostic.Kind.WARNING,
    "This annotation is deprecated",
    element
);

// Report a note
processingEnv.getMessager().printMessage(
    Diagnostic.Kind.NOTE,
    "Processing element: " + element.getSimpleName()
);
```

## Key Source Files

| File | Purpose |
|------|---------|
| `com/sun/tools/javac/processing/JavacProcessingEnvironment.java` | APT driver |
| `com/sun/tools/javac/processing/ProcProcessor.java` | Processor wrapper |
| `com/sun/tools/javac/processing/ProcessingEnvironment.java` | Processing context |
| `javax/annotation/processing/AbstractProcessor.java` | Base processor class |
| `javax/annotation/processing/Filer.java` | File generation API |
| `javax/annotation/processing/Messager.java` | Error reporting API |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
