# Examples: Class Introspection

## Example 1: Complete Class Inspector

```java
package academy.javaengineering.reflection.classintrospection;

import java.lang.reflect.*;
import java.util.*;

public class ClassInspector {

    public static void inspect(Class<?> clazz) {
        System.out.println("=== " + clazz.getName() + " ===");
        System.out.println("Simple Name: " + clazz.getSimpleName());
        System.out.println("Package: " + clazz.getPackage().getName());
        System.out.println("Modifiers: " + Modifier.toString(clazz.getModifiers()));
        System.out.println("Superclass: " +
            (clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "none"));

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.println("\nInterfaces:");
            for (Class<?> iface : interfaces) {
                System.out.println("  - " + iface.getName());
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            System.out.println("\nFields:");
            for (Field f : fields) {
                System.out.printf("  %s %s %s%n",
                    Modifier.toString(f.getModifiers()),
                    f.getType().getSimpleName(), f.getName());
            }
        }

        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            System.out.println("\nMethods:");
            for (Method m : methods) {
                System.out.printf("  %s %s %s(%s)%n",
                    Modifier.toString(m.getModifiers()),
                    m.getReturnType().getSimpleName(), m.getName(),
                    Arrays.toString(m.getParameterTypes()));
            }
        }
    }

    public static void main(String[] args) {
        inspect(ArrayList.class);
        System.out.println();
        inspect(String.class);
    }
}
```

## Example 2: Hierarchy Walker

```java
package academy.javaengineering.reflection.classintrospection;

public class HierarchyWalker {

    public static void printHierarchy(Class<?> clazz) {
        Class<?> current = clazz;
        int depth = 0;
        while (current != null) {
            String indent = "  ".repeat(depth);
            System.out.println(indent + current.getSimpleName());
            current = current.getSuperclass();
            depth++;
        }
    }

    public static void main(String[] args) {
        printHierarchy(javax.swing.JButton.class);
    }
}
```

## Example 3: Interface Scanner

```java
package academy.javaengineering.reflection.classintrospection;

import java.lang.reflect.*;
import java.util.*;

public class InterfaceScanner {

    public static Set<String> findAllInterfaces(Class<?> clazz) {
        Set<String> result = new LinkedHashSet<>();
        collectInterfaces(clazz, result);
        return result;
    }

    private static void collectInterfaces(Class<?> clazz, Set<String> result) {
        if (clazz == null) return;
        for (Class<?> iface : clazz.getInterfaces()) {
            result.add(iface.getName());
            collectInterfaces(iface, result);
        }
        collectInterfaces(clazz.getSuperclass(), result);
    }

    public static void main(String[] args) {
        System.out.println(findAllInterfaces(ArrayList.class));
    }
}
```
