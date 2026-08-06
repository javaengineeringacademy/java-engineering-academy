package academy.javaengineering.exercises;

import java.lang.reflect.*;
import java.util.*;

/**
 * Exercises: Class Loading and Reflection
 *
 * Complete the TODO sections below.
 */
public class ClassLoadingExercises {

    // TODO 1: Get all public methods of a class (including inherited)
    public List<String> getPublicMethods(Class<?> clazz) {
        // TODO: implement using getDeclaredMethods and getMethods
        return new ArrayList<>();
    }

    // TODO 2: Create an instance of a class by its no-arg constructor
    // Throw RuntimeException if creation fails
    public <T> T createInstance(Class<T> clazz) {
        // TODO: implement using getDeclaredConstructor().newInstance()
        return null;
    }

    // TODO 3: Set a private field value using reflection
    public void setPrivateField(Object obj, String fieldName, Object value) throws Exception {
        // TODO: implement using getDeclaredField, setAccessible, set
    }

    // TODO 4: Invoke a private method using reflection
    public Object invokePrivateMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] args) throws Exception {
        // TODO: implement using getDeclaredMethod, setAccessible, invoke
        return null;
    }

    // TODO 5: Get all fields annotated with a specific annotation
    public List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends java.lang.annotation.Annotation> annotation) {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 6: Implement a simple dependency injector
    // Scan fields annotated with @Inject and set them from the provided map
    public void injectDependencies(Object obj, Map<Class<?>, Object> providers) throws Exception {
        // TODO: implement
    }

    // Simple @Inject annotation for testing
    @Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @Target(java.lang.annotation.ElementType.FIELD)
    public @interface Inject {}

    // ==================== TEST METHODS ====================

    public static class TestBean {
        private String name;
        private int value;

        public TestBean() {}

        public TestBean(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        private String secretMethod() { return "secret"; }
        private void setValue(int v) { this.value = v; }
        public int getValue() { return value; }
    }

    public static class ServiceA {
        @Inject
        private ServiceB dependency;
        public ServiceB getDependency() { return dependency; }
    }

    public static class ServiceB {}

    public static void main(String[] args) {
        ClassLoadingExercises exercises = new ClassLoadingExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ClassLoadingExercises Tests ===\n");

        // Test 1
        total++;
        List<String> methods = exercises.getPublicMethods(TestBean.class);
        if (methods.contains("getName") && methods.contains("getValue")) {
            System.out.println("Test 1 PASSED: getPublicMethods");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: getPublicMethods - " + methods);
        }

        // Test 2
        total++;
        try {
            TestBean bean = exercises.createInstance(TestBean.class);
            if (bean != null) {
                System.out.println("Test 2 PASSED: createInstance");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: createInstance returned null");
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            TestBean bean = new TestBean("test", 0);
            exercises.setPrivateField(bean, "name", "reflected");
            if ("reflected".equals(bean.getName())) {
                System.out.println("Test 3 PASSED: setPrivateField");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: setPrivateField - name=" + bean.getName());
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            TestBean bean = new TestBean("test", 0);
            Object result = exercises.invokePrivateMethod(bean, "secretMethod", new Class[]{}, new Object[]{});
            if ("secret".equals(result)) {
                System.out.println("Test 4 PASSED: invokePrivateMethod");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: invokePrivateMethod - " + result);
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            List<Field> fields = exercises.getFieldsWithAnnotation(ServiceA.class, Inject.class);
            if (fields.size() == 1 && "dependency".equals(fields.get(0).getName())) {
                System.out.println("Test 5 PASSED: getFieldsWithAnnotation");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: getFieldsWithAnnotation - " + fields.size());
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        // Test 6
        total++;
        try {
            ServiceA service = new ServiceA();
            ServiceB impl = new ServiceB();
            Map<Class<?>, Object> providers = new HashMap<>();
            providers.put(ServiceB.class, impl);
            exercises.injectDependencies(service, providers);
            if (service.getDependency() == impl) {
                System.out.println("Test 6 PASSED: injectDependencies");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: injectDependencies");
            }
        } catch (Exception e) {
            System.out.println("Test 6 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
