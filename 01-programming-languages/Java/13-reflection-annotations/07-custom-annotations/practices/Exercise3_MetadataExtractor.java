package academy.javaengineering.reflection.annotations.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise3_MetadataExtractor {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ApiEndpoint {
        String path();
        String method() default "GET";
        String description() default "";
    }

    public static Map<String, Object> extractMetadata(Class<?> clazz) {
        Map<String, Object> meta = new LinkedHashMap<>();
        ApiEndpoint ann = clazz.getAnnotation(ApiEndpoint.class);
        if (ann != null) {
            meta.put("path", ann.path());
            meta.put("method", ann.method());
            meta.put("description", ann.description());
        }
        return meta;
    }

    @ApiEndpoint(path = "/api/users", method = "GET", description = "List all users")
    static class UserController {}

    public static void main(String[] args) {
        System.out.println(extractMetadata(UserController.class));
    }
}
