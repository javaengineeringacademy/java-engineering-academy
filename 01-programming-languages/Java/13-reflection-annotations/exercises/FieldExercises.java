package reflection.exercises;

import java.lang.reflect.*;
import java.util.*;

/**
 * TOPIC 3 EXERCISES — Field Access
 * 5 practice problems.
 */
public class FieldExercises {

    // =========================================================================
    // EXERCISE 1: Read Private Field
    // =========================================================================
    /**
     * Given an object and a field name, read the value of that field
     * (even if private) using reflection. Return the value as an Object.
     *
     * TODO: Implement this method
     */
    public static Object readField(Object obj, String fieldName) throws Exception {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 2: Write Private Field
    // =========================================================================
    /**
     * Given an object, a field name, and a value, set that field on the object
     * using reflection (even if private). Handle type conversion for primitives.
     *
     * TODO: Implement this method
     */
    public static void writeField(Object obj, String fieldName, Object value) throws Exception {
        // TODO: Your code here
    }

    // =========================================================================
    // EXERCISE 3: Get All Field Names and Types
    // =========================================================================
    /**
     * Given a Class object, return a Map of field names to their type names
     * for ALL declared fields (including private).
     *
     * TODO: Implement this method
     */
    public static Map<String, String> getFieldMap(Class<?> clazz) {
        // TODO: Your code here
        return null;
    }

    // =========================================================================
    // EXERCISE 4: Copy All Fields Between Objects
    // =========================================================================
    /**
     * Copy all field values from source to target, matching by name and type.
     * Skip fields that don't exist in target or have incompatible types.
     * Both objects should be of the same class or related classes.
     *
     * TODO: Implement this method
     */
    public static void copyFields(Object source, Object target) throws Exception {
        // TODO: Your code here
    }

    // =========================================================================
    // EXERCISE 5: Count Fields by Modifier
    // =========================================================================
    /**
     * Given a Class object, return a Map where keys are modifier strings
     * (e.g., "private", "public static final") and values are the count
     * of fields with that exact modifier combination.
     *
     * TODO: Implement this method
     */
    public static Map<String, Integer> countFieldsByModifier(Class<?> clazz) {
        // TODO: Your code here
        return null;
    }
}
