# Examples: Custom Annotations

## Example 1: Basic Custom Annotation

```java
package academy.javaengineering.reflection.annotations;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Description {
    String value();
    String author() default "unknown";
    int version() default 1;
}

@Description(value = "User entity", author = "Alice", version = 2)
class User {}

public class BasicAnnotationExample {
    public static void main(String[] args) {
        Description ann = User.class.getAnnotation(Description.class);
        System.out.println("Description: " + ann.value());
        System.out.println("Author: " + ann.author());
        System.out.println("Version: " + ann.version());
    }
}
```

## Example 2: Field-Level Annotations

```java
package academy.javaengineering.reflection.annotations;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface JsonField {
    String name();
    boolean required() default false;
}

class User {
    @JsonField(name = "user_name", required = true)
    private String name;

    @JsonField(name = "user_age")
    private int age;

    private String password;
}

public class FieldAnnotationExample {
    public static void main(String[] args) throws Exception {
        for (Field field : User.class.getDeclaredFields()) {
            JsonField ann = field.getAnnotation(JsonField.class);
            if (ann != null) {
                System.out.println(field.getName() + " -> " + ann.name() +
                    " (required=" + ann.required() + ")");
            }
        }
    }
}
```

## Example 3: Repeatable Annotations

```java
package academy.javaengineering.reflection.annotations;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Roles.class)
@interface Role {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Roles {
    Role[] value();
}

@Role("admin")
@Role("editor")
@Role("viewer")
class AdminUser {}

public class RepeatableAnnotationExample {
    public static void main(String[] args) {
        Role[] roles = AdminUser.class.getAnnotationsByType(Role.class);
        for (Role role : roles) {
            System.out.println("Role: " + role.value());
        }
    }
}
```
