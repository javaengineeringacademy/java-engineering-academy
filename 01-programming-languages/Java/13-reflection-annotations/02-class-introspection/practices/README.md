# Practices: Class Introspection

## Exercise 1: Type Category Detector

Write a method that classifies any `Class<?>` into categories: primitive, array, enum, interface, annotation, or concrete class.

```java
package academy.javaengineering.reflection.classintrospection.practices;

public class Exercise1_TypeCategoryDetector {

    public enum Category {
        PRIMITIVE, ARRAY, ENUM, INTERFACE, ANNOTATION, ABSTRACT_CLASS, CONCRETE_CLASS
    }

    /**
     * Classify the given class into one of the categories.
     */
    public static Category classify(Class<?> clazz) {
        // TODO: Implement
        return null;
    }

    public static void main(String[] args) {
        System.out.println(classify(int.class));
        System.out.println(classify(String[].class));
        System.out.println(classify(Thread.State.class));
        System.out.println(classify(Runnable.class));
        System.out.println(classify(Deprecated.class));
        System.out.println(classify(String.class));
    }
}
```

## Exercise 2: Modifier Analyzer

Write a method that returns a human-readable description of all modifiers on a class.

```java
package academy.javaengineering.reflection.classintrospection.practices;

import java.lang.reflect.Modifier;
import java.util.*;

public class Exercise2_ModifierAnalyzer {

    /**
     * Return a sorted list of modifier names for the given class.
     * Example: for "public final class String" returns ["final", "public"]
     */
    public static List<String> analyzeModifiers(Class<?> clazz) {
        // TODO: Implement
        return null;
    }

    public static void main(String[] args) {
        System.out.println(analyzeModifiers(String.class));
        System.out.println(analyzeModifiers(Runnable.class));
        System.out.println(analyzeModifiers(int.class));
    }
}
```

## Exercise 3: Generic Superclass Resolver

Write a method that resolves the type arguments of a generic superclass.

```java
package academy.javaengineering.reflection.classintrospection.practices;

import java.lang.reflect.*;
import java.util.*;

public class Exercise3_GenericResolver {

    static class StringList extends ArrayList<String> {}
    static class IntMap extends HashMap<String, Integer> {}

    /**
     * Returns the type arguments of the first generic superclass.
     * For StringList: returns ["String"]
     * For IntMap: returns ["String", "Integer"]
     * For Object: returns empty list
     */
    public static List<String> resolveGenericSuperclass(Class<?> clazz) {
        // TODO: Implement
        return null;
    }

    public static void main(String[] args) {
        System.out.println(resolveGenericSuperclass(StringList.class));
        System.out.println(resolveGenericSuperclass(IntMap.class));
        System.out.println(resolveGenericSuperclass(Object.class));
    }
}
```
