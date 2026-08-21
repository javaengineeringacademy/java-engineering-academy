# Practices: Custom Annotations

## Exercise 1: Validation Annotation

Create a @NotEmpty annotation and a validator that checks String fields.

```java
package academy.javaengineering.reflection.annotations.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise1_ValidationAnnotation {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NotEmpty { String message() default "Cannot be empty"; }

    public static List<String> validate(Object obj) throws Exception {
        // TODO: Find @NotEmpty fields, check if null or empty
        return null;
    }
}
```

## Exercise 2: Cache Annotation

Create a @Cacheable annotation and a method that reads cache configuration.

```java
package academy.javaengineering.reflection.annotations.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise2_CacheAnnotation {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Cacheable { int ttlSeconds() default 300; }

    public static Map<String, Integer> getCacheConfig(Class<?> clazz) {
        // TODO: Return map of method name -> TTL
        return null;
    }
}
```

## Exercise 3: Metadata Extractor

Create an annotation and extract all its values into a Map.

```java
package academy.javaengineering.reflection.annotations.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise3_MetadataExtractor {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ApiEndpoint {
        String path();
        String method() default "GET";
        String description() default "";
    }

    public static Map<String, Object> extractMetadata(Class<?> clazz) {
        // TODO: Read @ApiEndpoint values into a Map
        return null;
    }
}
```
