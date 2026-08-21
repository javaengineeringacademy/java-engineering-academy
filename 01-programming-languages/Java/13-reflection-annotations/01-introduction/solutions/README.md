# Solutions: Introduction to Reflection

Solutions to the exercises in the [practices](../practices/) directory.

## Exercise 1: Class Introspector

```java
package academy.javaengineering.reflection.intro.solutions;

import java.lang.reflect.*;
import java.util.*;

public class Solution1_ClassIntrospector {

    public static void printClassInfo(Object obj) {
        Class<?> clazz = obj.getClass();

        List<String> hierarchy = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null) {
            hierarchy.add(current.getSimpleName());
            current = current.getSuperclass();
        }

        Set<String> interfaces = new LinkedHashSet<>();
        current = clazz;
        while (current != null) {
            for (Class<?> iface : current.getInterfaces()) {
                collectInterfaces(iface, interfaces);
            }
            current = current.getSuperclass();
        }

        System.out.println("Hierarchy: " + String.join(" -> ", hierarchy));
        System.out.println("Interfaces: " + String.join(", ", interfaces));
        System.out.println();
    }

    private static void collectInterfaces(Class<?> iface, Set<String> result) {
        if (result.add(iface.getSimpleName())) {
            for (Class<?> parent : iface.getInterfaces()) {
                collectInterfaces(parent, result);
            }
        }
    }

    public static void main(String[] args) {
        printClassInfo("hello");
        printClassInfo(new java.util.ArrayList<>());
        printClassInfo(42);
    }
}
```

**Key concepts:** `getSuperclass()` for hierarchy, `getInterfaces()` for interface discovery, recursive collection for transitive interfaces.

---

## Exercise 2: Class Name Resolver

```java
package academy.javaengineering.reflection.intro.solutions;

public class Solution2_ClassNameResolver {

    private static final String[] COMMON_PACKAGES = {
        "java.lang", "java.util", "java.io", "java.net",
        "java.math", "java.time", "java.util.concurrent", "java.util.function"
    };

    public static Class<?> resolve(String simpleName) {
        if (simpleName == null || simpleName.isEmpty()) return null;

        try {
            return Class.forName(simpleName);
        } catch (ClassNotFoundException ignored) {}

        for (String pkg : COMMON_PACKAGES) {
            try {
                return Class.forName(pkg + "." + simpleName);
            } catch (ClassNotFoundException ignored) {}
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(resolve("String"));
        System.out.println(resolve("ArrayList"));
        System.out.println(resolve("NoSuchClass"));
    }
}
```

**Key concepts:** `Class.forName()` with try-catch, systematic package prefix search.

---

## Exercise 3: Safety Checker

```java
package academy.javaengineering.reflection.intro.solutions;

import java.lang.reflect.*;
import java.util.*;

public class Solution3_SafetyChecker {

    public static List<String> checkInstantiationSafety(Class<?> clazz) {
        List<String> issues = new ArrayList<>();

        if (clazz.isInterface()) issues.add("Cannot instantiate an interface");
        if (Modifier.isAbstract(clazz.getModifiers())) issues.add("Cannot instantiate an abstract class");
        if (clazz.isPrimitive()) issues.add("Cannot instantiate a primitive type");
        if (clazz.isArray()) issues.add("Cannot instantiate an array type directly");
        if (clazz.isAnnotation()) issues.add("Cannot instantiate an annotation type");
        if (clazz.isEnum()) issues.add("Cannot instantiate an enum type via constructor");

        try {
            Constructor<?> ctor = clazz.getDeclaredConstructor();
            if (!Modifier.isPublic(ctor.getModifiers())) {
                issues.add("No-arg constructor is not public");
            }
        } catch (NoSuchMethodException e) {
            issues.add("No default (no-arg) constructor found");
        }

        return issues;
    }

    public static void main(String[] args) {
        System.out.println("String: " + checkInstantiationSafety(String.class));
        System.out.println("ArrayList: " + checkInstantiationSafety(java.util.ArrayList.class));
        System.out.println("Serializable: " + checkInstantiationSafety(java.io.Serializable.class));
    }
}
```

**Key concepts:** `isInterface()`, `isAbstract()`, `getDeclaredConstructor()` for safety checks.
