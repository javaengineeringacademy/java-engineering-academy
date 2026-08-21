# Examples: Annotation Processing

## Example 1: Basic Processor

```java
package academy.javaengineering.reflection.annotationprocessing;

import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.*;
import java.io.*;
import java.util.*;

@SupportedAnnotationTypes("academy.javaengineering.reflection.annotationprocessing.SimpleAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class SimpleProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SimpleAnnotation.class)) {
            SimpleAnnotation ann = element.getAnnotation(SimpleAnnotation.class);
            String className = element.getSimpleName() + "Info";
            String packageName = processingEnv.getElementUtils()
                .getPackageOf(element).getQualifiedName().toString();

            try {
                JavaFileObject file = processingEnv.getFiler()
                    .createSourceFile(packageName + "." + className);
                try (PrintWriter out = new PrintWriter(file.openWriter())) {
                    out.println("package " + packageName + ";");
                    out.println("public class " + className + " {");
                    out.println("    public static String getInfo() {");
                    out.println("        return \"" + ann.value() + "\";");
                    out.println("    }");
                    out.println("}");
                }
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Could not generate: " + e.getMessage());
            }
        }
        return true;
    }
}
```

## Example 2: Builder Generator

```java
package academy.javaengineering.reflection.annotationprocessing;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Builder {}
```

The processor generates a Builder class with setter methods for each field.

## Example 3: Validation Processor

Reports compile-time errors when annotations have invalid values.

```java
@Override
public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element element : roundEnv.getElementsAnnotatedWith(Validated.class)) {
        Validated ann = element.getAnnotation(Validated.class);
        if (ann.value().isEmpty()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "value cannot be empty", element);
        }
    }
    return true;
}
```
