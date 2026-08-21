package academy.javaengineering.reflection.realworld.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise2_MiniValidator {

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface Required {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface MinLength { int value(); }

    public static List<String> validate(Object obj) throws Exception {
        List<String> errors = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (field.isAnnotationPresent(Required.class)) {
                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    errors.add(field.getName() + " is required");
                }
            }

            MinLength minLen = field.getAnnotation(MinLength.class);
            if (minLen != null && value instanceof String) {
                if (((String) value).length() < minLen.value()) {
                    errors.add(field.getName() + " must be at least " + minLen.value() + " chars");
                }
            }
        }
        return errors;
    }

    static class User {
        @Required private String name;
        @MinLength(3) @Required private String password;
        private String phone;
        User(String n, String p, String ph) { name = n; password = p; phone = ph; }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(validate(new User("", "ab", "123")));
        System.out.println(validate(new User("Alice", "secret", null)));
    }
}
