package academy.javaengineering.reflection.annotations.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise1_ValidationAnnotation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NotEmpty { String message() default "Cannot be empty"; }

    public static List<String> validate(Object obj) throws Exception {
        List<String> errors = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            NotEmpty ann = field.getAnnotation(NotEmpty.class);
            if (ann == null) continue;
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                errors.add(field.getName() + ": " + ann.message());
            }
        }
        return errors;
    }

    static class User {
        @NotEmpty private String name;
        @NotEmpty private String email;
        private String phone;
        User(String name, String email, String phone) { this.name = name; this.email = email; this.phone = phone; }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(validate(new User("", "", "123")));
        System.out.println(validate(new User("Alice", "a@b.com", null)));
    }
}
