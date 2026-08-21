package academy.javaengineering.reflection.annotations.solutions;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class Solution2_CacheAnnotation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Cacheable { int ttlSeconds() default 300; }

    public static Map<String, Integer> getCacheConfig(Class<?> clazz) {
        Map<String, Integer> config = new LinkedHashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            Cacheable ann = method.getAnnotation(Cacheable.class);
            if (ann != null) config.put(method.getName(), ann.ttlSeconds());
        }
        return config;
    }

    static class DataService {
        @Cacheable(ttlSeconds = 60) public String getData(String k) { return k; }
        @Cacheable public int compute(int x) { return x * 2; }
        public void noCache() {}
    }

    public static void main(String[] args) {
        System.out.println(getCacheConfig(DataService.class));
    }
}
