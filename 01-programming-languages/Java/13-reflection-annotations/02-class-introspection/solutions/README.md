# Solutions: Class Introspection

## Exercise 1: Type Category Detector

```java
package academy.javaengineering.reflection.classintrospection.solutions;

import java.lang.reflect.Modifier;

public class Solution1_TypeCategoryDetector {

    public enum Category {
        PRIMITIVE, ARRAY, ENUM, INTERFACE, ANNOTATION, ABSTRACT_CLASS, CONCRETE_CLASS
    }

    public static Category classify(Class<?> clazz) {
        if (clazz.isPrimitive()) return Category.PRIMITIVE;
        if (clazz.isArray()) return Category.ARRAY;
        if (clazz.isEnum()) return Category.ENUM;
        if (clazz.isInterface()) return Category.INTERFACE;
        if (clazz.isAnnotation()) return Category.ANNOTATION;
        if (Modifier.isAbstract(clazz.getModifiers())) return Category.ABSTRACT_CLASS;
        return Category.CONCRETE_CLASS;
    }

    public static void main(String[] args) {
        System.out.println(classify(int.class));              // PRIMITIVE
        System.out.println(classify(String[].class));          // ARRAY
        System.out.println(classify(Thread.State.class));      // ENUM
        System.out.println(classify(Runnable.class));          // INTERFACE
        System.out.println(classify(Deprecated.class));        // ANNOTATION
        System.out.println(classify(String.class));            // CONCRETE_CLASS
    }
}
```

## Exercise 2: Modifier Analyzer

```java
package academy.javaengineering.reflection.classintrospection.solutions;

import java.lang.reflect.Modifier;
import java.util.*;

public class Solution2_ModifierAnalyzer {

    public static List<String> analyzeModifiers(Class<?> clazz) {
        int mods = clazz.getModifiers();
        List<String> result = new ArrayList<>();

        if (Modifier.isPublic(mods)) result.add("public");
        if (Modifier.isPrivate(mods)) result.add("private");
        if (Modifier.isProtected(mods)) result.add("protected");
        if (Modifier.isStatic(mods)) result.add("static");
        if (Modifier.isFinal(mods)) result.add("final");
        if (Modifier.isAbstract(mods)) result.add("abstract");
        if (Modifier.isInterface(mods)) result.add("interface");
        if (Modifier.isEnum(mods)) result.add("enum");
        if (Modifier.isAnnotation(mods)) result.add("annotation");
        if (Modifier.isSynthetic(mods)) result.add("synthetic");

        Collections.sort(result);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(analyzeModifiers(String.class));      // [final, public]
        System.out.println(analyzeModifiers(Runnable.class));    // [abstract, interface, public]
        System.out.println(analyzeModifiers(int.class));         // [public]
    }
}
```

## Exercise 3: Generic Superclass Resolver

```java
package academy.javaengineering.reflection.classintrospection.solutions;

import java.lang.reflect.*;
import java.util.*;

public class Solution3_GenericResolver {

    static class StringList extends ArrayList<String> {}
    static class IntMap extends HashMap<String, Integer> {}

    public static List<String> resolveGenericSuperclass(Class<?> clazz) {
        List<String> result = new ArrayList<>();
        Type genericSuper = clazz.getGenericSuperclass();

        if (genericSuper instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericSuper;
            for (Type typeArg : pt.getActualTypeArguments()) {
                if (typeArg instanceof Class) {
                    result.add(((Class<?>) typeArg).getSimpleName());
                } else if (typeArg instanceof TypeVariable) {
                    result.add(((TypeVariable<?>) typeArg).getName());
                } else {
                    result.add(typeArg.getTypeName());
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(resolveGenericSuperclass(StringList.class)); // [String]
        System.out.println(resolveGenericSuperclass(IntMap.class));     // [String, Integer]
        System.out.println(resolveGenericSuperclass(Object.class));     // []
    }
}
```
