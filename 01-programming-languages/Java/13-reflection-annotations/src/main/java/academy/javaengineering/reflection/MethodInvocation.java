package academy.javaengineering.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates method invocation via reflection including:
 * - Dynamic method invocation with Method.invoke()
 * - MethodHandles and MethodType
 * - Method signature matching
 * - VarHandle usage
 * - Method overload resolution
 */
public class MethodInvocation {

    // Instance methods
    public String greet(String name) {
        return "Hello, " + name + "!";
    }

    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public String format(Object... args) {
        return Arrays.stream(args)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    // Private method
    private String internalProcess(String input) {
        return "Processed: " + input.toUpperCase();
    }

    // Static method
    public static int staticMethod(int x) {
        return x * x;
    }

    // Overloaded methods
    public String process(String input) {
        return "string:" + input;
    }

    public String process(int input) {
        return "int:" + input;
    }

    public String process(String input, int count) {
        return "string:" + input + "x" + count;
    }

    // Method with varargs
    public int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }

    /**
     * Demonstrates basic method invocation via reflection.
     */
    public static void demonstrateBasicInvocation() {
        System.out.println("=== Basic Method Invocation ===");
        MethodInvocation obj = new MethodInvocation();
        Class<?> clazz = obj.getClass();

        try {
            // Invoke instance method
            Method greetMethod = clazz.getMethod("greet", String.class);
            Object result = greetMethod.invoke(obj, "World");
            System.out.println("greet(\"World\"): " + result);

            // Invoke overloaded method
            Method addInt = clazz.getMethod("add", int.class, int.class);
            System.out.println("add(3, 4): " + addInt.invoke(obj, 3, 4));

            Method addDouble = clazz.getMethod("add", double.class, double.class);
            System.out.println("add(2.5, 3.5): " + addDouble.invoke(obj, 2.5, 3.5));

            // Invoke varargs method
            Method formatMethod = clazz.getMethod("format", Object[].class);
            System.out.println("format(\"a\", \"b\", \"c\"): " +
                    formatMethod.invoke(obj, (Object) new Object[]{"a", "b", "c"}));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.println("Method invocation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates invoking private methods.
     */
    public static void demonstratePrivateMethodInvocation() {
        System.out.println("\n=== Private Method Invocation ===");
        MethodInvocation obj = new MethodInvocation();
        Class<?> clazz = obj.getClass();

        try {
            Method privateMethod = clazz.getDeclaredMethod("internalProcess", String.class);
            privateMethod.setAccessible(true);
            Object result = privateMethod.invoke(obj, "hello");
            System.out.println("internalProcess(\"hello\"): " + result);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.println("Private method invocation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates invoking static methods.
     */
    public static void demonstrateStaticMethodInvocation() {
        System.out.println("\n=== Static Method Invocation ===");
        try {
            Method staticMethod = MethodInvocation.class.getMethod("staticMethod", int.class);
            // Pass null for the instance when invoking static methods
            Object result = staticMethod.invoke(null, 5);
            System.out.println("staticMethod(5): " + result);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.println("Static method invocation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates resolving and invoking overloaded methods.
     */
    public static void demonstrateOverloadResolution() {
        System.out.println("\n=== Overload Resolution ===");
        MethodInvocation obj = new MethodInvocation();
        Class<?> clazz = obj.getClass();

        try {
            // Find specific overloads
            Method stringProcess = clazz.getMethod("process", String.class);
            Method intProcess = clazz.getMethod("process", int.class);
            Method stringIntProcess = clazz.getMethod("process", String.class, int.class);

            System.out.println("process(\"test\"): " + stringProcess.invoke(obj, "test"));
            System.out.println("process(42): " + intProcess.invoke(obj, 42));
            System.out.println("process(\"test\", 3): " + stringIntProcess.invoke(obj, "test", 3));

            // List all process methods
            System.out.println("\nAll 'process' overloads:");
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.getName().equals("process")) {
                    System.out.println("  " + m);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.println("Overload resolution error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates MethodHandle usage.
     */
    public static void demonstrateMethodHandles() {
        System.out.println("\n=== MethodHandles ===");
        MethodInvocation obj = new MethodInvocation();
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            // Create MethodHandle for instance method
            MethodType greetType = MethodType.methodType(String.class, String.class);
            MethodHandle greetHandle = lookup.findVirtual(MethodInvocation.class, "greet", greetType);
            String result = (String) greetHandle.invoke(obj, "Handle");
            System.out.println("greet via MethodHandle: " + result);

            // Create MethodHandle for static method
            MethodType staticType = MethodType.methodType(int.class, int.class);
            MethodHandle staticHandle = lookup.findStatic(MethodInvocation.class, "staticMethod", staticType);
            int staticResult = (int) staticHandle.invoke(7);
            System.out.println("staticMethod via MethodHandle: " + staticResult);

            // Create MethodHandle for private method
            MethodType privateType = MethodType.methodType(String.class, String.class);
            MethodHandle privateHandle = lookup.findVirtual(MethodInvocation.class, "internalProcess", privateType);
            String privateResult = (String) privateHandle.invoke(obj, "methodhandle");
            System.out.println("internalProcess via MethodHandle: " + privateResult);
        } catch (NoSuchMethodException | IllegalAccessException | Throwable e) {
            System.err.println("MethodHandle error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates MethodType creation and manipulation.
     */
    public static void demonstrateMethodType() {
        System.out.println("\n=== MethodType ===");

        // Create MethodTypes
        MethodType voidType = MethodType.methodType(void.class);
        MethodType stringType = MethodType.methodType(String.class);
        MethodType addType = MethodType.methodType(int.class, int.class, int.class);
        MethodType varargsType = MethodType.methodType(int.class, int[].class);

        System.out.println("void type: " + voidType);
        System.out.println("string type: " + stringType);
        System.out.println("add type: " + addType);
        System.out.println("varargs type: " + varargsType);

        // Manipulate MethodTypes
        MethodType extended = addType.appendParameterTypes(String.class);
        System.out.println("add type with extra param: " + extended);

        MethodType reduced = MethodType.methodType(int.class, int.class);
        System.out.println("reduced type: " + reduced);

        // Get return type
        System.out.println("add return type: " + addType.returnType());
        System.out.println("add parameter count: " + addType.parameterCount());
        System.out.println("add parameter types: " + addType.parameterList());
    }

    /**
     * Demonstrates finding methods by various criteria.
     */
    public static void demonstrateMethodSearch() {
        System.out.println("\n=== Method Search ===");
        Class<?> clazz = MethodInvocation.class;

        // Find all methods with a specific name
        System.out.println("Methods named 'process':");
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals("process"))
                .forEach(m -> System.out.println("  " + m));

        // Find methods by return type
        System.out.println("\nMethods returning String:");
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getReturnType() == String.class)
                .forEach(m -> System.out.println("  " + m));

        // Find methods with specific parameter types
        System.out.println("\nMethods with (int, int) params:");
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getParameterCount() == 2
                        && m.getParameterTypes()[0] == int.class
                        && m.getParameterTypes()[1] == int.class)
                .forEach(m -> System.out.println("  " + m));
    }

    /**
     * Demonstrates invoking methods with dynamic argument types.
     */
    public static void demonstrateDynamicInvocation() {
        System.out.println("\n=== Dynamic Invocation ===");
        MethodInvocation obj = new MethodInvocation();

        // Dynamically find and invoke a method based on runtime types
        Object[] args = {"Dynamic", 2};
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
            if (argTypes[i] == Integer.class) argTypes[i] = int.class;
        }

        try {
            Method method = obj.getClass().getMethod("process", argTypes);
            Object result = method.invoke(obj, args);
            System.out.println("Dynamic invocation result: " + result);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.println("Dynamic invocation error: " + e.getMessage());
        }

        // Invoke varargs method dynamically
        try {
            Method sumMethod = obj.getClass().getMethod("sum", int[].class);
            Object result = sumMethod.invoke(obj, (Object) new int[]{1, 2, 3, 4, 5});
            System.out.println("sum(1,2,3,4,5): " + result);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            System.err.println("Varargs invocation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates creating method invoker utilities.
     */
    public static void demonstrateInvokerUtilities() {
        System.out.println("\n=== Invoker Utilities ===");
        MethodInvocation obj = new MethodInvocation();

        // Generic invoker
        Invoker invoker = new Invoker(obj);
        Object result = invoker.invoke("greet", "Utility");
        System.out.println("invoke(\"greet\", \"Utility\"): " + result);

        Object sumResult = invoker.invoke("sum", (Object) new int[]{10, 20, 30});
        System.out.println("invoke(\"sum\", {10,20,30}): " + sumResult);
    }

    // Utility class for generic method invocation
    static class Invoker {
        private final Object target;

        Invoker(Object target) {
            this.target = target;
        }

        public Object invoke(String methodName, Object... args) {
            try {
                Class<?>[] paramTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i].getClass();
                    // Handle primitive types
                    if (paramTypes[i] == Integer.class) paramTypes[i] = int.class;
                    else if (paramTypes[i] == Long.class) paramTypes[i] = long.class;
                    else if (paramTypes[i] == Double.class) paramTypes[i] = double.class;
                    else if (paramTypes[i] == Boolean.class) paramTypes[i] = boolean.class;
                }

                Method method = target.getClass().getMethod(methodName, paramTypes);
                return method.invoke(target, args);
            } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                throw new RuntimeException("Failed to invoke " + methodName, e);
            }
        }
    }

    public static void main(String[] args) {
        demonstrateBasicInvocation();
        demonstratePrivateMethodInvocation();
        demonstrateStaticMethodInvocation();
        demonstrateOverloadResolution();
        demonstrateMethodHandles();
        demonstrateMethodType();
        demonstrateMethodSearch();
        demonstrateDynamicInvocation();
        demonstrateInvokerUtilities();
    }
}
