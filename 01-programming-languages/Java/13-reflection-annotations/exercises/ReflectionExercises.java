package academy.javaengineering.exercises;

import java.lang.reflect.*;
import java.util.*;

/**
 * Exercises: Reflection (getDeclaredField, invoke, etc.)
 *
 * Complete the TODO sections below.
 */
public class ReflectionExercises {

    // TODO 1: Implement a deep equals using reflection
    // Compare all declared fields of two objects
    public boolean deepEquals(Object a, Object b) throws IllegalAccessException {
        // TODO: implement
        return false;
    }

    // TODO 2: Create a copy of an object using reflection
    // Copy all field values from source to a new instance
    @SuppressWarnings("unchecked")
    public <T> T deepCopy(T source) throws Exception {
        // TODO: implement
        return null;
    }

    // TODO 3: Implement a toString method using reflection
    // Format: ClassName(field1=value1, field2=value2, ...)
    public String reflectiveToString(Object obj) throws IllegalAccessException {
        // TODO: implement
        return "";
    }

    // TODO 4: Find all fields of a class and its superclasses
    public List<Field> getAllFields(Class<?> clazz) {
        // TODO: implement - walk up the class hierarchy
        return new ArrayList<>();
    }

    // TODO 5: Invoke a static method by name
    public Object invokeStaticMethod(Class<?> clazz, String methodName,
                                      Class<?>[] paramTypes, Object[] args) throws Exception {
        // TODO: implement
        return null;
    }

    // TODO 6: Create a new instance and set fields from a Map
    public <T> T createAndPopulate(Class<T> clazz, Map<String, Object> fieldValues) throws Exception {
        // TODO: implement
        return null;
    }

    // Test classes
    public static class Person {
        private String name;
        private int age;

        public Person() {}

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
    }

    public static class Employee extends Person {
        private String department;

        public Employee() {}

        public Employee(String name, int age, String department) {
            super(name, age);
            this.department = department;
        }

        public String getDepartment() { return department; }
    }

    public static class MathUtils {
        public static int add(int a, int b) { return a + b; }
        public static String concat(String a, String b) { return a + b; }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        ReflectionExercises exercises = new ReflectionExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ReflectionExercises Tests ===\n");

        // Test 1
        total++;
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Alice", 30);
        Person p3 = new Person("Bob", 25);
        if (exercises.deepEquals(p1, p2) && !exercises.deepEquals(p1, p3)) {
            System.out.println("Test 1 PASSED: deepEquals");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: deepEquals");
        }

        // Test 2
        total++;
        Person original = new Person("Charlie", 35);
        Person copy = exercises.deepCopy(original);
        if (copy != null && copy != original
            && "Charlie".equals(copy.getName()) && copy.getAge() == 35) {
            System.out.println("Test 2 PASSED: deepCopy");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: deepCopy");
        }

        // Test 3
        total++;
        Person person = new Person("Dave", 40);
        String str = exercises.reflectiveToString(person);
        if (str.contains("name=Dave") && str.contains("age=40")) {
            System.out.println("Test 3 PASSED: reflectiveToString");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: reflectiveToString - " + str);
        }

        // Test 4
        total++;
        List<Field> fields = exercises.getAllFields(Employee.class);
        Set<String> fieldNames = new HashSet<>();
        for (Field f : fields) fieldNames.add(f.getName());
        if (fieldNames.contains("name") && fieldNames.contains("age") && fieldNames.contains("department")) {
            System.out.println("Test 4 PASSED: getAllFields");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: getAllFields - " + fieldNames);
        }

        // Test 5
        total++;
        Object result = exercises.invokeStaticMethod(MathUtils.class, "add",
            new Class[]{int.class, int.class}, new Object[]{3, 4});
        if (result != null && ((Integer) result) == 7) {
            System.out.println("Test 5 PASSED: invokeStaticMethod");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: invokeStaticMethod");
        }

        // Test 6
        total++;
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Eve");
        values.put("age", 28);
        Person created = exercises.createAndPopulate(Person.class, values);
        if (created != null && "Eve".equals(created.getName()) && created.getAge() == 28) {
            System.out.println("Test 6 PASSED: createAndPopulate");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: createAndPopulate");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
