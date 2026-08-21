# Practices: Field Access

Complete the following exercises to reinforce your understanding of field access via reflection.

## Exercise 1: Field Metadata Extractor

Write a method that returns a Map of field names to their modifier strings for a given class.

```java
package academy.javaengineering.reflection.fieldaccess.practices;

import java.lang.reflect.*;
import java.util.*;

public class Exercise1_FieldMetadataExtractor {
    public static Map<String, String> extractMetadata(Class<?> clazz) {
        // TODO: Return map of field name -> Modifier.toString(modifiers)
        return null;
    }
}
```

## Exercise 2: Annotation-Aware Field Reader

Write a method that reads field values only if the field has a specific annotation.

```java
package academy.javaengineering.reflection.fieldaccess.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise2_AnnotationAwareReader {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface JsonField { String value() default ""; }

    @JsonField("user_name") private String name = "Alice";
    @JsonField("user_age") private int age = 30;
    private String ignored = "not serialized";

    public static Map<String, Object> readAnnotatedFields(Object obj) {
        // TODO: Read fields with @JsonField, use annotation value as key
        return null;
    }
}
```

## Exercise 3: Deep Field Comparison

Write a method that compares all fields of two objects of the same class recursively, returning a list of field names that differ.

```java
package academy.javaengineering.reflection.fieldaccess.practices;

import java.lang.reflect.Field;
import java.util.*;

public class Exercise3_DeepFieldComparison {
    public static List<String> findDifferences(Object a, Object b) {
        // TODO: Compare all fields, return names of differing fields
        return null;
    }
}
```
