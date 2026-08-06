package reflection;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * CustomAnnotations - Creating and using annotations
 *
 * Covers:
 * - Built-in annotations
 * - Creating custom annotations
 * - Annotation retention policies
 * - Processing annotations
 */
public class CustomAnnotations {

    // Custom annotation for validation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface NotNull {
        String message() default "Field cannot be null";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Range {
        int min() default 0;
        int max() default Integer.MAX_VALUE;
        String message() default "Value out of range";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Deprecated {
        String reason() default "No longer used";
        String since() default "1.0";
    }

    // Class using custom annotations
    static class User {
        @NotNull(message = "Name is required")
        private String name;

        @Range(min = 0, max = 150, message = "Age must be between 0 and 150")
        private int age;

        @NotNull(message = "Email is required")
        private String email;

        public User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }

        @Deprecated(reason = "Use getFullName() instead", since = "2.0")
        public String getName() {
            return name;
        }

        public String getFullName() {
            return name + " <" + email + ">";
        }

        public int getAge() {
            return age;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Built-in Annotations ===");
        builtInAnnotations();

        System.out.println("\n=== Custom Annotation Definitions ===");
        customAnnotationDefinitions();

        System.out.println("\n=== Reading Annotations ===");
        readingAnnotations();

        System.out.println("\n=== Validating with Annotations ===");
        validatingWithAnnotations();
    }

    static void builtInAnnotations() {
        System.out.println("Common Built-in Annotations:");
        System.out.println();
        System.out.println("@Override - Method overrides superclass method");
        System.out.println("@Deprecated - Element is deprecated");
        System.out.println("@SuppressWarnings - Suppress compiler warnings");
        System.out.println("@FunctionalInterface - Interface is functional");
        System.out.println("@SafeVarargs - Suppress heap pollution warnings");
        System.out.println();
        System.out.println("Java 8+:");
        System.out.println("@Repeatable - Annotation can be applied multiple times");
        System.out.println();
        System.out.println("Java 9+:");
        System.out.println("@Native - Native method declaration");
    }

    static void customAnnotationDefinitions() {
        System.out.println("Custom Annotation Structure:");
        System.out.println();
        System.out.println("@Retention(RetentionPolicy.RUNTIME)");
        System.out.println("@Target(ElementType.FIELD)");
        System.out.println("@interface NotNull {");
        System.out.println("    String message() default \"Cannot be null\";");
        System.out.println("}");
        System.out.println();
        System.out.println("Retention Policies:");
        System.out.println("  SOURCE - Not retained in .class file");
        System.out.println("  CLASS - Retained in .class, not available at runtime");
        System.out.println("  RUNTIME - Available at runtime via reflection");
        System.out.println();
        System.out.println("Target Types:");
        System.out.println("  TYPE - Class, interface, enum");
        System.out.println("  FIELD - Field declaration");
        System.out.println("  METHOD - Method declaration");
        System.out.println("  PARAMETER - Method/constructor parameter");
        System.out.println("  CONSTRUCTOR - Constructor declaration");
    }

    static void readingAnnotations() {
        System.out.println("Reading annotations on User class:");
        System.out.println();

        Class<?> clazz = User.class;

        // Read field annotations
        System.out.println("Field annotations:");
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            Annotation[] annotations = field.getAnnotations();
            if (annotations.length > 0) {
                System.out.println("  " + field.getName() + ":");
                for (Annotation annotation : annotations) {
                    System.out.println("    @" + annotation.annotationType().getSimpleName());
                }
            }
        }

        // Read method annotations
        System.out.println("\nMethod annotations:");
        for (Method method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            if (annotations.length > 0) {
                System.out.println("  " + method.getName() + "():");
                for (Annotation annotation : annotations) {
                    System.out.println("    @" + annotation.annotationType().getSimpleName());
                }
            }
        }
    }

    static void validatingWithAnnotations() {
        System.out.println("Validating User objects:");
        System.out.println();

        User validUser = new User("John", 25, "john@example.com");
        User invalidUser = new User(null, 200, null);

        validateUser(validUser);
        validateUser(invalidUser);
    }

    static void validateUser(User user) {
        System.out.println("Validating: " + user.getName());
        boolean valid = true;

        for (java.lang.reflect.Field field : User.class.getDeclaredFields()) {
            // Check @NotNull
            if (field.isAnnotationPresent(NotNull.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(user);
                    if (value == null) {
                        NotNull annotation = field.getAnnotation(NotNull.class);
                        System.out.println("  ERROR: " + annotation.message());
                        valid = false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // Check @Range
            if (field.isAnnotationPresent(Range.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(user);
                    if (value instanceof Integer) {
                        int intValue = (Integer) value;
                        Range annotation = field.getAnnotation(Range.class);
                        if (intValue < annotation.min() || intValue > annotation.max()) {
                            System.out.println("  ERROR: " + annotation.message());
                            valid = false;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("  Result: " + (valid ? "VALID" : "INVALID"));
        System.out.println();
    }
}