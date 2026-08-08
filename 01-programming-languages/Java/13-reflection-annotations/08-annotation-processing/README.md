# 08 — Annotation Processing

## Why Annotation Processing Matters

Annotation processing happens at compile time, not runtime. While runtime reflection reads annotations to make decisions, compile-time processing generates new source code based on annotations. This is how Lombok generates getters/setters, how Dagger 2 generates dependency injection code, and how MapStruct generates type mappers.

The key advantage: errors are caught at compile time, not runtime. And generated code runs at full speed with no reflection overhead.

---

## The Annotation Processing API

Java provides `javax.annotation.processing` package:

| Class | Purpose |
|-------|---------|
| `AbstractProcessor` | Base class for annotation processors |
| `ProcessingEnvironment` | Provides access to compiler utilities |
| `Filer` | Creates new source/class/resource files |
| `Messager` | Reports errors/warnings during processing |
| `Elements` | Utility for working with program elements |
| `Types` | Utility for working with types |

---

## Creating a Processor

```java
@SupportedAnnotationTypes("com.example.MyAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MyProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                          RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
            // Process each annotated element
            MyAnnotation annotation = element.getAnnotation(MyAnnotation.class);
            String value = annotation.value();
            
            // Generate code, validate, etc.
            generateCode(element, value);
        }
        return true; // Claim these annotations
    }
}
```

---

## The Processing Round

Annotation processing happens in rounds:

1. **Round 1:** Process all annotations in the source tree
2. **Round 2:** Process any new annotations generated in Round 1
3. **Round N:** Continue until no new annotations are generated

```java
@Override
public boolean process(Set<? extends TypeElement> annotations,
                      RoundEnvironment roundEnv) {
    // RoundEnvironment gives access to:
    
    // All elements annotated with a specific annotation
    Set<? extends Element> elements = 
        roundEnv.getElementsAnnotatedWith(MyAnnotation.class);
    
    // All root elements (top-level classes)
    Set<? extends Element> roots = roundEnv.getRootElements();
    
    // All elements annotated with any of the processing annotations
    Set<? extends Element> allAnnotated = roundEnv.getElementsAnnotatedWithAny(annotations);
    
    // Check if processing is over
    boolean isLastRound = roundEnv.processingOver();
    
    return true;
}
```

---

## Generating Source Code

```java
private void generateCode(Element element, String value) throws IOException {
    String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
    String className = element.getSimpleName().toString() + "Helper";
    
    JavaFileObject file = filer.createSourceFile(packageName + "." + className);
    
    try (PrintWriter out = new PrintWriter(file.openWriter())) {
        out.println("package " + packageName + ";");
        out.println();
        out.println("public class " + className + " {");
        out.println("    public static String getValue() {");
        out.println("        return \"" + value + "\";");
        out.println("    }");
        out.println("}");
    }
}
```

---

## Generating Class Files (Bytecode)

```java
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.tools.*;
import java.io.*;
import java.util.*;

@SupportedAnnotationTypes("com.example.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                          RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            TypeElement typeElement = (TypeElement) element;
            generateBuilderClass(typeElement);
        }
        return true;
    }

    private void generateBuilderClass(TypeElement typeElement) {
        String packageName = processingEnv.getElementUtils()
            .getPackageOf(typeElement).getQualifiedName().toString();
        String builderName = typeElement.getSimpleName() + "Builder";
        
        try {
            JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(packageName + "." + builderName);
            
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                out.println("package " + packageName + ";");
                out.println();
                out.println("public class " + builderName + " {");
                
                // Generate fields
                for (Element enclosed : typeElement.getEnclosedElements()) {
                    if (enclosed.getKind() == ElementKind.FIELD) {
                        VariableElement field = (VariableElement) enclosed;
                        out.println("    private " + field.asType() + " " + 
                            field.getSimpleName() + ";");
                    }
                }
                
                // Generate setter methods
                for (Element enclosed : typeElement.getEnclosedElements()) {
                    if (enclosed.getKind() == ElementKind.FIELD) {
                        VariableElement field = (VariableElement) enclosed;
                        String fieldName = field.getSimpleName().toString();
                        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() 
                            + fieldName.substring(1);
                        
                        out.println("    public " + builderName + " " + setterName + 
                            "(" + field.asType() + " " + fieldName + ") {");
                        out.println("        this." + fieldName + " = " + fieldName + ";");
                        out.println("        return this;");
                        out.println("    }");
                    }
                }
                
                // Generate build method
                out.println("    public " + typeElement.getSimpleName() + " build() {");
                out.println("        return new " + typeElement.getSimpleName() + "(this);");
                out.println("    }");
                
                out.println("}");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "Failed to generate builder: " + e.getMessage());
        }
    }
}
```

---

## Element Types

```java
@Override
public boolean process(Set<? extends TypeElement> annotations,
                      RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
        switch (element.getKind()) {
            case CLASS:
                TypeElement classElement = (TypeElement) element;
                processClass(classElement);
                break;
            case METHOD:
                ExecutableElement methodElement = (ExecutableElement) element;
                processMethod(methodElement);
                break;
            case FIELD:
                VariableElement fieldElement = (VariableElement) element;
                processField(fieldElement);
                break;
            case CONSTRUCTOR:
                ExecutableElement ctorElement = (ExecutableElement) element;
                processConstructor(ctorElement);
                break;
            case PARAMETER:
                VariableElement paramElement = (VariableElement) element;
                processParameter(paramElement);
                break;
            case PACKAGE:
                PackageElement packageElement = (PackageElement) element;
                processPackage(packageElement);
                break;
        }
    }
    return true;
}
```

---

## Reporting Errors and Warnings

```java
// Error — stops compilation
messager.printMessage(Diagnostic.Kind.ERROR,
    "Invalid configuration: " + element.getSimpleName(),
    element);

// Warning — continues compilation
messager.printMessage(Diagnostic.Kind.WARNING,
    "Deprecated usage detected",
    element);

// Note — informational message
messager.printMessage(Diagnostic.Kind.NOTE,
    "Processing " + element.getSimpleName(),
    element);
```

---

## Accessing Types and Type Mirrors

```java
// Get the type of an element
TypeMirror typeMirror = element.asType();

// Check type kind
if (typeMirror.getKind() == TypeKind.DECLARED) {
    DeclaredType declaredType = (DeclaredType) typeMirror;
    Element typeElement = declaredType.asElement();
}

// Check if type is a specific class
Types typeUtils = processingEnv.getTypeUtils();
TypeMirror stringType = elementUtils.getTypeElement("java.lang.String").asType();
boolean isString = typeUtils.isSameType(typeMirror, stringType);

// Check assignability
boolean assignable = typeUtils.isAssignable(stringType, 
    elementUtils.getTypeElement("java.lang.Object").asType());
```

---

## Generating Resources

```java
// Generate a resource file (not Java source)
try {
    FileObject resource = filer.createResource(
        StandardLocation.CLASS_OUTPUT,
        "",
        "META-INF/services/com.example.Plugin"
    );
    
    try (PrintWriter out = new PrintWriter(resource.openWriter())) {
        out.println("com.example.MyPlugin");
    }
} catch (IOException e) {
    messager.printMessage(Diagnostic.Kind.ERROR, "Failed to create resource");
}
```

---

## Registering Processors

### ServiceLoader (Standard)

Create `META-INF/services/javax.annotation.processing.Processor`:
```
com.example.MyProcessor
```

### Compile-time Registration

```bash
javac -processor com.example.MyProcessor \
      -processorpath /path/to/processor.jar \
      src/**/*.java
```

### Maven Configuration

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>com.example</groupId>
                <artifactId>my-processor</artifactId>
                <version>1.0</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

---

## Complete Example: Builder Generator

```java
package com.example;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Builder { }
```

```java
package com.example;

import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.*;
import java.io.*;
import java.util.*;

@SupportedAnnotationTypes("com.example.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                          RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "@Builder can only be applied to classes", element);
                continue;
            }
            
            TypeElement classElement = (TypeElement) element;
            String packageName = processingEnv.getElementUtils()
                .getPackageOf(classElement).getQualifiedName().toString();
            String className = classElement.getSimpleName().toString();
            String builderName = className + "Builder";
            
            try {
                JavaFileObject file = processingEnv.getFiler()
                    .createSourceFile(packageName + "." + builderName);
                
                try (PrintWriter out = new PrintWriter(file.openWriter())) {
                    out.println("package " + packageName + ";");
                    out.println();
                    out.println("public class " + builderName + " {");
                    
                    List<? extends Element> fields = classElement.getEnclosedElements();
                    
                    for (Element field : fields) {
                        if (field.getKind() == ElementKind.FIELD) {
                            out.println("    private " + field.asType() + 
                                " " + field.getSimpleName() + ";");
                        }
                    }
                    
                    out.println();
                    
                    for (Element field : fields) {
                        if (field.getKind() == ElementKind.FIELD) {
                            String name = field.getSimpleName().toString();
                            String capitalized = name.substring(0, 1).toUpperCase() + 
                                name.substring(1);
                            out.println("    public " + builderName + " " + name + 
                                "(" + field.asType() + " " + name + ") {");
                            out.println("        this." + name + " = " + name + ";");
                            out.println("        return this;");
                            out.println("    }");
                        }
                    }
                    
                    out.println();
                    out.println("    public " + className + " build() {");
                    out.println("        return new " + className + "(this);");
                    out.println("    }");
                    
                    out.println("}");
                }
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Could not generate builder: " + e.getMessage());
            }
        }
        return true;
    }
}
```

---

## Production Incident: Processor Infinite Loop

**Incident:** An annotation processor generated new classes with the same annotation it was processing. This caused an infinite loop of processing rounds.

**Root cause:** The processor did not check if the current round was the same as the previous round, and generated annotated classes that triggered re-processing.

**Fix:** Check `roundEnv.processingOver()` and use a `Set<String>` to track already-processed elements:

```java
private Set<String> processed = new HashSet<>();

@Override
public boolean process(Set<? extends TypeElement> annotations,
                      RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(MyAnnotation.class)) {
        String key = element.toString();
        if (processed.contains(key)) continue;
        processed.add(key);
        // ... process
    }
    return true;
}
```

---

## Code Review Checklist

- [ ] Does the processor handle all element kinds it claims to support?
- [ ] Are errors reported via `Messager` (not thrown as exceptions)?
- [ ] Is `@SupportedAnnotationTypes` accurate?
- [ ] Is `@SupportedSourceVersion` set correctly?
- [ ] Does the processor handle repeated rounds (idempotency)?
- [ ] Is generated code valid Java (compiles independently)?
- [ ] Are resource files created in the correct location?

---

## Debugging Tips

1. Use `-verbose` javac flag to see processing rounds
2. Print `element.getKind()` to identify element type
3. Use `processingEnv.getMessager()` for compiler messages
4. Check generated source in `target/generated-sources`
5. Add `-Xlint:processing` for processor-related warnings

---

## Interview Questions

1. What is the difference between compile-time and runtime annotation processing?
2. How does `AbstractProcessor.process()` return value affect annotation claiming?
3. What is the `Filer` API and how do you use it?
4. How do you register an annotation processor?
5. What is a processing round and when does it end?

---

## Summary

| Concept | Key Point |
|---------|-----------|
| AbstractProcessor | Base class for all processors |
| Filer | Creates new source/class/resource files |
| Messager | Reports errors/warnings to compiler |
| Elements | Utility for working with program elements |
| process() | Main method called for each round |
| @SupportedAnnotationTypes | Declares which annotations this processor handles |

---

*Next: [09 — Real-World Use Cases](../09-real-world-use-cases/README.md)*
