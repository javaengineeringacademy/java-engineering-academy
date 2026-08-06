package academy.javaengineering.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Demonstrates field manipulation via reflection including:
 * - Reading and writing private fields
 * - Working with static fields
 * - Modifying final fields
 * - Bulk field operations
 * - Field metadata extraction
 */
public class FieldManipulation {

    public String publicField = "public";
    protected String protectedField = "protected";
    String packageField = "package";
    private String privateField = "private";
    private final String finalField = "immutable";
    private static String staticField = "static_value";
    private static final String STATIC_FINAL = "static_immutable";
    private int numberField = 42;
    private static int staticNumber = 100;

    public FieldManipulation() {}

    public FieldManipulation(String publicField, String privateField, int numberField) {
        this.publicField = publicField;
        this.privateField = privateField;
        this.numberField = numberField;
    }

    /**
     * Reads a field value from an object, bypassing access restrictions.
     */
    public static Object readField(Object obj, String fieldName) throws ReflectiveOperationException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * Writes a value to a field on an object, bypassing access restrictions.
     */
    public static void writeField(Object obj, String fieldName, Object value) throws ReflectiveOperationException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * Demonstrates reading all fields of an object.
     */
    public static void demonstrateReadingFields() {
        System.out.println("=== Reading Fields ===");
        FieldManipulation obj = new FieldManipulation("pub", "priv", 99);
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                System.out.println(field.getName() + " = " + value
                        + " (type=" + field.getType().getSimpleName()
                        + ", modifiers=" + Modifier.toString(field.getModifiers()) + ")");
            } catch (IllegalAccessException e) {
                System.out.println(field.getName() + " = <inaccessible>");
            }
        }
    }

    /**
     * Demonstrates writing to private fields.
     */
    public static void demonstrateWritingPrivateFields() {
        System.out.println("\n=== Writing Private Fields ===");
        FieldManipulation obj = new FieldManipulation();
        Class<?> clazz = obj.getClass();

        // Modify privateField
        try {
            Field privateField = clazz.getDeclaredField("privateField");
            privateField.setAccessible(true);
            System.out.println("Before: " + privateField.get(obj));
            privateField.set(obj, "modified_via_reflection");
            System.out.println("After: " + privateField.get(obj));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Cannot modify privateField: " + e.getMessage());
        }

        // Modify numberField
        try {
            Field numberField = clazz.getDeclaredField("numberField");
            numberField.setAccessible(true);
            System.out.println("\nBefore: " + numberField.get(obj));
            numberField.setInt(obj, 999);
            System.out.println("After: " + numberField.get(obj));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Cannot modify numberField: " + e.getMessage());
        }
    }

    /**
     * Demonstrates reading and writing static fields.
     */
    public static void demonstrateStaticFieldManipulation() {
        System.out.println("\n=== Static Field Manipulation ===");
        Class<?> clazz = FieldManipulation.class;

        try {
            // Read static field
            Field staticField = clazz.getDeclaredField("staticField");
            staticField.setAccessible(true);
            System.out.println("staticField (from class): " + staticField.get(null));

            // Write static field (pass null as instance)
            staticField.set(null, "new_static_value");
            System.out.println("staticField (after write): " + staticField.get(null));

            // Read/write static final
            Field staticFinal = clazz.getDeclaredField("STATIC_FINAL");
            staticFinal.setAccessible(true);
            System.out.println("STATIC_FINAL: " + staticFinal.get(null));

            // Write static number
            Field staticNumber = clazz.getDeclaredField("staticNumber");
            staticNumber.setAccessible(true);
            System.out.println("staticNumber before: " + staticNumber.getInt(null));
            staticNumber.setInt(null, 999);
            System.out.println("staticNumber after: " + staticNumber.getInt(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Static field manipulation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates modifying final fields (Java 17+ allows this with --add-opens).
     */
    public static void demonstrateFinalFieldManipulation() {
        System.out.println("\n=== Final Field Manipulation ===");
        FieldManipulation obj = new FieldManipulation();
        Class<?> clazz = obj.getClass();

        try {
            Field finalField = clazz.getDeclaredField("finalField");
            finalField.setAccessible(true);

            // Remove final modifier
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(finalField, finalField.getModifiers() & ~Modifier.FINAL);

            System.out.println("Before: " + finalField.get(obj));
            finalField.set(obj, "changed_final");
            System.out.println("After: " + finalField.get(obj));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // On some JVMs/configurations, final field modification may throw
            System.out.println("Final field modification result: " + e.getMessage());
            System.out.println("Note: Final field modification may be restricted by JVM security.");
        }
    }

    /**
     * Demonstrates bulk field operations: copy all fields between objects.
     */
    public static Map<String, Object> copyAllFields(Object source, Object target) throws ReflectiveOperationException {
        Map<String, Object> changes = new LinkedHashMap<>();
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        for (Field sourceField : sourceClass.getDeclaredFields()) {
            try {
                Field targetField = targetClass.getDeclaredField(sourceField.getName());
                if (targetField.getType().equals(sourceField.getType())) {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);

                    Object oldValue = targetField.get(target);
                    Object newValue = sourceField.get(source);
                    targetField.set(target, newValue);
                    changes.put(sourceField.getName(), newValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // Skip fields that don't exist or aren't accessible
            }
        }
        return changes;
    }

    /**
     * Demonstrates field metadata extraction.
     */
    public static void demonstrateFieldMetadata() {
        System.out.println("\n=== Field Metadata ===");
        Class<?> clazz = FieldManipulation.class;

        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("Field: " + field.getName());
            System.out.println("  Type: " + field.getType().getName());
            System.out.println("  Declaring class: " + field.getDeclaringClass().getSimpleName());
            System.out.println("  Modifiers: " + Modifier.toString(field.getModifiers()));
            System.out.println("  Is synthetic: " + field.isSynthetic());
            System.out.println("  Is enum constant: " + field.isEnumConstant());
            System.out.println("  Generic type: " + field.getGenericType());
        }
    }

    /**
     * Demonstrates converting an object's fields to a Map.
     */
    public static Map<String, Object> objectToMap(Object obj) throws ReflectiveOperationException {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    /**
     * Demonstrates populating an object from a Map.
     */
    public static void mapToObject(Map<String, Object> map, Object target) throws ReflectiveOperationException {
        Class<?> clazz = target.getClass();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(target, entry.getValue());
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                // Skip fields that don't exist or aren't accessible
            }
        }
    }

    public static void main(String[] args) {
        demonstrateReadingFields();
        demonstrateWritingPrivateFields();
        demonstrateStaticFieldManipulation();
        demonstrateFinalFieldManipulation();
        demonstrateFieldMetadata();

        try {
            System.out.println("\n=== Object to Map ===");
            FieldManipulation obj = new FieldManipulation("a", "b", 1);
            Map<String, Object> map = objectToMap(obj);
            map.forEach((k, v) -> System.out.println(k + " = " + v));

            System.out.println("\n=== Map to Object ===");
            FieldManipulation target = new FieldManipulation();
            Map<String, Object> data = Map.of(
                    "publicField", "from_map",
                    "privateField", "also_from_map",
                    "numberField", 123
            );
            mapToObject(data, target);
            System.out.println("publicField: " + target.publicField);
            System.out.println("privateField: " + (String) readField(target, "privateField"));
            System.out.println("numberField: " + target.numberField);
        } catch (ReflectiveOperationException e) {
            System.err.println("Field manipulation error: " + e.getMessage());
        }
    }
}
