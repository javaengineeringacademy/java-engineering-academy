# Solutions: Field Access

Solutions to the exercises in the [practices](../practices/) directory.

## Exercise 1: Field Metadata Extractor

```java
package academy.javaengineering.reflection.fieldaccess.solutions;

import java.lang.reflect.*;
import java.util.*;

public class Solution1_FieldMetadataExtractor {

    public static Map<String, String> extractMetadata(Class<?> clazz) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            result.put(field.getName(), Modifier.toString(field.getModifiers()));
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(extractMetadata(String.class));
    }
}
```

## Exercise 2: Annotation-Aware Field Reader

```java
package academy.javaengineering.reflection.fieldaccess.solutions;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Solution2_AnnotationAwareReader {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface JsonField { String value() default ""; }

    @JsonField("user_name") private String name = "Alice";
    @JsonField("user_age") private int age = 30;
    private String ignored = "not serialized";

    public static Map<String, Object> readAnnotatedFields(Object obj) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            JsonField ann = field.getAnnotation(JsonField.class);
            if (ann != null) {
                field.setAccessible(true);
                result.put(ann.value(), field.get(obj));
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(readAnnotatedFields(new Solution2_AnnotationAwareReader()));
    }
}
```

## Exercise 3: Deep Field Comparison

```java
package academy.javaengineering.reflection.fieldaccess.solutions;

import java.lang.reflect.Field;
import java.util.*;

public class Solution3_DeepFieldComparison {

    public static List<String> findDifferences(Object a, Object b) throws Exception {
        List<String> diffs = new ArrayList<>();
        if (a.getClass() != b.getClass()) {
            diffs.add("Different classes");
            return diffs;
        }
        for (Field field : a.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object valA = field.get(a);
            Object valB = field.get(b);
            if (!Objects.equals(valA, valB)) {
                diffs.add(field.getName());
            }
        }
        return diffs;
    }

    static class Person {
        private String name;
        private int age;
        Person(String name, int age) { this.name = name; this.age = age; }
    }

    public static void main(String[] args) throws Exception {
        Person a = new Person("Alice", 30);
        Person b = new Person("Bob", 30);
        Person c = new Person("Alice", 30);
        System.out.println("a vs b: " + findDifferences(a, b));
        System.out.println("a vs c: " + findDifferences(a, c));
    }
}
```
