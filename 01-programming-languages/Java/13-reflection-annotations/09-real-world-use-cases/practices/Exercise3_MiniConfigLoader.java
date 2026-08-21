package academy.javaengineering.reflection.realworld.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Exercise3_MiniConfigLoader {

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface Config { String key(); String defaultValue() default ""; }

    public static <T> T load(Class<T> clazz, Map<String, String> props) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            Config ann = field.getAnnotation(Config.class);
            if (ann == null) continue;
            String value = props.getOrDefault(ann.key(), ann.defaultValue());
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == String.class) field.set(instance, value);
            else if (type == int.class) field.setInt(instance, Integer.parseInt(value));
            else if (type == boolean.class) field.setBoolean(instance, Boolean.parseBoolean(value));
            else if (type == double.class) field.setDouble(instance, Double.parseDouble(value));
        }
        return instance;
    }

    static class AppConfig {
        @Config(key = "app.host", defaultValue = "localhost") private String host;
        @Config(key = "app.port", defaultValue = "8080") private int port;
        @Config(key = "app.ssl", defaultValue = "true") private boolean ssl;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> props = Map.of("app.host", "example.com", "app.port", "443");
        AppConfig config = load(AppConfig.class, props);
        System.out.println("host=" + config.host + " port=" + config.port + " ssl=" + config.ssl);
    }
}
