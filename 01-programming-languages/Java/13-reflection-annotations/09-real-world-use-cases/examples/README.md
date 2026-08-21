# Examples: Real-World Use Cases

## Example 1: Mini Spring DI Container

```java
package academy.javaengineering.reflection.realworld;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class MiniSpring {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Component {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Autowired {}

    private final Map<Class<?>, Object>容器 = new HashMap<>();

    public void scan(Class<?>... classes) throws Exception {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
               容器.put(clazz, instance);

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Object dependency =容器.get(field.getType());
                        if (dependency == null) {
                            dependency = field.getType().getDeclaredConstructor().newInstance();
                           容器.put(field.getType(), dependency);
                        }
                        field.setAccessible(true);
                        field.set(instance, dependency);
                    }
                }
            }
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(容器.get(clazz));
    }

    @Component
    static class UserRepository {
        public String findById(int id) { return "User" + id; }
    }

    @Component
    static class UserService {
        @Autowired private UserRepository repository;
        public String getUser(int id) { return repository.findById(id); }
    }

    public static void main(String[] args) throws Exception {
        MiniSpring app = new MiniSpring();
        app.scan(UserRepository.class, UserService.class);
        UserService service = app.getBean(UserService.class);
        System.out.println(service.getUser(1));
    }
}
```

## Example 2: Mini JUnit Runner

```java
package academy.javaengineering.reflection.realworld;

import java.lang.annotation.*;
import java.lang.reflect.*;

public class MiniJUnit {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface BeforeEach {}

    public static void runTests(Class<?> testClass) throws Exception {
        Object instance = testClass.getDeclaredConstructor().newInstance();

        Method beforeEach = null;
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(BeforeEach.class)) {
                beforeEach = m;
                break;
            }
        }

        int passed = 0, failed = 0;
        for (Method m : testClass.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(Test.class)) continue;
            try {
                if (beforeEach != null) beforeEach.invoke(instance);
                m.invoke(instance);
                passed++;
                System.out.println("PASS: " + m.getName());
            } catch (Exception e) {
                failed++;
                System.out.println("FAIL: " + m.getName() + " - " + e.getCause());
            }
        }
        System.out.println("\nResults: " + passed + " passed, " + failed + " failed");
    }

    static class CalculatorTest {
        @BeforeEach void setUp() { System.out.println("  [setup]"); }
        @Test void addition() { assert 1 + 1 == 2; }
        @Test void subtraction() { assert 5 - 3 == 2; }
        @Test void failing() { assert 1 + 1 == 3; }
    }

    public static void main(String[] args) {
        runTests(CalculatorTest.class);
    }
}
```

## Example 3: Mini JSON Serializer

```java
package academy.javaengineering.reflection.realworld;

import java.lang.reflect.Field;
import java.util.StringJoiner;

public class MiniJson {

    public static String toJson(Object obj) throws Exception {
        StringBuilder sb = new StringBuilder("{");
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(obj);

            sb.append("\"").append(fields[i].getName()).append("\":");
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value);
            } else if (value == null) {
                sb.append("null");
            } else {
                sb.append(toJson(value));
            }

            if (i < fields.length - 1) sb.append(",");
        }

        sb.append("}");
        return sb.toString();
    }

    static class User {
        String name = "Alice";
        int age = 30;
        boolean active = true;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(toJson(new User()));
    }
}
```
