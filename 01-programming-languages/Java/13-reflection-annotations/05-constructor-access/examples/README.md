# Examples: Constructor Access

## Example 1: Basic Constructor Instantiation

```java
package academy.javaengineering.reflection.constructor;

import java.lang.reflect.Constructor;

public class BasicConstructorExample {

    static class User {
        private String name;
        private int age;

        public User() { this.name = "Unknown"; this.age = 0; }
        public User(String name) { this.name = name; this.age = 0; }
        public User(String name, int age) { this.name = name; this.age = age; }
        private User(int id) { this.name = "User" + id; this.age = 0; }

        @Override public String toString() { return "User{name='" + name + "', age=" + age + "}"; }
    }

    public static void main(String[] args) throws Exception {
        Constructor<User> ctor1 = User.class.getDeclaredConstructor();
        System.out.println(ctor1.newInstance());

        Constructor<User> ctor2 = User.class.getDeclaredConstructor(String.class);
        System.out.println(ctor2.newInstance("Alice"));

        Constructor<User> ctor3 = User.class.getDeclaredConstructor(String.class, int.class);
        System.out.println(ctor3.newInstance("Bob", 30));

        Constructor<User> ctor4 = User.class.getDeclaredConstructor(int.class);
        ctor4.setAccessible(true);
        System.out.println(ctor4.newInstance(42));
    }
}
```

## Example 2: Dynamic Instantiator

```java
package academy.javaengineering.reflection.constructor;

import java.lang.reflect.Constructor;

public class DynamicInstantiator {

    public static <T> T create(Class<T> clazz, Object... args) throws Exception {
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
            if (paramTypes[i] == Integer.class) paramTypes[i] = int.class;
            if (paramTypes[i] == Long.class) paramTypes[i] = long.class;
            if (paramTypes[i] == Double.class) paramTypes[i] = double.class;
            if (paramTypes[i] == Boolean.class) paramTypes[i] = boolean.class;
        }

        Constructor<T> ctor = clazz.getDeclaredConstructor(paramTypes);
        ctor.setAccessible(true);
        return ctor.newInstance(args);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(create(StringBuilder.class));
        System.out.println(create(String.class, "hello".getBytes()));
    }
}
```

## Example 3: Constructor Metadata Inspector

```java
package academy.javaengineering.reflection.constructor;

import java.lang.reflect.*;
import java.util.*;

public class ConstructorMetadataInspector {

    public static void inspect(Class<?> clazz) {
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            System.out.printf("%s %s(%s) throws %s%n",
                Modifier.toString(ctor.getModifiers()),
                clazz.getSimpleName(),
                Arrays.toString(ctor.getParameterTypes()),
                Arrays.toString(ctor.getExceptionTypes()));
        }
    }

    public static void main(String[] args) {
        inspect(StringBuilder.class);
    }
}
```
