# Examples: Introduction to Reflection

## Basic Reflection Operations

### Example 1: Three Ways to Get a Class Object

```java
package academy.javaengineering.reflection.intro;

public class ClassObjectDemo {
    public static void main(String[] args) throws Exception {
        // Way 1: .class literal (compile-time known)
        Class<String> clazz1 = String.class;
        System.out.println("Way 1: " + clazz1.getName());

        // Way 2: getClass() on instance (runtime type)
        String s = "hello";
        Class<?> clazz2 = s.getClass();
        System.out.println("Way 2: " + clazz2.getName());

        // Way 3: Class.forName() (dynamic)
        Class<?> clazz3 = Class.forName("java.lang.String");
        System.out.println("Way 3: " + clazz3.getName());

        // All three refer to the same Class object
        System.out.println("Same class? " + (clazz1 == clazz2 && clazz2 == clazz3));
    }
}
```

### Example 2: Inspecting Class Metadata

```java
package academy.javaengineering.reflection.intro;

import java.lang.reflect.*;
import java.util.*;

public class MetadataDemo {
    public static void main(String[] args) {
        Class<?> clazz = ArrayList.class;

        System.out.println("Name: " + clazz.getName());
        System.out.println("Simple Name: " + clazz.getSimpleName());
        System.out.println("Package: " + clazz.getPackage().getName());
        System.out.println("Is Interface: " + clazz.isInterface());
        System.out.println("Is Abstract: " + Modifier.isAbstract(clazz.getModifiers()));

        System.out.println("\nInterfaces:");
        for (Class<?> iface : clazz.getInterfaces()) {
            System.out.println("  - " + iface.getSimpleName());
        }

        System.out.println("\nSuperclass: " + clazz.getSuperclass().getSimpleName());
    }
}
```

### Example 3: Dynamic Instance Creation

```java
package academy.javaengineering.reflection.intro;

public class DynamicCreationDemo {
    public static void main(String[] args) throws Exception {
        String className = "java.util.ArrayList";

        // Load class dynamically
        Class<?> clazz = Class.forName(className);

        // Create instance using default constructor
        Object instance = clazz.getDeclaredConstructor().newInstance();

        System.out.println("Created: " + instance.getClass().getSimpleName());
        System.out.println("Is List? " + (instance instanceof java.util.List));
    }
}
```

### Example 4: Reflection vs Direct Access Performance

```java
package academy.javaengineering.reflection.intro;

import java.lang.reflect.Method;

public class PerformanceDemo {
    public static void main(String[] args) throws Exception {
        String str = "hello";
        Method lengthMethod = String.class.getMethod("length");

        // Direct access
        long start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            str.length();
        }
        long directTime = System.nanoTime() - start;

        // Reflection
        start = System.nanoTime();
        for (int i = 0; i < 10_000_000; i++) {
            lengthMethod.invoke(str);
        }
        long reflectiveTime = System.nanoTime() - start;

        System.out.printf("Direct: %d ms%n", directTime / 1_000_000);
        System.out.printf("Reflection: %d ms%n", reflectiveTime / 1_000_000);
        System.out.printf("Slowdown: %.1fx%n", (double) reflectiveTime / directTime);
    }
}
```
