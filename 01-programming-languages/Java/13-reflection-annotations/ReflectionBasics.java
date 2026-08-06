package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * ReflectionBasics - Class.forName, getMethod, invoke
 *
 * Covers:
 * - Getting Class objects
 * - Accessing fields
 * - Invoking methods
 * - Creating instances
 * - Understanding modifiers
 */
public class ReflectionBasics {

    private String name;
    private int age;
    private static String staticField = "Static Value";

    public ReflectionBasics() {
        this.name = "Default";
        this.age = 0;
    }

    public ReflectionBasics(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String greet() {
        return "Hello, " + name + "!";
    }

    private String secretMethod() {
        return "This is private";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Getting Class Objects ===");
        gettingClassObjects();

        System.out.println("\n=== Accessing Fields ===");
        accessingFields();

        System.out.println("\n=== Invoking Methods ===");
        invokingMethods();

        System.out.println("\n=== Creating Instances ===");
        creatingInstances();

        System.out.println("\n=== Modifiers ===");
        modifiersDemo();
    }

    static void gettingClassObjects() throws ClassNotFoundException {
        // Method 1: Using .class literal
        Class<?> class1 = ReflectionBasics.class;
        System.out.println("Using .class: " + class1.getName());

        // Method 2: Using getClass()
        ReflectionBasics obj = new ReflectionBasics();
        Class<?> class2 = obj.getClass();
        System.out.println("Using getClass(): " + class2.getName());

        // Method 3: Using Class.forName()
        Class<?> class3 = Class.forName("reflection.ReflectionBasics");
        System.out.println("Using Class.forName(): " + class3.getName());

        // All three refer to the same Class object
        System.out.println("All same class: " +
            (class1 == class2 && class2 == class3));
    }

    static void accessingFields() throws Exception {
        ReflectionBasics obj = new ReflectionBasics("John", 25);
        Class<?> clazz = obj.getClass();

        // Get all declared fields (including private)
        System.out.println("All declared fields:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("  " + Modifier.toString(field.getModifiers()) +
                " " + field.getType().getSimpleName() + " " + field.getName());
        }

        // Get specific field
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true); // Allow access to private field

        // Read field value
        String nameValue = (String) nameField.get(obj);
        System.out.println("\nName field value: " + nameValue);

        // Write field value
        nameField.set(obj, "Jane");
        System.out.println("Name after modification: " + obj.getName());

        // Access static field
        Field staticField = clazz.getDeclaredField("staticField");
        staticField.setAccessible(true);
        String staticValue = (String) staticField.get(null); // null for static
        System.out.println("Static field value: " + staticValue);
    }

    static void invokingMethods() throws Exception {
        ReflectionBasics obj = new ReflectionBasics("John", 25);
        Class<?> clazz = obj.getClass();

        // Get all declared methods
        System.out.println("All declared methods:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  " + Modifier.toString(method.getModifiers()) +
                " " + method.getReturnType().getSimpleName() +
                " " + method.getName() + "()");
        }

        // Invoke public method
        Method getNameMethod = clazz.getDeclaredMethod("getName");
        String name = (String) getNameMethod.invoke(obj);
        System.out.println("\ngetName() result: " + name);

        // Invoke method with parameters
        Method setNameMethod = clazz.getDeclaredMethod("setName", String.class);
        setNameMethod.invoke(obj, "Jane");
        System.out.println("After setName('Jane'): " + obj.getName());

        // Invoke private method
        Method secretMethod = clazz.getDeclaredMethod("secretMethod");
        secretMethod.setAccessible(true);
        String secret = (String) secretMethod.invoke(obj);
        System.out.println("Private method result: " + secret);
    }

    static void creatingInstances() throws Exception {
        Class<?> clazz = Class.forName("reflection.ReflectionBasics");

        // Method 1: Using default constructor
        Object obj1 = clazz.getDeclaredConstructor().newInstance();
        System.out.println("Default constructor: " + obj1);

        // Method 2: Using parameterized constructor
        Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, int.class);
        Object obj2 = constructor.newInstance("John", 25);
        System.out.println("Parameterized constructor: " + obj2);

        // Get all constructors
        System.out.println("\nAll constructors:");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> c : constructors) {
            System.out.println("  " + Modifier.toString(c.getModifiers()) +
                " " + c.getName() + "(" +
                java.util.Arrays.toString(c.getParameterTypes()) + ")");
        }
    }

    static void modifiersDemo() {
        Class<?> clazz = ReflectionBasics.class;

        int classModifiers = clazz.getModifiers();
        System.out.println("Class modifiers:");
        System.out.println("  Is public: " + Modifier.isPublic(classModifiers));
        System.out.println("  Is abstract: " + Modifier.isAbstract(classModifiers));
        System.out.println("  Is final: " + Modifier.isFinal(classModifiers));

        try {
            Field nameField = clazz.getDeclaredField("name");
            int fieldModifiers = nameField.getModifiers();
            System.out.println("\nField 'name' modifiers:");
            System.out.println("  Is private: " + Modifier.isPrivate(fieldModifiers));
            System.out.println("  Is static: " + Modifier.isStatic(fieldModifiers));
            System.out.println("  Is final: " + Modifier.isFinal(fieldModifiers));

            Method greetMethod = clazz.getDeclaredMethod("greet");
            int methodModifiers = greetMethod.getModifiers();
            System.out.println("\nMethod 'greet' modifiers:");
            System.out.println("  Is public: " + Modifier.isPublic(methodModifiers));
            System.out.println("  Is static: " + Modifier.isStatic(methodModifiers));
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}