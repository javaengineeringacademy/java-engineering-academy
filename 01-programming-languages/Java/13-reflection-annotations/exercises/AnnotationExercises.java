package reflection.exercises;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * TOPIC 7 & 8 EXERCISES — Custom Annotations and Annotation Processing
 * 5 practice problems.
 */
public class AnnotationExercises {

    // =========================================================================
    // EXERCISE 1: Define a Validation Annotation
    // =========================================================================
    /**
     * Create an annotation called @NotEmpty that:
     * - Can be applied to fields
     * - Is available at runtime
     * - Has a message() element with default "Field cannot be empty"
     *
     * TODO: Define the annotation and implement the validator
     */

    // TODO: Define @NotEmpty annotation here

    /**
     * Validate an object: for every field annotated with @NotEmpty,
     * check that the field value is not null and not an empty string.
     * Return a list of error messages for all invalid fields.
     *
     * TODO: Implement this method
     */
    public static List<String> validateNotEmpty(Object obj) throws IllegalAccessException {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: Read Multiple Annotations
    // =========================================================================
    /**
     * Given a Class, find all fields that have ANY annotation from the
     * java.lang package (@Deprecated, @FunctionalInterface is not on fields,
     * but @SuppressWarnings is not either — use reflection to check).
     * Actually, check for annotations whose simple name starts with a given prefix.
     * Return a Map of field name to list of annotation simple names.
     *
     * TODO: Implement this method
     */
    public static Map<String, List<String>> findFieldsWithAnnotationPrefix(
            Class<?> clazz, String prefix) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 3: Annotation-to-Map Converter
    // =========================================================================
    /**
     * Convert any annotation instance to a Map<String, Object> where
     * keys are element names and values are element values.
     * Handle all annotation element types.
     *
     * TODO: Implement this method
     */
    public static Map<String, Object> annotationToMap(Annotation annotation) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: Find All Repeatable Annotations
    // =========================================================================
    /**
     * Given a class and an annotation type, find ALL instances of that
     * annotation on the class (including repeatable ones). Return them
     * as a list.
     *
     * TODO: Implement this method
     */
    public static <T extends Annotation> List<T> findAllAnnotations(
            Class<?> clazz, Class<T> annotationType) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 5: Annotation-Based Configuration Loader
    // =========================================================================
    /**
     * Given an object and a Map<String, String> of configuration values,
     * for every field annotated with @ConfigKey (define it), set the field
     * value from the config map using the annotation's value() as the key.
     * Handle String, int, boolean, and double types.
     *
     * TODO: Define @ConfigKey and implement the loader
     */

    // TODO: Define @ConfigKey annotation here

    /**
     * Load configuration values into an object's @ConfigKey fields.
     *
     * TODO: Implement this method
     */
    public static void loadConfig(Object obj, Map<String, String> config) throws Exception {
        // TODO: Your code here
    }
}
