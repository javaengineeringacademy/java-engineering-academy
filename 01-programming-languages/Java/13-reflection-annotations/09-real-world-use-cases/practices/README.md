# Practices: Real-World Use Cases

## Exercise 1: Mini Bean Container

Implement a simple DI container that wires @Inject annotated fields.

```java
package academy.javaengineering.reflection.realworld.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;

public class Exercise1_MiniBeanContainer {
    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface Inject {}

    public static <T> T createAndWire(Class<T> clazz) throws Exception {
        // TODO: Create instance, find @Inject fields, wire dependencies
        return null;
    }
}
```

## Exercise 2: Mini Validator

Implement a validator that checks @Required and @MinLength annotations.

```java
package academy.javaengineering.reflection.realworld.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise2_MiniValidator {
    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface Required {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface MinLength { int value(); }

    public static List<String> validate(Object obj) throws Exception {
        // TODO: Check @Required and @MinLength annotations
        return null;
    }
}
```

## Exercise 3: Mini Config Loader

Implement a config loader that reads @Config annotations and maps to properties.

```java
package academy.javaengineering.reflection.realworld.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise3_MiniConfigLoader {
    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface Config { String key(); String defaultValue() default ""; }

    public static <T> T load(Class<T> clazz, Map<String, String> props) throws Exception {
        // TODO: Create instance, read @Config annotations, set field values
        return null;
    }
}
```
