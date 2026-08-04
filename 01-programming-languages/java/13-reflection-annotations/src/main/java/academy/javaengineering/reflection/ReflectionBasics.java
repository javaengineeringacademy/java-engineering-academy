package academy.javaengineering.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates fundamental Java Reflection concepts including:
 * - Obtaining Class objects via multiple mechanisms
 * - Inspecting fields, methods, and constructors
 * - Accessing and modifying private members
 * - Creating instances dynamically
 * - Working with modifiers
 */
public class ReflectionBasics {

    // Public field
    public String name;

    // Private field
    private int age;

    // Final field
    private final String id;

    // Static field
    private static int instanceCount = 0;

    // Constructor
    public ReflectionBasics(String name, int age, String id) {
        this.name = name;
        this.age = age;
        this.id = id;
        instanceCount++;
    }

    // Private constructor
    private ReflectionBasics(String name) {
        this(name, 0, "default");
    }

    // Default constructor (package-private)
    ReflectionBasics() {
        this("unknown", -1, "none");
    }

    // Public method
    public String getInfo() {
        return name + ", age " + age + ", id=" + id;
    }

    // Private method
    private String secretMethod() {
        return "Secret: " + id;
    }

    // Static method
    public static int getInstanceCount() {
        return instanceCount;
    }

    // Method with parameters
    public boolean isOlderThan(int otherAge) {
        return this.age > otherAge;
    }

    // Method with varargs
    public String concatenate(String... parts) {
        return String.join(" ", parts);
    }

    /**
     * Demonstrates obtaining Class objects via all three mechanisms:
     * 1. Class.forName(String className)
     * 2. .class literal
     * 3. object.getClass()
     */
    public static void demonstrateClassObjectCreation() {
        System.out.println("=== Class Object Creation ===");

        // Method 1: Class.forName()
        try {
            Class<?> stringClass = Class.forName("java.lang.String");
            System.out.println("Class.forName(\"java.lang.String\"): " + stringClass.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Method 2: .class literal
        Class<?> integerClass = Integer.class;
        System.out.println("Integer.class: " + integerClass.getName());

        // Method 3: getClass()
        ReflectionBasics obj = new ReflectionBasics("Alice", 30, "ID001");
        Class<?> objClass = obj.getClass();
        System.out.println("obj.getClass(): " + objClass.getName());

        // All three produce the same Class object for the same type
        System.out.println("String.class == Class.forName(\"java.lang.String\"): " +
                (String.class == objClass));
    }

    /**
     * Retrieves and displays all declared fields of a class (including private ones).
     */
    public static List<Field> getDeclaredFields(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields());
    }

    /**
     * Demonstrates accessing fields via reflection, including private fields.
     */
    public static void demonstrateFieldAccess() {
        System.out.println("\n=== Field Access ===");
        ReflectionBasics obj = new ReflectionBasics("Bob", 25, "ID002");
        Class<?> clazz = obj.getClass();

        // Get all declared fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("Field: " + field.getName() + " | Type: " + field.getType().getSimpleName()
                    + " | Modifiers: " + Modifier.toString(field.getModifiers()));

            // Make private fields accessible
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                System.out.println("  Value: " + value);
            } catch (IllegalAccessException e) {
                System.err.println("  Cannot access: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves and displays all declared methods of a class (including private ones).
     */
    public static List<Method> getDeclaredMethods(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredMethods());
    }

    /**
     * Demonstrates invoking methods via reflection.
     */
    public static void demonstrateMethodInvocation() {
        System.out.println("\n=== Method Invocation ===");
        ReflectionBasics obj = new ReflectionBasics("Charlie", 35, "ID003");
        Class<?> clazz = obj.getClass();

        // Invoke public method
        try {
            Method getInfo = clazz.getMethod("getInfo");
            String result = (String) getInfo.invoke(obj);
            System.out.println("getInfo(): " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Invoke private method
        try {
            Method secret = clazz.getDeclaredMethod("secretMethod");
            secret.setAccessible(true);
            String result = (String) secret.invoke(obj);
            System.out.println("secretMethod(): " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Invoke static method
        try {
            Method countMethod = clazz.getMethod("getInstanceCount");
            int count = (int) countMethod.invoke(null);
            System.out.println("getInstanceCount(): " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Invoke method with parameters
        try {
            Method isOlder = clazz.getMethod("isOlderThan", int.class);
            boolean result = (boolean) isOlder.invoke(obj, 30);
            System.out.println("isOlderThan(30): " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves and displays all declared constructors (including private ones).
     */
    public static List<Constructor<?>> getDeclaredConstructors(Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredConstructors());
    }

    /**
     * Demonstrates creating instances dynamically using constructors.
     */
    public static void demonstrateDynamicInstantiation() {
        System.out.println("\n=== Dynamic Instantiation ===");
        Class<?> clazz = ReflectionBasics.class;

        // Create using public constructor
        try {
            Constructor<?> ctor = clazz.getConstructor(String.class, int.class, String.class);
            Object instance = ctor.newInstance("Dynamic", 40, "DYN001");
            System.out.println("Created via public constructor: " + ((ReflectionBasics) instance).getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create using private constructor
        try {
            Constructor<?> privateCtor = clazz.getDeclaredConstructor(String.class);
            privateCtor.setAccessible(true);
            Object instance = privateCtor.newInstance("Secret");
            System.out.println("Created via private constructor: " + ((ReflectionBasics) instance).getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create using default constructor
        try {
            Constructor<?> defaultCtor = clazz.getDeclaredConstructor();
            defaultCtor.setAccessible(true);
            Object instance = defaultCtor.newInstance();
            System.out.println("Created via default constructor: " + ((ReflectionBasics) instance).getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create using newInstance shortcut
        try {
            ReflectionBasics instance = (ReflectionBasics) clazz.getDeclaredConstructor(String.class, int.class, String.class)
                    .newInstance("Shortcut", 50, "SHT001");
            System.out.println("Created via newInstance shortcut: " + instance.getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates reading and understanding modifiers on class members.
     */
    public static void demonstrateModifiers() {
        System.out.println("\n=== Modifiers ===");
        Class<?> clazz = ReflectionBasics.class;

        // Class modifiers
        int classMods = clazz.getModifiers();
        System.out.println("Class modifiers: " + Modifier.toString(classMods));
        System.out.println("  isPublic: " + Modifier.isPublic(classMods));
        System.out.println("  isAbstract: " + Modifier.isAbstract(classMods));
        System.out.println("  isFinal: " + Modifier.isFinal(classMods));

        // Field modifiers
        try {
            Field nameField = clazz.getDeclaredField("name");
            int nameMods = nameField.getModifiers();
            System.out.println("Field 'name' modifiers: " + Modifier.toString(nameMods));

            Field ageField = clazz.getDeclaredField("age");
            int ageMods = ageField.getModifiers();
            System.out.println("Field 'age' modifiers: " + Modifier.toString(ageMods));
            System.out.println("  isPrivate: " + Modifier.isPrivate(ageMods));

            Field idField = clazz.getDeclaredField("id");
            int idMods = idField.getModifiers();
            System.out.println("Field 'id' modifiers: " + Modifier.toString(idMods));
            System.out.println("  isFinal: " + Modifier.isFinal(idMods));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        // Method modifiers
        try {
            Method getInfo = clazz.getDeclaredMethod("getInfo");
            int methodMods = getInfo.getModifiers();
            System.out.println("Method 'getInfo' modifiers: " + Modifier.toString(methodMods));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates a complete reflection workflow: introspect, modify, invoke.
     */
    public static void demonstrateCompleteWorkflow() {
        System.out.println("\n=== Complete Reflection Workflow ===");
        try {
            // 1. Get Class object
            Class<?> clazz = Class.forName("academy.javaengineering.reflection.ReflectionBasics");

            // 2. Create instance
            Constructor<?> ctor = clazz.getConstructor(String.class, int.class, String.class);
            Object instance = ctor.newInstance("Workflow", 28, "WF001");

            // 3. Read field value
            Field nameField = clazz.getDeclaredField("name");
            System.out.println("Name before: " + nameField.get(instance));

            // 4. Modify field value
            nameField.set(instance, "ModifiedWorkflow");
            System.out.println("Name after: " + nameField.get(instance));

            // 5. Invoke method
            Method getInfo = clazz.getMethod("getInfo");
            System.out.println("getInfo: " + getInfo.invoke(instance));

            // 6. List all members
            System.out.println("\nAll Fields:");
            for (Field f : clazz.getDeclaredFields()) {
                System.out.println("  " + f.getName());
            }
            System.out.println("All Methods:");
            for (Method m : clazz.getDeclaredMethods()) {
                System.out.println("  " + m.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        demonstrateClassObjectCreation();
        demonstrateFieldAccess();
        demonstrateMethodInvocation();
        demonstrateDynamicInstantiation();
        demonstrateModifiers();
        demonstrateCompleteWorkflow();
    }
}
