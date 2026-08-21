# Examples: Field Access

## Example 1: Reading Private Fields

```java
package academy.javaengineering.reflection.fieldaccess;

import java.lang.reflect.Field;

public class PrivateFieldReadExample {

    static class User {
        private String name;
        private int age;
        private boolean active;

        User(String name, int age, boolean active) {
            this.name = name;
            this.age = age;
            this.active = active;
        }
    }

    public static void main(String[] args) throws Exception {
        User user = new User("Alice", 30, true);

        Field nameField = User.class.getDeclaredField("name");
        nameField.setAccessible(true);
        System.out.println("Name: " + nameField.get(user));

        Field ageField = User.class.getDeclaredField("age");
        ageField.setAccessible(true);
        System.out.println("Age: " + ageField.getInt(user));

        Field activeField = User.class.getDeclaredField("active");
        activeField.setAccessible(true);
        System.out.println("Active: " + activeField.getBoolean(user));
    }
}
```

## Example 2: Modifying Fields

```java
package academy.javaengineering.reflection.fieldaccess;

import java.lang.reflect.Field;

public class FieldModifyExample {

    static class Config {
        private String host = "localhost";
        private int port = 8080;
        private boolean ssl = false;
    }

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        System.out.println("Before: " + config.host + ":" + config.port);

        Field hostField = Config.class.getDeclaredField("host");
        hostField.setAccessible(true);
        hostField.set(config, "example.com");

        Field portField = Config.class.getDeclaredField("port");
        portField.setAccessible(true);
        portField.setInt(config, 443);

        Field sslField = Config.class.getDeclaredField("ssl");
        sslField.setAccessible(true);
        sslField.setBoolean(config, true);

        System.out.println("After: " + config.host + ":" + config.port + " ssl=" + config.ssl);
    }
}
```

## Example 3: Static Field Access

```java
package academy.javaengineering.reflection.fieldaccess;

import java.lang.reflect.Field;

public class StaticFieldExample {

    static class SystemProps {
        public static final String LINE_SEP = System.lineSeparator();
        private static int instanceCount = 0;
    }

    public static void main(String[] args) throws Exception {
        Field lineSepField = SystemProps.class.getDeclaredField("LINE_SEP");
        lineSepField.setAccessible(true);
        System.out.println("Line separator: " + lineSepField.get(null));

        Field countField = SystemProps.class.getDeclaredField("instanceCount");
        countField.setAccessible(true);
        System.out.println("Count: " + countField.get(null));
        countField.set(null, 42);
        System.out.println("New count: " + countField.get(null));
    }
}
```

## Example 4: Field Copier

```java
package academy.javaengineering.reflection.fieldaccess;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldCopier {

    public static void copyFields(Object source, Object target) throws Exception {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        for (Field sourceField : sourceClass.getDeclaredFields()) {
            if (Modifier.isStatic(sourceField.getModifiers())) continue;

            try {
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                if (!sourceField.getType().equals(targetField.getType())) continue;

                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                targetField.set(target, sourceField.get(source));
            } catch (NoSuchFieldException ignored) {
            }
        }
    }

    static class Source { private String name = "Alice"; private int age = 30; }
    static class Target { private String name; private int age; }

    public static void main(String[] args) throws Exception {
        Source src = new Source();
        Target tgt = new Target();
        copyFields(src, tgt);
        System.out.println("Copied: name=" + tgt.name + ", age=" + tgt.age);
    }
}
```
