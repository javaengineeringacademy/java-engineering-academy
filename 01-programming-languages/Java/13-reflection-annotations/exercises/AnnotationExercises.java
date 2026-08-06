package academy.javaengineering.exercises;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Exercises: Annotations (Create Custom Annotations, Process with Reflection)
 *
 * Complete the TODO sections below.
 */
public class AnnotationExercises {

    // TODO 1: Create a @Validate annotation
    // It should have: min (int), max (int), message (String)
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Validate {
        int min() default 0;
        int max() default Integer.MAX_VALUE;
        String message() default "Validation failed";
    }

    // TODO 2: Create a @JsonField annotation
    // It should have: name (String) - the JSON key name
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface JsonField {
        String name();
    }

    // TODO 3: Create a @Cacheable annotation
    // It should have: ttl (long, in seconds, default 300)
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Cacheable {
        long ttl() default 300;
    }

    // TODO 4: Implement a validator that checks @Validate annotations
    public static class ValidationProcessor {
        public static List<String> validate(Object obj) throws IllegalAccessException {
            List<String> errors = new ArrayList<>();
            // TODO: implement - check all fields with @Validate
            return errors;
        }
    }

    // TODO 5: Implement a JSON serializer using @JsonField
    public static class JsonSerializer {
        public static String toJson(Object obj) throws IllegalAccessException {
            // TODO: implement - convert object to JSON string
            return "";
        }
    }

    // TODO 6: Implement a cache tracker using @Cacheable
    public static class CacheTracker {
        private final Map<String, Long> cacheHits = new HashMap<>();

        public boolean shouldCache(Method method) {
            // TODO: implement - check if method has @Cacheable
            return false;
        }

        public long getTtl(Method method) {
            // TODO: implement - return TTL from @Cacheable
            return 0;
        }

        public void recordHit(String key) {
            cacheHits.put(key, cacheHits.getOrDefault(key, 0L) + 1);
        }

        public int getHitCount(String key) {
            return cacheHits.getOrDefault(key, 0L).intValue();
        }
    }

    // Test classes
    public static class User {
        @Validate(min = 1, max = 100, message = "Age must be between 1 and 100")
        private int age;

        @Validate(min = 2, message = "Name must be at least 2 characters")
        private String name;

        @JsonField(name = "user_name")
        private String displayName;

        public User(int age, String name, String displayName) {
            this.age = age;
            this.name = name;
            this.displayName = displayName;
        }
    }

    public static class ProductService {
        @Cacheable(ttl = 60)
        public String getProduct(String id) {
            return "Product-" + id;
        }

        @Cacheable(ttl = 120)
        public String getCategory(String id) {
            return "Category-" + id;
        }

        public String getUncached(String id) {
            return "Data-" + id;
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        AnnotationExercises exercises = new AnnotationExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== AnnotationExercises Tests ===\n");

        // Test 4 - Validation
        total++;
        User validUser = new User(25, "Alice", "Ali");
        List<String> errors = ValidationProcessor.validate(validUser);
        if (errors.isEmpty()) {
            System.out.println("Test 4a PASSED: validate - valid user");
            passed++;
        } else {
            System.out.println("Test 4a FAILED: validate - " + errors);
        }

        total++;
        User invalidUser = new User(150, "X", "X");
        errors = ValidationProcessor.validate(invalidUser);
        if (errors.size() >= 2) {
            System.out.println("Test 4b PASSED: validate - invalid user");
            passed++;
        } else {
            System.out.println("Test 4b FAILED: validate - expected 2+ errors, got " + errors.size());
        }

        // Test 5 - JSON Serializer
        total++;
        User jsonUser = new User(30, "Bob", "Bobby");
        String json = JsonSerializer.toJson(jsonUser);
        if (json.contains("age") && json.contains("30") && json.contains("user_name")) {
            System.out.println("Test 5 PASSED: JsonSerializer");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: JsonSerializer - " + json);
        }

        // Test 6 - Cache Tracker
        total++;
        CacheTracker tracker = new CacheTracker();
        Method getProduct = ProductService.class.getMethod("getProduct", String.class);
        Method getUncached = ProductService.class.getMethod("getUncached", String.class);
        if (tracker.shouldCache(getProduct) && !tracker.shouldCache(getUncached)) {
            System.out.println("Test 6a PASSED: shouldCache");
            passed++;
        } else {
            System.out.println("Test 6a FAILED: shouldCache");
        }

        total++;
        if (tracker.getTtl(getProduct) == 60) {
            System.out.println("Test 6b PASSED: getTtl");
            passed++;
        } else {
            System.out.println("Test 6b FAILED: getTtl - " + tracker.getTtl(getProduct));
        }

        total++;
        tracker.recordHit("product:123");
        tracker.recordHit("product:123");
        if (tracker.getHitCount("product:123") == 2) {
            System.out.println("Test 6c PASSED: CacheTracker hit counting");
            passed++;
        } else {
            System.out.println("Test 6c FAILED: CacheTracker hit counting");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
