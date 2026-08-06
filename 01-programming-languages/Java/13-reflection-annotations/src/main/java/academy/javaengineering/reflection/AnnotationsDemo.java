package academy.javaengineering.reflection;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates Java Annotation concepts including:
 * - Custom annotations with @Retention and @Target
 * - Reading annotations via reflection
 * - Built-in annotations: @Override, @Deprecated, @SuppressWarnings
 * - @FunctionalInterface analysis
 * - Annotation processing patterns
 */
public class AnnotationsDemo {

    // Custom annotation: Runtime retention (accessible via reflection)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @interface ApiInfo {
        String version() default "1.0";
        String author() default "unknown";
        String description() default "";
    }

    // Custom annotation: Source only (not available at runtime)
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @interface InternalUseOnly {
        String reason() default "internal";
    }

    // Custom annotation: Class file retention
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.TYPE)
    @interface Persistent {
        String tableName() default "";
    }

    // Custom annotation with Enum
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.FIELD})
    @interface CachePolicy {
        CacheLevel level() default CacheLevel.MEDIUM;
        int ttlSeconds() default 300;
    }

    enum CacheLevel {
        LOW, MEDIUM, HIGH
    }

    // Custom annotation with array values
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface ValidationRules {
        String[] requiredFields() default {};
        int minLength() default 0;
        int maxLength() default Integer.MAX_VALUE;
    }

    // Using custom annotations on a class
    @ApiInfo(version = "2.0", author = "Alice", description = "User entity")
    @Persistent(tableName = "users")
    @ValidationRules(requiredFields = {"name", "email"}, minLength = 2, maxLength = 100)
    static class User {
        @ApiInfo(description = "User identifier")
        private Long id;

        @ApiInfo(description = "User full name")
        private String name;

        @CachePolicy(level = CacheLevel.HIGH, ttlSeconds = 600)
        private String email;

        public User() {}

        public User(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
        }

        @Deprecated
        public String getLegacyFormat() {
            return id + ":" + name + ":" + email;
        }

        @InternalUseOnly(reason = "temporary migration helper")
        public String toMigrationFormat() {
            return String.format("%d|%s|%s", id, name, email);
        }
    }

    // Functional interface (annotated or not)
    @FunctionalInterface
    interface Processor<T, R> {
        R process(T input);

        // Default method is allowed in @FunctionalInterface
        default Processor<T, R> andThen(Processor<R, R> after) {
            return input -> after.process(this.process(input));
        }
    }

    // Non-functional interface (has two abstract methods)
    interface MultiOperation {
        void operation1();
        void operation2();
    }

    // Another functional interface (no annotation)
    interface SimpleFunction {
        int apply(int value);
    }

    /**
     * Demonstrates reading class-level annotations.
     */
    public static void demonstrateClassAnnotations() {
        System.out.println("=== Class-Level Annotations ===");
        Class<User> clazz = User.class;

        // Get all annotations on the class
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("Annotation: " + annotation.annotationType().getSimpleName());
            System.out.println("  Values:");
            Arrays.stream(annotation.annotationType().getDeclaredMethods()).forEach(method -> {
                try {
                    Object value = method.invoke(annotation);
                    System.out.println("    " + method.getName() + " = " + value);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    System.err.println("Cannot read annotation value: " + e.getMessage());
                }
            });
        }

        // Get specific annotation
        ApiInfo apiInfo = clazz.getAnnotation(ApiInfo.class);
        if (apiInfo != null) {
            System.out.println("\n@ApiInfo: version=" + apiInfo.version() + ", author=" + apiInfo.author());
        }

        // Check if annotation exists
        boolean hasPersistent = clazz.isAnnotationPresent(Persistent.class);
        System.out.println("Has @Persistent: " + hasPersistent);

        // Read ValidationRules annotation
        ValidationRules rules = clazz.getAnnotation(ValidationRules.class);
        if (rules != null) {
            System.out.println("@ValidationRules: required=" + Arrays.toString(rules.requiredFields())
                    + ", minLength=" + rules.minLength() + ", maxLength=" + rules.maxLength());
        }
    }

    /**
     * Demonstrates reading method-level annotations.
     */
    public static void demonstrateMethodAnnotations() {
        System.out.println("\n=== Method-Level Annotations ===");
        Class<User> clazz = User.class;

        for (var method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            if (annotations.length > 0) {
                System.out.println("Method: " + method.getName());
                for (Annotation annotation : annotations) {
                    System.out.println("  @" + annotation.annotationType().getSimpleName());
                    if (annotation instanceof CachePolicy cachePolicy) {
                        System.out.println("    level=" + cachePolicy.level()
                                + ", ttl=" + cachePolicy.ttlSeconds());
                    }
                }
            }

            // Check @Deprecated
            if (method.isAnnotationPresent(Deprecated.class)) {
                System.out.println("  [DEPRECATED]");
            }

            // Check @Override
            Override override = method.getAnnotation(Override.class);
            if (override != null) {
                System.out.println("  [OVERRIDE]");
            }
        }
    }

    /**
     * Demonstrates reading field-level annotations.
     */
    public static void demonstrateFieldAnnotations() {
        System.out.println("\n=== Field-Level Annotations ===");
        Class<User> clazz = User.class;

        for (var field : clazz.getDeclaredFields()) {
            ApiInfo apiInfo = field.getAnnotation(ApiInfo.class);
            if (apiInfo != null) {
                System.out.println("Field: " + field.getName() + " - " + apiInfo.description());
            }

            CachePolicy cachePolicy = field.getAnnotation(CachePolicy.class);
            if (cachePolicy != null) {
                System.out.println("Field: " + field.getName() + " - cache level="
                        + cachePolicy.level());
            }
        }
    }

    /**
     * Demonstrates annotation retention policies.
     */
    public static void demonstrateRetentionPolicies() {
        System.out.println("\n=== Retention Policies ===");

        // Runtime retention - accessible via reflection
        ApiInfo runtimeAnnotation = User.class.getAnnotation(ApiInfo.class);
        System.out.println("@ApiInfo (RUNTIME): " + (runtimeAnnotation != null ? "found" : "not found"));

        // Source retention - NOT accessible at runtime
        try {
            InternalUseOnly sourceAnnotation = User.class.getMethod("toMigrationFormat")
                    .getAnnotation(InternalUseOnly.class);
            System.out.println("@InternalUseOnly (SOURCE): " + (sourceAnnotation != null ? "found" : "not found"));
        } catch (NoSuchMethodException e) {
            System.out.println("@InternalUseOnly (SOURCE): method not found");
        }

        // Class retention - available in class file but not runtime
        Persistent classAnnotation = User.class.getAnnotation(Persistent.class);
        System.out.println("@Persistent (CLASS): " + (classAnnotation != null ? "found" : "not found"));
    }

    /**
     * Demonstrates analyzing @FunctionalInterface.
     */
    public static void demonstrateFunctionalInterfaceAnalysis() {
        System.out.println("\n=== Functional Interface Analysis ===");

        // Check if a class is a functional interface
        analyzeFunctionalInterface(Processor.class, "Processor");
        analyzeFunctionalInterface(SimpleFunction.class, "SimpleFunction");
        analyzeFunctionalInterface(MultiOperation.class, "MultiOperation");
        analyzeFunctionalInterface(Runnable.class, "Runnable");
        analyzeFunctionalInterface(java.util.Comparator.class, "Comparator");
    }

    private static void analyzeFunctionalInterface(Class<?> clazz, String name) {
        FunctionalInterface annotation = clazz.getAnnotation(FunctionalInterface.class);
        boolean isAnnotated = annotation != null;
        boolean isFunctional = clazz.isInterface() && countAbstractMethods(clazz) == 1;

        System.out.println(name + ":");
        System.out.println("  @FunctionalInterface: " + isAnnotated);
        System.out.println("  Is functional (1 abstract method): " + isFunctional);

        if (isAnnotated && !isFunctional) {
            System.out.println("  WARNING: @FunctionalInterface on non-functional interface!");
        }
    }

    private static int countAbstractMethods(Class<?> clazz) {
        int count = 0;
        for (var method : clazz.getDeclaredMethods()) {
            if (java.lang.reflect.Modifier.isAbstract(method.getModifiers())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Demonstrates using annotations for processing logic.
     */
    public static void demonstrateAnnotationProcessing() {
        System.out.println("\n=== Annotation Processing ===");

        // Build validation schema from annotations
        ValidationRules rules = User.class.getAnnotation(ValidationRules.class);
        if (rules != null) {
            System.out.println("Validation Schema for " + User.class.getSimpleName() + ":");
            System.out.println("  Required fields: " + Arrays.toString(rules.requiredFields()));
            System.out.println("  Min length: " + rules.minLength());
            System.out.println("  Max length: " + rules.maxLength());
        }

        // Build cache configuration from annotations
        for (var field : User.class.getDeclaredFields()) {
            CachePolicy policy = field.getAnnotation(CachePolicy.class);
            if (policy != null) {
                System.out.println("Cache config for field '" + field.getName() + "':");
                System.out.println("  Level: " + policy.level());
                System.out.println("  TTL: " + policy.ttlSeconds() + "s");
            }
        }

        // Build API documentation from annotations
        ApiInfo classApi = User.class.getAnnotation(ApiInfo.class);
        if (classApi != null) {
            System.out.println("API Documentation:");
            System.out.println("  Class: " + User.class.getSimpleName());
            System.out.println("  Version: " + classApi.version());
            System.out.println("  Author: " + classApi.author());
            System.out.println("  Description: " + classApi.description());
        }
    }

    /**
     * Demonstrates annotation inheritance and meta-annotations.
     */
    public static void demonstrateMetaAnnotations() {
        System.out.println("\n=== Meta-Annotations ===");

        // Get meta-annotations on @ApiInfo
        Class<ApiInfo> apiInfoClass = ApiInfo.class;
        System.out.println("Meta-annotations on @ApiInfo:");
        for (Annotation meta : apiInfoClass.getAnnotations()) {
            System.out.println("  @" + meta.annotationType().getSimpleName());
        }

        // Get target elements for @ApiInfo
        Target target = apiInfoClass.getAnnotation(Target.class);
        if (target != null) {
            System.out.println("@ApiInfo can be applied to:");
            for (ElementType element : target.value()) {
                System.out.println("  " + element);
            }
        }
    }

    public static void main(String[] args) {
        demonstrateClassAnnotations();
        demonstrateMethodAnnotations();
        demonstrateFieldAnnotations();
        demonstrateRetentionPolicies();
        demonstrateFunctionalInterfaceAnalysis();
        demonstrateAnnotationProcessing();
        demonstrateMetaAnnotations();
    }
}
