package academy.javaengineering.reflection.fieldaccess.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise2_AnnotationAwareReader {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface JsonField { String value() default ""; }

    @JsonField("user_name") private String name = "Alice";
    @JsonField("user_age") private int age = 30;
    private String ignored = "not serialized";

    public static Map<String, Object> readAnnotatedFields(Object obj) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            JsonField ann = field.getAnnotation(JsonField.class);
            if (ann != null) {
                field.setAccessible(true);
                result.put(ann.value(), field.get(obj));
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(readAnnotatedFields(new Exercise2_AnnotationAwareReader()));
    }
}
