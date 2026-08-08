package reflection.solutions;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class AnnotationSolutions {

    // === @NotEmpty annotation definition ===

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NotEmpty {
        String message() default "Field cannot be empty";
    }

    public static List<String> validateNotEmpty(Object obj) throws IllegalAccessException {
        List<String> errors = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(NotEmpty.class)) continue;
            field.setAccessible(true);
            Object value = field.get(obj);
            NotEmpty annotation = field.getAnnotation(NotEmpty.class);
            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                errors.add(field.getName() + ": " + annotation.message());
            }
        }
        return errors;
    }

    // === FIND FIELDS WITH ANNOTATION PREFIX ===

    public static Map<String, List<String>> findFieldsWithAnnotationPrefix(
            Class<?> clazz, String prefix) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            List<String> matching = new ArrayList<>();
            for (Annotation ann : field.getAnnotations()) {
                if (ann.annotationType().getSimpleName().startsWith(prefix)) {
                    matching.add(ann.annotationType().getSimpleName());
                }
            }
            if (!matching.isEmpty()) result.put(field.getName(), matching);
        }
        return result;
    }

    // === ANNOTATION TO MAP ===

    public static Map<String, Object> annotationToMap(Annotation annotation) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            try {
                map.put(method.getName(), method.invoke(annotation));
            } catch (Exception e) { /* skip */ }
        }
        return map;
    }

    // === FIND ALL ANNOTATIONS ===

    public static <T extends Annotation> List<T> findAllAnnotations(
            Class<?> clazz, Class<T> annotationType) {
        List<T> result = new ArrayList<>();
        T ann = clazz.getAnnotation(annotationType);
        if (ann != null) result.add(ann);
        T[] repeated = clazz.getAnnotationsByType(annotationType);
        for (T a : repeated) {
            if (!result.contains(a)) result.add(a);
        }
        return result;
    }

    // === @ConfigKey annotation definition ===

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ConfigKey {
        String value();
    }

    public static void loadConfig(Object obj, Map<String, String> config) throws Exception {
        for (Field field : obj.getClass().getDeclaredFields()) {
            ConfigKey ck = field.getAnnotation(ConfigKey.class);
            if (ck == null) continue;
            String key = ck.value();
            if (!config.containsKey(key)) continue;
            String val = config.get(key);
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == String.class) field.set(obj, val);
            else if (type == int.class) field.setInt(obj, Integer.parseInt(val));
            else if (type == boolean.class) field.setBoolean(obj, Boolean.parseBoolean(val));
            else if (type == double.class) field.setDouble(obj, Double.parseDouble(val));
        }
    }
}
